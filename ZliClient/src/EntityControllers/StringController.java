package EntityControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
*This class stores the Strings ArrayLists the hold data that is returned from the DB and passes it to the Server
*so we can have access to it without directly calling the Data base each time we want to view the orders
* @author Shay Zak
*/
public class StringController {
	private static ObservableList<String> monthsByYear=FXCollections.observableArrayList();
	private String errorLabel = null;

	public static ObservableList<String> getString() {
		return monthsByYear;
	}

	public static void setString(ObservableList<String> observableList) {
		monthsByYear=observableList;
	}

	public String getErrorLabel() {
		return errorLabel;
	}

	public void setErrorLabel(String errorLabel) {
		this.errorLabel = errorLabel;
	}
	
	
}