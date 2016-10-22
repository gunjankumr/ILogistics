package com.compass.utils;

import java.util.ArrayList;

import com.compass.model.CustomerModel;
import com.compass.model.RouteJob;
import com.compass.model.ProblemSet;

public class ValueHolder {

	private static ValueHolder singletonObject;
		
	private ValueHolder(){
		  //	 Optional Code
	}
	
	public static synchronized ValueHolder getSingletonObject()
	{
	    if (singletonObject == null){
	    	singletonObject = new ValueHolder();
	    }
	    return singletonObject;
	}
	
	public Object clone()throws CloneNotSupportedException
	{
	    throw new CloneNotSupportedException(); 
	}
	
	public String username;
	public String routingCode;
	public String deliverySeq;
	public String deliveryDate;
	public String deliverySeqNew;
	public String deliveryDateNew;
	public String companyCode;
	public String assignDate;
	public ArrayList<CustomerModel> customerList = new ArrayList<CustomerModel>();
	public ArrayList<RouteJob> routeJobList = new ArrayList<RouteJob>();
	public ArrayList<ProblemSet> problemSetList = new ArrayList<ProblemSet>();
	public int customerSelected;
	public int invoiceSelected;
	public boolean childSelected;
	public String menuID;
	public String checkInTime = "";
	public String checkOutTime = "";
	public String Latitude = "";
	public String Longitude = "";
	public String isFromProductProblemTable;
	public String isProblemExist = "N";
	
	public String getUsername() {
		if(username != null)	
			return username;
		else
			return "jbt04";
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getIsFromProductProblemTable() {
		// TODO Auto-generated method stub
		if(isFromProductProblemTable != null)	
			return isFromProductProblemTable;
		else
			return "False";
	}
	
	public void setIsFromProductProblemTable(String strVal) {
		// TODO Auto-generated method stub
		this.isFromProductProblemTable = strVal;
	}
	
	
	
	public String getRoutingCode() {
		return routingCode;
	}
	public void setRoutingCode(String routingCode) {
		this.routingCode = routingCode;
	}
	public String getDeliverySeq() {
		return deliverySeq;
	}
	public void setDeliverySeq(String deliverySeq) {
		this.deliverySeq = deliverySeq;
	}
	public String getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getDeliverySeqNew() {
		return deliverySeqNew;
	}
	public void setDeliverySeqNew(String deliverySeqNew) {
		this.deliverySeqNew = deliverySeqNew;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getIsProblemExist() {
		return isProblemExist;
	}
	public void setIsProblemExist(String status) {
		this.isProblemExist = status;
	}
	
	public String getDeliveryDateNew() {
		return deliveryDateNew;
	}
	public void setDeliveryDateNew(String deliveryDateNew) {
		this.deliveryDateNew = deliveryDateNew;
	}
	

	public String getAssignDate() {
		return assignDate;
	}
	public void setAssignDate(String assignDate) {
		this.assignDate = assignDate;
	}
	public ArrayList<CustomerModel> getCustomerList() {
		return customerList;
	}
	public void setCustomerList(ArrayList<CustomerModel> customerList) {
		this.customerList = customerList;
	}
	public ArrayList<ProblemSet> getProblemList() {
		return problemSetList;
	}
	public void setProblemList(ArrayList<ProblemSet> serviceResponseList) {
		this.problemSetList = serviceResponseList;
	}
	public ArrayList<RouteJob> getRouteJobList() {
		return routeJobList;
	}
	public void setRouteJobList(ArrayList<RouteJob> routeJobList) {
		this.routeJobList = routeJobList;
	}
	public int getCustomerSelected() {
		return customerSelected;
	}
	public void setCustomerSelected(int customerSelected) {
		this.customerSelected = customerSelected;
	}
	public int getInvoiceSelected() {
		return invoiceSelected;
	}
	public void setInvoiceSelected(int invoiceSelected) {
		this.invoiceSelected = invoiceSelected;
	}
	public boolean isChildSelected() {
		return childSelected;
	}
	public void setChildSelected(boolean childSelected) {
		this.childSelected = childSelected;
	}
	public String getMenuID() {
		return menuID;
	}
	public void setMenuID(String menuID) {
		this.menuID = menuID;
	}
	
	public String getCheckInTime() {
		return checkInTime;
	}
	public void setCheckInTime(String checkInTime) {
		this.checkInTime = checkInTime;
	}
	
	public String getCheckOutTime() {
		return checkOutTime;
	}
	public void setCheckOutTime(String checkOutTime) {
		this.checkOutTime = checkOutTime;
	}
	
	public String getLatitude() {
		return Latitude;
	}

	public void setLatitude(String latitude) {
		Latitude = latitude;
	}
	public String getLongitude() {
		return Longitude;
	}

	public void setLongitude(String longitude) {
		Longitude = longitude;
	}
	
	
	public void clearAll() {
		this.routingCode =  "";
		this.customerList = new ArrayList<CustomerModel>();
		this.customerSelected = 0;
		this.invoiceSelected = 0; 
	}
	
	public void clearLocation() {
		this.checkInTime = "";
		this.checkOutTime = "";
		this.Latitude = "";
		this.Longitude = "";
	}

	
}
