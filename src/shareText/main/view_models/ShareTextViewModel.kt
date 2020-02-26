package shareText.main.view_models

import shareText.server_socket.ServerSocketIncomeService
import shareText.server_socket.ServerSocketOutcomeService
import shareText.server_socket.interfaces.IncomeMessage
import shareText.server_socket.models.MessageModel

class ShareTextViewModel(private val incomeMessage: IncomeMessage) {

    var serviceIsStartingReceiving: Boolean = false
        set(value) {
            field = value
            if (value) {
                ServerSocketIncomeService(incomeMessage = incomeMessage).start()
            }
        }

    var outcomeMessageModelString: String? = null
        set(value) {
            field = value
            value?.let { ServerSocketOutcomeService(messageModelString = it).start() }
        }
}