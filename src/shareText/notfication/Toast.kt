package shareText.notfication

import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.util.Duration

object Toast {

    fun makeText(ownerStage: Stage, toastMsg: String, toastDelay: Long, fadeInDelay: Double, fadeOutDelay: Double,
            textColor: Color, typeface: String, size: Double) {
        val toastStage = Stage()
        toastStage.width = 400.0
        toastStage.height = 50.0
        toastStage.initOwner(ownerStage)
        toastStage.isResizable = false
        toastStage.initStyle(StageStyle.TRANSPARENT)
        val text = Text(toastMsg)
        text.font = Font.font(typeface, FontWeight.BOLD, size)
        text.fill = textColor
        val root = StackPane(text)
        root.style = "-fx-background-radius: 6px; -fx-background-color: rgba(159, 105, 134, 1.0); -fx-padding: 20px;"
        root.opacity = 0.0
        val scene = Scene(root)
        scene.fill = Color.TRANSPARENT
        toastStage.scene = scene
        toastStage.x = ownerStage.x + (ownerStage.width / 2) - 200
        toastStage.y = ownerStage.y
        toastStage.show()

        val fadeInTimeline = Timeline()
        val fadeInKey1 = KeyFrame(Duration.millis(fadeInDelay), KeyValue (toastStage.scene.root.opacityProperty(), 1))
        fadeInTimeline.keyFrames.add(fadeInKey1)
        fadeInTimeline.setOnFinished {
            Thread(Runnable {
                try { Thread.sleep(toastDelay) }
                catch (e: Exception) { }
                val fadeOutTimeline = Timeline()
                val fadeOutKey1 = KeyFrame(Duration.millis(fadeOutDelay), KeyValue (toastStage.scene.root.opacityProperty(), 0))
                fadeOutTimeline.keyFrames.add(fadeOutKey1)
                fadeOutTimeline.setOnFinished { toastStage.close() }
                fadeOutTimeline.play()
            }).start()
        }
        fadeInTimeline.play()
    }

}