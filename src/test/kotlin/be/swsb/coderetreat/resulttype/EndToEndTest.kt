package be.swsb.coderetreat.resulttype

import be.swsb.coderetreat.resulttype.TimeOutFrequency.Always
import be.swsb.coderetreat.resulttype.TimeOutFrequency.Never
import be.swsb.coderetreat.resulttype.domain.*
import be.swsb.coderetreat.resulttype.rest.ErrorReporter
import be.swsb.coderetreat.resulttype.rest.ResponseEntity
import be.swsb.coderetreat.resulttype.rest.TryAgain
import be.swsb.coderetreat.resulttype.service.Library
import be.swsb.coderetreat.resulttype.service.LoanTracker
import be.swsb.coderetreat.resulttype.service.LoanTrackerTimedOutException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.random.Random


class EndToEndTest {

    private val availableBooks: MutableList<Book> = mutableListOf()
    private val reliableTracker: LoanTracker = LoanTrackerForTests(emptySet(), timeOutFrequency = Never)
    private val unreliableTracker: LoanTracker = LoanTrackerForTests(emptySet(), timeOutFrequency = Always)
    private val queryableErrorReporter = QueryableErrorReporter()

    private fun bookResource(loanTracker: LoanTracker) = be.swsb.coderetreat.resulttype.rest.BookResource(
        library = Library(BookRepository(database = availableBooks), loanedBooksTracker = loanTracker),
        errorReporter = queryableErrorReporter
    )

    @Test
    fun `An available book can be loaned`() {
        val expectedBook = aBook("123", "Thundercats", "Lion-O")
        availableBooks += expectedBook

        val actual = bookResource(reliableTracker).loanBookByISBN("123")
        
        assertThat(actual).isEqualTo(ResponseEntity(expectedBook))
        assertThat(reliableTracker.isLoaned(ISBN("123"))).isTrue
    }

    @Test
    fun `An evil book cannot be loaned`() {
        val expectedBook = aBook("123", "Thundercats", "Lion-O")
        availableBooks += expectedBook

        val actual = bookResource(reliableTracker).loanBookByISBN("978-0321556059")
        
        assertThat(actual).isEqualTo(ResponseEntity(Library.BookRetrievalFailure(ISBN("978-0321556059"))))
        assertThat(reliableTracker.isLoaned(ISBN("123"))).isFalse
        queryableErrorReporter.assertLogsContain(Library.BookRetrievalFailure(ISBN("978-0321556059")).message)
    }

    @Test
    fun `An already loaned book cannot be loaned again, because we only have one copy of each book, or some other silly reason`() {
        val expectedBook = aBook("123", "Thundercats", "Lion-O")
        availableBooks += expectedBook

        val bookResource = bookResource(reliableTracker)
        
        val firstLoanAttempt = bookResource.loanBookByISBN("123")
        assertThat(firstLoanAttempt).isEqualTo(ResponseEntity(expectedBook))
        assertThat(reliableTracker.isLoaned(ISBN("123"))).isTrue
        
        val secondAttempt = bookResource.loanBookByISBN("123")
        assertThat(secondAttempt).isEqualTo(ResponseEntity(Library.AlreadyLoaned(ISBN("123"))))
        assertThat(reliableTracker.isLoaned(ISBN("123"))).isTrue
        queryableErrorReporter.assertLogsContain(Library.AlreadyLoaned(ISBN("123")).message)
    }

    @Test
    fun `We need to be able to deal with LoanTracker timeouts`() {
        availableBooks += aBook("123", "Thundercats", "Lion-O")

        val actual = bookResource(unreliableTracker).loanBookByISBN("123")
        assertThat(actual).isEqualTo(ResponseEntity(TryAgain))
        queryableErrorReporter.assertLogsContain(TryAgain.message)
    }
}



private class LoanTrackerForTests(previouslyLoanedBooks: Set<ISBN>, private val timeOutFrequency: TimeOutFrequency) : LoanTracker {
    private val loanedBooks: MutableSet<ISBN> = previouslyLoanedBooks.toMutableSet()

    override fun isLoaned(isbn: ISBN): Boolean = possiblyThrowsRuntime(timeOutFrequency) {
        isbn in loanedBooks
    }

    override fun loan(isbn: ISBN): Unit = possiblyThrowsRuntime(timeOutFrequency) {
        loanedBooks += isbn
    }
}

private fun <T> possiblyThrowsRuntime(frequency: TimeOutFrequency, block: () -> T): T = when (frequency) {
    is Never -> block()
    is Always -> throw LoanTrackerTimedOutException()
    is TimeOutFrequency.Sometimes -> {
        if (Random.nextInt(frequency.chance, 10) == 10) throw LoanTrackerTimedOutException()
        else block()
    }
}

private sealed interface TimeOutFrequency {
    object Never : TimeOutFrequency
    object Always : TimeOutFrequency
    data class Sometimes(val chance: Int) : TimeOutFrequency {
        init {
            require(chance in 1..9) { "Chance $chance should be between 1 or 9, maybe use `Never` or `Always` instead?" }
        }
    }
}

private fun aBook(
    isbn: String = "27690",
    title: String = "Olfgar goes punch",
    author: String = "MeeMaw",
) = Book(ISBN(isbn), Title(title), Author(author))

private class QueryableErrorReporter: ErrorReporter {
    private val log: MutableList<String> = mutableListOf()
    
    override fun logError(message: String) {
        log += message
    }

    fun assertLogsContain(message: String) {
        assertThat(log).contains(message)
    }

}