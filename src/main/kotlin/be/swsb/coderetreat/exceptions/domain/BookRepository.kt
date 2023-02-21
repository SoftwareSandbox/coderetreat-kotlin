package be.swsb.coderetreat.exceptions.domain

class BookRepository(
    private val database: List<Book> = listOf(
        Book(ISBN("978-1492082279"), Title("Java to Kotlin: A Refactoring Guidebook"), Author("Duncan McGregor, Nat Pryce")),
        Book(ISBN("978-0321556059"), Title("Programmer's Guide to Java SCJP Certification, A: A Comprehensive Primer"), Author("Khalid Mughal, Rolf Rasmussen"))
    )
) {

    fun findByIsbn(isbn: ISBN): Book? {
        return if (isbn.isEvil()) throw WeDontDoThatHereException()
        else database.firstOrNull { book -> book.isbn == isbn }
    }

    class WeDontDoThatHereException : Exception()
}


private fun ISBN.isEvil() = this == ISBN("978-0321556059")
