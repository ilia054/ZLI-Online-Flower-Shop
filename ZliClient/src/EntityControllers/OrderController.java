package EntityControllers;

import Entities.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
*This class stores the Orders that are returned from the DB and passes it to the Server
*so we can have access to it without directly calling the Data base each time we want to view the orders
* @author Michael Ioffe
*/
public class OrderController {
	private  ObservableList<Order> orders=FXCollections.observableArrayList();

	public  ObservableList<Order> getOrders() {
		return orders;
	}

	public  void setOrders(ObservableList<Order> observableList) {
		orders=observableList;
	}
}