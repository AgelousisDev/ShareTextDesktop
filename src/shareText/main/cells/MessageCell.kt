package shareText.main.cells

import javafx.scene.control.ListCell
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import shareText.server_socket.models.MessageModel
import shareText.utilities.extensions.isLink

class MessageCell: ListCell<MessageModel>() {

    override fun updateItem(item: MessageModel?, empty: Boolean) {
        super.updateItem(item, empty)
        font = Font.font("Ubuntu Mono", FontWeight.BOLD, 14.0)
        if (empty || item == null) {
            text = null
            graphic = null
        }
        else {
            isUnderline = item.body.isLink
            text = item.body
        }
    }
}