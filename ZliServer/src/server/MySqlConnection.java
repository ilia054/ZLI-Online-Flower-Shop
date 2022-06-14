package server;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import Entities.AbstractProduct;
import Entities.Complaint;
import Entities.Costumer;
import Entities.Delivery;
import Entities.MonthlyIncomesReport;
import Entities.MonthlyOrdersReport;
import Entities.Order;
import Entities.Product;
import Entities.QuarterlySatisfactionReport;
import Entities.Sale;
import Entities.Storestaff;
import Entities.Survey;
import Entities.User;
import Enums.ComplaintStatus;
import Enums.CostumerPremissions;
import Enums.OrderStatus;
import Enums.PaymentMethod;
import Enums.Refund;
import Enums.Store;
import Enums.StorestaffPermissions;
import Enums.SupplyMethod;
import Enums.UserType;
import common.MyFile;
import common.Time;

/**
 * This class holds the connection between the Server and the MySql data base we
 * Update,retrieve and set up data in our Database this class will run Queries
 * based on the needs of the system
 */

public class MySqlConnection {
	static Connection conn;

	/**
	 * This hook method sets up a connection between the Server and the Data base we
	 * connect to the server by passing the DB address, DB user name and password
	 * 
	 * @param username  address of our DB
	 * @param password  the password of the Machines DB
	 * @param DBAddress String DBAdress the location of the DB
	 */
	public static void connectToDb(String username, String password, String DBAddress) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error */
			System.out.println("Driver definition failed");
		}
		try {
			conn = DriverManager.getConnection(DBAddress, username, password);
			System.out.println("SQL connection succeed");
		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			System.out.println("\n");
		}
	}

	/**
	 * this method closes the connection to the DB and the server
	 */
	public static void closeConnection() {
		if (conn == null)
			System.out.println("Server Connection has been closed");
		else {
			try {
				conn.close();
			} catch (SQLException e) {
				// ignore
			}
		}
	}

	/**
	 * This method runs a SELECT * FROM zli.orders query to the DB and returns an
	 * 
	 * @return arraylist of the orders in our System
	 */
	public static ArrayList<Order> getDataOrders() {
		ArrayList<Order> orders = new ArrayList<Order>();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM zli.orders");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				orders.add(new Order(rs.getInt("orderNumber"), rs.getFloat("price"), rs.getString("greetingCard"),
						rs.getString("components"), Store.valueOf(rs.getString("store")),
						rs.getString("estimatedDeliveryDate"), rs.getString("orderDate"),
						rs.getString("confirmationDate"), OrderStatus.valueOf(rs.getString("orderStatus")),
						rs.getInt("costumerID"), rs.getFloat("refund"),
						SupplyMethod.valueOf(rs.getString("supplyMethod")),
						PaymentMethod.valueOf(rs.getString("paymentMethod"))));
			}
		} catch (Exception e) {
			System.out.println("Importing orders from zli.Orders has failed!");
		}
		return orders;
	}

	/**
	 * This method runs a SELECT * FROM zli.orders query to the DB and returns an
	 * ArrayList of the orders from specific store in our System
	 * 
	 * @param store       get all orders from given store
	 * @param orderStatus ll orders from given store where status = orderStatus
	 * @return all the orders where storename = store and status = orderStatus
	 */
	public static ArrayList<Order> getDataOrdersByStore(String store, OrderStatus orderStatus) {
		ArrayList<Order> orders = new ArrayList<Order>();
		try {
			PreparedStatement ps = conn
					.prepareStatement("SELECT * FROM zli.orders WHERE Store = ? AND orderStatus = ?");
			try {
				ps.setString(1, store);
				ps.setString(2, orderStatus.toString());
			} catch (Exception e) {
				System.out.println("Executing statement-Updating orders table from TableView failed!");
			}
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				orders.add(new Order(rs.getInt("orderNumber"), rs.getFloat("price"), rs.getString("greetingCard"),
						rs.getString("components"), Store.valueOf(rs.getString("store")),
						rs.getString("estimatedDeliveryDate"), rs.getString("orderDate"),
						rs.getString("confirmationDate"), OrderStatus.valueOf(rs.getString("orderStatus")),
						rs.getInt("costumerID"), rs.getFloat("refund"),
						SupplyMethod.valueOf(rs.getString("supplyMethod")),
						PaymentMethod.valueOf(rs.getString("paymentMethod"))));
			}
		} catch (Exception e) {
			System.out.println("Importing orders by store from zli.Orders has failed!");
			System.out.println("Inside getDataOrdersByStore() method");
		}
		return orders;
	}

	/**
	 * This method runs a SELECT * FROM zli.orders query to the DB and returns an
	 * ArrayList of the orders from specific store in our System
	 * 
	 * @param user the user that works in the store
	 * @return all the orders where the user works
	 */
	public static ArrayList<Order> getDataOrdersToCancelByStore(User user) {
		ArrayList<Order> orders = new ArrayList<Order>();
		String store = getStore(user.getUsername());
		try {
			PreparedStatement ps = conn
					.prepareStatement("SELECT * FROM zli.orders WHERE store = ? AND orderStatus = ?");
			try {
				ps.setString(1, store);
				ps.setString(2, "REQUEST_TO_CANCELED");
			} catch (Exception e) {
				System.out.println("Error initialize the query");
			}
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				orders.add(new Order(rs.getInt("orderNumber"), rs.getFloat("price"), rs.getString("greetingCard"),
						rs.getString("components"), Store.valueOf(rs.getString("store")),
						rs.getString("estimatedDeliveryDate"), rs.getString("orderDate"),
						rs.getString("confirmationDate"), OrderStatus.valueOf(rs.getString("orderStatus")),
						rs.getInt("costumerID"), rs.getFloat("refund"),
						SupplyMethod.valueOf(rs.getString("supplyMethod")),
						PaymentMethod.valueOf(rs.getString("paymentMethod"))));
			}
		} catch (Exception e) {
			System.out.println("Importing orders by store from zli.Orders has failed!");
		}
		return orders;
	}

	/**
	 * This method perform query in order to get the store of the employee that
	 * connected to the system
	 * 
	 * @param username the username we want to get the store from
	 * @return Store the store of the username
	 **/
	public static String getStore(String username) {
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT storeName FROM zli.storestaff where username = ?");
			try {
				ps.setString(1, username);
			} catch (Exception e) {
				System.out.println("Error initialize the query");
			}
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString("storeName");
			}
		} catch (Exception e) {
			System.out.println("Get storeName from storestaff has failed");
		}
		return null;
	}

	public static ArrayList<String> getPremission(String username) {
		ArrayList<String> permission = new ArrayList<String>();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT permissions FROM zli.storestaff where username = ?");
			try {
				ps.setString(1, username);
			} catch (Exception e) {
				System.out.println("Error initialize the query");
			}
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				permission.add(rs.getString("permissions"));
			}
		} catch (Exception e) {
			System.out.println("Get permission from storestaff has failed");
		}
		return permission;
	}

	/**
	 * This method runs a UPDATE zli.orders query to the DB updates the Order's with
	 * the User changes we save the changes into our DB
	 * 
	 * @param updatedOrders all the orders we want to update in DB
	 */
	public static void updateOrdersTableOnDB(ArrayList<Order> updatedOrders) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(
					"UPDATE zli.orders SET price = ?, greetingCard = ?,estimatedDeliveryDate = ?, orderStatus = ? WHERE orderNumber = ?");
		} catch (SQLException e1) {
			System.out.println("Statement failure");
		}

		for (Order order : updatedOrders) {
			int primaryKeyOrder = order.getOrderNumber();
			try {
				ps.setFloat(1, order.getPrice());
				ps.setString(2, order.getGreetingCard());
				ps.setString(3, order.getEstimatedDeliveryDate());
				ps.setString(4, order.getOrderStatus().toString());
				ps.setInt(5, primaryKeyOrder);
				ps.executeUpdate();
			} catch (Exception e) {
				System.out.println("Executing statement-Updating orders table from TableView failed!");
			}
		}
	}

	public static ArrayList<String> updateCancelationOrdersTableOnDB(ArrayList<Order> updatedOrders) {
		ArrayList<String> emails = new ArrayList<>();
		float storeCredit = 0;
		PreparedStatement ps = null;
		for (Order order : updatedOrders) {
			String user = null;
			try {
				ps = conn.prepareStatement("SELECT username, storeCredit FROM zli.costumers WHERE costumerID = ?");
				try {
					ps.setInt(1, order.getCostumerID());
				} catch (Exception e) {
					System.out.println("Error initialize the query on 'updateCancelationOrdersTableOnDB()'");
					return null;
				}
				ResultSet rs1 = ps.executeQuery();

				if (rs1.next()) {
					user = rs1.getString("username");
					storeCredit = rs1.getFloat("storeCredit");
				}
			} catch (Exception e) {
				System.out.println("Statement execute failed on 'updateCancelationOrdersTableOnDB()'");
			}
			try {
				ps = conn.prepareStatement("SELECT email FROM zli.users where username = ?");
				try {
					ps.setString(1, user);
				} catch (Exception e) {
					System.out.println("Error initialize the query on 'updateCancelationOrdersTableOnDB()'");
					return null;
				}
				ResultSet rs2 = ps.executeQuery();
				while (rs2.next()) {
					if (order.getOrderStatus().toString().equals("CANCELED")) {
						emails.add(rs2.getString("email"));
						emails.add(user);
						emails.add(String.valueOf(order.getOrderNumber()));
						emails.add(order.getOrderDate());
						emails.add(order.getStore().toString());
						emails.add(String.valueOf(order.getRefund()));
					}
				}

			} catch (Exception e) {
				System.out.println("Error initialize the query on 'updateCancelationOrdersTableOnDB()'");
			}
			try {
				ps = conn.prepareStatement("UPDATE zli.orders SET orderStatus = ? WHERE orderNumber = ?");
			} catch (SQLException e1) {
				System.out.println("Statement initialize on 'updateCancelationOrdersTableOnDB()'");
			}
			int primaryKeyOrder = order.getOrderNumber();
			try {
				ps.setString(1, "CANCELED");
				ps.setInt(2, primaryKeyOrder);
				ps.executeUpdate();
			} catch (Exception e) {
				System.out.println("Executing statement-Updating orders table from TableView failed!");
				emails.removeAll(emails);
			}

			try {
				ps = conn.prepareStatement("UPDATE zli.costumers SET storeCredit = ? WHERE costumerID = ?");
			} catch (SQLException e1) {
				System.out.println("Statement initialize on 'updateCancelationOrdersTableOnDB()'");
			}
			try {
				ps.setFloat(1, order.getRefund() + storeCredit);
				ps.setInt(2, order.getCostumerID());
				ps.executeUpdate();
			} catch (Exception e) {
				System.out.println("Executing statement-Updating orders table from TableView failed!");
				emails.removeAll(emails);
			}
		}
		return emails;
	}

	public static ArrayList<String> updateOrdersStatusAndConfirmationDateOnDB(ArrayList<Order> updatedOrders) {
		ArrayList<String> emails = new ArrayList<>();
		PreparedStatement ps = null;

		for (Order order : updatedOrders) {
			///////////////////////////////////

			String user = null;
			try {
				ps = conn.prepareStatement("SELECT username FROM zli.costumers WHERE costumerID = ?");
				try {
					System.out.println(order.getCostumerID());
					ps.setInt(1, order.getCostumerID());
				} catch (Exception e) {
					System.out.println("Error initialize the query");
					return null;
				}
				ResultSet rs1 = ps.executeQuery();

				while (rs1.next()) {
					user = rs1.getString("username");
					System.out.println(user + "check");

				}
			} catch (Exception e) {
				System.out.println("Get CustomerID from Customer Table has failed");
			}
			try {
				ps = conn.prepareStatement("SELECT email FROM zli.users where username = ?");
				try {
					ps.setString(1, user);
				} catch (Exception e) {
					System.out.println("Error initialize the query");
					return null;
				}
				ResultSet rs2 = ps.executeQuery();
				while (rs2.next()) {
					if (order.getOrderStatus().toString().equals("APPROVED")) {
						emails.add(rs2.getString("email"));
						emails.add(user);
						emails.add(String.valueOf(order.getOrderNumber()));
						emails.add(order.getOrderDate());
						emails.add(order.getStore().toString());
					}

				}

			} catch (Exception e) {
				System.out.println("Get CustomerID from Customer Table has failed");
			}

			//////////////////////////////////
			try {
				ps = conn.prepareStatement(
						"UPDATE zli.orders SET confirmationDate = ?, orderStatus = ? WHERE orderNumber = ?");
			} catch (SQLException e1) {
				System.out.println("updateOrdersStatusOnDB failed!");
			}
			int primaryKeyOrder = order.getOrderNumber();
			try {
				ps.setString(1, Time.formatLocalDate());
				ps.setString(2, "APPROVED");
				ps.setInt(3, primaryKeyOrder);
				ps.executeUpdate();
			} catch (Exception e) {
				System.out.println("updateOrdersStatusOnDB failed!");
			}
			try {
				ps = conn.prepareStatement(
						"UPDATE zli.orders SET estimatedDeliveryDate = ? WHERE orderNumber = ? AND supplyMethod=? ");// Priority_Delivery
			} catch (SQLException e1) {
				System.out.println("updateOrdersStatusOnDB failed!");
			}
			String timeString = Time.formatLocalDate();
			String hours = timeString.substring(11, 13);
			int hoursi = (int) Integer.valueOf(hours) + 3;
			if (hoursi == 24)
				hoursi = 0;
			else if (hoursi == 25)
				hoursi = 1;
			else if (hoursi == 26)
				hoursi = 2;
			hours = String.valueOf(hoursi);
			timeString = timeString.substring(0, 11) + hours + timeString.substring(13, 16);
			try {
				ps.setString(1, timeString);
				ps.setInt(2, primaryKeyOrder);
				ps.setString(3, "Priority_Delivery");
				ps.executeUpdate();
			} catch (Exception e) {
				System.out.println("updateOrdersStatusOnDB failed!");
			}

			try {
				ps = conn.prepareStatement(
						"UPDATE zli.deliveries SET estimatedDeliveryDate = ? WHERE orderNumber = ? AND immediateSupply=? ");
			} catch (SQLException e1) {
				System.out.println("updateOrdersStatusOnDB failed!");
			}
			try {
				ps.setString(1, timeString);
				ps.setInt(2, primaryKeyOrder);
				ps.setInt(3, 1);
				ps.executeUpdate();
			} catch (Exception e) {
				System.out.println("updateOrdersStatusOnDB failed!");
			}
		}
		return emails;
	}

	public static User checkLoginCredentialsAndImportUserDetails(ArrayList<String> userLoginInput) {
		if(userLoginInput == null) throw new NullPointerException();
		try {
			PreparedStatement ps = conn
					.prepareStatement("SELECT * FROM zli.users WHERE username = ? and password = ?;");
			ps.setString(1, userLoginInput.get(0));
			ps.setString(2, userLoginInput.get(1));
			ResultSet rs = ps.executeQuery();
			if (rs.next() == false) {
				return null;
			} else {
				// Save user details
				User user = new User(rs.getString("username"), rs.getString("password"), rs.getString("firstName"),
						rs.getString("lastName"), UserType.valueOf(rs.getString("role")), rs.getString("email"),
						rs.getString("phoneNumber"), rs.getBoolean("isLoggedIn"), rs.getString("id"));
				// Set the login status to true so no one else can access it
				try {
					ps = conn.prepareStatement("UPDATE zli.users SET isLoggedIn = ? WHERE username = ?");
				} catch (SQLException e1) {
					System.out.println("Update isLoggedIn status statement failure on 'checkLoginCredentialsAndImportUserDetails()'");
				}
				try {
					ps.setBoolean(1, true);
					ps.setString(2, rs.getString("username"));
					ps.executeUpdate();
				} catch (Exception e) {
					System.out.println("Executing statement-Updating login status on users table had failed!");
				}
				// Return user details
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void disconnectUserByUpdatingUserLoginStatus(String username) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("UPDATE zli.users SET isLoggedIn = ? WHERE username = ?");
		} catch (SQLException e1) {
			System.out.println(
					"Update isLoggedIn to false in order to disconnect user statement failure on 'disconnectUserByUpdatingUserLoginStatus()'");
		}
		try {
			ps.setBoolean(1, false);
			ps.setString(2, username);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(
					"Executing statement-Updating login status for disconnect user on 'users' table had failed!");
		}

	}

	// Method that returns an User object by specific 'username'
	public static User getUserByUsername(String username) {
		try {
			// create the required SQL query
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM zli.users WHERE username = ?");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next() == false) {
				return null;
			} else {
				return new User(rs.getString("username"), rs.getString("password"), rs.getString("firstName"),
						rs.getString("lastName"), UserType.valueOf(rs.getString("role")), rs.getString("email"),
						rs.getString("phoneNumber"), rs.getBoolean("isLoggedIn"), rs.getString("id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static User getNotApprovedCostumerByExsitingUsername(String username) {
		User user = getUserByUsername(username);
		if (user == null)
			return null;
		try {
			// create the required SQL query
			PreparedStatement ps = conn
					.prepareStatement("SELECT * FROM zli.costumers WHERE username = ? AND permissions = ?");
			ps.setString(1, user.getUsername());
			ps.setString(2, "NOT_APPROVED");
			ResultSet rs = ps.executeQuery();
			if (rs.next() == false) {
				return null;
			} else {
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Query to get all products(Products and Items) in the catalog from DB and
	 * 
	 * @return of all the products in DB
	 */
	public static ArrayList<Product> getProductCatalog() {
		ArrayList<Product> productCatalog = new ArrayList<Product>();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM zli.productcatalog");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				productCatalog.add(new Product(rs.getString("name"), rs.getString("imgSrc"), rs.getString("color"),
						rs.getString("category"), rs.getString("type"), rs.getString("components"),
						rs.getFloat("price"), rs.getInt("id")));
			}
		} catch (Exception e) {
			System.out.println("Importing Product Catalog from zli.Ordersd has failed!");
		}
		return productCatalog;
	}

	public static int getNextProductID() {
		PreparedStatement ps = null;
		int max = 0;
		try {
			ps = conn.prepareStatement("Select MAX(id) as Max_ID FROM zli.productcatalog");
			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next())
					max = rs.getInt("Max_ID");
			}
		} catch (SQLException e) {
			System.out.println("Executing statement-> Slecet MAX(ID) from productcatalog failed!");
		}
		return ++max;
	}

	/* one order or array of orders */
	public static void updateOrderCancelationRequest(Integer orderNumber) {
		float amountToReturn = 0, orderPrice = 0;
		String orderDate = null;
		boolean isPriorityAndNotApproved = false;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("SELECT * FROM zli.orders WHERE orderNumber = ? ");
			ps.setInt(1, orderNumber);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				if (rs.getString("estimatedDeliveryDate") == null) {
					isPriorityAndNotApproved = true;
				}
				if (!isPriorityAndNotApproved)
					orderDate = rs.getString("estimatedDeliveryDate") + ":00";
				orderPrice = rs.getFloat("price");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (!isPriorityAndNotApproved) {

			Long timeDifference = Time.calculateTimeDiff(orderDate);

			if (timeDifference < 60) {
				amountToReturn = 0;
			} else if (timeDifference >= 60 && timeDifference < 180) {
				amountToReturn = (float) (orderPrice * 0.5);
			} else
				amountToReturn = orderPrice;
		}
		if (isPriorityAndNotApproved)
			amountToReturn = orderPrice;
		try {
			ps = conn.prepareStatement("UPDATE zli.orders SET orderStatus = ? , refund = ? WHERE orderNumber = ?");
		} catch (SQLException e1) {
			System.out.println("Statement failure");
		}
		try {
			ps.setString(1, "REQUEST_TO_CANCELED");
			ps.setFloat(2, amountToReturn);
			ps.setInt(3, orderNumber);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(
					"Executing statement-Updating order to cancel status and refund in table Orders TableView failed!");
		}
	}

	public static ArrayList<Costumer> getDataCostumersApprovedAndFrozen() {
		ArrayList<Costumer> costumers = new ArrayList<Costumer>();
		try {
			// create the required SQL query
			PreparedStatement ps = conn
					.prepareStatement("SELECT * FROM zli.costumers WHERE permissions = ? OR permissions = ?");
			ps.setString(1, "APPROVED");
			ps.setString(2, "FROZEN");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				costumers.add(new Costumer(rs.getInt("costumerID"), rs.getString("username"), rs.getString("debt"),
						CostumerPremissions.valueOf(rs.getString("permissions")), rs.getString("storeCredit"),
						rs.getString("creditCard"), rs.getString("expirationDate"), rs.getString("cvv")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Importing customers failed");
		}
		return costumers;
	}

	public static ArrayList<String> updateCostumerCreditcard(ArrayList<String> detailsToUpdate) {
		PreparedStatement ps = null;
		String username = detailsToUpdate.get(0);
		String creditCardNumber = detailsToUpdate.get(1);
		String expirationDate = detailsToUpdate.get(2);
		String cvv = detailsToUpdate.get(3);
		String email = null;
		ArrayList<String> returnArrayList = new ArrayList<String>();
		try {
			ps = conn.prepareStatement(
					"UPDATE zli.costumers SET permissions = ?, creditCard = ?, expirationDate = ?, cvv = ? WHERE username = ?");
		} catch (SQLException e1) {
			System.out.println("prepareStatement failure in updateCostumerCreditcard");
		}
		try {
			ps.setString(1, "APPROVED");
			ps.setString(2, creditCardNumber);
			ps.setString(3, expirationDate);
			ps.setString(4, cvv);
			ps.setString(5, username);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("Couldn't set parameters for prepareStatement of updateCostumerCreditcard method query");
		}

		try {
			ps = conn.prepareStatement("SELECT email FROM zli.users WHERE username = ?");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				email = rs.getString("username");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("updateCostumerCreditcard failed!!!");
		}
		returnArrayList.add(username);
		returnArrayList.add(email);
		return returnArrayList;

	}

	public static void updateCostumerPermissions(ArrayList<Costumer> updatedCostumers) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("UPDATE zli.costumers SET permissions = ? WHERE costumerID = ?");
		} catch (SQLException e1) {
			System.out.println("Statement failure");
		}
		for (Costumer costumer : updatedCostumers) {
			int primaryKeyOrder = costumer.getCostumerID();
			try {
				ps.setString(1, costumer.getPermissions().toString());
				ps.setInt(2, primaryKeyOrder);
				ps.executeUpdate();
			} catch (Exception e) {
				System.out.println("Executing statement-Updating costumers permissions table from TableView failed!");
			}
		}
	}

	/**
	 * This method updates the monthly surveys details in the survey DB. it sends
	 * the object the the client sent to the server and each line that is applied to
	 * this survey type Receives the analyzeID of the pdf file that has been created
	 * 
	 * @author Michael Ioffe
	 * @param monthyearArray(strings)
	 */
	public static void analyzeSurveyByMonthOrYear(ArrayList<String> monthyearArray) {
		int sum = Integer.parseInt(monthyearArray.get(2)), countQuestions = 0, flag = 2;
		SurveyQuestionsResult(sum, countQuestions, monthyearArray.get(0), monthyearArray.get(1), flag);

	}

	/**
	 * This method updates the Store sale/Monthly surveys details in the survey DB.
	 * it sends the object the the client sent to the server and each line that is
	 * applied to this survey type Receives the analyzeID of the PDF file that has
	 * been created
	 * 
	 * @author Michael Ioffe
	 * @param input monthyearArray(strings)
	 */
	public static void analyzeSurveyBySaleOrStore(ArrayList<String> input) {

		int countQuestions = 0, flag = 0;
		String salename = input.get(0);
		int id = Integer.parseInt(input.get(1));
		if (salename.equals("Spring Sale") || salename.equals("Summer Sale") || salename.equals("Fall Sale")
				|| salename.equals("Winter Sale")) {
			switch (salename) {
			case "Spring Sale":
				SurveyQuestionsResult(id, countQuestions, "Spring", null, flag);

				break;
			case "Summer Sale":
				SurveyQuestionsResult(id, countQuestions, "Summer", null, flag);

				break;
			case "Fall Sale":
				SurveyQuestionsResult(id, countQuestions, "Fall", null, flag);

				break;
			case "Winter Sale":
				SurveyQuestionsResult(id, countQuestions, "Winter", null, flag);

				break;
			}
		} else {
			flag = 1;
			SurveyQuestionsResult(id, countQuestions, salename, null, flag);

		}

	}

	/**
	 * This method used by previous two methods in order to update the surveye by
	 * type, the results(Surveys) will Receive the analyzedID(PDF ID) in the survey
	 * DB in each line that got link to the recived information from the DB.
	 * 
	 * @author Michael Ioffe
	 * @param sum(the                 id of pdf),
	 * @param counter(num             of questions)
	 * @param seasonOrNameOrYear(type of survey)
	 * @param Month                   of the survery
	 * @param flag(to                 recognize the survey type)
	 */
	public static void SurveyQuestionsResult(int sum, int counter, String seasonOrNameOrYear, String Month, int flag) {
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy");
		Date Year = new Date();
		String year = formatter2.format(Year).toString();
		PreparedStatement ps;
		try {
			// create the required SQL query
			if (flag == 0) {
				ps = conn.prepareStatement(
						"UPDATE  zli.survey SET analyzeID=? WHERE TYPE='Store Sale' AND year =? AND Season =?");
				ps.setString(2, year);
				ps.setString(3, seasonOrNameOrYear);
			}

			else if (flag == 1) {
				ps = conn.prepareStatement(
						"UPDATE  zli.survey SET analyzeID = ? WHERE TYPE='Store Name' AND year =? AND StoreName =?");
				ps.setString(2, year);
				ps.setString(3, seasonOrNameOrYear);
			} else {
				ps = conn.prepareStatement(
						"UPDATE  zli.survey SET analyzeID =? WHERE TYPE='Monthly' AND year =? AND Month =?");
				ps.setString(2, seasonOrNameOrYear);
				ps.setString(3, Month);
			}

			try {
				ps.setInt(1, sum);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Importing Surveys results  failed");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Importing Surveys results  failed");
		}
	}

	/**
	 * This method returns the store names into the combox in the
	 * ManageSurveyByStroeNameController .
	 * 
	 * @return for all the store names of the surveys
	 */
	public static ArrayList<String> getStoreNameFromSurvey() {
		PreparedStatement ps1 = null;
		ArrayList<String> monthsByYear = new ArrayList<String>();

		try {
			ps1 = conn.prepareStatement("SELECT StoreName FROM zli.survey WHERE  TYPE = ? ");
			try {
				ps1.setString(1, "Store Name");
			} catch (Exception e) {
				System.out.println("Error initialize the query");
			}

			ResultSet rs1 = ps1.executeQuery();

			while (rs1.next()) {
				String str = rs1.getString("StoreName");
				if (!monthsByYear.contains(str))
					monthsByYear.add(str);
			}
			return monthsByYear;
		} catch (Exception e) {
			System.out.println("Get Store Names from Survey table has failed");
		}
		return null;

	}

	/**
	 * This method returns the months of a specific year into the combox of the GUI
	 * controller ( ManageSurveyByYearAndMonthController)
	 * 
	 * @param year the year we want to get all the survey
	 * @return all the months there is a suvery in the year "year"
	 */
	public static ArrayList<String> getMonthsByYear(String year) {
		PreparedStatement ps1 = null;
		ArrayList<String> monthsByYear = new ArrayList<String>();

		try {
			ps1 = conn.prepareStatement("SELECT Month FROM zli.survey WHERE Year = ? AND TYPE = ? ");
			try {
				ps1.setString(1, year);
				ps1.setString(2, "Monthly");
			} catch (Exception e) {
				System.out.println("Error initialize the query");
			}

			ResultSet rs1 = ps1.executeQuery();

			while (rs1.next()) {
				String str = rs1.getString("Month");
				if (!monthsByYear.contains(str))
					monthsByYear.add(str);
			}
			return monthsByYear;
		} catch (Exception e) {
			System.out.println("Get Months by Year from Survey table has failed");
		}
		return null;

	}

	/**
	 * This method adds new survey details to the DB.
	 * 
	 * @author Michael Ioffe
	 * @param newSurvey(survey details)
	 */
	public static void updateSurveysTableOnDB(ArrayList<String> newSurvey) {
		Survey newSurvey1 = new Survey(newSurvey.get(7), newSurvey.get(0), newSurvey.get(1), newSurvey.get(2),
				newSurvey.get(3), newSurvey.get(4), newSurvey.get(5), newSurvey.get(6), newSurvey.get(8));
		PreparedStatement ps = null;
		SimpleDateFormat formatter = new SimpleDateFormat("MMMMM");
		Date month = new Date();

		String forma = formatter.format(month).toString();
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy");
		Date year = new Date();
		String forma2 = formatter2.format(year).toString();
		String season;
		if (forma.equals("March") || forma.equals("April") || forma.equals("May"))
			season = "Spring";
		else if (forma.equals("June") || forma.equals("July") || forma.equals("August"))
			season = "Summer";
		else if (forma.equals("September") || forma.equals("October") || forma.equals("November"))
			season = "Fall";
		else
			season = "Winter";
		try {

			ps = conn.prepareStatement(
					"INSERT INTO zli.survey (Question1, Question2, Question3, Question4, Question5, Question6, StoreName, CustomerName,TYPE,Month,Year,Season) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
		} catch (SQLException e1) {
			System.out.println("Statement failure");
		}
		String primaryKeySurvey = newSurvey1.getUser();
		String storename = getStore(newSurvey1.getStore());

		try {
			ps.setString(1, newSurvey1.getQuestion1());
			ps.setString(2, newSurvey1.getQuestion2());
			ps.setString(3, newSurvey1.getQuestion3());
			ps.setString(4, newSurvey1.getQuestion4());
			ps.setString(5, newSurvey1.getQuestion5());
			ps.setString(6, newSurvey1.getQuestion6());
			ps.setString(7, storename);
			ps.setString(8, primaryKeySurvey);
			ps.setString(9, newSurvey1.getType());
			ps.setString(10, forma);
			ps.setString(11, forma2);
			ps.setString(12, season);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("Executing statement-Updating Surveys table from TableView failed!");
			e.printStackTrace();
		}
	}

	/**
	 * This method returns all order numbers by customers id. first we get the
	 * customer id by the customers username then we receive the ordernumber by the
	 * customerID from the complaint DB
	 * 
	 * @author Michael Ioffe
	 * @param name(customer username)
	 * @return all the order numbers made by customer
	 */
	public static ArrayList<Integer> getOrdersByCustomerId(String name) {
		ArrayList<Integer> allreadysubmited = new ArrayList<>();
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		ArrayList<Integer> ordersById = new ArrayList<Integer>();
		int costumerID = -1;

		try {
			ps = conn.prepareStatement("SELECT costumerID FROM zli.costumers where username = ?");
			try {
				ps.setString(1, name);
			} catch (Exception e) {
				System.out.println("Error initialize the query");
				return null;
			}
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				costumerID = rs.getInt("costumerID");
			}
		} catch (Exception e) {
			System.out.println("Get CustomerID from Customer Table has failed");
		}
		try {
			ps2 = conn.prepareStatement("SELECT orderNumber FROM zli.complaint WHERE username=?");
			try {
				ps2.setString(1, name);
			} catch (Exception e) {
				System.out.println("Error initialize the query");
			}

			ResultSet rs2 = ps2.executeQuery();
			while (rs2.next()) {
				allreadysubmited.add(rs2.getInt("orderNumber"));

			}
		} catch (Exception e) {
			System.out.println("Get Orders by Id from Orders table has failed");
		}
		if (costumerID != -1) {
			try {
				ps1 = conn.prepareStatement(
						"SELECT orderNumber FROM zli.orders WHERE costumerID = ? AND (orderStatus = ? OR orderStatus = ?)");
				try {
					ps1.setInt(1, costumerID);
					ps1.setString(2, "APPROVED");
					ps1.setString(3, "NOT_APPROVED");
				} catch (Exception e) {
					System.out.println("Error initialize the query");
				}

				ResultSet rs1 = ps1.executeQuery();

				while (rs1.next()) {
					int orderid = rs1.getInt("orderNumber");
					if (!allreadysubmited.contains(orderid))
						ordersById.add(orderid);

				}
				return ordersById;
			} catch (Exception e) {
				System.out.println("Get Orders by Id from Orders table has failed");
			}
			return null;
		}
		return null;

	}

	/**
	 * @author Michael Ioffe
	 * @param orderid(order id)
	 * @return order price by order id.
	 */
	public static float getOrderPriceByOrderId(int orderid) {
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT price FROM zli.orders where orderNumber = ?");
			try {
				ps.setInt(1, orderid);
			}

			catch (Exception e) {
				System.out.println("Error initialize the query");
			}
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {

				return rs.getFloat("price");
			}
		} catch (Exception e) {
			System.out.println("Get price from orders has failed");
		}
		System.out.println("faild to return price");
		return 0;
	}

	/**
	 * that been submited by the logged in customer service employ
	 * 
	 * @author Michael Ioffe
	 * @param username(customer)
	 * @return the complaints details from the DB to a Table on the GUI of
	 *         ManageComplaints of complaints.
	 */
	public static ArrayList<Complaint> getComplaintsData(ArrayList<String> username) {
		String user = username.get(0);
		ArrayList<Complaint> complaints = new ArrayList<Complaint>();
		try {

			PreparedStatement ps = conn
					.prepareStatement("SELECT * FROM zli.complaint WHERE complaintStatus = ? AND EmployeSubmit=?");
			try {
				ps.setString(1, "UNHANDLED");
				ps.setString(2, user);
			} catch (Exception e) {
				System.out.println("Error initialize the query");
			}
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				complaints.add(
						new Complaint(rs.getString("username"), rs.getString("complaint"), rs.getString("dateTime"),
								rs.getInt("orderNumber"), ComplaintStatus.valueOf(rs.getString("complaintStatus")),
								Refund.valueOf(rs.getString("refund")), rs.getFloat("orderPrice"),
								rs.getString("EmployeSubmit")));

			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return complaints;
	}

	/**
	 * This method insert the complaint details in the DB.
	 * 
	 * @author Michael Ioffe
	 * @param updatedComplaint(complaint)
	 */
	public static void updateComplaintTableOnDB(Complaint updatedComplaint) {
		PreparedStatement ps = null;
		try {

			ps = conn.prepareStatement(
					"INSERT INTO zli.complaint (username,complaint,dateTime,orderNumber,complaintStatus,refund,orderPrice,EmployeSubmit) VALUES(?,?,?,?,?,?,?,?)");
		} catch (SQLException e1) {
			System.out.println("Statement failure");
		}
		int forma1 = updatedComplaint.getOrderNumber();
		try {

			ps.setString(1, updatedComplaint.getUserName());
			ps.setString(2, updatedComplaint.getComplaint());
			ps.setString(3, updatedComplaint.getDateTime());
			ps.setInt(4, forma1);
			ps.setString(5, updatedComplaint.getComplaintStatus().toString());
			ps.setString(6, updatedComplaint.getRefund().toString());
			ps.setFloat(7, updatedComplaint.getOrderPrice());
			ps.setString(8, updatedComplaint.getEmployeSubmit());
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("Executing statement-Updating Complaint table from TableView failed!");
		}
	}

	/**
	 * This method updates all complaints in the DB after all changes. and adds the
	 * refund amount to the customer account (into storeCredit)
	 * 
	 * @author Michael Ioffe
	 * @param updatedComplaints updatedComplaint(complaint)
	 * @throws SQLException
	 */
	public static ArrayList<String> updateComplaintsTableOnDB(ArrayList<Complaint> updatedComplaints) {
		ArrayList<String> email = new ArrayList<>();
		PreparedStatement ps = null;
		PreparedStatement ps5 = null;
		try {
			ps = conn.prepareStatement(
					"UPDATE zli.complaint SET username = ?, complaint = ?, dateTime = ?, complaintStatus = ?, refund = ? ,orderPrice = ? WHERE orderNumber = ?");
		} catch (SQLException e1) {
			System.out.println("Statement failure");
		}

		for (Complaint complaint : updatedComplaints) {
			int primaryKeyOrder = complaint.getOrderNumber();
			if (complaint.getComplaintStatus().toString() == "APPROVED") {
				try {
					ps5 = conn.prepareStatement("SELECT email FROM zli.users where username = ?");
					try {
						ps5.setString(1, complaint.getUserName());
					} catch (Exception e) {
						System.out.println("Error initialize the query on 'updateCancelationOrdersTableOnDB()'");
						return null;
					}

					ResultSet rs4 = ps5.executeQuery();
					while (rs4.next()) {
						email.add(rs4.getString("email"));
						email.add(complaint.getUserName());
						email.add(String.valueOf(complaint.getOrderNumber()));
						email.add(String.valueOf(
								getUpdatedRefund(complaint.getRefund().toString(), complaint.getOrderPrice(), 0)));
					}
				} catch (SQLException e1) {
					System.out.println("Statement failure");
				}

				try {

					ps.setString(1, complaint.getUserName());
					ps.setString(2, complaint.getComplaint());
					ps.setString(3, complaint.getDateTime());
					ps.setString(4, complaint.getComplaintStatus().toString());
					String time = complaint.getRefund().toString();
					ps.setString(5, time);
					ps.setFloat(6, complaint.getOrderPrice());
					ps.setInt(7, primaryKeyOrder);

					ps.executeUpdate();
				} catch (Exception e) {
					System.out.println(e.getMessage());

				}
			}
		}
		PreparedStatement ps1 = null;
		try {
			ps1 = conn.prepareStatement(
					"UPDATE zli.costumers SET storeCredit = ? WHERE username = ? AND permissions = ? ");
		} catch (SQLException e1) {
			System.out.println("Statement failure");
		}

		for (Complaint complaint : updatedComplaints) {
			String name = complaint.getUserName();

			if (complaint.getComplaintStatus().toString() == "APPROVED") {
				float oldprice = getOldStoreCredit(name);
				try {
					ps1.setString(2, name);
					ps1.setString(3, "APPROVED");
				} catch (Exception e) {
					System.out.println("Error initialize the query");
				}
				try {
					ps1.setFloat(1,
							getUpdatedRefund(complaint.getRefund().toString(), complaint.getOrderPrice(), oldprice));
					ps1.executeUpdate();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}
		return email;
	}

	/**
	 * This method calculates the refund and returns the updated refund price.
	 * 
	 * @author Michael Ioffe
	 * @param refund(refund)
	 * @param orderPrice(order    Price)
	 * @param oldStoreCredit(oldS tore Credit)
	 * @return the updated refund price.
	 */
	public static float getUpdatedRefund(String refund, float orderPrice, float oldStoreCredit) {
		float RefundPrice = orderPrice;
		if (refund.equals("QUARTER_REFUND")) {
			RefundPrice = RefundPrice / 4;
			RefundPrice += oldStoreCredit;
			return RefundPrice;
		}
		if (refund.equals("HALF_REFUND")) {
			RefundPrice = RefundPrice / 2;
			RefundPrice += oldStoreCredit;
			return RefundPrice;
		}

		if (refund.equals("NO_REFUND")) {
			return oldStoreCredit;
		}

		return RefundPrice + orderPrice;

	}

	/**
	 * @author Michael Ioffe
	 * @param username(user)
	 * @return the original store credit for the chosen customer.
	 */
	public static Float getOldStoreCredit(String username) {
		float oldstoreprice = 0;
		try {

			PreparedStatement ps = conn.prepareStatement("SELECT storeCredit FROM zli.costumers where username = ?");
			try {
				ps.setString(1, username);
			}

			catch (Exception e) {
				System.out.println("Error initialize the query");
			}
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {

				oldstoreprice = rs.getFloat("storeCredit");
				return oldstoreprice;
			}
		} catch (Exception e) {
			System.out.println("Get Old Store Credit from costumers has failed");
		}
		return oldstoreprice;
	}

	public static ArrayList<Storestaff> getDataStorestaffOfTheSameStore(User storeManager) {
		if (storeManager.getStore() == null)
			return null;
		ArrayList<Storestaff> storestaff = new ArrayList<Storestaff>();
		try {
			// create the required SQL query
			PreparedStatement ps = conn
					.prepareStatement("SELECT * FROM zli.storestaff WHERE storeName = ? AND username != ?");
			ps.setString(1, storeManager.getStore());
			ps.setString(2, storeManager.getUsername());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				storestaff.add(new Storestaff(rs.getInt("storeEmployeeID"), rs.getString("username"),
//						 				   rs.getString("storeName"),
						StorestaffPermissions.valueOf(rs.getString("permissions"))));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Importing storestaff of the same store failed");
		}
		return storestaff;
	}

	public static void updateStorestaffPermissions(ArrayList<Storestaff> updatedStorestaff) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("UPDATE zli.storestaff SET permissions = ? WHERE storeEmployeeID = ?");
		} catch (SQLException e1) {
			System.out.println("Statement failure");
		}
		for (Storestaff storeEmployee : updatedStorestaff) {
			int primaryKeyOrder = storeEmployee.getStoreEmployeeID();
			try {
				ps.setString(1, storeEmployee.getPermissions().toString());
				ps.setInt(2, primaryKeyOrder);
				ps.executeUpdate();
			} catch (Exception e) {
				System.out.println("Executing statement-Updating Storestaff permissions table from TableView failed!");
			}
		}
	}

	/*
	 * this sql query returns the nextOrderID (takes highes value of id in orders
	 * table and returns it + 1
	 */
	public static int getNextOrderID() {
		PreparedStatement ps = null;
		int max = 0;
		try {
			ps = conn.prepareStatement("Select MAX(orderNumber) as Max_ID FROM zli.orders");
			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next())
					max = rs.getInt("Max_ID");
			}
		} catch (SQLException e) {
			System.out.println("Executing statement-> Slecet MAX(ID) from orders failed!");
		}
		return ++max;
	}

	public static void InsertNewDelivery(Delivery delivery) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(
					"INSERT INTO zli.deliveries (orderNumber, address, receiverName, receiverPhoneNumber, confirmationDate, estimatedDeliveryDate, actualDeliveryDate, immediateSupply) VALUES(?,?,?,?,?,?,?,?)");
		} catch (SQLException e1) {
			System.out.println("Statement Insert New Delivery to deliveries failure");
		}
		try {
			ps.setInt(1, delivery.getOrderNumber());
			ps.setString(2, delivery.getAddress());
			ps.setString(3, delivery.getReceiverName());
			ps.setString(4, delivery.getReceiverPhoneNumber());
			ps.setString(5, delivery.getConfirmationDate());
			ps.setString(6, delivery.getEstimatedDeliveryDate());
			ps.setString(7, delivery.getActualDeliveryDate());
			ps.setBoolean(8, delivery.getImmediateSupply());
			ps.executeUpdate();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("Executing statement-Insert New Delivery to deliveries failed!");
		}
	}

	public static void InsertNewOrder(Order order) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(
					"INSERT INTO zli.orders (orderNumber, price, greetingCard, Components, store, estimatedDeliveryDate, orderDate, confirmationDate,orderStatus,costumerID,refund,supplyMethod,paymentMethod) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
		} catch (SQLException e1) {
			System.out.println("Statement Insert New Order to orders failure");
		}
		try {
			ps.setInt(1, order.getOrderNumber());
			ps.setFloat(2, order.getPrice());
			ps.setString(3, order.getGreetingCard());
			ps.setString(4, order.getComponents());
			ps.setString(5, order.getStore().toString());
			ps.setString(6, order.getEstimatedDeliveryDate());
			ps.setString(7, order.getOrderDate());
			ps.setString(8, order.getConfirmationDate());
			ps.setString(9, order.getOrderStatus().toString());
			ps.setInt(10, order.getCostumerID());
			ps.setFloat(11, order.getRefund());
			ps.setString(12, order.getSupplyMethod().toString());
			ps.setString(13, order.getPaymentMethod().toString());
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("Executing statement-Insert New Order to orders failed!");
		}
	}

	public static Costumer getCostumerByUserName(User user) {
		PreparedStatement ps = null;
		Costumer tmp = null;
		try {
			ps = conn.prepareStatement("SELECT * FROM zli.costumers WHERE username = ?");
			ps.setString(1, user.getUsername());
			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next())
					tmp = new Costumer(rs.getInt("costumerID"), rs.getString("username"), rs.getString("debt"),
							CostumerPremissions.valueOf(rs.getString("permissions")), rs.getString("storeCredit"),
							rs.getString("creditCard"), rs.getString("expirationDate"), rs.getString("cvv"));
			}
		} catch (SQLException e) {
			System.out.println("Statement getCustomerByUserName has failed!");
		}
		System.out.println("Succefully imported Client Data!");
		return tmp;
	}

	public static void updateCustomerStoreCredit(int CustomerID, int StoreCreditUsed, int CustomerStoreCredit) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("UPDATE zli.costumers SET storeCredit = ? WHERE costumerID = ?");
		} catch (SQLException e1) {
			System.out.println("Statement For Updating Store Credit for customer has failed!!");
		}
		try {
			ps.setString(1, Integer.toString(CustomerStoreCredit - StoreCreditUsed));
			ps.setInt(2, CustomerID);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("Executing statement-Updating Store Credit for customer has failed");
		}
	}

	public static ArrayList<String> changeOrderStatusToArrived(int orderNumber) {
		PreparedStatement ps = null;
		int costumerID = 0;
		String estimatedDeliveryDate = null;
		String storeCredit = null;
		String email = null;
		double price = 0;
		String username = null;
		String isLate = "0";
		ArrayList<String> returnArrayList = new ArrayList<String>();
		ArrayList<Integer> orders = new ArrayList<Integer>();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime now = LocalDateTime.now();

		try {
			ps = conn.prepareStatement("SELECT orderNumber FROM zli.deliveries WHERE actualDeliveryDate IS NULL");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				orders.add(rs.getInt("orderNumber"));
			}
		} catch (SQLException e) {
			System.out.println("changeOrderStatusToArrived failed!");
		}

		if (!orders.contains(orderNumber)) {
			returnArrayList.add("invalidID");
			return returnArrayList;
		}

		try {
			ps = conn.prepareStatement(
					"SELECT costumerID, estimatedDeliveryDate, price FROM zli.orders WHERE orderNumber = ? AND orderStatus = ?");
			ps.setInt(1, orderNumber);
			ps.setString(2, "APPROVED");

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				costumerID = rs.getInt("costumerID");
				estimatedDeliveryDate = rs.getString("estimatedDeliveryDate");
				price = rs.getFloat("price");
			} else {
				returnArrayList.add("invalidID");
				return returnArrayList;
			}
		} catch (SQLException e) {
			System.out.println("changeOrderStatusToArrived failed!");
		}

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		// Save current time
		String savedTime = estimatedDeliveryDate; // Applies local time zone 1
		Calendar cal = Calendar.getInstance();

		try {
			cal.setTime(df.parse(savedTime));
			if (System.currentTimeMillis() > cal.getTimeInMillis()) {
				isLate = "1";
				try {
					ps = conn.prepareStatement("SELECT storeCredit, username FROM zli.costumers WHERE costumerID = ?");
					ps.setInt(1, costumerID);
					ResultSet rs = ps.executeQuery();
					if (rs.next()) {
						storeCredit = rs.getString("storeCredit");
						username = rs.getString("username");
						int iend = storeCredit.indexOf(".");
						if (iend != -1)
							storeCredit = storeCredit.substring(0, iend);
					}
				} catch (SQLException e) {
					System.out.println("changeOrderStatusToArrived failed!");
				}

				try {
					ps = conn.prepareStatement("UPDATE zli.costumers SET storeCredit = ? WHERE costumerID = ?");
				} catch (SQLException e) {
					System.out.println("Statement For Updating Store Credit for customer has failed!!");
				}
				try {
					int temp = Integer.parseInt(storeCredit) + (int) price;
					System.out.println(temp);
					ps.setString(1, String.valueOf(temp));
					ps.setInt(2, costumerID);
					ps.executeUpdate();
				} catch (Exception e) {
					System.out.println("changeOrderStatusToArrived failed!");
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} //

		try {
			ps = conn.prepareStatement("UPDATE zli.orders SET orderStatus = ? WHERE orderNumber = ?");
		} catch (SQLException e) {
			System.out.println("changeOrderStatusToArrived failed!");
		}
		try {
			ps.setString(1, "ARRIVED");
			ps.setInt(2, orderNumber);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("changeOrderStatusToArrived failed!");
		}

		try {
			ps = conn.prepareStatement("UPDATE zli.deliveries SET actualDeliveryDate = ? WHERE orderNumber = ?");
		} catch (SQLException e) {
			System.out.println("changeOrderStatusToArrived failed!");
		}
		try {
			ps.setString(1, dtf.format(now));
			ps.setInt(2, orderNumber);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("changeOrderStatusToArrived failed!");
		}

		try {
			ps = conn.prepareStatement("SELECT email FROM zli.users WHERE username = ?");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				email = rs.getString("email");
			}
		} catch (SQLException e) {
			System.out.println("changeOrderStatusToArrived failed!");
		}

		returnArrayList.add(String.valueOf(price));
		returnArrayList.add(isLate);
		returnArrayList.add(email);
		returnArrayList.add(username);
		returnArrayList.add(String.valueOf(orderNumber));
		return returnArrayList;

	}

	// This method generates Monthly Incomes Reports for a specific store
	public static void generateMonthlyIncomesReport(ArrayList<String> reportDetails) {
		if (reportDetails == null)
			throw new NullPointerException();
		String month = reportDetails.get(0), year = reportDetails.get(1), store = reportDetails.get(2);
		float ordersIncomes = 0, deliveriesIncomes = 0, refunds = 0;
		ArrayList<Order> allOrderOfTheCurrentStore = getDataOrdersByStore(store, OrderStatus.APPROVED);
		System.out.println(allOrderOfTheCurrentStore.size());
		if (allOrderOfTheCurrentStore.size() != 0) {
			for (Order order : allOrderOfTheCurrentStore) {
				if (order.getMonthOfOrderDate().equals(month) && order.getYearOfOrderDate().equals(year)) {
					ordersIncomes = ordersIncomes + order.getPrice();
					if (order.getSupplyMethod() == SupplyMethod.Priority_Delivery
							|| order.getSupplyMethod() == SupplyMethod.Standard_Delivery) {
						deliveriesIncomes = deliveriesIncomes + Delivery.deliveryPrice;
						ordersIncomes = ordersIncomes - Delivery.deliveryPrice;
					}
					refunds = refunds + order.getRefund();
				}
			}
		} else
			return;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(
					"INSERT INTO zli.monthlyincomesreports (orderIncomes, deliveriesIncomes, refunds, store, month, year) VALUES (?,?,?,?,?,?)");
		} catch (SQLException e1) {
			System.out.println("Statement Insert New monthly incomes report to monthly incomes reports table failure");
		}
		try {
			ps.setFloat(1, ordersIncomes);
			ps.setFloat(2, deliveriesIncomes);
			ps.setFloat(3, refunds);
			ps.setString(4, store);
			ps.setString(5, month);
			ps.setString(6, year);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("Executing statement-Generate New monthly incomes report to db failed!");
		}
	}

	public static void generateMonthlyOrdersReport(ArrayList<String> reportDetails) {
		String month = reportDetails.get(0), year = reportDetails.get(1), store = reportDetails.get(2);
		String mostWantedItemName = null;
		int numOfTotalOrders = 0, mostWantedMaxAmount = 0, numOfOrdersThatCanceled = 0;
		String items = "";

		HashMap<String, Integer> mapOfItems = new HashMap<String, Integer>();
		ArrayList<Order> allOrderOfTheCurrentStore = getDataOrdersByStore(store, OrderStatus.APPROVED);
		allOrderOfTheCurrentStore.addAll(getDataOrdersByStore(store, OrderStatus.NOT_APPROVED));

		ArrayList<Order> allOrderOfTheCurrentStoreThatCanceled = getDataOrdersByStore(store, OrderStatus.CANCELED);
		allOrderOfTheCurrentStoreThatCanceled.addAll(getDataOrdersByStore(store, OrderStatus.REQUEST_TO_CANCELED));

		for (Order order : allOrderOfTheCurrentStore) {
			if (order.getMonthOfOrderDate().equals(month) && order.getYearOfOrderDate().equals(year)) {
				String component = order.getComponents();
				String[] item = component.split(",");
				for (int i = 0; i < item.length; i++) {
					String itemName = item[i];
					int count = 0;
					if (!mapOfItems.containsKey(itemName)) {
						mapOfItems.put(itemName, 0);
					}
					count = mapOfItems.get(itemName) + 1;
					mapOfItems.put(itemName, count);
					if (mapOfItems.get(itemName) > mostWantedMaxAmount) {
						mostWantedMaxAmount = mapOfItems.get(itemName);
						mostWantedItemName = itemName;
					}
				}
				numOfTotalOrders++;
			}
		}
		for (Order order : allOrderOfTheCurrentStoreThatCanceled) {
			if (order.getMonthOfOrderDate().equals(month) && order.getYearOfOrderDate().equals(year)) {
				numOfOrdersThatCanceled++;
			}
		}
		for (Map.Entry<String, Integer> entry : mapOfItems.entrySet()) {
			items = items + (String) entry.getKey() + "," + entry.getValue().toString() + ",";
		}
		if (!items.equals("")) {
			items = items.substring(0, items.length() - 1);
			PreparedStatement ps = null;
			try {
				ps = conn.prepareStatement(
						"INSERT INTO zli.monthlyordersreports (items, numOfTotalOrders,numOfOrdersThatCanceled,mostWantedItemName, store, month, year) VALUES (?,?,?,?,?,?,?)");
			} catch (SQLException e1) {
				System.out.println(
						"Statement Insert New monthly incomes report to monthly incomes reports table failure");
			}
			try {
				ps.setString(1, items);
				ps.setInt(2, numOfTotalOrders);
				ps.setInt(3, numOfOrdersThatCanceled);
				ps.setString(4, mostWantedItemName);
				ps.setString(5, store);
				ps.setString(6, month);
				ps.setString(7, year);
				ps.executeUpdate();
			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.out.println("Executing statement-Generate New monthly incomes report to db failed!");
			}
		}
	}

	public static void generateMonthlySatisfactionReport(ArrayList<String> reportDetails) {
		String month = reportDetails.get(0), year = reportDetails.get(1), store = reportDetails.get(2);
		int orders = 0, complaints = 0;
		ArrayList<Order> allOrderOfTheCurrentStore = getDataOrdersByStore(store, OrderStatus.APPROVED);
		allOrderOfTheCurrentStore.addAll(getDataOrdersByStore(store, OrderStatus.ARRIVED));
		allOrderOfTheCurrentStore.addAll(getDataOrdersByStore(store, OrderStatus.NOT_APPROVED));
		for (Order order : allOrderOfTheCurrentStore) {
			if (order.getMonthOfOrderDate().equals(month) && order.getYearOfOrderDate().equals(year)) {
				orders++;
				complaints += countComplaintsByOrderNumber(order.getOrderNumber());
			}
		}

		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(
					"INSERT INTO zli.monthlysatisfactionreports (orders, complaints, store, month, year) VALUES (?,?,?,?,?)");
		} catch (SQLException e1) {
			System.out.println("Statement Insert New monthly SatisfactionReport to SatisfactionReport table failure");
		}
		try {
			ps.setInt(1, orders);
			ps.setInt(2, complaints);
			ps.setString(3, store);
			ps.setString(4, month);
			ps.setString(5, year);

			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("Executing statement-Generate New SatisfactionReport to db failed!");
		}
	}

	public static MonthlyOrdersReport getMonthlyOrdersReport(ArrayList<String> reportDetails) {
		String month = reportDetails.get(0), year = reportDetails.get(1), store = reportDetails.get(2);
		String[] itemsList = null;
		String items = null, mostWantedItemName = null;
		HashMap<String, Integer> mapOfItems = new HashMap<String, Integer>();
		int numOfTotalOrders = 0, numOfOrdersThatCanceled = 0;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(
					"SELECT * FROM zli.monthlyordersreports WHERE month = ? AND year = ? AND store = ?");
			ps.setString(1, month);
			ps.setString(2, year);
			ps.setString(3, store);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				items = rs.getString("items");
				numOfTotalOrders = rs.getInt("numOfTotalOrders");
				numOfOrdersThatCanceled = rs.getInt("numOfOrdersThatCanceled");
				mostWantedItemName = rs.getString("mostWantedItemName");
			} else
				return null;
		} catch (SQLException e) {
			System.out.println("Statement for getting Monthly Incomes Report has failed!");
		}
		System.out.println("Succefully imported Monthly Incomes Report Data!");
		String date = month + "/" + year;
		itemsList = items.split(",");
		for (int i = 0; i < (itemsList.length); i = i + 2) {
			mapOfItems.put(itemsList[i], (int) Integer.valueOf(itemsList[i + 1]));
		}

		return new MonthlyOrdersReport(mapOfItems, numOfTotalOrders, numOfOrdersThatCanceled,
				(float) numOfTotalOrders / 30, store, date, mostWantedItemName);
	}

	public static MonthlyIncomesReport getMonthlyIncomesReport(ArrayList<String> reportDetails) {
		String month = reportDetails.get(0), year = reportDetails.get(1), store = reportDetails.get(2);
		float ordersIncomes = 0, deliveriesIncomes = 0, refunds = 0;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(
					"SELECT * FROM zli.monthlyincomesreports WHERE month = ? AND year = ? AND store = ?");
			ps.setString(1, month);
			ps.setString(2, year);
			ps.setString(3, store);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				ordersIncomes = rs.getFloat("orderIncomes");
				deliveriesIncomes = rs.getFloat("deliveriesIncomes");
				refunds = rs.getFloat("refunds");
			} else
				return null;
		} catch (SQLException e) {
			System.out.println("Statement for getting Monthly Incomes Report has failed!");
		}
		System.out.println("Succefully imported Monthly Incomes Report Data!");
		String date = month + "/" + year;
		return new MonthlyIncomesReport(ordersIncomes, deliveriesIncomes, refunds, store, date);
	}

	public static QuarterlySatisfactionReport getMonthlySatisfactionReport(ArrayList<String> reportDetails) {
		String month = reportDetails.get(0), year = reportDetails.get(1), store = reportDetails.get(2);
		int orders = 0, complaints = 0;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(
					"SELECT * FROM zli.monthlysatisfactionreports WHERE month = ? AND year = ? AND store = ?");
			ps.setString(1, month);
			ps.setString(2, year);
			ps.setString(3, store);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				orders = rs.getInt("orders");
				complaints = rs.getInt("complaints");
			} else
				return null;
		} catch (SQLException e) {
			System.out.println("Statement for getting Monthly Incomes Report has failed!");
		}
		System.out.println("Succefully imported Monthly Incomes Report Data!");
		String date = month + "/" + year;
		if (orders == 0)
			return new QuarterlySatisfactionReport(orders, complaints, 0, store, date);
		return new QuarterlySatisfactionReport(orders, complaints,
				((float) (1 - ((float) (float) complaints / (float) orders)) * 100), store, date);
	}

	public static ArrayList<MonthlyIncomesReport> getQuarterlyIncomesReport(ArrayList<String> reportDetails) {
		if (reportDetails == null)
			throw new NullPointerException();
		String quarter = reportDetails.get(0), year = reportDetails.get(1), store = reportDetails.get(2);
		ArrayList<MonthlyIncomesReport> monthlyIncomesReports = new ArrayList<MonthlyIncomesReport>();
		ArrayList<String> monthInQuarter = new ArrayList<String>();
		ArrayList<String> reportDetails1 = new ArrayList<String>();
		switch (quarter) {
		case "1":
			monthInQuarter.addAll(Arrays.asList("01", "02", "03"));
			break;
		case "2":
			monthInQuarter.addAll(Arrays.asList("04", "05", "06"));
			break;
		case "3":
			monthInQuarter.addAll(Arrays.asList("07", "08", "09"));
			break;
		case "4":
			monthInQuarter.addAll(Arrays.asList("10", "11", "12"));
			break;
		}

		for (int i = 0; i < 3; i++) {
			reportDetails1.clear();
			reportDetails1.addAll(Arrays.asList(monthInQuarter.get(i), year, store));
			if (getMonthlyIncomesReport(reportDetails1) != null)
				monthlyIncomesReports.add(getMonthlyIncomesReport(reportDetails1));
		}
		return monthlyIncomesReports;
	}

	public static Object getQuarterlySatisfactionReport(ArrayList<String> reportDetails) {
		ArrayList<QuarterlySatisfactionReport> quarterlySatisfactionReports = new ArrayList<QuarterlySatisfactionReport>();
		String quarter = reportDetails.get(0), year = reportDetails.get(1), store = reportDetails.get(2);
		ArrayList<String> monthInQuarter = new ArrayList<String>();
		ArrayList<String> reportDetails1 = new ArrayList<String>();
		switch (quarter) {
		case "1":
			monthInQuarter.addAll(Arrays.asList("01", "02", "03"));
			break;
		case "2":
			monthInQuarter.addAll(Arrays.asList("04", "05", "06"));
			break;
		case "3":
			monthInQuarter.addAll(Arrays.asList("07", "08", "09"));
			break;
		case "4":
			monthInQuarter.addAll(Arrays.asList("10", "11", "12"));
			break;
		}

		for (int i = 0; i < 3; i++) {
			reportDetails1.clear();
			reportDetails1.addAll(Arrays.asList(monthInQuarter.get(i), year, store));
			if (getMonthlySatisfactionReport(reportDetails1) != null)
				quarterlySatisfactionReports.add(getMonthlySatisfactionReport(reportDetails1));
		}
		return quarterlySatisfactionReports;
	}

	public static int countComplaintsByOrderNumber(int orderNumber) {
		int numOfComplaints = 0;
		try {

			PreparedStatement ps = conn.prepareStatement("SELECT * FROM zli.complaint WHERE orderNumber = ?");
			try {
				ps.setInt(1, orderNumber);
			} catch (Exception e) {
				System.out.println("Error initialize the query");
			}
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				numOfComplaints++;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			// System.out.println("Importing orders from zli.Complaints has failed!");
		}
		return numOfComplaints;
	}

	public static boolean checkFirstOrder(int customerID) {
		boolean ans = false;
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT orderNumber FROM zli.orders where costumerID = ?");
			try {
				ps.setInt(1, customerID);
			}

			catch (Exception e) {
				System.out.println("Error initialize the query");
			}
			ResultSet rs = ps.executeQuery();
			ans = !rs.next();
		} catch (Exception e) {
			System.out.println("check first order from orders has failed");
		}

		return ans;
	}

	// get product from zli.productcatalog, if product doesn't exists returns null
	public static AbstractProduct getProductByID(int productID) {
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM zli.productcatalog WHERE id = ?");
			ps.setInt(1, productID);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return new Product(rs.getString("name"), rs.getString("imgSrc"), rs.getFloat("price"), productID);
		} catch (Exception e) {
			System.out.println("importing product by id from zli.productcatalog has failed!");
		}
		return null;
	}

	public static void deleteProductByID(int productID) {
		Sale sale = getActiveSaleById(productID);
		if (sale != null) // if product to delete is in sale, we delete it from activesales table and
							// insert it to salehistory
		{
			ArrayList<Sale> sales = new ArrayList<>();
			sales.add(sale);
			deleteInActiveSales(sales);
			AddNewSale(sale, "saleshistory");
		}
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM zli.productcatalog WHERE id = ?");
			ps.setInt(1, productID);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("Deleting product from zli.productcatalog has failed!");
		}
	}

	public static void AddNewProduct(Product product) {
		int id = getNextProductID();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(
					"INSERT INTO zli.productcatalog (id, name, imgSrc, color, category, components, price, type) "
							+ "VALUES(?,?,?,?,?,?,?,?)");
		} catch (Exception e) {
			System.out.println("Statement Insert New Product to zli.productcatalog failure");
		}
		try {
			ps.setInt(1, id);
			ps.setString(2, product.getName());
			ps.setString(3, product.getImgSrc());
			ps.setString(4, product.getColor());
			ps.setString(5, product.getCategory());
			ps.setString(6, product.getComponents());
			ps.setFloat(7, product.getPrice());
			ps.setString(8, product.getType());
			ps.executeUpdate();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("Executing statement-Insert New Product to zli.productcatalog failed!");
		}
	}

	public static void updatePriceInCatalog(ArrayList<Float> info) {
		int id = (int) (float) info.get(0);
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("UPDATE zli.productcatalog SET price = ? WHERE id = ?");
		} catch (SQLException e1) {
			System.out.println("Statement For Updating Price for ID: " + id + " has FAILED!");
		}
		try {
			ps.setInt(2, id);
			ps.setFloat(1, info.get(1));
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("Executing statement-Updating  Price for ID: " + id + " has FAILED!\"");
		}
		System.out.println("Succefully Updated Price for ID: " + id);
	}

	public static int getNextSaleID(String TableName) {
		PreparedStatement ps = null;
		int max = 0;
		try {
			ps = conn.prepareStatement("Select MAX(id) as Max_ID FROM zli." + TableName);
			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next())
					max = rs.getInt("Max_ID");
			}
		} catch (SQLException e) {
			System.out.println("Executing statement-> Slecet MAX(ID) from productcatalog failed!");
		}
		return ++max;
	}

	public static void AddNewSale(Sale sale, String TableName) {
		int id;
		if (TableName.equals("saleshistory"))
			id = sale.getId();
		int idactive = getNextSaleID("activesales");
		int idhistory = getNextSaleID("saleshistory");
		id = Math.max(idactive, idhistory);
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("INSERT INTO zli." + TableName + " (id, productID, startDate, endDate, price) "
					+ "VALUES(?,?,?,?,?)");
		} catch (Exception e) {
			System.out.println("Statement Insert New Product to zli." + TableName + " failure");
		}
		try {
			ps.setInt(1, id);
			ps.setInt(2, sale.getProductID());
			ps.setString(3, sale.getStartDate());
			ps.setString(4, sale.getEndDate());
			ps.setFloat(5, sale.getPrice());
			ps.executeUpdate();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("Executing statement-Insert New Product to zli." + TableName + " failed!");
		}
	}

	public static ArrayList<Sale> getActiveSales() {
		ArrayList<Sale> sales = new ArrayList<>();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM zli.activesales");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				sales.add(new Sale(rs.getInt("id"), rs.getInt("productID"), rs.getString("startDate"),
						rs.getString("endDate"), rs.getFloat("price")));
			}
		} catch (Exception e) {
			System.out.println("Importing active sales from zli.activesales has failed!");
		}
		System.out.println("Importing active sales from zli.activesales successfull!");

		ArrayList<Sale> inActiveSales = new ArrayList<>();
		for (int i = 0; i < sales.size(); i++) {
			if (Time.calculateTimeDiff(sales.get(i).getEndDate() + ":00") < 0) {
				inActiveSales.add(sales.get(i));
				sales.remove(i);
				i--;
			}
		}
		deleteInActiveSales(inActiveSales);
		// add to inactive table
		for (Sale sale : inActiveSales) {
			AddNewSale(sale, "saleshistory");
		}
		return sales;
	}

	private static Sale getActiveSaleById(int productID) {
		try {
			PreparedStatement ps = conn.prepareStatement("Select * FROM zli.activesales WHERE productID = ?");
			ps.setInt(1, productID);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return new Sale(rs.getInt("id"), rs.getInt("productID"), rs.getString("startDate"),
						rs.getString("endDate"), rs.getFloat("price"));
		} catch (Exception e) {
			System.out.println("get sale from zli.activesales by productID has failed!");
		}
		return null;
	}

	private static void deleteInActiveSales(ArrayList<Sale> inActiveSales) {
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM zli.activesales WHERE id = ?");
			for (Sale sale : inActiveSales) {
				ps.setInt(1, sale.getId());
				ps.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("Deleting product from zli.activesales has failed!");
		}
	}

	public static boolean CheckActiveSaleById(int id) {
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM zli.activesales WHERE productID= ?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return false;
		} catch (Exception e) {
			System.out.println("Searching zli.activesales with Prodcut ID: " + id + " has Failed!");
		}
		System.out.println("There is no Active Sale for Product ID: " + id);
		return true;

	}

	public static ArrayList<String> SavePDFInDB(MyFile myfile) throws FileNotFoundException {
		ArrayList<String> analyzeid = new ArrayList<>();
		File newFile = null;
		FileOutputStream fileOut;
		try {
			newFile = new File(myfile.getFileName());
			fileOut = new FileOutputStream(newFile);
			BufferedOutputStream bufferOut = new BufferedOutputStream(fileOut);
			try {
				bufferOut.write(myfile.getMybytearray(), 0, myfile.getSize());
				fileOut.flush();
				bufferOut.flush();
				bufferOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO zli.analyzedsurveys set analyzeFile= ?");
			ps.setBinaryStream(1, new FileInputStream(newFile));
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("PDF FAILED");
			e.printStackTrace();
		}
		String id = String.valueOf(getLastanalyzeID());
		analyzeid.add(id);
		return analyzeid;
	}

	private static int getLastanalyzeID() {
		PreparedStatement ps = null;
		int max = 0;
		try {
			ps = conn.prepareStatement("Select MAX(analyzeID) as Max_ID FROM zli.analyzedsurveys");
			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next())
					max = rs.getInt("Max_ID");
			}
		} catch (SQLException e) {
			System.out.println("Executing statement-> Slecet MAX(ID) from anaylzedsurveys failed!");
		}
		return max;
	}

	public static void importusersfromexternalsystem() {
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		try {
			ps = conn.prepareStatement("SELECT * FROM externalsystem.externalusermanagmentsystem");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				try {
					ps1 = conn.prepareStatement(
							"INSERT INTO externalsystem.users (username, password, firstName, lastName, role, email, phoneNumber, isLoggedIn, id, storeName) VALUES (?,?,?,?,?,?,?,?,?,?)");
				} catch (SQLException e1) {
					System.out.println("Statement importusersfromexternalsystem failure");
				}
				try {
					ps1.setString(1, rs.getString("username"));
					ps1.setString(2, rs.getString("password"));
					ps1.setString(3, rs.getString("firstName"));
					ps1.setString(4, rs.getString("lastName"));
					ps1.setString(5, rs.getString("role"));
					ps1.setString(6, rs.getString("email"));
					ps1.setString(7, rs.getString("phoneNumber"));
					ps1.setBoolean(8, rs.getBoolean("isLoggedIn"));
					ps1.setString(9, rs.getString("id"));
					ps1.setString(10, rs.getString("storeName"));
					ps1.executeUpdate();
				} catch (Exception e) {
					System.out.println(e.getMessage());
					System.out.println("Executing importusersfromexternalsystem failed!");
				}
			}
		} catch (Exception e) {
			System.out.println("importusersfromexternalsystem has failed");
		}
		try {
			ps = conn.prepareStatement("SELECT * FROM externalsystem.users where role = ?");
			try {
				ps.setString(1, "Costumer");
			} catch (Exception e) {
				System.out.println("importusersfromexternalsystem has failed");
			}
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				try {
					ps1 = conn.prepareStatement(
							"INSERT INTO externalsystem.costumers (username, debt, permissions, storeCredit) VALUES (?,?,?,?)");
				} catch (SQLException e1) {
					System.out.println(
							"Statement Insert New monthly incomes report to monthly incomes reports table failure");
				}
				try {
					ps1.setString(1, rs.getString("username"));
					ps1.setString(2, "0");
					ps1.setString(3, "NOT_APPROVED");
					ps1.setString(4, "0");
					ps1.executeUpdate();
				} catch (Exception e) {
					System.out.println(e.getMessage());
					System.out.println("Executing statement-Generate New monthly incomes report to db failed!");
				}
			}
		} catch (Exception e) {
			System.out.println("importusersfromexternalsystem has failed");
		}

		try {
			ps = conn.prepareStatement("SELECT * FROM externalsystem.users WHERE role = ? OR role = ?");
			try {
				ps.setString(1, "Store_Employee");
				ps.setString(2, "Store_Manager");

			} catch (Exception e) {
				System.out.println("importusersfromexternalsystem has failed");
			}
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				try {
					ps1 = conn.prepareStatement(
							"INSERT INTO externalsystem.storestaff (username, storeName, permissions) VALUES (?,?,?)");
				} catch (SQLException e1) {
					System.out.println(
							"Statement Insert New monthly incomes report to monthly incomes reports table failure");
				}
				try {
					ps1.setString(1, rs.getString("username"));
					ps1.setString(2, rs.getString("storeName"));
					ps1.setString(3, "REGULAR");
					ps1.executeUpdate();
				} catch (Exception e) {
					System.out.println(e.getMessage());
					System.out.println("Executing statement-Generate New monthly incomes report to db failed!");
				}
			}
		} catch (Exception e) {
			System.out.println("importusersfromexternalsystem has failed");
		}

	}
}
