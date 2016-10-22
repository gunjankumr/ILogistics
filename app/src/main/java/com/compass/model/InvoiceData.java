package com.compass.model;

import java.util.ArrayList;

public class InvoiceData {
	private String customerID;
	private String InvoiceID;
	private String company;
	private String TransactionType;
	private String invoiceBook;
	private String InvoiceNo;
	private String invoiceBookNo;
	private String deliverySeq;
	private String invoiceScan;
	private String deliverStatus;
	private String cashReceived;
	private String staffCode;
	private String docDate;
	private String paymentType;
	private String branchCode;
	private String invoiceAmount;
	private String deliveryTo;
	private String expectedTime;
	
	private ArrayList<ProductData> arrListProduct;

	/**
	 * @return the customerID
	 */
	public String getCustomerID() {
		return customerID;
	}
	/**
	 * @param customerID the customerID to set
	 */
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	/**
	 * @return the invoiceID
	 */
	public String getInvoiceID() {
		return InvoiceID;
	}
	/**
	 * @param invoiceID the invoiceID to set
	 */
	public void setInvoiceID(String invoiceID) {
		InvoiceID = invoiceID;
	}
	/**
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}
	/**
	 * @param company the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}
	/**
	 * @return the transactionType
	 */
	public String getTransactionType() {
		return TransactionType;
	}
	/**
	 * @param transactionType the transactionType to set
	 */
	public void setTransactionType(String transactionType) {
		TransactionType = transactionType;
	}
	/**
	 * @return the invoiceBook
	 */
	public String getInvoiceBook() {
		return invoiceBook;
	}
	/**
	 * @param invoiceBook the invoiceBook to set
	 */
	public void setInvoiceBook(String invoiceBook) {
		this.invoiceBook = invoiceBook;
	}
	/**
	 * @return the invoiceNo
	 */
	public String getInvoiceNo() {
		return InvoiceNo;
	}
	/**
	 * @param invoiceNo the invoiceNo to set
	 */
	public void setInvoiceNo(String invoiceNo) {
		InvoiceNo = invoiceNo;
	}
	/**
	 * @return the invoiceBookNo
	 */
	public String getInvoiceBookNo() {
		return invoiceBookNo;
	}
	/**
	 * @param invoiceBookNo the invoiceBookNo to set
	 */
	public void setInvoiceBookNo(String invoiceBookNo) {
		this.invoiceBookNo = invoiceBookNo;
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
	 * @return the invoiceScan
	 */
	public String getInvoiceScan() {
		return invoiceScan;
	}
	/**
	 * @param invoiceScan the invoiceScan to set
	 */
	public void setInvoiceScan(String invoiceScan) {
		this.invoiceScan = invoiceScan;
	}
	/**
	 * @return the deliverStatus
	 */
	public String getDeliverStatus() {
		return deliverStatus;
	}
	/**
	 * @param deliverStatus the deliverStatus to set
	 */
	public void setDeliverStatus(String deliverStatus) {
		this.deliverStatus = deliverStatus;
	}
	/**
	 * @return the cashReceived
	 */
	public String getCashReceived() {
		return cashReceived;
	}
	/**
	 * @param cashReceived the cashReceived to set
	 */
	public void setCashReceived(String cashReceived) {
		this.cashReceived = cashReceived;
	}
	/**
	 * @return the staffCode
	 */
	public String getStaffCode() {
		return staffCode;
	}
	/**
	 * @param staffCode the staffCode to set
	 */
	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}
	/**
	 * @return the docDate
	 */
	public String getDocDate() {
		return docDate;
	}
	/**
	 * @param docDate the docDate to set
	 */
	public void setDocDate(String docDate) {
		this.docDate = docDate;
	}
	/**
	 * @return the paymentType
	 */
	public String getPaymentType() {
		return paymentType;
	}
	/**
	 * @param paymentType the paymentType to set
	 */
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
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
	 * @return the invoiceAmount
	 */
	public String getInvoiceAmount() {
		return invoiceAmount;
	}
	/**
	 * @param invoiceAmount the invoiceAmount to set
	 */
	public void setInvoiceAmount(String invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	/**
	 * @return the deliveryTo
	 */
	public String getDeliveryTo() {
		return deliveryTo;
	}
	/**
	 * @param deliveryTo the deliveryTo to set
	 */
	public void setDeliveryTo(String deliveryTo) {
		this.deliveryTo = deliveryTo;
	}
	/**
	 * @return the expectedTime
	 */
	public String getExpectedTime() {
		return expectedTime;
	}
	/**
	 * @param expectedTime the expectedTime to set
	 */
	public void setExpectedTime(String expectedTime) {
		this.expectedTime = expectedTime;
	}
	/**
	 * @return the arrListProduct
	 */
	public ArrayList<ProductData> getArrListProduct() {
		return arrListProduct;
	}
	/**
	 * @param arrListProduct the arrListProduct to set
	 */
	public void setArrListProduct(ArrayList<ProductData> arrListProduct) {
		this.arrListProduct = arrListProduct;
	}
}
