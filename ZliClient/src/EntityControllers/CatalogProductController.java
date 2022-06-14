package EntityControllers;


import Entities.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
*The class CatalogProductController stores the Product Catalog that are returned from the DB and passes it to the Server
*so we can have access to it without directly calling the Data base each time we want to view the orders
* @author Biran Fridman
*/
public class CatalogProductController {
	private static ObservableList<Product> productCatalog=FXCollections.observableArrayList();

	public ObservableList<Product> getProductCatalog() {
		return productCatalog;
	}

	public void setProductCatalog(ObservableList<Product> observableList) {
		productCatalog=observableList;
	}
}