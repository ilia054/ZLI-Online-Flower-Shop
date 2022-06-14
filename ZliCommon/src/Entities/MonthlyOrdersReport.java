package Entities;

import java.io.Serializable;
import java.util.HashMap;

/**
 * This class is stores data of MonthlyOrdersReport that imported or will be saved in database 
 */
public class MonthlyOrdersReport implements Serializable{
	private static final long serialVersionUID = 1L;
	private HashMap<String, Integer> itemsAndAmountHashMap = new HashMap<String, Integer>();
	private int totalOrdersCount = 0, numberOfCanceledOrders;
	private float ordersPerDayAvg = 0;
	
	String store, date, mostWantedItemName;
	
	public MonthlyOrdersReport(HashMap<String, Integer> itemsAndAmountHashMap, int totalOrdersCount, int numberOfCanceledOrders, float ordersPerDayAvg, String store, String date, String mostWantedItemName) {
		this.itemsAndAmountHashMap = itemsAndAmountHashMap;
		this.totalOrdersCount = totalOrdersCount;
		this.numberOfCanceledOrders = numberOfCanceledOrders;
		this.ordersPerDayAvg = ordersPerDayAvg;
		this.store = store;
		this.date = date;
		this.mostWantedItemName = mostWantedItemName;
	}
	
	public int getNumberOfCanceledOrders() {
		return numberOfCanceledOrders;
	}
	public float getOrdersPerDayAvg() {
		return ordersPerDayAvg;
	}
	public void setNumberOfCanceledOrders(int numberOfCanceledOrders) {
		this.numberOfCanceledOrders = numberOfCanceledOrders;
	}
	public void setOrdersPerDayAvg(float ordersPerDayAvg) {
		this.ordersPerDayAvg = ordersPerDayAvg;
	}
	public void setMostWantedItemName(String mostWantedItemName) {
		this.mostWantedItemName = mostWantedItemName;
	}

	public HashMap<String, Integer> getItemsAndAmountHashMap() {
		return itemsAndAmountHashMap;
	}
	public void setItemsAndAmountHashMap(HashMap<String, Integer> itemsAndAmountHashMap) {
		this.itemsAndAmountHashMap = itemsAndAmountHashMap;
	}
	
	public String getStore() {
		return store;
	}
	public String getDate() {
		return date;
	}
	public void setStore(String store) {
		this.store = store;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getTotalOrdersCount() {
		return totalOrdersCount;
	}
	public void setTotalOrdersCount(int count) {
		totalOrdersCount = count;
	}
	public String getMostWantedItemName() {
		return mostWantedItemName;
	}
}
