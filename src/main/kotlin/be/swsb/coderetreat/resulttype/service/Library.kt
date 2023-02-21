package be.swsb.coderetreat.resulttype.service

import be.swsb.coderetreat.resulttype.domain.Book
import be.swsb.coderetreat.resulttype.domain.BookRepository
import be.swsb.coderetreat.resulttype.domain.ISBN
import dev.forkhandles.result4k.*
import kotlin.random.Random

class Library(
    private val bookRepository: BookRepository,
    private val loanedBooksTracker: LoanTracker = RealLoanTracker(emptySet()),
) {
    fun tryToLoan(isbn: ISBN): Result<Book,LibraryFailure> {
        return if (loanedBooksTracker.isLoaned(isbn)) Failure(AlreadyLoaned(isbn))
        else bookRepository.findByIsbn(isbn)
            .mapFailure { _ -> BookRetrievalFailure(isbn) } //Translating the evil isbn failure
            .flatMap { possiblyFoundBook ->
                if (possiblyFoundBook == null) Failure(BookNotFound(isbn)) 
                else Success(possiblyFoundBook.also { loanedBooksTracker.loan(possiblyFoundBook.isbn) })
            }
    }
    
    interface LibraryFailure {
        val message: String
    }
    data class AlreadyLoaned(private val isbn: ISBN) : LibraryFailure {
        override val message: String = "Book with $isbn is already loaned to somebody else."
    }
    data class BookRetrievalFailure(private val isbn: ISBN) : LibraryFailure {
        override val message: String = "There was an issue in finding a book for isbn: $isbn."
    }
    data class BookNotFound(private val isbn: ISBN) : LibraryFailure {
        override val message: String = "Book with $isbn isn't in our collection."
    }
}

interface LoanTracker {
    @Throws(LoanTrackerTimedOutException::class) fun isLoaned(isbn: ISBN): Boolean
    @Throws(LoanTrackerTimedOutException::class) fun loan(isbn: ISBN): Unit
}

//For some reason this is owned by another team, and we need to perform network operations in order to check if a book is loaned or not
//Unfortunately their service is flaky, so we need to be able to deal with time-out issues
class RealLoanTracker(previouslyLoanedBooks: Set<ISBN>) : LoanTracker {
    private val loanedBooks: MutableSet<ISBN> = previouslyLoanedBooks.toMutableSet()
    
    override fun isLoaned(isbn: ISBN): Boolean = possiblyThrowsRuntime {
        isbn in loanedBooks
    }
    
    override fun loan(isbn: ISBN): Unit = possiblyThrowsRuntime {
        loanedBooks += isbn
    }
}

class LoanTrackerTimedOutException: RuntimeException("LoanTracker timed out")
private fun <T> possiblyThrowsRuntime(block: () -> T) : T {
    return if (Random.nextInt(1,10) == 7) throw LoanTrackerTimedOutException()
    else block()
}
