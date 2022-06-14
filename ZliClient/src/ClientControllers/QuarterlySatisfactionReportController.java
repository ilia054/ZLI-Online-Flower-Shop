package ClientControllers;


import Entities.QuarterlySatisfactionReport;
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
 * The class QuarterlySatisfactionReportController is the Controller of "QuarterlySatisfactionReport" Screen
 */
public class QuarterlySatisfactionReportController {

	    @FXML
	    private Button XBtn;

	    @FXML
	    private FontAwesomeIconView BackBtn;

	    @FXML
	    private Label storeLabel;

	    @FXML
	    private Label dateLabel;

	    @FXML
	    private Label ordersLabel;

	    @FXML
	    private Label complaintsLabel;

	    @FXML
	    private Label satisfactionRateLabel;

	    @FXML
	    private Label bestMonthLabel;

	    @FXML
	    private Label worstMonthLabel;

	    @FXML
	    private BarChart<?, ?> satisfactionBar;

	    @FXML
	    private Label averageComplaintsPerMonthLabel;

	    private ChangeScene scene = new ChangeScene();
	    
	    @FXML
	    private Label errorLabel;
	    
		/**
		 * This method initiate the screen of the relevance FXML. get from controller the data about the report
		 * and show it on screen.
		 */
	    @SuppressWarnings({ "unchecked", "rawtypes" })
		@FXML
		public void initialize() {
	    	ObservableList<QuarterlySatisfactionReport> quarterlySatisfactionReports = ZliClient.quarterlySatisfactionReportController.getQuarterlySatisfactionReport();
	    	int orders = 0;
			int complaints = 0;
			float satisfactionRate = 0;
			float bestfloat = 0;
			float worstfloat  = 1000000;
			String bestMonth = null;
			String worstMonth = null;
			@SuppressWarnings("unused")
			float totalIncomes = 0;
	    	final CategoryAxis xAxis = new CategoryAxis();
	        final NumberAxis yAxis = new NumberAxis();
	        xAxis.setLabel("Month");       
	        yAxis.setLabel("Orders/Complaints");
	    	XYChart.Series series1 = new XYChart.Series();
	        series1.setName("Orders"); 
	        XYChart.Series series2 = new XYChart.Series();
	        series2.setName("Complaints");
	        
	    	for (QuarterlySatisfactionReport monthSatisfactionReport : quarterlySatisfactionReports) {
	    		if (monthSatisfactionReport == null)
	    			continue;
	    		orders += monthSatisfactionReport.getOrders();
	    		complaints += monthSatisfactionReport.getComplaints();
	    		satisfactionRate = monthSatisfactionReport.getSatisfactionRate();
	    		if (satisfactionRate > bestfloat) {
	    			bestfloat = satisfactionRate;
	    			bestMonth = monthSatisfactionReport.getDate();
	    		}
	    		if (satisfactionRate < worstfloat) {
	    			worstfloat = satisfactionRate;
	    			worstMonth = monthSatisfactionReport.getDate();
	    		}
	    		series1.getData().add(new XYChart.Data(monthSatisfactionReport.getDate(), monthSatisfactionReport.getOrders()));
	    		series2.getData().add(new XYChart.Data(monthSatisfactionReport.getDate(), monthSatisfactionReport.getComplaints()));
	    		
			}
	    	String date = "";
	    	satisfactionBar.getData().addAll(series1, series2);
	        storeLabel.setText(ZliClient.quarterlySatisfactionReportController.getQuarterlySatisfactionReport().get(0).getStore());
	        date = ZliClient.quarterlySatisfactionReportController.getQuarterlySatisfactionReport().get(0).getDate();
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
	        ordersLabel.setText("Orders: " + orders);
	        complaintsLabel.setText("Complaints: "+ complaints);
	        if(orders == 0)
	        	satisfactionRateLabel.setText("Satisfaction rate: 100%");
	        else
	        	satisfactionRateLabel.setText("Satisfaction rate: " + String.format("%.2f", ((1-((float)complaints / (float)orders)))*100) + "%");
	        bestMonthLabel.setText("Best month: " + bestMonth);
	        worstMonthLabel.setText("Worst month: " + worstMonth);
	        averageComplaintsPerMonthLabel.setText("Average complaints per month: " + String.format("%.2f", (float)((float)(complaints) / 3)));

		}
		@FXML
		void Help(MouseEvent event) {
			scene.HelpMessage(
					"The  best and worst months are choosen by the satisfaction rate parameter\n(total orders/complaints)");
		}
		/**
		 * This method terminates QuarterlySatisfactionReportController scene and opens the ChooseQuarterlyReports
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
	    	ZliClientUI.chat.accept(new Message(Task.Request_logout, ZliClient.userController.getUser().getUsername()));
			ZliClientUI.chat.accept(new Message(Task.Request_disconnected, null));
			System.exit(0);
				
	    }


}
