package EntityControllers;

import Entities.Storestaff;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
* The class StorestaffController represent the data structure that save Storestaff that imported from data base.
* @author Omri Cohen
*/
public class StorestaffController {
	private static ObservableList<Storestaff> storestaffFromTheSameStore=FXCollections.observableArrayList();

	public StorestaffController() {
	}

	public static ObservableList<Storestaff> getStorestaffFromTheSameStore() {
		return storestaffFromTheSameStore;
	}

	public static void setStorestaffFromTheSameStore(ObservableList<Storestaff> storestaffFromTheSameStore) {
		StorestaffController.storestaffFromTheSameStore = storestaffFromTheSameStore;
	}
}