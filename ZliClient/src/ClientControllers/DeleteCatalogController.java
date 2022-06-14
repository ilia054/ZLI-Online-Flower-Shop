package ClientControllers;

import java.net.URL;
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
 * The class DeleteCatalogController is the controller for "Delte product\Item" from catalog
 * @author Ilya
 *
 */

public class DeleteCatalogController implements Initializable {

	@FXML
	private Button XBtn;

	@FXML
	private FontAwesomeIconView XIcon;

	@FXML
	private FontAwesomeIconView BackBtn;

	@FXML
	private TextField AbstractProductID;

	@FXML
	private FontAwesomeIconView SearchByID;

	@FXML
	private VBox updateHBOX;

	@FXML
	private Text nameTXT;

	@FXML
	private ImageView ItemProductIMG;

	@FXML
	private TextField currentPriceTXT;

	@FXML
	private FontAwesomeIconView searchCostumerBtn1;

	@FXML
	private Button DeletePressed;

	@FXML
	private Text ErrorTXT;

	private ChangeScene scene = new ChangeScene();
	private AbstractProduct Product;

	/**
	 * initialize()
	 * This method is called when the screen "Delete Product\Item" is called
	 * it initializes all of our Logic\Buttons\etc...
	 */ 

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		updateHBOX.setVisible(false);
		ErrorTXT.setVisible(false);
	}

	/**
	 * searchByID(MouseEvent event)
	 * This method checks to see if there is an Product\Item with the ID that the user entered
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
		nameTXT.setText(Product.getName());
		currentPriceTXT.setText(String.valueOf(Product.getPrice()));
		Image productImage = new Image(getClass().getResourceAsStream(Product.getImgSrc()));
		ItemProductIMG.setImage(productImage);
	}
	/**
	 * DeletePressed(ActionEvent event)
	 * This method runs a qurey request to the Server side 
	 * the query deletes the item\product the user selected
	 * @param event when user clicks Delete button
	 **/    

	@FXML
	void DeletePressed(ActionEvent event) {
		ZliClientUI.chat.accept(new Message(Task.Delete_Product_By_ID, Product.getId()));
		scene.HelpMessage("Successfully deleted ID: " + Product.getId());
		updateHBOX.setVisible(false);
		ErrorTXT.setVisible(false);
	}
	/**
	 * X(ActionEvent event)
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
	 * Help(MouseEvent event)
	 * Displays a Prompt screen to user with Information regarding the current screen
	 * @param event mouse clicked on "?" icon
	 **/
	@FXML
	void Help(MouseEvent event) {
		scene.HelpMessage("*You MUST enter a Product/Item ID in order to delete a product\n\n"
				+ "*Product/Item will be pemenantly deleted from catalog and won't be recoverable");
	}

}
