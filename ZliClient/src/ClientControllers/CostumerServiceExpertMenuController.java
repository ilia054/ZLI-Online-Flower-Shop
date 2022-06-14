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
 * GUI Controller of CostumerServiceExpertMenu
 */
public class CostumerServiceExpertMenuController {

    @FXML
    private Button LogOutBtn;

    @FXML
    private Button manageSurvey;

    @FXML
    private Button XBtn;

    @FXML
    private Text welcomeLabel;
    
    private ChangeScene screen = new ChangeScene();

    @FXML
    private FontAwesomeIconView XIcon;
    
    @FXML
  	public void initialize() {
    	welcomeLabel.setText("Welcome back " + ZliClient.userController.getUser().getFirstName() + " " + ZliClient.userController.getUser().getLastName() );
      }

    public void LogOut(ActionEvent event) throws Exception {
    	ZliClientUI.chat.accept(new Message(Task.Request_logout, ZliClient.userController.getUser().getUsername()));
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		screen.changeScreen(new Stage(),"/GuiClientScreens/LoginZLI.fxml", true);
    }


    @FXML
    void ManageSurvey(ActionEvent event) {
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		screen.changeScreen(new Stage(),"/GuiClientScreens/ManageSurveyMenu.fxml", true);
    }

    @FXML
    public void X(ActionEvent event) throws Exception {
    	ZliClientUI.chat.accept(new Message(Task.Request_logout, ZliClient.userController.getUser().getUsername()));
    	ZliClientUI.chat.accept(new Message(Task.Request_disconnected, null));
    	System.exit(0);
    }

}
