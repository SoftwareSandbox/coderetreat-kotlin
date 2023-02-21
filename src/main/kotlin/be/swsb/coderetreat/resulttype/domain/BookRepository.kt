package be.swsb.coderetreat.resulttype.domain

import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.Success

class BookRepository(
    private val database: List<Book> = listOf(
        Book(ISBN("978-1492082279"), Title("Java to Kotlin: A Refactoring Guidebook"), Author("Duncan McGregor, Nat Pryce")),
        Book(ISBN("978-0321556059"), Title("Programmer's Guide to Java SCJP Certification, A: A Comprehensive Primer"), Author("Khalid Mughal, Rolf Rasmussen"))
    )
) {

    fun findByIsbn(isbn: ISBN): Result<Book?, WeDontDoThatHere> {
        return if (isbn.isEvil()) Failure(WeDontDoThatHere)
        else Success(database.firstOrNull { book -> book.isbn == isbn })
    }

    object WeDontDoThatHere
}


private fun ISBN.isEvil() = this == ISBN("978-0321556059")
