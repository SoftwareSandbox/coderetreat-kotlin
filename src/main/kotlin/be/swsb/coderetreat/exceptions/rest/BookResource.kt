package be.swsb.coderetreat.exceptions.rest

import be.swsb.coderetreat.exceptions.domain.BookRepository
import be.swsb.coderetreat.exceptions.domain.ISBN
import be.swsb.coderetreat.exceptions.service.Library
import be.swsb.coderetreat.exceptions.service.LoanTrackerTimedOutException
import java.util.logging.Level
import java.util.logging.Logger

class BookResource(
    private val library: Library,
    private val errorReporter: ErrorReporter = LoggingErrorReporter(),
) {

    fun loanBookByISBN(isbn: String): ResponseEntity<*> {
        return try {
            library.tryToLoan(ISBN(isbn))
        } catch (e: BookRepository.WeDontDoThatHereException) {
            EvilBooksCannotBeLoaned
        } catch (e: Library.LibraryException) {
            if (e.contains("already loaned")) AlreadyLoaned(isbn)
            else SomethingWentWrong(e.message)
        } catch (e: LoanTrackerTimedOutException) {
            TryAgain
        }.asResponse()
    }

    private fun Any.asResponse() = when (this) {
        is BookResourceError -> ResponseEntity(this.also { errorReporter.logError(it.message) })
        else -> ResponseEntity(this)
    }

    //Unfortunate Exception API 😢
    private fun Library.LibraryException.contains(partOfMessage: String): Boolean =
        if (message == null) false
        else partOfMessage in message!!
}

data class ResponseEntity<T>(val value: T)

interface BookResourceError {
    val message: String
}

object EvilBooksCannotBeLoaned : BookResourceError {
    override val message: String = "Evil books cannot be loaned"
}

data class AlreadyLoaned(private val isbn: String) : BookResourceError {
    override val message: String = "Book with $isbn is already loaned, sorry."
}

object TryAgain : BookResourceError {
    override val message: String = """We couldn't absolutely confirm your book was registered as "loaned", please try again."""
}

//Unfortunate Exception API doesn't allow us to pass a default 😢
data class SomethingWentWrong(private val _message: String?) : BookResourceError {
    override val message: String = _message ?: "Something unexpected went wrong."
}

interface ErrorReporter {
    fun logError(message: String) : Unit
}

class LoggingErrorReporter: ErrorReporter {
    private val logger: Logger = Logger.getLogger(BookResource::class.qualifiedName)
    
    override fun logError(message: String) : Unit {
        logger.log(Level.SEVERE, message)
    }
}