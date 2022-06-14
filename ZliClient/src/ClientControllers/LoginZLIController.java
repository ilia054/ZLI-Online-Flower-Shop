package ClientControllers;


import java.util.ArrayList;
import EntityControllers.UserController;
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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * This class is the GUI Controller of LoginZLI, it has all of the functionality that is
 * built in to the GUI, That means its the "Back-End" of our Buttons that are displayed in the GUI
 */
public class LoginZLIController {

	@FXML
	private TextField username;

	@FXML
	private PasswordField password;

    @FXML
    private Button LoginBtn;
    
    @FXML
    private FontAwesomeIconView xBTN;
    
    @FXML
    private Label errorLabel;
    
    private ChangeScene screen = new ChangeScene();
    
    public String role;
    
    public void initialize() {
    	errorLabel.setVisible(false);
    }
    
    public UserController userController;
    
/**
* This method is called when the User clicks on "Login"
* this will close the current Scene and will display the CostumerMenu screen
* @param event of the current Screen that is displayed  
* @throws Exception if ActionEvent fails
*/    
    public void Login(ActionEvent event) throws Exception {
    	privateLogin(event);
	}
	
    private String privateLogin(ActionEvent event) {
		userLoginAddAndSet();
		if(!userController.isExist()) {
			//In case the user login input was invalid (username/password) - error label will be shown
			setErrorTxtUserNotExists();
			return "UserNotExists";
		}
		else {
			if(userController.getUser().getLoginStatus() == true) {
				setErrorTxtAlreadyLoggedIN();
				return "AlreadyLoggedIN";
			}
			else {
				role = userController.getUser().getRole().toString();
				showRelevanceGui(event, role);
				return "showRelevanceGui " + role;
			}
		}
    }
    
	public void showRelevanceGui(ActionEvent event, String role) {
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		screen.changeScreen(new Stage(),"/GuiClientScreens/"+ role, true);
	}
	
	public void setErrorTxtAlreadyLoggedIN()
	{
		errorLabel.setVisible(true);
		errorLabel.setText(Task.Logged_UnSuccessful_already_logged_in.toString());
	}
	
	public void setErrorTxtUserNotExists()
	{
		errorLabel.setVisible(true);
		errorLabel.setText("Wrong username OR password! Try again!");
	}

	public void userLoginAddAndSet()
	{
		ArrayList<String> userLoginInput = new ArrayList<>();
		userLoginInput.add(username.getText());
		userLoginInput.add(password.getText());
		ZliClientUI.chat.accept(new Message(Task.Request_Login, userLoginInput));
		userController = ZliClient.userController;
	}
	
/**
* This method is called when the User clicks on "X"
* This method will close the connection to the client and close all of the Scenes
* @param event that event that happened in the Scene 
*/  
    @FXML
    void clickedX(MouseEvent event) {
    	ZliClientUI.chat.accept(new Message(Task.Request_disconnected, null));
    	System.exit(0);
    }
}
