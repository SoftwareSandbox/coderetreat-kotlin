package be.swsb.coderetreat.exceptions

import be.swsb.coderetreat.exceptions.domain.*

object BookTestBuilder {
    fun aBook(
        id: BookId = BookId(),
        isbn: String = "27690",
        title: String = "Olfgar goes punch",
        author: String = "MeeMaw",
    ) = Book.restore(id, ISBN(isbn), Title(title), Author(author))
}