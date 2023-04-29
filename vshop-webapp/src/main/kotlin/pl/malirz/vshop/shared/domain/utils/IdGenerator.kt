package pl.malirz.vshop.shared.domain.utils

interface IdGenerator {
    fun generate(): String
}

class UUIDIdGenerator : IdGenerator {
    override fun generate(): String {
        return java.util.UUID.randomUUID().toString()
    }
}