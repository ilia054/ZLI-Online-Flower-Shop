package ClientControllers;

import EntityControllers.StringController;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * GUI Controller of StoreEmployeeMenu
 */
public class StoreEmployeeMenuController {

    @FXML
    private Button LogOutBtn;

    @FXML
    private Button submitSurveyBtn;

    @FXML
    private Button XBtn;

    @FXML
    private FontAwesomeIconView XIcon;

    @FXML
    private Label ErrorSurveyMenu;


    @FXML
    private Text welcomeLabel;
    @FXML
    private Text MarketingMenuTXT;
    private ChangeScene screen = new ChangeScene();
	private ObservableList<String> permission = FXCollections.observableArrayList();
	/** 
	 * When the user enters this GUI screen he will see the menu of Store Employee
	 * only user with the permission "INPUT_SURVEY" will be able to go further in this menu 
	 * @author Shay Zak
	 */
    @FXML
	public void initialize() {
    	welcomeLabel.setText("Welcome back " + ZliClient.userController.getUser().getFirstName() + " " + ZliClient.userController.getUser().getLastName() );
    	ZliClientUI.chat.accept(new Message(Task.Get_user_store, ZliClient.userController.getUser()));
		submitSurveyBtn.setDisable(false);
		ErrorSurveyMenu.setVisible(false);
		String username=ZliClient.userController.getUser().getUsername();
		Message message = new Message(Task.Get_User_Permission, username);
		ZliClientUI.chat.accept(message);
		permission=StringController.getString();
		if(!permission.get(0).equals("INPUT_SURVEY")) {
		submitSurveyBtn.setDisable(true);
		ErrorSurveyMenu.setVisible(true);
		MarketingMenuTXT.setText(MarketingMenuTXT.getText() + " here You can:\n1) Submit a new survey");
		}
	}
	
    public void LogOut(ActionEvent event) throws Exception {
    	ZliClientUI.chat.accept(new Message(Task.Request_logout, ZliClient.userController.getUser().getUsername()));
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		screen.changeScreen(new Stage(),"/GuiClientScreens/LoginZLI.fxml", true);
    }

    @FXML
    public void X(ActionEvent event) throws Exception {
    	ZliClientUI.chat.accept(new Message(Task.Request_logout, ZliClient.userController.getUser().getUsername()));
    	ZliClientUI.chat.accept(new Message(Task.Request_disconnected, null));
    	System.exit(0);
    }

    @FXML
    void submitSurvey(ActionEvent event) {
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		screen.changeScreen(new Stage(),"/GuiClientScreens/SubmitSurvery.fxml", true);
    }

}
