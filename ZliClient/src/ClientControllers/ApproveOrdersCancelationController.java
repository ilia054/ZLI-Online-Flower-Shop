package ClientControllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import Entities.Order;
import Enums.OrderStatus;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * The class ApproveOrdersCancelationController is the Controller of "ApproveOrdersCancelation" Screen
 */
public class ApproveOrdersCancelationController {

    @FXML
    private Button XBtn;

    @FXML
    private FontAwesomeIconView refreshBTN;

    @FXML
    private FontAwesomeIconView saveBTN;

    @FXML
    private TableView<Order> OrderTable;

    @FXML
    private TableColumn<Order, Integer> orderNumber;

    @FXML
    private TableColumn<Order, Integer> costumerID;

    @FXML
    private TableColumn<Order, Float> Price;

    @FXML
    private TableColumn<Order, String> estimatedDeliveryDate;

    @FXML
    private TableColumn<Order, String> orderDate;

    @FXML
    private TableColumn<Order, OrderStatus> orderStatus;
    
    @FXML
    private Label storeName;

    @FXML
    private FontAwesomeIconView BackBtn;
    
    private ChangeScene scene = new ChangeScene();
    
    private ObservableList<Order> orders = FXCollections.observableArrayList();
    private OrderStatus[] orderStatusArray = new OrderStatus[] {OrderStatus.CANCELED,  OrderStatus.REQUEST_TO_CANCELED};
    Set<Order> orderChangedSet = new HashSet<>();
    List<Order> orderChangedList;
    
	/**
	 * This method initiate the screen of the relevance FXML.
	 */
	@SuppressWarnings("static-access")
	@FXML
	public void initialize() {
		storeName.setVisible(false);
		setColumnsInTable();
		// This method is requesting our Order data from the Server
		ZliClientUI.chat.accept(new Message(Task.Import_orderstocancel_data_by_store, ZliClient.userController.getUser()));
		orders = ZliClient.orderController.getOrders();
		OrderTable.setItems(orders);
		createDropBoxInCell(orderStatus);
		setEditableOrderStatusCell(orderStatus);
		OrderTable.autosize();
		OrderTable.setEditable(true);
		storeName.setVisible(false);
		storeName.setVisible(true);
		storeName.setText(ZliClient.userController.getUser().getStore());
	}
	
	/**
	 * This method create Drop Box In order status cell in the table on the screen.
	 * @param Column
	 */
	private void createDropBoxInCell(TableColumn<Order, OrderStatus> Column) {
		orderStatus.setCellFactory((param) -> new ComboBoxTableCell<>(new StringConverter<OrderStatus>() {

			@Override
			public String toString(OrderStatus object) {
				return object.toString();
			}

			@Override
			public OrderStatus fromString(String string) {
				return OrderStatus.valueOf(string);
			}

		}, orderStatusArray));
	}

	/**
	 * This method set the columns of the table on the screen.
	 */
	private void setColumnsInTable() {
		orderNumber.setCellValueFactory(new PropertyValueFactory<Order, Integer>("orderNumber"));
		costumerID.setCellValueFactory(new PropertyValueFactory<Order, Integer>("costumerID"));
		Price.setCellValueFactory(new PropertyValueFactory<Order, Float>("price"));
		estimatedDeliveryDate.setCellValueFactory(new PropertyValueFactory<Order, String>("estimatedDeliveryDate"));
		orderDate.setCellValueFactory(new PropertyValueFactory<Order, String>("orderDate"));
		orderStatus.setCellValueFactory(new PropertyValueFactory<Order, OrderStatus>("orderStatus"));
	}

	/**
	 * This method count and save the orders that changed in the table on screen.
	 * @param orderStatus
	 */
	private void setEditableOrderStatusCell(TableColumn<Order, OrderStatus> orderStatus) {
		orderStatus.setOnEditCommit(event -> {
			((Order) event.getRowValue()).setOrderStatus(event.getNewValue());
			orderChangedSet.add((Order) event.getRowValue());
		});
		
	}
	
	/**
	 * This method terminates ApproveOrdersCancelation scene and opens the StoreMangerMenu
	 * @param event mouse clicked on Back button
	 */
    @FXML
    void Back(MouseEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		// scene.BackBtnOnManageOrders(new Stage());
		scene.changeScreen(new Stage(), "/GuiClientScreens/StoreManagerMenu.fxml", true);
    }

	/**
	 * Closes the applications and sends a Request_disconnected to server side
	 * @param event mouse clicked on X Icon
	 **/
    @FXML
    void X(ActionEvent event) {
    	ZliClientUI.chat.accept(new Message(Task.Request_logout, ZliClient.userController.getUser().getUsername()));
		ZliClientUI.chat.accept(new Message(Task.Request_disconnected, null));
		System.exit(0);
    }

	/**
	 * This method refresh the table according to the database.
	 * @param event mouse clicked on refreshed button
	 */
    @SuppressWarnings("static-access")
	@FXML
    void refreshedClicked(MouseEvent event) {
		setColumnsInTable();
		ZliClientUI.chat.accept(new Message(Task.Import_orderstocancel_data_by_store, ZliClient.userController.getUser()));
		orders = ZliClient.orderController.getOrders();
		OrderTable.setItems(orders);
		createDropBoxInCell(orderStatus);
		setEditableOrderStatusCell(orderStatus);
		OrderTable.autosize();
		OrderTable.setEditable(true);
    }

	/**
	 * This method save the changes has been done by the user on table on database.
	 * @param event mouse clicked on save button
	 */
    @FXML
    void saveClicked(MouseEvent event) {
    	ArrayList<String> mail = new ArrayList<String>();
    	orderChangedList = new ArrayList<Order>();
    	orderChangedList.addAll(orderChangedSet);
		Message message = new Message(Task.Update_orders_cancelation, orderChangedList);
		ZliClientUI.chat.accept(message);
		@SuppressWarnings("static-access")
		ObservableList<String> emails = ZliClient.complaintEmail.getString();
		int size=emails.size();
		if(size>0)
		{
			for (int i=0;i<size;i++) {
		    	StringBuilder str=new StringBuilder();
				mail.add(emails.get(i++).toString());
				
				str.append("Dear "+emails.get(i++).toString()+ 
						"\nYour order Number: "+emails.get(i++).toString()
						+" that you ordered at " +emails.get(i++)
						+"\nHas Been Cancaled at store:"+ emails.get(i++).toString()+"\nAnd You have recived:$"
						+emails.get(i)+" As store credit\nIn order to get further information please contact ZLI customer service"
						+ "\nThanks from Zli Store Manager");

				
				
				mail.add(str.toString());
				ZliClientUI.chat.accept(new Message(Task.Send_Email, mail));
				mail.removeAll(mail);
				}
		}
		orderChangedSet.removeAll(orderChangedSet);
		scene.HelpMessage("The changes have been saved successfully");
    }

}

