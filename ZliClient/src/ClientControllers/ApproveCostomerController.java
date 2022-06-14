package ClientControllers;

import java.util.ArrayList;


import Entities.User;
import Enums.Task;
import client.ZliClient;
import client.ZliClientUI;
import common.ChangeScene;
import common.Message;
import common.Time;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * The class ApproveCostomerController is the Controller of "ApproveCostomer" Screen
 */

public class ApproveCostomerController {

	private ChangeScene scene = new ChangeScene();
	
    @FXML
    private Button XBtn;

    @FXML
    private TextField firstName;

    @FXML
    private Button approveCostumer;

    @FXML
    private TextField cardNumberTxt;

    @FXML
    private TextField cardExpirationDateTxt;

    @FXML
    private TextField cvvTxt;

    @FXML
    private FontAwesomeIconView BackBtn;

    @FXML
    private TextField costumerIdTxt;

    @FXML
    private Button importCostumerBtn;

    @FXML
    private TextField lastName;

    @FXML
    private TextField phoneNumber;

    @FXML
    private TextField email;
    
    @FXML
    private Label errorLabel;

    ChangeScene screen = new ChangeScene();
    
    User userToManage;
    
    @FXML
    private TextField usernameTxt;

    
	/**
	 * This method initiate the screen of the relevance FXML.
	 */
    public void initialize() {
    	usernameTxt.clear();
    	firstName.clear();
		lastName.clear();
		email.clear(); 
		phoneNumber.clear();
		cardNumberTxt.clear();
    	cardExpirationDateTxt.clear();
    	cvvTxt.clear();
    	errorLabel.setVisible(false);
    	usernameTxt.setDisable(false);
		firstName.setDisable(true);
		lastName.setDisable(true);
		email.setDisable(true); 
		phoneNumber.setDisable(true);
		cardNumberTxt.setDisable(true);
    	cardExpirationDateTxt.setDisable(true);
    	cvvTxt.setDisable(true);
    }
    
	/**
	 * This method take user details from screen and ask the server to save and update it on database.
	 * @param event mouse clicked on ApproveCostumer button
	 */
    @FXML
    void ApproveCostumerClicked(ActionEvent event) {
    	String username = ZliClient.manageOtherUserController.getUser().getUsername();
    	ArrayList<String> costumerUsernameAndCreditcard = new ArrayList<>();
    	costumerUsernameAndCreditcard.add(username);
    	if(!cardNumberTxt.getText().equals("") && !cardExpirationDateTxt.getText().equals("") && !cvvTxt.getText().equals("")) {
    		if(isExpirationDateTxtIsCorrect(cardExpirationDateTxt.getText())) {
    			costumerUsernameAndCreditcard.add(cardNumberTxt.getText());
            	costumerUsernameAndCreditcard.add(cardExpirationDateTxt.getText());
            	costumerUsernameAndCreditcard.add(cvvTxt.getText());
            	ZliClientUI.chat.accept(new Message(Task.Update_costumer_creditcard, costumerUsernameAndCreditcard));
            	screen.HelpMessage(username + "has been successfuly added as APPROVED Costumer, he can now start place his orders! He's eligble for 20% discount for the first order");
            	ArrayList<String> info = new ArrayList<>();
            	info.add(userToManage.getEmail());
            	info.add("Congratulations you have been approved as costumer! As new customer you have 20% discount for your first order!");
            	ZliClientUI.chat.accept(new Message(Task.Send_Email, info));
        		initialize();
    		}
    		else {
    			errorLabel.setVisible(true);
    			errorLabel.setText("Expiration date is not valid!");
    		}
    	}
    	else {
    		errorLabel.setVisible(true);
			errorLabel.setText("All fields must be filled!");
    	}
    }

    
    boolean isExpirationDateTxtIsCorrect(String dateTxt) {
    	String[] dateStrings = dateTxt.split("/");
    	if(dateStrings.length == 2) {
    	String currentMonth = Time.getCurrentMonthAndYear().get(0);
    	String currentYear = Time.getCurrentMonthAndYear().get(1);
    	System.out.println("input: "+dateStrings[0]+"/"+dateStrings[1]+" current Month: "+ currentMonth +"current Year:"+currentYear);
    		if(Integer.valueOf(dateStrings[1]) >= Integer.valueOf(currentYear)) {
    			if(Integer.valueOf(dateStrings[1]) > Integer.valueOf(currentYear))
    				return true;
    			if(Integer.valueOf(dateStrings[0]) >= Integer.valueOf(currentMonth))
    				return true;
    		}
    		return false;
    	}
    	return false;
    }
    
	/**
	 * This method terminates ApproveCostumer scene and opens the StoreMangerMenu
	 * @param event mouse clicked on Back button
	 * @throws Exception if MouseEvent Fails
	 */
	@FXML
	public void Back(MouseEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		scene.changeScreen(new Stage(), "/GuiClientScreens/StoreManagerMenu.fxml", true);
	}

	/**
	 * This method taked username from screen and ask the server to import all his details and show them on screen. 
	 * @param event mouse clicked on ImportCostumer button
	 */
    @FXML
    void ImportCostumerClicked(ActionEvent event) {
    		errorLabel.setVisible(false);
    		ZliClientUI.chat.accept(new Message(Task.Import_costumer_data_by_username, usernameTxt.getText()));
    		userToManage = ZliClient.manageOtherUserController.getUser();
    	if(userToManage!=null) {
    		firstName.setText(userToManage.getFirstName()); 
    		lastName.setText(userToManage.getLastName());
    		email.setText(userToManage.getEmail()); 
    		phoneNumber.setText(userToManage.getPhoneNumber());
    		firstName.setDisable(true);
    		lastName.setDisable(true);
    		email.setDisable(true); 
    		phoneNumber.setDisable(true);
    		cardNumberTxt.setDisable(false);
        	cardExpirationDateTxt.setDisable(false);
        	cvvTxt.setDisable(false);
        	usernameTxt.setDisable(true);
    	}
    	else {
    		errorLabel.setVisible(true);
			errorLabel.setText("Username not exist or already approved");
		}
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

}