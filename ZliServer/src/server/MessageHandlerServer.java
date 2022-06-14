package server;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import Entities.Complaint;
import Entities.ConnectedClient;
import Entities.Costumer;
import Entities.Delivery;
import Entities.Order;
import Entities.ProductMyFile;
import Entities.Sale;
import Entities.Storestaff;
import Entities.User;
import Enums.OrderStatus;
import Enums.Task;
import common.Message;
import common.MyFile;
import javafx.collections.ObservableList;
import ocsf.server.ConnectionToClient;

/**
 * This class handles the messages that are passed from the clients
 */

public class MessageHandlerServer {
	/**
	 * This method is called when the client's use accept in order to pass a message
	 * to the server, we handle it call MySqlConnection class instance in order to
	 * speak with the DB and provide and retrieve Data
	 * @param msg The message from the client
	 * @param client the client the message came from
	 * @throws IOException if the sending the message from server to client has failed
	 */
	@SuppressWarnings("unchecked")
	public static void HandleMessage(Object msg, ConnectionToClient client) throws IOException {
		/**
		 * This Switch case handles different types of messages from the specific client
		 * To each different message type there is a different response
		 */
		Message message = (Message) msg;
		switch (message.getTask()) {
		case Request_connect:
			updateClientList(client, "Connected");
			System.out.println("Client Wants to connect");
			client.sendToClient(new Message(Task.Connected, null));
			break;
		case Request_disconnected:
			updateClientList(client, "Disconnected");
			client.sendToClient(new Message(Task.Disconnected, null));
			break;
		case Request_logout:
			MySqlConnection.disconnectUserByUpdatingUserLoginStatus((String)message.getObject());
			client.sendToClient(new Message(Task.Logged_out_Successful, null));
		/* User asks to import the order data in the order Scheme in DB */
		case Import_order_data:
			client.sendToClient(new Message(Task.Order_data_imported, MySqlConnection.getDataOrders()));
			break;
		case Import_order_data_by_store:
			client.sendToClient(new Message(Task.Order_data_imported, MySqlConnection.getDataOrdersByStore(((String) message.getObject()), OrderStatus.NOT_APPROVED)));
			break;
		case Import_orderstocancel_data_by_store:
			client.sendToClient(new Message(Task.Order_data_imported,
					MySqlConnection.getDataOrdersToCancelByStore((User) message.getObject())));
			break;
		case Request_to_cancel_order:
			MySqlConnection.updateOrderCancelationRequest((Integer) message.getObject());
			client.sendToClient(new Message(Task.Cancel_request_successfully, null));
			break;
		case Update_orders_table:
			// Execute the update
			ArrayList<Order> updatedOrders = (ArrayList<Order>) message.getObject();
			MySqlConnection.updateOrdersTableOnDB(updatedOrders);
			System.out.println("Orders table has been successfully updated");
			// Send message to the client that everything went well
			client.sendToClient(new Message(Task.Update_orders_successfully, null));
			break;
			
		case Update_orders_table_to_approved:
			// Execute the update
			ArrayList<Order> updatedOrders2 = (ArrayList<Order>) message.getObject();
			// Send message to the client that everything went well
			client.sendToClient(new Message(Task.Update_orders_successfully_and_Send_email, MySqlConnection.updateOrdersStatusAndConfirmationDateOnDB(updatedOrders2)));
			System.out.println("Orders table has been successfully updated");
			break;
			
		// Sending the userDetails if the user exist and the login input was correct
		// If not send ENUM that says Logged_UnSuccessful and User object as 'null'
		case Request_Login:
			Message msgForClient;
			// Getting the username and password from the messege
			ArrayList<String> userLoginInput = (ArrayList<String>) message.getObject();
			// User can be null
			User userDetails = MySqlConnection.checkLoginCredentialsAndImportUserDetails(userLoginInput);
			if (userDetails != null) {
				if(userDetails.getLoginStatus() == false) {
					System.out.println("Username: " + userDetails.getUsername() + " Password: " + userDetails.getPassword());
					System.out.println("User details were imported successfully");
					msgForClient = new Message(Task.Logged_Successful, userDetails);
				}
				else {
					System.out.println("User already logged in");
					msgForClient = new Message(Task.Logged_UnSuccessful_already_logged_in, userDetails);
				}
			} else {
				System.out.println("User login failed");
				msgForClient = new Message(Task.Logged_UnSuccessful, null);
			}
			// Send message to the client that everything went well
			client.sendToClient(msgForClient);
			break;
		case Get_ProductCatalog_table:
			client.sendToClient(new Message(Task.Get_ProductCatalog_table, MySqlConnection.getProductCatalog()));
			System.out.println("Product Catalog has been successfully imported!");
			break;
		case Get_nextProductID:
			client.sendToClient(new Message(Task.Get_nextProductID, MySqlConnection.getNextProductID()));
			break;
		case Get_user_store:
			client.sendToClient(new Message(Task.Got_store_user,
					MySqlConnection.getStore(((User) message.getObject()).getUsername())));
			break;
		case Import_costumer_data_by_username:
			client.sendToClient(new Message(Task.Costumer_data_was_imported,
					MySqlConnection.getNotApprovedCostumerByExsitingUsername((String) message.getObject())));
			break;
		case Import_user_by_username:
			client.sendToClient(new Message(Task.User_data_was_imported,
					MySqlConnection.getUserByUsername((String) message.getObject())));
			break;
		case Update_costumer_creditcard:
			ArrayList<String> returnArrayList = MySqlConnection
					.updateCostumerCreditcard((ArrayList<String>) message.getObject());
			sendEmailToUser(returnArrayList.get(1), "congratulations " + returnArrayList.get(0)
					+ ", the store manager aprroved you, you can start place your orders now!");
			client.sendToClient(new Message(Task.Costumer_creditcard_was_updated, null));
			break;
		case Import_data_costumers_approved_and_frozen:
			client.sendToClient(new Message(Task.Costumers_approved_and_frozen_was_imported,
					MySqlConnection.getDataCostumersApprovedAndFrozen()));
			break;
		case Update_costumer_table:
			MySqlConnection.updateCostumerPermissions((ArrayList<Costumer>) message.getObject());
			client.sendToClient(new Message(Task.Update_costumer_successfully, null));
			break;
		case Import_data_storestaff_of_the_same_store:
			client.sendToClient(new Message(Task.Storestaff_of_the_same_store_was_imported,
					MySqlConnection.getDataStorestaffOfTheSameStore((User) message.getObject())));
			break;
		case Update_storestaff_table:
			MySqlConnection.updateStorestaffPermissions((ArrayList<Storestaff>) message.getObject());
			client.sendToClient(new Message(Task.Update_storestaff_successfully, null));
			break;
		case Get_nextOrderID:
			int nextId = MySqlConnection.getNextOrderID();
			client.sendToClient(new Message(Task.Get_nextOrderID, nextId));
		case Update_surveys_table:
			// Execute the update
			ArrayList<String> updatedSurveys = (ArrayList<String>) message.getObject();
			MySqlConnection.updateSurveysTableOnDB(updatedSurveys);
			System.out.println("Surveys table has been successfully updated");
			// Send message to the client that everything went well
			client.sendToClient(new Message(Task.Update_surveys_successfully, null));
			break;
		case Update_complaints_table:
			// Execute the update
			MySqlConnection.updateComplaintTableOnDB((Complaint) message.getObject());
			System.out.println("Complaints table has been successfully updated");
			// Send message to the client that everything went well
			client.sendToClient(new Message(Task.Update_complaints_successfully, null));
			break;
			
		case Get_Complaints_table:
			ArrayList<String> username1 = (ArrayList<String>) message.getObject();
			client.sendToClient(new Message(Task.Get_Complaints_table, MySqlConnection.getComplaintsData(username1)));
			break;

		case Get_OrdersBy_id:
			String name = (String) message.getObject();
			client.sendToClient(new Message(Task.OrdersBy_id_imported, MySqlConnection.getOrdersByCustomerId(name)));
			System.out.println("Orders By Id has been imported successfully!");
			break;
		case Update_Complaints_DB:
			// Execute the update
			ArrayList<Complaint> updatedComplaintsDB = (ArrayList<Complaint>) message.getObject();
			System.out.println("Complaints table has been successfully updated");
			// Send message to the client that everything went well
			ArrayList<String> arr2=MySqlConnection.updateComplaintsTableOnDB(updatedComplaintsDB);
			client.sendToClient(new Message(Task.Update_complaints_successfully, arr2));
			break;
		case Get_price_for_comaplaint_by_order_id:
			client.sendToClient(new Message(Task.Got_price_by_order_id_for_complaint,
					MySqlConnection.getOrderPriceByOrderId((Integer) message.getObject())));
			break;
		case Insert_newDelivery:
			MySqlConnection.InsertNewDelivery((Delivery) message.getObject());
			client.sendToClient(new Message(Task.Insert_newDelivery, null));
			System.out.println("Insert New Delivery to deliveris table has been successfully added");
			break;
		case Insert_newOrder:
			MySqlConnection.InsertNewOrder((Order) message.getObject());
			client.sendToClient(new Message(Task.Insert_newOrder, null));
			System.out.println("Insert New Order to orders table has been successfully added");
			break;
		case Get_Costumer_By_User_Name:
			client.sendToClient(new Message(Task.Get_Costumer_By_User_Name,
					MySqlConnection.getCostumerByUserName((User) message.getObject())));
			break;
		case Updated_Customer_Store_Credit:
			ArrayList<Integer> arr = (ArrayList<Integer>) message.getObject();
			MySqlConnection.updateCustomerStoreCredit(arr.get(0), arr.get(1), arr.get(2));
			client.sendToClient(new Message(Task.Updated_Customer_Store_Credit, null));
			break;
		case Get_MonthsBy_Year:
			String year = (String) message.getObject();
			client.sendToClient(new Message(Task.MonthsBy_Year_imported, MySqlConnection.getMonthsByYear(year)));
			System.out.println("Months By Year has been imported successfully!");
			break;
		case Get_Store_Names:
			client.sendToClient(new Message(Task.Survey_Stores_Imported, MySqlConnection.getStoreNameFromSurvey()));
			System.out.println("Months By Year has been imported successfully!");
			break;
		case Get_Sale_Analyze:
			ArrayList<String> salename = (ArrayList<String>) message.getObject();
			MySqlConnection.analyzeSurveyBySaleOrStore(salename);
			client.sendToClient(new Message(Task.Sale_Analyze_Imported, null));
			System.out.println("Analyze for " + salename + " has been created!");
			break;
		case Get_Month_Year_Analyze:
			ArrayList<String> monthYear = (ArrayList<String>) message.getObject();
			MySqlConnection.analyzeSurveyByMonthOrYear(monthYear);
			client.sendToClient(new Message(Task.Year_And_Month_Analyze_Imported, null));
			break;
		case Get_User_Permission:
			String username = (String) message.getObject();
			client.sendToClient(new Message(Task.User_Permission_Imported, MySqlConnection.getPremission(username)));
			break;
		case Change_Order_Status_To_Arrived:
			ArrayList<String> returnArrayList1 = MySqlConnection.changeOrderStatusToArrived((int) message.getObject());
			if (!returnArrayList1.get(0).equals("invalidID") && returnArrayList1.get(1).equals("1"))
				sendEmailToUser(returnArrayList1.get(2),
						"Hi " + returnArrayList1.get(3) + " Sorry for the delay in shipping an order "
								+ returnArrayList1.get(4) + ", your store credit will be compensated with "
								+ returnArrayList1.get(0) + ". Thanks.");
			client.sendToClient(new Message(Task.Order_Status_Changed_To_Arrived, returnArrayList1.get(0)));
			break;
		case Send_Email:
			// first index in array represent the email address
			// second index in array represent the message
			ArrayList<String> list = (ArrayList<String>) message.getObject();
			client.sendToClient(new Message(Task.Send_Email, null));
			sendEmailToUser(list.get(0), list.get(1));
			break;
		case Get_monthly_incomes_report:
			client.sendToClient(new Message(Task.Monthly_incomes_report_was_got,
					MySqlConnection.getMonthlyIncomesReport((ArrayList<String>) message.getObject())));
			break;
		case Get_quarterly_incomes_report:
			client.sendToClient(new Message(Task.Quarterly_incomes_report_was_got,
					MySqlConnection.getQuarterlyIncomesReport((ArrayList<String>) message.getObject())));
			break;
		case Get_quarterly_costumer_satisfaction_report:
			client.sendToClient(new Message(Task.Quarterly_costumer_satisfaction_report_was_got,
					MySqlConnection.getQuarterlySatisfactionReport((ArrayList<String>) message.getObject())));
			break;
		case Get_monthly_orders_report:
			client.sendToClient(new Message(Task.Monthly_orders_report_was_got,
					MySqlConnection.getMonthlyOrdersReport((ArrayList<String>) message.getObject())));
			break;
		case Get_Customer_first_order_status:
			client.sendToClient(new Message(Task.Get_Customer_first_order_status,
					MySqlConnection.checkFirstOrder((int) message.getObject())));
			break;
		case Get_Product_By_ID:
			client.sendToClient(
					new Message(Task.Get_Product_By_ID, MySqlConnection.getProductByID((int) message.getObject())));
			break;
		case Delete_Product_By_ID:
			MySqlConnection.deleteProductByID((int) message.getObject());
			client.sendToClient(new Message(Task.Delete_Product_By_ID, null));
			break;
		case Update_Price:
			ArrayList<Float> infoArray = (ArrayList<Float>) message.getObject();
			MySqlConnection.updatePriceInCatalog(infoArray);
			client.sendToClient(new Message(Task.Update_Price, null));
			break;
		case Add_New_Product_To_Catalog:
			((ProductMyFile)message.getObject()).getProduct().setImgSrc(getImg(((ProductMyFile)message.getObject()).getMyfile()));
			MySqlConnection.AddNewProduct(((ProductMyFile)message.getObject()).getProduct());
			client.sendToClient(new Message(Task.Add_New_Product_To_Catalog, null));
			break;
		case Get_ActiveSales:
			client.sendToClient(new Message(Task.Get_ActiveSales, MySqlConnection.getActiveSales()));
			break;
		case Add_New_Sale:
			MySqlConnection.AddNewSale((Sale) message.getObject(), "activesales");
			client.sendToClient(new Message(Task.Add_New_Sale, null));
			break;
		case Check_Active_Sale_By_ID:
			int id = (Integer) message.getObject();
			client.sendToClient(new Message(Task.Check_Active_Sale_By_ID, MySqlConnection.CheckActiveSaleById(id)));
			break;
		case Update_orders_cancelation:
			ArrayList<Order> canceledOrders = (ArrayList<Order>) message.getObject();
			// Send message to the client that everything went well
			client.sendToClient(new Message(Task.orders_successfully_canceled, MySqlConnection.updateCancelationOrdersTableOnDB(canceledOrders)));
			System.out.println("Orders table has been successfully updated");
			break;
		case Send_PDF:
			client.sendToClient(new Message(Task.Send_PDF,MySqlConnection.SavePDFInDB((MyFile) message.getObject())));
			break;
		default:
			break;
		
		}
	}

	/**
	 * This method updates the Connected\Disconnect clients to our Server Each time
	 * a user Connect\disconnects we update the Connect Users in our Server GUI if a
	 * user connects his status is "Connected" and if he disconnects status changes
	 * to "Disconnected"
	 * @param client
	 * @param connectionStatus
	 */
	static void updateClientList(ConnectionToClient client, String connectionStatus) {
		ObservableList<ConnectedClient> clientList = ZliServer.getClientList();

		for (int i = 0; i < clientList.size(); i++) {
			/* Comparing clients by IP addresses */
			if (clientList.get(i).getIp().equals(client.getInetAddress().getHostAddress()))
				clientList.remove(i);
		}

		/*
		 * In both cases of Connect and Disconnected we will need to add Client into the
		 * list so this function covers both of them simultaneously
		 */
		clientList.add(new ConnectedClient(client.getInetAddress().getHostAddress(),
				client.getInetAddress().getHostName(), connectionStatus));
		ZliServer.setClientList(clientList);
	}

	/**
	 * @param address
	 * @param message
	 */
	@SuppressWarnings("static-access")
	static void sendEmailToUser(String address, String message) {
		try {
			JavaMailUtil util=new JavaMailUtil();
			util.sendMail(address, message);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Sending mail to" + address + " has failed!");
		}
	}

	/**
	 * @param msg
	 * @return String
	 */
	private static String getImg(MyFile msg) {
		String userHomePath="";
			try {
				userHomePath=System.getProperty("user.home") +"\\"+msg.getFileName();
				File newFile = new File(userHomePath);
				FileOutputStream fileOut;
				fileOut = new FileOutputStream(newFile);
				BufferedOutputStream bufferOut = new BufferedOutputStream(fileOut);
				try {
					bufferOut.write(msg.getMybytearray(), 0,  msg.getSize());
					fileOut.flush();
					bufferOut.flush();
					bufferOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return "/GuiAssests/userIcon.png";
			
	}
}
