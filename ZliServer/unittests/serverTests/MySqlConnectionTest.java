package serverTests;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import Entities.MonthlyIncomesReport;
import Entities.User;
import Enums.UserType;
import server.MySqlConnection;
/**
 * IMPORTANT INFORMATION
 * In our project quarter incomes report is based on monthly incomes report (3 reports for each quarter).
 * We are checking both quarter and monthly incomes report methods (generate methods and 'get' methods).
 * For month with no orders at all - no report will be issued
*/

public class MySqlConnectionTest {

	//Variables for report's tests:
	ArrayList<String> quarterWith3MonthsWithOrdersForKarmiel; //Report details for quarter with 3 monthly reports for 'Karmiel' store
	ArrayList<String> quarterWith2MonthsWithOrdersForKarmiel; //Report details for quarter with 2 monthly reports for 'Karmiel' store
	ArrayList<String> quarterWithNoMonthsWithOrdersForKarmiel; //Report details for quarter with no monthly reports at all for 'Karmiel' store
	ArrayList<String> MonthWithNoWithOrdersForKarmiel; //Report details for month with no orders for 'Karmiel' store
	ArrayList<String> MonthWithOrders; //Report details for month with number of orders (bigger than '0') for 'Karmiel' store
	ArrayList<String> month1; //Report details for month
	ArrayList<String> month2; //Report details for month 
	ArrayList<String> month3; //Report details for month 
	
	MonthlyIncomesReport month12022;
	MonthlyIncomesReport month22022;
	MonthlyIncomesReport month32022;
	
	//Variables for login's tests:
	ArrayList<String> existingUserDetailsThatIsntLoggedIn;
	ArrayList<String> existingUserDetailsThatIsLoggedIn;
	ArrayList<String> existingUsernameWrongPassword;
	ArrayList<String> nonExistingUser;
	
	@Before
	public void setUp() throws Exception {
		//Reports variables initialization
		quarterWith3MonthsWithOrdersForKarmiel = new ArrayList<String>(Arrays.asList("1","2022","KARMIEL"));
		quarterWith2MonthsWithOrdersForKarmiel = new ArrayList<String>(Arrays.asList("2","2022","KARMIEL"));
		quarterWithNoMonthsWithOrdersForKarmiel = new ArrayList<String>(Arrays.asList("3","2022","KARMIEL"));
		month1 = new ArrayList<String>(Arrays.asList("01","2022","KARMIEL"));
		month2 = new ArrayList<String>(Arrays.asList("08","2022","KARMIEL"));
		month3 = new ArrayList<String>(Arrays.asList("03","2022","KARMIEL"));
		
		//Login variables initialization
		existingUserDetailsThatIsntLoggedIn = new ArrayList<String>(Arrays.asList("ceo","123456"));
		existingUserDetailsThatIsLoggedIn = new ArrayList<String>(Arrays.asList("costumer6","123456"));
		existingUsernameWrongPassword = new ArrayList<String>(Arrays.asList("costumer2","111111"));
		nonExistingUser = new ArrayList<String>(Arrays.asList("tempUser","000000"));
		MySqlConnection.connectToDb("root", "Omri1511!", "jdbc:mysql://localhost/zli?serverTimezone=IST");
	}
	
	/**
	 * Description: checking that the method get the details of one month with orders
	 * Input data: String array list with details ("01","2022","KARMIEL").
	 * Expected result: MonthlyIncomesReport with the correct details from database.
	 */
	@Test
	public void getMonthlyIncomesReportMonthsWithOrdersTest() {
		month12022 = new MonthlyIncomesReport(2530, 362, 400, "KARMIEL", "01/2022");
		MonthlyIncomesReport result = MySqlConnection.getMonthlyIncomesReport(month1);
		MonthlyIncomesReport expected = month12022;
		assertEquals(result, expected);
	}
	
	/**
	 * Description: checking that the method get null for month with no orders
	 * Input data: String array list with details ("08","2022","KARMIEL").
	 * Expected result: null.
	 */
	@Test
	public void getMonthlyIncomesReportMonthsWithNoOrdersTest() {
		MonthlyIncomesReport result = MySqlConnection.getMonthlyIncomesReport(month2);
		MonthlyIncomesReport expected = null;
		assertEquals(result, expected);
	}
	
	/**
	 * Description: checking that the method throw exception while getting null as parameter (reportDetails).
	 * Input data: null.
	 * Expected result: NullPointerException.
	 */
	@Test(expected = NullPointerException.class)
	public void getMonthlyIncomesReportSendNullAsParameterTest() {
		MySqlConnection.getQuarterlyIncomesReport(null);
	}

	
	
	
	/**
	 * Description: checking that the method get the details of 3 months of required quarter.
	 * Input data: String array list with details (quarter 1, year 2022, store karmiel).
	 * Expected result: MonthlyIncomesReport array list with the correct details of the 3 months from database.
	 */
	@Test
	public void getQuarterlyIncomesReportQuarterWith3MonthsWithOrdersTest() {
		month12022 = new MonthlyIncomesReport(2530, 362, 400, "KARMIEL", "01/2022");
		month22022 = new MonthlyIncomesReport(4523, 963, 453, "KARMIEL", "02/2022");
		month32022 = new MonthlyIncomesReport(7053, 1024, 348, "KARMIEL", "03/2022");
		ArrayList<MonthlyIncomesReport> result = MySqlConnection.getQuarterlyIncomesReport(quarterWith3MonthsWithOrdersForKarmiel);
		ArrayList<MonthlyIncomesReport> expected = new ArrayList<MonthlyIncomesReport>(Arrays.asList(month12022, month22022, month32022));
		assertArrayEquals(expected.toArray(), result.toArray());
	}
	
	/**
	 * Description: checking that the method get the details of 2 months of required quarter and handle the fact
	 * there is one month without report.
	 * Input data: String array list with details (quarter 2, year 2022, store karmiel).
	 * Expected result: MonthlyIncomesReport array list with the correct details of the 2 months from database.
	 */
	@Test
	public void getQuarterlyIncomesReportQuarterWith2MonthsWithOrdersTest() {
		month12022 = new MonthlyIncomesReport(5025, 893, 354, "KARMIEL", "04/2022");
		month22022 = new MonthlyIncomesReport(4720, 725, 410, "KARMIEL", "05/2022");
		month32022 = null;
		ArrayList<MonthlyIncomesReport> result = MySqlConnection.getQuarterlyIncomesReport(quarterWith2MonthsWithOrdersForKarmiel);
		ArrayList<MonthlyIncomesReport> expected = new ArrayList<MonthlyIncomesReport>(Arrays.asList(month12022, month22022));
		assertArrayEquals(expected.toArray(), result.toArray());
	}
	
	/**
	 * Description: checking that the method get no details for quarter without reports.
	 * Input data: String array list with details (quarter 3, year 2022, store karmiel).
	 * Expected result: Empty MonthlyIncomesReport array list.
	 */
	@Test
	public void getQuarterlyIncomesReportQuarterWithNoMonthsWithOrdersTest() {
		ArrayList<MonthlyIncomesReport> result = MySqlConnection.getQuarterlyIncomesReport(quarterWithNoMonthsWithOrdersForKarmiel);
		int expected = 0;
		assertEquals(expected, result.size());
	}
	
	/**
	 * Description: checking that the method throw exception while getting null as parameter (reportDetails).
	 * Input data: null.
	 * Expected result: NullPointerException.
	 */
	@Test(expected = NullPointerException.class)
	public void getQuarterlyIncomesReportSendNullAsParameterTest() {
		MySqlConnection.getQuarterlyIncomesReport(null);
	}
	
	
	
	
	
	/**
	 * Description: checking that the method *will not* generate+insert to database a report for month with
	 * no orders.
	 * In this test we use by the logic: call the method under test -> get the report that has been generated from the real database
	 * by using the method "getMonthlyIncomesReport"(that works properly)-> check if the result imported from the database is null.
	 * Input data: month details (month 7, year 2022, store KIRYAT_ATA).
	 * Expected result: null.
	 */
	@Test
	public void generateMonthlyIncomesReportForReportOfMonthWithZeroOrdersTest() {
		MonthWithNoWithOrdersForKarmiel = new ArrayList<String>(Arrays.asList("07","2022","KIRYAT_ATA")); 
		MySqlConnection.generateMonthlyIncomesReport(MonthWithNoWithOrdersForKarmiel);
		MonthlyIncomesReport result = MySqlConnection.getMonthlyIncomesReport(MonthWithNoWithOrdersForKarmiel);
		MonthlyIncomesReport expected = null;
		assertEquals(expected, result);
	}
	
	/**
	 * Description: checking that the method *will* generate+insert to database a report for month with amount of orders bigger than zero.
	 * In this test we use by the logic: call the method under test -> get the report that has been generated from the real database
	 * by using the method "getMonthlyIncomesReport"(that works properly)-> 
	 * check if the report that imported from the database is the same as the report we defined in the 'expected' variable.
	 * Input data: month details (month 7, year 2022, store KIRYAT_ATA).
	 * Expected result: object of type MonthlyIncomesReport with - 
	 * order incomes: 2053, deliveries incomes: 50, total refunds amount: 0, store name: "KARMIEL", report date: "06/2022"
	 */
	@Test
	public void generateMonthlyIncomesReportForReportOfMonthWithOrdersTest() {
		MonthWithOrders = new ArrayList<String>(Arrays.asList("06","2022","HAIFA")); 
		MySqlConnection.generateMonthlyIncomesReport(MonthWithOrders);
		MonthlyIncomesReport result = MySqlConnection.getMonthlyIncomesReport(MonthWithOrders);
		MonthlyIncomesReport expected = new MonthlyIncomesReport(458, 10, 25, "HAIFA", "06/2022");
		assertEquals(expected, result);
	}
	
	/**
	 * Description: checking that the method *will not* generate+insert to database a report with a given null 'ArrayList<String>' object as the report details(parameter).
	 * In this test we use by the logic: call the method under test -> get the report that has been generated from the real database
	 * by using the method "getMonthlyIncomesReport"(that works properly)-> 
	 * check if the report that imported from the database is the same as the report we defined in the 'expected' variable.
	 * Input data: month details - null object.
	 * Expected result: NullPointerException.
	 */
	@Test(expected = NullPointerException.class)
	public void generateMonthlyIncomesReportForNullMonthWithOrdersTest() {
		MySqlConnection.generateMonthlyIncomesReport(null);
	}
	
	
	
	
	/**
	 * Description: Checking login method for valid user (successful login)
	 * Input data: User details - username: "ceo", password: "123456"
	 * Expected result: User object as follow:
	 * 					new User("ceo", "123456", "Eliav", "Shabat", UserType.CEO, "zlibraude@gmail.com", "0543320955", false, "1");
	 */
	@Test
	public void loginForExitingUloginForExitingUserThatIsNotLoggedInTestserThatIsNotLoggedInTest() {
		User result = MySqlConnection.checkLoginCredentialsAndImportUserDetails(existingUserDetailsThatIsntLoggedIn);
		System.out.println("-------------------" + result.getLoginStatus() + "-------------------");
		User expected = new User("ceo", "123456", "Eliav", "Shabat", UserType.CEO, "zlibraude@gmail.com", "0543320955", false, "1");
		assertEquals(expected, result);
	}
	
	/**
	 * Description: Checking login method for valid user that already logged in (unsuccessful login)
	 * Input data: User details - username: "costumer6", password: "123456"
	 * Expected result: User object as follow:
	 * 					new User("ceo", "123456", "Eliav", "Shabat", UserType.CEO, "zlibraude@gmail.com", "0543320955", false, "1");
	 */
	@Test
	public void loginForExitingUserThatIsLoggedInTest() {
		User result = MySqlConnection.checkLoginCredentialsAndImportUserDetails(existingUserDetailsThatIsLoggedIn);
		User expected = new User("costumer6", "123456", "Prim", "Brand", UserType.Costumer, "zlibraude@gmail.com", "0523215682", true, "7");
		assertEquals(expected, result);
	}
	
	/**
	 * Description: Checking login method for invalid user (unsuccessful login)
	 * Input data: User details - username: "costumer2", password: "111111"
	 * Expected result: null
	 */
	@Test
	public void loginForExitingUserWrongPassword() {
		User result = MySqlConnection.checkLoginCredentialsAndImportUserDetails(existingUsernameWrongPassword);
		User expected = null;
		assertEquals(expected, result);
	}
	
	/**
	 * Description: Checking login method for non exist user (unsuccessful login)
	 * Input data: User details - username: "tempUser", password:"000000".
	 * Expected result: null
	 */
	@Test
	public void loginForNonExitingUserThatIsNotLoggedInTest() {
		User result = MySqlConnection.checkLoginCredentialsAndImportUserDetails(nonExistingUser);
		User expected = null;
		assertEquals(expected, result);
	}
	
	
}
