package shareText.connect.service

import javafx.concurrent.Service
import javafx.concurrent.Task
import shareText.application.MainApplication
import shareText.server_socket.models.DeviceModel
import shareText.server_socket.models.MessageModel
import shareText.utilities.ConnectServiceBlock
import shareText.utilities.Constants
import shareText.utilities.Timer
import shareText.utilities.extensions.initJsonMessageObject
import shareText.utilities.extensions.messageModel
import java.io.DataInputStream
import java.io.DataOutputStream
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
                val outToClient = MainApplication.server?.getOutputStream()
                val out = DataOutputStream(outToClient)
                out.writeUTF(initJsonMessageObject(type = Constants.infoMessageType, instantValue = false, body = channelName))
                //Get from Client
                val inputStream = DataInputStream(MainApplication.server?.getInputStream().takeIf { it != null } ?: return null)
                return inputStream.readUTF().messageModel
            }

            override fun succeeded() {
                super.succeeded()
                connectServiceBlock(DeviceModel(deviceName = this@ConnectService.value?.body))
            }
        }

    }
}