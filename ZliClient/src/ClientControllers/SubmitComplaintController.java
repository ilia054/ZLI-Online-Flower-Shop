package ClientControllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import Entities.Complaint;
import EntityControllers.IdOrderController;
import Enums.ComplaintStatus;
import Enums.Refund;
import Enums.Task;
import client.ZliClient;
import client.ZliClientUI;
import common.ChangeScene;
import common.Message;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * GUI Controller of SubmitComplaint
 */
public class SubmitComplaintController {

    @FXML
    private TextField userName;

    @FXML
    private TextArea complaintField;

    @FXML
    private Button XBtn;

    @FXML
    private FontAwesomeIconView undoBTN;

    @FXML
    private FontAwesomeIconView submitBTN;
    
    @FXML
    private FontAwesomeIconView searchCostumerBtn;

    @FXML
    private FontAwesomeIconView BackBtn;
    @FXML
    private FontAwesomeIconView Warning;
    @FXML
    private Label ErrorComplaint;
    @FXML
    private ComboBox<Integer> OrderIdBox;
    


	private ObservableList<Integer> Idorders = FXCollections.observableArrayList();
	private ChangeScene scene = new ChangeScene();

	public void initialize() {
		ErrorComplaint.setVisible(false);
		OrderIdBox.setDisable(true);//added
		complaintField.setDisable(true);//added
		submitBTN.setDisable(true);//added

		
	}
	public void X(ActionEvent event) throws Exception {
		ZliClientUI.chat.accept(new Message(Task.Request_logout, ZliClient.userController.getUser().getUsername()));
		ZliClientUI.chat.accept(new Message(Task.Request_disconnected, null));
		System.exit(0);
	}
	
	/** 
     * This method searches the orders of a specific customer when the user clicks on the text field.
     * If there is no such customer then an appropriate message will be shown.
     * If the specific customer has no orders then a screen with an appropriate message will be shown.
     * @author Michael Ioffe
     * @param mouse event(click)
     */

	@FXML
    void SearchCostumerClicked(MouseEvent event) {
		IdOrderController.setIdOrders(null);

		if(userName.getText().isEmpty()) {
    		ErrorComplaint.setVisible(true);
			return;
		}
    	String username=userName.getText();
    	ObservableList<Integer> ids= FXCollections.observableArrayList();
		ZliClientUI.chat.accept(new Message(Task.Get_OrdersBy_id, username));
		Idorders=IdOrderController.getIdorders();
		if(Idorders==null) {
    		ErrorComplaint.setVisible(true);
			return;
		}
		else ErrorComplaint.setVisible(false);
		for(int i=0;i<Idorders.size();i++) {
			ids.add(Idorders.get(i));
		}
		if(ids.size()<1) {
	    	scene.ErrorMessage("No active orders that has not been submited for this customer!");
			return;
		}
		OrderIdBox.setItems(ids);
		OrderIdBox.setDisable(false);//added
		complaintField.setDisable(false);//added
		submitBTN.setDisable(false);//added
    }
    @FXML
    void Back(MouseEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		// scene.BackBtnOnManageOrders(new Stage());
		scene.changeScreen(new Stage(), "/GuiClientScreens/CostumerServiceEmployeeMenu.fxml", true);
    }

    /** 
     * This method submits the complaint with the relevant details and time of the 
     * complaint.
     * After the click, the next time the user will search for the customer, he will not see the id order of
     * the submitted complaint for the specific order.
     * @author Michael Ioffe
     * @param mouse event(click)
     */

    @FXML
    void submitClicked(MouseEvent event) {
    	while(userName.getText().isEmpty()||complaintField.getText().isEmpty()) {
    		ErrorComplaint.setVisible(true);

    		return;
    	}
    	
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
        Date date = new Date();  
        String forma = formatter.format(date).toString();
		Complaint newComplaint=new Complaint
				(userName.getText()
				,complaintField.getText()
				,forma
				,OrderIdBox.getValue()
				,ComplaintStatus.UNHANDLED
				,Refund.NO_REFUND
				,0
				,ZliClient.userController.getUser().getUsername());
		ZliClient.complaintController.setComplaintToUpdateOnDB(newComplaint);
		ZliClientUI.chat.accept(new Message(Task.Get_price_for_comaplaint_by_order_id,ZliClient.complaintController.getComplaintToUpdateOnDB().getOrderNumber()));
    	//complaints.add("UNHANDLED");
 
		ZliClientUI.chat.accept(new Message(Task.Update_complaints_table, ZliClient.complaintController.getComplaintToUpdateOnDB()));
    	scene.HelpMessage("Complaint has been submited!!");
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		scene.changeScreen(new Stage(), "/GuiClientScreens/CostumerServiceEmployeeMenu.fxml", true);
		
    }

    @FXML
    void undoClicked(MouseEvent event) {
    	complaintField.clear();
    }
    
    

}
