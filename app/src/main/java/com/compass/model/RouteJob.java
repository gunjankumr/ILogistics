package com.compass.model;

public class RouteJob {
	
	public String POSITION;
	public String COMPANY;
	public String TRANSACTION_TYPE;
	public String INV_BOOK;
	public String INV_NO;
	public String INV_BOOK_NO;
	public String CUST_CODE;
	public String CUST_NAME;
	public String JOB_ORDER;

	public RouteJob(String POSITION, String COMPANY, String TRANSACTION_TYPE, String INV_BOOK, String INV_NO, String INV_BOOK_NO, String INV_AMOUNT, String PAYMENT_TYPE, String JOB_ORDER, String INV_STATUS, String CUST_CODE, String CUST_NAME) {
		 super();
		 this.POSITION = POSITION;
		 this.COMPANY = COMPANY;
		 this.TRANSACTION_TYPE = TRANSACTION_TYPE;
		 this.INV_BOOK= INV_BOOK;
		 this.INV_NO= INV_NO;
		 this.INV_BOOK_NO= INV_BOOK_NO;
		 this.JOB_ORDER = JOB_ORDER;
		 this.CUST_CODE = CUST_CODE;
		 this.CUST_NAME = CUST_NAME;
	}
	  
	public RouteJob() {}
	
	
	public String getPOSITION() {
		return POSITION;
	}
	public void setPOSITION(String pOSITION) {
		POSITION = pOSITION;
	}
	
	public String getCOMPANY() {
		return COMPANY;
	}
	public void setCOMPANY(String cOMPANY) {
		COMPANY = cOMPANY;
	}

	public String getTRANSACTION_TYPE() {
		return TRANSACTION_TYPE;
	}
	public void setTRANSACTION_TYPE(String tRANSACTION_TYPE) {
		TRANSACTION_TYPE = tRANSACTION_TYPE;
	}

	public String getINV_BOOK() {
		return INV_BOOK;
	}
	public void setINV_BOOK(String iNV_BOOK) {
		INV_BOOK = iNV_BOOK;
	}

	public String getINV_NO() {
		return INV_NO;
	}
	public void setINV_NO(String iNV_NO) {
		INV_NO = iNV_NO;
	}
	
	public String getINV_BOOK_NO() {
		return INV_BOOK_NO;
	}
	public void setINV_BOOK_NO(String iNV_BOOK_NO) {
		INV_BOOK_NO = iNV_BOOK_NO;
	}
	
	public String getCUST_CODE() {
		return CUST_CODE;
	}
	public void setCUST_CODE(String cUST_CODE) {
		CUST_CODE = cUST_CODE;
	}
	
	public String getJOB_ORDER() {
		return JOB_ORDER;
	}
	public void setJOB_ORDER(String jOB_ORDER) {
		JOB_ORDER = jOB_ORDER;
	}
	
	public String getCUST_NAME() {
		return CUST_NAME;
	}
	public void setCUST_NAME(String cUST_NAME) {
		CUST_NAME = cUST_NAME;
	}
	
	
	@Override
	public String toString() {
		return  INV_BOOK_NO;
	}
}