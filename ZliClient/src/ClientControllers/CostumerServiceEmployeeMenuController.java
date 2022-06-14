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
 * GUI Controller of CostumerServiceEmployeeMenu
 */
public class CostumerServiceEmployeeMenuController {

    @FXML
    private Button LogOutBtn;

    @FXML
    private Button submitComplaintsBtn;

    @FXML
    private Button XBtn;

    @FXML
    private Text welcomeLabel;
    
    @FXML
    private FontAwesomeIconView XIcon;

    private ChangeScene screen = new ChangeScene();	
    @FXML
    private Button handleCOMPLAINTbutton;

    @FXML
	public void initialize() {
    	welcomeLabel.setText("Welcome back " + ZliClient.userController.getUser().getFirstName() + " " + ZliClient.userController.getUser().getLastName() );
    }
	
    
    @FXML
    void LogOut(ActionEvent event) {
    	ZliClientUI.chat.accept(new Message(Task.Request_logout, ZliClient.userController.getUser().getUsername()));
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		screen.changeScreen(new Stage(),"/GuiClientScreens/LoginZLI.fxml", true);
    }
    @FXML
    void X(ActionEvent event) {
    	ZliClientUI.chat.accept(new Message(Task.Request_logout, ZliClient.userController.getUser().getUsername()));
    	ZliClientUI.chat.accept(new Message(Task.Request_disconnected, null));
		System.exit(0);
    }

    @FXML
    void submitComplaints(ActionEvent event) {
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		screen.changeScreen(new Stage(),"/GuiClientScreens/SubmitComplaint.fxml", true);
    }
    @FXML
    void handleComplaints(ActionEvent event) {
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		screen.changeScreen(new Stage(),"/GuiClientScreens/ManageComplaints.fxml", true);
    	
    }
    
}
