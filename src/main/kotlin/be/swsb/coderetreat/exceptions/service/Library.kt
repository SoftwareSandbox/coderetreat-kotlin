package be.swsb.coderetreat.exceptions.service

import be.swsb.coderetreat.exceptions.db.BookDAO
import be.swsb.coderetreat.exceptions.domain.Author
import be.swsb.coderetreat.exceptions.domain.Book
import be.swsb.coderetreat.exceptions.domain.ISBN
import be.swsb.coderetreat.exceptions.domain.Title
import kotlin.random.Random

class Library(
    private val bookRepository: BookDAO,
    private val loanedBooksTracker: LoanTracker = RealLoanTracker(emptySet()),
) {
    @Throws(LibraryException::class)
    fun tryToLoan(isbn: ISBN): Book {
        return if (loanedBooksTracker.isLoaned(isbn)) throw LibraryException("Book with $isbn is already loaned to somebody else.")
        else bookRepository.findByIsbn(isbn)
            ?.also { book -> loanedBooksTracker.loan(book.isbn) }
            ?: throw LibraryException("Book with $isbn isn't in our collection.")
    }

    @Throws(LibraryException::class)
    fun acquireNewBook(isbn: ISBN, title: Title, author: Author) : Book {
        return bookRepository.save(Book.acquireNewBook(isbn, title, author))
    }

    class LibraryException(message: String): RuntimeException(message)
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
