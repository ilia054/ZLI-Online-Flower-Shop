package Entities;

/**
 * This class is stores data of Product that imported or will be saved in database 
 */
public class Product extends AbstractProduct  {

	private static final long serialVersionUID = 1L;


	public Product(String name, String imgSrc, String color, String category, String type,String components, float price, int id) {
		super(name,imgSrc,color,category,type,components,id,price);
	}
	
	//constructor for discount 
	public Product(String name, String imgSrc, float price) { 
		super(name,imgSrc,price);
	}
	
	//constructor for and Marketing employee editing price
		public Product(String name, String imgSrc, float price,int id) { 
			super(name,imgSrc,price,id);
		}

}
