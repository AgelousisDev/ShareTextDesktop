package shareText.dialogs

import com.jfoenix.controls.JFXButton
import javafx.concurrent.Worker
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.web.WebView
import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.w3c.dom.events.EventTarget
import org.w3c.dom.html.HTMLAnchorElement
import shareText.controller.UIController
import shareText.dialogs.models.BasicDialogDataModel
import shareText.utilities.extensions.browseUrlOnLinux
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
        basicDialogWebView?.engine?.loadWorker?.stateProperty()?.addListener { _, _, p2 ->
            if (p2 == Worker.State.SUCCEEDED)
                handleWebViewHyperLinks(document = basicDialogWebView?.engine?.document ?: return@addListener)
        }
        basicDialogMainButton?.setOnMouseClicked { if (it.isPrimaryButton) primaryStage?.hide() }
    }

    private fun handleWebViewHyperLinks(document: Document) {
        val nodeList: NodeList = document.getElementsByTagName("a")
        for (i in 0 until nodeList.length) {
            val node: Node = nodeList.item(i)
            val eventTarget: EventTarget = node as EventTarget
            eventTarget.addEventListener("click", {
                val target = it.currentTarget
                val anchorElement = target as? HTMLAnchorElement
                it.preventDefault()
                browseUrlOnLinux(urlString = anchorElement?.href ?: return@addEventListener)
            }, false)
        }
    }

}