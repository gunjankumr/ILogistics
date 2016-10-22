package com.compass.model;

public class Product {
	
	String POSITION = null;
	String PRODUCT_SEQ = null;
	String PRODUCT_CODE = null;
	String PRODUCT_NAME = null;
	String WARE_CODE = null;
	String WARE_ZONE = null;
	String UNIT_CODE = null;
	String UNIT= null;
	String QTY = null;
	String QTY_PICKED = null;
	String QTY_RETURN = null;
	String PROBLEM_COMPLAIN = null;
	String PROBLEM_REMARK = null;

	public Product(String PRODUCT_SEQ, String PRODUCT_CODE, String PRODUCT_NAME, String UNIT, String QTY, String QTY_PICKED) {
		 super();
		 this.PRODUCT_SEQ  =  PRODUCT_SEQ;
		 this.PRODUCT_CODE = PRODUCT_CODE;
		 this.PRODUCT_NAME = PRODUCT_NAME;
		 this.UNIT = UNIT;
		 this.QTY = QTY;
		 this.QTY_PICKED = QTY_PICKED;
	 }
	  
	
	public String getPOSITION() {
		return POSITION;
	}
	public void setPOSITION(String pOSITION) {
		POSITION = pOSITION;
	}
	
	public String getPRODUCT_SEQ() {
		return PRODUCT_SEQ;
	}
	public void setPRODUCT_SEQ(String pRODUCT_SEQ) {
		PRODUCT_SEQ = pRODUCT_SEQ;
	}
	
	public String getPRODUCT_CODE() {
		return PRODUCT_CODE;
	}
	public void setPRODUCT_CODE(String pRODUCT_CODE) {
		PRODUCT_CODE = pRODUCT_CODE;
	}
	
	public String getPRODUCT_NAME() {
		return PRODUCT_NAME;
	}
	public void setPRODUCT_NAME(String pRODUCT_NAME) {
		PRODUCT_NAME = pRODUCT_NAME;
	}
	
	public String getWARE_CODE() {
		return WARE_CODE;
	}
	public void setWARE_CODE(String wARE_CODE) {
		WARE_CODE = wARE_CODE;
	}

	public String getWARE_ZONE() {
		return WARE_ZONE;
	}
	public void setWARE_ZONE(String wARE_ZONE) {
		WARE_ZONE = wARE_ZONE;
	}
	
	public String getUNIT_CODE() {
		return UNIT_CODE;
	}
	public void setUNIT_CODE(String uNIT_CODE) {
		UNIT_CODE = uNIT_CODE;
	}
	
	public String getUNIT() {
		return UNIT;
	}
	public void setUNIT(String uNIT) {
		UNIT = uNIT;
	}
	
	public String getQTY() {
		return QTY;
	}
	public void setQTY(String qTY) {
		QTY = qTY;
	}
	
	public String getQTY_PICKED() {
		return QTY_PICKED;
	}
	public void setQTY_PICKED(String qTY_PICKED) {
		QTY_PICKED = qTY_PICKED;
	}
	
	public String getQTY_RETURN() {
		return QTY_RETURN;
	}
	public void setQTY_RETURN(String qTY_RETURN) {
		QTY_RETURN = qTY_RETURN;
	}


	public String getPROBLEM_COMPLAIN() {
		return PROBLEM_COMPLAIN;
	}
	public void setPROBLEM_COMPLAIN(String pROBLEM_COMPLAIN) {
		PROBLEM_COMPLAIN = pROBLEM_COMPLAIN;
	}


	public String getPROBLEM_REMARK() {
		return PROBLEM_REMARK;
	}
	public void setPROBLEM_REMARK(String pROBLEM_REMARK) {
		PROBLEM_REMARK = pROBLEM_REMARK;
	}
	
	@Override
	 public String toString() {
	  return  PRODUCT_CODE+" "+PRODUCT_NAME;
	 }	  
	 
}