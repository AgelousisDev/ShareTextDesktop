package shareText.contact

import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import shareText.controller.UIController
import shareText.utilities.Constants
import shareText.utilities.extensions.browseUrlOnLinux
import shareText.utilities.extensions.isPrimaryButton
import shareText.utilities.extensions.localizable
import java.net.URL
import java.util.*

class ContactViewController: UIController() {
    @FXML private var projectLabel: Label? = null
    @FXML private var githubButton: VBox? = null
    @FXML private var developerContactLabel: Label? = null
    @FXML private var facebookButton: VBox? = null
    @FXML private var instagramButton: VBox? = null
    @FXML private var linkedinButton: VBox? = null
    @FXML private var emailButton: VBox? = null

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        configureContactLayout()
    }

    private fun configureContactLayout() {
        projectLabel?.text = Constants.Localizable.PROJECT_LABEL.value.localizable
        githubButton?.setOnMouseClicked { if (it.isPrimaryButton) browseUrlOnLinux(urlString = Constants.githubLink) }

        developerContactLabel?.text = Constants.Localizable.CONTACT_DEVELOPER_LABEL.value.localizable
        facebookButton?.setOnMouseClicked { if (it.isPrimaryButton) browseUrlOnLinux(urlString = Constants.facebookLink) }
        instagramButton?.setOnMouseClicked { if (it.isPrimaryButton) browseUrlOnLinux(urlString = Constants.instagramLink) }
        linkedinButton?.setOnMouseClicked { if (it.isPrimaryButton) browseUrlOnLinux(urlString = Constants.linkedInLink) }
        emailButton?.setOnMouseClicked { if (it.isPrimaryButton) browseUrlOnLinux(urlString = Constants.emailLink) }

    }

}