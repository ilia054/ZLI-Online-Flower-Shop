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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * GUI Controller of DeliveryManMenu
 */
public class DeliveryManMenuController {

    @FXML
    private Button LogOutBtn;

    @FXML
    private Button XBtn;
    
    @FXML
    private Label errorTxt;
    
    @FXML
    private TextField orderNumberTxt;
    
    @FXML
    private Button approveDeliveryBtn;

    @FXML
    private Text welcomeLabel;
    
    @FXML
    private FontAwesomeIconView XIcon;
    
    private ChangeScene screen = new ChangeScene();
	
    public void initialize() {
    	welcomeLabel.setText("Welcome back " + ZliClient.userController.getUser().getFirstName());
    	orderNumberTxt.clear();
    	orderNumberTxt.setDisable(false);
    	errorTxt.setVisible(false);
    }
    
    @FXML
    void approveDeliveryClicked(ActionEvent event) {
    	String tmp = orderNumberTxt.getText();
    	if (isNumeric(tmp)) {
    	ZliClientUI.chat.accept(new Message(Task.Change_Order_Status_To_Arrived, Integer.parseInt(tmp)));
    	if (!(ZliClient.permission.getErrorLabel().equals("invalidID"))) {
    		screen.HelpMessage("Seccessfully approved delivery");
    		initialize();
    	}
    	else 
    		errorTxt.setVisible(true);
    	}
    	else 
    		errorTxt.setVisible(true);
		
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
    
    public static boolean isNumeric(String str) { 
    	  try {  
    	    Double.parseDouble(str);  
    	    return true;
    	  } catch(NumberFormatException e){  
    	    return false;  
    	  }  
    	}

}