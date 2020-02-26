package shareText.utilities.extensions

import javafx.scene.control.Label
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Paint
import javafx.stage.Stage
import org.json.JSONObject
import shareText.server_socket.models.MessageModel
import shareText.utilities.Constants
import java.io.BufferedReader
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.InetSocketAddress
import java.net.Socket
import java.util.*
import kotlin.system.exitProcess

val String.localizable: String
    get() = BufferedReader(InputStreamReader(javaClass.getResourceAsStream("/resources/strings/strings_${System.getProperty("user.language")}.json"))).use { JSONObject(it.readText()).getString(this) }

val MouseEvent.isPrimaryButton: Boolean
    get() = this.button == MouseButton.PRIMARY

val Int.port: String
    get() {
        val chars = "123456789"
        val salt = StringBuilder()
        val rnd = Random()
        while(salt.length < this) {
            val index: Int = (rnd.nextFloat() * chars.length).toInt()
            salt.append(chars[index])
        }
        return salt.toString()
    }

val String.localIPAdress: String?
    get() = try { with(Socket()) {
        connect(InetSocketAddress(this@localIPAdress, 80))
        this
    }.localAddress?.hostAddress } catch (e: Exception) { null }

val String?.messageModel: MessageModel?
    get() = this?.let {
        with(JSONObject(it)) {
            MessageModel(connectionState = this.getBoolean(Constants.CONNECTION_STATE), type = this.getString(Constants.MESSAGE_TYPE), body = this.getString(Constants.MESSAGE_BODY), isInstantMessage = this.getBoolean(Constants.INSTANT_VALUE))
        }
    }

fun initJsonMessageObject(connectionState: Boolean = true, type: String, instantValue: Boolean, body: String) = with(JSONObject()) {
    this.put(Constants.CONNECTION_STATE, connectionState)
    this.put(Constants.MESSAGE_TYPE, type)
    this.put(Constants.INSTANT_VALUE, instantValue)
    this.put(Constants.MESSAGE_BODY, body)
    this.toString()
}

fun Label.executeTimerColors(seconds: Int) {
    when (seconds) {
        in 50..60 -> this.textFill = Paint.valueOf("#4080C2")
        in 40..49 -> this.textFill = Paint.valueOf("#40C24C")
        in 30..39 -> this.textFill = Paint.valueOf("#E4C30F")
        in 20..29 -> this.textFill = Paint.valueOf("#FA9723")
        in 10..19 -> this.textFill = Paint.valueOf("#D52B33")
        in 0..9 -> this.textFill = Paint.valueOf("#9C191F")
        else -> this.textFill = Paint.valueOf("#4080C2")
    }
}

fun <T> T?.whenNull(receiver: () -> Unit): T? {
    return if (this == null) {
        receiver.invoke()
        null
    } else this
}

fun <T> T?.otherwise(receiver: (unwrapped: T) -> Unit): T? {
    return if (this != null) {
        receiver.invoke(this)
        this
    } else null
}

fun browseUrlOnLinux(urlString: String) {
    if (Runtime.getRuntime().exec(arrayOf("which", "xdg-open")).inputStream.read() != -1)
        Runtime.getRuntime().exec(arrayOf("xdg-open", urlString))
}

val Socket.receivedMessageModel: MessageModel?
    get() = try { this.getInputStream()?.let { DataInputStream(it).readUTF().messageModel } } catch(e: Exception) { null }

fun Socket.sendMessageModel(messageModelString: String) = this.getOutputStream()?.let { DataOutputStream(it).writeUTF(messageModelString) }

fun Stage.exitOnClose() {
    this.setOnCloseRequest { exitProcess(0) }
}

fun <T> List<T>.secondOrNull(): T? = if (isEmpty()) null else this[1]

val String?.isLink: Boolean
    get() = this?.startsWith("https://") == true || this?.startsWith("http://") == true

fun copyTextToClipboard(text: String) = Clipboard.getSystemClipboard().setContent(with(ClipboardContent()) {
        putString(text)
        this
    })

inline fun <T> Array<out T>.applyToAll(action: (T) -> Unit) { for (element in this) action(element) }