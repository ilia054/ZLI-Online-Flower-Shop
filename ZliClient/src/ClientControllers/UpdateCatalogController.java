package ClientControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Entities.AbstractProduct;
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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
/**
 * this is the controller for Update Catalog, it holds all of the Update Catalog screen logic
 * @author Ilya 
 */

public class UpdateCatalogController implements Initializable {

	@FXML
	private TextField AbstractProductID;

	@FXML
	private FontAwesomeIconView SearchByID;

	@FXML
	private Text ErrorTXT;

	@FXML
	private Text nameTXT;

	@FXML
	private ImageView ItemProductIMG;

	@FXML
	private TextField currentPriceTXT;

	@FXML
	private FontAwesomeIconView searchCostumerBtn1;

	@FXML
	private TextField newPriceTXT;

	@FXML
	private FontAwesomeIconView searchCostumerBtn11;

	@FXML
	private Button saveChanges;

	@FXML
	private Button XBtn;

	@FXML
	private VBox updateHBOX;
	private ChangeScene scene = new ChangeScene();
	private AbstractProduct Product;
	/**
	 * initialize()
	 * This method is called when the screen "Update Catalog" is calleed
	 * it initializes all of our Logic\Buttons\etc...
	 */  

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		updateHBOX.setVisible(false);
		ErrorTXT.setVisible(false);
	}
	/**
	 * saveChange()
	 * This method checks if the user entered all the fields correctly
	 * if not it promts the appropriate error text on screen
	 * if all input is intact it sends a Message to server
	 * the Task is to updated the price of product/item
	 * The object sent is arrayList<Float> 
	 * first cell contains id
	 * second cell contains new price for product/item
	 * @param event when user clicks saveChanges button
	 */  
	@FXML
	void saveChange(ActionEvent event) {
		String toCheck = newPriceTXT.getText();

		if (toCheck.length() < 1) {
			setErrorText("This field cannot be Empty!");
			return;
		}
		if (!checkString(toCheck, "[0-9]+")) {
			setErrorText("New Price can only contain Integers");
			return;
		}
		float newPrice = Float.valueOf(toCheck);
		if (newPrice < 5.00) {
			setErrorText("Minimum price is 5.00$ Please Enter a New Value");
			return;
		}
		if (newPrice > 150.00) {
			setErrorText("Maximum price is 150.00$ Please Enter a New Value");
			return;
		}
		ArrayList<Float> updateInfo = new ArrayList<>();
		updateInfo.add((float) Product.getId());
		updateInfo.add(newPrice);
		ZliClientUI.chat.accept(new Message(Task.Update_Price, updateInfo));
		scene.HelpMessage("Successfully changed ID: " + Product.getId() + "" + "\nPrice From: "
				+ String.format("%.2f", Product.getPrice()) + "$ To: " + String.format("%.2f", newPrice));
		
		ErrorTXT.setVisible(false);
		newPriceTXT.setText("");
		currentPriceTXT.setText(String.valueOf(newPrice));
		
		
	}
	/**
	 * searchByID(MouseEvent event)
	 * This method checks to see if there is and Product\Item with the ID that the user entered
	 * if there is none we set the error string to the relevent error
	 * if yes we load all of the relevent information for the product\item such as:
	 * Image,Price,Name
	 * And enable the create sale button
	 * @param event when user clicks on the Search icon
	 **/ 

	@FXML
	void searchByID(MouseEvent event) {
		String toCheck = AbstractProductID.getText();
		if (toCheck.length() == 0) {
			setErrorText("Please enter an Integer value");
			return;
		} else if (!checkString(toCheck, "[0-9]+")) {
			setErrorText("ID can only be an Integer Value > 0");
			return;
		} else {
			ZliClientUI.chat.accept(new Message(Task.Get_Product_By_ID, Integer.valueOf(toCheck)));
			Product = ZliClient.marketingEmployeeController.getProduct();
			if (Product == null) {
				setErrorText("There is no such ID!");
				return;
			}
		}

		ErrorTXT.setVisible(false);
		updateHBOX.setVisible(true);
		newPriceTXT.setEditable(true);
		nameTXT.setText(Product.getName());
		currentPriceTXT.setText(String.valueOf(Product.getPrice()));
		Image productImage = new Image(getClass().getResourceAsStream(Product.getImgSrc()));
		ItemProductIMG.setImage(productImage);
	}
	/**
	 * checkString(String toCheck, String pattern)
	 * This functions checks if toCheck string is only made of String pattern elements
	 * if yes we return True, else False
	 * @param toCheck user passes us the string he wants checked
	 * @param pattern the pattern The user would like to be comapre in the toCheck string
	 * @return boolean True\False depending on success\failiure
	 **/

	private boolean checkString(String toCheck, String pattern) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(toCheck);
		return m.matches();
	}
	/**
	 * setErrorText(String error)
	 * This functions sets the Error text to the String error user has provided
	 * @param error , error to display to user
	 **/

	void setErrorText(String error) {
		ErrorTXT.setVisible(true);
		ErrorTXT.setText(error);
		return;
	}
	/**
	 * clickedX(MouseEvent event)
	 * Closes the applications and sends a Request_disconnected to server side
	 * @param event mouse clicked on Back Icon
	 **/

	@FXML
	void clickedX(MouseEvent event) {
    	ZliClientUI.chat.accept(new Message(Task.Request_logout, ZliClient.userController.getUser().getUsername()));
		ZliClientUI.chat.accept(new Message(Task.Request_disconnected, null));
		System.exit(0);
	}
	/**
	 * Back(MouseEvent event)
	 * Returns the user the the previous screen while closing current screen
	 * @param event mouse clicked on Back Icon
	 **/

	@FXML
	void Back(MouseEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		scene.changeScreen(new Stage(), "/GuiClientScreens/MarketingEmployeeMenu.fxml", true);
	}
	/**
	 * Help(MouseEvent event)
	 * Displays a Prompt screen to user with Information regarding the current screen
	 * @param event mouse clicked on "?" icon
	 **/
	@FXML
	void Help(MouseEvent event) {
		scene.HelpMessage("*You MUST enter a Product/Item ID in order to update a product\n\n"
				+ "*Price can only be an Integer number!!\n\n" + "*Price can only be in the range of 5-150$");
	}

}