package be.swsb.coderetreat.exceptions.domain

import java.util.*

class Book private constructor(
    val id: BookId,
    val isbn: ISBN,
    val title: Title,
    val author: Author,
) {
    companion object {
        fun acquireNewBook(isbn: ISBN, title: Title, author: Author) = Book(BookId(), isbn, title, author)
        fun restore(id: BookId, isbn: ISBN, title: Title, author: Author) = Book(id, isbn, title, author)
    }
}


typealias BookId = EntityId<Book>

@JvmInline
value class ISBN(val value: String)
@JvmInline
value class Title(val value: String)
@JvmInline
value class Author(val value: String)

@Suppress("unused")
data class EntityId<Entity>(
    private val value: UUID = UUID.randomUUID()
) {
    val valueAsString = value.toString()
    companion object {
        fun <Entity> fromString(s: String): EntityId<Entity> = EntityId(UUID.fromString(s))
    }
}
