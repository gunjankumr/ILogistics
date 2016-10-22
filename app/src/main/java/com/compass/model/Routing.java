package com.compass.model;

public class Routing {
	  
	 String CODE = null;
	 String ROUTE_CODE = null;
	 String ROUTE_NAME = null;
	 String DELIVERY_SEQ = null;
	 String PLATE_NO = null;
	 String SUP_NAME = null;
	 String DRIVER_NAME = null;
	 String PICKUP_TIME = null;
	 String LOCATION = null;
	 String TOTAL_CUST = null;
	 String TOTAL_INVOICE = null;
	

	public Routing(String CODE, String ROUTE_CODE, String ROUTE_NAME, String DELIVERY_SEQ, String PLATE_NO, String SUP_NAME, String DRIVER_NAME, String PICKUP_TIME, String LOCATION, String TOTAL_CUST, String TOTAL_INVOICE) {
		 super();
		 this.CODE = CODE;
		 this.ROUTE_CODE = ROUTE_CODE;
		 this.ROUTE_NAME = ROUTE_NAME;
		 this.DELIVERY_SEQ = DELIVERY_SEQ;
		 this.PLATE_NO   = PLATE_NO;
		 this.SUP_NAME   = SUP_NAME;
		 this.DRIVER_NAME = DRIVER_NAME;
		 this.PICKUP_TIME = PICKUP_TIME;
		 this.LOCATION = LOCATION;
		 this.TOTAL_CUST = TOTAL_CUST;
		 this.TOTAL_INVOICE = TOTAL_INVOICE;
	 }
	  
	 
	public String getCODE() {
		return CODE;
	}
	public void setCODE(String cODE) {
		CODE = cODE;
	}
	
	 public String getROUTE_CODE() {
		 return ROUTE_CODE;
	 }

	 public void setROUTE_CODE(String ROUTE_CODE) {
		 this.ROUTE_CODE = ROUTE_CODE;
	 }

	 public String getROUTE_NAME() {
		 return ROUTE_NAME;
	 }

	 public void setROUTE_NAME(String ROUTE_NAME) {
		 this.ROUTE_NAME = ROUTE_NAME;
	 }

	 public String getDELIVERY_SEQ() {
		return DELIVERY_SEQ;
	}

	public void setDELIVERY_SEQ(String dELIVERY_SEQ) {
		DELIVERY_SEQ = dELIVERY_SEQ;
	}

	public String getPLATE_NO() {
		 return PLATE_NO;
	 }

	 public void setPLATE_NO(String PLATE_NO) {
		 this.PLATE_NO = PLATE_NO;
	 }

	 public String getSUP_NAME() {
		 return SUP_NAME;
	 }

	 public void setSUP_NAME(String SUP_NAME) {
		 this.SUP_NAME = SUP_NAME;
	 }
	 
	 public String getDRIVER_NAME() {
		 return DRIVER_NAME;
	 }

	 public void setDRIVER_NAME(String dRIVER_NAME) {
		 this.DRIVER_NAME = dRIVER_NAME;
	 }
	 
	 public String getPICKUP_TIME() {
		 return PICKUP_TIME;
	 }
	 public void setPICKUP_TIME(String pICKUP_TIME) {
		 this.PICKUP_TIME = pICKUP_TIME;
	 }
	
	 public String getLOCATION() {
		 return LOCATION;
	 }
	 public void setLOCATION(String lOCATION) {
		 this.LOCATION = lOCATION;
	 }
		
	 public String getTOTAL_CUST() {
		 return TOTAL_CUST;
	 }
	 public void setTOTAL_CUST(String tOTAL_CUST) {	
		 TOTAL_CUST = tOTAL_CUST;
	 }

	 public String getTOTAL_INVOICE() {
		 return TOTAL_INVOICE;
	 }
	 public void setTOTAL_INVOICE(String tOTAL_INVOICE) {
		 TOTAL_INVOICE = tOTAL_INVOICE;
	 }
	
	 @Override
	 public String toString() {
	  return  ROUTE_CODE+" "+ROUTE_NAME+" "+PLATE_NO+" "+SUP_NAME;
	 }	  
}