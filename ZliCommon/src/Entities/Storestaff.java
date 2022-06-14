package Entities;

import java.io.Serializable;

import Enums.StorestaffPermissions;

/**
 * This class is stores data of Storestaff that imported or will be saved in database 
 */
public class Storestaff implements Serializable{
	private static final long serialVersionUID = 1L;
	private int storeEmployeeID;
	private String username;
	private StorestaffPermissions permissions;
	
	public Storestaff(int storeEmployeeID, String username, StorestaffPermissions permissions) {
		this.storeEmployeeID = storeEmployeeID;
		this.username = username;
		this.permissions = permissions;
	}

	public int getStoreEmployeeID() {
		return storeEmployeeID;
	}

	public String getUsername() {
		return username;
	}

	public StorestaffPermissions getPermissions() {
		return permissions;
	}

	public void setStoreEmployeeID(int storeEmployeeID) {
		this.storeEmployeeID = storeEmployeeID;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPermissions(StorestaffPermissions permissions) {
		this.permissions = permissions;
	}
}
