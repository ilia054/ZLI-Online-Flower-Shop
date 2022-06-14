package ClientControllers;

import java.util.ArrayList;
import java.util.Arrays;
import Enums.Task;
import Enums.UserType;
import client.ZliClientUI;
import common.ChangeScene;
import common.Message;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import Enums.Store;
import client.ZliClient;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * The class ChooseMonthlyReportsController is the Controller of "ChooseMonthlyReports" Screen
 */
public class ChooseMonthlyReportsController {

    @FXML
    private ComboBox<String> monthComboBox;

    @FXML
    private ComboBox<String> storeComboBox;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private ComboBox<String> yearComboBox;

    @FXML
    private FontAwesomeIconView BackBtn;
    
    @FXML
    private Button XBtn;
    
    @FXML
    private Label storeName;

    @FXML
    private Button showReportBtn;
    
    @FXML
    private Label errorLabel;
    
    private ChangeScene scene = new ChangeScene();

	/**
	 * This method initiate the screen of the relevance FXML. 
	 */
	public void initialize() {
		ArrayList<String> months= new ArrayList<String>(Arrays.asList("01","02","03","04","05","06","07","08","09","10","11","12"));
		monthComboBox.getItems().addAll(months);
		ArrayList<String> years= new ArrayList<String>(Arrays.asList("2015","2016","2017","2018","2019","2020","2021","2022"));
		yearComboBox.getItems().addAll(years);
		ArrayList<Store> stores;
		ArrayList<String> reportsType= new ArrayList<String>(Arrays.asList("Incomes reports","Orders reports"));
		typeComboBox.getItems().addAll(reportsType);
		errorLabel.setVisible(false);
		if (ZliClient.userController.getUser().getRole() == UserType.Store_Manager) {
			storeComboBox.setValue(ZliClient.userController.getUser().getStore());
			storeComboBox.setVisible(false);
			storeName.setVisible(false);
			storeName.setVisible(true);
			storeName.setText(ZliClient.userController.getUser().getStore());
		}
		else {
			stores= new ArrayList<Store>(Arrays.asList(Store.values()));
			for (Store store : stores) {
				storeComboBox.getItems().add(store.toString());
			}
		}
	}
    
	/**
	 * This method terminates choosemonthlyreports scene and opens the CEO/storemanager menu
	 * @param event mouse clicked on Back button
	 * @throws Exception if MouseEvent Fails
	 */
	@FXML
	public void Back(MouseEvent event) throws Exception {
		if (ZliClient.userController.getUser().getRole() == UserType.Store_Manager) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		scene.changeScreen(new Stage(), "/GuiClientScreens/StoreManagerMenu.fxml", true);
		}
		else {
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			scene.changeScreen(new Stage(), "/GuiClientScreens/CEOmenu.fxml", true);
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
	
	/**
	 * This method get from screen information about the report required to show, check if he is existing in database
	 * if does it will show the relevance report, if not it will show error
	 * @param event mouse clicked on showReport button
	 */
    @FXML
    void showReportClicked(ActionEvent event) {
    	errorLabel.setVisible(false);
    	if(monthComboBox.getValue() == null || yearComboBox.getValue() == null || storeComboBox.getValue() == null) {
    		errorLabel.setVisible(true);
			errorLabel.setText("Inorder to view report you must choose   'YEAR', 'MONTH'/'QUARTER' and Store!");
    	} else {
    		ArrayList<String> reportDetails = new ArrayList<String>(Arrays.asList(monthComboBox.getValue(), yearComboBox.getValue(), storeComboBox.getValue()));
    		
    		switch(typeComboBox.getValue()) 
    		{
    		  case "Incomes reports":
    			  ZliClientUI.chat.accept(new Message(Task.Get_monthly_incomes_report, reportDetails));
    			  if(ZliClient.monthlyIncomesReportController.getMonthlyIncomesReport() == null) {
    				  errorLabel.setVisible(true);
    			  	  errorLabel.setText("No such report");
    			  }
    			  else {
	    			  ((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
	    			  scene.changeScreen(new Stage(), "/GuiClientScreens/MonthlyIncomesReport.fxml", true);
    			  }
    		    break;
    		  case "Orders reports":
    			  ZliClientUI.chat.accept(new Message(Task.Get_monthly_orders_report, reportDetails));
    			  if(ZliClient.monthlyOrdersReportController.getMonthlyOrdersReport() == null) {
    				  errorLabel.setVisible(true);
    				  errorLabel.setText("No such report");
    			  }
    			  else {
	    			  ((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
	    			  scene.changeScreen(new Stage(), "/GuiClientScreens/MonthlyOrdersReport.fxml", true);
    			  }
    		    break;
    		}

    	}
    	
    }

}
