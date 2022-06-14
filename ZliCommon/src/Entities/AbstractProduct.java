package Entities;

import java.io.Serializable;

/**
 * This class is stores data of AbstractProduct that imported or will be saved in database 
 */
public abstract class AbstractProduct implements Serializable  {
	private static final long serialVersionUID = 1L;
	private String name;
	private String imgSrc;
	private String color;
	private String category;
	private String type;
	private String components;
	private int id;
	private float price;
	public AbstractProduct(String name, String imgSrc, String color, String category, String type,String components,int id,float price) {
		this.name = name;
		this.imgSrc = imgSrc;
		this.color = color;
		this.category = category;
		this.type=type;
		this.components=components;
		this.id = id;
		this.price = price;
	}
	//constructor for discount 
	public AbstractProduct(String name, String imgSrc,float price) {
		this.name = name;
		this.imgSrc = imgSrc;
		this.price = price;
	}
	//constructor for and Marketing employee editing price
	public AbstractProduct(String name, String imgSrc,float price,int id) {
		this.name = name;
		this.imgSrc = imgSrc;
		this.price = price;
		this.id = id;
	}
	
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getComponents() {
		return components;
	}
	public void setComponents(String components) {
		this.components = components;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImgSrc() {
		return imgSrc;
	}
	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getCategoryImgPath(String Category) {
		switch (Category) {
		case "Boquet":
			return "/GuiAssests/Boquet.png";
		case "BlossomingFlower":
			return "/GuiAssests/BlossomingFlower.png";
		case "FlowerArrangment":
			return "/GuiAssests/FlowerArrangment.png";
		default:
			return "/GuiAssests/userIcon.png";
		}
	}
	@Override
	public String toString() {
		return "AbstractProduct [name=" + name + ", imgSrc=" + imgSrc + ", color=" + color + ", category=" + category
				+ ", id=" + id + "]";
	}
	
	
	
}
