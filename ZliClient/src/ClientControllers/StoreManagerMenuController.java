package ClientControllers;

import Enums.Task;
import client.ZliClient;
import client.ZliClientUI;
import common.ChangeScene;
import common.Message;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The class StoreManagerMenuController is the Controller of "StoreManagerMenu" Screen
 */
public class StoreManagerMenuController {

    @FXML
    private Button approveCostumerBtn;

    @FXML
    private Button LogOutBtn;
    
    @FXML
    private Button monthlyReportsBtn;

    @FXML
    private Button approveOrdersBtn;

    @FXML
    private Button approveOrdersCancelationBtn;
    
    @FXML
    private Button manageCostumersPermissionsBtn;

    @FXML
    private Button manageStorestaffPermissionsBtn;
    
    @FXML
    private Button quarterlyReportsBtn;
    
    @FXML
    private Text welcomeLabel;
    
    @FXML
    private Button XBtn;

    @FXML
    private FontAwesomeIconView XIcon;

    private ChangeScene scene = new ChangeScene();
    
	/**
	 * This method initiate the screen of the relevance FXML.
	 */
    @FXML
	public void initialize() {
    	welcomeLabel.setText("Welcome back " + ZliClient.userController.getUser().getFirstName());
    	ZliClientUI.chat.accept(new Message(Task.Get_user_store, ZliClient.userController.getUser()));
	}
    
	/**
	 * This method terminates storemanagermenu scene and opens the ApproveCostumers screen.
	 * @param event mouse clicked on ApproveCostumer button
	 */
    @FXML
    void ApproveCostumerClicked(ActionEvent event) {
    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		scene.changeScreen(new Stage(), "/GuiClientScreens/ApproveCostumers.fxml", true);
    }
    
	/**
	 * This method terminates storemanagermenu scene and opens the login screen. ask server to update database that user is loged
	 * out
	 * @param event mouse clicked on LogOut button
	 */
    @FXML
    void LogOut(ActionEvent event) {
    	ZliClientUI.chat.accept(new Message(Task.Request_logout, ZliClient.userController.getUser().getUsername()));
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		scene.changeScreen(new Stage(), "/GuiClientScreens/LoginZLI.fxml", true);
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

    @FXML
    void approveOrdersCancelationClicked(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		scene.changeScreen(new Stage(), "/GuiClientScreens/ApproveOrdersCancelation.fxml", true);
    }

    @FXML
    void approveOrdersClicked(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		scene.changeScreen(new Stage(), "/GuiClientScreens/ApproveOrders.fxml", true);
    }
    

    @FXML
    void manageCostumersPermissionsClicked(ActionEvent event) {
    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		scene.changeScreen(new Stage(), "/GuiClientScreens/ManageCostumersPermissions.fxml", true);
    }

    @FXML
    void manageStorestaffPermissionsClicked(ActionEvent event) {
    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		scene.changeScreen(new Stage(), "/GuiClientScreens/ManageStorestaffPermissions.fxml", true);
    }

    @FXML
    void monthlyReportsClicked(ActionEvent event) {
    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		scene.changeScreen(new Stage(), "/GuiClientScreens/ChooseMonthlyReports.fxml", true);
    }

    @FXML
    void quarterlyReportsClicked(ActionEvent event) {
    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		scene.changeScreen(new Stage(), "/GuiClientScreens/ChooseQuarterlyReports.fxml", true);
    }
}
