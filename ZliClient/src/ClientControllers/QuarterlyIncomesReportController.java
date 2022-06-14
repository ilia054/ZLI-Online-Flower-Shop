package ClientControllers;


import java.util.ArrayList;
import Entities.MonthlyIncomesReport;
import Enums.Task;
import client.ZliClient;
import client.ZliClientUI;
import common.ChangeScene;
import common.Message;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * The class QuarterlyIncomesReportController is the Controller of "QuarterlyIncomesReport" Screen
 */
public class QuarterlyIncomesReportController {

	    @FXML
	    private Button XBtn;
	    @FXML
	    private Label storeLabel;

	    @FXML
	    private Label dateLabel;

	    @FXML
	    private Label orderIncomesLabel;

	    @FXML
	    private Label deliveriesIncomesLabel;

	    @FXML
	    private Label refundsOutcomesLabel;
	    
	    @FXML
	    private Label mostProfitableMonth;

	    @FXML
	    private Label leastProfitableMonth;
	    
	    @FXML
	    private Label totalIncomesLabel;
	    
	    @FXML
	    private FontAwesomeIconView BackBtn;
	    
	    @FXML
	    private Label averageIncomePerMonthLabel;

	    @FXML
	    private BarChart<?, ?> incomesBar;

	    private ChangeScene scene = new ChangeScene();
	    
	    @FXML
	    private Label errorLabel;
	    
	    @FXML
	    private Button compareReportsBtn;
	    
	    private static int screenCounter = 0;
	    
	    public ArrayList<String> dataToReport = new ArrayList<String>();
	

		/**
		 * This method initiate the screen of the relevance FXML. get from controller the data about the report
		 * and show it on screen.
		 */
	    
	    @SuppressWarnings({ "unchecked", "rawtypes" })
		@FXML
		public void initialize() {
	    	ZliClientUI.chat.accept(new Message(Task.Get_user_store, ZliClient.userController.getUser()));
	    	ObservableList<MonthlyIncomesReport> monthlyIncomesReports = ZliClient.monthlyIncomesReportController.getMonthlyIncomesReports();
	    	CategoryAxis xAxis = new CategoryAxis();
	    	NumberAxis yAxis = new NumberAxis();
	        xAxis.setLabel("Month");       
	        yAxis.setLabel("Profit");
	    	XYChart.Series series1 = new XYChart.Series();
	        series1.setName("Orders Incomes"); 
	        XYChart.Series series2 = new XYChart.Series();
	        series2.setName("Deliveries Incomes");
	        XYChart.Series series3 = new XYChart.Series();
	        series3.setName("Refunds");
	        
			getDetailsForReport(monthlyIncomesReports);
			
	    	for (MonthlyIncomesReport monthlyIncomesReport : monthlyIncomesReports) {
	    		series1.getData().add(new XYChart.Data(monthlyIncomesReport.getDate(), monthlyIncomesReport.getOrdersIncomes()));
	    		series2.getData().add(new XYChart.Data(monthlyIncomesReport.getDate(), monthlyIncomesReport.getDeliveriesIncomes()));
	    		series3.getData().add(new XYChart.Data(monthlyIncomesReport.getDate(), monthlyIncomesReport.getRefunds()));
	    	}
	    	
	        incomesBar.getData().addAll(series1, series2, series3);
	        storeLabel.setText(ZliClient.monthlyIncomesReportController.getMonthlyIncomesReports().get(0).getStore());
	        String date = ZliClient.monthlyIncomesReportController.getMonthlyIncomesReports().get(0).getDate();
	        date = date.substring(0, 2);
	        switch (date) {
	        	case "01":
	        		date = "First quarter";
	        		break;
	        	case "04":
	        		date = "Second quarter";
	        		break;
	        	case "07":
	        		date = "Third quarter";
	        		break;
	        	case "10":
	        		date = "Forth quarter";
	        		break;
	        }
	        dateLabel.setText(date);
	        orderIncomesLabel.setText("Orders incomes: " + dataToReport.get(2) + "$");
	        deliveriesIncomesLabel.setText("Deliveries incomes: "+ dataToReport.get(3)+ "$");
	        refundsOutcomesLabel.setText("Refunds outcomes: " + dataToReport.get(4)+ "$");
	        totalIncomesLabel.setText("Total incomes: " + (dataToReport.get(5)) + "$");
	        mostProfitableMonth.setText("Most profitable month: " + dataToReport.get(0));
	        leastProfitableMonth.setText("Least profitable month: " + dataToReport.get(1));
	        averageIncomePerMonthLabel.setText("Average income per month: " + String.format("%.2f", (dataToReport.get(6)) + "$"));
	        errorLabel.setVisible(false);
		}
	    
		public void getDetailsForReport(ObservableList<MonthlyIncomesReport> monthlyIncomesReports) {
			float ordersIncomes = 0;
			float deliveriesIncomes = 0;
			float refunds = 0;
			float bestFloat = 0;
			float leastFloat = Integer.MAX_VALUE;
			String bestMonth = null;
			String leastMonth = null;
			float totalIncomes = 0;
			float realtotalIncomes = 0;
	    	for (MonthlyIncomesReport monthlyIncomesReport : monthlyIncomesReports) {
	    		if (monthlyIncomesReport == null)
	    			continue;
	    		ordersIncomes += monthlyIncomesReport.getOrdersIncomes();
	    		deliveriesIncomes += monthlyIncomesReport.getDeliveriesIncomes();
	    		refunds += monthlyIncomesReport.getRefunds();
	    		totalIncomes = monthlyIncomesReport.getOrdersIncomes() + monthlyIncomesReport.getDeliveriesIncomes() - monthlyIncomesReport.getRefunds();
	    		realtotalIncomes += totalIncomes;
	    		if (totalIncomes > bestFloat) {
	    			bestFloat = totalIncomes;
	    			bestMonth = monthlyIncomesReport.getDate();
	    		}
	    		if (totalIncomes < leastFloat) {
	    			leastFloat = totalIncomes;
	    			leastMonth = monthlyIncomesReport.getDate();
	    		}
			}
	    	
	    	dataToReport.add(bestMonth);
	    	dataToReport.add(leastMonth);
	    	dataToReport.add(String.valueOf(ordersIncomes));
	    	dataToReport.add(String.valueOf(deliveriesIncomes));
	    	dataToReport.add(String.valueOf(refunds));
	    	dataToReport.add(String.valueOf(realtotalIncomes));
	    	dataToReport.add(String.valueOf((realtotalIncomes) / 3));
		}
		
		public QuarterlyIncomesReportController() {
		}

		/**
		 * This method terminates QuarterlyIncomesReportController scene and opens the ChooseQuarterlyReports
		 * @param event mouse clicked on Back button
		 */
	    @FXML
	    void Back(MouseEvent event) {
	    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			scene.changeScreen(new Stage(), "/GuiClientScreens/ChooseQuarterlyReports.fxml", true);
	    }

		/**
		 * Closes the applications and sends a Request_disconnected to server side
		 * @param event mouse clicked on X Icon
		 **/
	    @FXML
	    void X(ActionEvent event) {
			if (screenCounter == 1) {
				screenCounter--;
		    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window\
			}
			else if (screenCounter == 0) {
					ZliClientUI.chat.accept(new Message(Task.Request_logout, ZliClient.userController.getUser().getUsername()));
					ZliClientUI.chat.accept(new Message(Task.Request_disconnected, null));
					System.exit(0);
				}	
	    }
	    
		/**
		 * This method let the user to open second screen and choose another report in order to compare 2 reports
		 * @param event mouse clicked on compareReports button
		 **/
		@FXML
		void compareReportsClicked(ActionEvent event) {
			if(screenCounter == 0) {
				screenCounter++;
				scene.changeScreen(new Stage(), "/GuiClientScreens/ChooseQuarterlyReports.fxml", true);
			}
			else {
				errorLabel.setVisible(true);
			}
			
		}

}
