package com.compass.model;

public class Customer {
	
	String POSITION = null;
	String CUST_BRANCH = null;
	String CUST_CODE = null;
	String CUST_NAME = null;
	String BRANCH_CODE = null;
	String BRANCH_NAME = null;
	String TOTAL_INVOICE = null;
	String TOTAL_REMARK = null;
	String COMPLETE_STATUS = null;
	String CASH_STATUS = null;
	String EXPECTDTIME = null;
	String STAUS = null;
	String CHECKIN = null;
	public Customer(String POSITION, String CUST_CODE, String CUST_NAME, String TOTAL_INVOICE, String COMPLETE_STATUS, String TOTAL_REMARK) {
		 super();
		 this.POSITION	= POSITION;
		 this.CUST_CODE = CUST_CODE;
		 this.CUST_NAME = CUST_NAME;
		 this.TOTAL_INVOICE  = TOTAL_INVOICE;
		 this.TOTAL_REMARK   = TOTAL_REMARK;
		 this.COMPLETE_STATUS = COMPLETE_STATUS;
		 this.STAUS   = "Check In";
	 }
	  
	 
	public String getPOSITION() {
		return POSITION;
	}
	public void setPOSITION(String pOSITION) {
		POSITION = pOSITION;
	}
	
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
		CUST_CODE = cUST_CODE;
	}

	public String getCUST_NAME() {
		return CUST_NAME;
	}
	public void setCUST_NAME(String cUST_NAME) {
		CUST_NAME = cUST_NAME;
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
		TOTAL_INVOICE = tOTAL_INVOICE;
	}
	
	public String getCOMPLETE_STATUS() {
		return COMPLETE_STATUS;
	}
	public void setCOMPLETE_STATUS(String cOMPLETE_STATUS) {
		COMPLETE_STATUS = cOMPLETE_STATUS;
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
		EXPECTDTIME = eXPECTDTIME;
	}
	
	public String getSTAUS() {
		return STAUS;
	}
	public void setSTAUS(String sTAUS) {
		STAUS = sTAUS;
	}
	
	public String getTOTAL_REMARK() {
		return TOTAL_REMARK;
	}
	public void setTOTAL_REMARK(String tOTAL_REMARK) {
		TOTAL_REMARK = tOTAL_REMARK;
	}
	public String getCHECKIN() {
		return CHECKIN;
	}
	public void setCHECKIN(String cHECKIN) {
		CHECKIN = cHECKIN;
	}
	
	 @Override
	 public String toString() {
	  return  CUST_CODE+" "+CUST_NAME;
	 }	  
}