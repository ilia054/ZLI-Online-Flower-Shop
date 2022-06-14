package Entities;

import java.io.Serializable;
import java.util.Objects;
/**
 * This class is stores data of MonthlyIncomesReport that imported or will be saved in database 
 */
public class MonthlyIncomesReport implements Serializable{
	private static final long serialVersionUID = 1L;
	private float ordersIncomes, deliveriesIncomes, refunds;
	String store, date;

	public MonthlyIncomesReport(float ordersIncomes, float deliveriesIncomes, float refunds, String store, String date) {
		this.ordersIncomes = ordersIncomes;
		this.deliveriesIncomes = deliveriesIncomes;
		this.refunds = refunds;
		this.store = store;
		this.date = date;
	}
	
	public float getTotalIncomes() {
		return ordersIncomes+deliveriesIncomes-refunds;
	}
	
	public float getOrdersIncomes() {
		return ordersIncomes;
	}

	public float getDeliveriesIncomes() {
		return deliveriesIncomes;
	}

	public float getRefunds() {
		return refunds;
	}

	public void setOrdersIncomes(float ordersIncomes) {
		this.ordersIncomes = ordersIncomes;
	}

	public void setDeliveriesIncomes(float deliveriesIncomes) {
		this.deliveriesIncomes = deliveriesIncomes;
	}

	public void setRefunds(float refunds) {
		this.refunds = refunds;
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

	@Override
	public int hashCode() {
		return Objects.hash(date, deliveriesIncomes, ordersIncomes, refunds, store);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MonthlyIncomesReport other = (MonthlyIncomesReport) obj;
		return Objects.equals(date, other.date)
				&& Float.floatToIntBits(deliveriesIncomes) == Float.floatToIntBits(other.deliveriesIncomes)
				&& Float.floatToIntBits(ordersIncomes) == Float.floatToIntBits(other.ordersIncomes)
				&& Float.floatToIntBits(refunds) == Float.floatToIntBits(other.refunds)
				&& Objects.equals(store, other.store);
	}
	
	

}
