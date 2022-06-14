package ClientControllers;

import Entities.Costumer;
import Enums.CostumerPremissions;
import Enums.Task;
import client.ZliClient;
import client.ZliClientUI;
import common.ChangeScene;
import common.Message;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This class is the GUI Controller of CostumerMenu, it has all of the functionality that is
 * built in to the GUI, That means its the "Back-End" of our Buttons that are displayed in the GUI
 */
public class CostumerMenuController {

	@FXML
    private Button createNewOrderBtn;
	
    @FXML
    private Button ManageOrderBtn;
    
    @FXML
    private Text welcomeLabel;
    
    @FXML
    private Text CreditTXT;
    
    @FXML
    private Text userPremmissions;
    
    @FXML
    private Text CustomerMenuTXT;
    

    @FXML
    private ImageView costumerimg;

    
    @FXML
    private Button XBtn;
    
    private ChangeScene screen = new ChangeScene();
	/**
	* This method is called when the User clicks on "Manage Orders"
	* this will close the current Scene and will display the ManaOrder screen
	*/
    public void initialize() {
    	welcomeLabel.setText("Welcome back " + ZliClient.userController.getUser().getFirstName() + " " + ZliClient.userController.getUser().getLastName() );
    	ZliClientUI.chat.accept(new Message(Task.Get_Costumer_By_User_Name, ZliClient.userController.getUser()));
    	Costumer costumer = ZliClient.cartController.getCostumer();
    	CostumerPremissions costumerPremissions = costumer.getPermissions();
    	if(costumerPremissions == CostumerPremissions.FROZEN || ZliClient.cartController.getCostumer().getPermissions() == CostumerPremissions.NOT_APPROVED)
    		createNewOrderBtn.setDisable(true);
    	userPremmissions.setText(costumerPremissions.toString().replace("_"," "));
    	CreditTXT.setText("$" + costumer.getStoreCredit());
    	CustomerMenuTXT.setText(CustomerMenuTXT.getText() + " here You can:\n1) Create a new order\n2) Manage existing orders\n"
    			+ "3) View the catalog\n4) View available store credit");
		Image image = new Image(getClass().getResourceAsStream("/GuiAssests/CostumerMenu.png"));
		costumerimg.setImage(image);

    }
    
    @SuppressWarnings("static-access")
	public void ManageOrders(ActionEvent event) throws Exception {
    	ZliClientUI.chat.accept(new Message(Task.Get_OrdersBy_id, ZliClient.cartController.getCostumer().getUsername()));
    	ObservableList<Integer> Idorders = ZliClient.idordercontroller.getIdorders();
    	if(Idorders==null)return;
    	else 
    		if(Idorders.size()<1) {
    			screen.ErrorMessage("No active orders that has not been submited for this customer!");
    		}
			else {
				((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
				screen.changeScreen(new Stage(),"/GuiClientScreens/ManageOrdersForCostumer.fxml", true);
			}
    }
    
    @FXML
    void createNewOrder(ActionEvent event) {
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
    	screen.changeScreen(new Stage(),"/GuiClientScreens/ProductCatalog.fxml", true);
    }
    
    @FXML
    void ViewCatalog(ActionEvent event) {
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
    	screen.changeScreen(new Stage(),"/GuiClientScreens/ViewProductCatalog.fxml", true);
    }

    
	/**
	* This method is called when the User clicks on "Log Out" button 
	* this will close the current Scene and will display the LoginZli screen
	* @param event of the current Screen that is displayed 
	* @throws Exception if ActionEvent Fails
	*/    
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

}
