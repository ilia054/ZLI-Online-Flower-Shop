package ClientControllers;

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

/**
 * This is the Item controller, it holds all of the logic for Item screen
 * @author Ilya
 */
public class ItemController {

	public static ItemCatalogController catalogInstance;

	@FXML
	private ImageView productIMG;

	@FXML
	private HBox picHBox;

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
	private ImageView SaleIMG;

	@FXML
	private ImageView categoryIMG;
	
	@FXML
    private Label ProductCompTXT;


	@FXML
	private Label saleEndsTimerTXT;

	@FXML
	private Button addToCartBTN;

	private Product item;
	
	private Sale sale;
	/**
	 * SetData sets the Data for the item
	 * Price,Image,Name,Quantity,etc..
	 * @param item, item data we would like to add
	 */
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
		if (item.getType().equals("Item")) {
			componentTXT.setText("");
			ProductCompTXT.setText("");
			;
		} else
			componentTXT.setText("[" + item.getComponents() + "]");

	}
	/**
	 * setInstance(ItemCatalogController catalog)
	 * This is a setter for ItemCatalogController instnace so we can use the previous screen logic
	 * @param catalog, the controller of the selected item
	 */
	public static void setInstance(ItemCatalogController catalog) {
		if (catalogInstance == null)
			catalogInstance = catalog;
	}
	/**
	 * deleteInstance()
	 * deltes the ItemCatalogController instance
	 */
	public static void deleteInstance() {
		if (catalogInstance != null)
			catalogInstance = null;
	}
	/**
	 * deleteInstance()
	 * Adds the item to the cart
	 * @param event, when add to cart is clicked
	 */
	@FXML
	void addToCart(ActionEvent event) {
		if (catalogInstance != null) {
			catalogInstance.addToCart(item);
		}
	}
	
	/**
	 * isOnSale(Sale sale)
	 * This item sets the looks and logic for when the item is on sale
	 * we add "sale" picture, old and new price
	 * and some visual\numerical logic
	 * @param sale ,item sale from DB
	 */
		public void isOnSale(Sale sale)
		{
			this.sale=sale;
			SaleIMG.setVisible(true);
			Image productImage = new Image("/GuiAssests/OnSale.png");
			SaleIMG.setImage(productImage);
			item.setPrice(sale.getPrice());
			StringBuilder newSaleEnd=new StringBuilder();
			ArrayList<Integer> dateArray=Time.saleDateReminder(sale.getEndDate());
			newSaleEnd.append(dateArray.get(0)+"-Days ");
			newSaleEnd.append(dateArray.get(1)+"-Hours ");
			newSaleEnd.append(dateArray.get(2)+"-Minutes");
			saleEndsTimerTXT.setText(newSaleEnd.toString());	
		}
		/**
		 * notOnSale()
		 * If item is not on sale we will remove a few componenets
		 * such as Old price, Sale picture and etc..
		 */
		public void notOnSale()
		{
			picHBox.getChildren().remove(0);
			saleEndsTimerTXT.setVisible(false);
			
		}
		/**
		 * getSale()
		 * @return sale the sale if there is one, null otherwise
		 */		
		public Sale getSale()
		{
			return sale;
		}
}
