package ClientControllersTest;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import ClientControllers.QuarterlyIncomesReportController;
import Entities.MonthlyIncomesReport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.Arrays;


public class QuarterlyIncomesReportControllerTest {
	
	private MonthlyIncomesReport month1;
	private MonthlyIncomesReport month2;
	private MonthlyIncomesReport month3;
	private QuarterlyIncomesReportController quarterlyIncomesReportController; 
	private static ObservableList<MonthlyIncomesReport> monthlyIncomesReports;
	
	/** 
	 * We use mock to return value for getMonthlyIncomesReports() function that used in initialize in
	 * order to check initialize calculations
	 */
	@Before
	public void setUp() throws Exception {
		monthlyIncomesReports = FXCollections.observableArrayList();
		quarterlyIncomesReportController = new QuarterlyIncomesReportController();
	}

	/**
	 * Description: checking calculations of initialize method on valid full qurater (3 monthes).
	 * Input data: using mock, quarter 1 with 3 monthes with orders
	 * Expected result: ArrayList<String> with correct calculations of 3 monthes (best month, least Month,
	 * orders Incomes, deliveries Incomes, refunds, totalIncomes, totalIncomesAverage)
	 */
	@Test
	public void getDetailsForReportTestFor3MonthsWithOrders() {
		month1 = new MonthlyIncomesReport(2000, 200, 300, "KARMIEL", "01/2022");
		month2 = new MonthlyIncomesReport(1500, 100, 400, "KARMIEL", "02/2022");
		month3 = new MonthlyIncomesReport(5000, 300, 300, "KARMIEL", "03/2022");
		monthlyIncomesReports.add(month1);
		monthlyIncomesReports.add(month2);
		monthlyIncomesReports.add(month3);
		ArrayList<String> expected = new ArrayList<String>(Arrays.asList("03/2022", "02/2022", "8500.0", "600.0", "1000.0", "8100.0", "2700.0"));
		quarterlyIncomesReportController.getDetailsForReport(monthlyIncomesReports);
		ArrayList<String> result = quarterlyIncomesReportController.dataToReport;
		assertArrayEquals(expected.toArray(), result.toArray());
	}
	
	/**
	 * Description: checking calculations of initialize method on valid not full qurater (2 monthes).
	 * Input data: using mock, quarter 1 with 2 monthes with orders.
	 * Expected result: ArrayList<String> with correct calculations of 2 monthes (best month, least Month,
	 * orders Incomes, deliveries Incomes, refunds, totalIncomes, totalIncomesAverage)
	 */
	@Test
	public void getDetailsForReportTestFor2MonthsWithOrders() {
		month1 = new MonthlyIncomesReport(2000, 300, 500, "TEL_AVIV", "04/2022");
		month2 = new MonthlyIncomesReport(1000, 200, 400, "TEL_AVIV", "05/2022");
		monthlyIncomesReports.add(month1);
		monthlyIncomesReports.add(month2);
		ArrayList<String> expected = new ArrayList<String>(Arrays.asList("04/2022", "05/2022", "3000.0", "500.0", "900.0", "2600.0", "866.6667"));
		quarterlyIncomesReportController.getDetailsForReport(monthlyIncomesReports);
		ArrayList<String> result = quarterlyIncomesReportController.dataToReport;
		assertArrayEquals(expected.toArray(), result.toArray());
	}
	
	/**
	 * Description: checking calculations of initialize method on empty quarter.
	 * Input data: using mock, empty quarter.
	 * Expected result: ArrayList<String> with initial values (best month, least Month,
	 * orders Incomes, deliveries Incomes, refunds, totalIncomes, totalIncomesAverage)
	 */
	@Test
	public void getDetailsForReportTestForEmptyList() {
		ArrayList<String> expected = new ArrayList<String>(Arrays.asList(null, null, "0.0", "0.0", "0.0", "0.0", "0.0"));
		quarterlyIncomesReportController.getDetailsForReport(monthlyIncomesReports);
		ArrayList<String> result = quarterlyIncomesReportController.dataToReport;
		assertArrayEquals(expected.toArray(), result.toArray());
	}
	
	/**
	 * Description: checking that initialize throws exception when recived null from getMonthlyIncomesReports().
	 * Input data: using mock, null.
	 * Expected result: NullPointerException
	 */
	@Test(expected = NullPointerException.class)
	public void getDetailsForReportTestForNull() {
		quarterlyIncomesReportController.getDetailsForReport(null);
	}

}
