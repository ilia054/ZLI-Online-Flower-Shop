package ClientControllers;

import Entities.AbstractProduct;
import Entities.CustomProduct;
import Entities.Product;
import client.ZliClient;
import common.ChangeScene;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * this is the Product in cart Controller, it holds all of the logic
 * for the product in the cart screen (basiclly an HBOX) in the cart
 * @author Ilya
 */
public class ProductInCartController {

	@FXML
	private ImageView productIMG;

	@FXML
	private Label productNameTXT;

	@FXML
	private FontAwesomeIconView minusFXICON;
	@FXML
	private FontAwesomeIconView ComponenetsFXICON;

	@FXML
	private TextField quantityTXTField;

	@FXML
	private FontAwesomeIconView plusFXICON;

	@FXML
	private Label priceTXT;

	@FXML
	private FontAwesomeIconView removeFXICON;

	private int indexInCatalogVBOX;
	private int productAmountInCart = 1;
	private AbstractProduct product;
	private float price;
	private ChangeScene scene = new ChangeScene();

	@FXML
	void removeFromCart(MouseEvent event) {
		removeFromCart();
	}

	@FXML
	void incrementProductAmount(MouseEvent event) {
		incrementProductAmount(true);
	}

	@FXML
	void showComponenets(MouseEvent event) {
		if (product.getComponents() == null)
			scene.ErrorMessage("There are no Components in an ITEM");

		else
			scene.HelpMessage("Componenets: \n\n" + product.getComponents() + "\n\nDominant Color: "
					+ product.getColor() + "\n\nArrangment: " + product.getCategory());

	}
	/**
	 * SetData sets the Data for the Product\Item\Discount
	 * Price,Image,Name,Quantity,etc..
	 * @param product is the product user has choosen
	 * @param index The index the FXML instance in the VBOX
	 */
	public void setData(AbstractProduct product, int index) {
		this.product = product;
		indexInCatalogVBOX = index;
		Image productImage = new Image(getClass().getResourceAsStream(product.getImgSrc()));
		productIMG.setImage(productImage);
		productNameTXT.setText(product.getName());
		if (product instanceof Product)
			price = ((Product) product).getPrice();
		else
			price = (float) ((CustomProduct) product).getPrice();

		priceTXT.setText(String.format("%.2f",price)+"$");
		quantityTXTField.setEditable(false);

	}
	/**
	 * incrementProductAmount(boolean AddToCart)
	 * called when user clicks "+" in the prodcut\item\customproduct in cart.
	 * increases the quantity of the product selecte by 1
	 * @param AddToCart indicates whether to add the product to the cartController cart or not
	 */
	public void incrementProductAmount(boolean AddToCart) {
		if (AddToCart)
			ZliClient.cartController.getCart().add(product);
		quantityTXTField.setText(String.valueOf(++productAmountInCart));
		ProductController.catalogInstance.updateOrderTotal();
	}
	/**
	 * incrementProductAmount(MouseEvent event)
	 * called when user clicks "-" in the prodcut\item\customproduct in cart.
	 * decreases the quantity of the product selecte by 1
	 * @param event
	 */
	@FXML
	void decrementProductAmount(MouseEvent event) {
		if (productAmountInCart == 1)
			removeFromCart();
		else {
			quantityTXTField.setText(String.valueOf(--productAmountInCart));
			ZliClient.cartController.getCart().remove(product);
			ProductController.catalogInstance.updateOrderTotal();
		}
	}
	/**
	 * removeFromCart()
	 * called when user clicks "X" in the prodcut\item\customproduct in cart.
	 * removes the product from the cart
	 */
	void removeFromCart() {
		ProductController.catalogInstance.removeFromCart(indexInCatalogVBOX);
		for (int i = 0; i < productAmountInCart; i++)
			ZliClient.cartController.getCart().remove(product);
	}
	/**
	 * decIndex()
	 * this method is invoked when user removes a product from the cart
	 * we need to "fix" the indexes in the VBOX so we use this method to decrease the index by 1
	 */
	public void decIndex() {
		--indexInCatalogVBOX;
	}
	/**
	 * getProductID()
	 * getter for product ID
	 * @return the product id
	 */
	public int getProductID() {
		return product.getId();
	}
	/**
	 * getTotalPrice()
	 * getter total price for the product in the cart
	 * @return float quantity*price
	 */
	public float getTotalPrice() {
		return price * productAmountInCart;
	}
	/**
	 * getIndex()
	 * returns the indedx of the product in the Vbox
	 * @return int index of the product in the cart
	 */
	public int getIndex() {
		return indexInCatalogVBOX;
	}
	/**
	 * getProduct()
	 * returns the product instnace of the Product in cart selected
	 * @return AbstractProduct of the product selected
	 */
	public AbstractProduct getProduct() {
		return product;
	}
	/**
	 * getAmountInCart()
	 * returns the product quantity in cart
	 * @return int amount of product in cart, basiclly the quantity
	 */
	public int getAmountInCart() {
		return productAmountInCart;
	}

}
