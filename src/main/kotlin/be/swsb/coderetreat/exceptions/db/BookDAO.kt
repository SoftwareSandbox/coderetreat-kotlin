package be.swsb.coderetreat.exceptions.db

import be.swsb.coderetreat.exceptions.domain.*
import java.util.*

class BookDAO(
    private val database: Database = Database(),
) : BookRepository {

    override fun findByIsbn(isbn: ISBN): Book? {
        return if (isbn.isEvil()) throw BookRepository.WeDontDoThatHereException()
        else database.firstOrNull { book -> book.isbn == isbn.value }?.restoredAsBook()
    }

    override fun save(newlyAcquiredBook: Book): Book {
        database.persist(newlyAcquiredBook.asRecord())
        return newlyAcquiredBook
    }

    private fun Database.BookRecord.restoredAsBook() =
        Book.restore(
            id = BookId.fromString(id),
            isbn = ISBN(isbn),
            title = Title(title),
            author = Author(author),
            )

    private fun ISBN.isEvil() = this == ISBN("978-0321556059")
}

fun Book.asRecord(): Database.BookRecord
        = Database.BookRecord(id.valueAsString, isbn.value, title.value, author.value)


class Database(
    private val bookTable: MutableList<BookRecord> = mutableListOf(
        BookRecord(
            id = UUID.randomUUID().toString(),
            isbn = "978-1492082279",
            title = "Java to Kotlin: A Refactoring Guidebook",
            author = "Duncan McGregor, Nat Pryce",
        ),
        BookRecord(
            id = UUID.randomUUID().toString(),
            isbn = "978-0321556059",
            title = "Programmer's Guide to Java SCJP Certification, A: A Comprehensive Primer",
            author = "Khalid Mughal, Rolf Rasmussen",
        )
    )
) {
    data class BookRecord(
        val id: String,
        val isbn: String,
        val title: String,
        val author: String,
    )

    fun firstOrNull(predicate: (BookRecord) -> Boolean): BookRecord?
        = bookTable.firstOrNull(predicate)

    fun persist(bookRecord: BookRecord) {
        bookTable += bookRecord
    }

}