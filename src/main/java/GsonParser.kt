import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dto.CategoryDto
import java.lang.reflect.Type


class GsonParser {
    private val g = Gson()
    fun parserCategory(str: String): CategoryDto {
        println("GsonParser parserCategory")
        return g.fromJson(str, CategoryDto::class.java) as CategoryDto
    }

    fun parserCategories(str: String): List<CategoryDto> {
        println("GsonParser parserCategories")
        val listOfMyClassObject: Type = object : TypeToken<ArrayList<CategoryDto>>() {}.type
        return g.fromJson<java.util.ArrayList<CategoryDto>>(str, listOfMyClassObject)
    }

}