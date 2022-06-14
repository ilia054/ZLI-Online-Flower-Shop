package ClientControllers;

import Entities.Product;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
/**
 * This class holds the logic and visual for Item in cart Screen
 * @author Ilya
 *
 */

public class ItemInCartController {

	@FXML
	private ImageView itemIMG;

	@FXML
	private Label itemNameTXT;

	@FXML
	private FontAwesomeIconView removeFXICON;

	private int indexInCatalogVBOX;

	private Product item;
/**
 * removeFromCart(MouseEvent event)
 * removes the item from the cart, and removs it from CartController
 * @param event when X is clicked on the cart HBOX
 */
	@FXML
	void removeFromCart(MouseEvent event) {
		ItemController.catalogInstance.removeFromCart(indexInCatalogVBOX);
	}
	/**
	 * SetData sets the Data for the Product\Item\Discount
	 * Price,Image,Name,Quantity,etc..
	 * @param item is the product\item user has choosen
	 * @param index The index the FXML instance in the VBOX
	 */
	public void setData(Product item, int index) {
		this.item = item;
		indexInCatalogVBOX = index;
		Image itemImage = new Image(getClass().getResourceAsStream(item.getImgSrc()));
		itemIMG.setImage(itemImage);
		itemNameTXT.setText(item.getName());

	}
	/**
	 * decIndex()
	 * decresses index by 1
	 */
	public void decIndex() {
		--indexInCatalogVBOX;
	}
	/**
	 * getItemID()
	 * @return item.ID
	 */
	public int getItemID() {
		return item.getId();
	}
	/**
	 * getItemColor()
	 * @return the String for the color of the item in cart
	 */
	public String getItemColor() {
		return item.getColor();
	}
	/**
	 * getItemName()
	 * @return a String of item name
	 */
	public String getItemName() {
		return item.getName();
	}
	/**
	 * getIndex()
	 * @return the index of the HBOX in the cart
	 */
	public int getIndex() {
		return indexInCatalogVBOX;
	}
	/**
	 * getItemPrice()
	 * @return item price
	 */
	public double getItemPrice() {
		return (double) item.getPrice();
	}

}
