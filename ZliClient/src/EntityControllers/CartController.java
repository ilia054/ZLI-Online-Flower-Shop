package EntityControllers;

import java.util.ArrayList;

import ClientControllers.ProductCatalogController;
import Entities.AbstractProduct;
import Entities.Costumer;
import Entities.CustomProduct;
import Entities.Delivery;
import Enums.Store;
import Enums.SupplyMethod;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
*The class CartController holds the customers Cart at any given time
*we can add AbstractProducts to it, remove all of them,and pass in a new cart value
* @author Biran Fridman
*/

public class CartController {
	private static ObservableList<AbstractProduct> myCart=FXCollections.observableArrayList();
	private float orderTotalPrice=-1;
	private int nextProductID=-1; //indicates the id for next custom product.
	private int nextOrderID=-1;
	private Delivery delivery;
	private SupplyMethod supplyMethod;
	private Store store;
	private String greetingCard;
	private boolean firstOrder;
	private float discountAmount;
	private String pickUpDate;
	private Costumer costumer;
	private ProductCatalogController instance;

	

	public float getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(float discountAmount) {
		this.discountAmount = discountAmount;
	}
	public boolean isFirstOrder() {
		return firstOrder;
	}
	public void setFirstOrder(boolean firstOrder) {
		this.firstOrder = firstOrder;
	}
	public String getGreetingCard() {
		return greetingCard;
	}
	public void setGreetingCard(String greetingCard) {
		this.greetingCard = greetingCard;
	}

	public Costumer getCostumer() {
		return costumer;
	}
	public ProductCatalogController getInstance() {
		return instance;
	}
	
	public void setInstance(ProductCatalogController instance) {
		this.instance = instance;
	}

	public void setCostumer(Costumer costumer) {
		this.costumer = costumer;
	}

	public String getPickUpDate() {
		return pickUpDate;
	}

	public void setPickUpDate(String pickUpDate) {
		this.pickUpDate = pickUpDate;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public SupplyMethod getSupplyMethod() {
		return supplyMethod;
	}

	public void setSupplyMethod(SupplyMethod supplyMethod) {
		this.supplyMethod = supplyMethod;
	}

	public Delivery getDelivery() {
		return delivery;
	}

	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}

	public int getNextOrderID() {
		return nextOrderID;
	}

	public void setNextOrderID(int nextOrderID) {
		this.nextOrderID = nextOrderID;
	}

	public float getOrderTotalPrice() {
		return orderTotalPrice;
	}

	public void setOrderTotalPrice(float orderTotalPrice) {
		this.orderTotalPrice = orderTotalPrice;
	}

	public int getNextProductID() {
		return nextProductID;
	}

	public void setNextProductID(int nextProductID) {
		this.nextProductID = nextProductID;
	}

	public ObservableList<AbstractProduct> getCart() {
		return myCart;
	}

	public void setCart(ObservableList<AbstractProduct> cart) {
		myCart=cart;
	}
	
	public void addToCart(AbstractProduct abstractProduct)
	{
		myCart.add(abstractProduct);
	}
	
	public static void emptyCart()
	{
		myCart.clear();
	}
	/**
	 * toString()
	 * @return a string representing a receipt for the order 
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("\t\tTHANK YOU FOR PURCHASING FROM ZLI FLOWER SHOP G11\n\n"
				+ "..................................Order Summary....................................................\n\n");
		if(greetingCard!=null)
			str.append(greetingCard+"\n\n");
		ArrayList<Integer> alreadyInString= new ArrayList<>();
		int i = 1;
		for(AbstractProduct product : myCart)
		{
			if(alreadyInString.contains(product.getId())) //check if we already have this item in the string
				continue;
			alreadyInString.add(product.getId());	
			int amount = AmountInCart(product);
			String firstLine =product.getName();
			while(firstLine.length() < 35)
				firstLine += ".";
			str.append(firstLine);
			str.append("x" + amount + " Total price: " + String.format("%.02f", product.getPrice() * amount) + "$");
			str.append("\nDetails: id: " + product.getId() + ", Type: " + product.getType() 
			+ ", Category: " + product.getCategory() + ", Color: " + product.getColor()  
			+ ", Price Per Unit: " + product.getPrice() +  "$\n");
			if(product.getType().equals("Item"))
			{
				str.append("\n");
				continue;
			}
			str.append("Components: " + product.getComponents() +"\n\n");
		}
		for(i = 0; i < 100 ; i++)
			str.append(".");
		if(firstOrder)
			str.append("\n\t\t  Discount for first order: " + discountAmount + "$");
		str.append("\n\t\t\t\t  Total Price: " + orderTotalPrice + "$");
		return str.toString();
	}

	/**
	 * AmountInCart(AbstractProduct product)
	 * @param product
	 * @return the amount of times AbstractProduct is in cart
	 */
	private int AmountInCart(AbstractProduct product) {
		int cnt = 0;
		for(AbstractProduct element : myCart)
		{
			if(product.getId() == element.getId())
				cnt++;
		}
		return cnt;
	}
	/**
	 * ClearDelivery()
	 *  resets the delivery releted variables
	 */
	public void ClearDelivery() 
	{
		delivery = null;
		supplyMethod = null;
		store = null;
		pickUpDate = null;
	}

	/**
	 * ClearWhenOrderEnds()
	 *  resets the all order related variables
	 */
	public void ClearWhenOrderEnds()
	{
		ClearDelivery();
		orderTotalPrice=-1;
		nextProductID=-1; 
		nextOrderID=-1;
		instance.clearBeforeExit(true);
		System.gc();
	}
	/**
	 * getComponents()
	 *  bulds a string of the components in the cart.
	 *	the components include:products and items names(if its a product we just enter its name and not its components)
	 *	if we have a custom product, we enter its components as above
	 *@return represantaion of the components in the cart.
	 */
	public String getComponents() //returns the components of the cart.
								  //the components include:products and items names(if its a product we just enter its name and not its components)
								  //if we have a custom product, we enter its components as above
	{
		StringBuilder str = new StringBuilder();
		boolean flag = false;
		for(AbstractProduct p : myCart)
		{
			if(flag)
				str.append(",");
			else flag = true;
			if(p instanceof CustomProduct)
			{
				str.append(p.getComponents());
			}
			else str.append(p.getName());
		}
		return str.toString();
	}
}

