package EntityControllers;

import Entities.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
*The class CatalogItemController stores the Item Catalog that are returned from the DB and passes it to the Server.
* @author Biran Fridman
*/
public class CatalogItemController {
	private static ObservableList<Product> itemCatalog=FXCollections.observableArrayList();

	public static ObservableList<Product> getItemCatalog() {
		return itemCatalog;
	}

	public static void setItemCatalog(ObservableList<Product> observableList) {
		itemCatalog=observableList;
	}
}
