package com.compass.model;

public class Invoice {
	
	public String POSITION;
	public String CUSTOMER_POSITION;
	public String CUSTOMER;
	public String COMPANY;
	public String TRANSACTION_TYPE;
	public String INV_BOOK;
	public String INV_NO;
	public String INV_BOOK_NO;
	public String STAFF_CODE;
	public String PAYMENT_TYPE;
	public String INV_AMOUNT;
	public String INV_STATUS;
	public String INV_BARCODE;
	public String INV_SCAN;
	public String INV_PROBLEM;
	public String JOB_ORDER;
	public String INV_LATITUDE;
	public String INV_LONGITUDE;
	public String INV_BRANCH;
	public String INV_EXPECTDTIME;
	public String INV_SHOWTIME;
	
	public Invoice(String POSITION, String COMPANY, String TRANSACTION_TYPE, String INV_BOOK, String INV_NO, String INV_BOOK_NO, String INV_AMOUNT, String PAYMENT_TYPE, String JOB_ORDER, String INV_STATUS, String LAT, String LONG, String EXPECTDTIME) {
		 super();
		 this.POSITION = POSITION;
		 this.COMPANY = COMPANY;
		 this.TRANSACTION_TYPE = TRANSACTION_TYPE;
		 this.INV_BOOK= INV_BOOK;
		 this.INV_NO= INV_NO;
		 this.INV_BOOK_NO= INV_BOOK_NO;
		 this.PAYMENT_TYPE = PAYMENT_TYPE;
		 this.INV_AMOUNT = INV_AMOUNT;
		 this.JOB_ORDER = JOB_ORDER;
		 this.INV_BARCODE = "";
		 this.INV_STATUS = INV_STATUS;
		 this.INV_LATITUDE = LAT;
		 this.INV_LONGITUDE = LONG;
		 this.INV_EXPECTDTIME = EXPECTDTIME;
	 }
	  
	 
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

	public String getCUSTOMER_POSITION() {
		return CUSTOMER_POSITION;
	}
	public void setCUSTOMER_POSITION(String cUSTOMER_POSITION) {
		CUSTOMER_POSITION = cUSTOMER_POSITION;
	}
	
	public String getCUSTOMER() {
		return CUSTOMER;
	}
	public void setCUSTOMER(String cUSTOMER) {
		CUSTOMER = cUSTOMER;
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
	
	public String getSTAFF_CODE() {
		return STAFF_CODE;
	}
	public void setSTAFF_CODE(String sTAFF_CODE) {
		STAFF_CODE = sTAFF_CODE;
	}
	
	public String getPAYMENT_TYPE() {
		return PAYMENT_TYPE;
	}
	public void setPAYMENT_TYPE(String pAYMENT_TYPE) {
		PAYMENT_TYPE = pAYMENT_TYPE;
	}
	
	public String getINV_AMOUNT() {
		return INV_AMOUNT;
	}
	public void setINV_AMOUNT(String iNV_AMOUNT) {
		INV_AMOUNT = iNV_AMOUNT;
	}
	
	public String getINV_STATUS() {
		return INV_STATUS;
	}
	public void setINV_STATUS(String iNV_STATUS) {
		INV_STATUS = iNV_STATUS;
	}
	
	public String getINV_BARCODE() {
		return INV_BARCODE;
	}
	public void setINV_BARCODE(String iNV_BARCODE) {
		INV_BARCODE = iNV_BARCODE;
	}
	
	public String getINV_SCAN() {
		return INV_SCAN;
	}
	public void setINV_SCAN(String iNV_SCAN) {
		INV_SCAN = iNV_SCAN;
	}
	
	public String getINV_PROBLEM() {
		return INV_PROBLEM;
	}
	public void setINV_PROBLEM(String iNV_PROBLEM) {
		INV_PROBLEM = iNV_PROBLEM;
	}
	
	public String getJOB_ORDER() {
		return JOB_ORDER;
	}
	public void setJOB_ORDER(String jOB_ORDER) {
		JOB_ORDER = jOB_ORDER;
	}
	
	public String getINV_LATITUDE() {
		return INV_LATITUDE;
	}
	public void setINV_LATITUDE(String iNV_LATITUDE) {
		INV_LATITUDE = iNV_LATITUDE;
	}

	public String getINV_LONGITUDE() {
		return INV_LONGITUDE;
	}
	public void setINV_LONGITUDE(String iNV_LONGITUDE) {
		INV_LONGITUDE = iNV_LONGITUDE;
	}
	
	public String getINV_BRANCH() {
		return INV_BRANCH;
	}
	public void setINV_BRANCH(String iNV_BRANCH) {
		INV_BRANCH = iNV_BRANCH;
	}
	
	public String getINV_EXPECTDTIME() {
		return INV_EXPECTDTIME;
	}
	public void setINV_EXPECTDTIME(String iNV_EXPECTDTIME) {
		INV_EXPECTDTIME = iNV_EXPECTDTIME;
	}
	
	public String getINV_SHOWTIME() {
		return INV_SHOWTIME;
	}
	public void setINV_SHOWTIME(String iNV_SHOWTIME) {
		INV_SHOWTIME = iNV_SHOWTIME;
	}
		
	
	 @Override
	 public String toString() {
	  return  CUSTOMER + INV_BOOK_NO;
	 }	  
}