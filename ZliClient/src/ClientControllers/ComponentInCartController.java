package ClientControllers;

import Entities.Product;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * The class ComponentInCartController is the Controller of "Component in the cart of Component Catalog" user Screen
 * @author Biran Fridman
 *
 */
public class ComponentInCartController {
    @FXML
    private ImageView itemIMG;

    @FXML
    private Label itemNameTXT;

    @FXML
    private FontAwesomeIconView removeFXICON;
    
    private int indexInCatalogVBOX;

	private Product item;

    @FXML
    void removeFromCart(MouseEvent event) {
    	ComponentController.catalogInstance.removeFromCart(indexInCatalogVBOX);

    }
    /**
     * setData(Product item, int index)
     * this method is called by the Component catalog controller
     * it sets the data of the Component in cart on the User screen
     * @param item The Component we want to set it's data
     * @param index The index of the component in the cart
     **/
    public void setData(Product item, int index) {
		this.item = item;
		indexInCatalogVBOX = index;
		Image itemImage = new Image(getClass().getResourceAsStream(item.getImgSrc()));
		itemIMG.setImage(itemImage);
		itemNameTXT.setText(item.getName());

	}
    
    public void decIndex() {
		--indexInCatalogVBOX;
	}

	public int getItemID() {
		return item.getId();
	}

	public String getItemColor() {
		return item.getColor();
	}

	public String getItemName() {
		return item.getName();
	}

	public int getIndex() {
		return indexInCatalogVBOX;
	}
	
	public Product getComponent() {
		return item;
	}

}
