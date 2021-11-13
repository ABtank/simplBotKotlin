import dto.CategoryDto
import org.json.JSONObject
import org.json.simple.parser.JSONParser
import java.io.BufferedReader

class JsonSimplParser {

    fun parserCategory(str: String): List<CategoryDto> {
        println("parserCategory")
        var list = ArrayList<CategoryDto>()
        val parser = JSONParser()
        var jsonObj = parser.parse(str) as JSONObject
        println(" jsonObj.isEmpty ${jsonObj.isEmpty}")

        return list
    }

}