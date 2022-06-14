package ClientControllers;

import Entities.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
/**
 * The class ComponentController is the Controller of "Component in the Component Catalog" user Screen
 * @author Biran Fridman
 */
public class ComponentController {
	
	public static ComponentsCatalogController catalogInstance;

    @FXML
    private ImageView productIMG;

    @FXML
    private Label nameTXT;

    @FXML
    private Label idTXT;

    @FXML
    private Label categoryTXT;

    @FXML
    private Label colorTXT;

    @FXML
    private Label componentTXT;

    @FXML
    private HBox picHBox;

    @FXML
    private ImageView categoryIMG;

    @FXML
    private Button addToCartBTN;
    
    private Product item;
    
    /**
     * addToCart(ActionEvent event)
     * this method is called when addToCartBTN is pressed
     * it calls the addToCart function of the Component Catalog Instance using CatalogInstance variable
     * @param event
     **/
    @FXML
    void addToCart(ActionEvent event) {
    	if (catalogInstance != null) {
			catalogInstance.addToCart(item);
		}
    }
    /**
     * setData(Product item)
     * this method is called by the Component catalog controller
     * it sets the data of the Component on the User screen
     * @param item the component we want to set it's details
     **/
    public void setData(Product item) { // initializing the text fields and image for each item screen
		this.item = item;
		Image itemImage = new Image(getClass().getResourceAsStream(item.getImgSrc()));
		productIMG.setImage(itemImage);
		nameTXT.setText(item.getName());
		idTXT.setText(idTXT.getText() + " " + String.valueOf(item.getId()));
		categoryTXT.setText(categoryTXT.getText() + " " + item.getCategory());
		colorTXT.setText(colorTXT.getText() + " " + item.getColor());
		Image categoryImage = new Image(getClass().getResourceAsStream(item.getCategoryImgPath(item.getCategory())));
		categoryIMG.setImage(categoryImage);
	}
    /**
     * setInstance(ComponentsCatalogController catalog)
     * static method that sets the catalog instance if it is null, singeltone desgin pattern
     * we use this instance to call setdata
     * @param catalog Instance of the ComponentsCatalogController
     **/
    public static void setInstance(ComponentsCatalogController catalog) {
		if (catalogInstance == null)
			catalogInstance = catalog;
	}
    /**
     * deleteInstance()
     * static method that deletes the catalog instance if it is not null, singeltone desgin pattern
     * we use this when we exist the catalog
     **/
	public static void deleteInstance() {
		if (catalogInstance != null)
			catalogInstance = null;
	}

}
