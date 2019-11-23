package shareText.main

import com.jfoenix.controls.*
import javafx.fxml.FXML
import shareText.controller.UIController
import java.net.URL
import java.util.*
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import shareText.notfication.Toast
import shareText.server_socket.models.MessageModel
import shareText.utilities.Constants
import shareText.utilities.extensions.*

class MainController: UIController() {
    @FXML private var developerContactLabel: Label? = null
    @FXML private var outcomeTextArea: JFXTextArea? = null
    @FXML private var incomeJfxListView: JFXListView<String>? = null
    @FXML private var facebookButton: VBox? = null
    @FXML private var instagramButton: VBox? = null
    @FXML private var linkedinButton: VBox? = null
    @FXML private var emailButton: VBox? = null

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        showConnectDialog()
        configureLabels()
        configureContactLayout()
    }

    private fun showConnectDialog() {
        initController(fxmlName = "${Constants.LAYOUT_PATH}connect_controller_layout.fxml", windowTitle = Constants.Localizable.APP_NAME_KEY.value.localizable, isOnTop = true) {

        }
    }

    private fun configureLabels() {
        developerContactLabel?.text = Constants.Localizable.CONTACT_DEVELOPER_LABEL.value.localizable
    }

    private fun configureContactLayout() {
        facebookButton?.setOnMouseClicked { if (it.isPrimaryButton) browseUrlOnLinux(urlString = Constants.facebookLink) }
        instagramButton?.setOnMouseClicked { if (it.isPrimaryButton) browseUrlOnLinux(urlString = Constants.instagramLink) }
        linkedinButton?.setOnMouseClicked { if (it.isPrimaryButton) browseUrlOnLinux(urlString = Constants.linkedInLink) }
        emailButton?.setOnMouseClicked { if (it.isPrimaryButton) browseUrlOnLinux(urlString = Constants.emailLink) }
    }

}