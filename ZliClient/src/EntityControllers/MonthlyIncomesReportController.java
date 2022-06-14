package EntityControllers;

import Entities.MonthlyIncomesReport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
* The class MonthlyIncomesReportController represent the data structure that save Monthly Incomes Reports that imported from data base.
* @author Omri Cohen
*/
public class MonthlyIncomesReportController {
	private static ObservableList<MonthlyIncomesReport> monthlyIncomesReports=FXCollections.observableArrayList();
	private static MonthlyIncomesReport monthlyIncomesReport = new MonthlyIncomesReport(0, 0, 0, null, null);


	public ObservableList<MonthlyIncomesReport> getMonthlyIncomesReports() {
		return monthlyIncomesReports;
	}

	public static void setMonthlyIncomesReports(ObservableList<MonthlyIncomesReport> observableList) {
		monthlyIncomesReports=observableList;
	}

	public MonthlyIncomesReport getMonthlyIncomesReport() {
		return monthlyIncomesReport;
	}

	public void setMonthlyIncomesReport(MonthlyIncomesReport monthlyIncomesReport) {
		MonthlyIncomesReportController.monthlyIncomesReport = monthlyIncomesReport;
	}
	
	
}
