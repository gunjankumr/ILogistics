package com.compass.model;

import java.util.ArrayList;

public class RoutingDetailModel {
	
	public String ROUTE_CODE;
	public ArrayList<CustomerModel> customerList = new ArrayList<CustomerModel>();
	
	public RoutingDetailModel(ArrayList<CustomerModel> customerList) {
		 super();
		 this.customerList 	= customerList;
	 }
	

	public ArrayList<CustomerModel> getCustomerList() {
		return customerList;
	}

	public void setCustomerList(ArrayList<CustomerModel> customerList) {
		this.customerList = customerList;
	}
	
}