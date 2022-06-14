package Entities;

public class CustomProduct extends AbstractProduct {

	private static final long serialVersionUID = 1L;
	
	private double minPrice;
	private double maxPrice;
	
	public CustomProduct(String name, String imgSrc, String color, String category, String components, int id,double minPrice,double maxPrice,float price) {
		super(name, imgSrc, color, category,"Custom Product",components, id,price);
		this.minPrice=minPrice;
		this.maxPrice=maxPrice;
	}

	public double getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(double minPrice) {
		this.minPrice = minPrice;
	}

	public double getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(double maxPrice) {
		this.maxPrice = maxPrice;
	}



	@Override
	public String toString() {
		
		return super.toString()+ "CustomProduct [minPrice=" + minPrice + ", maxPrice=" + maxPrice + ", price=" + super.getPrice() + ", Componenets="+super.getComponents()+"]";
	}
	
	
	
	


}
