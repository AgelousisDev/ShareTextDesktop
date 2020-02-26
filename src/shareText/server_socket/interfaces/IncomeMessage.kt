package shareText.server_socket.interfaces

import shareText.server_socket.models.MessageModel

interface IncomeMessage {
    fun onMessageReceived(message: MessageModel?)
}