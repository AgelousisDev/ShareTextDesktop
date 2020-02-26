package shareText.controller

import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.StageStyle
import shareText.utilities.ViewControllerOnTopHideBlock
import java.net.URL
import java.util.*

open class UIController: Initializable {

    open var primaryStage: Stage? = null
    open var params: Any? = null
    var viewControllerOnTopHideBlock: ViewControllerOnTopHideBlock? = null

    override fun initialize(location: URL?, resources: ResourceBundle?) {}

    /**
     * Main Controller
     */
    fun setController(fxmlName: String, params: Any? = null) {
        val fxmlLoader = FXMLLoader(javaClass.getResource(fxmlName))
        val root: Parent = fxmlLoader.load()
        val scene = Scene(root)
        primaryStage?.scene = scene
        val controller = fxmlLoader.getController<UIController>()
        controller.params = params
        controller.primaryStage = primaryStage
    }

    /**
     * Secondary Controller
     */
    fun initController(fxmlName: String, windowTitle: String, isOnTop: Boolean = false, params: Any? = null, viewControllerOnTopHideBlock: ViewControllerOnTopHideBlock? = null) {
        val fxmlLoader = FXMLLoader(javaClass.getResource(fxmlName))
        val root: Parent = fxmlLoader.load()
        val scene = Scene(root)
        val stage = Stage()
        stage.isAlwaysOnTop = isOnTop
        stage.isResizable = false
        stage.title = windowTitle
        stage.scene = scene
        stage.initModality(Modality.WINDOW_MODAL)
        stage.initStyle(StageStyle.DECORATED)
        stage.icons.add(Image(javaClass.getResource("/resources/images/share_text_icon.png").toExternalForm()))
        stage.show()
        val controller = fxmlLoader.getController<UIController>()
        controller.primaryStage = stage
        controller.viewControllerOnTopHideBlock = viewControllerOnTopHideBlock
        controller.params = params
    }
}