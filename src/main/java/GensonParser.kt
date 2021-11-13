import com.owlike.genson.Genson
import dto.CategoryDto

class GensonParser {
    val genson = Genson()
    fun parserCategories(str: String): List<CategoryDto> {
        println("parserCategories")
        var list = ArrayList<CategoryDto>()
        val category: MutableList<CategoryDto>? = genson.deserialize(str, MutableList::class.java) as MutableList<CategoryDto>?
        println(list)
        return list
    }
    fun parserCategory(str: String): CategoryDto {
        println("parserCategory")
        println(str)
        val category: CategoryDto = genson.deserialize(str, CategoryDto::class.java)
        println(category)
        return category
    }
}