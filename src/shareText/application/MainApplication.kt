package shareText.application

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import shareText.controller.UIController
import shareText.utilities.Constants
import shareText.utilities.extensions.localizable
import java.lang.Exception
import javafx.util.Duration.seconds
import javafx.animation.ScaleTransition
import shareText.utilities.extensions.exitOnClose
import java.net.ServerSocket
import java.net.Socket

class MainApplication: Application() {

    companion object {
        var serverSocket: ServerSocket? = null
        var server: Socket? = null
        fun main(args: Array<String>) {
            launch(MainApplication::class.java)
        }
    }

    @Throws(Exception::class)
    override fun start(primaryStage: Stage) {
        val fxmlLoader = FXMLLoader(javaClass.getResource("${Constants.LAYOUT_PATH}splash_controller_layout.fxml"))
        val root: Parent = fxmlLoader.load()
        val scaleIn = ScaleTransition(seconds(1.0), root)
        scaleIn.fromX = 0.0
        scaleIn.fromY = 0.0
        scaleIn.toX = 1.0
        scaleIn.toY = 1.0
        scaleIn.play()
        primaryStage.isResizable = false
        primaryStage.title = Constants.Localizable.APP_NAME_KEY.value.localizable
        primaryStage.scene = Scene(root, 1280.0, 720.0)
        primaryStage.icons.add(Image(javaClass.getResource("/resources/images/share_text_icon.png").toExternalForm()))
        primaryStage.show()
        val controller = fxmlLoader.getController<UIController>()
        controller.primaryStage = primaryStage
        primaryStage.exitOnClose()
    }
}
