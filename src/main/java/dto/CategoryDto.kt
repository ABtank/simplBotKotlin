package dto

import java.util.*

class CategoryDto {
    val id: Int? = null
    val name: String? = null
    val descr: String? = null
    val creatorLogin: String? = null
    val createDate: Date? = null
    override fun toString(): String {
        return "CategoryDto(id=$id, name=$name, descr=$descr, creatorLogin=$creatorLogin, createDate=$createDate)"
    }
}