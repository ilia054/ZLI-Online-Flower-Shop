package ClientControllers;

import Entities.MonthlyIncomesReport;
import Enums.Task;
import client.ZliClient;
import client.ZliClientUI;
import common.ChangeScene;
import common.Message;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * The class MonthlyIncomesReportController is the Controller of "MonthlyIncomesReport" Screen
 */
public class MonthlyIncomesReportController {

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
	    private Label totalIncomesLabel;
	    @FXML
	    private FontAwesomeIconView BackBtn;

	    @FXML
	    private PieChart IncomeReportPie;

	    private ChangeScene scene = new ChangeScene();
	    

		/**
		 * This method initiate the screen of the relevance FXML. get from controller the data about the report
		 * and show it on screen.
		 */
	    @FXML
		public void initialize() {
	    	ZliClientUI.chat.accept(new Message(Task.Get_user_store, ZliClient.userController.getUser()));
	    	MonthlyIncomesReport monthlyIncomesReport = ZliClient.monthlyIncomesReportController.getMonthlyIncomesReport();
			float ordersIncomes = monthlyIncomesReport.getOrdersIncomes();
			float deliveriesIncomes = monthlyIncomesReport.getDeliveriesIncomes();
			float refunds = monthlyIncomesReport.getRefunds();
	        ObservableList<PieChart.Data> pieChartData =
	                FXCollections.observableArrayList(
	                								  new PieChart.Data("orders Incomes", (double)ordersIncomes),
	                								  new PieChart.Data("deliveries Incomes", (double)deliveriesIncomes),
	                								  new PieChart.Data("refunds", (double)refunds));
	        IncomeReportPie.setData(pieChartData);
	        storeLabel.setText(ZliClient.monthlyIncomesReportController.getMonthlyIncomesReport().getStore());
	        dateLabel.setText(ZliClient.monthlyIncomesReportController.getMonthlyIncomesReport().getDate());
	        orderIncomesLabel.setText("Orders incomes: " + ordersIncomes + "$");
	        deliveriesIncomesLabel.setText("Deliveries incomes: "+ deliveriesIncomes+ "$");
	        refundsOutcomesLabel.setText("Refunds outcomes: " + refunds+ "$");
	        totalIncomesLabel.setText("Total incomes: " + (ordersIncomes + deliveriesIncomes - refunds)+ "$");
		}
	    
		/**
		 * This method terminates MonthlyIncomesReport scene and opens the ChooseMonthlyReports
		 * @param event mouse clicked on Back button
		 */
	    @FXML
	    void Back(MouseEvent event) {
	    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			scene.changeScreen(new Stage(), "/GuiClientScreens/ChooseMonthlyReports.fxml", true);
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
