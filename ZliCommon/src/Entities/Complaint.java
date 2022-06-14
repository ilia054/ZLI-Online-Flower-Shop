package Entities;

import java.io.Serializable;

import Enums.ComplaintStatus;
import Enums.Refund;
/**
*This class is a class that stores complaint info
*we use it to store the complaints that are passed from the DB to the Server and to the User
*it uses an Enum of type refund and complaintStatus in complaint to get the String for the select color
* @author Shay Zak
*/
public class Complaint implements Serializable {
	private static final long serialVersionUID = 1L;
	private ComplaintStatus complaintStatus;
	//private String userName,complaint,orderNumber,dateTime,refund;
	private String userName,complaint,dateTime;
	private float orderPrice;
	private int orderNumber;
	private Refund refund;
	private String EmployeSubmit;

	public Complaint(String userName, String complaint,String dateTime, int orderNumber,ComplaintStatus complaintStatus
			,Refund refund,float orderPrice,String EmployeSubmit ) {
		this.complaintStatus = complaintStatus;
		this.userName = userName;
		this.complaint = complaint;
		this.dateTime = dateTime;
		this.orderPrice = orderPrice;
		this.orderNumber = orderNumber;
		this.refund = refund;
		this.EmployeSubmit=EmployeSubmit;
	}
	public ComplaintStatus getComplaintStatus() {
		return complaintStatus;
	}
	public String getEmployeSubmit() {
		return EmployeSubmit;
	}
	public void setEmployeSubmit(String employeSubmit) {
		EmployeSubmit = employeSubmit;
	}
	public void setComplaintStatus(ComplaintStatus complaintStatus) {
		this.complaintStatus = complaintStatus;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getComplaint() {
		return complaint;
	}
	public void setComplaint(String complaint) {
		this.complaint = complaint;
	}

	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
	public float getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(float orderPrice) {
		this.orderPrice = orderPrice;
	}
	public Refund getRefund() {
		return refund;
	}
	public void setRefund(Refund refund) {
		this.refund = refund;
	}
	public int getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}


}


