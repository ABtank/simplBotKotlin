import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.util.ArrayList

class ButtonHendler {

    fun sendCustomKeyboard(chatId: String?):SendMessage {
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
        return message
    }

    fun sendInlineKeyboard(chatId: String?):SendMessage {
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
        return message
    }

    fun sendClearCustomKeyboard(chatId: String?):SendMessage {
        val message = SendMessage()
        message.chatId = chatId!!
        message.text = "clear keyboard\n return keyboard /cbtn"
        message.replyMarkup = ReplyKeyboardRemove(true, false)
        return message
    }
}