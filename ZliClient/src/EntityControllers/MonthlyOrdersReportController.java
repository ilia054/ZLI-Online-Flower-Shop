package EntityControllers;

import Entities.MonthlyOrdersReport;

/**
* The class MonthlyOrdersReportController represent the data structure that save Monthly Orders Report that imported from data base.
* @author Omri Cohen
*/
public class MonthlyOrdersReportController {
	private MonthlyOrdersReport monthlyOrdersReport;

	public MonthlyOrdersReport getMonthlyOrdersReport() {
		return monthlyOrdersReport;
	}

	public void setMonthlyOrdersReport(MonthlyOrdersReport monthlyOrdersReport) {
		this.monthlyOrdersReport = monthlyOrdersReport;
	}
}
