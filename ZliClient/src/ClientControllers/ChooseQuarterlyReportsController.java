package ClientControllers;

import java.util.ArrayList;
import java.util.Arrays;
import Enums.Task;
import Enums.UserType;
import client.ZliClientUI;
import common.ChangeScene;
import common.Message;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import Entities.QuarterlySatisfactionReport;
import Enums.Store;
import client.ZliClient;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * The class ChooseQuarterlyReportsController is the Controller of "ChooseQuarterlyReports" Screen
 */
public class ChooseQuarterlyReportsController {

    @FXML
    private ComboBox<String> quarterlyComboBox;

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
		ArrayList<String> quarters= new ArrayList<String>(Arrays.asList("1","2","3","4"));
		quarterlyComboBox.getItems().addAll(quarters);
		ArrayList<String> years= new ArrayList<String>(Arrays.asList("2015","2016","2017","2018","2019","2020","2021","2022"));
		yearComboBox.getItems().addAll(years);
		ArrayList<Store> stores;
		errorLabel.setVisible(false);
		if (ZliClient.userController.getUser().getRole() == UserType.Store_Manager) {
			ArrayList<String> reportsType= new ArrayList<String>(Arrays.asList("Costumers satisfaction reports"));
			typeComboBox.getItems().addAll(reportsType);
			storeComboBox.setValue(ZliClient.userController.getUser().getStore());
			storeComboBox.setVisible(false);
			storeName.setVisible(false);
			storeName.setVisible(true);
			storeName.setText(ZliClient.userController.getUser().getStore());
		}
		else {
			ArrayList<String> reportsType= new ArrayList<String>(Arrays.asList("Incomes reports","Costumers satisfaction reports"));
			typeComboBox.getItems().addAll(reportsType);
			stores= new ArrayList<Store>(Arrays.asList(Store.values()));
			for (Store store : stores) {
				storeComboBox.getItems().add(store.toString());
			}
		}
		
//		setColumnsInTable();
//		ZliClientUI.chat.accept(new Message(Task.Import_data_costumers_approved_and_frozen, null));
//		costumers = CostumerController.getApprovedOrFrozenCostumers();
//		CostumersTable.setItems(costumers);
//		createDropBoxInCell(permissions);
//		setEditablePermissionsCell(permissions);
//		CostumersTable.autosize();
//		CostumersTable.setEditable(true);
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
//    	if(monthComboBox.getValue().equals("Month") || yearComboBox.getValue().equals("Year") || storeComboBox.getValue().equals("Store")) {
    	if(quarterlyComboBox.getValue() == null || yearComboBox.getValue() == null || storeComboBox.getValue() == null) {
    		errorLabel.setVisible(true);
			errorLabel.setText("Inorder to view report you must choose   'YEAR', 'MONTH'/'QUARTER' and Store!");
    	} else {
    		ArrayList<String> reportDetails = new ArrayList<String>(Arrays.asList(quarterlyComboBox.getValue(), yearComboBox.getValue(), storeComboBox.getValue()));
    		
    		switch(typeComboBox.getValue()) 
    		{
    		  case "Incomes reports":
    			  ZliClientUI.chat.accept(new Message(Task.Get_quarterly_incomes_report, reportDetails));
    			  if(ZliClient.monthlyIncomesReportController.getMonthlyIncomesReports() == null || ZliClient.monthlyIncomesReportController.getMonthlyIncomesReports().isEmpty()) {
    				  errorLabel.setVisible(true);
    			  	  errorLabel.setText("No such report");
    			  }
    			  else {
	    			  ((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
	    			  scene.changeScreen(new Stage(), "/GuiClientScreens/QuarterlyIncomesReport.fxml", true);
    			  }
    		    break;
    		  case "Costumers satisfaction reports":
    			  ZliClientUI.chat.accept(new Message(Task.Get_quarterly_costumer_satisfaction_report, reportDetails));
    			  ObservableList<QuarterlySatisfactionReport> quarterlySatisfactionReport=FXCollections.observableArrayList();
    			  quarterlySatisfactionReport = ZliClient.quarterlySatisfactionReportController.getQuarterlySatisfactionReport();
    			  if(quarterlySatisfactionReport.isEmpty() || quarterlySatisfactionReport == null) {
    				  errorLabel.setVisible(true);
    			  	  errorLabel.setText("No such report");
    			  }
    			  else {
	    			  ((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
	    			  scene.changeScreen(new Stage(), "/GuiClientScreens/QuarterlySatisfactionReport.fxml", true);
    			  }
    		    // code block
    		    break;
    		}

    	}
    	
    }

}
