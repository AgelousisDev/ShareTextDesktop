package shareText.main

import com.jfoenix.controls.*
import javafx.collections.FXCollections
import javafx.fxml.FXML
import shareText.controller.UIController
import java.net.URL
import java.util.*
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import shareText.main.cells.DeviceCell
import shareText.main.cells.MessageCell
import shareText.main.view_models.ShareTextViewModel
import shareText.server_socket.interfaces.IncomeMessage
import shareText.server_socket.models.DeviceModel
import shareText.server_socket.models.MessageModel
import shareText.utilities.Constants
import shareText.utilities.extensions.*

class MainController: UIController(), IncomeMessage {
    @FXML private var developerContactLabel: Label? = null
    @FXML private var reConnectAndroidButton: JFXButton? = null
    @FXML private var connectedDevicesLabel: Label? = null
    @FXML private var connectedDevicesListView: ListView<DeviceModel>? = null
    @FXML private var facebookButton: VBox? = null
    @FXML private var instagramButton: VBox? = null
    @FXML private var linkedinButton: VBox? = null
    @FXML private var emailButton: VBox? = null
    @FXML private var messageTextField: TextField? = null
    @FXML private var sendMessageButton: StackPane? = null
    @FXML private var shareTextListView: ListView<MessageModel>? = null

    private var shareTextViewModel: ShareTextViewModel? = null

    override fun onMessageReceived(message: MessageModel?) {
        message.whenNull {
            showConnectDialog()
        }.otherwise {
            shareTextListView?.items?.add(it)
        }
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        showConnectDialog()
        configureLabels()
        configureUI()
        configureDeviceListView()
        configureContactLayout()
        configureShareTextListView()
        configureMessageFieldLayout()
        configureViewModel()
    }

    private fun showConnectDialog() {
        initController(fxmlName = "${Constants.LAYOUT_PATH}connect_controller_layout.fxml", windowTitle = Constants.Localizable.APP_NAME_KEY.value.localizable, isOnTop = true) {
            (it as? DeviceModel)?.apply {
                connectedDevicesListView?.items = FXCollections.observableArrayList(this)
            }
        }
    }

    private fun configureLabels() {
        reConnectAndroidButton?.text = Constants.Localizable.RECONNECT_ANDROID_DEVICE_LABEL.value.localizable
        connectedDevicesLabel?.text = Constants.Localizable.CONNECTED_DEVICES_LABEL.value.localizable
        developerContactLabel?.text = Constants.Localizable.CONTACT_DEVELOPER_LABEL.value.localizable
        messageTextField?.promptText = Constants.Localizable.ENTER_TEXT_HERE_LABEL.value.localizable
    }

    private fun configureUI() {
        reConnectAndroidButton?.setOnMouseClicked { if (it.isPrimaryButton) showConnectDialog() }
        primaryStage?.setOnHiding {
            shareTextViewModel?.outcomeMessageModelString = initJsonMessageObject(connectionState = false, type = Constants.infoMessageType, instantValue = false, body = "")
        }
    }

    private fun configureDeviceListView() {
        connectedDevicesListView?.setCellFactory { with(DeviceCell()) {
            this.stylesheets.add(javaClass.getResource(Constants.styleClass).toURI().toString())
            this.styleClass.add("list-cell")
            this
        } }
    }

    private fun configureContactLayout() {
        facebookButton?.setOnMouseClicked { if (it.isPrimaryButton) browseUrlOnLinux(urlString = Constants.facebookLink) }
        instagramButton?.setOnMouseClicked { if (it.isPrimaryButton) browseUrlOnLinux(urlString = Constants.instagramLink) }
        linkedinButton?.setOnMouseClicked { if (it.isPrimaryButton) browseUrlOnLinux(urlString = Constants.linkedInLink) }
        emailButton?.setOnMouseClicked { if (it.isPrimaryButton) browseUrlOnLinux(urlString = Constants.emailLink) }
    }

    private fun configureShareTextListView() {
        shareTextListView?.setCellFactory { MessageCell() }
        //shareTextListView?.items = FXCollections.observableArrayList(MessageModel(type = Constants.textType, body = "Hello, how are you?", isInstantMessage = false), MessageModel(type = Constants.textType, body = "I am fine you?", isInstantMessage = false), MessageModel(type = Constants.textType, body = "This is made with RecyclerView.ViewHolder", isInstantMessage = false), MessageModel(type = Constants.textType, body = "Did you integrate git?", isInstantMessage = false), MessageModel(type = Constants.textType, body = "What about clicking on the item?\nEh?", isInstantMessage = false), MessageModel(type = Constants.textType, body = "Maybe a dialog to get the details of it", isInstantMessage = false), MessageModel(type = Constants.textType, body = "Check the margins better", isInstantMessage = false), MessageModel(type = Constants.textType, body = "https://www.google.com", isInstantMessage = false))
    }

    private fun configureMessageFieldLayout() {
        sendMessageButton?.setOnMouseClicked {
            if (it.isPrimaryButton)
                messageTextField?.text?.let { text -> shareTextViewModel?.outcomeMessageModelString = initJsonMessageObject(connectionState = true, type = Constants.textType, instantValue = false, body = text) } }
    }

    private fun configureViewModel() {
        shareTextViewModel = ShareTextViewModel(incomeMessage = this)
        shareTextViewModel?.serviceIsStartingReceiving = true
    }

}