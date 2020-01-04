package shareText.dialogs

import com.jfoenix.controls.JFXButton
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.web.WebView
import shareText.controller.UIController
import shareText.dialogs.models.BasicDialogDataModel
import shareText.utilities.extensions.isPrimaryButton
import java.net.URL
import java.util.*

class BasicDialogController: UIController() {

    @FXML private var basicDialogHeaderImageView: ImageView? = null
    @FXML private var basicDialogHeaderLabel: Label? = null
    @FXML private var basicDialogWebView: WebView? = null
    @FXML private var basicDialogMainButton: JFXButton? = null
    override var params: Any? = null
        set(value) {
            field = value
            (value as? BasicDialogDataModel)?.let { dataModel ->
                basicDialogHeaderImageView?.image = Image(javaClass.getResource(dataModel.headerImage).toExternalForm())
                basicDialogHeaderLabel?.text = dataModel.header
                basicDialogWebView?.engine?.loadContent(dataModel.webViewContent)
                basicDialogMainButton?.text = dataModel.mainButtonText
            }
        }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        configureUI()
    }

    private fun configureUI() {
        basicDialogWebView?.engine?.loadWorker?.stateProperty()?.addListener { _, _, _ ->
            if (basicDialogWebView?.engine?.location?.startsWith("http") == true)
                basicDialogWebView?.engine?.loadWorker?.cancel()
        }
        basicDialogMainButton?.setOnMouseClicked { if (it.isPrimaryButton) primaryStage?.hide() }
    }

}