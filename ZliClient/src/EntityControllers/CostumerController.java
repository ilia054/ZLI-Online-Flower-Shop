package EntityControllers;

import java.util.ArrayList;

import Entities.Costumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
* The class CostumerController represent the data structure that save Costumer that imported from data base.
* @author Omri Cohen
*/
public class CostumerController {
	private ArrayList<String> usernames;
	private static ObservableList<Costumer> approvedOrFrozenCostumers=FXCollections.observableArrayList();
	
	public CostumerController() {
		
	}

	public ArrayList<String> getUsernames() {
		return usernames;
	}

	public void setUsernames(ArrayList<String> usernames) {
		this.usernames = usernames;
	}

	public static ObservableList<Costumer> getApprovedOrFrozenCostumers() {
		return approvedOrFrozenCostumers;
	}

	public static void setApprovedOrFrozenCostumers(ObservableList<Costumer> approvedOrFrozenCostumers) {
		CostumerController.approvedOrFrozenCostumers = approvedOrFrozenCostumers;
	}
}
