package EntityControllers;

import java.util.HashMap;

import Entities.Sale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
*The class SalesController stores information the MarketingEmpoyee user uses for sales
* @author Biran Fridman
*/
public class SalesController {
	private ObservableList<Sale> activesales = FXCollections.observableArrayList();
	private boolean hasSaleForProductID;
	private HashMap<Integer, Sale> activeSalesMap = new HashMap<>();


	public boolean getHasSaleForProductID() {
		return hasSaleForProductID;
	}

	public void setHasSaleForProductID(boolean hasSaleForProductID) {
		this.hasSaleForProductID = hasSaleForProductID;
	}

	public HashMap<Integer, Sale> getActiveSalesMap() {
		return activeSalesMap;
	}

	public void setActivesales(ObservableList<Sale> activesales) {
		this.activesales = activesales;
		changeFromListToMap();
	};

	/**
	 * changeFromListToMap()
	 * creates a hashmap from the ObservableList
	 */
	private void changeFromListToMap() {
		for (Sale sale : activesales) {
			if (!activeSalesMap.containsKey(sale.getProductID()))
				activeSalesMap.put(sale.getProductID(), sale);
		}
	}

}
