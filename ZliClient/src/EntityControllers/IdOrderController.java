package EntityControllers;

import java.util.HashMap;
import Entities.Order;
import client.ZliClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
* The class IdOrderController represent the data structure that save IdOrders that imported or will be saved in data base.
* @author Omri Cohen
*/
public class IdOrderController {
	private static ObservableList<Integer> idorder=FXCollections.observableArrayList();
	private static HashMap<Integer,Order> ordersMap = new HashMap<>();
	private static ObservableList<Order> ordersArrayList =FXCollections.observableArrayList();

	public static ObservableList<Integer> getIdorders() {
		return idorder;
	}

	public static void setIdOrders(ObservableList<Integer> observableList) {
		idorder=observableList;

	}

	@SuppressWarnings("static-access")
	public static  HashMap<Integer, Order> getOrdersMap() {
		ordersArrayList = ZliClient.orderController.getOrders();
		changeFromListToMap();
		return ordersMap;
	}

	@SuppressWarnings("static-access")
	public void setOrdersMap(HashMap<Integer, Order> ordersMap) {
		this.ordersMap = ordersMap;
	}
	
	private static void changeFromListToMap() {
		for (Order order : ordersArrayList) {
			if (!ordersMap.containsKey(order.getOrderNumber()))
				ordersMap.put(order.getOrderNumber(), order);
		}
	}

}