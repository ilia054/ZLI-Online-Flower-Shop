package ClientControllers;


import Entities.AbstractProduct;
import Entities.CustomProduct;
import Entities.Product;
import common.ChangeScene;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
/**
 * The class AbsProductInCartController represents item in cart in the "Payment" screen
 * @author Ilya
 *
 */
public class AbsProductInCartController {

    @FXML
    private ImageView productIMG;

    @FXML
    private Label productNameTXT;

    @FXML
    private FontAwesomeIconView ComponenetsFXICON;

    @FXML
    private TextField quantityTXTField;

    @FXML
    private Label priceTXT;

	private int indexInCatalogVBOX;
	private int productAmountInCart = 1;
	private AbstractProduct product;
	private float price;
	private ChangeScene scene = new ChangeScene();

/**
 * Method is called when "Show Componenets" icon is clicked
 * this method displays all of the componenents of the item in the cart
 * if its an "item" we show no componenets
 * @param event the button clicked
 */
	@FXML
	void showComponenets(MouseEvent event) {
		if (product.getComponents() == null)
		{
			if(product.getName().equals("20% Discount")) //product is discount
				scene.HelpMessage("20% discount for first order");
			else scene.ErrorMessage("There are no Components in an ITEM"); //product is item
		}
		else// product is product
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
		String priceString="";
		priceString=String.format("%.02f", price);
		priceTXT.setText(priceString+"$");
		quantityTXTField.setEditable(false);

	}
	/**
	 * increaseAmount
	 * Increases the amount of the item\product in cart by 1
	 */
	public void increaseAmount() {
		quantityTXTField.setText(String.valueOf(++productAmountInCart));
	}
	/**
	 * decIndex
	 * Decreases the Index of the controller in the VBOX
	 * if an item\product is deleted we need to "balance" the indexses of all children
	 */
	public void decIndex() {
		--indexInCatalogVBOX;
	}
	/**
	 * getProductID
	 * @return returns the product ID (Integer)
	 */
	public int getProductID() {
		return product.getId();
	}
	/**
	 * getTotalPrice
	 * @return returns the total price of the Item\Product in cart (Pricec * quantity) as FLOAT
	 */
	public float getTotalPrice() {
		return price * productAmountInCart;
	}
	/**
	 * getIndex
	 * @return returns the nodes index (Integer)
	 */
	public int getIndex() {
		return indexInCatalogVBOX;
	}
	/**
	 * getProduct
	 * @return returns the AbstractProduct that the Node is holding
	 */
	public AbstractProduct getProduct() {
		return product;
	}
	/**
	 * getAmountInCart
	 * @return returns amount of the AbstractProduct in cart (Integer)
	 */
	public int getAmountInCart() {
		return productAmountInCart;
	}

}
