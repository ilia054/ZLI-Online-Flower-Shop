package ClientControllers;
import java.io.File;
/**
 * This is the class for Product screen, it hold all of its logic
 */
import java.util.ArrayList;

import Entities.Product;
import Entities.Sale;
import common.Time;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 * This class holds the logic for Product screen
 * @author Ilya
 */
public class ProductController {
	public static ProductCatalogController catalogInstance;

	@FXML
	private ImageView productIMG;

	@FXML
	private Label nameTXT;

	@FXML
	private Text oldPriceTXT;

	@FXML
	private Label idTXT;

	@FXML
	private Label categoryTXT;

	@FXML
	private Label colorTXT;

	@FXML
	private Label ProductCompTXT;

	@FXML
	private Label componentTXT;

	@FXML
	private ImageView SaleIMG;

	@FXML
	private ImageView categoryIMG;

	@FXML
	private Label priceTXT;

	@FXML
	private Button addToCartBTN;

	@FXML
	private HBox priceHBox;

	@FXML
	private HBox picHBox;

	@FXML
	private Label saleEndsTimerTXT;

	private Product product;

	private Sale sale;
	/**
	 * SetData sets the Data for the Product
	 * Price,Image,Name,Quantity,etc..
	 * @param product is the product user has chosen
	 */
	public void setData(Product product) {
		this.product = product;
		productIMG.setImage(setImage(product.getImgSrc()));
		nameTXT.setText(product.getName());
		idTXT.setText(idTXT.getText() + " " + String.valueOf(product.getId()));
		categoryTXT.setText(categoryTXT.getText() + " " + product.getCategory());
		colorTXT.setText(colorTXT.getText() + " " + product.getColor());
		if (product.getType().equals("Item")) {
			componentTXT.setText("");
			ProductCompTXT.setText("");
		} else
			componentTXT.setText("[" + product.getComponents() + "]");

		Image categoryImage = new Image(
				getClass().getResourceAsStream(product.getCategoryImgPath(product.getCategory())));

		categoryIMG.setImage(categoryImage);
		priceTXT.setText("$" + String.format("%.2f", product.getPrice()));

		// Maybe remove
		SaleIMG.setVisible(false);
		oldPriceTXT.setVisible(false);
	}
	/**
	 * setInstance(ProductCatalogController catalog)
	 * setter for ProductCatalogController
	 * @param catalog instance of the ProductCatalogController
	 */
	public static void setInstance(ProductCatalogController catalog) {
		if (catalogInstance == null)
			catalogInstance = catalog;
	}
	/**
	 * deleteInstance()
	 * deletes  ProductCatalogController instance
	 */
	public static void deleteInstance() {
		if (catalogInstance != null)
			catalogInstance = null;
	}

	/**
	 * isOnSale(Sale sale)
	 * if product is on sale we set the sale, add the new sale price, change sale picture
	 * @param sale the current sale details of the product 
	 */
	public void isOnSale(Sale sale) {
		this.sale = sale;
		SaleIMG.setVisible(true);
		oldPriceTXT.setVisible(true);
		Image productImage = new Image("/GuiAssests/OnSale.png");
		SaleIMG.setImage(productImage);
		priceTXT.setText("$" + String.format("%.2f", sale.getPrice()));
		oldPriceTXT.setText("$" + String.format("%.2f", product.getPrice()));
		product.setPrice(sale.getPrice());
		StringBuilder newSaleEnd = new StringBuilder();
		ArrayList<Integer> dateArray = Time.saleDateReminder(sale.getEndDate());
		newSaleEnd.append(dateArray.get(0) + "-Days ");
		newSaleEnd.append(dateArray.get(1) + "-Hours ");
		newSaleEnd.append(dateArray.get(2) + "-Minutes");
		saleEndsTimerTXT.setText(newSaleEnd.toString());
	}
	/**
	 * notOnSale()
	 * if product is no on sale we remove a few visual  (image, sale price) from the product 
	 */
	public void notOnSale() {
		picHBox.getChildren().remove(0);
		priceHBox.getChildren().remove(0);
		saleEndsTimerTXT.setVisible(false);

	}
	/**
	 * addToCart(ActionEvent event)
	 * Called when user clicks "add to cart" or "+" button, we add the item to cart or increase its quantity
	 * @param event
	 */
	@FXML
	void addToCart(ActionEvent event) {
		if (catalogInstance != null)
			catalogInstance.addToCart(product, true);
	}
	/**
	 * Sale getSale()
	 * @return sale the current sale of the product, if product is not on sale return null
	 */
	public Sale getSale()
	{
		return sale;
	}
	/**
	 * Image setImage(String path)
	 * returns the image for the product
	 * @param path to the PNG picture for product
	 */	
	private Image setImage(String path)
	{
		if(path.contains("GuiAssests"))
		{
			Image image = new Image(getClass().getResourceAsStream(product.getImgSrc()));
			return image;
		}
		else
		{
			File file = new File(path);
			Image image = new Image(file.toURI().toString());
			return image;
		}
	}
}
