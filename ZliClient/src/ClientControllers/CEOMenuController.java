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
 * The class CEOMenuController is the Controller of "CEOMenu" Screen
 */
public class CEOMenuController {

    @FXML
    private Button LogOutBtn;

    @FXML
    private Button viewMonthlyReportBtn;

    @FXML
    private Button viewQuarterlyReportBtn;

    @FXML
    private Button XBtn;

    @FXML
    private FontAwesomeIconView XIcon;

    @FXML
    private Text welcomeLabel;
    
    private ChangeScene scene = new ChangeScene();
    
	/**
	 * This method initiate the screen of the relevance FXML. 
	 */
    @FXML
	public void initialize() {
    	welcomeLabel.setText("Welcome back " + ZliClient.userController.getUser().getFirstName());
    }
    
	/**
	 * This method terminates CEOmenu scene and opens the login screen. ask server to update database that user is loged
	 * out
	 * @param event mouse clicked on LogOut button
	 * @throws Exception if ActionEvent Fails
	 */
    public void LogOut(ActionEvent event) throws Exception {
		ZliClientUI.chat.accept(new Message(Task.Request_logout, ZliClient.userController.getUser().getUsername()));
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		scene.changeScreen(new Stage(),"/GuiClientScreens/LoginZLI.fxml", true);
    }

	/**
	 * Closes the applications and sends a Request_disconnected to server side
	 * @param event mouse clicked on X Icon
	 * @throws Exception if ActionEvent Fails
	 **/
	@FXML
	public void X(ActionEvent event) throws Exception {
		ZliClientUI.chat.accept(new Message(Task.Request_logout, ZliClient.userController.getUser().getUsername()));
		ZliClientUI.chat.accept(new Message(Task.Request_disconnected, null));
		System.exit(0);
	}

	/**
	 * This method terminates CEOmenu scene and opens the ChooseMonthlyReports screen.
	 * @param event mouse clicked on viewMonthlyReport button
	 */
    @FXML
    void viewMonthlyReportClicked(ActionEvent event) {
    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		scene.changeScreen(new Stage(), "/GuiClientScreens/ChooseMonthlyReports.fxml", true);
    }

	/**
	 * This method terminates CEOmenu scene and opens the ChooseQuarterlyReports screen.
	 * @param event mouse clicked on viewQuarterlyReport button
	 */
    @FXML
    void viewQuarterlyReportClicked(ActionEvent event) {
    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		scene.changeScreen(new Stage(), "/GuiClientScreens/ChooseQuarterlyReports.fxml", true);
    }

}