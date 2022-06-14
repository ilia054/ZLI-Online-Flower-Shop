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
 * GUI Controller of MarketingEmployeeMenu
 */
public class MarketingEmployeeMenuController {

    @FXML
    private Button LogOutBtn;

    @FXML
    private Text welcomeLabel;

    @FXML
    private Button updateCatalogBtn;

    @FXML
    private Button AddCatalogBtn;

    @FXML
    private Button deleteFromCatalogBtn;

    @FXML
    private Button createSalesBtn;

    @FXML
    private Button XBtn;

    @FXML
    private FontAwesomeIconView XIcon;
    
    @FXML
    private Text MarketingMenuTXT;
    
    private ChangeScene scene = new ChangeScene();

    @FXML
	public void initialize() {
    	welcomeLabel.setText("Welcome back " + ZliClient.userController.getUser().getFirstName() + " " + ZliClient.userController.getUser().getLastName());
    	MarketingMenuTXT.setText("Here you can:\n1) Update catalog Products/Items Prices\n2) Add an Item/Product to Catalog\n"
    			+ "3) Remove an Item/Product From Catalog\n4) Create Sales for existing Items/Products");
    }
    

    @FXML
    void addToCatalog(ActionEvent event) {
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		scene.changeScreen(new Stage(),"/GuiClientScreens/AddNewAbstractProduct.fxml", true);
    }

    @FXML
    void createSale(ActionEvent event) {
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		scene.changeScreen(new Stage(),"/GuiClientScreens/CreateSale.fxml", true);
    }

    @FXML
    void deleteFromCatalog(ActionEvent event) {
    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		scene.changeScreen(new Stage(), "/GuiClientScreens/DeleteProductAndItem.fxml", true);
    }

    @FXML
    void updateCatalog(ActionEvent event) {
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		scene.changeScreen(new Stage(),"/GuiClientScreens/UpdateProductAndItem.fxml", true);
    }

    @FXML
    void LogOut(ActionEvent event) {
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
    	scene.changeScreen(new Stage(),"/GuiClientScreens/LoginZLI.fxml", true);
    }
    
    @FXML
    void X(ActionEvent event) {
    	ZliClientUI.chat.accept(new Message(Task.Request_logout, ZliClient.userController.getUser().getUsername()));
    	ZliClientUI.chat.accept(new Message(Task.Request_disconnected, null));
    	System.exit(0);
    }
}
