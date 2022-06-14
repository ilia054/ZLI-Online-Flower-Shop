package ClientControllers;

import java.util.ArrayList;

import Entities.Costumer;
import EntityControllers.CostumerController;
import Enums.CostumerPremissions;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * The class ManageCostumersPermissionsController is the Controller of "ManageCostumersPermissions" Screen
 */
public class ManageCostumersPermissionsController {
	
	@FXML
    private Button XBtn;

    @FXML
    private FontAwesomeIconView refreshBTN;

    @FXML
    private FontAwesomeIconView saveBTN;

    @FXML
    private TableView<Costumer> CostumersTable;

    @FXML
    private TableColumn<Costumer, Integer> costumerID;

    @FXML
    private TableColumn<Costumer, String> username;

    @FXML
    private TableColumn<Costumer, String> debt;

    @FXML
    private TableColumn<Costumer, CostumerPremissions> permissions;

    @FXML
    private TableColumn<Costumer, String> storeCredit;

    @FXML
    private TableColumn<Costumer, String> creditCard;

    @FXML
    private TableColumn<Costumer, String> expirationDate;

    @FXML
    private TableColumn<Costumer, String> cvv;

    @FXML
    private FontAwesomeIconView BackBtn;
    
	@FXML
	private HBox SaveBtn;

	@FXML
	private FontAwesomeIconView RefreshBtn;

	private ObservableList<Costumer> costumers = FXCollections.observableArrayList();
	private CostumerPremissions[] costumerPermissions = new CostumerPremissions[] {CostumerPremissions.FROZEN, CostumerPremissions.APPROVED};
	private ArrayList<Costumer> updatedCostumers = new ArrayList<>();
	private ChangeScene scene = new ChangeScene();

	/**
	 * This method initiate the screen of the relevance FXML. 
	 */
	@FXML
	public void initialize() {
		setColumnsInTable();
		ZliClientUI.chat.accept(new Message(Task.Import_data_costumers_approved_and_frozen, null));
		costumers = CostumerController.getApprovedOrFrozenCostumers();
		CostumersTable.setItems(costumers);
		createDropBoxInCell(permissions);
		setEditablePermissionsCell(permissions);
		CostumersTable.autosize();
		CostumersTable.setEditable(true);
	}

	/**
	 * This method create Drop Box In order status cell in the table on the screen.
	 * @param Column
	 */
	private void createDropBoxInCell(TableColumn<Costumer, CostumerPremissions> Column) {
		permissions.setCellFactory((param) -> new ComboBoxTableCell<>(new StringConverter<CostumerPremissions>() {

			@Override
			public String toString(CostumerPremissions object) {
				return object.toString();
			}

			@Override
			public CostumerPremissions fromString(String string) {
				return CostumerPremissions.valueOf(string);
			}

		}, costumerPermissions));
	}

	/**
	 * This method set the columns of the table on the screen.
	 */
	private void setColumnsInTable() {
		costumerID.setCellValueFactory(new PropertyValueFactory<Costumer, Integer>("costumerID"));
		username.setCellValueFactory(new PropertyValueFactory<Costumer, String>("username"));
		debt.setCellValueFactory(new PropertyValueFactory<Costumer, String>("debt"));
		permissions.setCellValueFactory(new PropertyValueFactory<Costumer, CostumerPremissions>("permissions"));
		storeCredit.setCellValueFactory(new PropertyValueFactory<Costumer, String>("storeCredit"));
		creditCard.setCellValueFactory(new PropertyValueFactory<Costumer, String>("creditCard"));
		expirationDate.setCellValueFactory(new PropertyValueFactory<Costumer, String>("expirationDate"));
		cvv.setCellValueFactory(new PropertyValueFactory<Costumer, String>("cvv"));
	}

	/**
	 * This method count and save the orders that changed in the table on screen.
	 * @param orderStatus
	 */
	private void setEditablePermissionsCell(TableColumn<Costumer, CostumerPremissions> permissions) {
		permissions.setOnEditCommit(event -> {
			((Costumer) event.getRowValue()).setPermissions(event.getNewValue());
		});
	}

	/**
	 * This method is connect to the Save Button on the GUI scene When a user clicks
	 * on "Save" we collect the locally store Orders in the TableView and pass it on
	 * to the server, the server runs a Query and saves the changes in the DB we
	 * create Message and send the correct Message info and the Updated orders List
	 * @param event mouse clicked on save button
	 */
	@FXML
	void saveClicked(MouseEvent event) {
		for (int i = 0; i < CostumersTable.getItems().size(); i++)
			updatedCostumers.add(CostumersTable.getItems().get(i));
		ZliClientUI.chat.accept(new Message(Task.Update_costumer_table, updatedCostumers));
		scene.HelpMessage("The changes have been saved successfully");
	}

	/**
	 * This method terminates ManageCostumersPermissionsController scene and opens the StoreManagerMenu screen
	 * @param event mouse clicked on Back button
	 * @throws Exception if MouseEvent fails
	 */
	@FXML
	public void Back(MouseEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		// scene.BackBtnOnManageOrders(new Stage());
		scene.changeScreen(new Stage(), "/GuiClientScreens/StoreManagerMenu.fxml", true);
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
	 * This method is called when the User clicks on "Refresh" the method will
	 * reload the Order's that are in the DB so if a user made a Change and clicked
	 * on Save the refresh button will display the Orders that's in the DB
	 * @param event mouse clicked on refreshed button
	 */
	@FXML
	void refreshedClicked(MouseEvent event) {
		CostumersTable.getItems().clear();
		updatedCostumers.clear();
		ZliClientUI.chat.accept(new Message(Task.Import_data_costumers_approved_and_frozen, null));
		costumers = CostumerController.getApprovedOrFrozenCostumers();
		CostumersTable.setItems(costumers);
		CostumersTable.refresh();	
	}
}
