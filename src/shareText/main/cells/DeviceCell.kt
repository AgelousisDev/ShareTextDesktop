package shareText.main.cells

import javafx.scene.control.ListCell
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import shareText.server_socket.models.ClientHost

class DeviceCell: ListCell<ClientHost>() {

    override fun updateItem(item: ClientHost?, empty: Boolean) {
        super.updateItem(item, empty)
        font = Font.font("Ubuntu Mono", FontWeight.BOLD, 14.0)
        if (empty || item == null) {
            text = null
            graphic = null
        }
        else {
            text = item.deviceName
        }
    }
}