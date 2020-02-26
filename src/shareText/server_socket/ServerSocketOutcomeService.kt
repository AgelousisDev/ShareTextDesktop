package shareText.server_socket

import javafx.concurrent.Service
import javafx.concurrent.Task
import shareText.application.MainApplication
import shareText.utilities.extensions.messageModel
import shareText.utilities.extensions.sendMessageModel

class ServerSocketOutcomeService(private val messageModelString: String): Service<Void?>() {

    override fun createTask(): Task<Void?> {
        return object: Task<Void?>() {
            override fun call(): Void? {
                MainApplication.server?.sendMessageModel(messageModelString = messageModelString)
                if (messageModelString.messageModel?.connectionState == false) {
                    MainApplication.server?.close()
                    MainApplication.serverSocket?.close()
                }
                return null
            }
        }
    }

}