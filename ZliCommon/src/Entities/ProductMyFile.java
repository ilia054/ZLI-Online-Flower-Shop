package Entities;

import java.io.Serializable;

import common.MyFile;

/**
 * This class is stores data of ProductMyFile that imported or will be saved in database 
 */
public class ProductMyFile implements Serializable  {
	private static final long serialVersionUID = 1L;
	private Product product;
	private MyFile myfile;
	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public MyFile getMyfile() {
		return myfile;
	}
	public void setMyfile(MyFile myfile) {
		this.myfile = myfile;
	}
}
