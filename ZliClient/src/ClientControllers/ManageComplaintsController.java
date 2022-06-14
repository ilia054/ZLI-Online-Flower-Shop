package ClientControllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import Entities.Complaint;
import EntityControllers.ComplaintController;
import EntityControllers.StringController;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * GUI Controller of ManageComplaints
 */
public class ManageComplaintsController {

    @FXML
    private Button XBtn;

    @FXML
    private FontAwesomeIconView refreshBTN;

    @FXML
    private FontAwesomeIconView saveBTN;

    @FXML
    private TableView<Complaint> ComplaintsTable;

    @FXML
    private TableColumn<Complaint, String> userName;

    @FXML
    private TableColumn<Complaint, String> complaint;

    @FXML
    private TableColumn<Complaint, String> dateTIME;

    @FXML
    private TableColumn<Complaint, Integer> orderId;

    @FXML
    private TableColumn<Complaint, ComplaintStatus> complaintStatus;

    @FXML
    private TableColumn<Complaint, Refund> refund;

    @FXML
    private FontAwesomeIconView BackBtn;

    @FXML
    private TableColumn<Complaint, String> orderPrice;


	private ObservableList<Complaint> complaints = FXCollections.observableArrayList();
	private ChangeScene scene = new ChangeScene();
	private ArrayList<Complaint> updateComplaints = new ArrayList<>();
	private StringBuilder hours24=new StringBuilder();
	ArrayList<String> username= new ArrayList<>();
	/** 
     * This method sets the customers complaint details and sets combo boxes for "complaint status" and
     * "refund" for each complaint by enums.
     * Before the employee sees the details, a screen of complaints that have been submitted more 
     * than 24 hours ago  will be shown with an appropriate message for each complaint.
     * @author Michael Ioffe
     */

	@FXML
	public void initialize() {
		userName.setCellValueFactory(new PropertyValueFactory<Complaint, String>("userName"));
		complaint.setCellValueFactory(new PropertyValueFactory<Complaint, String>("complaint"));
		dateTIME.setCellValueFactory(new PropertyValueFactory<Complaint, String>("dateTime"));
		orderId.setCellValueFactory(new PropertyValueFactory<Complaint, Integer>("orderNumber"));
		complaintStatus.setCellValueFactory(new PropertyValueFactory<Complaint, ComplaintStatus>("complaintStatus"));
		refund.setCellValueFactory(new PropertyValueFactory<Complaint, Refund>("refund"));
		orderPrice.setCellValueFactory(new PropertyValueFactory<Complaint, String>("orderPrice"));
		username.add(ZliClient.userController.getUser().getUsername());
		ZliClientUI.chat.accept(new Message(Task.Get_Complaints_table, username));

		complaints = ComplaintController.getComplaints();
		for(Complaint comp : complaints) {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		// Save current time
		String savedTime = comp.getDateTime(); // Applies local time zone 1
		// Check time elapsed
		Calendar cal = Calendar.getInstance();

		try {
			cal.setTime(df.parse(savedTime));
			cal.add(Calendar.DAY_OF_MONTH, 1);
			if (System.currentTimeMillis() >= cal.getTimeInMillis()) {
				hours24.append(comp.getUserName()+"'s Complaint for Order Number: "+comp.getOrderNumber()+ " has been submited more then 24 hours ago!");
				hours24.append(System.getProperty("line.separator"));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} //

		}
		ComplaintsTable.setItems(complaints);
		createDropBoxInCellStatus(complaintStatus);

		setEditableStatusCell(complaintStatus);
		createDropBoxInCellRefund(refund);
		setEditableRefundCell(refund);
		ComplaintsTable.autosize();
		ComplaintsTable.setEditable(true);
		if(!hours24.toString().isEmpty())
    	scene.HelpMessage(hours24.toString());

	}
	@FXML
	void Help(MouseEvent event) {
		scene.HelpMessage(
				"\"in order to update complaint ,\n you must change the complaint status to \\\"APPROVED\\\" .\notherwise the complaint wont be updated!!\");\r\n");
	}
    @FXML
    void Back(MouseEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		// scene.BackBtnOnManageOrders(new Stage());
		scene.changeScreen(new Stage(), "/GuiClientScreens/CostumerServiceEmployeeMenu.fxml", true);
    }

 
    public void X(ActionEvent event) {
    	ZliClientUI.chat.accept(new Message(Task.Request_logout, ZliClient.userController.getUser().getUsername()));
		ZliClientUI.chat.accept(new Message(Task.Request_disconnected, null));
		System.exit(0);
    }
    /** 
     * This method loads the screen to its original state before making any changes to it (before clicking submit) . 
     * @param mouse event(click)
     */

    @FXML
	void refreshedClicked(MouseEvent event) {
		ComplaintsTable.getItems().clear();
		ZliClientUI.chat.accept(new Message(Task.Get_Complaints_table, username));
		complaints = ComplaintController.getComplaints();
		ComplaintsTable.setItems(complaints);
		ComplaintsTable.refresh();	
	}
    /** 
     * This method saves all the complaints details and exit from the screen.
     * If there are complaints which are not handled and have been submitted more than 24 hours ago then 
     * a screen of those complaints will be shown and will return the user to the complaints screen.
     * @param mouse event(click)
     */

    @FXML
    void saveClicked(MouseEvent event) {
		for (int i = 0; i < ComplaintsTable.getItems().size(); i++)
			updateComplaints.add(ComplaintsTable.getItems().get(i));
    	ArrayList<String> mail = new ArrayList<String>();

		Message message = new Message(Task.Update_Complaints_DB, updateComplaints);
		ZliClientUI.chat.accept(message);
		ObservableList<String> emails = StringController.getString();
		
		scene.HelpMessage("The changes have been saved successfully");
		int size=emails.size();
		if(size>0)
		{
			for (int i=0;i<size;i++) {
		    	StringBuilder str=new StringBuilder();
				mail.add(emails.get(i++).toString());
				str.append("Dear Customer ID: "+emails.get(i++).toString()+ "\nthe complaint for your order Number "+emails.get(i++).toString()+"\n Has been handeled and you have recived $"+ emails.get(i)+ " as store credit");
				mail.add(str.toString());
				ZliClientUI.chat.accept(new Message(Task.Send_Email, mail));
				mail.removeAll(mail);
				}
		}
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		scene.changeScreen(new Stage(), "/GuiClientScreens/ManageComplaints.fxml", true);
    }
    /** 
     * This method creates combo box for the "complaint status" column.
     * and it adds the Statuses from our ENUM class "ComplaintStatus"
     * @author Michael Ioffe
     * @param(Columns)
     */
	private void createDropBoxInCellStatus(TableColumn<Complaint, ComplaintStatus> Column) {
		complaintStatus.setCellFactory((param) -> new ComboBoxTableCell<>(new StringConverter<ComplaintStatus>() {

			@Override
			public String toString(ComplaintStatus object) {
				return object.toString();
			}

			@Override
			public ComplaintStatus fromString(String string) {
				return ComplaintStatus.valueOf(string);
			}

		}, ComplaintStatus.values()));
	}
	/** 
     * This method sets the "complaint status" to be editable for each customer.
     * @author Michael Ioffe
     * @param(Complaints)
     */

	private void setEditableStatusCell(TableColumn<Complaint, ComplaintStatus> complaint) {
		complaint.setOnEditCommit(event -> {
			((Complaint) event.getRowValue()).setComplaintStatus(event.getNewValue());
		});
	}
	/** 
     * This method creates combo box for the "refund" column.
     * and it adds the refunds from our ENUM class "Refund"
     * @author Michael Ioffe
     * @param(Columns)
     */

	private void createDropBoxInCellRefund(TableColumn<Complaint, Refund> Column) {
		refund.setCellFactory((param) -> new ComboBoxTableCell<>(new StringConverter<Refund>() {

			@Override
			public String toString(Refund object) {
				return object.toString();
			}

			@Override
			public Refund fromString(String string) {
				return Refund.valueOf(string);
			}

		}, Refund.values()));
	}
	/** 
     * This method sets the "refund" to be editable for each customer.
     * @author Michael Ioffe
     * @param(Complaints)
     */

	private void setEditableRefundCell(TableColumn<Complaint, Refund> complaint) {
		complaint.setOnEditCommit(event -> {
			((Complaint) event.getRowValue()).setRefund(event.getNewValue());
		});
	}

}
