// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import javafx.collections.FXCollections;
import java.io.*;
import java.util.ArrayList;
import Entities.AbstractProduct;
import Entities.Complaint;
import Entities.Costumer;
import Entities.MonthlyIncomesReport;
import Entities.MonthlyOrdersReport;
import Entities.Order;
import Entities.Product;
import Entities.QuarterlySatisfactionReport;
import Entities.Sale;
import Entities.Storestaff;
import Entities.User;
import EntityControllers.CartController;
import EntityControllers.CatalogItemController;
import EntityControllers.CatalogProductController;
import EntityControllers.ComplaintController;
import EntityControllers.CostumerController;
import EntityControllers.IdOrderController;
import EntityControllers.MarketingEmployeeController;
import EntityControllers.MonthlyIncomesReportController;
import EntityControllers.MonthlyOrdersReportController;
import EntityControllers.StringController;
import EntityControllers.OrderController;
import EntityControllers.SalesController;
import EntityControllers.StorestaffController;
import EntityControllers.UserController;

/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 */
public class ZliClient extends AbstractClient {
	// Instance variables ****************

	/**
	 * The interface type variable. It allows the implementation of the display
	 * method in the client.
	 */
	public static ClientCommu client;
	private ChatIF clientUI;
	private boolean waitForResponse = false;
	public static OrderController orderController = new OrderController();
	public static UserController userController = new UserController();
	public static CatalogProductController catalogProductController = new CatalogProductController();
	public static CatalogItemController catalogItemController = new CatalogItemController();
	public static UserController manageOtherUserController = new UserController();
	public static CostumerController costumerController = new CostumerController();
	public static StorestaffController storestaffController = new StorestaffController();
	public static IdOrderController idordercontroller = new IdOrderController();
	public static CartController cartController = new CartController();
	public static ComplaintController complaintController = new ComplaintController();
	public static StringController monthbyYearController = new StringController();
	public static StringController storeNamefromsurveyController = new StringController();
	public static StringController permission = new StringController();
	public static StringController aprrovedCustumer = new StringController();
	public static MonthlyIncomesReportController monthlyIncomesReportController = new MonthlyIncomesReportController();
	public static EntityControllers.QuarterlySatisfactionReportController quarterlySatisfactionReportController = new EntityControllers.QuarterlySatisfactionReportController();
	public static MonthlyOrdersReportController monthlyOrdersReportController = new MonthlyOrdersReportController();
	public static MarketingEmployeeController marketingEmployeeController = new MarketingEmployeeController();
	public static SalesController salesController = new SalesController();
	public static StringController emails = new StringController();//////maybe
	public static StringController emailsCancelation = new StringController();
	public static StringController analyzeID = new StringController();
	public static StringController complaintEmail= new StringController();
	/**
	 * Constructs an instance of the chat client.
	 * @param host     The server to connect to.
	 * @param port     The port number to connect on.
	 * @param clientUI The interface type variable.
	 * @throws IOException when something happens
	 */
	public ZliClient(String host, int port, ChatIF clientUI) throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
	}
	
	/**
	 * This method handles the packet that is received from the ZLIServer There are
	 * 2 types that can be received, String and Message In the below Switch case we
	 * break down the Object that is sent by the server and act accordingly in the
	 * Client side
	 * @param msg The message from the server
	 */
	@SuppressWarnings({ "static-access", "unchecked" })
	public void handleMessageFromServer(Object msg) {
		// While we wait for the server to respond to our server
		waitForResponse = false;

		Message msgFromServer = (Message) msg;
		switch (msgFromServer.getTask()) {
		case Connected:
			break;
		case Disconnected:
			break;
		case Update_orders_successfully:
			break;
		case Logged_Successful:
			User user = (User) msgFromServer.getObject();
			userController.setUser(user);
			break;
		case Logged_UnSuccessful_already_logged_in:
			userController.setUser((User) msgFromServer.getObject());
			break;
		case Logged_UnSuccessful:
			userController.setUser(null);
			break;
		case Logged_out_Successful:
			userController.setUser(null);
			break;
		case Get_ProductCatalog_table:
			catalogProductController.setProductCatalog(
					FXCollections.observableArrayList((ArrayList<Product>) msgFromServer.getObject()));
			break;
		case Get_nextProductID:
			cartController.setNextProductID((int) msgFromServer.getObject());
			break;
		case Order_data_imported:
			orderController.setOrders(FXCollections.observableArrayList((ArrayList<Order>) msgFromServer.getObject()));
			break;
		case Cancel_request_successfully:
			break;
		case Got_store_user:
			userController.getUser().setStore((String) msgFromServer.getObject());
			break;
		case Update_surveys_successfully:
			break;
		case Update_complaints_successfully:
			complaintEmail.setString(FXCollections.observableArrayList((ArrayList<String>) msgFromServer.getObject()));
			break;
		case Costumer_data_was_imported:
			manageOtherUserController.setUser((User) msgFromServer.getObject());
			break;
		case Costumer_creditcard_was_updated:
			break;
		case OrdersBy_id_imported:
			idordercontroller
					.setIdOrders(FXCollections.observableArrayList((ArrayList<Integer>) msgFromServer.getObject()));
			break;
		case Costumers_approved_and_frozen_was_imported:
			costumerController.setApprovedOrFrozenCostumers(
					FXCollections.observableArrayList((ArrayList<Costumer>) msgFromServer.getObject()));
		case Update_costumer_successfully:
			break;
		case Storestaff_of_the_same_store_was_imported:
			storestaffController.setStorestaffFromTheSameStore(
					FXCollections.observableArrayList((ArrayList<Storestaff>) msgFromServer.getObject()));
			break;
		case Get_nextOrderID:
			cartController.setNextOrderID((Integer) msgFromServer.getObject());
		case Get_Complaints_table:
			complaintController
					.setComplaints(FXCollections.observableArrayList((ArrayList<Complaint>) msgFromServer.getObject()));
			break;
		case Update_Complaints_DB:
			break;
		case Got_price_by_order_id_for_complaint:
			complaintController.getComplaintToUpdateOnDB().setOrderPrice((float) msgFromServer.getObject());
			break;
		case Insert_newDelivery:
			break;
		case Insert_newOrder:
			break;
		case Get_Costumer_By_User_Name:
			cartController.setCostumer((Costumer) msgFromServer.getObject());
			break;
		case Updated_Customer_Store_Credit:
			break;
		case MonthsBy_Year_imported:
			monthbyYearController
					.setString(FXCollections.observableArrayList((ArrayList<String>) msgFromServer.getObject()));
			break;
		case Survey_Stores_Imported:
			storeNamefromsurveyController
					.setString(FXCollections.observableArrayList((ArrayList<String>) msgFromServer.getObject()));
			break;
		case Sale_Analyze_Imported:
			break;
		case User_Permission_Imported:
			permission
					.setString(FXCollections.observableArrayList((ArrayList<String>) msgFromServer.getObject()));
			break;
		case Order_Status_Changed_To_Arrived:
			String string = (String) msgFromServer.getObject();
			permission.setErrorLabel(string);
			break;
		case Send_Email:
			break;
		case Monthly_incomes_report_was_got:
			monthlyIncomesReportController.setMonthlyIncomesReport((MonthlyIncomesReport) msgFromServer.getObject());
			break;
		case Quarterly_costumer_satisfaction_report_was_got:
			quarterlySatisfactionReportController.setQuarterlySatisfactionReport(FXCollections
					.observableArrayList((ArrayList<QuarterlySatisfactionReport>) msgFromServer.getObject()));
			break;
		case Monthly_orders_report_was_got:
			monthlyOrdersReportController.setMonthlyOrdersReport((MonthlyOrdersReport) msgFromServer.getObject());
			break;
		case Quarterly_incomes_report_was_got:
			monthlyIncomesReportController.setMonthlyIncomesReports(
					FXCollections.observableArrayList((ArrayList<MonthlyIncomesReport>) msgFromServer.getObject()));
			break;
		case Get_Customer_first_order_status:
			cartController.setFirstOrder((boolean) msgFromServer.getObject());
			break;
		case Get_Product_By_ID:
			marketingEmployeeController.setProduct((AbstractProduct) msgFromServer.getObject());
			break;
		case Delete_Product_By_ID:
			break;
		case Update_Price:
			break;
		case Add_New_Product_To_Catalog:
			break;
		case Get_ActiveSales:
			salesController.setActivesales(
					FXCollections.observableArrayList((ArrayList<Sale>) msgFromServer.getObject()));
			break;
		case Add_New_Sale:
			break;
		case  Check_Active_Sale_By_ID:
			salesController.setHasSaleForProductID((boolean)msgFromServer.getObject());
			break;
		case Update_orders_successfully_and_Send_email:
			emails.setString(FXCollections.observableArrayList((ArrayList<String>)msgFromServer.getObject()));
			break;
		case orders_successfully_canceled:
			emailsCancelation.setString(FXCollections.observableArrayList((ArrayList<String>)msgFromServer.getObject()));
			break;
		case Year_And_Month_Analyze_Imported:
			break;
		case  Send_PDF:
			analyzeID.setString(FXCollections.observableArrayList((ArrayList<String>)msgFromServer.getObject()));
			break;
		default:
			break;
		}
	}

	/**
	 * This method handles all data coming from the UI
	 * @param message The message from the UI.
	 */
	public void handleMessageFromClientUI(String message) {
		try {
			openConnection();
			waitForResponse = true;
			sendToServer(message);
			// wait for response
			while (waitForResponse) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			clientUI.display("Could not send message to server.  Terminating client.");
			quit();
		}
	}

	/**
	 * This method handles all Message datatype's coming from the UI
	 * @param message The message from the server
	 */
	public void handleMessageFromClientUI(Message message) {
		try {
			openConnection();
			waitForResponse = true;
			sendToServer(message);
			// wait for response
			while (waitForResponse) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			clientUI.display("Could not send message to server.  Terminating client.");
			quit();
		}
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}
}