package Entities;

import java.io.Serializable;

import Enums.OrderStatus;
import Enums.PaymentMethod;
import Enums.Store;
import Enums.SupplyMethod;
/**
*This class is a class that stores order info
*we use it to store the orders that are passed from the DB to the Server and to the User
*it uses an Enum of type Colors in order to get the String for the select color
* @author Omri Cohen
*/
public class Order implements Serializable, Comparable<Order> {
	private static final long serialVersionUID = 1L;
	private int orderNumber, costumerID;
	private float refund,price;
	private String greetingCard,components,estimatedDeliveryDate,orderDate,confirmationDate;
		
	private Store store;
	private SupplyMethod supplyMethod; 
	private OrderStatus orderStatus;
	private PaymentMethod paymentMethod;
		
	//only used when customer completes payment, that's why default value for orderstatus is not approved
	public Order(int orderNumber, int costumerID, float price, 
			String greetingCard, String components,
			Store store, String estimatedDeliveryDate, String orderDate, 
			SupplyMethod supplyMethod, PaymentMethod paymentMethod) {
		this.orderNumber = orderNumber;
		this.costumerID = costumerID;
		this.price = price;
		this.greetingCard = greetingCard;
		this.components = components;
		this.store = store;
		this.estimatedDeliveryDate = estimatedDeliveryDate;
		this.orderDate = orderDate;
		this.supplyMethod = supplyMethod;
		this.paymentMethod = paymentMethod;
		orderStatus = OrderStatus.NOT_APPROVED;
	}
	
	public Order(int orderNumber,float price, String greetingCard, String components,
			 Store store, String estimatedDeliveryDate, String orderDate, String confirmationDate,
			 OrderStatus orderStatus, int costumerID,float refund, SupplyMethod supplyMethod, PaymentMethod paymentMethod) {
		this.orderNumber = orderNumber;
		this.costumerID = costumerID;
		this.refund = refund;
		this.price = price;
		this.greetingCard = greetingCard;
		this.components = components;
		this.estimatedDeliveryDate = estimatedDeliveryDate;
		this.orderDate = orderDate;
		this.confirmationDate = confirmationDate;
		this.store = store;
		this.supplyMethod = supplyMethod;
		this.orderStatus = orderStatus;
		this.paymentMethod = paymentMethod;
	}

	public String getConfirmationDate() {
		return confirmationDate;
	}

	public void setConfirmationDate(String confirmationDate) {
		this.confirmationDate = confirmationDate;
	}

	public SupplyMethod getSupplyMethod() {
		return supplyMethod;
	}
	
	public void setSupplyMethod(SupplyMethod supplyMethod) {
		this.supplyMethod = supplyMethod;
	}
	
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}
	
	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getGreetingCard() {
		return greetingCard;
	}

	public void setGreetingCard(String greetingCard) {
		this.greetingCard = greetingCard;
	}


	public String getComponents() {
		return components;
	}

	public void setComponents(String components) {
		this.components = components;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}


	public String getEstimatedDeliveryDate() {
		return estimatedDeliveryDate;
	}

	public void setEstimatedDeliveryDate(String estimatedDeliveryDate) {
		this.estimatedDeliveryDate = estimatedDeliveryDate;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	
	public int getCostumerID() {
		return costumerID;
	}

	public void setCostumerID(int costumerID) {
		this.costumerID = costumerID;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(orderNumber);
	}

	public float getRefund() {
		return refund;
	}

	public void setRefund(float refund) {
		this.refund = refund;
	}
	
	public String getMonthOfOrderDate() {
		return orderDate.substring(5, 7);
	}
	
	public String getYearOfOrderDate() {
		return orderDate.substring(0, 4);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		return orderNumber == other.orderNumber;
	}

	@Override
	public int compareTo(Order o) {
		if (this.equals(o)) return 0;
		else if (this.orderNumber > o.orderNumber)
			return 1;
		return -1;
		
	}

}
