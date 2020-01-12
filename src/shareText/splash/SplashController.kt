package shareText.splash

import javafx.application.Platform
import javafx.scene.paint.Color
import shareText.controller.UIController
import shareText.network.InternetConnection
import shareText.notfication.Toast
import shareText.server_socket.models.DeviceModel
import shareText.utilities.Constants
import shareText.utilities.extensions.localizable
import java.net.URL
import java.util.*

class SplashController: UIController() {

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        with(Timer()) {
            schedule(object : TimerTask() {
                override fun run() {
                    Platform.runLater {
                        with(InternetConnection {
                            if (!it)
                                primaryStage?.let { stage -> Toast.makeText(ownerStage = stage, toastMsg = Constants.Localizable.INTERNET_CONNECTION_NOT_AVAILABLE_LABEL.value.localizable, toastDelay = 2000, fadeInDelay = 250.0, fadeOutDelay = 250.0, textColor = Color.WHITE, typeface = "Ubuntu Mono", size = 18.0) }
                            else {
                                initController(fxmlName = Constants.CONNECT_CONTROLLER_LAYOUT, windowTitle = Constants.Localizable.APP_NAME_KEY.value.localizable, isOnTop = true) { params ->
                                    (params as? DeviceModel)?.apply {
                                        setController(fxmlName = "${Constants.LAYOUT_PATH}main_controller.fxml", params = this)
                                    }
                                }
                            }
                        }) { start() }
                    }
                }
            }, 2000)
        }
    }

}
