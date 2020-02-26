package shareText.utilities

import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.util.Duration

object Timer {
    const val TIMER_SECONDS = 60.0
    val timeLine = Timeline()
    fun start(timerBlock: TimerBlock) {
        var seconds = TIMER_SECONDS
        timeLine.cycleCount = Timeline.INDEFINITE
        timeLine.keyFrames.add(KeyFrame(Duration.seconds(1.0), EventHandler<ActionEvent> { seconds -= 1.0; timerBlock(seconds <= 0, seconds.toInt().toString()); }))
        timeLine.playFromStart()
    }
    fun stop() = timeLine.stop()
}