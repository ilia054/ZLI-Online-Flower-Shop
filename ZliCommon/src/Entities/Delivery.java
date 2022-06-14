package Entities;

import java.io.Serializable;

/**
 * This class is stores data of Delivery that imported or will be saved in database 
 */
public class Delivery implements Serializable{
	private static final long serialVersionUID = 1L;
		public static final float deliveryPrice=10;
		private int orderNumber;
		private String address;
		private String receiverName;
		private String receiverPhoneNumber;
		private String confirmationDate;
		private String estimatedDeliveryDate;
		private String actualDeliveryDate;
		private boolean immediateSupply;
	
	//for Standard-Delivery
	public Delivery(int orderNumber, String address, String receiverName, String receiverPhoneNumber,
			String estimatedDeliveryDate) {
		this.orderNumber = orderNumber;
		this.address = address;
		this.receiverName = receiverName;
		this.receiverPhoneNumber = receiverPhoneNumber;
		this.estimatedDeliveryDate = estimatedDeliveryDate;
		immediateSupply = false;
	}
	
	//For Pick-Up option *Depracaeted(Currently not used)
	public Delivery(int orderNumber, String estimatedDeliveryDate) {
		this.orderNumber = orderNumber;
		this.estimatedDeliveryDate = estimatedDeliveryDate;
		immediateSupply = false;
	}
	
	//For Priority Delivery
	public Delivery(int orderNumber, String address, String receiverName, String receiverPhoneNumber) {
		this.orderNumber = orderNumber;
		this.address = address;
		this.receiverName = receiverName;
		this.receiverPhoneNumber = receiverPhoneNumber;
		immediateSupply = true;
	}


	public int getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getReceiverPhoneNumber() {
		return receiverPhoneNumber;
	}
	public void setReceiverPhoneNumber(String receiverPhoneNumber) {
		this.receiverPhoneNumber = receiverPhoneNumber;
	}
	public String getConfirmationDate() {
		return confirmationDate;
	}
	public void setConfirmationDate(String confirmationDate) {
		this.confirmationDate = confirmationDate;
	}
	public String getEstimatedDeliveryDate() {
		return estimatedDeliveryDate;
	}
	public void setEstimatedDeliveryDate(String estimatedDeliveryDate) {
		this.estimatedDeliveryDate = estimatedDeliveryDate;
	}
	public String getActualDeliveryDate() {
		return actualDeliveryDate;
	}
	public void setActualDeliveryDate(String actualDeliveryDate) {
		this.actualDeliveryDate = actualDeliveryDate;
	}
	public boolean getImmediateSupply() {
		return immediateSupply;
	}
	public void setImmediateSupply(boolean immediateSupply) {
		this.immediateSupply = immediateSupply;
	}
	@Override
	public String toString() {
		return "Delivery [orderNumber=" + orderNumber + ", address=" + address + ", receiverName=" + receiverName
				+ ", receiverPhoneNumber=" + receiverPhoneNumber + ", confirmationDate=" + confirmationDate
				+ ", estimatedDeliveryDate=" + estimatedDeliveryDate + ", actualDeliveryDate=" + actualDeliveryDate
				+ ", immediateSupply=" + immediateSupply + "]";
	}
}
