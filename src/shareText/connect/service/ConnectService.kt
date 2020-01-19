package shareText.connect.service

import javafx.concurrent.Service
import javafx.concurrent.Task
import shareText.application.MainApplication
import shareText.server_socket.models.ClientHost
import shareText.server_socket.models.MessageModel
import shareText.utilities.ConnectServiceBlock
import shareText.utilities.Constants
import shareText.utilities.Timer
import shareText.utilities.extensions.initJsonMessageObject
import shareText.utilities.extensions.receivedMessageModel
import shareText.utilities.extensions.sendMessageModel
import java.net.ServerSocket

class ConnectService(private val port: Int, private val channelName: String, private val connectServiceBlock: ConnectServiceBlock): Service<MessageModel?>() {

    override fun createTask(): Task<MessageModel?> {
        return object : Task<MessageModel?>() {
            override fun call(): MessageModel? {
                MainApplication.serverSocket = ServerSocket(port)
                MainApplication.serverSocket?.reuseAddress = true
                MainApplication.serverSocket?.soTimeout = Timer.TIMER_SECONDS.toInt() * 1000
                MainApplication.server = MainApplication.serverSocket?.accept()
                //Write to Client
                MainApplication.server?.sendMessageModel(messageModelString = initJsonMessageObject(type = Constants.infoMessageType, instantValue = false, body = channelName))
                //Get from Client
                return MainApplication.server?.receivedMessageModel
            }

            override fun succeeded() {
                super.succeeded()
                connectServiceBlock(ClientHost(deviceName = this@ConnectService.value?.body))
            }
        }

    }
}