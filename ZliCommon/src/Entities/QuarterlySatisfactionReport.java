package Entities;

import java.io.Serializable;

/**
 * This class is stores data of QuarterlySatisfactionReport that imported or will be saved in database 
 */
public class QuarterlySatisfactionReport implements Serializable{
	private static final long serialVersionUID = 1L;
	private int orders, complaints;
	private float satisfactionRate;
	String store, date;
	public QuarterlySatisfactionReport(int orders, int complaints, float satisfactionRate, String store, String date) {
		super();
		this.orders = orders;
		this.complaints = complaints;
		this.satisfactionRate = satisfactionRate;
		this.store = store;
		this.date = date;
	}
	public int getOrders() {
		return orders;
	}
	public void setOrders(int orders) {
		this.orders = orders;
	}
	public int getComplaints() {
		return complaints;
	}
	public void setComplaints(int complaints) {
		this.complaints = complaints;
	}
	public float getSatisfactionRate() {
		return satisfactionRate;
	}
	public void setSatisfactionRate(float satisfactionRate) {
		this.satisfactionRate = satisfactionRate;
	}
	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	

}
