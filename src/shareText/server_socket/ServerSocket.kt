package shareText.server_socket

import javafx.concurrent.Service
import javafx.concurrent.Task
import java.net.ServerSocket
import java.net.Socket
import javafx.stage.Stage
import shareText.server_socket.interfaces.IncomeMessage
import shareText.server_socket.models.MessageModel
import shareText.utilities.Timer
import shareText.utilities.extensions.messageModel
import java.io.*

class ServerSocket(private val stage: Stage?, port: Int, private val incomeMessage: IncomeMessage): Service<MessageModel?>() {

    override fun createTask(): Task<MessageModel?> = object: Task<MessageModel?>() {

            override fun call(): MessageModel? {
                server = serverSocket?.accept()
                val inputStream = DataInputStream(server?.getInputStream().takeIf { it != null } ?: return null)
                inputStream.use {
                    return it.readUTF().messageModel
                }
            }

            override fun succeeded() {
                super.succeeded()
                incomeMessage.onMessageReceived(message = value)
                start()
            }

        }

  private var server: Socket? = null
  private var serverSocket: ServerSocket? = null

    init {
        serverSocket = ServerSocket(port)
        serverSocket?.reuseAddress = true
        serverSocket?.soTimeout = Timer.TIMER_SECONDS.toInt() * 1000
        closeServerSocket()
    }
   
   fun sendMessage(username: String, message: String) {
        if (serverSocket != null) {
            try
            {
                val out = DataOutputStream(server?.getOutputStream())
                out.writeByte(0)
                out.writeUTF("$username wrote: $message")
            }
            catch(ex: Exception) {
                println(ex.toString())
            }
        }
    }
    
    fun attachFile(username: String, message: String, filePath: String) {
        sendMessage(username, message)
        try
        {
            val dos = DataOutputStream(server?.getOutputStream())
            dos.writeByte(1)
            dos.writeUTF(filePath)
            val fis = FileInputStream(filePath)
            var bytesAvailable = fis.available()

            val maxBufferSize = 1024
            var bufferSize = Math.min(bytesAvailable, maxBufferSize)
            val buffer = ByteArray(bufferSize)

            var bytesRead = fis.read(buffer, 0, bufferSize)

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize)
                bytesAvailable = fis.available()
                bufferSize = Math.min(bytesAvailable, maxBufferSize)
                bytesRead = fis.read(buffer, 0, bufferSize)
            }
            dos.flush()
            dos.close()
        }
        catch(ex: IOException) {
            println(ex.toString())
        }
    }

    private fun closeServerSocket() {
        stage?.setOnHiding {
            try
            {
                serverSocket?.close()
            }
            catch (ex: IOException) {
                println(ex.toString())
            }
        }
    }
    
}
//java GreetingServer 6066
