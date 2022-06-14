package ClientControllers;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import Entities.AbstractProduct;
import Entities.Sale;
import Enums.Task;
import client.ZliClient;
import client.ZliClientUI;
import common.ChangeScene;
import common.Message;
import common.Time;
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
 * The class AbsProductInCartController represents item in cart in the "Payment" screen
 * @author Ilya
 *
 */

public class createSaleController implements Initializable {

	@FXML
	private Button XBtn;

	@FXML
	private FontAwesomeIconView XIcon;

	@FXML
	private FontAwesomeIconView BackBtn;

	@FXML
	private FontAwesomeIconView HelpBtn;

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
	private TextField salePriceTXT;

	@FXML
	private FontAwesomeIconView searchCostumerBtn11;

	@FXML
	private JFXDatePicker saleEndDate;

	@FXML
	private Button createSale;

	@FXML
	private Text ErrorTXT;

	@FXML
	private JFXTimePicker saleEndTime;

	private AbstractProduct Product;
	private ChangeScene scene = new ChangeScene();
	private HashMap<Integer, Sale> activeSales;
	private int productID;
	/**
	 * initialize()
	 * This method is called when the screen "Create Sale" is calleed
	 * it initializes all of our Logic\Buttons\etc...
	 */   

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		updateHBOX.setVisible(false);
		ErrorTXT.setVisible(false);
		createSale.setDisable(true);
		ZliClientUI.chat.accept(new Message(Task.Get_ActiveSales, null));
		activeSales = ZliClient.salesController.getActiveSalesMap();
	}
	/**
	 * createSale(ActionEvent event)
	 * This method is called when use is "Done" with creating a sale
	 * we run test for Price,Time,Date, etc..
	 * If all test passed we create a Sale in the DB, if no we set the error text to the current error
	 * @param event when user clicks on Create Sale
	 **/ 

	@FXML
	void createSale(ActionEvent event) {
		String toCheck = salePriceTXT.getText();
		if (toCheck.length() < 1) {
			setErrorText("Sale price cannot be Empty!");
			return;
		}
		if (!checkString(toCheck, "[0-9]+")) {
			setErrorText("Sale price can only contain Integers");
			return;
		}
		float newPrice = Float.valueOf(toCheck);
		if (newPrice < 0.2 * Product.getPrice() || newPrice > 0.8 * Product.getPrice()) {
			setErrorText("Minimum Sale price is: $" + 0.8 * Product.getPrice() + "\nMaximum Sale Price is: $"
					+ 0.2 * Product.getPrice());
			return;
		}
		if (saleEndDate.getValue() == null || saleEndTime.getValue() == null) {
			setErrorText("You must pick date and time!");
			return;
		}
		String timeAndDate = saleEndDate.getValue().toString() + " " + saleEndTime.getValue().toString();
		Long diff = Time.calculateTimeDiff(timeAndDate + ":00");
		if (diff < 1440 || diff > 1440 * 7) {
			setErrorText("Sale date MUST be between 1 to 7 days");
			return;
		}

		Sale sale = new Sale(productID, Time.formatLocalDate(), timeAndDate, newPrice);
		ZliClientUI.chat.accept(new Message(Task.Add_New_Sale, sale));

		scene.HelpMessage(
				sale.toString() + "\nOriginal price: " + Product.getPrice() + "$" + "\nSale price: " + newPrice + "$");
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		scene.changeScreen(new Stage(), "/GuiClientScreens/MarketingEmployeeMenu.fxml", true);
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
		}
		if (!checkString(toCheck, "[0-9]+")) {
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
		productID = Integer.valueOf(toCheck);

		if (activeSales.containsKey(productID)) {
			setErrorText("Product ID: " + productID + "\nAlready has an Active Sale!");
			return;
		}
		ErrorTXT.setVisible(false);
		updateHBOX.setVisible(true);
		salePriceTXT.setEditable(true);
		nameTXT.setText(Product.getName());
		currentPriceTXT.setText(String.valueOf(Product.getPrice()));
		Image productImage = new Image(getClass().getResourceAsStream(Product.getImgSrc()));
		ItemProductIMG.setImage(productImage);
		createSale.setDisable(false);

	}
	/**
	 * setErrorText(String error)
	 * Sets ErrorTXT to error, a String of error provided to display to user
	 * @param error the error text to present
	 * @return boolean True\False depending on success\failiure
	 **/  

	void setErrorText(String error) {
		ErrorTXT.setVisible(true);
		ErrorTXT.setText(error);
		return;
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
		scene.HelpMessage("*You MUST enter a Product/Item ID in order to creat a sale\n\n"
				+ "*You can only select a Prodcut\\Item which does not have an Active sale!"
				+ "\n\n*The Minimum Discount allowed is 20% of the Current price"
				+ "\n\n*The Maximum Discount allowed is 80% of the Current price"
				+ "\n\n*The Minimum Time of a sale is At least 24 hours!"
				+ "\n\n*The Maximum Time of a Sale is At Most 7 days!");
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

}
