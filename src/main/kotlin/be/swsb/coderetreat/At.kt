package be.swsb.coderetreat

data class At(val x: Int, val y: Int) {
    operator fun rangeTo(other: At): List<At> {
        return when {
            this.y == other.y -> (this.x..other.x).map { At(it, this.y) }
            this.x == other.x -> (this.y..other.y).map { At(this.x, it) }
            else -> emptyList()
        }
    }

    companion object {
        operator fun invoke(string: String): At {
            val col = string.first()
            val row = string.drop(1)
            val x = ('A'..'J').zip(0..9).toMap()[col] ?: error("Column $col out of bounds")
            return At(x, row.toInt() - 1)
        }
    }
}