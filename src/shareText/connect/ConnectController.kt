package shareText.connect

import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXTextField
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.ProgressIndicator
import javafx.scene.image.ImageView
import shareText.connect.service.ConnectService
import shareText.controller.UIController
import shareText.dialogs.models.BasicDialogDataModel
import shareText.utilities.Constants
import shareText.utilities.Timer
import shareText.utilities.extensions.*
import java.net.URL
import java.util.*

class ConnectController: UIController() {
    @FXML private var searchButton: JFXButton? = null
    @FXML private var ipAddressAndPortNumberLabel: Label? = null
    @FXML private var channelNameField: JFXTextField? = null
    @FXML private var ipAddressField: JFXTextField? = null
    @FXML private var portField: JFXTextField? = null
    @FXML private var timerLabel: Label? = null
    @FXML private var progressBar: ProgressIndicator? = null
    @FXML private var infoIconImageView: ImageView? = null

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        configureLabels()
        configureCredentials()
        configureLayout()
    }

    private fun configureLayout() {
        searchButton?.isDisable = true
        channelNameField?.textProperty()?.addListener { _, _, newValue ->
            searchButton?.isDisable = newValue.isEmpty()
        }
        searchButton?.setOnMouseClicked { if (it.isPrimaryButton) {
            Timer.start { state, seconds ->
                timerLabel?.executeTimerColors(seconds = seconds.toInt())
                searchButton?.isDisable = !state
                if (!state) { timerLabel?.isVisible = true; progressBar?.isVisible = true; timerLabel?.text = seconds } else { timerLabel?.isVisible = false; progressBar?.isVisible = false }
            }
            initServerSocket()
        } }
        infoIconImageView?.setOnMouseClicked { if (it.isPrimaryButton) {
            initController(fxmlName = Constants.BASIC_DIALOG_LAYOUT, windowTitle = "", isOnTop = true,
                    params = BasicDialogDataModel(headerImage = Constants.IC_INFO_IMAGE, header = Constants.Localizable.INSTRUCTIONS_LABEL.value.localizable,
                            webViewContent = Constants.Localizable.APPLICATION_INFORMATION.value.localizable, mainButtonText = Constants.Localizable.OK_LABEL.value.localizable))
        } }
    }

    private fun initServerSocket() {
        ConnectService(port = portField?.text?.split(":")?.secondOrNull()?.trim()?.toInt() ?: 0, channelName = channelNameField?.text ?: "") {
            it?.channelName = channelNameField?.text
            primaryStage?.hide()
            viewControllerOnTopHideBlock?.invoke(it)
        }.start()
    }

    private fun configureLabels() {
        searchButton?.text = Constants.Localizable.SEARCH_FOR_ANDROID_DEVICE.value.localizable
        ipAddressAndPortNumberLabel?.text = Constants.Localizable.IP_ADDRESS_AND_POST_NUMBER_LABEL.value.localizable
        channelNameField?.promptText = Constants.Localizable.CHANNEL_NAME_LABEL.value.localizable
    }

    private fun configureCredentials() {
        ipAddressField?.text = String.format("%s : %s", Constants.Localizable.IP_ADDRESS_LABEL.value.localizable, "google.com".localIPAdress)
        portField?.text = String.format("%s : %s", Constants.Localizable.PORT_LABEL.value.localizable, Constants.PORT_LENGTH.port)
    }


}