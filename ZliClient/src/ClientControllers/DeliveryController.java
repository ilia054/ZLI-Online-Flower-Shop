package ClientControllers;

/**
 * This class is the Controller for Delivery FXML file
 * This holds all the logic of the Delivery window
 * @author Biran Fridman, Ilya Lev
 */
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import Entities.Delivery;
import Enums.Store;
import Enums.SupplyMethod;
import Enums.Task;
import client.ZliClient;
import client.ZliClientUI;
import common.ChangeScene;
import common.Message;
import common.Time;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * GUI Controller of Delivery
 */
public class DeliveryController implements Initializable {

	@FXML
	private ComboBox<String> storeComboBox;

	@FXML
	private Button XBtn;

	@FXML
	private ComboBox<String> supplyComboBox;

	@FXML
	private Text priceTXT;

	@FXML
	private FontAwesomeIconView HelpBtn;

	@FXML
	private Button proceedBTN;

	@FXML
	private Text errorTXT;

	@FXML
	private Pane deliveryPANE;

	@FXML
	private TextField cityTXT;

	@FXML
	private TextField receiverTXT;

	@FXML
	private TextField phoneTXT;

	@FXML
	private TextField streetTXT;

	@FXML
	private TextField houseNumTXT;

	@FXML
	private HBox dateTimeHBOX;

	@FXML
	private JFXDatePicker dateJFX;

	@FXML
	private JFXTimePicker timeJFX;

	@FXML
	private Text dateTimeStar;

	private ChangeScene scene = new ChangeScene();
	private ObservableList<String> stores = FXCollections.observableArrayList("KIRYAT_ATA", "KARMIEL", "HAIFA",
			"TEL_AVIV", "BEER_SHEVA");
	private ObservableList<String> supplyMethods = FXCollections.observableArrayList("Store Pick Up",
			"Standard Delivery", "Priority Delivery");

	/**
	 * initialize()
	 * This method is called when the screen "Create Sale" is calleed
	 * it initializes all of our Logic\Buttons\etc...
	 */   
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		errorTXT.setText("");
		storeComboBox.setItems(stores);
		supplyComboBox.setItems(supplyMethods);
		deliveryPANE.setVisible(false);
		dateTimeHBOX.setVisible(false);
		dateTimeStar.setVisible(false);
		timeJFX.setValue(LocalTime.NOON);
		System.out.println(timeJFX.getValue());

	}

	/**
	 * supplyMethodChoosen()
	 *	his method is called when use has choosen one of the
	 * option's in the SupplyComboBox we enable/disable the Pane that holds the
	 * Personal info that is needed only for Delivery we also enable/disable the
	 * HBOX that holds the Delivery Time and Date in case user picked "Priorty
	 * Delivery" case "Store Pick Up": we disable the deliveryPane and enable the
	 * Time and Date picker case "Standard Delivery": we enable the deliverPane and
	 * enable the Time and Date picker default "Priorty Delivery": we enable the
	 * DeliveryPane and disable the Time and Date picker
	 * @param event ActionEvent when button clicked
	 */
	@FXML
	void supplyMethodChoosen(ActionEvent event) {
		switch (supplyComboBox.getValue()) {
		case "Store Pick Up":
			deliveryPANE.setVisible(false);
			dateTimeHBOX.setVisible(true);
			dateTimeStar.setVisible(true);
			break;
		case "Standard Delivery":
			deliveryPANE.setVisible(true);
			dateTimeHBOX.setVisible(true);
			dateTimeStar.setVisible(true);
			break;
		default:
			deliveryPANE.setVisible(true);
			dateTimeHBOX.setVisible(false);
			dateTimeStar.setVisible(false);
			break;
		}

	}

	/**
	 * testComboBox()
	 *  this method checks if the user has selected both Supply and
	 * Store options, if not we return False otherwaise we return True
	 * @return true if both values are !=null else False
	 */
	private boolean testComboBox() {
		return supplyComboBox.getValue() != null && storeComboBox.getValue() != null;
	}

	/**
	 * testDateAndTime()
	 *  this method checks if the user has selected Date and Time
	 * if the user Did select Date and Time we compare the Date and Time he selected
	 * to the current time if The difference between (User Selected time - current
	 * system time less than 180 we return FALSE other waise user has selected a legal time
	 * @return True if test passed,else False
	 */
	private boolean testDateAndTime() {
		if (dateJFX.getValue() == null || timeJFX.getValue() == null) {
			errorTXT.setText("You must pick date and time!");
			return false;
		}
		String timeAndDate = dateJFX.getValue().toString() + " " + timeJFX.getValue().toString() + ":00";
		Long diff = Time.calculateTimeDiff(timeAndDate);
		if (diff <= 180) {
			errorTXT.setText("Time and Date Differnce MUST be higher then 3 Hours from current time!");
			return false;
		}
		else if (diff > 60*24*21)
		{
			errorTXT.setText("Maximum time of delivery is 3 weeks from now!! Please pick a diffrent time and date");
			return false;
		}
		return true;

	}

	/**
	 * AbstractTest(String testAddress, int highBound, String TextField, String charBound,String ErrorMsg)
	 *  This method is "Abstract" for testing various types of TXT fields 
	 * @param testAddress the TXT varliable we would like to be tested
	 * @param highBound the char limit for the TXT field we are checking
	 * @param TextField String to check
	 * @param charBound what is "legal" for the testAddress to contain
	 * @param ErrorMsg Error to present to user if test failed
	 * @return True/False if the test passed/failed
	 */
	private boolean AbstractTest(String testAddress, int highBound, String TextField, String charBound,
			String ErrorMsg) {
		if (testAddress.length() == 0 || testAddress.length() > highBound) {
			errorTXT.setText(TextField + " cannot be empty and Max length is " + highBound);
			return false;
		} else if (!checkString(charBound, testAddress)) {
			errorTXT.setText(ErrorMsg);
			return false;
		}
		return true;
	}

	/**
	 * testInfoFields()
	 *  this method test all the TXT fields that are related to the
	 * "Info" section in delivery,so every option but "Store Pick Up" this Method
	 * just calls AbstractTest(), we send the corresponding correct variables
	 * 1)First we check cityTXT , 2)check streetTXT , 3)check houseNum , 4)check
	 * reicverTXT , 5)check phoneTXT if all fields are with legal input, we return
	 * True otherwise if some field is not legal we return FALSE
	 * @return boolean True if all test passed,False if atleast 1 failed
	 */
	private boolean testInfoFields() {
		if (!AbstractTest(cityTXT.getText(), 10, "City", "^[ A-Za-z]+$",
				"City can only contain English letters and Space"))
			return false;
		if (!AbstractTest(streetTXT.getText(), 15, "Street", "^[ A-Za-z]+$",
				"Street can only contain English letters and Space"))
			return false;
		if (!AbstractTest(houseNumTXT.getText(), 3, "House", "[0-9]+$", "House can only contain Numbers"))
			return false;
		if (!AbstractTest(receiverTXT.getText(), 20, "Receiver Name", "^[ A-Za-z]+$",
				"Receiver Name cannot be empty and can only contain English letters and Space"))
			return false;
		if (!AbstractTest(phoneTXT.getText(), 10, "Phone Number", "[0-9]+",
				"Phone Number cannot be empty and can only cotain numbers!"))
			return false;
		return true;
	}

	/**
	 * checkString(String toCheck, String pattern)
	 * This functions checks if toCheck string is only made of String pattern elements
	 * if yes we return True, else False
	 * @param toCheck user passes us the string he wants checked
	 * @param matchingChars the pattern The user would like to be comapre in the toCheck string
	 * @return True or False depending on success or failiure
	 **/
	boolean checkString(String matchingChars, String toCheck) {
		Pattern p = Pattern.compile(matchingChars);
		Matcher m = p.matcher(toCheck);
		return m.matches();
	}

	/**
	 * Help(MouseEvent event)
	 * Displays a Prompt screen to user with Information regarding the current screen
	 * @param event mouse clicked on "?" icon
	 **/
	@FXML
	void Help(MouseEvent event) {
		scene.HelpMessage(
				"You can choose 3 types of Supply methods:\n1)Store Pick up\t2)Standart Delivery\t3)Priority Delivery"
						+ "\n\nPriority Delivery shipping speed is at Maximum 3 hours (From manager approval time)"
						+ "\n\nIf you choose a shipping option you MUST provide  Shipping Address, Reciver full Name, and Phone Number!"
						+ "\n\nIf your Delivery exceeds the time you have choosen you will be granted a FULL REFUND of your order total!"
						+ "\n\nMaximum time of delivery is 3 weeks from current time"
						+ "\n\nMinimum time of delivery except priorty delivery is 3 hours from current time");
	}

	/**
	 * finishDelivery()
	 *  Finish delivery is linked to the "Proceed to payment" button
	 * When user clicks the button we run all the test to see if user has enterd all
	 * the needed information We also check to see if the information is "Legal", if
	 * all the test come back positive We create a new Instance of Delivery with the
	 * relelvent delivery data, update The cart with our Delivery And close the
	 * "Delivery" scene.
	 * @param event when user clicks on Finish Delivery
	 */
	@FXML
	void finishDelivery(ActionEvent event) {
		// Runing test to test valid and legal information entered by user
		if (!testComboBox()) {
			errorTXT.setText("You MUST pick a Store and Supply method");
			return;
		}

		if (!supplyComboBox.getValue().equals("Priority Delivery")) {
			if (!testDateAndTime()) {
				return;
			}
		}
		if (!supplyComboBox.getValue().equals("Store Pick Up")) {
			if (!testInfoFields())
				return;
		}
		// All test have passed we create the instace of our Delivery based on users
		// input
		Delivery newDelivery;
		String address;
		if (!supplyComboBox.getValue().equals("Store Pick Up"))
			address = cityTXT.getText() + " " + streetTXT.getText() + " " + houseNumTXT.getText();
		else
			address = null;
		String estimatedDate;
		if (!supplyComboBox.getValue().equals("Priority Delivery"))
			estimatedDate = dateJFX.getValue().toString() + " " + timeJFX.getValue().toString();
		else
			estimatedDate = null;
		// Run a query to get the next Legal Delivery Primary Key
		ZliClientUI.chat.accept(new Message(Task.Get_nextOrderID, null));
		int nextOrderID = ZliClient.cartController.getNextOrderID();
		// Depens on the user selection we have Overloaded contractors for each Supply
		// method
		switch (supplyComboBox.getValue()) {
		case "Priority Delivery":
			newDelivery = new Delivery(nextOrderID, address, receiverTXT.getText(), phoneTXT.getText());
			break;
		case "Standard Delivery":
			newDelivery = new Delivery(nextOrderID, address, receiverTXT.getText(), phoneTXT.getText(), estimatedDate);
			break;
		default:
			newDelivery = null;
			ZliClient.cartController.setPickUpDate(estimatedDate);
			break;
		}
		// Save the delivery instance for future screens
		ZliClient.cartController.setDelivery(newDelivery);
		// Used for future screens (Payment)
		ZliClient.cartController.setSupplyMethod(getSupplyMethod());
		// Saving the Store user has selected
		ZliClient.cartController.setStore(Store.valueOf(storeComboBox.getValue()));
		// open payment screen
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		scene.changeScreen(new Stage(), "/GuiClientScreens/Payment.fxml", true);

	}
	/**
	 * SupplyMethod getSupplyMethod()
	 * Returns the correct Enums according to user selected Supply Method
	 * @return returns Enum of SupplayMethod
	 **/
	SupplyMethod getSupplyMethod() {
		switch (supplyComboBox.getValue()) {
		case "Store Pick Up":
			return SupplyMethod.Store_PickUp;
		case "Standard Delivery":
			return SupplyMethod.Standard_Delivery;
		default:
			return SupplyMethod.Priority_Delivery;
		}
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
	 * Back(MouseEvent event)
	 * Returns the user the the previous screen while closing current screen
	 * @param event mouse clicked on Back Icon
	 **/
	@FXML
	void Back(MouseEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		scene.changeScreen(new Stage(), "/GuiClientScreens/GreetingCardController.fxml", true);
	}

}
