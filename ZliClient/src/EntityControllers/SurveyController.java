package EntityControllers;

import Entities.Survey;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
*This class stores the Surveys that are returned from the DB and passes it to the Server
*so we can have access to it without directly calling the Data base each time we want to view the orders
* @author Michael Ioffe
*/
public class SurveyController {


		private static ObservableList<Survey> surveys=FXCollections.observableArrayList();

		public static ObservableList<Survey> getSurveys() {
			return surveys;
		}

		public static void setOrders(ObservableList<Survey> observableList) {
			surveys=observableList;
		}
	}