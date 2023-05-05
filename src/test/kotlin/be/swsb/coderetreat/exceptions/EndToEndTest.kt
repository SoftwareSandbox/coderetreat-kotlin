package be.swsb.coderetreat.exceptions

import be.swsb.coderetreat.exceptions.TimeOutFrequency.Always
import be.swsb.coderetreat.exceptions.TimeOutFrequency.Never
import be.swsb.coderetreat.exceptions.db.BookDAO
import be.swsb.coderetreat.exceptions.db.Database
import be.swsb.coderetreat.exceptions.db.asRecord
import be.swsb.coderetreat.exceptions.domain.ISBN
import be.swsb.coderetreat.exceptions.rest.*
import be.swsb.coderetreat.exceptions.service.Library
import be.swsb.coderetreat.exceptions.service.LoanTracker
import be.swsb.coderetreat.exceptions.service.LoanTrackerTimedOutException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.random.Random


class EndToEndTest {

    private val availableBooks: MutableList<Database.BookRecord> = mutableListOf()
    private val reliableTracker: LoanTracker = LoanTrackerForTests(emptySet(), timeOutFrequency = Never)
    private val unreliableTracker: LoanTracker = LoanTrackerForTests(emptySet(), timeOutFrequency = Always)
    private val queryableErrorReporter = QueryableErrorReporter()

    private fun bookResource(loanTracker: LoanTracker) = BookResource(
        library = Library(BookDAO(database = Database(availableBooks)), loanedBooksTracker = loanTracker),
        errorReporter = queryableErrorReporter
    )

    @Test
    fun `An available book can be loaned`() {
        val expectedBook = BookTestBuilder.aBook(isbn = "123", title = "Thundercats", author = "Lion-O")
        availableBooks += expectedBook.asRecord()

        val actual = bookResource(reliableTracker).loanBookByISBN("123")

        assertThat(actual).usingRecursiveComparison().isEqualTo(ResponseEntity(expectedBook))
        assertThat(reliableTracker.isLoaned(ISBN("123"))).isTrue
    }

    @Test
    fun `An evil book cannot be loaned`() {
        val expectedBook = BookTestBuilder.aBook(isbn = "123", title = "Thundercats", author = "Lion-O")
        availableBooks += expectedBook.asRecord()

        val actual = bookResource(reliableTracker).loanBookByISBN("978-0321556059")

        assertThat(actual).isEqualTo(ResponseEntity(EvilBooksCannotBeLoaned))
        assertThat(reliableTracker.isLoaned(ISBN("123"))).isFalse
        queryableErrorReporter.assertLogsContain(EvilBooksCannotBeLoaned.message)
    }

    @Test
    fun `An already loaned book cannot be loaned again, because we only have one copy of each book, or some other silly reason`() {
        val expectedBook = BookTestBuilder.aBook(isbn = "123", title = "Thundercats", author = "Lion-O")
        availableBooks += expectedBook.asRecord()

        val bookResource = bookResource(reliableTracker)

        val firstLoanAttempt = bookResource.loanBookByISBN("123")
        assertThat(firstLoanAttempt).usingRecursiveComparison().isEqualTo(ResponseEntity(expectedBook))
        assertThat(reliableTracker.isLoaned(ISBN("123"))).isTrue

        val secondAttempt = bookResource.loanBookByISBN("123")
        assertThat(secondAttempt).usingRecursiveComparison().isEqualTo(ResponseEntity(AlreadyLoaned("123")))
        assertThat(reliableTracker.isLoaned(ISBN("123"))).isTrue
        queryableErrorReporter.assertLogsContain(AlreadyLoaned("123").message)
    }

    @Test
    fun `We need to be able to deal with LoanTracker timeouts`() {
        availableBooks += BookTestBuilder.aBook(isbn = "123", title = "Thundercats", author = "Lion-O").asRecord()

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

private class QueryableErrorReporter: ErrorReporter {
    private val log: MutableList<String> = mutableListOf()
    
    override fun logError(message: String) {
        log += message
    }

    fun assertLogsContain(message: String) {
        assertThat(log).contains(message)
    }

}