package ClientControllers;

import java.util.HashMap;

import Entities.Order;
import Enums.Task;
import client.ZliClient;
import client.ZliClientUI;
import common.ChangeScene;
import common.Message;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The class ManageOrdersForCostumerController is the Controller of "ManageOrdersForCostumer" Screen
 */
public class ManageOrdersForCostumerController {

	   @FXML
	    private Button LogOutBtn;

	    @FXML
	    private Button requestToCancelBtn;

	    @FXML
	    private ComboBox<Integer> orderNumberComoboBox;

	    @FXML
	    private VBox detailsVBOX;

	    @FXML
	    private Text storeTXT;

	    @FXML
	    private Text orderDateTXT;

	    @FXML
	    private Text statusTXT;

	    @FXML
	    private Text estimatedDeliveryDateTXT;

	    @FXML
	    private Text SupplyTXT;

	    @FXML
	    private Text ConfirmationDateTXT;

	    @FXML
	    private Text priceTXT;

	    @FXML
	    private Text deliveryPriceTXT;

	    @FXML
	    private Text orderDetailsTXT;

	    @FXML
	    private Button XBtn;

	    @FXML
	    private FontAwesomeIconView XIcon;
  
	    private ObservableList<Integer> Idorders = FXCollections.observableArrayList();
	    private HashMap<Integer,Order> orderMap;
	    private ChangeScene scene = new ChangeScene();
	    
		/**
		 * This method initiate the screen of the relevance FXML.
		 */
	    @SuppressWarnings("static-access")
		public void initialize() {
	    	Idorders = ZliClient.idordercontroller.getIdorders();
			ZliClientUI.chat.accept(new Message(Task.Import_order_data,null));
	    	orderMap = ZliClient.idordercontroller.getOrdersMap();
	    	detailsVBOX.setVisible(false);
	    	orderDetailsTXT.setVisible(false);
			orderNumberComoboBox.setItems(Idorders);
	    }
	    
		/**
		 * This method get order number from screen and ask the server to update the order to status request to cancel
		 * @param event mouse clicked on requestToCancel button
		 */
	    @FXML
	    void requestToCancelClicked(ActionEvent event) {
	    	ZliClientUI.chat.accept(new Message(Task.Request_to_cancel_order, orderNumberComoboBox.getValue()));
	    	scene.HelpMessage("Cancelation request for Order Number: " + orderNumberComoboBox.getValue() + " has benn successfully sumbited"
	    			+ "\n\nNow awaiting for manager Approval for cancelation");
	    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
	    	scene.changeScreen(new Stage(),"/GuiClientScreens/CostumerMenu.fxml", true);
	    }
	    
		/**
		 * This method get order number from screen and shows the order details on screen
		 * @param event order id picked on screen
		 */
	    @FXML
	    void OrederIdPicked(ActionEvent event) {
	    	detailsVBOX.setVisible(true);
	    	orderDetailsTXT.setVisible(true);
	    	setDetails();
	    }
	    
		/**
		 * This method shows the order details on screen
		 */
	    private void setDetails() {
	    	Order order = orderMap.get(orderNumberComoboBox.getValue());
	    	storeTXT.setText(order.getStore().toString().replace("_", " "));
	    	orderDateTXT.setText(order.getOrderDate());
	    	statusTXT.setText(order.getOrderStatus().toString().replace("_", " "));
	    	String edt = order.getEstimatedDeliveryDate();
	    	if(edt == null)
	    		estimatedDeliveryDateTXT.setText("No delivery date for priorty delivery");
	    	estimatedDeliveryDateTXT.setText(order.getEstimatedDeliveryDate());
	    	String sm = order.getSupplyMethod().toString();
	    	if(sm.equals("Store_PickUp"))
	    		sm = "Store Pick Up";
	    	SupplyTXT.setText(sm.replace("_", " "));
	    	String cd = order.getConfirmationDate();
	    	if(cd == null)
	    		cd = "Not confirmed yet";
	    	ConfirmationDateTXT.setText(cd);
	    	priceTXT.setText(String.valueOf(order.getPrice()));
	    	float deliveryprice;
	    	if(sm.equals("Store Pick Up"))
	    		deliveryprice = 0;
	    	else deliveryprice = 10;
	    	deliveryPriceTXT.setText(String.valueOf(deliveryprice));	   
	    }

		/**
		 * This method terminates CEOmenu scene and opens the login screen. ask server to update database that user is loged
		 * out
		 * @param event mouse clicked on LogOut button
		 * @throws Exception if ActionEvent fails
		 */
	    public void LogOut(ActionEvent event) throws Exception {
	    	ZliClientUI.chat.accept(new Message(Task.Request_logout, ZliClient.userController.getUser().getUsername()));
	    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
	    	scene.changeScreen(new Stage(),"/GuiClientScreens/LoginZLI.fxml", true);
	    }
	    
		/**
		 * Closes the applications and sends a Request_disconnected to server side
		 * @param event mouse clicked on X Icon
		 * @throws Exception if ActionEvent fails
		 **/
	    @FXML
	    public void X(ActionEvent event) throws Exception {
	    	ZliClientUI.chat.accept(new Message(Task.Request_logout, ZliClient.userController.getUser().getUsername()));
	    	ZliClientUI.chat.accept(new Message(Task.Request_disconnected, null));
	    	System.exit(0);
	    }
	    
		/**
		 * This method terminates ManageOrdersForCostumerController scene and opens the CostumerMenu screen
		 * @param event mouse clicked on Back button
		 */
	    @FXML
	    void Back(MouseEvent event) {
	    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
	    	scene.changeScreen(new Stage(),"/GuiClientScreens/CostumerMenu.fxml", true);
	    }
	    
}
