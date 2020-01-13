package shareText.main.cells

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.ListCell
import javafx.scene.paint.Paint
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import shareText.server_socket.models.MessageModel
import shareText.utilities.extensions.isLink

class MessageCell: ListCell<MessageModel>() {
    private var textView: Text? = null
    var wrappingWidth: Double? = null

    override fun updateItem(item: MessageModel?, empty: Boolean) {
        super.updateItem(item, empty)
        font = Font.font("Ubuntu Mono", FontWeight.BOLD, 14.0)
        if (empty || item == null) {
            text = null
            graphic = null
        }
        else {
            wrappingWidth?.let { prefWidth = it - 48 }
            isUnderline = item.body.isLink
            isWrapText = true
            text = item.body
            graphic = null
        }
    }
}