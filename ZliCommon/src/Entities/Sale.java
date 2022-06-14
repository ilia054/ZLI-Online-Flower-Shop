package Entities;

import java.io.Serializable;

/**
 * This class is stores data of Sale that imported or will be saved in database 
 */
public class Sale implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int id;
	private int productID;
	private String startDate;
	private String endDate;
	private Float price;
	
	
	public Sale(int id, int productID, String startDate, String endDate, Float price) {
		this.id = id;
		this.productID = productID;
		this.startDate = startDate;
		this.endDate = endDate;
		this.price = price;
	}
	
	public Sale(int productID, String startDate, String endDate, Float price) {
		this.productID = productID;
		this.startDate = startDate;
		this.endDate = endDate;
		this.price = price;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getProductID() {
		return productID;
	}
	public void setProductID(int productID) {
		this.productID = productID;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	 
	@Override
	public String toString() {
		return "Added a new sale successfully for Product ID " + productID + "\nFrom " + startDate + " To " + 
				endDate;
	}
}
