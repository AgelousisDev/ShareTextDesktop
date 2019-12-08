package shareText.server_socket

import javafx.concurrent.Service
import javafx.concurrent.Task
import shareText.application.MainApplication
import shareText.server_socket.interfaces.IncomeMessage
import shareText.server_socket.models.MessageModel
import shareText.utilities.extensions.receivedMessageModel

class ServerSocketIncomeService(private val incomeMessage: IncomeMessage): Service<MessageModel?>() {

    override fun createTask(): Task<MessageModel?> {
        return object: Task<MessageModel?>() {
            override fun call(): MessageModel? {
                return MainApplication.server?.receivedMessageModel
            }

            override fun succeeded() {
                super.succeeded()
                incomeMessage.onMessageReceived(message = this.value)
                this.value?.let { restart() }
            }
        }
    }

}