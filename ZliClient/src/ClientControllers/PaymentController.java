package ClientControllers;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Entities.AbstractProduct;
import Entities.Costumer;
import Entities.Delivery;
import Entities.Order;
import Entities.Product;
import Entities.User;
import Enums.PaymentMethod;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This class holds the logic for Payment screen
 * @author Ilya
 */
public class PaymentController implements Initializable {

	@FXML
	private Button PlaceOrderBTN;

	@FXML
	private Label orderTotalTXT;

	@FXML
	private Button XBtn;

	@FXML
	private FontAwesomeIconView XIcon;

	@FXML
	private ScrollPane cartScrollPane;

	@FXML
	private VBox cartVBOX;

	@FXML
	private Label deliveryPriceTXT;

	@FXML
	private FontAwesomeIconView BackBtn;

	@FXML
	private Text storeTXT;

	@FXML
	private Text supplyMethodTXT;

	@FXML
	private Text supplyDateTXT;

	@FXML
	private Text addressTXT;

	@FXML
	private Text nameTXT;

	@FXML
	private Text phoneTXT;

	@FXML
	private Text expTXT;

	@FXML
	private Text cvvTXT;

	@FXML
	private TextField storeCreditTXT;

	@FXML
	private ComboBox<String> paymentComboBox;

	@FXML
	private HBox creditCardHBOX;

	@FXML
	private TextField cardAmountTXT;

	@FXML
	private HBox creditHBOX;

	@FXML
	private TextField creditAmountTXT;

	@FXML
	private TextField ccTXT;

	@FXML
	private Text errorTXT;

	@FXML
	private TextField emailTXT;

	private String emailAddress = "";
	private ChangeScene scene = new ChangeScene();
	private float totalPrice = 0;
	private Costumer costumer;
	private int productInCartCnt = 0;
	private float credit;
	private ArrayList<AbsProductInCartController> List = new ArrayList<>();
	private ObservableList<String> paymentOption = FXCollections.observableArrayList("Credit Card", "Store Credit",
			"Split CC and Credit");
	/**
	 * initialize()
	 * this method is called when the Payment screen is presenet to user
	 * we set all of the logic\info\comboBox etc..
	 * Then we iterate over the Cart AbstractProducts and add them to the Cart in the right side of the screen
	 */ 
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cartScrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
		ObservableList<AbstractProduct> cart = ZliClient.cartController.getCart();
		if (cart.size() != 0) {
			for (int i = 0; i < cart.size(); i++)
				addToCart(cart.get(i));
		}
		deliveryPriceTXT.setText("");
		updateOrderTotal();
		ZliClientUI.chat.accept(new Message(Task.Get_Customer_first_order_status,
				ZliClient.cartController.getCostumer().getCostumerID()));
		if (ZliClient.cartController.isFirstOrder()) // if it's the first order of the client, we add a 20% discount
		{
			float discountAmount = (float) (-1 * (totalPrice * 0.2));
			addToCart(new Product("20% Discount", "/GuiAssests/discount1.png", discountAmount));
			totalPrice = 0;
			updateOrderTotal();
			ZliClient.cartController.setDiscountAmount(discountAmount);
		}
		paymentComboBox.setItems(paymentOption);
		creditCardHBOX.setVisible(false);
		creditHBOX.setVisible(false);
		ccTXT.setEditable(false);
		storeCreditTXT.setEditable(false);
		setUpInfo();
		errorTXT.setText("");
	}

	public void setUpInfo() {
		ZliClientUI.chat.accept(new Message(Task.Get_Costumer_By_User_Name, ZliClient.userController.getUser()));
		costumer = ZliClient.cartController.getCostumer();
		String supplyMethod = ZliClient.cartController.getSupplyMethod().toString();
		Delivery tmp = ZliClient.cartController.getDelivery();
		User user = ZliClient.userController.getUser();
		storeTXT.setText(ZliClient.cartController.getStore().toString());
		supplyMethodTXT.setText(ZliClient.cartController.getSupplyMethod().toString());

		if (supplyMethod.equals("Store_PickUp")) {
			nameTXT.setText(user.getFirstName() +" " + user.getLastName());
			phoneTXT.setText(user.getPhoneNumber());
			addressTXT.setText(storeTXT.getText());
			supplyDateTXT.setText(ZliClient.cartController.getPickUpDate());

		} else {
			nameTXT.setText(tmp.getReceiverName());
			phoneTXT.setText(tmp.getReceiverPhoneNumber());
			addressTXT.setText(tmp.getAddress());
			if (!supplyMethod.equals("Standard_Delivery"))
				supplyDateTXT.setText("Up to 3 hours\nfrom Manager Approval");
			else
				supplyDateTXT.setText(tmp.getEstimatedDeliveryDate());
		}
		ccTXT.setText(costumer.getCreditCard());
		expTXT.setText(costumer.getExpirationDate());
		cvvTXT.setText(costumer.getCvv());
		storeCreditTXT.setText(costumer.getStoreCredit());

	}
	/**
	 * addToCart(AbstractProduct product)
	 * We add all of the user Cart products\items into the cart in the screen
	 * @param product the product to add to the cart
	 **/
	public void addToCart(AbstractProduct product) {
		for (AbsProductInCartController Element : List) { // if product already exists in cart, add one to amount and
			// return
			if (Element.getProductID() == product.getId()) {
				Element.increaseAmount();
				return;
			}
		}

		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("/GuiClientScreens/AbstractProductInSummry.fxml"));
		try {
			cartVBOX.getChildren().add(fxmlLoader.load());
			List.add(fxmlLoader.getController());
			List.get(productInCartCnt).setData(product, productInCartCnt++);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	/**
	 * updateOrderTotal()
	 * Update the order total to the user,calculates the sum of all the products in the cart
	 * if there is an 20% sale for first purchase we will apply it
	 **/
	public void updateOrderTotal() {
		for (AbsProductInCartController tmp : List)
			totalPrice += tmp.getTotalPrice();
		if (!ZliClient.cartController.getSupplyMethod().equals(SupplyMethod.Store_PickUp)) {
			totalPrice += Delivery.deliveryPrice;
			deliveryPriceTXT.setText("Delivery Price: $" + String.format("%.02f", Delivery.deliveryPrice));
		}
		totalPrice = (float) Math.round(totalPrice);
		String formatString = String.format("%.02f", totalPrice);
		orderTotalTXT.setText("Order Total: " + "$" + formatString);
	}
	/**
	 * Back(MouseEvent event)
	 * Returns the user the the previous screen while closing current screen
	 * @param event mouse clicked on payment option combobox option
	 **/
	@FXML
	void paymentOptionChoosen(ActionEvent event) {
		switch (paymentComboBox.getValue()) {
		case "Credit Card":
			creditCardHBOX.setVisible(true);
			creditHBOX.setVisible(false);
			cardAmountTXT.setText(String.valueOf(totalPrice));
			cardAmountTXT.setEditable(false);
			break;
		case "Store Credit":
			creditHBOX.setVisible(true);
			creditCardHBOX.setVisible(false);
			creditAmountTXT.setText(String.valueOf(totalPrice));
			creditAmountTXT.setEditable(false);
			break;
		default:
			creditHBOX.setVisible(true);
			creditCardHBOX.setVisible(true);
			creditAmountTXT.setEditable(true);
			cardAmountTXT.setEditable(true);
			creditAmountTXT.setText("");
			cardAmountTXT.setText("");
			break;
		}

	}
	/**
	 * Back(MouseEvent event)
	 * Returns the user the the previous screen while closing current screen
	 * @param event mouse clicked on Back Icon
	 **/
	@FXML
	void Back(MouseEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		scene.changeScreen(new Stage(), "/GuiClientScreens/Delivery.fxml", true);

	}
	/**
	 * testSplit()
	 * This method Checks if user provided the correct sum in Credit Card +Store credit
	 * so that it is equals to the order total
	 * @return boolean True\False depending on success\failiure of test
	 **/
	private boolean testSplit() {
		if (cardAmountTXT.getText().length() == 0 || creditAmountTXT.getText().length() == 0) {
			errorTXT.setText("You must Enter Credit Card and  Store Credit Value!");
			return false;
		}

		if (!checkString(cardAmountTXT.getText()) || !checkString(creditAmountTXT.getText())) {
			errorTXT.setText("You Can only enter 0-9 and [.]");
			return false;
		}

		float cardAmount, creditAmount;
		cardAmount = Float.parseFloat(cardAmountTXT.getText());
		creditAmount = Float.parseFloat(creditAmountTXT.getText());
		if (cardAmount == 0 || creditAmount == 0) {
			errorTXT.setText("You must enter a Value greater then 0 in both options!");
			return false;
		}
		if (creditAmount > credit) {
			errorTXT.setText("You dont have that much store credit!");
			return false;
		}
		if (creditAmount + cardAmount != totalPrice) {
			String price = String.format("%.02f", totalPrice);
			errorTXT.setText("Total price is " + price + "$ Please make sure your sum add's to it!");
			return false;
		}

		return true;

	}
	/**
	 * placeOrder(ActionEvent event)
	 * This method is called when use clicks "Place Order"
	 * It runs all the test related to all of the info fields (email,payment options,etc..)
	 * if all test passed we generate a new Order query into the DB
	 * if the user selected an Delivery option we also generate a new Dlivery query into the DB
	 * we then generate and Email Send query to the DB
	 * Lastly we Promt the a screen to the user with order confirmation  and switch the screen to Costumer portal
	 * @param event   happens when mouse is clicked on place order button
	 **/
	@FXML
	void placeOrder(ActionEvent event) {
		if (paymentComboBox.getValue() == null) {
			errorTXT.setText("You must select a Payment option");
			return;
		}
		credit = Float.parseFloat(costumer.getStoreCredit());
		switch (paymentComboBox.getValue()) {
		case "Store Credit":
			if (credit < totalPrice) {
				errorTXT.setText("You dont have enough store credit!");
				return;
			}
			break;
		case "Split CC and Credit":
			if (testSplit())
				break;
			else
				return;
		default:
			break;
		}
		if (emailTXT.getText().length() == 0 || !isEmailAddressValid(emailTXT.getText())) {
			errorTXT.setText("Please Enter a VALID Email address");
			return;
		}
		emailAddress = emailTXT.getText();
		String deliveryDate = "";
		if (!ZliClient.cartController.getSupplyMethod().equals(SupplyMethod.Priority_Delivery))
			deliveryDate = supplyDateTXT.getText();
		else
			deliveryDate = null;
		Order newOrder = new Order(ZliClient.cartController.getNextOrderID(),
				ZliClient.cartController.getCostumer().getCostumerID(), totalPrice,
				ZliClient.cartController.getGreetingCard(), ZliClient.cartController.getComponents(),
				ZliClient.cartController.getStore(), deliveryDate, Time.formatLocalDate(),
				ZliClient.cartController.getSupplyMethod(), getPaymentMethod());

		ZliClientUI.chat.accept(new Message(Task.Insert_newOrder, newOrder));

		if (!paymentComboBox.getValue().equals("Credit Card")) // update customer store credit in database
		{
			ArrayList<Integer> arr = new ArrayList<>();
			arr.add(ZliClient.cartController.getCostumer().getCostumerID());
			arr.add((int) (float) Float.valueOf(creditAmountTXT.getText()));
			arr.add((int) (float) Float.valueOf(costumer.getStoreCredit()));
			ZliClientUI.chat.accept(new Message(Task.Updated_Customer_Store_Credit, arr));
		}

		Delivery delivery = ZliClient.cartController.getDelivery();
		if (delivery != null) // insert new delivery into zli.delivery in database
			ZliClientUI.chat.accept(new Message(Task.Insert_newDelivery, delivery));

		sendOrderEmailToClient();
		ZliClient.cartController.ClearWhenOrderEnds();
		scene.HelpMessage("Order Completed successfully,Now awaiting manager approval\nAn Email with the Order info will be send shortly :)\nThank you for youre purchase!");
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		scene.changeScreen(new Stage(), "/GuiClientScreens/CostumerMenu.fxml", true);
	}
	/**
	 * checkString(String toCheck)
	 * This method checks of toCheck string is made of of only 0-9 and dot
	 * @param toCheck user passes us the string he wants checked
	 * @return boolean True\False depending on success\failiure of test
	 **/
	private boolean checkString(String toCheck) {
		Pattern p = Pattern.compile("[0-9.]+");
		Matcher m = p.matcher(toCheck);
		return m.matches();
	}
/**
 * getPaymentMethod()
 * Returns the correct Enum according to user selected Payment Method ComboBox value
 * "Credit Card"  PaymentMethod.Credit_Card
 * "Store Credit" PaymentMethod.Store_Credit
 * "Store Credit" PaymentMethod.Store_Card_Credit
 * @return PaymentMethod enum
 */
	private PaymentMethod getPaymentMethod() {
		switch (paymentComboBox.getValue()) {
		case "Credit Card":
			return PaymentMethod.Credit_Card;
		case "Store Credit":
			return PaymentMethod.Store_Credit;
		default:
			return PaymentMethod.Store_Card_Credit;
		}
	}

	/**
	 * sendOrderEmailToClient()
	 *  This method request the server side to send an email
	 * to the cleint provided email address The email body is the Cart that he
	 * pruchased +total price and greeting
	 */
	private void sendOrderEmailToClient() {
		ArrayList<String> info = new ArrayList<>();
		info.add(emailAddress);
		info.add(ZliClient.cartController.toString());
		ZliClientUI.chat.accept(new Message(Task.Send_Email, info));
	}

	/**
	 * isEmailAddressValid(String email) This method tests the email the user
	 * provided it has to be of form: email@email.com
	 * 
	 * @param email the user provided email string
	 * @return boolean true if test passed, false if didnt pass
	 */
	private boolean isEmailAddressValid(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";

		Pattern pat = Pattern.compile(emailRegex);
		if (email == null)
			return false;
		return pat.matcher(email).matches();
	}

	/**
	 * Help(MouseEvent event) Displays a Prompt screen to user with Information
	 * regarding the current screen
	 * 
	 * @param event mouse clicked on "?" icon
	 **/
	@FXML
	void Help(MouseEvent event) {
		scene.HelpMessage("*You MUST pick a Payment option!\n\n"
				+ "*If you pick Credit Card as your payment option you will be charged"
				+ " the full price of the order via your Credit Card!\n\n"
				+ "*If you pick Store Credit as your payment option you will be charged"
				+ " the full price of the order via your Store Credit!\n\n"
				+ "*You can only use the amount of Store Credit available in your account!!\n\n"
				+ "*If you pick Split CC and Credit as your payment option, you will be able"
				+ " to pay with both Credit Card and Store Credit!\n\n"
				+ "*In order to use Split CC and Credit option you MUST enter a value greater than zero in "
				+ " both Credit Card and Store Credit!\n\n"
				+ " If it's your first order, you are entitled for 20% discount from the total order price");
	}

	/**
	 * X(ActionEvent event) Closes the applications and sends a Request_disconnected
	 * to server side
	 * 
	 * @param event mouse clicked on Back Icon
	 **/
	@FXML
	void clickedX(MouseEvent event) {
    	ZliClientUI.chat.accept(new Message(Task.Request_logout, ZliClient.userController.getUser().getUsername()));
		ZliClientUI.chat.accept(new Message(Task.Request_disconnected, null));
		System.exit(0);
	}
}
