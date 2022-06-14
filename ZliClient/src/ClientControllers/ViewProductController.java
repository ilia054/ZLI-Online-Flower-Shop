package ClientControllers;


import java.util.ArrayList;

import Entities.Product;
import Entities.Sale;
import common.Time;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
/**
 * This class is View Prodcut Controller, it holds the logic for ViewProdcutController screen
 * @author Ilya
 */
public class ViewProductController {

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
	private Text oldPriceTXT;

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
	private HBox priceHBox;

	@FXML
	private HBox picHBox;

	@FXML
	private Label saleEndsTimerTXT;

	private Product product;

	private Sale sale;
	/**
	 * SetData sets the Data for the item
	 * Price,Image,Name,Quantity,etc..
	 * @param product data we would like to add
	 */
	public void setData(Product product) {
		this.product = product;
		Image productImage = new Image(getClass().getResourceAsStream(product.getImgSrc()));
		productIMG.setImage(productImage);
		nameTXT.setText(product.getName());
		idTXT.setText(idTXT.getText() + " " + String.valueOf(product.getId()));
		categoryTXT.setText(categoryTXT.getText() + " " + product.getCategory());
		colorTXT.setText(colorTXT.getText() + " " + product.getColor());

		if (product.getType().equals("Item")) {
			componentTXT.setText("");
			ProductCompTXT.setText("");
			;
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

	// If the item is On Sale we will change a few paramters, add the "Count Down"
	// for the sale
	/**
	 * isOnSale(Sale sale)
	 * This item sets the looks and logic for when the item is on sale
	 * we add "sale" picture, old and new price
	 * and some visual\numerical logic
	 * @param sale ,item sale from DB
	 */
	public void isOnSale(Sale sale) {
		this.sale = sale;
		SaleIMG.setVisible(true);
		oldPriceTXT.setVisible(true);
		Image productImage = new Image("/GuiAssests/OnSale.png");
		SaleIMG.setImage(productImage);
		priceTXT.setText("$" + String.format("%.2f", sale.getPrice()));
		oldPriceTXT.setText("$" + String.format("%.2f", product.getPrice()));
		StringBuilder newSaleEnd = new StringBuilder();
		ArrayList<Integer> dateArray = Time.saleDateReminder(sale.getEndDate());
		newSaleEnd.append(dateArray.get(0) + "-Days ");
		newSaleEnd.append(dateArray.get(1) + "-Hours ");
		newSaleEnd.append(dateArray.get(2) + "-Minutes");
		saleEndsTimerTXT.setText(newSaleEnd.toString());
	}

	// Item is not on sale , no need for Sale picture, only need one price, and
	// remove Sale end time TXT
	/**
	 * notOnSale()
	 * If item is not on sale we will remove a few componenets
	 * such as Old price, Sale picture and etc..
	 */
	public void notOnSale() {
		picHBox.getChildren().remove(0);
		priceHBox.getChildren().remove(0);
		saleEndsTimerTXT.setVisible(false);

	}
	/**
	 * getSale()
	 * a getter for Sale
	 * @return Sale the sale of the product if there is one else null
	 */
	public Sale getSale() {
		return sale;
	}

}
