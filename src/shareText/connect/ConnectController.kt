package shareText.connect

import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXTextField
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.ProgressIndicator
import shareText.connect.service.ConnectService
import shareText.controller.UIController
import shareText.utilities.Constants
import shareText.utilities.Timer
import shareText.utilities.extensions.*
import java.net.URL
import java.util.*

class ConnectController: UIController() {
    @FXML private lateinit var searchButton: JFXButton
    @FXML private lateinit var credentialsLabel: Label
    @FXML private lateinit var channelNameLabel: Label
    @FXML private lateinit var channelNameField: JFXTextField
    @FXML private lateinit var ipAddressLabel: Label
    @FXML private lateinit var ipAddressField: JFXTextField
    @FXML private lateinit var portLabel: Label
    @FXML private lateinit var portField: JFXTextField
    @FXML private lateinit var timerLabel: Label
    @FXML private lateinit var progressBar: ProgressIndicator

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        configureLabels()
        configureCredentials()
        configureLayout()
    }

    private fun configureLayout() {
        searchButton.isDisable = true
        channelNameField.textProperty().addListener { _, _, newValue ->
            searchButton.isDisable = newValue.isEmpty()
        }
        searchButton.setOnMouseClicked { if (it.isPrimaryButton) {
            Timer.start { state, seconds ->
                timerLabel.executeTimerColors(seconds = seconds.toInt())
                searchButton.isDisable = !state
                if (!state) { timerLabel.isVisible = true; progressBar.isVisible = true; timerLabel.text = seconds } else { timerLabel.isVisible = false; progressBar.isVisible = false }
            }
            initServerSocket()
        } }
    }

    private fun initServerSocket() {
        ConnectService(port = portField.text?.toInt() ?: 0, channelName = channelNameField.text) {
            primaryStage?.hide()
            viewControllerOnTopHideBlock?.invoke(it)
        }.start()
    }

    private fun configureLabels() {
        searchButton.text = Constants.Localizable.SEARCH_FOR_ANDROID_DEVICE.value.localizable
        credentialsLabel.text = Constants.Localizable.CREDENTIALS_FOR_ANDROID_APP_LABEL.value.localizable
        channelNameLabel.text = Constants.Localizable.CHANNEL_NAME_LABEL.value.localizable
        ipAddressLabel.text = Constants.Localizable.IP_ADDRESS_LABEL.value.localizable
        portLabel.text = Constants.Localizable.PORT_LABEL.value.localizable
    }

    private fun configureCredentials() {
        ipAddressField.text = "google.com".localIPAdress
        portField.text = Constants.PORT_LENGTH.port
    }


}