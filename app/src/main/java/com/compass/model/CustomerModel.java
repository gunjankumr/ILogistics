package com.compass.model;

import java.util.ArrayList;


public class CustomerModel {
	
	public String CUST_BRANCH;
	public String CUST_CODE;
	public String CUST_NAME;
	public String BRANCH_CODE;
	public String BRANCH_NAME;
	public String TOTAL_INVOICE;
	public String TOTAL_REMARK;
	public String COMPLETE_STATUS;
	public String CASH_STATUS;
	public String EXPECTDTIME;
	public String MIN_TIME;
	public String CHECKIN;
	public ArrayList<InvoiceModel> invoiceList = new ArrayList<InvoiceModel>();
	
	public String getCUST_BRANCH() {
		return CUST_BRANCH;
	}
	public void setCUST_BRANCH(String cUST_BRANCH) {
		CUST_BRANCH = cUST_BRANCH;
	}
	public String getCUST_CODE() {
		return CUST_CODE;
	}
	public void setCUST_CODE(String cUST_CODE) {
		this.CUST_CODE = cUST_CODE;
	}
	public String getCUST_NAME() {
		return CUST_NAME;
	}
	public void setCUST_NAME(String cUST_NAME) {
		this.CUST_NAME = cUST_NAME;
	}
	public String getBRANCH_CODE() {
		return BRANCH_CODE;
	}
	public void setBRANCH_CODE(String bRANCH_CODE) {
		BRANCH_CODE = bRANCH_CODE;
	}
	public String getBRANCH_NAME() {
		return BRANCH_NAME;
	}
	public void setBRANCH_NAME(String bRANCH_NAME) {
		BRANCH_NAME = bRANCH_NAME;
	}
	public String getTOTAL_INVOICE() {
		return TOTAL_INVOICE;
	}
	public void setTOTAL_INVOICE(String tOTAL_INVOICE) {
		this.TOTAL_INVOICE = tOTAL_INVOICE;
	}
	public String getTOTAL_REMARK() {
		return TOTAL_REMARK;
	}
	public void setTOTAL_REMARK(String tOTAL_REMARK) {
		this.TOTAL_REMARK = tOTAL_REMARK;
	}
	public String getCOMPLETE_STATUS() {
		return COMPLETE_STATUS;
	}
	public void setCOMPLETE_STATUS(String cOMPLETE_STATUS) {
		this.COMPLETE_STATUS = cOMPLETE_STATUS;
	}
	public String getCASH_STATUS() {
		return CASH_STATUS;
	}
	public void setCASH_STATUS(String cASH_STATUS) {
		CASH_STATUS = cASH_STATUS;
	}
	public String getEXPECTDTIME() {
		return EXPECTDTIME;
	}
	public void setEXPECTDTIME(String eXPECTDTIME) {
		this.EXPECTDTIME = eXPECTDTIME;
	}
	public String getMIN_TIME() {
		return MIN_TIME;
	}
	public void setMIN_TIME(String mIN_TIME) {
		this.MIN_TIME = mIN_TIME;
	}
	public String getCHECKIN() {
		return CHECKIN;
	}
	public void setCHECKIN(String cHECKIN) {
		this.CHECKIN = cHECKIN;
	}
	public ArrayList<InvoiceModel> getInvoiceList() {
		return invoiceList;
	}
	public void setInvoiceList(ArrayList<InvoiceModel> invoiceList) {
		this.invoiceList = invoiceList;
	}
	
}