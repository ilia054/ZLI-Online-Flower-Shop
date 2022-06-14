package ClientControllers;

import java.util.ArrayList;

import Entities.Storestaff;
import EntityControllers.StorestaffController;
import Enums.StorestaffPermissions;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * The class ManageStorestaffPermissionsController is the Controller of "ManageStorestaffPermissions" Screen
 */
public class ManageStorestaffPermissionsController {
	
	@FXML
    private Button XBtn;

    @FXML
    private Label storeName;

    @FXML
    private TableView<Storestaff> storestaffTable;

    @FXML
    private TableColumn<Storestaff, Integer> storeEmployeeID;

    @FXML
    private TableColumn<Storestaff, String> username;

    @FXML
    private TableColumn<Storestaff, StorestaffPermissions> permissions;
	
    @FXML
    private FontAwesomeIconView refreshBTN;

    @FXML
    private FontAwesomeIconView saveBTN;

    @FXML
    private FontAwesomeIconView BackBtn;
    
	@FXML
	private HBox SaveBtn;

	@FXML
	private FontAwesomeIconView RefreshBtn;

	private ObservableList<Storestaff> storestaff = FXCollections.observableArrayList();
	private StorestaffPermissions[] storestaffPermissions = new StorestaffPermissions[] {StorestaffPermissions.REGULAR, StorestaffPermissions.INPUT_SURVEY};
	private ArrayList<Storestaff> updatedStorestaff = new ArrayList<>();
	private ChangeScene scene = new ChangeScene();

	/**
	 * This method is automatically called when the screen is about to be displayed
	 * to the User It initializes all of the scene's components (ComboBox,
	 * TableViews, Data, Buttons and etc...) This functions calls Most of the
	 * methods of other functions to implements the functionality of the scene
	 */
	@FXML
	public void initialize() {
		setColumnsInTable();
		ZliClientUI.chat.accept(new Message(Task.Import_data_storestaff_of_the_same_store, ZliClient.userController.getUser()));
		storestaff = StorestaffController.getStorestaffFromTheSameStore();
		storestaffTable.setItems(storestaff);
		createDropBoxInCell(permissions);
		setEditablePermissionsCell(permissions);
		storestaffTable.autosize();
		storestaffTable.setEditable(true);
		storeName.setVisible(false);
		storeName.setVisible(true);
		storeName.setText(ZliClient.userController.getUser().getStore());
	}

	/**
	 * This method create Drop Box In order status cell in the table on the screen.
	 * @param Column
	 */
	private void createDropBoxInCell(TableColumn<Storestaff, StorestaffPermissions> Column) {
		permissions.setCellFactory((param) -> new ComboBoxTableCell<>(new StringConverter<StorestaffPermissions>() {

			@Override
			public String toString(StorestaffPermissions object) {
				return object.toString();
			}

			@Override
			public StorestaffPermissions fromString(String string) {
				return StorestaffPermissions.valueOf(string);
			}

		}, storestaffPermissions));
	}

	/**
	 * This method creates and sets the Cell value in our TableView
	 */
	private void setColumnsInTable() {
		storeEmployeeID.setCellValueFactory(new PropertyValueFactory<Storestaff, Integer>("storeEmployeeID"));
		username.setCellValueFactory(new PropertyValueFactory<Storestaff, String>("username"));
		permissions.setCellValueFactory(new PropertyValueFactory<Storestaff, StorestaffPermissions>("permissions"));
	}

	/**
	 * This method creates an Event each time a User changes the permissions So if a user
	 * Select a different permissions for the order, lets say REGULAR->INPUT_SURVEY and saves the
	 * change in the local memory of the Orders in the TableView
	 * @param permissions
	 */
	private void setEditablePermissionsCell(TableColumn<Storestaff, StorestaffPermissions> permissions) {
		permissions.setOnEditCommit(event -> {
			((Storestaff) event.getRowValue()).setPermissions(event.getNewValue());
		});
	}

	/**
	 * This method is connect to the Save Button on the GUI scene When a user clicks
	 * on "Save" we collect the locally store Orders in the TableView and pass it on
	 * to the server, the server runs a Query and saves the changes in the DB we
	 * create Message and send the correct Message info and the Updated orders List
	 * @param event
	 */
	@FXML
	void saveClicked(MouseEvent event) {
		for (int i = 0; i < storestaffTable.getItems().size(); i++)
			updatedStorestaff.add(storestaffTable.getItems().get(i));
		ZliClientUI.chat.accept(new Message(Task.Update_storestaff_table, updatedStorestaff));
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
		storestaffTable.getItems().clear();
		updatedStorestaff.clear();
		ZliClientUI.chat.accept(new Message(Task.Import_data_storestaff_of_the_same_store, ZliClient.userController.getUser()));
		storestaff = StorestaffController.getStorestaffFromTheSameStore();
		storestaffTable.setItems(storestaff);
		storestaffTable.refresh();	
	}
}


