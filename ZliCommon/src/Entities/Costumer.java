package Entities;

import java.io.Serializable;

import Enums.CostumerPremissions;

/**
 * This class is a class that stores data of customers that imported or will be saved in database 
 */
public class Costumer implements Serializable{
	private static final long serialVersionUID = 1L;
	private int costumerID;
	private String username;
	private String debt;
	private CostumerPremissions permissions;
	private String storeCredit;
	private String creditCard;
	private String expirationDate;
	private String cvv;
	
	public Costumer(int costumerID, String username, String debt, CostumerPremissions premissions, String storeCredit,
			String creditCard, String expirationDate, String cvv) {
		super();
		this.costumerID = costumerID;
		this.username = username;
		this.debt = debt;
		this.permissions = premissions;
		this.storeCredit = storeCredit;
		this.creditCard = creditCard;
		this.expirationDate = expirationDate;
		this.cvv = cvv;
	}

	public int getCostumerID() {
		return costumerID;
	}

	public String getUsername() {
		return username;
	}

	public String getDebt() {
		return debt;
	}

	public CostumerPremissions getPermissions() {
		return permissions;
	}

	public String getStoreCredit() {
		return storeCredit;
	}

	public String getCreditCard() {
		return creditCard;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCostumerID(int costumerID) {
		this.costumerID = costumerID;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setDebt(String debt) {
		this.debt = debt;
	}

	public void setPermissions(CostumerPremissions permissions) {
		this.permissions = permissions;
	}

	public void setStoreCredit(String storeCredit) {
		this.storeCredit = storeCredit;
	}

	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

}