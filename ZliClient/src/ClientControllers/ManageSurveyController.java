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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * GUI Controller of ManageSurvey
 */
public class ManageSurveyController {

    @FXML
    private Button periodTimeBtn;

    @FXML
    private Button storeNameBtn;

    @FXML
    private Button saleBtn;

    @FXML
    private FontAwesomeIconView BackBtn;

    @FXML
    private Button XBtn;

    @FXML
    private FontAwesomeIconView XIcon;

    private ChangeScene scene = new ChangeScene();

    @FXML
    void Back(MouseEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		// scene.BackBtnOnManageOrders(new Stage());
		scene.changeScreen(new Stage(), "/GuiClientScreens/CostumerServiceExpertMenu.fxml", true);
    }

    @FXML
    void PerioudTimeBtn(ActionEvent event) {
        ((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
        scene.changeScreen(new Stage(),"/GuiClientScreens/ManageSurveyByTimePeriod.fxml", true);
        
    }

    @FXML
    void SaleBtn(ActionEvent event) {
        ((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
        scene.changeScreen(new Stage(),"/GuiClientScreens/ManageSurveyBySale.fxml", true);
        
    }

    @FXML
    void StoreNameBtn(ActionEvent event) {

        ((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
        scene.changeScreen(new Stage(),"/GuiClientScreens/ManageSurveyByStoreName.fxml", true);
        
    }

    @FXML
    public void X(ActionEvent event) throws Exception {
    	ZliClientUI.chat.accept(new Message(Task.Request_logout, ZliClient.userController.getUser().getUsername()));
    	ZliClientUI.chat.accept(new Message(Task.Request_disconnected, null));
    	System.exit(0);
    }

}
