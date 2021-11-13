import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.GetFile
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.File
import org.telegram.telegrambots.meta.api.objects.PhotoSize
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import java.util.*


class TelegramBotApplication

fun main() {
    try {
        val botsApi = TelegramBotsApi(DefaultBotSession::class.java)
        botsApi.registerBot(Bot())
        println("Hi!")
    } catch (e: TelegramApiException) {
        e.printStackTrace()
    }
    println("Hi!")
}


class Bot() : TelegramLongPollingBot() {
    private val BOT_NAME = "test1KotlinBot"
    private val BOT_TOKEN = "2111330616:AAEJ5b5CIdNlvCe-dV9RStmnZCzx6JfVBec"
    private val API_Path = "http://localhost:8189/fitnessab"
    private val httpHendler = HttpHendler()
    private val gsonParser = GsonParser()

    //    val jsonSimplParser = JsonSimplParser()
//    val gensonParser = GensonParser()
    val listUrl = java.util.List.of("$API_Path/api/v1/type", "$API_Path/api/v1/category", "$API_Path/api/v1/role"
    )

    override fun getBotToken() = BOT_TOKEN

    override fun getBotUsername() = BOT_NAME

    // получать сообщения которые пишут боту
    override fun onUpdateReceived(update: Update) {
        val chatIdLong: Long = update.message.getChatId()

//        действие по кнопке старт
        if (update.message.text == "/auth") {
            var sb = StringBuilder()
            sb.append("OK. Отправь мне login и password. Пожалуйста используй данный формат:\n")
            sb.append("login - password")
            try {
                val message = SendMessage.builder()
                        .chatId(chatIdLong.toString())
                        .text(sb.toString())
                        .build()
                execute(message)
            } catch (e: TelegramApiException) {
                e.printStackTrace()
            }

        } else if (update.message.text == "/help") {
            var sb = StringBuilder()
            sb.append("/help - все команды\n/auth - авторизация в системе\n/cbtn - клавиатура\n/linkbtn - ссылки\n/clear - удалить клаву\n/category - все категории")
            execute(SendMessage.builder()
                    .chatId(chatIdLong.toString())
                    .text(sb.toString())
                    .build())

        } else if (update.message.text == "/category") {
            var sb = StringBuilder()
//            val categoryDto = gsonParser.parserCategory(httpHendler.printHttp("$API_Path/api/v1/category/1"))
            val categories = gsonParser.parserCategories(httpHendler.printHttp("$API_Path/api/v1/category"))
            for (o in categories) {
                sb.append("\n").append("${o.name}").append("\n")
            }

            execute(SendMessage.builder()
                    .chatId(chatIdLong.toString())
                    .text(sb.toString())
                    .build())

        } else if (update.message.text == "/clear") {
            sendClearCustomKeyboard(chatIdLong.toString())

        } else if (update.message.text == "/linkbtn") {
            sendInlineKeyboard(chatIdLong.toString())

        } else if (update.message.text == "/cbtn") {
            sendCustomKeyboard(chatIdLong.toString())

        } else if (update.hasMessage()) {
            println("hasText = ${update.message.hasText()}")
            println("hasPhoto = ${update.message.hasPhoto()}")
            if (update.message.hasText()) {
                try {
                    execute(SendMessage.builder()
                            .chatId(chatIdLong.toString())
                            .text("Че?\nЯ не расслышал.")
                            .build())

                    execute(SendMessage.builder()
                            .chatId(chatIdLong.toString())
                            .text("${update.message.text}?")
                            .build())

                    execute(SendMessage.builder()
                            .chatId(chatIdLong.toString())
                            .text("Не важно, ♥ ♥ ♥")
                            .build())
                } catch (e: TelegramApiException) {
                    e.printStackTrace()
                }
            } else if (update.message.hasPhoto()) {
                val photoSize = getPhoto(update)
                if (photoSize != null) {
                    downloadPhotoByFilePath(getFilePath(photoSize))
                }


            }
        }

    }

    fun getPhoto(update: Update): PhotoSize? {
        if (update.message.hasPhoto()) {
            val photos = update.message.photo

            return photos.stream()
                    .max(Comparator.comparing { obj: PhotoSize? -> obj!!.fileSize }).orElse(null)
        }
        // Return null if not found
        return null
    }

    fun getFilePath(photo: PhotoSize): String? {
        Objects.requireNonNull(photo)
        if (photo.filePath != null) { // If the file_path is already present, we are done!
            return photo.filePath
        } else { // If not, let find it
            // We create a GetFile method and set the file_id from the photo
            val getFileMethod = GetFile()
            getFileMethod.fileId = photo.fileId
            try {
                // We execute the method using AbsSender::execute method.
                val file: File = execute(getFileMethod)
                // We now have the file_path
                return file.getFilePath()
            } catch (e: TelegramApiException) {
                e.printStackTrace()
            }
        }
        return null // Just in case
    }

    fun downloadPhotoByFilePath(filePath: String?): java.io.File? {
        try {
            // Download the file calling AbsSender::downloadFile method
            return downloadFile(filePath)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
        return null
    }


    fun sendClearCustomKeyboard(chatId: String?) {
        val message = SendMessage()
        message.chatId = chatId!!
        message.text = "clear keyboard\n return keyboard /cbtn"
        message.replyMarkup = ReplyKeyboardRemove(true, false)
        try {
            // Send the message
            execute(message)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }

    fun sendCustomKeyboard(chatId: String?) {
        val message = SendMessage()
        message.chatId = chatId!!
        message.text = "Custom message text"

        // Create ReplyKeyboardMarkup object
        val keyboardMarkup = ReplyKeyboardMarkup()
        // Create the keyboard (list of keyboard rows)
        val keyboard: MutableList<KeyboardRow> = ArrayList()
        // Create a keyboard row
        var row = KeyboardRow()
        // Set each button, you can also use KeyboardButton objects if you need something else than text
        row.add("/auth")
        row.add("/help")
        row.add("/clear")
        // Add the first row to the keyboard
        keyboard.add(row)
        // Create another keyboard row
        row = KeyboardRow()
        // Set each button for the second line
        row.add("/linkbtn")
        row.add("/category")
        row.add("/cbtn")
        // Add the second row to the keyboard
        keyboard.add(row)
        // Set the keyboard to the markup
        keyboardMarkup.keyboard = keyboard
        // Add it to the message
        message.replyMarkup = keyboardMarkup
        try {
            // Send the message
            execute(message)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }

    fun sendInlineKeyboard(chatId: String?) {
        val message = SendMessage()
        message.chatId = chatId!!
        message.text = "Inline model below."

        // Create InlineKeyboardMarkup object
        val inlineKeyboardMarkup = InlineKeyboardMarkup()
        // Create the keyboard (list of InlineKeyboardButton list)
        val keyboard: MutableList<List<InlineKeyboardButton>> = ArrayList()
        // Create a list for buttons
        val Buttons: MutableList<InlineKeyboardButton> = ArrayList()
        // Initialize each button, the text must be written
        val youtube = InlineKeyboardButton("youtube")
        // Also must use exactly one of the optional fields,it can edit  by set method
        youtube.url = "https://www.youtube.com"
        // Add button to the list
        Buttons.add(youtube)
        val dtln = InlineKeyboardButton("DTLN")
        // Also must use exactly one of the optional fields,it can edit  by set method
        dtln.url = "https://www.dtln.com"
        // Add button to the list
        Buttons.add(dtln)
        // Initialize each button, the text must be written
        val github = InlineKeyboardButton("github")
        // Also must use exactly one of the optional fields,it can edit  by set method
        github.url = "https://github.com"
        // Add button to the list
        Buttons.add(github)
        keyboard.add(Buttons)
        inlineKeyboardMarkup.keyboard = keyboard
        // Add it to the message
        message.replyMarkup = inlineKeyboardMarkup
        try {
            // Send the message
            execute(message)
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }

}