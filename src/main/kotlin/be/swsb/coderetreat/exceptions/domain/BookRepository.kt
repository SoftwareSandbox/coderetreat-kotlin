package be.swsb.coderetreat.exceptions.domain

interface BookRepository {
    fun findByIsbn(isbn: ISBN): Book?
    fun save(newlyAcquiredBook: Book): Book

    class WeDontDoThatHereException : Exception()
}
