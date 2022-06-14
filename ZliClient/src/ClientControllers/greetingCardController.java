package ClientControllers;

import java.net.URL;
import java.util.ResourceBundle;
import Enums.Task;
import client.ZliClient;
import client.ZliClientUI;
import common.ChangeScene;
import common.Message;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
/**
 * this class is the controller for Greeting Card screen, holds all of the logic
 * @author Ilya
 *
 */
public class greetingCardController implements Initializable {

	@FXML
	private Button XBtn;

	@FXML
	private Button noBTN;

	@FXML
	private Button yesBTN;

	@FXML
	private TextArea greetingTXT;

	@FXML
	private FontAwesomeIconView BackBtn;

	@FXML
	private Button proceedBTN;

	@FXML
	private Text errorTXT;

	private boolean addGreetingCard;
	private ChangeScene scene = new ChangeScene();
	/**
	 * initialize()
	 * This method is called when user clicked "Complete" in the Purchase catalog
	 * it initializes all of our Logic\Buttons\etc...
	 */ 
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		greetingTXT.setDisable(true);
		greetingTXT.clear();
		errorTXT.setText("");
		addGreetingCard = false;
	}
	/**
	 * yesClicked(MouseEvent event)
	 * if uses clicked "Yes" we enable the Greeting Car Text Area
	 * and set the boolean AddGreetingCard true to get the greeting card user has entered
	 * @param event "yes" button clicked
	 */ 
	@FXML
	void yesClicked(MouseEvent event) {
		greetingTXT.setDisable(false);
		yesBTN.setDisable(true);
		greetingTXT.clear();
		addGreetingCard = true;
	}
	/**
	 * noClicked(MouseEvent event)
	 * User does not want to add greeting we move on to the next screen
	 * @param event user clicked "NO"
	 **/
	@FXML
	void noClicked(MouseEvent event) {
		changeToDelivery(event);
	}
	/**
	 * Help(MouseEvent event)
	 * Displays a Prompt screen to user with Information regarding the current screen
	 * @param event mouse clicked on "?" icon
	 **/
	@FXML
	void Help(MouseEvent event) {
		scene.HelpMessage(
				"You can add a greeting to your Order for FREE!"
				+ "\nBut it can only contain up to 40 letters!"
				+ "\n\n You can press 'No' Button at any time to continue without greeting card");
	}
	/**
	 * proceedToDelivery(MouseEvent event)
	 * This method checks if user has provided the correct Length and legal arguments to the greeting
	 * @param event mouse clicked "Procceed to Delivery"
	 **/
	@FXML
	void proceedToDelivery(MouseEvent event) {
		if (addGreetingCard) {
			if (greetingTXT.getText().length() == 0 || greetingTXT.getText() == null
					|| greetingTXT.getText().length() > 40) {
				errorTXT.setText("You have to Enter a greeting Up to 40 letters!");
				return;
			}
		}
		ZliClient.cartController.setGreetingCard(greetingTXT.getText());
		changeToDelivery(event);
	}
	/**
	 * Back(MouseEvent event)
	 * Returns the user the the previous screen while closing current screen
	 * @param event mouse clicked on Back Icon
	 **/
	@FXML
	void Back(MouseEvent event) {
		ZliClient.cartController.getInstance().clearBeforeExit(false);
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		scene.changeScreen(new Stage(), "/GuiClientScreens/ProductCatalog.fxml", true);
	}
	/**
	 * changeToDelivery(MouseEvent event)
	 * Changes screen to Delivery screen
	 * @param event mouse clicked Done btn
	 **/
	void changeToDelivery(MouseEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		scene.changeScreen(new Stage(), "/GuiClientScreens/Delivery.fxml", true);
	}
	/**
	 * X(ActionEvent event)
	 * Closes the applications and sends a Request_disconnected to server side
	 * @param event mouse clicked on Back Icon
	 **/
	@FXML
	void X(ActionEvent event) {
    	ZliClientUI.chat.accept(new Message(Task.Request_logout, ZliClient.userController.getUser().getUsername()));
		ZliClientUI.chat.accept(new Message(Task.Request_disconnected, null));
		System.exit(0);
	}

}
