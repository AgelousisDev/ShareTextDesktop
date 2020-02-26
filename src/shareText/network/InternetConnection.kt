package shareText.network

import javafx.concurrent.Service
import javafx.concurrent.Task
import shareText.utilities.InternetConnectionBlock
import java.net.URL

class InternetConnection(private val internetConnectionBlock: InternetConnectionBlock): Service<Boolean>() {
    override fun createTask(): Task<Boolean> = object: Task<Boolean>() {
        override fun call(): Boolean {
            return try { with(URL("https://google.com").openConnection()) { this }.getInputStream() != null }
            catch(ex: Exception) {
                false
            }
        }

        override fun succeeded() {
            super.succeeded()
            internetConnectionBlock(value)
        }
    }
}