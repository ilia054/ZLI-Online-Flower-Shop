package ServerControllers;

import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

import Entities.ConnectedClient;
import Enums.Store;
import common.Time;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import server.Console;
import server.MySqlConnection;
import server.ZliServer;
import server.ZliServerUi;

/**
 * The class ZliServerUIController is the Controller of "ZliServerUI" Screen
 */
public class ZliServerUIController {

	@FXML
	AnchorPane Anchor;
	@FXML
	private TextArea Console;
	@FXML
	private TextField IPTXT;

	@FXML
	private TextField PortTXT;

	@FXML
	private TextField DBNameTXT;

	@FXML
	private TextField DBUserNameTXT;

	@FXML
	private PasswordField DBPasswordTXT;

	@FXML
	private TableView<ConnectedClient> connectedClients;

	@FXML
	private TableColumn<ConnectedClient, String> IP;

	@FXML
	private TableColumn<ConnectedClient, String> Host;

	@FXML
	private TableColumn<ConnectedClient, String> Status;

	@FXML
	private Button importDataBtn;

	@FXML
	private Button ConnectBtn;

	@FXML
	private Button DisconnectBtn;
	// this replaces the console stream into GUI
	private PrintStream replaceConsole;

	public String getLocalIp() {
		String ip = null;
		try {
			ip = Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {

			e.printStackTrace();
		}
		return ip;
	}

	/**
	 * This method initiate the screen of the relevance FXML.
	 */
	@FXML
	public void initialize() {
		// Initialize the table columns about the connected clients detail
		connectedClients.setItems(ZliServer.getClientList());
		// Setting up our TableView columns
		setTableColumns();
		// Change output stream into the ServerGUI Console Area
		consoleStreamIntoGUI();
		IPTXT.setText(getLocalIp());
		PortTXT.setText("5555");
		DBNameTXT.setText("jdbc:mysql://localhost/zli?serverTimezone=IST");
		DBUserNameTXT.setText("root");
		DBPasswordTXT.setText("");
		DisconnectBtn.setDisable(true);
	}
	
	/**
	 * This function sets up the TableView columns into the table
	 */
	private void setTableColumns() {
		IP.setCellValueFactory(new PropertyValueFactory<ConnectedClient, String>("ip"));
		Host.setCellValueFactory(new PropertyValueFactory<ConnectedClient, String>("host"));
		Status.setCellValueFactory(new PropertyValueFactory<ConnectedClient, String>("status"));
	}

	@FXML
	/**
	 * consoleStreamIntoGUI replace the System stream output console into our ZliServerUI FXML component
	 */
	void consoleStreamIntoGUI() {
		replaceConsole = new PrintStream(new Console(Console));
		System.setOut(replaceConsole);
		System.setErr(replaceConsole);
	}

	@FXML
	/**
	 * Connect functions sets up a connection between our server and our MySql DataBase
	 * @param event mouse clicked on Connect button
	 */
	void Connect(ActionEvent event) throws InterruptedException {
		ZliServerUi.runServer(PortTXT.getText(), DBNameTXT.getText(), DBUserNameTXT.getText(), DBPasswordTXT.getText());
		ConnectBtn.setDisable(true);
		DisconnectBtn.setDisable(false);
		disableDataInput(true);
	}

	/**
	 * Disconnect functions Disconnect the connection between our server and our MySql DataBase
	 * @param event mouse clicked on Disconnect button
	 */
	@FXML
	void Disconnect(ActionEvent event) {
		ZliServerUi.disconnect();
		DisconnectBtn.setDisable(true);
		ConnectBtn.setDisable(false);
		disableDataInput(false);

	}

	/**
	 * Closes the applications and sends a Request_disconnected to server side
	 * @param event mouse clicked on X Icon
	 **/
	@FXML
	void X(ActionEvent event) {
		System.exit(0);
	}
	/**
	 * This functions turns off the option to pass data in the TextArea
	 */
	void disableDataInput(boolean Condition) {
		IPTXT.setDisable(Condition);
		PortTXT.setDisable(Condition);
		DBNameTXT.setDisable(Condition);
		DBUserNameTXT.setDisable(Condition);
		DBPasswordTXT.setDisable(Condition);
	}

	/**
	 * This method import all users from "external system" to zli users table on database. also check the current date, if it end of month it
	 * will create all the required reports and ave them in the data base.
	 * @param event mouse clicked on importData button
	 **/
	@FXML
	void importDataClicked(ActionEvent event) {
		MySqlConnection.importusersfromexternalsystem();
		ArrayList<String> currentDate = Time.getCurrentMonthAndYearOnlyIfItIsTheEndOfTheMonth();
		if (currentDate != null) {
			for (Store storeName : Store.values()) {
				MySqlConnection.generateMonthlyIncomesReport(new ArrayList<String>(
						Arrays.asList(currentDate.get(0), currentDate.get(1), storeName.toString())));
				MySqlConnection.generateMonthlyOrdersReport(new ArrayList<String>(
						Arrays.asList(currentDate.get(0), currentDate.get(1), storeName.toString())));
				MySqlConnection.generateMonthlySatisfactionReport(new ArrayList<String>(
						Arrays.asList(currentDate.get(0), currentDate.get(1), storeName.toString())));
			}
		}
	}
}
