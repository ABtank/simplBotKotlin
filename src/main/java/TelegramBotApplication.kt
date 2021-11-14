import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.GetFile
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
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
    private val buttonHendler = ButtonHendler()

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
            sb.append("OK.\nОтправь мне login и password.\n")
            sb.append("\nВаш пароль должен содержать цифры, буквы, знаки пунктуации, завязку, развитие, кульминацию и неожиданный финал.\n")
            sb.append("\nПожалуйста используй данный формат:\n")
            sb.append("login - password")
            try {
                val deleteMessage = DeleteMessage(chatIdLong.toString(), update.message.messageId)
                execute(deleteMessage);
//                val editMessage = EditMessageText(chatIdLong.toString(), update.message.messageId, text="****** - *****")
//                execute(editMessage);

                execute(SendMessage.builder()
                        .chatId(chatIdLong.toString())
                        .text(sb.toString())
                        .build())
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
            execute((buttonHendler.sendClearCustomKeyboard(chatIdLong.toString())))

        } else if (update.message.text == "/linkbtn") {
            execute((buttonHendler.sendInlineKeyboard(chatIdLong.toString())))

        } else if (update.message.text == "/cbtn") {
            execute((buttonHendler.sendCustomKeyboard(chatIdLong.toString())))

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
                            .text("Не важно.\nНичего вам не скажу.\nВы все равно не зарегистрированны.\n")
                            .build())
                } catch (e: TelegramApiException) {
                    e.printStackTrace()
                }
            } else if (update.message.hasPhoto()) {
//                val photoSize = getPhoto(update)
//                if (photoSize != null) {
//                    downloadPhotoByFilePath(getFilePath(photoSize))
//                }
                execute(SendMessage.builder()
                        .chatId(chatIdLong.toString())
                        .text("Моя твоя фото не понимать.")
                        .build())


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

}