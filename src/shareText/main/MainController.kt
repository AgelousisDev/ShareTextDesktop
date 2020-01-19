package shareText.main

import com.jfoenix.controls.*
import javafx.fxml.FXML
import shareText.controller.UIController
import java.net.URL
import java.util.*
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.stage.Stage
import shareText.application.MainApplication
import shareText.main.cells.DeviceCell
import shareText.main.cells.MessageCell
import shareText.main.enumerations.ShareTextViewType
import shareText.main.view_models.ShareTextViewModel
import shareText.notfication.Toast
import shareText.server_socket.interfaces.IncomeMessage
import shareText.server_socket.models.ClientHost
import shareText.server_socket.models.MessageModel
import shareText.utilities.Constants
import shareText.utilities.extensions.*
import kotlin.system.exitProcess

class MainController: UIController(), IncomeMessage {
    @FXML private var reConnectAndroidButton: JFXButton? = null
    @FXML private var connectedDevicesLabel: Label? = null
    @FXML private var connectedDevicesListView: ListView<ClientHost>? = null
    @FXML private var messageTextField: TextField? = null
    @FXML private var sendMessageButton: StackPane? = null
    @FXML private var channelLabel: Label? = null
    @FXML private var toolbar: VBox? = null
    @FXML private var toolbarCloseButton: ImageView? = null
    @FXML private var toolbarCopyButton: ImageView? = null
    @FXML private var shareTextListView: ListView<MessageModel>? = null
    @FXML private var shareTextEmptyVBox: VBox? = null
    @FXML private var contactUsButton: JFXButton? = null

    private var shareTextViewModel: ShareTextViewModel? = null
    override var params: Any? = null
        set(value) {
            field = value
            (value as? ClientHost)?.apply {
                connectedDevicesListView?.items?.add(this)
                channelLabel?.text = this.channelName
            }
        }
    override var primaryStage: Stage? = null
        set(value) {
            field = value
            value?.setOnCloseRequest {
                if (MainApplication.serverSocket != null && MainApplication.server != null) {
                    shareTextViewModel?.outcomeMessageModelString = initJsonMessageObject(connectionState = false, type = Constants.infoMessageType, instantValue = false, body = "")
                    exitProcess(0)
                }
            }
        }
    private var shareTextViewType = ShareTextViewType.EMPTY_VIEW
        set(value) {
            field = value
            shareTextEmptyVBox?.isVisible = value == ShareTextViewType.EMPTY_VIEW
            toolbar?.isVisible = value == ShareTextViewType.MESSAGE_VIEW
        }

    override fun onMessageReceived(message: MessageModel?) {
        message.whenNull {
            clearAfterDisconnect()
            shareTextViewType = ShareTextViewType.EMPTY_VIEW
            showConnectDialog()
        }.otherwise {
            shareTextViewType = ShareTextViewType.MESSAGE_VIEW
            shareTextListView?.items?.add(it)
        }
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        configureLabels()
        configureUI()
        configureDeviceListView()
        configureContactLayout()
        configureToolbarActions()
        configureShareTextListView()
        configureMessageFieldLayout()
        configureViewModel()
    }

    private fun showConnectDialog() {
        initController(fxmlName = Constants.CONNECT_CONTROLLER_LAYOUT, windowTitle = Constants.Localizable.APP_NAME_KEY.value.localizable, isOnTop = true) {
            (it as? ClientHost)?.apply {
                connectedDevicesListView?.items?.add(this)
                configureViewModel()
            }
        }
    }

    private fun configureLabels() {
        reConnectAndroidButton?.text = Constants.Localizable.RECONNECT_ANDROID_DEVICE_LABEL.value.localizable
        connectedDevicesLabel?.text = Constants.Localizable.CONNECTED_DEVICES_LABEL.value.localizable
        messageTextField?.promptText = Constants.Localizable.ENTER_TEXT_HERE_LABEL.value.localizable
    }

    private fun configureUI() {
        reConnectAndroidButton?.setOnMouseClicked { if (it.isPrimaryButton) showConnectDialog() }
    }

    private fun configureDeviceListView() {
        connectedDevicesListView?.setCellFactory { DeviceCell() }
    }

    private fun configureContactLayout() {
        contactUsButton?.setOnMouseClicked { if (it.isPrimaryButton) initController(fxmlName = Constants.CONTACT_VIEW_LAYOUT, windowTitle = Constants.Localizable.SHARE_TEXT_PROJECT_LABEL.value.localizable, isOnTop = true) }
    }

    private fun configureToolbarActions() {
        toolbarCloseButton?.setOnMouseClicked { if (it.isPrimaryButton) {
            shareTextListView?.selectionModel?.clearSelection()
            configureToolbarButtons(state = false)
        } }
        toolbarCopyButton?.setOnMouseClicked { if (it.isPrimaryButton) {
            copyTextToClipboard(text = shareTextListView?.selectionModel?.selectedItem?.body ?: return@setOnMouseClicked)
            shareTextListView?.selectionModel?.clearSelection()
            configureToolbarButtons(state = false)
            Toast.makeText(ownerStage = primaryStage ?: return@setOnMouseClicked, toastMsg = Constants.Localizable.COPIED_TO_CLIPBOARD_LABEL.value.localizable, toastDelay = 2000, fadeInDelay = 250.0, fadeOutDelay = 250.0, textColor = Color.WHITE, typeface = "Ubuntu Mono", size = 18.0)
        } }
    }

    private fun configureShareTextListView() {
        shareTextListView?.setCellFactory { with(MessageCell()) {
            wrappingWidth = shareTextListView?.width
            this
        } }
        shareTextListView?.setOnMouseClicked { if (it.isPrimaryButton) {
            configureToolbarButtons()
            shareTextListView?.selectionModel?.selectedItem?.body?.takeIf { body -> body.isLink }?.apply { browseUrlOnLinux(urlString = this) }
        } }
    }

    private fun configureMessageFieldLayout() {
        sendMessageButton?.setOnMouseClicked {
            if (it.isPrimaryButton) sendMessage(body = messageTextField?.text?.takeIf { text -> text.isNotEmpty() } ?: return@setOnMouseClicked)
        }
        messageTextField?.setOnKeyPressed {
            if (it.code == KeyCode.ENTER) sendMessage(body = messageTextField?.text?.takeIf { text -> text.isNotEmpty() } ?: return@setOnKeyPressed)
        }
    }

    private fun sendMessage(body: String) {
        shareTextViewModel?.outcomeMessageModelString = initJsonMessageObject(type = Constants.textType, instantValue = false, body = body)
        messageTextField?.clear()
        shareTextListView?.requestFocus()
    }

    private fun configureViewModel() {
        shareTextViewModel = ShareTextViewModel(incomeMessage = this)
        shareTextViewModel?.serviceIsStartingReceiving = true
    }

    private fun clearAfterDisconnect() {
        shareTextListView?.items?.clear()
        connectedDevicesListView?.items?.clear()
    }

    private fun configureToolbarButtons(state: Boolean = true) {
        arrayOf(toolbarCloseButton, toolbarCopyButton).applyToAll {
            it?.opacity = if (state) 1.0 else 0.5
            it?.isDisable = !state
        }
    }

}