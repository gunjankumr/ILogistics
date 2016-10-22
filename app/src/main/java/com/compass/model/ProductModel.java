package com.compass.model;

import java.io.Serializable;


public class ProductModel implements Serializable {
	
	public String POSITION;
	public String CUSTOMER;
	public String INVOICE;
	public String PRODUCT_SEQ;
	public String PRODUCT_CODE;
	public String PRODUCT_NAME;
	public String WARE_CODE;
	public String WARE_ZONE;
	public String UNIT_CODE;
	public String UNIT;
	public String QTY;
	public String QTY_PICKED;
	public String QTY_RETURN;
	public String PROBLEM_COMPLAIN;
	public String PROBLEM_REMARK;
	
	public String getPOSITION() {
		return POSITION;
	}
	public void setPOSITION(String pOSITION) {
		POSITION = pOSITION;
	}
	
	public String getCUSTOMER() {
		return CUSTOMER;
	}
	public void setCUSTOMER(String cUSTOMER) {
		CUSTOMER = cUSTOMER;
	}
	
	public String getINVOICE() {
		return INVOICE;
	}
	public void setINVOICE(String iNVOICE) {
		INVOICE = iNVOICE;
	}
	
	public String getPRODUCT_SEQ() {
		return PRODUCT_SEQ;
	}
	public void setPRODUCT_SEQ(String pRODUCT_SEQ) {
		this.PRODUCT_SEQ = pRODUCT_SEQ;
	}
	
	public String getPRODUCT_CODE() {
		return PRODUCT_CODE;
	}
	public void setPRODUCT_CODE(String pRODUCT_CODE) {
		this.PRODUCT_CODE = pRODUCT_CODE;
	}
	
	public String getPRODUCT_NAME() {
		return PRODUCT_NAME;
	}
	public void setPRODUCT_NAME(String pRODUCT_NAME) {
		this.PRODUCT_NAME = pRODUCT_NAME;
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
	
	public String getUNIT() {
		return UNIT;
	}
	public void setUNIT(String uNIT) {
		this.UNIT = uNIT;
	}
	
	public String getUNIT_CODE() {
		return UNIT_CODE;
	}
	public void setUNIT_CODE(String uNIT_CODE) {
		this.UNIT_CODE = uNIT_CODE;
	}
	
	public String getQTY() {
		return QTY;
	}
	public void setQTY(String qTY) {
		this.QTY = qTY;
	}
	
	public String getQTY_PICKED() {
		return QTY_PICKED;
	}
	public void setQTY_PICKED(String qTY_PICKED) {
		this.QTY_PICKED = qTY_PICKED;
	}
	public String getQTY_RETURN() {
		return QTY_RETURN;
	}
	public void setQTY_RETURN(String qTY_RETURN) {
		this.QTY_RETURN = qTY_RETURN;
	}
	public String getPROBLEM_COMPLAIN() {
		return PROBLEM_COMPLAIN;
	}
	public void setPROBLEM_COMPLAIN(String pROBLEM_COMPLAIN) {
		this.PROBLEM_COMPLAIN = pROBLEM_COMPLAIN;
	}
	public String getPROBLEM_REMARK() {
		return PROBLEM_REMARK;
	}
	public void setPROBLEM_REMARK(String pROBLEM_REMARK) {
		this.PROBLEM_REMARK = pROBLEM_REMARK;
	}
	
	@Override
	 public String toString() {
	  return  PRODUCT_CODE+" "+PRODUCT_NAME;
	 }
}