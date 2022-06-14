package ClientControllers;

import java.util.HashMap;
import java.util.Map;
import Entities.MonthlyOrdersReport;
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
 * The class MonthlyOrdersReportGuiController is the Controller of "MonthlyOrdersReportGui" Screen
 */
public class MonthlyOrdersReportGuiController {

    @FXML
    private Button XBtn;

    @FXML
    private FontAwesomeIconView BackBtn;

    @FXML
    private Label storeLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private PieChart OrdersReportPie;

    @FXML
    private Label mostWantedLabel;

    @FXML
    private Label canceledOrdersLabel;

    @FXML
    private Label numberOfOrdersPerDayLabel;

    @FXML
    private Label totalOrdersCountLabel;
    
    private ChangeScene scene = new ChangeScene();

	/**
	 * This method initiate the screen of the relevance FXML. get from controller the data about the report
	 * and show it on screen.
	 */
    public void initialize() {
    	MonthlyOrdersReport monthlyOrdersReport = ZliClient.monthlyOrdersReportController.getMonthlyOrdersReport();
		String mostWantedItem = monthlyOrdersReport.getMostWantedItemName();
		int numberOfCanceledOrders = monthlyOrdersReport.getNumberOfCanceledOrders();
		float ordersPerDayAvg = monthlyOrdersReport.getOrdersPerDayAvg();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        HashMap<String, Integer> itemsCounter = monthlyOrdersReport.getItemsAndAmountHashMap();
        for (Map.Entry<String, Integer> item : itemsCounter.entrySet()) {
			String itemName = item.getKey();
			Integer itemCounter = item.getValue();
			pieChartData.add(new PieChart.Data(itemName, itemCounter));
		}
        OrdersReportPie.setData(pieChartData);
        storeLabel.setText(ZliClient.monthlyOrdersReportController.getMonthlyOrdersReport().getStore());
        dateLabel.setText(ZliClient.monthlyOrdersReportController.getMonthlyOrdersReport().getDate());
        mostWantedLabel.setText("Most wanted: " + mostWantedItem);
        canceledOrdersLabel.setText("Canceled orders: "+ numberOfCanceledOrders);
        numberOfOrdersPerDayLabel.setText("No. orders per day avg: " + String.format("%.2f",ordersPerDayAvg));
        totalOrdersCountLabel.setText("Total orders count: "+ monthlyOrdersReport.getTotalOrdersCount());
	}
    
	/**
	 * This method terminates MonthlyOrdersReportGuiController scene and opens the ChooseMonthlyReports
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