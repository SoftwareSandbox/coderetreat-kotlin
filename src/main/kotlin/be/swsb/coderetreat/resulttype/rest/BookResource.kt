package be.swsb.coderetreat.resulttype.rest

import be.swsb.coderetreat.resulttype.domain.ISBN
import be.swsb.coderetreat.resulttype.service.Library
import be.swsb.coderetreat.resulttype.service.LoanTrackerTimedOutException
import dev.forkhandles.result4k.*
import java.util.logging.Level
import java.util.logging.Logger

class BookResource(
    private val library: Library,
    private val errorReporter: ErrorReporter = LoggingErrorReporter(),
) {

    fun loanBookByISBN(isbn: String): ResponseEntity<*> {
        return resultFrom { //We chose to deal with LoanTracker failures here for some reason
            library.tryToLoan(ISBN(isbn))
        }
        .mapFailure { e -> //All of a sudden we have to deal with different kinds of Exceptions, other than LoanTrackerTimedOutExceptions
            when (e) {
                is LoanTrackerTimedOutException -> TryAgain
                else -> UncaughtError.also { errorReporter.logError(e.stackTraceToString()) }
            }
        }
        .flatMap { it } //since library.tryToLoan returns a Result<>, resultFrom's return type is a Result<Result<S,F>, F>, so we first flatMap the inner Result<Book,F> to just a Book 
        .map { book -> ResponseEntity(book) }
        .recover { libraryFailure -> ResponseEntity(libraryFailure.also { errorReporter.logError(it.message) }) }
    }


    //No need for extension functions to "fix" the broken Exception API, we already have proper failure types
//    private fun Library.LibraryException.contains(partOfMessage: String): Boolean =
//        if (message == null) false
//        else partOfMessage in message!!

}


interface BookResourceError : Library.LibraryFailure {
    override val message: String
}

object TryAgain : BookResourceError {
    override val message: String = """We couldn't absolutely confirm your book was registered as "loaned", please try again."""
}

object UncaughtError : BookResourceError {
    override val message: String = "Something unexpected went wrong."
}

interface ErrorReporter {
    fun logError(message: String): Unit
}

class LoggingErrorReporter : ErrorReporter {
    private val logger: Logger = Logger.getLogger(BookResource::class.qualifiedName)

    override fun logError(message: String): Unit {
        logger.log(Level.SEVERE, message)
    }
}

data class ResponseEntity<T>(val value: T)
