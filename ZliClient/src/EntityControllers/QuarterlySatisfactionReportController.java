package EntityControllers;

import Entities.QuarterlySatisfactionReport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
* The class QuarterlySatisfactionReportController represent the data structure that save Quarterly Satisfaction Report that imported from data base.
* @author Omri Cohen
*/
public class QuarterlySatisfactionReportController {
	private static ObservableList<QuarterlySatisfactionReport> quarterlySatisfactionReport=FXCollections.observableArrayList();

	public ObservableList<QuarterlySatisfactionReport> getQuarterlySatisfactionReport() {
		return quarterlySatisfactionReport;
	}

	public static void setQuarterlySatisfactionReport(
			ObservableList<QuarterlySatisfactionReport> quarterlySatisfactionReport) {
		QuarterlySatisfactionReportController.quarterlySatisfactionReport = quarterlySatisfactionReport;
	}


}
