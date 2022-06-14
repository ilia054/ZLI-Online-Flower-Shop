package ClientControllers;


import Enums.Task;
import client.ZliClientUI;
import common.ChangeScene;
import common.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
/**
 * GUI Controller of UserInputIPConnect
 */
public class UserInputIPConnectController {

	//@FXML
    //private Button xBTN;

    @FXML
    private TextField ipFieldTXT;

    @FXML
    private Text enterServerIPTXT;

    @FXML
    private Button connectBTN;

    @FXML
    void ClickX(ActionEvent event) {
    	System.exit(0);
    }

    @FXML
    void clickConnect(ActionEvent event) throws Exception {
    	ZliClientUI.setChat(ipFieldTXT.getText(),5555);
    	ZliClientUI.chat.accept(new Message(Task.Request_connect, null));
    	ChangeScene scene=new ChangeScene();
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		scene.changeScreen(new Stage(),"/GuiClientScreens/LoginZLI.fxml", true);
    }

}
