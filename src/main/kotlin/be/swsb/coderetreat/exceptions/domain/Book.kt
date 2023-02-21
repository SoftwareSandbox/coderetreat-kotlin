package be.swsb.coderetreat.exceptions.domain

data class Book(
    val isbn: ISBN,
    val title: Title,
    val author: Author,
)

@JvmInline value class ISBN(val value: String)
@JvmInline value class Title(val value: String)
@JvmInline value class Author(val value: String)
