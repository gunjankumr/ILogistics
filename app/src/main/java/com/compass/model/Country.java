package com.compass.model;

public class Country {
	  
	String code = null;
	String name = null;
	String text = null;
	boolean selected = false;
	  
	 public Country(String code, String name, String text, boolean selected) {
	  super();
	  this.code = code;
	  this.name = name;
	  this.text = text;
	  this.selected = selected;
	 }
	  
	 public String getCode() {
	  return code;
	 }
	 public void setCode(String code) {
	  this.code = code;
	 }
	 public String getName() {
	  return name;
	 }
	 public void setName(String name) {
	  this.name = name;
	 }
	 public String getText() {
		return text;
	 }
	 public void setText(String text) {
		this.text = text;
	 }		
	 public boolean isSelected() {
	  return selected;
	 }
	 public void setSelected(boolean selected) {
	  this.selected = selected;
	 }
	  
}