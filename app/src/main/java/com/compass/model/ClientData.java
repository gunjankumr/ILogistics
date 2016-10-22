package com.compass.model;

import java.util.ArrayList;

public class ClientData {
	private String custID;
	private String custCode;
	private String custName;
	private String branchCode;
	private String branchName;
	private String routeCode;
	private String deliverySeq;
	private String totalInvoice;
	
	private ArrayList<InvoiceData> arrListInvoice;

	/**
	 * @return the custID
	 */
	public String getCustID() {
		return custID;
	}

	/**
	 * @param custID the custID to set
	 */
	public void setCustID(String custID) {
		this.custID = custID;
	}

	/**
	 * @return the custCode
	 */
	public String getCustCode() {
		return custCode;
	}

	/**
	 * @param custCode the custCode to set
	 */
	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}

	/**
	 * @return the custName
	 */
	public String getCustName() {
		return custName;
	}

	/**
	 * @param custName the custName to set
	 */
	public void setCustName(String custName) {
		this.custName = custName;
	}

	/**
	 * @return the branchCode
	 */
	public String getBranchCode() {
		return branchCode;
	}

	/**
	 * @param branchCode the branchCode to set
	 */
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	/**
	 * @return the branchName
	 */
	public String getBranchName() {
		return branchName;
	}

	/**
	 * @param branchName the branchName to set
	 */
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	/**
	 * @return the routeCode
	 */
	public String getRouteCode() {
		return routeCode;
	}

	/**
	 * @param routeCode the routeCode to set
	 */
	public void setRouteCode(String routeCode) {
		this.routeCode = routeCode;
	}

	/**
	 * @return the deliverySeq
	 */
	public String getDeliverySeq() {
		return deliverySeq;
	}

	/**
	 * @param deliverySeq the deliverySeq to set
	 */
	public void setDeliverySeq(String deliverySeq) {
		this.deliverySeq = deliverySeq;
	}

	/**
	 * @return the totalInvoice
	 */
	public String getTotalInvoice() {
		return totalInvoice;
	}

	/**
	 * @param totalInvoice the totalInvoice to set
	 */
	public void setTotalInvoice(String totalInvoice) {
		this.totalInvoice = totalInvoice;
	}

	/**
	 * @return the arrListInvoice
	 */
	public ArrayList<InvoiceData> getArrListInvoice() {
		return arrListInvoice;
	}

	/**
	 * @param arrListInvoice the arrListInvoice to set
	 */
	public void setArrListInvoice(ArrayList<InvoiceData> arrListInvoice) {
		this.arrListInvoice = arrListInvoice;
	}
}
