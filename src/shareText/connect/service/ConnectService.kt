package shareText.connect.service

import javafx.concurrent.Service
import javafx.concurrent.Task
import shareText.application.MainApplication
import shareText.server_socket.models.MessageModel
import shareText.utilities.ConnectServiceBlock
import shareText.utilities.Constants
import shareText.utilities.Timer
import shareText.utilities.extensions.messageModel
import java.io.DataInputStream
import java.net.ServerSocket

class ConnectService(private val port: Int, private val connectServiceBlock: ConnectServiceBlock): Service<MessageModel?>() {

    override fun createTask(): Task<MessageModel?> {
        return object : Task<MessageModel?>() {
            override fun call(): MessageModel? {
                MainApplication.serverSocket = ServerSocket(port)
                MainApplication.serverSocket?.reuseAddress = true
                MainApplication.serverSocket?.soTimeout = Timer.TIMER_SECONDS.toInt() * 1000
                MainApplication.server = MainApplication.serverSocket?.accept()
                val inputStream = DataInputStream(MainApplication.server?.getInputStream().takeIf { it != null }
                        ?: return null)
                inputStream.use {
                    return it.readUTF().messageModel
                }
            }

            override fun succeeded() {
                super.succeeded()
                when (this.value?.type) {
                    Constants.infoMessageType -> {
                        connectServiceBlock(this@ConnectService.value)
                    }
                }
            }
        }

    }
}