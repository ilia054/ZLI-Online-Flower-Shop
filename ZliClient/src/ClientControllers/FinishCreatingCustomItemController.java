package ClientControllers;

/**
 * The class FinishCreatingCustomItemController is the controller for finish creating Custom Item in the Purchase options
 * @author Ilya
 *
 */
import java.net.URL;

import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Entities.CustomProduct;
import Enums.Task;
import client.ZliClient;
import client.ZliClientUI;
import common.ChangeScene;
import common.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * GUI Controller of FinishCreatingCustomItem
 */
public class FinishCreatingCustomItemController implements Initializable {

	@FXML
	private TextField ProductNameTXT;
	@FXML
	private Slider minSlider;
	@FXML
	private Slider maxSlider;
	@FXML
	private ComboBox<String> ColorComboBox;
	@FXML
	private ComboBox<String> ArrangementComboBox;
	@FXML
	private Button CompleteProductBtn;
	@FXML
	private Text errorTXT;
	@FXML
	private Button XBtn;

	private ChangeScene scene = new ChangeScene();
	private String components;
	private ObservableList<String> Arrangement = FXCollections.observableArrayList("Boquet", "FlowerArrangment",
			"BlossomingFlower", "FlowerPot");

	/**
	 * initialize() This method is called when the screen "Create Item\Product" is
	 * calleed it initializes all of our Logic\Buttons\etc...
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		errorTXT.setVisible(false);
		ArrangementComboBox.setItems(Arrangement);

	}

	@FXML
	void CompleteProduct(ActionEvent event) {
		if (!testName()) {
			errorTXT.setVisible(true);
			errorTXT.setText(
					"Name cannot be empty and must only contain English letters and spaces, Max 20 Characters!");
			return;
		}
		if (!testPrice()) {
			errorTXT.setVisible(true);
			errorTXT.setText("Max price must be Equal or Greater than Min price!");
			return;
		}
		if (!testColor()) {
			errorTXT.setVisible(true);
			errorTXT.setText("you MUST choose a Dominant color!");
			return;
		}

		if (!testArrangement()) {

			errorTXT.setVisible(true);
			errorTXT.setText("you MUST choose an Arrangement!");
			return;
		}
		int productId = ZliClient.cartController.getNextProductID();
		if (productId == -1)
			ZliClientUI.chat.accept(new Message(Task.Get_nextProductID, null));
		else
			ZliClient.cartController.setNextProductID(productId + 1);
		double price;
		Random rng = new Random();
		if (maxSlider.getValue() != minSlider.getValue())
			price = (double) rng.nextInt((int) maxSlider.getValue() - (int) minSlider.getValue())
					+ (int) minSlider.getValue();
		else
			price = minSlider.getValue();
		CustomProduct customProduct = new CustomProduct(ProductNameTXT.getText(), "/GuiAssests/userIcon.png",
				ColorComboBox.getValue(), ArrangementComboBox.getValue(), components,
				ZliClient.cartController.getNextProductID(), minSlider.getValue(), maxSlider.getValue(), (float) price);
		ZliClient.cartController.addToCart(customProduct);
		((Node) event.getSource()).getScene().getWindow().hide();
		scene.changeScreen(new Stage(), "/GuiClientScreens/ProductCatalog.fxml", true);

	}
	/**
	 * Help(MouseEvent event)
	 * Displays a Prompt screen to user with Information regarding the current screen
	 * @param event mouse clicked on "?" icon
	 **/
	@FXML
	void Help(MouseEvent event) {
		scene.HelpMessage("You MUST pick a product name!"
				+ "\n\nName can only contain English letters and spaces, Max 20 Characters!"
				+ "\n\nMax price has to be bigger or equal to Min price!" + "\n\nYou MUST pick a Dominant color!"
				+ "\n\nYou MUST pick an Arrangement!");
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
	/**
	 * SetData sets the Data for the screen
	 * Minimum Slider,Max Slider, Color CombBox, Components,Price...
	 * @param colors the colors of the items and products in the cart of the custom product created
	 * @param components all the components of the products in the cart
	 * @param price hte total price of all the items and products in the cart
	 */
	public void setData(ObservableList<String> colors, String components, double price) {
		if (colors.size() > 1 && !colors.contains("MIX"))
			colors.add("MIX");
		ColorComboBox.setItems(colors);
		this.components = components;
		minSlider.setMin(price);
		minSlider.setValue(price);
		minSlider.setMax(price + 450);
		maxSlider.setMin(price);
		maxSlider.setValue(price);
		maxSlider.setMax(price + 450);
	}
	/**
	 * Back(MouseEvent event)
	 * Returns the user the the previous screen while closing current screen
	 * @param event mouse clicked on Back Icon
	 **/
	@FXML
	void Back(MouseEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		scene.changeScreen(new Stage(), "/GuiClientScreens/ItemCatalog.fxml", true);
	}
	/**
	 * boolean testName()
	 * checks the name provided by the user
	 * Max of 20 chars, can only contain a-z,A-Z,and space
	 * @return true if test passed else false
	 **/
	private boolean testName() {
		if (ProductNameTXT.getText().length() > 20)
			return false;
		Pattern p = Pattern.compile("^[ A-Za-z]+$");
		Matcher m = p.matcher(ProductNameTXT.getText());
		return m.matches();
	}
	/**
	 * boolean testPrice()
	 * Checks that the Max price is >= min price selected
	 * @return true if test passed else false
	 **/
	private boolean testPrice() {
		return maxSlider.getValue() >= minSlider.getValue();
	}
	/**
	 * boolean testColor()
	 * Checks to see if user selected a color from Color Combo Box
	 * @return true if test passed else false
	 **/
	private boolean testColor() {
		return ColorComboBox.getValue() != null;
	}
	/**
	 * testArrangement()
	 * Checks to see if user selected a an Arrangment from Arrangment Combo Box
	 * @return true if test passed else false
	 **/
	private boolean testArrangement() {
		return ArrangementComboBox.getValue() != null;
	}
}
