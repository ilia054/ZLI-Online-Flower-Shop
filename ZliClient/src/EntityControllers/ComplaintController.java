package EntityControllers;

import Entities.Complaint;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
*This class stores the Complaints that are returned from the DB and passes it to the Server
*so we can have access to it without directly calling the Data base each time we want to view the orders
* @author Shay Zak
*/
public class ComplaintController {
	private Complaint complaintToUpdateOnDB;
	private static ObservableList<Complaint> complaints=FXCollections.observableArrayList();

	public static ObservableList<Complaint> getComplaints() {
		return complaints;
	}

	public static void setComplaints(ObservableList<Complaint> observableList) {
		complaints=observableList;
	}

	public Complaint getComplaintToUpdateOnDB() {
		return complaintToUpdateOnDB;
	}

	public void setComplaintToUpdateOnDB(Complaint complaintToUpdateOnDB) {
		this.complaintToUpdateOnDB = complaintToUpdateOnDB;
	}
	
}