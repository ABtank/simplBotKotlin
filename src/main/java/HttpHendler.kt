import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class HttpHendler {

    fun printHttp(u: String?): String {
        var sb = StringBuilder()
        try {
            val url = URL(u)
            println(url)
            val hr = url.openConnection() as HttpURLConnection
            println(hr.responseCode)
            if (hr.responseCode == 200) {
                val inputStream = hr.inputStream
                val br = BufferedReader(InputStreamReader(inputStream))
                var line = br.readLine()
                while (line != null) {
                    println(line)
                    sb.append(line+"\n")
                    line = br.readLine()
                }
                br.close()
            }
        } catch (e: Exception) {
            println(e)
        }
        return sb.toString()
    }

    fun printHttps(u: String?) {
        try {
            val url = URL(u)
            println(url)
            val hr = url.openConnection() as HttpsURLConnection
            println(hr.responseCode)
            if (hr.responseCode == 200) {
                val inputStream = hr.inputStream
                val br = BufferedReader(InputStreamReader(inputStream))
                var line = br.readLine()
                while (line != null) {
                    println(line)
                    println()
                    line = br.readLine()
                }
            }
        } catch (e: Exception) {
            println(e)
        }
    }

}