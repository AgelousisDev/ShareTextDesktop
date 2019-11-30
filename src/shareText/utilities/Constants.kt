package shareText.utilities

import shareText.server_socket.models.DeviceModel

typealias TimerBlock = (Boolean, String) -> Unit
typealias InternetConnectionBlock = (Boolean) -> Unit
typealias ConnectServiceBlock = (DeviceModel?) -> Unit
typealias ViewControllerOnTopHideBlock = (Any?) -> Unit
object Constants {
    const val LAYOUT_PATH = "/resources/layout/"
    const val styleClass = "/resources/style/style.css"
    const val PORT_LENGTH = 4
    const val MESSAGE_TYPE = "type"
    const val INSTANT_VALUE = "instant"
    const val MESSAGE_BODY = "body"
    const val infoMessageType = "text/info"
    const val textType = "text/plain"
    const val facebookLink = "https://www.facebook.com/vagelakis.agelousis"
    const val instagramLink = "https://www.instagram.com/vagelakis_agelousis"
    const val linkedInLink = "https://www.linkedin.com/in/vagelis-agelousis-7a8793124/"
    const val emailLink = "mailto:vagelis_agelousis@outlook.com?subject=ShareText"

    enum class Localizable(val value: String) {
        APP_NAME_KEY("app_name"),
        SEARCH_FOR_ANDROID_DEVICE("search_for_android_label"),
        CREDENTIALS_FOR_ANDROID_APP_LABEL("credentials_for_android_app_label"),
        IP_ADDRESS_LABEL("ip_address_label"),
        PORT_LABEL("port_label"),
        APPLICATION_INFORMATION("application_information"),
        INTERNET_CONNECTION_NOT_AVAILABLE_LABEL("internet_connection_not_available_label"),
        CONTACT_DEVELOPER_LABEL("contact_developer_label"),
        CHANNEL_NAME_LABEL("channel_name_label"),
        RECONNECT_ANDROID_DEVICE_LABEL("reconnect_android_device_label"),
        CONNECTED_DEVICES_LABEL("connected_devices_label"),
        ENTER_TEXT_HERE_LABEL("enter_message_here_label")
    }

}