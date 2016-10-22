package com.compass.utils;

import com.google.common.base.Strings;

import com.compass.activity.ILogisticsApplication;
import com.compass.dbhelper.CheckInCheckOut;
import com.compass.model.CustomerModel;
import com.compass.model.InvoiceModel;
import com.compass.model.ProblemSet;
import com.compass.model.ProductModel;
import com.compass.model.RouteJob;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.content.Context;
import android.util.Log;

import java.io.StringWriter;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;



public class WebService {
	
	private SoapObject request;
	private SoapSerializationEnvelope envelope;
	private AndroidHttpTransport androidHttpTransport;
	private ValueHolder valueHolder;

	//private ValueHolder valueHolder;
	
	public WebService() {
		//valueHolder = ValueHolder.getSingletonObject();
	}
	
	
	public String checkVersion(String version) throws UnknownHostException,TimeoutException, Exception {
		String METHOD_NAME = "CheckVersion";
		request = new SoapObject(Constants.NAMESPACE, METHOD_NAME);		
		PropertyInfo property = new PropertyInfo();		
		property.setName("inVersion");
		property.setValue(version);
		property.setType(String.class);
        request.addProperty(property);
        
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        androidHttpTransport = new AndroidHttpTransport(Constants.URL);
        androidHttpTransport.debug = true;
        try
        {
            androidHttpTransport.call(Constants.NAMESPACE+METHOD_NAME, envelope);
            Log.d(Constants.LOGD, "------request checkVersion------------"+androidHttpTransport.requestDump);
            Log.d(Constants.LOGD, "------response checkVersion------------"+androidHttpTransport.responseDump);

            if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
            	SoapObject soapObject = (SoapObject) envelope.bodyIn;
        	} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault = FAILURE
        		SoapFault soapFault = (SoapFault) envelope.bodyIn;
        		throw new Exception(soapFault.getMessage());
        	}
            return envelope.getResponse().toString();
        }catch(UnknownHostException e) {
        	throw e;
        }catch (TimeoutException e) {
        	throw e;
		}catch(Exception e) {
            throw e;            
        }
	}
	
	public String checkUserLogin(String userName, String password) throws UnknownHostException,TimeoutException, Exception {
		String METHOD_NAME = "CheckUserLogin";
		request = new SoapObject(Constants.NAMESPACE, METHOD_NAME);		
		PropertyInfo property = new PropertyInfo();		
		property.setName("inUsername");
		property.setValue(userName);
		property.setType(String.class);
        request.addProperty(property);
        
        property = new PropertyInfo();
        property.setName("inPassword");
        property.setValue(password);
        property.setType(String.class);
        request.addProperty(property);
        
        property = new PropertyInfo();
        property.setName("inDeviceID");
        property.setValue(password);
        property.setType(String.class);
        request.addProperty(property);
        
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        androidHttpTransport = new AndroidHttpTransport(Constants.URL);
        androidHttpTransport.debug = true;
        try
        {
            androidHttpTransport.call(Constants.NAMESPACE+METHOD_NAME, envelope);
            Log.d(Constants.LOGD, "------request checkUserLogin------------"+androidHttpTransport.requestDump);
            Log.d(Constants.LOGD, "------response checkUserLogin------------"+androidHttpTransport.responseDump);

            if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
            	SoapObject soapObject = (SoapObject) envelope.bodyIn;
        	} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault = FAILURE
        		SoapFault soapFault = (SoapFault) envelope.bodyIn;
        		throw new Exception(soapFault.getMessage());
        	}   
            return envelope.getResponse().toString();
        }catch(UnknownHostException e) {
        	throw e;
        }catch (TimeoutException e) {
        	throw e;
		}catch(Exception e) {
            throw e;            
        }
	}
	
	
	// Need to call and test output 
		public ArrayList<HashMap<String, String>> GetDailyRouteInfoByOTP(String inUsername, String inDeviceID, String inCompany, String inTel, String inOTP) throws UnknownHostException,Exception {			
			final String NAMESPACE = "http://tempuri.org/";
			final String SOAP_ACTION = "http://tempuri.org/GetDailyRouteInfoByOTP";
			String METHOD_NAME = "GetDailyRouteInfoByOTP";
			
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
			
//			DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
//			DateFormat outputFormat = new SimpleDateFormat("yyyyMMdd");
//			Date date = inputFormat.parse(inDeliveryDate);
//			String outputDateStr = outputFormat.format(date);
//			System.out.println(outputDateStr);		
//			inDeliveryDate = outputDateStr;
			
//			inDeliveryDate = "20140822";
			request.addProperty("inUsername", inUsername);
			request.addProperty("inDeviceID", inDeviceID);
			request.addProperty("inCompany", inCompany);
			request.addProperty("inTel", inTel);
			request.addProperty("inOTP", inOTP);
			

	      
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;

			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(Constants.URLJSON);
			androidHttpTransport.debug = true;
			ArrayList<HashMap<String, String>> responseList = new ArrayList<HashMap<String, String>>();
	        try
	        {	
	        	androidHttpTransport.call(SOAP_ACTION, envelope);
	    		SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
	    		System.out.println(response);

	    		String responseJSON=response.toString();
	   
	    		
	    		if(responseJSON != null)
	    		{
	    			try 
	                {
	                    //JSONObject parent = new JSONObject(responseJSON);
	                    //JSONArray taskDetails = parent.getJSONArray("result");

	    				JSONArray taskDetails = new JSONArray(responseJSON);
	    				
	                    for(int i=0; i < taskDetails.length(); i++)
	                    {
	                    	if(taskDetails.getJSONObject(i).getString("FLAG").toString().equalsIgnoreCase("0")) {
	            	    		return responseList;
	                    	}
	                    	if(taskDetails.getJSONObject(i).getString("ROUTE_CODE").toString().length() <= 0 || taskDetails.getJSONObject(i).getString("DOC_DATE").toString().length() <=0 || taskDetails.getJSONObject(i).getString("DELIVERY_SEQ").toString().length() <= 0) {
	                    		return responseList;
	                    	}
	                    	HashMap<String, String> map;
	  	             		map = new HashMap<String, String>();
	  	             		map.put("ROUTE_CODE",Utilities.getServiceString(taskDetails.getJSONObject(i).getString("ROUTE_CODE").toString()));
	  	             		//map.put("DELIVER_NAME",Utilities.getServiceString(taskDetails.getJSONObject(i).getString("DELIVER_NAME").toString()));
	  	             		//map.put("ROUTE_DESCRIPTION",Utilities.getServiceString(taskDetails.getJSONObject(i).getString("ROUTE_DESCRIPTION").toString()));
	  	             		//map.put("PLATE_NO",Utilities.getServiceString(taskDetails.getJSONObject(i).getString("PLATE_NO").toString()));
	  	             		//map.put("CURRENT_DATE",Utilities.getServiceString(taskDetails.getJSONObject(i).getString("CURRENT_DATE").toString()));
	  	             		map.put("DOC_DATE",Utilities.getServiceString(taskDetails.getJSONObject(i).getString("DOC_DATE").toString()));
	  	             		map.put("DELIVERY_SEQ",Utilities.getServiceString(taskDetails.getJSONObject(i).getString("DELIVERY_SEQ").toString()));
	  	             		map.put("COMPANY",Utilities.getServiceString(taskDetails.getJSONObject(i).getString("COMPANY").toString()));
	  	             		responseList.add(map);

	                    }
	                }  catch (JSONException e) 
	                    {
	                    Log.e("Json Error", "Error: " + e.toString());
	                        e.printStackTrace();
	                    }

	    		}

	    		else if (envelope.bodyIn instanceof SoapFault) { // SoapFault = FAILURE
	        		SoapFault soapFault = (SoapFault) envelope.bodyIn;
	        		throw new Exception(soapFault.getMessage());
	        	}
	    		return responseList;
	        }catch(UnknownHostException e) {
	        	throw e;
			}catch(Exception e) {
	            throw e;            
	        }
		}
	
	
	// Need to call and test output 
	public ArrayList<HashMap<String, String>> GetRouteByPhoneNo(String username, String deviceID, String compCode, String inDeliveryDate, String inPhoneNumber) throws UnknownHostException,Exception {
		final String NAMESPACE = "http://tempuri.org/";
		final String SOAP_ACTION = "http://tempuri.org/GetRouteByPhoneNo";
		String METHOD_NAME = "GetRouteByPhoneNo";
		//inDeliveryDate = "12/09/2014 13:25";
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		
		DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat outputFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = inputFormat.parse(inDeliveryDate);
		String outputDateStr = outputFormat.format(date);
		System.out.println(outputDateStr);		
		inDeliveryDate = outputDateStr;
		
//		inDeliveryDate = "20160418";
		request.addProperty("inUsername", username);
		request.addProperty("inDeviceID", deviceID);
		request.addProperty("inCompany", compCode);
		request.addProperty("inDeliveryDate", inDeliveryDate);
		request.addProperty("inPhoneNumber", inPhoneNumber);
		

      
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(Constants.URLJSON);
		androidHttpTransport.debug = true;
		ArrayList<HashMap<String, String>> responseList = new ArrayList<HashMap<String, String>>();
        try
        {	
        	androidHttpTransport.call(SOAP_ACTION, envelope);
    		SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
    		System.out.println(response);

    		String responseJSON=response.toString();
   
    		
    		if(responseJSON != null)
    		{
    			try 
                {
                    JSONObject parent = new JSONObject(responseJSON);
                    JSONArray taskDetails = parent.getJSONArray("result");

                    for(int i=0; i < taskDetails.length(); i++)
                    {
                    	HashMap<String, String> map;
  	             		map = new HashMap<String, String>();
  	             		map.put("ROUTE_CODE",Utilities.getServiceString(taskDetails.getJSONObject(i).getString("ROUTE_CODE").toString()));
  	             		map.put("DELIVER_NAME",Utilities.getServiceString(taskDetails.getJSONObject(i).getString("DELIVER_NAME").toString()));
  	             		map.put("ROUTE_DESCRIPTION",Utilities.getServiceString(taskDetails.getJSONObject(i).getString("ROUTE_DESCRIPTION").toString()));
  	             		map.put("PLATE_NO",Utilities.getServiceString(taskDetails.getJSONObject(i).getString("PLATE_NO").toString()));
  	             		map.put("CURRENT_DATE",Utilities.getServiceString(taskDetails.getJSONObject(i).getString("CURRENT_DATE").toString()));
  	             		responseList.add(map);

                    }
                }  catch (JSONException e) 
                    {
                    Log.e("Json Error", "Error: " + e.toString());
                        e.printStackTrace();
                    }

    		}

    		else if (envelope.bodyIn instanceof SoapFault) { // SoapFault = FAILURE
        		SoapFault soapFault = (SoapFault) envelope.bodyIn;
        		throw new Exception(soapFault.getMessage());
        	}
    		return responseList;
        }catch(UnknownHostException e) {
        	throw e;
		}catch(Exception e) {
            throw e;            
        }
	}
	
	// Need to call and test output 
	public ArrayList<HashMap<String, String>> GetAppVersion(String inApplicationName) throws UnknownHostException,Exception {
		final String NAMESPACE = "http://tempuri.org/";
		final String SOAP_ACTION = "http://tempuri.org/CRMAppVersion";
		String METHOD_NAME = "CRMAppVersion";		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		request.addProperty("inApplicationName", inApplicationName);
      
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(Constants.URLJSONVERSION);
		androidHttpTransport.debug = true;
		ArrayList<HashMap<String, String>> responseList = new ArrayList<HashMap<String, String>>();
        try
        {	
        	androidHttpTransport.call(SOAP_ACTION, envelope);
    		SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
    		System.out.println(response);

    		String responseJSON=response.toString();

    		
    		if(responseJSON != null)
    		{
    			try 
                {
       				JSONArray appVersion = new JSONArray(responseJSON);


                    for(int i=0; i < appVersion.length(); i++)
                    {
                    	HashMap<String, String> map;
  	             		map = new HashMap<String, String>();
  	             		map.put("APP_VERSION",Utilities.getServiceString(appVersion.getJSONObject(i).getString("APP_VERSION").toString()));
  	             		map.put("UPDATE_URL",Utilities.getServiceString(appVersion.getJSONObject(i).getString("UPDATE_URL").toString()));
  	             		responseList.add(map);

                    }
                }  catch (JSONException e) 
                    {
                    Log.e("Json Error", "Error: " + e.toString());
                        e.printStackTrace();
                    }

    		}

    		else if (envelope.bodyIn instanceof SoapFault) { // SoapFault = FAILURE
        		SoapFault soapFault = (SoapFault) envelope.bodyIn;
        		throw new Exception(soapFault.getMessage());
        	}
    		return responseList;
        }catch(UnknownHostException e) {
        	throw e;
		}catch(Exception e) {
            throw e;            
        }
	}

	
	// Need to call and test output 
	public ArrayList<HashMap<String, String>> GetRouteSeq(String username, String deviceID, String compCode, String inRouteCode, String inDeliveryDate) throws UnknownHostException,Exception {
		final String NAMESPACE = "http://tempuri.org/";
		final String SOAP_ACTION = "http://tempuri.org/GetRouteSeq";
		String METHOD_NAME = "GetRouteSeq";
		//inDeliveryDate = "12/09/2014 13:25";
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		
		DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat outputFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = inputFormat.parse(inDeliveryDate);
		String outputDateStr = outputFormat.format(date);
		System.out.println(outputDateStr);		
		inDeliveryDate = outputDateStr;
		
//		username = "JBT04";
//		deviceID = "123456";
//		compCode = "JB";
//		inDeliveryDate = "20160418";
		//inRouteCode = "IA2F";
		request.addProperty("inUsername", username);
		request.addProperty("inDeviceID", deviceID);
		request.addProperty("inCompany", compCode);
		request.addProperty("inRouteCode", inRouteCode);
		request.addProperty("inDeliveryDate", inDeliveryDate);
		

      
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(Constants.URLJSON);
		androidHttpTransport.debug = true;
		ArrayList<HashMap<String, String>> responseList = new ArrayList<HashMap<String, String>>();
        try
        {	
        	androidHttpTransport.call(SOAP_ACTION, envelope);
    		SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
    		System.out.println(response);

    		String responseJSON=response.toString();

    		
    		if(responseJSON != null)
    		{
    			try 
                {
                    JSONObject parent = new JSONObject(responseJSON);
                    JSONArray taskDetails = parent.getJSONArray("result");

                    for(int i=0; i < taskDetails.length(); i++)
                    {
                    	HashMap<String, String> map;
  	             		map = new HashMap<String, String>();
  	             		map.put("DELIVERY_SEQ",Utilities.getServiceString(taskDetails.getJSONObject(i).getString("DELIVERY_SEQ").toString()));
  	             		map.put("REC_CNT",Utilities.getServiceString(taskDetails.getJSONObject(i).getString("REC_CNT").toString()));
  	             		responseList.add(map);

                    }
                }  catch (JSONException e) 
                    {
                    Log.e("Json Error", "Error: " + e.toString());
                        e.printStackTrace();
                    }

    		}

    		else if (envelope.bodyIn instanceof SoapFault) { // SoapFault = FAILURE
        		SoapFault soapFault = (SoapFault) envelope.bodyIn;
        		throw new Exception(soapFault.getMessage());
        	}
    		return responseList;
        }catch(UnknownHostException e) {
        	throw e;
		}catch(Exception e) {
            throw e;            
        }
	}
	

	public ArrayList<HashMap<String, String>> getRouting(String username, String deviceID, String compCode, String inRouteCode, String inDeliverySeq, String inDeliveryDate, int page) throws UnknownHostException,Exception {
		final String NAMESPACE = "http://tempuri.org/";
		final String SOAP_ACTION = "http://tempuri.org/GetRouting";
		final String METHOD_NAME = "GetRouting";

		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		
		DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat outputFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = inputFormat.parse(inDeliveryDate);
		String outputDateStr = outputFormat.format(date);
		System.out.println(outputDateStr);		
		inDeliveryDate = outputDateStr;
		
//		inDeliveryDate = "20160418";
		request.addProperty("inUsername", username);
		request.addProperty("inDeviceID", deviceID);
		request.addProperty("inCompany", compCode);
		request.addProperty("inRouteCode", inRouteCode);
		request.addProperty("inDeliverySeq", inDeliverySeq);
		request.addProperty("inDeliveryDate", inDeliveryDate);
		request.addProperty("inPage", page);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(Constants.URLJSON);
		androidHttpTransport.debug = true;

		try {
		androidHttpTransport.call(SOAP_ACTION, envelope);
		SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
		System.out.println(response);

		String responseJSON=response.toString();

		JSONArray jarray =new JSONArray(responseJSON);


		System.out.println(request);
		System.out.println(responseJSON);
		ArrayList<HashMap<String, String>> responseList = new ArrayList<HashMap<String, String>>();

            if (jarray.length() > 0) { // SoapObject = SUCCESS
            	Log.e("response", "response=" + response.toString());
            	for (int i = 0; i < jarray.length(); i++) {
              			HashMap<String, String> map;
  	             		map = new HashMap<String, String>();
  	             		map.put("ROUTE_CODE", Utilities.getServiceString(jarray.getJSONObject(i).getString("ROUTE_CODE").toString()));
  	             		;
  	             		map.put("ROUTE_NAME", Utilities.getServiceString(jarray.getJSONObject(i).getString("ROUTE_NAME").toString()));
  	             		map.put("DELIVERY_SEQ", Utilities.getServiceString(jarray.getJSONObject(i).getString("DELIVERY_SEQ").toString()));
  	             		map.put("PLATE_NO", Utilities.getServiceString(jarray.getJSONObject(i).getString("PLATE_NO").toString()));
  	             		map.put("SUP_NAME", Utilities.getServiceString(jarray.getJSONObject(i).getString("SUP_NAME").toString()));
  	             		map.put("DELIVER_NAME", Utilities.getServiceString(jarray.getJSONObject(i).getString("DELIVER_NAME").toString()));
  	             		map.put("TOTAL_CUST", Utilities.getServiceString(jarray.getJSONObject(i).getString("TOTAL_CUST").toString()));
  	             		map.put("TOTAL_INVOICE", Utilities.getServiceString(jarray.getJSONObject(i).getString("TOTAL_INVOICE").toString()));
  	             		map.put("DOC_DATE", Utilities.getServiceString(jarray.getJSONObject(i).getString("DOC_DATE").toString()));
  	             		map.put("PICKUP_TIME", Utilities.getServiceString(jarray.getJSONObject(i).getString("PICKUP_TIME").toString()));
  	             		map.put("LOCATION", Utilities.getServiceString(jarray.getJSONObject(i).getString("LOCATION").toString()));
  	             			
  	             		responseList.add(map);
  	              }
  	         
        	} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault = FAILURE
        		SoapFault soapFault = (SoapFault) envelope.bodyIn;
        		throw new Exception(soapFault.getMessage());
        	}   
            
            return responseList;
        }catch(UnknownHostException e) {
        	throw e;
		}catch(Exception e) {
            throw e;            
        }
	}


	
	public ArrayList<CustomerModel> getRoutingDetails1(String username, String deviceID, String compCode, String inRouteCode, String inDeliverySeq, String inDeliveryDate) throws UnknownHostException,TimeoutException,Exception {
		//String METHOD_NAME = "GetRoutingDetails1";
			final String NAMESPACE = "http://tempuri.org/";
			final String SOAP_ACTION = "http://tempuri.org/GetRoutingDetails";
			final String METHOD_NAME = "GetRoutingDetails";

			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	
			DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
			DateFormat outputFormat = new SimpleDateFormat("yyyyMMdd");
			Date date = inputFormat.parse(inDeliveryDate);
			String outputDateStr = outputFormat.format(date);
			System.out.println(outputDateStr);		
			inDeliveryDate = outputDateStr;
	
	
//			inDeliveryDate = "20160418";
			request.addProperty("inUsername", username);
			request.addProperty("inDeviceID", deviceID);
			request.addProperty("inCompany", compCode);
			request.addProperty("inRouteCode", inRouteCode);
			request.addProperty("inDeliverySeq", inDeliverySeq);
			request.addProperty("inDeliveryDate", inDeliveryDate);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;

			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(Constants.URLJSON);
			androidHttpTransport.debug = true;
			ArrayList<CustomerModel> customerList = new ArrayList<CustomerModel>();
		
		
        try
        {	

        	androidHttpTransport.call(SOAP_ACTION, envelope);
    		SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
    		System.out.println(response);

    		String responseJSON=response.toString();
    		JSONObject job = new JSONObject(responseJSON);
//    		JSONArray jaClients = job.getJSONObject("result").getJSONArray("ClientData");
    		
    		
    		JSONObject json2 = job.getJSONObject("result"); 
    		
    		int clientDataCount = Integer.parseInt(json2.getString("Count"));
    		
    		
    		if(clientDataCount > 1) {
    			JSONArray jaClients = json2.getJSONArray("ClientData"); 
    			if (jaClients != null && jaClients.length() > 0) {
    				for (int i = 0; i < jaClients.length(); i++) {
    					JSONObject jobClient = jaClients.getJSONObject(i);
//    					JSONObject jobClient = json2.getJSONObject("ClientData");
    					CustomerModel customerModel = new CustomerModel();
    					customerModel.setCUST_BRANCH(jobClient.getString("CUST_CODE") + jobClient.getString("BRANCH_CODE"));
    					customerModel.setCUST_CODE(jobClient.getString("CUST_CODE"));
    					customerModel.setCUST_NAME(jobClient.getString("CUST_NAME"));
    					customerModel.setBRANCH_CODE(jobClient.getString("BRANCH_CODE"));
    					customerModel.setBRANCH_NAME(jobClient.getString("BRANCH_NAME"));
    					customerModel.setTOTAL_INVOICE(jobClient.getString("TOTAL_INVOICE"));
    					customerModel.setTOTAL_REMARK(jobClient.getString("TOTAL_REMARKS"));
    					customerModel.setCOMPLETE_STATUS("");
    					customerModel.setCASH_STATUS("N");
    					customerModel.setCHECKIN("N");
    					int invCnt = Integer.parseInt(jobClient.getString("TOTAL_INVOICE"));
    					ArrayList<InvoiceModel> invoiceList = new ArrayList<InvoiceModel>();
    					if(invCnt > 1){
    						JSONArray jaInvoice = jobClient.getJSONArray("ArrayOfInvoiceData");
    						if (jaInvoice != null && jaInvoice.length() > 0) {
    							for (int j = 0; j < jaInvoice.length(); j++) {
    								JSONObject jobInvoice = jaInvoice.getJSONObject(j);
    								InvoiceModel invoiceModel = new InvoiceModel();
    								invoiceModel.setCOMPANY(jobInvoice.getString("COMPANY"));
    								invoiceModel.setTRANSACTION_TYPE(jobInvoice.getString("TRANSACTION_TYPE"));
    								invoiceModel.setINV_BOOK(jobInvoice.getString("INV_BOOK"));
    								invoiceModel.setINV_NO(jobInvoice.getString("INV_NO"));
    								invoiceModel.setINV_BOOK_NO(jobInvoice.getString("INV_BOOK_NO"));
    								invoiceModel.setSTAFF_CODE(jobInvoice.getString("STAFF_CODE"));
    								invoiceModel.setINV_AMOUNT(jobInvoice.getString("INV_AMOUNT"));
    								invoiceModel.setPAYMENT_TYPE(jobInvoice.getString("PAYMENT_TYPE"));
    								invoiceModel.setJOB_ORDER(jobInvoice.getString("JOB_ORDER"));
    								invoiceModel.setINV_BARCODE("");
    								invoiceModel.setINV_STATUS(jobInvoice.getString("DELIVER_STATUS"));
    								invoiceModel.setINV_SCAN(jobInvoice.getString("INV_SCAN"));
    								invoiceModel.setINV_PROBLEM(jobInvoice.getString("PROBLEM_NOTE"));
    								invoiceModel.setINV_LATITUDE(jobInvoice.getString("LATITUDE"));
    								invoiceModel.setINV_LONGITUDE(jobInvoice.getString("LONGITUDE"));
    								invoiceModel.setINV_BRANCH(jobInvoice.getString("BRANCH_CODE"));
    								invoiceModel.setINV_EXPECTDTIME(jobInvoice.getString("EXPECTED_TIME"));
    								invoiceList.add(invoiceModel);
                     		
    								ArrayList<ProductModel> productList = new ArrayList<ProductModel>();
    								int prdCnt = Integer.parseInt(jobInvoice.getString("TOTAL_PRODUCT"));
    								if(prdCnt > 1){
    									JSONArray jaProducts = jobInvoice.getJSONArray("ArrayOfProductData");
    									if (jaProducts != null && jaProducts.length() > 0) {
    										for (int k = 0; k < jaProducts.length(); k++) {
    											JSONObject jobProduct = jaProducts.getJSONObject(k);
    								
    											ProductModel productModel = new ProductModel();
    											productModel.setCUSTOMER(jobClient.getString("CUST_CODE")+" - "+jobClient.getString("CUST_NAME"));
    											productModel.setINVOICE(jobInvoice.getString("INV_BOOK_NO")); 
    											productModel.setPRODUCT_SEQ(jobProduct.getString("PRODUCT_SEQ"));
    											productModel.setPRODUCT_CODE(jobProduct.getString("PRODUCT_CODE"));
    											productModel.setPRODUCT_NAME(jobProduct.getString("PRODUCT_NAME"));
    											productModel.setWARE_CODE(jobProduct.getString("WARE_CODE"));
    											productModel.setWARE_ZONE(jobProduct.getString("WARE_ZONE"));
    											//productModel.setUNIT_CODE(jobProduct.getString("UNIT_CODE"));
    											productModel.setUNIT(jobProduct.getString("UNIT"));
    											productModel.setQTY(jobProduct.getString("QTY"));
    											productModel.setQTY_PICKED(jobProduct.getString("QTY_PICKED"));
    											productModel.setQTY_RETURN(jobProduct.getString("QTY_RETURN"));
    											productModel.setPROBLEM_COMPLAIN(jobProduct.getString("PROBLEM_COMPLAIN"));
    											productModel.setPROBLEM_REMARK(jobProduct.getString("PROBLEM_REMARK"));
    											productList.add(productModel);  
    								
    										}
    										invoiceModel.setProductList(productList);
    									}		
    								}
    								else if(prdCnt == 1)
    								{
    									// for one record
    									JSONObject json_PrdData = jobInvoice.getJSONObject("ArrayOfProductData");
    									if (json_PrdData != null && json_PrdData.length() > 0) {
    										ProductModel productModel = new ProductModel();
    										productModel.setCUSTOMER(jobClient.getString("CUST_CODE")+" - "+jobClient.getString("CUST_NAME"));
    										productModel.setINVOICE(jobInvoice.getString("INV_BOOK_NO")); 
    										productModel.setPRODUCT_SEQ(json_PrdData.getString("PRODUCT_SEQ"));
    										productModel.setPRODUCT_CODE(json_PrdData.getString("PRODUCT_CODE"));
    										productModel.setPRODUCT_NAME(json_PrdData.getString("PRODUCT_NAME"));
    										productModel.setWARE_CODE(json_PrdData.getString("WARE_CODE"));
    										productModel.setWARE_ZONE(json_PrdData.getString("WARE_ZONE"));
    										//productModel.setUNIT_CODE(json_PrdData.getString("UNIT_CODE"));
    										productModel.setUNIT(json_PrdData.getString("UNIT"));
    										productModel.setQTY(json_PrdData.getString("QTY"));
    										productModel.setQTY_PICKED(json_PrdData.getString("QTY_PICKED"));
    										productModel.setQTY_RETURN(json_PrdData.getString("QTY_RETURN"));
    										productModel.setPROBLEM_COMPLAIN(json_PrdData.getString("PROBLEM_COMPLAIN"));
    										productModel.setPROBLEM_REMARK(json_PrdData.getString("PROBLEM_REMARK"));
    										productList.add(productModel); 
    										invoiceModel.setProductList(productList);
    									}
                         		
    								}
    								
    								customerModel.setInvoiceList(invoiceList);
    		                   
    							}
    						}
    					}
    					else if(invCnt == 1)
    					{
    						// for one record
    					
    						JSONObject json_InvData = jobClient.getJSONObject("ArrayOfInvoiceData");
    						if (json_InvData != null && json_InvData.length() > 0) {
        					
        						InvoiceModel invoiceModel = new InvoiceModel();
                         		invoiceModel.setCOMPANY(json_InvData.getString("COMPANY"));
                         		invoiceModel.setTRANSACTION_TYPE(json_InvData.getString("TRANSACTION_TYPE"));
                         		invoiceModel.setINV_BOOK(json_InvData.getString("INV_BOOK"));
                         		invoiceModel.setINV_NO(json_InvData.getString("INV_NO"));
                         		invoiceModel.setINV_BOOK_NO(json_InvData.getString("INV_BOOK_NO"));
                         		invoiceModel.setSTAFF_CODE(json_InvData.getString("STAFF_CODE"));
                         		invoiceModel.setINV_AMOUNT(json_InvData.getString("INV_AMOUNT"));
                         		invoiceModel.setPAYMENT_TYPE(json_InvData.getString("PAYMENT_TYPE"));
                         		invoiceModel.setJOB_ORDER(json_InvData.getString("JOB_ORDER"));
                         		invoiceModel.setINV_BARCODE("");
                         		invoiceModel.setINV_STATUS(json_InvData.getString("DELIVER_STATUS"));
                         		invoiceModel.setINV_SCAN(json_InvData.getString("INV_SCAN"));
                         		invoiceModel.setINV_PROBLEM(json_InvData.getString("PROBLEM_NOTE"));
                         		invoiceModel.setINV_LATITUDE(json_InvData.getString("LATITUDE"));
                         		invoiceModel.setINV_LONGITUDE(json_InvData.getString("LONGITUDE"));
                         		invoiceModel.setINV_BRANCH(json_InvData.getString("BRANCH_CODE"));
                         		invoiceModel.setINV_EXPECTDTIME(json_InvData.getString("EXPECTED_TIME"));
                         		invoiceList.add(invoiceModel);
                         		
        						ArrayList<ProductModel> productList = new ArrayList<ProductModel>();
        						int prdCnt = Integer.parseInt(json_InvData.getString("TOTAL_PRODUCT"));
        						if(prdCnt > 1){
        						JSONArray jaProducts = json_InvData.getJSONArray("ArrayOfProductData");
        						if (jaProducts != null && jaProducts.length() > 0) {
        							for (int k = 0; k < jaProducts.length(); k++) {
        								JSONObject jobProduct = jaProducts.getJSONObject(k);
        								
        								ProductModel productModel = new ProductModel();
                                 		productModel.setCUSTOMER(jobClient.getString("CUST_CODE")+" - "+jobClient.getString("CUST_NAME"));
                                 		productModel.setINVOICE(json_InvData.getString("INV_BOOK_NO")); 
                                 		productModel.setPRODUCT_SEQ(jobProduct.getString("PRODUCT_SEQ"));
                                 		productModel.setPRODUCT_CODE(jobProduct.getString("PRODUCT_CODE"));
                                 		productModel.setPRODUCT_NAME(jobProduct.getString("PRODUCT_NAME"));
                                 		productModel.setWARE_CODE(jobProduct.getString("WARE_CODE"));
                                 		productModel.setWARE_ZONE(jobProduct.getString("WARE_ZONE"));
                                 		//productModel.setUNIT_CODE(jobProduct.getString("UNIT_CODE"));
                                 		productModel.setUNIT(jobProduct.getString("UNIT"));
                                 		productModel.setQTY(jobProduct.getString("QTY"));
                                 		productModel.setQTY_PICKED(jobProduct.getString("QTY_PICKED"));
                                 		productModel.setQTY_RETURN(jobProduct.getString("QTY_RETURN"));
                                 		productModel.setPROBLEM_COMPLAIN(jobProduct.getString("PROBLEM_COMPLAIN"));
                                 		productModel.setPROBLEM_REMARK(jobProduct.getString("PROBLEM_REMARK"));
                                 		productList.add(productModel);  
        								
        							}
        							invoiceModel.setProductList(productList);
        						}		
        						}
        						else if(prdCnt == 1)
        						{
        							// for one record
        							JSONObject json_PrdData = json_InvData.getJSONObject("ArrayOfProductData");
        							if (json_PrdData != null && json_PrdData.length() > 0) {
    								ProductModel productModel = new ProductModel();
                             		productModel.setCUSTOMER(jobClient.getString("CUST_CODE")+" - "+jobClient.getString("CUST_NAME"));
                             		productModel.setINVOICE(json_InvData.getString("INV_BOOK_NO")); 
                             		productModel.setPRODUCT_SEQ(json_PrdData.getString("PRODUCT_SEQ"));
                             		productModel.setPRODUCT_CODE(json_PrdData.getString("PRODUCT_CODE"));
                             		productModel.setPRODUCT_NAME(json_PrdData.getString("PRODUCT_NAME"));
                             		productModel.setWARE_CODE(json_PrdData.getString("WARE_CODE"));
                             		productModel.setWARE_ZONE(json_PrdData.getString("WARE_ZONE"));
                             		//productModel.setUNIT_CODE(json_PrdData.getString("UNIT_CODE"));
                             		productModel.setUNIT(json_PrdData.getString("UNIT"));
                             		productModel.setQTY(json_PrdData.getString("QTY"));
                             		productModel.setQTY_PICKED(json_PrdData.getString("QTY_PICKED"));
                             		productModel.setQTY_RETURN(json_PrdData.getString("QTY_RETURN"));
                             		productModel.setPROBLEM_COMPLAIN(json_PrdData.getString("PROBLEM_COMPLAIN"));
                             		productModel.setPROBLEM_REMARK(json_PrdData.getString("PROBLEM_REMARK"));
                             		productList.add(productModel); 
                             		invoiceModel.setProductList(productList);
        							}
                             		
        						}
        								
        						customerModel.setInvoiceList(invoiceList);
        		
        				}
					}
    				 customerList.add(customerModel);
    				 }
    			}
    		}
    			else if(clientDataCount == 1){		
        			JSONObject jaClientsOne = json2.getJSONObject("ClientData"); 
        			if (jaClientsOne != null && jaClientsOne.length() > 0) {
//        				for (int i = 0; i < jaClients.length(); i++) {
//        					JSONObject jobClient = jaClients.getJSONObject(i);
        					JSONObject jobClient = json2.getJSONObject("ClientData");
        					CustomerModel customerModel = new CustomerModel();
        					customerModel.setCUST_BRANCH(jobClient.getString("CUST_CODE") + jobClient.getString("BRANCH_CODE"));
        					customerModel.setCUST_CODE(jobClient.getString("CUST_CODE"));
        					customerModel.setCUST_NAME(jobClient.getString("CUST_NAME"));
        					customerModel.setBRANCH_CODE(jobClient.getString("BRANCH_CODE"));
        					customerModel.setBRANCH_NAME(jobClient.getString("BRANCH_NAME"));
        					customerModel.setTOTAL_INVOICE(jobClient.getString("TOTAL_INVOICE"));
        					customerModel.setTOTAL_REMARK(jobClient.getString("TOTAL_REMARKS"));
        					customerModel.setCOMPLETE_STATUS("");
        					customerModel.setCASH_STATUS("N");
        					customerModel.setCHECKIN("N");
        					int invCnt = Integer.parseInt(jobClient.getString("TOTAL_INVOICE"));
        					ArrayList<InvoiceModel> invoiceList = new ArrayList<InvoiceModel>();
        					if(invCnt > 1){
        						JSONArray jaInvoice = jobClient.getJSONArray("ArrayOfInvoiceData");
        						if (jaInvoice != null && jaInvoice.length() > 0) {
        							for (int j = 0; j < jaInvoice.length(); j++) {
        								JSONObject jobInvoice = jaInvoice.getJSONObject(j);
        								InvoiceModel invoiceModel = new InvoiceModel();
        								invoiceModel.setCOMPANY(jobInvoice.getString("COMPANY"));
        								invoiceModel.setTRANSACTION_TYPE(jobInvoice.getString("TRANSACTION_TYPE"));
        								invoiceModel.setINV_BOOK(jobInvoice.getString("INV_BOOK"));
        								invoiceModel.setINV_NO(jobInvoice.getString("INV_NO"));
        								invoiceModel.setINV_BOOK_NO(jobInvoice.getString("INV_BOOK_NO"));
        								invoiceModel.setSTAFF_CODE(jobInvoice.getString("STAFF_CODE"));
        								invoiceModel.setINV_AMOUNT(jobInvoice.getString("INV_AMOUNT"));
        								invoiceModel.setPAYMENT_TYPE(jobInvoice.getString("PAYMENT_TYPE"));
        								invoiceModel.setJOB_ORDER(jobInvoice.getString("JOB_ORDER"));
        								invoiceModel.setINV_BARCODE("");
        								invoiceModel.setINV_STATUS(jobInvoice.getString("DELIVER_STATUS"));
        								invoiceModel.setINV_SCAN(jobInvoice.getString("INV_SCAN"));
        								invoiceModel.setINV_PROBLEM(jobInvoice.getString("PROBLEM_NOTE"));
        								invoiceModel.setINV_LATITUDE(jobInvoice.getString("LATITUDE"));
        								invoiceModel.setINV_LONGITUDE(jobInvoice.getString("LONGITUDE"));
        								invoiceModel.setINV_BRANCH(jobInvoice.getString("BRANCH_CODE"));
        								invoiceModel.setINV_EXPECTDTIME(jobInvoice.getString("EXPECTED_TIME"));
        								invoiceList.add(invoiceModel);
                         		
        								ArrayList<ProductModel> productList = new ArrayList<ProductModel>();
        								int prdCnt = Integer.parseInt(jobInvoice.getString("TOTAL_PRODUCT"));
        								if(prdCnt > 1){
        									JSONArray jaProducts = jobInvoice.getJSONArray("ArrayOfProductData");
        									if (jaProducts != null && jaProducts.length() > 0) {
        										for (int k = 0; k < jaProducts.length(); k++) {
        											JSONObject jobProduct = jaProducts.getJSONObject(k);
        								
        											ProductModel productModel = new ProductModel();
        											productModel.setCUSTOMER(jobClient.getString("CUST_CODE")+" - "+jobClient.getString("CUST_NAME"));
        											productModel.setINVOICE(jobInvoice.getString("INV_BOOK_NO")); 
        											productModel.setPRODUCT_SEQ(jobProduct.getString("PRODUCT_SEQ"));
        											productModel.setPRODUCT_CODE(jobProduct.getString("PRODUCT_CODE"));
        											productModel.setPRODUCT_NAME(jobProduct.getString("PRODUCT_NAME"));
        											productModel.setWARE_CODE(jobProduct.getString("WARE_CODE"));
        											productModel.setWARE_ZONE(jobProduct.getString("WARE_ZONE"));
        											//productModel.setUNIT_CODE(jobProduct.getString("UNIT_CODE"));
        											productModel.setUNIT(jobProduct.getString("UNIT"));
        											productModel.setQTY(jobProduct.getString("QTY"));
        											productModel.setQTY_PICKED(jobProduct.getString("QTY_PICKED"));
        											productModel.setQTY_RETURN(jobProduct.getString("QTY_RETURN"));
        											productModel.setPROBLEM_COMPLAIN(jobProduct.getString("PROBLEM_COMPLAIN"));
        											productModel.setPROBLEM_REMARK(jobProduct.getString("PROBLEM_REMARK"));
        											productList.add(productModel);  
        								
        										}
        										invoiceModel.setProductList(productList);
        									}		
        								}
        								else if(prdCnt == 1)
        								{
        									// for one record
        									JSONObject json_PrdData = jobInvoice.getJSONObject("ArrayOfProductData");
        									if (json_PrdData != null && json_PrdData.length() > 0) {
        										ProductModel productModel = new ProductModel();
        										productModel.setCUSTOMER(jobClient.getString("CUST_CODE")+" - "+jobClient.getString("CUST_NAME"));
        										productModel.setINVOICE(jobInvoice.getString("INV_BOOK_NO")); 
        										productModel.setPRODUCT_SEQ(json_PrdData.getString("PRODUCT_SEQ"));
        										productModel.setPRODUCT_CODE(json_PrdData.getString("PRODUCT_CODE"));
        										productModel.setPRODUCT_NAME(json_PrdData.getString("PRODUCT_NAME"));
        										productModel.setWARE_CODE(json_PrdData.getString("WARE_CODE"));
        										productModel.setWARE_ZONE(json_PrdData.getString("WARE_ZONE"));
        										//productModel.setUNIT_CODE(json_PrdData.getString("UNIT_CODE"));
        										productModel.setUNIT(json_PrdData.getString("UNIT"));
        										productModel.setQTY(json_PrdData.getString("QTY"));
        										productModel.setQTY_PICKED(json_PrdData.getString("QTY_PICKED"));
        										productModel.setQTY_RETURN(json_PrdData.getString("QTY_RETURN"));
        										productModel.setPROBLEM_COMPLAIN(json_PrdData.getString("PROBLEM_COMPLAIN"));
        										productModel.setPROBLEM_REMARK(json_PrdData.getString("PROBLEM_REMARK"));
        										productList.add(productModel); 
        										invoiceModel.setProductList(productList);
        									}
                             		
        								}
        								
        								customerModel.setInvoiceList(invoiceList);
        		                   
        							}
        						}
        					}
        					else if(invCnt == 1)
        					{
        						// for one record
        					
        						JSONObject json_InvData = jobClient.getJSONObject("ArrayOfInvoiceData");
        						if (json_InvData != null && json_InvData.length() > 0) {
            					
            						InvoiceModel invoiceModel = new InvoiceModel();
                             		invoiceModel.setCOMPANY(json_InvData.getString("COMPANY"));
                             		invoiceModel.setTRANSACTION_TYPE(json_InvData.getString("TRANSACTION_TYPE"));
                             		invoiceModel.setINV_BOOK(json_InvData.getString("INV_BOOK"));
                             		invoiceModel.setINV_NO(json_InvData.getString("INV_NO"));
                             		invoiceModel.setINV_BOOK_NO(json_InvData.getString("INV_BOOK_NO"));
                             		invoiceModel.setSTAFF_CODE(json_InvData.getString("STAFF_CODE"));
                             		invoiceModel.setINV_AMOUNT(json_InvData.getString("INV_AMOUNT"));
                             		invoiceModel.setPAYMENT_TYPE(json_InvData.getString("PAYMENT_TYPE"));
                             		invoiceModel.setJOB_ORDER(json_InvData.getString("JOB_ORDER"));
                             		invoiceModel.setINV_BARCODE("");
                             		invoiceModel.setINV_STATUS(json_InvData.getString("DELIVER_STATUS"));
                             		invoiceModel.setINV_SCAN(json_InvData.getString("INV_SCAN"));
                             		invoiceModel.setINV_PROBLEM(json_InvData.getString("PROBLEM_NOTE"));
                             		invoiceModel.setINV_LATITUDE(json_InvData.getString("LATITUDE"));
                             		invoiceModel.setINV_LONGITUDE(json_InvData.getString("LONGITUDE"));
                             		invoiceModel.setINV_BRANCH(json_InvData.getString("BRANCH_CODE"));
                             		invoiceModel.setINV_EXPECTDTIME(json_InvData.getString("EXPECTED_TIME"));
                             		invoiceList.add(invoiceModel);
                             		
            						ArrayList<ProductModel> productList = new ArrayList<ProductModel>();
            						int prdCnt = Integer.parseInt(json_InvData.getString("TOTAL_PRODUCT"));
            						if(prdCnt > 1){
            						JSONArray jaProducts = json_InvData.getJSONArray("ArrayOfProductData");
            						if (jaProducts != null && jaProducts.length() > 0) {
            							for (int k = 0; k < jaProducts.length(); k++) {
            								JSONObject jobProduct = jaProducts.getJSONObject(k);
            								
            								ProductModel productModel = new ProductModel();
                                     		productModel.setCUSTOMER(jobClient.getString("CUST_CODE")+" - "+jobClient.getString("CUST_NAME"));
                                     		productModel.setINVOICE(json_InvData.getString("INV_BOOK_NO")); 
                                     		productModel.setPRODUCT_SEQ(jobProduct.getString("PRODUCT_SEQ"));
                                     		productModel.setPRODUCT_CODE(jobProduct.getString("PRODUCT_CODE"));
                                     		productModel.setPRODUCT_NAME(jobProduct.getString("PRODUCT_NAME"));
                                     		productModel.setWARE_CODE(jobProduct.getString("WARE_CODE"));
                                     		productModel.setWARE_ZONE(jobProduct.getString("WARE_ZONE"));
                                     		//productModel.setUNIT_CODE(jobProduct.getString("UNIT_CODE"));
                                     		productModel.setUNIT(jobProduct.getString("UNIT"));
                                     		productModel.setQTY(jobProduct.getString("QTY"));
                                     		productModel.setQTY_PICKED(jobProduct.getString("QTY_PICKED"));
                                     		productModel.setQTY_RETURN(jobProduct.getString("QTY_RETURN"));
                                     		productModel.setPROBLEM_COMPLAIN(jobProduct.getString("PROBLEM_COMPLAIN"));
                                     		productModel.setPROBLEM_REMARK(jobProduct.getString("PROBLEM_REMARK"));
                                     		productList.add(productModel);  
            								
            							}
            							invoiceModel.setProductList(productList);
            						}		
            						}
            						else if(prdCnt == 1)
            						{
            							// for one record
            							JSONObject json_PrdData = json_InvData.getJSONObject("ArrayOfProductData");
            							if (json_PrdData != null && json_PrdData.length() > 0) {
        								ProductModel productModel = new ProductModel();
                                 		productModel.setCUSTOMER(jobClient.getString("CUST_CODE")+" - "+jobClient.getString("CUST_NAME"));
                                 		productModel.setINVOICE(json_InvData.getString("INV_BOOK_NO")); 
                                 		productModel.setPRODUCT_SEQ(json_PrdData.getString("PRODUCT_SEQ"));
                                 		productModel.setPRODUCT_CODE(json_PrdData.getString("PRODUCT_CODE"));
                                 		productModel.setPRODUCT_NAME(json_PrdData.getString("PRODUCT_NAME"));
                                 		productModel.setWARE_CODE(json_PrdData.getString("WARE_CODE"));
                                 		productModel.setWARE_ZONE(json_PrdData.getString("WARE_ZONE"));
                                 		//productModel.setUNIT_CODE(json_PrdData.getString("UNIT_CODE"));
                                 		productModel.setUNIT(json_PrdData.getString("UNIT"));
                                 		productModel.setQTY(json_PrdData.getString("QTY"));
                                 		productModel.setQTY_PICKED(json_PrdData.getString("QTY_PICKED"));
                                 		productModel.setQTY_RETURN(json_PrdData.getString("QTY_RETURN"));
                                 		productModel.setPROBLEM_COMPLAIN(json_PrdData.getString("PROBLEM_COMPLAIN"));
                                 		productModel.setPROBLEM_REMARK(json_PrdData.getString("PROBLEM_REMARK"));
                                 		productList.add(productModel); 
                                 		invoiceModel.setProductList(productList);
            							}
                                 		
            						}
            								
            						customerModel.setInvoiceList(invoiceList);
            		
            				}
    					}
        				 customerList.add(customerModel);
        			}	 
        } else if (envelope.bodyIn instanceof SoapFault) { // SoapFault = FAILURE
    		SoapFault soapFault = (SoapFault) envelope.bodyIn;
    		throw new Exception(soapFault.getMessage());
    	}
    		
    	


            Log.d(Constants.LOGD,"CUSTOMER SIZE : "+ customerList.size());
            for(int l=0; l<customerList.size(); l++) {
            	Log.d(Constants.LOGD,"CUSTOMER CODE : "+ customerList.get(l).getCUST_CODE());
            	ArrayList<InvoiceModel> invoiceList = customerList.get(l).getInvoiceList();
            	for(int m=0; m<invoiceList.size(); m++) {
            		Log.d(Constants.LOGD,"INV BOOK NO : "+ invoiceList.get(m).getINV_BOOK_NO());
            		ArrayList<ProductModel> productList = invoiceList.get(m).getProductList();
            		for(int n=0; n<productList.size(); n++) {
                		Log.d(Constants.LOGD,"PRODUCT CODE : "+ productList.get(n).getPRODUCT_CODE());
                	}
            	}
            }
            
                        
  
       for(int i=0;i<customerList.size();i++) {
        	List<String> expectedList= new ArrayList<String>();
        	CustomerModel customer  = customerList.get(i);
        	boolean cashStatus = false;
			for(int j=0;j<customer.getInvoiceList().size();j++) {
				boolean ck = false;
				for(int k=0;k<expectedList.size();k++) {
					if(customer.getInvoiceList().get(j).getINV_EXPECTDTIME().equalsIgnoreCase(expectedList.get(k).toString())) {
						ck = true;
						break;
					}
				}
				if(!ck) {
					expectedList.add(customer.getInvoiceList().get(j).getINV_EXPECTDTIME());
				}
				
				if(customer.getInvoiceList().get(j).getPAYMENT_TYPE().equalsIgnoreCase("CASH")) {
					if(!cashStatus) {
						cashStatus = true;
					}
				}
			}
			
			
			if(expectedList.size() == 0) {
				customerList.get(i).setEXPECTDTIME("");
			}else if(expectedList.size() == 1) {
				customerList.get(i).setEXPECTDTIME(expectedList.get(0).toString());
			}else {
				customerList.get(i).setEXPECTDTIME("");
			}

			if(cashStatus) {
				customerList.get(i).setCASH_STATUS("Y");
			}
       }
       
            /*
            for(int i=0;i<customerList.size();i++) {
            	boolean ck = false;
            	boolean ck1 = false;
            	CustomerModel customer  = customerList.get(i);
            	for(int j=0;j<customer.getInvoiceList().size();j++) {
            		if(customer.getInvoiceList().get(j).getINV_EXPECTDTIME().equalsIgnoreCase(expectedList.get(k).toString())) {
            			
            		}
                    }*/
    		return customerList;
        }catch(UnknownHostException e) {
        	throw e;
        }
        catch(TimeoutException e) {
         	throw e;
		}
        catch(Exception e) {
            throw e;
        }
	}
	
	
	
	public ArrayList<RouteJob> getRouteJob(String username, String deviceID, String compCode, String inRouteCode, String inDeliverySeq, String inDeliveryDate) throws UnknownHostException,Exception {
			final String NAMESPACE = "http://tempuri.org/";
			final String SOAP_ACTION = "http://tempuri.org/GetRouteJob";
			final String METHOD_NAME = "GetRouteJob";

			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
			
			DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
			DateFormat outputFormat = new SimpleDateFormat("yyyyMMdd");
			Date date = inputFormat.parse(inDeliveryDate);
			String outputDateStr = outputFormat.format(date);
			System.out.println(outputDateStr);		
			inDeliveryDate = outputDateStr;
			
//			inDeliveryDate = "20160418";
			//inDeliverySeq = "1";
			request.addProperty("inUsername", username);
			request.addProperty("inDeviceID", deviceID);
			request.addProperty("inCompany", compCode);
			request.addProperty("inRouteCode", inRouteCode);
			request.addProperty("inDeliverySeq", inDeliverySeq);
			request.addProperty("inDeliveryDate", inDeliveryDate);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;

			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(Constants.URLJSON);
			androidHttpTransport.debug = true;
        	ArrayList<RouteJob> responseList = new ArrayList<RouteJob>();

		
        try
        {		
            	androidHttpTransport.call(SOAP_ACTION, envelope);
        		SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
        		System.out.println(response);

        		String responseJSON=response.toString();
       
        		
        		if(responseJSON != null)
        		{
        			try 
                    {
                        JSONObject parent = new JSONObject(responseJSON);
                        JSONArray taskDetails = parent.getJSONArray("result");

                        for(int i=0; i < taskDetails.length(); i++)
                        {
                        	RouteJob routeJob = new RouteJob();
	    	          			routeJob.setPOSITION(String.valueOf(i));
	    	             		routeJob.setCOMPANY(Utilities.getServiceString(taskDetails.getJSONObject(i).getString("COMPANY").toString()));
	    	             		routeJob.setTRANSACTION_TYPE(Utilities.getServiceString(taskDetails.getJSONObject(i).getString("TRANSACTION_TYPE").toString()));
	    	             		routeJob.setINV_BOOK(Utilities.getServiceString(taskDetails.getJSONObject(i).getString("INV_BOOK").toString()));
	    	             		routeJob.setINV_NO(Utilities.getServiceString(taskDetails.getJSONObject(i).getString("INV_NO").toString()));
	    	             		routeJob.setINV_BOOK_NO(Utilities.getServiceString(taskDetails.getJSONObject(i).getString("INV_BOOK_NO").toString()));
	    	             		routeJob.setCUST_CODE(Utilities.getServiceString(taskDetails.getJSONObject(i).getString("CUST_CODE").toString()));
	    	             		routeJob.setCUST_NAME(Utilities.getServiceString(taskDetails.getJSONObject(i).getString("CUST_NAME").toString()));
	    	             		routeJob.setJOB_ORDER(Utilities.getServiceString(taskDetails.getJSONObject(i).getString("JOB_ORDER").toString()));
	    	             		responseList.add(routeJob);
         
                        }
                    }  catch (JSONException e) 
                        {
                        Log.e("Json Error", "Error: " + e.toString());
                            e.printStackTrace();
                        }

        		} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault = FAILURE
        		SoapFault soapFault = (SoapFault) envelope.bodyIn;
        		throw new Exception(soapFault.getMessage());
        	}   
            
            return responseList;
        }catch(UnknownHostException e) {
        	throw e;
		}catch(Exception e) {
            throw e;            
        }
	}
	
	
	// Not called from anywhere
	public ArrayList<HashMap<String, String>> getCustomer(String username, String deviceID, String compCode, String inRouteCode, String inDeliverySeq, String inDeliveryDate) throws UnknownHostException,Exception {
		final String NAMESPACE = "http://tempuri.org/";
		final String SOAP_ACTION = "http://tempuri.org/GetCustomer";
		final String METHOD_NAME = "GetCustomer";
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

		DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat outputFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = inputFormat.parse(inDeliveryDate);
		String outputDateStr = outputFormat.format(date);
		System.out.println(outputDateStr);		
		inDeliveryDate = outputDateStr;
		
//		inDeliveryDate = "20160418";
		request.addProperty("inUsername", username);
		request.addProperty("inDeviceID", deviceID);
		request.addProperty("inCompany", compCode);
		request.addProperty("inRouteCode", inRouteCode);
		request.addProperty("inDeliverySeq", inDeliverySeq);
		request.addProperty("inDeliveryDate", inDeliveryDate);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(Constants.URLJSON);
		androidHttpTransport.debug = true;
		
        try
        {	
        	androidHttpTransport.call(SOAP_ACTION, envelope);
    		SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
    		System.out.println(response);

    		String responseJSON=response.toString();

    		JSONArray jarray =new JSONArray(responseJSON);


    		System.out.println(request);
    		System.out.println(responseJSON);
    		ArrayList<HashMap<String, String>> responseList = new ArrayList<HashMap<String, String>>();

                if (jarray.length() > 0) { // SoapObject = SUCCESS
                	Log.e("response", "response=" + response.toString());
                	for (int i = 0; i < jarray.length(); i++) {
                  			HashMap<String, String> map;
      	             		map = new HashMap<String, String>();
      	             		map.put("CUST_CODE", Utilities.getServiceString(jarray.getJSONObject(i).getString("CUST_CODE").toString()));
      	             		;
      	             		map.put("CUST_NAME", Utilities.getServiceString(jarray.getJSONObject(i).getString("CUST_NAME").toString()));
      	             		;
      	             		map.put("TOTAL_CUST", Utilities.getServiceString(jarray.getJSONObject(i).getString("TOTAL_CUST").toString()));
      	             		;
      	             		map.put("TOTAL_INVOICE", Utilities.getServiceString(jarray.getJSONObject(i).getString("TOTAL_INVOICE").toString()));
  	             		responseList.add(map);
  	              }
        	} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault = FAILURE
        		SoapFault soapFault = (SoapFault) envelope.bodyIn;
        		throw new Exception(soapFault.getMessage());
        	}   
            
            return responseList;
        }catch(UnknownHostException e) {
        	throw e;
		}catch(Exception e) {
            throw e;            
        }
	}
	
	// Need to test output - not used
	public String updatePosition(String username, String deviceID, String compCode, String custCode, String branchCode, String lat, String lon) throws UnknownHostException,Exception {
			final String NAMESPACE = "http://tempuri.org/";
			final String SOAP_ACTION = "http://tempuri.org/UpdatePosition";
			final String METHOD_NAME = "UpdatePosition";

			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
			request.addProperty("inUsername", username);
			request.addProperty("inDeviceID", deviceID);
			request.addProperty("inCompany", compCode);
			request.addProperty("inCust", custCode);
			request.addProperty("inBranchSeq", branchCode);
			request.addProperty("inLat", lat);
			request.addProperty("inLng", lon);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;

			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(Constants.URLJSON);
			androidHttpTransport.debug = true;
		
        try
        {	
        	androidHttpTransport.call(SOAP_ACTION, envelope);
    		SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
    		System.out.println(response);
        	
            androidHttpTransport.call(Constants.NAMESPACE+METHOD_NAME, envelope);
            Log.d(Constants.LOGD, "------request updatePosition------------"+androidHttpTransport.requestDump);
            Log.d(Constants.LOGD, "------response updatePosition------------"+androidHttpTransport.responseDump);

            if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
            	SoapObject soapObject = (SoapObject) envelope.bodyIn;
        	} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault = FAILURE
        		SoapFault soapFault = (SoapFault) envelope.bodyIn;
        		throw new Exception(soapFault.getMessage());
        	}
            return envelope.getResponse().toString();
        }catch(UnknownHostException e) {
        	throw e;
		}catch(Exception e) {
            throw e;            
        }
	}
	
	// Need to call from check in button and test output 
		public ArrayList<HashMap<String, String>> CheckIn(String username, String deviceID, String compCode, String inCustCode, String inBranchCode, String inRouteCode, String inDeliverSeq, String inDeliveryDate, String lat, String lon) throws UnknownHostException,Exception {
			final String NAMESPACE = "http://tempuri.org/";
			final String SOAP_ACTION = "http://tempuri.org/CheckIn";
			String METHOD_NAME = "CheckIn";
			//inDeliveryDate = "12/09/2014 13:25";
			
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
			
			DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
			DateFormat outputFormat = new SimpleDateFormat("yyyyMMdd");
			Date date = inputFormat.parse(inDeliveryDate);
			String outputDateStr = outputFormat.format(date);
			System.out.println(outputDateStr);		
			inDeliveryDate = outputDateStr;
			
			//inDeliveryDate = "20140915";
//			inDeliveryDate = "20160418";
			request.addProperty("inUsername", username);
			request.addProperty("inDeviceID", deviceID);
			request.addProperty("inCompany", compCode);
			request.addProperty("inCust", inCustCode);
			request.addProperty("inBranchSeq", inBranchCode);
			request.addProperty("inRouteCode", inRouteCode);
			request.addProperty("inDeliverySeq", inDeliverSeq);
			request.addProperty("inDeliveryDate", inDeliveryDate);
			request.addProperty("inLat", lat);
			request.addProperty("inLng", lon);
			

	      
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;

			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(Constants.URLJSON);
			androidHttpTransport.debug = true;
			

			ArrayList<HashMap<String, String>> responseList = new ArrayList<HashMap<String, String>>();
	        try
	        {	
	        	androidHttpTransport.call(SOAP_ACTION, envelope);
	    		SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
	    		System.out.println(response);

	    		String responseJSON=response.toString();
	   
	    		
	    		if(responseJSON != null)
	    		{
	    			try 
	                {
	                    JSONObject parent = new JSONObject(responseJSON);
	                    JSONArray taskDetails = parent.getJSONArray("result");

	                    for(int i=0; i < taskDetails.length(); i++)
	                    {
	                    	HashMap<String, String> map;
	  	             		map = new HashMap<String, String>();
	  	             		map.put("FLAG",Utilities.getServiceString(taskDetails.getJSONObject(i).getString("FLAG").toString()));
	  	             		map.put("MSG",Utilities.getServiceString(taskDetails.getJSONObject(i).getString("MSG").toString()));
	  	             		responseList.add(map);

	                    }
	                }  catch (JSONException e) 
	                    {
	                    Log.e("Json Error", "Error: " + e.toString());
	                        e.printStackTrace();
	                    }

	    		}

	    		else if (envelope.bodyIn instanceof SoapFault) { // SoapFault = FAILURE
	        		SoapFault soapFault = (SoapFault) envelope.bodyIn;
	        		throw new Exception(soapFault.getMessage());
	        	}
	    		return responseList;
			
	        }catch(UnknownHostException e) {
	        	throw e;
			}catch(Exception e) {
	            throw e;            
	        }
		}
		
	
	// Need to test output -working
	public ArrayList<HashMap<String, String>> checkOut(String username, String deviceID, String compCode, String inCustCode, String inBranchCode, String inRouteCode, String inDeliverSeq, String inDeliveryDate) throws Exception {
		final String NAMESPACE = "http://tempuri.org/";
		final String SOAP_ACTION = "http://tempuri.org/CheckOut";
		String METHOD_NAME = "CheckOut";
		//inDeliveryDate = "12/09/2014 13:25";
		
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		
		DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat outputFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = inputFormat.parse(inDeliveryDate);
		String outputDateStr = outputFormat.format(date);
		System.out.println(outputDateStr);		
		inDeliveryDate = outputDateStr;
		
//		inDeliveryDate = "20160418";
		request.addProperty("inUsername", username);
		request.addProperty("inDeviceID", deviceID);
		request.addProperty("inCompany", compCode);
		request.addProperty("inCust", inCustCode);
		request.addProperty("inBranchSeq", inBranchCode);
		request.addProperty("inRouteCode", inRouteCode);
		request.addProperty("inDeliverySeq", inDeliverSeq);
		request.addProperty("inDeliveryDate", inDeliveryDate);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(Constants.URLJSON);
		androidHttpTransport.debug = true;

		ArrayList<HashMap<String, String>> responseList = new ArrayList<HashMap<String, String>>();
        try {
        	androidHttpTransport.call(SOAP_ACTION, envelope);
    		SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
    		System.out.println(response);

    		String responseJSON=response.toString();
   
    		
    		if(responseJSON != null)
    		{
    			try 
                {
                    JSONObject parent = new JSONObject(responseJSON);
                    JSONArray taskDetails = parent.getJSONArray("result");

                    for(int i=0; i < taskDetails.length(); i++)
                    {
                    	HashMap<String, String> map;
  	             		map = new HashMap<String, String>();
  	             		map.put("FLAG",Utilities.getServiceString(taskDetails.getJSONObject(i).getString("FLAG").toString()));
  	             		map.put("MSG",Utilities.getServiceString(taskDetails.getJSONObject(i).getString("MSG").toString()));
  	             		responseList.add(map);

                    }
                }  catch (JSONException e) 
                    {
                    Log.e("Json Error", "Error: " + e.toString());
                        e.printStackTrace();
                    }

    		}

    		else if (envelope.bodyIn instanceof SoapFault) { // SoapFault = FAILURE
        		SoapFault soapFault = (SoapFault) envelope.bodyIn;
        		throw new Exception(soapFault.getMessage());
        	}
    		return responseList;
        }catch(UnknownHostException e) {
        	throw e;
		}catch(Exception e) {
            throw e;            
        }
	}
	
/*	
	public String updateDeliverStatus(String inCompany, String inTransactionType, String inInvBook, String inInvNo, String inDeliverStatus, String inCashReceived, String inProblemNote,
									String inCustCode, String inBranchCode, String inLatitude, String inLongitude, String inCheckinTime) throws UnknownHostException,Exception {
		String METHOD_NAME = "UpdateDeliverStatus";
		request = new SoapObject(Constants.NAMESPACE, METHOD_NAME);		
		PropertyInfo property = new PropertyInfo();
		property.setName("inCompany");	property.setValue(inCompany);
		property.setType(String.class);
        request.addProperty(property);
        
        property = new PropertyInfo();
        property.setName("inTransactionType");	property.setValue(inTransactionType);
        property.setType(String.class);
        request.addProperty(property);
        
        property = new PropertyInfo();
        property.setName("inInvBook");	property.setValue(inInvBook);
        property.setType(String.class);
        request.addProperty(property);
        
        property = new PropertyInfo();
        property.setName("inInvNo");	property.setValue(inInvNo);
        property.setType(String.class);
        request.addProperty(property);
        
        property = new PropertyInfo();
        property.setName("inDeliverStatus");  property.setValue(inDeliverStatus);
        property.setType(String.class);
        request.addProperty(property);
        
        property = new PropertyInfo();
        property.setName("inCashReceived");	property.setValue(inCashReceived);
        property.setType(String.class);
        request.addProperty(property);
        
        property = new PropertyInfo();
        property.setName("inProblemNote");	property.setValue(inProblemNote);
        property.setType(String.class);
        request.addProperty(property);
        
        property = new PropertyInfo();
        property.setName("inCustCode");	property.setValue(inCustCode);
        property.setType(String.class);
        request.addProperty(property);
        
        property = new PropertyInfo();
        property.setName("inBranchCode");	property.setValue(inBranchCode);
        property.setType(String.class);
        request.addProperty(property);
        
        property = new PropertyInfo();
        property.setName("inLatitude");	property.setValue(inLatitude);
        property.setType(String.class);
        request.addProperty(property);
        
        property = new PropertyInfo();
        property.setName("inLongitude");	property.setValue(inLongitude);
        property.setType(String.class);
        request.addProperty(property);
        
        property = new PropertyInfo();
        property.setName("inCheckinTime");	property.setValue(inCheckinTime);
        property.setType(String.class);
        request.addProperty(property);
        
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        androidHttpTransport = new AndroidHttpTransport(Constants.URL);
        androidHttpTransport.debug = true;
        try
        {	

            androidHttpTransport.call(Constants.NAMESPACE+METHOD_NAME, envelope);
            Log.d(Constants.LOGD, "------request updateDeliverStatus------------"+androidHttpTransport.requestDump);
            Log.d(Constants.LOGD, "------response updateDeliverStatus------------"+androidHttpTransport.responseDump);

            if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
            	SoapObject soapObject = (SoapObject) envelope.bodyIn;
        	} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault = FAILURE
        		SoapFault soapFault = (SoapFault) envelope.bodyIn;
        		throw new Exception(soapFault.getMessage());
        	}
            return envelope.getResponse().toString();
        }catch(UnknownHostException e) {
        	throw e;
		}catch(Exception e) {
            throw e;            
        }
	}
*/
	public ArrayList<ProblemSet> getProblemList(String username, String deviceID, String compCode) throws UnknownHostException,Exception {
		final String NAMESPACE = "http://tempuri.org/";
		final String SOAP_ACTION = "http://tempuri.org/GetProblemList";
		final String METHOD_NAME = "GetProblemList";

		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		request.addProperty("inUsername", "\"\"");
		request.addProperty("inDeviceID", deviceID);
		request.addProperty("inCompany", compCode);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(Constants.URLJSON);
		androidHttpTransport.debug = true;
		ArrayList<ProblemSet> problemList = new ArrayList<ProblemSet>();

		try {
		androidHttpTransport.call(SOAP_ACTION, envelope);
		SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
		System.out.println(response);

		String responseJSON=response.toString();

		JSONArray jarray =new JSONArray(responseJSON);


		System.out.println(request);
		System.out.println(responseJSON);

            if (jarray.length() > 0) { // SoapObject = SUCCESS
            	Log.e("response", "response=" + response.toString());
            	for (int i = 0; i < jarray.length(); i++) {
					JSONObject jobClient = jarray.getJSONObject(i);
    					ProblemSet problemSetModel = new ProblemSet();
    					problemSetModel.setCOMPLAIN_CODE(jobClient.getString("COMPLAIN_CODE"));
    					problemSetModel.setREM_ID(jobClient.getString("REM_ID"));
    					problemSetModel.setCOMPLAIN_DESC(jobClient.getString("COMPLAIN_DESC"));
    					problemList.add(problemSetModel);
  	              }
  	         
        	} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault = FAILURE
        		SoapFault soapFault = (SoapFault) envelope.bodyIn;
        		throw new Exception(soapFault.getMessage());
        	}   
            return problemList;
        }catch(UnknownHostException e) {
        	throw e;
		}catch(Exception e) {
            throw e;            
        }
	}

	
	//new method to be called now
	public ArrayList<HashMap<String, String>> updateDeliverStatus(String username, String deviceID, String inCompany, int size,
			String inDeliveryDateNew, String inRoutingCode, String inDeliverySeqNew, String inCustCode, String inTRANSACTION_TYPE
			,String inINV_BOOK, String inINV_NO, ArrayList<HashMap<String, String>> problemListArr, String cash) throws UnknownHostException,Exception {
	
		final String NAMESPACE = "http://tempuri.org/";
		final String SOAP_ACTION = "http://tempuri.org/UpdateInvoiceProblem";
		String METHOD_NAME = "UpdateInvoiceProblem";
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		
		request.addProperty("inUsername", username);
		request.addProperty("inDeviceID", deviceID);
		request.addProperty("inCompany", inCompany);
		
		DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat outputFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = inputFormat.parse(inDeliveryDateNew);
		String outputDateStr = outputFormat.format(date);
		System.out.println(outputDateStr);		
		inDeliveryDateNew = outputDateStr;

		String json = "{\"INV_PROBLEM\":[";

		String jsonRow ;
		if(ILogisticsApplication.problemListArr != null && ILogisticsApplication.problemListArr.size() > 0)
		{
		
//		for (int i = 0; i < problemListArr.size(); i++) {
//			String jsonRow = "{";
			System.out.print(ILogisticsApplication.problemListArr.size());
		for (int i = 0; i < ILogisticsApplication.problemListArr.size(); i++) {
			jsonRow = "{";
//			if(ILogisticsApplication.problemListArr != null && ILogisticsApplication.problemListArr.size() > 0)
//			{
				jsonRow += "\"DELIVERY_DATE\":\""+inDeliveryDateNew+"\",";
				jsonRow += "\"ROUTE\":\""+ILogisticsApplication.problemListArr.get(i).get("ROUTE_CODE")+"\",";
				jsonRow += "\"DELIVERY_SEQ\":\""+inDeliverySeqNew+"\",";
				jsonRow += "\"CUST_CODE\":\""+ILogisticsApplication.problemListArr.get(i).get("CUST_CODE")+"\",";
				jsonRow += "\"TRANSACTION_TYPE\":\""+ILogisticsApplication.problemListArr.get(i).get("TRANSACTION_TYPE")+"\",";
				jsonRow += "\"INV_BOOK\":\""+ILogisticsApplication.problemListArr.get(i).get("INV_BOOK")+"\",";
				jsonRow += "\"INV_NO\":\""+ILogisticsApplication.problemListArr.get(i).get("INV_NO")+"\",";
				jsonRow += "\"PRODUCT_SEQ\":\""+ILogisticsApplication.problemListArr.get(i).get("PRODUCT_SEQ")+"\",";
				jsonRow += "\"PRODUCT_CODE\":\""+ILogisticsApplication.problemListArr.get(i).get("PRODUCT_CODE")+"\",";
				jsonRow += "\"COMPLAIN_CODE\":\""+ILogisticsApplication.problemListArr.get(i).get("COMPLAIN_CODE")+"\",";
				jsonRow += "\"REM_ID\":\""+ILogisticsApplication.problemListArr.get(i).get("REM_ID")+"\",";
				jsonRow += "\"QTY\":\""+ILogisticsApplication.problemListArr.get(i).get("QTY_RETURN")+"\",";		
				jsonRow += "\"UNIT\":\""+ILogisticsApplication.problemListArr.get(i).get("UNIT")+"\",";
				jsonRow += "\"REMARKS\":\""+ILogisticsApplication.problemListArr.get(i).get("REMARKS")+"\",";
				jsonRow += "\"WARE_CODE\":\""+ILogisticsApplication.problemListArr.get(i).get("WARE_CODE")+"\",";
				jsonRow += "\"WARE_ZONE\":\""+ILogisticsApplication.problemListArr.get(i).get("WARE_ZONE")+"\",";
				jsonRow += "\"CASH_AMOUNT\":\""+cash+"\"},";
				json += jsonRow;
		}
	}
		else
			{
			System.out.print(problemListArr.size());
			for (int i = 0; i < problemListArr.size(); i++) {
				jsonRow = "{";
				jsonRow += "\"DELIVERY_DATE\":\""+inDeliveryDateNew+"\",";
				jsonRow += "\"ROUTE\":\""+inRoutingCode+"\",";
				jsonRow += "\"DELIVERY_SEQ\":\""+inDeliverySeqNew+"\",";
				jsonRow += "\"CUST_CODE\":\""+inCustCode+"\",";
				jsonRow += "\"TRANSACTION_TYPE\":\""+inTRANSACTION_TYPE+"\",";
				jsonRow += "\"INV_BOOK\":\""+inINV_BOOK+"\",";
				jsonRow += "\"INV_NO\":\""+inINV_NO+"\",";
				jsonRow += "\"PRODUCT_SEQ\":\""+problemListArr.get(i).get("PRODUCT_SEQ")+"\",";
				jsonRow += "\"PRODUCT_CODE\":\""+problemListArr.get(i).get("PRODUCT_CODE")+"\",";
				jsonRow += "\"COMPLAIN_CODE\":\""+""+"\",";
				jsonRow += "\"REM_ID\":\""+""+"\",";
				jsonRow += "\"QTY\":\""+problemListArr.get(i).get("QTY_RETURN")+"\",";		
				jsonRow += "\"UNIT\":\""+problemListArr.get(i).get("UNIT")+"\",";
				jsonRow += "\"REMARKS\":\""+""+"\",";
				jsonRow += "\"WARE_CODE\":\""+problemListArr.get(i).get("WARE_CODE")+"\",";
				jsonRow += "\"WARE_ZONE\":\""+problemListArr.get(i).get("WARE_ZONE")+"\",";
				jsonRow += "\"CASH_AMOUNT\":\""+cash+"\"},";
				json += jsonRow;
			}
		}
			
//		}
		
		//remove last "," from json string
		json = json.substring(0, json.length() - 1);
		json += "]}";
		 
		request.addProperty("inJson", json);
		
        
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(Constants.URLJSON);
		androidHttpTransport.debug = true;
		

		ArrayList<HashMap<String, String>> responseList = new ArrayList<HashMap<String, String>>();
        try
        {	
        	androidHttpTransport.call(SOAP_ACTION, envelope);
    		SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
    		System.out.println(response);

    		String responseJSON=response.toString();
   
    		
    		if(responseJSON != null)
    		{
    			try 
                {
                    JSONObject parent = new JSONObject(responseJSON);
                    JSONArray taskDetails = parent.getJSONArray("result");
                    String flag = taskDetails.getJSONObject(0).getString("FLAG").toString();
//                    String msg = parent.getString("MSG");
                    if(!flag.equals("1")) {
                    	return responseList;
                    }
                    for(int i=0; i < taskDetails.length(); i++)
                    {
                    	HashMap<String, String> map;
  	             		map = new HashMap<String, String>();
  	             		map.put("FLAG",Utilities.getServiceString(taskDetails.getJSONObject(i).getString("FLAG").toString()));
  	             		map.put("MSG",Utilities.getServiceString(taskDetails.getJSONObject(i).getString("MSG").toString()));
  	             		responseList.add(map);

                    }
                }  catch (JSONException e) 
                    {
                    Log.e("Json Error", "Error: " + e.toString());
                        e.printStackTrace();
                    }

    		}

    		else if (envelope.bodyIn instanceof SoapFault) { // SoapFault = FAILURE
        		SoapFault soapFault = (SoapFault) envelope.bodyIn;
        		throw new Exception(soapFault.getMessage());
        	}
    		return responseList;
        }catch(UnknownHostException e) {
        	throw e;
		}catch(Exception e) {
            throw e;            
        }
	}

	public String updateScan(String inDeliveryDate, String inRouteCode, String inDeliverySeq, HashMap<String,String> list) throws UnknownHostException,Exception {
		String METHOD_NAME = "UpdateScan";
		request = new SoapObject(Constants.NAMESPACE, METHOD_NAME);
		
		DocumentBuilderFactory dFact = DocumentBuilderFactory.newInstance();
        DocumentBuilder build = dFact.newDocumentBuilder();
        Document doc = build.newDocument();

        Element root = doc.createElement("ListInfo");
        doc.appendChild(root);
  
        Iterator<String> it = list.keySet().iterator();
        while(it.hasNext()){
            String key = it.next();
            String value = list.get(key);
            //System.out.println("By Key :Key : "+key+"   Value: "+value);            
			Element Details = doc.createElement("Details");
	        root.appendChild(Details);
	        /*--------------------------------------------*/
	        Element ele = doc.createElement("COMPANY");
	        ele.appendChild(doc.createTextNode(valueHolder.getCompanyCode()));
	        Details.appendChild(ele);        
	        ele = doc.createElement("DELIVERY_DATE");
	        ele.appendChild(doc.createTextNode(inDeliveryDate));
	        Details.appendChild(ele);
	        ele = doc.createElement("ROUTE_CODE");
	        ele.appendChild(doc.createTextNode(inRouteCode));
	        Details.appendChild(ele);
	        ele = doc.createElement("SEQ");
	        ele.appendChild(doc.createTextNode(inDeliverySeq));
	        Details.appendChild(ele);        
	        ele = doc.createElement("INV_BOOK_NO");
	        ele.appendChild(doc.createTextNode(value));
	        Details.appendChild(ele);
		}
		
		TransformerFactory transfac = TransformerFactory.newInstance();
		Transformer trans = transfac.newTransformer();
		trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);
		DOMSource source = new DOMSource(doc);
		trans.transform(source, result);
		String xmlString = sw.toString();
		
		PropertyInfo property = new PropertyInfo();
		property.setName("inList");
		property.setValue(xmlString);
		property.setType(String.class);
        request.addProperty(property);       

        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        androidHttpTransport = new AndroidHttpTransport(Constants.URL);
        androidHttpTransport.debug = true;
        try
        {
            androidHttpTransport.call(Constants.NAMESPACE+METHOD_NAME, envelope);
            Log.d(Constants.LOGD, "------request updateScan------------"+androidHttpTransport.requestDump);
            Log.d(Constants.LOGD, "------response updateScan------------"+androidHttpTransport.responseDump);

            if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
            	SoapObject soapObject = (SoapObject) envelope.bodyIn;
        	} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault = FAILURE
        		SoapFault soapFault = (SoapFault) envelope.bodyIn;
        		throw new Exception(soapFault.getMessage());
        	}

            return envelope.getResponse().toString();
        }catch(UnknownHostException e) {
        	throw e;
		}catch(Exception e) {
            throw e;
        }
	}	
	
	// Need to test output - currently no call
	public String updateInvoiceScan(String username, String deviceID, String inCompany, String inRouteCode, String inDeliverSeq, String inDeliveryDate, String inInvBookNo) throws UnknownHostException,Exception {
			final String NAMESPACE = "http://tempuri.org/";
			final String SOAP_ACTION = "http://tempuri.org/UpdateInvoiceScan";
			final String METHOD_NAME = "UpdateInvoiceScan";

			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
			
			DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
			DateFormat outputFormat = new SimpleDateFormat("yyyyMMdd");
			Date date = inputFormat.parse(inDeliveryDate);
			String outputDateStr = outputFormat.format(date);
			System.out.println(outputDateStr);		
			inDeliveryDate = outputDateStr;
			
			request.addProperty("inUsername", username);
			request.addProperty("inDeviceID", deviceID);
			request.addProperty("inCompany", inCompany);
			request.addProperty("inRouteCode", inRouteCode);
			request.addProperty("inDeliverySeq", inDeliverSeq);
			request.addProperty("inDeliveryDate", inDeliveryDate);
			request.addProperty("inBarcode", inInvBookNo);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;

			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(Constants.URLJSON);
			androidHttpTransport.debug = true;
		
        try
        {
        	androidHttpTransport.call(SOAP_ACTION, envelope);
    		SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
    		System.out.println(response);

//    		String responseJSON=response.toString();
//
//    		JSONArray jarray =new JSONArray(responseJSON);
    		
            androidHttpTransport.call(Constants.NAMESPACE+METHOD_NAME, envelope);
            Log.d(Constants.LOGD, "------request updateInvoiceScan------------"+androidHttpTransport.requestDump);
            Log.d(Constants.LOGD, "------response updateInvoiceScan------------"+androidHttpTransport.responseDump);

            if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
            	SoapObject soapObject = (SoapObject) envelope.bodyIn;
        	} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault = FAILURE
        		SoapFault soapFault = (SoapFault) envelope.bodyIn;
        		throw new Exception(soapFault.getMessage());
        	}
            return envelope.getResponse().toString();
        }catch(UnknownHostException e) {
        	throw e;
		}catch(Exception e) {
            throw e;
        }
	}

	public String updateInvoiceProblem(ArrayList<HashMap<String, String>> problemList) throws UnknownHostException,Exception {
		String METHOD_NAME = "UpdateInvoiceProblem";
		request = new SoapObject(Constants.NAMESPACE, METHOD_NAME);
		
		DocumentBuilderFactory dFact = DocumentBuilderFactory.newInstance();
        DocumentBuilder build = dFact.newDocumentBuilder();
        Document doc = build.newDocument();

        Element root = doc.createElement("ProblemInfo");
        doc.appendChild(root);
  
		for(int i=0; i<problemList.size(); i++) {
			Log.d(Constants.LOGD, "Route Code : "+problemList.get(i).get("ROUTE_CODE"));
			Log.d(Constants.LOGD, "Cust Code : "+problemList.get(i).get("CUST_CODE"));
			Log.d(Constants.LOGD, "Company : "+problemList.get(i).get("COMPANY"));
			Log.d(Constants.LOGD, "Transaction Type : "+problemList.get(i).get("TRANSACTION_TYPE"));
			Log.d(Constants.LOGD, "Inv Book : "+problemList.get(i).get("INV_BOOK"));
			Log.d(Constants.LOGD, "Inv No : "+problemList.get(i).get("INV_NO"));
			Log.d(Constants.LOGD, "Staff Code : "+problemList.get(i).get("STAFF_CODE"));
			Log.d(Constants.LOGD, "Inv Seq : "+problemList.get(i).get("PRODUCT_SEQ"));
			Log.d(Constants.LOGD, "Product Code : "+problemList.get(i).get("PRODUCT_CODE"));
			Log.d(Constants.LOGD, "Ware Code : "+problemList.get(i).get("WARE_CODE"));
			Log.d(Constants.LOGD, "Ware Zone : "+problemList.get(i).get("WARE_ZONE"));
			Log.d(Constants.LOGD, "Qty Return : "+problemList.get(i).get("QTY_RETURN"));
			Log.d(Constants.LOGD, "Unit : "+problemList.get(i).get("UNIT"));
			Log.d(Constants.LOGD, "Complain Code : "+problemList.get(i).get("COMPLAIN_CODE"));
			Log.d(Constants.LOGD, "Rem ID : "+problemList.get(i).get("REM_ID"));
			Log.d(Constants.LOGD, "Remarks : "+problemList.get(i).get("REMARKS"));
	    	
			Log.d(Constants.LOGD, "-----------------------------------------------------");
			
			Element Details = doc.createElement("Details");
	        root.appendChild(Details);
	        /*--------------------------------------------*/
	        Element ele = doc.createElement("ROUTE_CODE");
	        ele.appendChild(doc.createTextNode(problemList.get(i).get("ROUTE_CODE")));
	        Details.appendChild(ele);
	        ele = doc.createElement("CUST_CODE");
	        ele.appendChild(doc.createTextNode(problemList.get(i).get("CUST_CODE")));
	        Details.appendChild(ele);
	        ele = doc.createElement("COMPANY");
	        ele.appendChild(doc.createTextNode(problemList.get(i).get("COMPANY")));
	        Details.appendChild(ele);
	        ele = doc.createElement("TRANSACTION_TYPE");
	        ele.appendChild(doc.createTextNode(problemList.get(i).get("TRANSACTION_TYPE")));
	        Details.appendChild(ele);
	        ele = doc.createElement("INV_BOOK");
	        ele.appendChild(doc.createTextNode(problemList.get(i).get("INV_BOOK")));
	        Details.appendChild(ele);
	        ele = doc.createElement("INV_NO");
	        ele.appendChild(doc.createTextNode(problemList.get(i).get("INV_NO")));
	        Details.appendChild(ele);
	        ele = doc.createElement("STAFF_CODE");
	        ele.appendChild(doc.createTextNode(problemList.get(i).get("STAFF_CODE")));
	        Details.appendChild(ele);
	        ele = doc.createElement("PRODUCT_SEQ");
	        ele.appendChild(doc.createTextNode(problemList.get(i).get("PRODUCT_SEQ")));
	        Details.appendChild(ele);
	        ele = doc.createElement("PRODUCT_CODE");
	        ele.appendChild(doc.createTextNode(problemList.get(i).get("PRODUCT_CODE")));
	        Details.appendChild(ele);
	        ele = doc.createElement("WARE_CODE");
	        ele.appendChild(doc.createTextNode(problemList.get(i).get("WARE_CODE")));
	        Details.appendChild(ele);
	        ele = doc.createElement("WARE_ZONE");
	        ele.appendChild(doc.createTextNode(problemList.get(i).get("WARE_ZONE")));
	        Details.appendChild(ele);
	        ele = doc.createElement("QTY_RETURN");
	        ele.appendChild(doc.createTextNode(problemList.get(i).get("QTY_RETURN")));
	        Details.appendChild(ele);
	        ele = doc.createElement("UNIT");
	        ele.appendChild(doc.createTextNode(problemList.get(i).get("UNIT")));
	        Details.appendChild(ele);
	        ele = doc.createElement("COMPLAIN_CODE");
	        ele.appendChild(doc.createTextNode(problemList.get(i).get("COMPLAIN_CODE")));
	        Details.appendChild(ele);
	        ele = doc.createElement("REM_ID");
	        ele.appendChild(doc.createTextNode(problemList.get(i).get("REM_ID")));
	        Details.appendChild(ele);
	        ele = doc.createElement("REMARKS");
	        ele.appendChild(doc.createTextNode(problemList.get(i).get("REMARKS")));
	        Details.appendChild(ele);
    
		}
		
		TransformerFactory transfac = TransformerFactory.newInstance();
		Transformer trans = transfac.newTransformer();
		trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		//trans.setOutputProperty(OutputKeys.INDENT, "yes");
		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);
		DOMSource source = new DOMSource(doc);
		trans.transform(source, result);		
		//String xmlString = sw.toString().replace("&", "&#x26;").replace("<", "&lt;").replace(">", "&gt;").replace("'","&#39;").replace("\"","&quot;");
		String xmlString = sw.toString();
		
		Log.d(Constants.LOGD, xmlString);
		
		PropertyInfo property = new PropertyInfo();
		property.setName("inProblem");
		property.setValue(xmlString);
		property.setType(String.class);
        request.addProperty(property);       

        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        androidHttpTransport = new AndroidHttpTransport(Constants.URL);
        androidHttpTransport.debug = true;
        try
        {
            androidHttpTransport.call(Constants.NAMESPACE+METHOD_NAME, envelope);
            Log.d(Constants.LOGD, "------request updateInvoiceProblem------------"+androidHttpTransport.requestDump);
            Log.d(Constants.LOGD, "------response updateInvoiceProblem------------"+androidHttpTransport.responseDump);

            if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
            	SoapObject soapObject = (SoapObject) envelope.bodyIn;
        	} else if (envelope.bodyIn instanceof SoapFault) { // SoapFault = FAILURE
        		SoapFault soapFault = (SoapFault) envelope.bodyIn;
        		throw new Exception(soapFault.getMessage());
        	}

            return envelope.getResponse().toString();
        }catch(UnknownHostException e) {
        	throw e;
		}catch(Exception e) {
            throw e;
        }
	}

	public ArrayList<HashMap<String, String>> getIncomingInvoice(Context context) {
		ArrayList<HashMap<String, String>> arrList = new ArrayList<HashMap<String, String>>();
		request = new SoapObject(Constants.NAMESPACE, Constants.METHOD_NAME_INCOME_INVOICE);
		
		PropertyInfo pi = new PropertyInfo();
        pi.setName("strCusID");
        pi.setValue("1");
        pi.setType(String.class);
        request.addProperty(pi);
        
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        androidHttpTransport = new AndroidHttpTransport(Constants.URL);
        androidHttpTransport.debug = true;
        try
        {
            androidHttpTransport.call(Constants.SOAP_ACTION_INCOME_INVOICE, envelope);
            
            Log.d(Constants.LOGD, "------request getIncomingInvoice------------"+androidHttpTransport.requestDump);
            Log.d(Constants.LOGD, "------response getIncomingInvoice------------"+androidHttpTransport.responseDump);
            
            SoapObject response = (SoapObject)envelope.getResponse();
            
            if(response != null) {
	            for (int i = 0; i < response.getPropertyCount(); i++) {
	              Object property = response.getProperty(i);
	              	if (property instanceof SoapObject) {	              		
	              		SoapObject soapObject= (SoapObject) property;
            			 HashMap<String, String> map;
	             		 map = new HashMap<String, String>();
	             		 map.put("Name", soapObject.getProperty("Name").toString());
	             		 Log.d(Constants.LOGD,"CUST_CODE : "+soapObject.getProperty("Name").toString());
	             		 arrList.add(map);
	              }
	            }
            }
            
        }catch(UnknownHostException hostException) {
        	hostException.printStackTrace();
        	//return context.getString(R.string.unknownhost);
        	// "Service is temporarily stopped!, please try later";
        }catch(SocketTimeoutException timeoutException) {
        	timeoutException.printStackTrace();
        	//return context.getString(R.string.sockettimeout);
        	// "Service timeout!, please check the data connection";
        }catch(Exception e) {
            e.printStackTrace();
            //return getClass().getName()+" : "+ e.getClass().getName()+" : "+e.getMessage() +" "+Constants.unknownException;
        }
		return arrList;
	}
	
	
	public ArrayList<HashMap<String, String>> getIncomingInvoice1(Context context) {
		ArrayList<HashMap<String, String>> arrList = new ArrayList<HashMap<String, String>>();
		request = new SoapObject(Constants.NAMESPACE, "GetXML");
		
		PropertyInfo pi = new PropertyInfo();
        pi.setName("strCusID");
        pi.setValue("1");
        pi.setType(String.class);
        request.addProperty(pi);
        
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        androidHttpTransport = new AndroidHttpTransport(Constants.URL);
        androidHttpTransport.debug = true;
        try
        {
            androidHttpTransport.call("http://tempuri.org/GetXML", envelope);
            
            Log.d(Constants.LOGD, "------request getIncomingInvoice------------"+androidHttpTransport.requestDump);
            Log.d(Constants.LOGD, "------response getIncomingInvoice------------"+androidHttpTransport.responseDump);
            
            SoapObject response = (SoapObject)envelope.getResponse();
            //Log.d(Constants.LOGD,envelope.getResponse().toString());
            //Log.d(Constants.LOGD,envelope.bodyIn.toString());
            //Log.d(Constants.LOGD,"CUST_CODE : "+response.getProperty("ArrayOfClientData").toString());            
            SoapObject soapObject= (SoapObject) response.getProperty("ArrayOfClientData");           
            Log.d(Constants.LOGD, ""+soapObject.getPropertyCount());
            for (int i = 0; i < soapObject.getPropertyCount(); i++) {
                Object property = soapObject.getProperty(i);
                if (property instanceof SoapObject) {
              		//SoapObject soapObject1= (SoapObject) property;
             		//Log.d(Constants.LOGD,"CUST_CODE : "+soapObject1.getProperty("ClientData").toString());       		 
             		//Object property1 = soapObject1.getProperty("ClientData");
             		SoapObject soapObject1= (SoapObject) property;
             		Log.d(Constants.LOGD,"Name : "+soapObject1.getProperty("Name").toString());
             		Log.d(Constants.LOGD,"ID : "+soapObject1.getProperty("ID").toString());
             		
             		SoapObject soapObjectChild= (SoapObject) soapObject1.getProperty("Child");
             		Log.d(Constants.LOGD,"Child : "+soapObjectChild.getProperty("Name").toString());
	
              }
            }
            /*
            Object property = response.getProperty("Names");
            SoapObject soapObject= (SoapObject) property;
            Log.d(Constants.LOGD,"CUST_CODE :" +soapObject.getProperty("Name"));
            */
            /*
            SoapObject soapObject = (SoapObject) envelope.bodyIn;
            Log.d(Constants.LOGD,"CUST_CODE : "+soapObject.toString());
            */	
            /*
            for (int i = 0; i < response.getPropertyCount(); i++) {
              Object property = response.getProperty(i);
              	if (property instanceof SoapObject) {	              		
              		SoapObject soapObject= (SoapObject) property;
             		 Log.d(Constants.LOGD,"CUST_CODE : "+soapObject.getProperty("Name").toString());
              }
            }
            */
            /*
            for (int i = 0; i < response.getPropertyCount(); i++) {
            	//Object property = response.getProperty(i);
            	Log.d(Constants.LOGD,"CUST_CODE : "+response.getProperty(i));
            }
            */
            /*
            if (envelope.bodyIn instanceof SoapObject) { // SoapObject = SUCCESS
            	SoapObject soapObject = (SoapObject) envelope.bodyIn;
            	SoapObject peopleNode = (SoapObject) response.getProperty("People");
            } else if (envelope.bodyIn instanceof SoapFault) { // SoapFault = FAILURE
            	SoapFault soapFault = (SoapFault) envelope.bodyIn;
            	throw new Exception(soapFault.getMessage());
            }
            */
            
            /*
            if(response != null) {
            	Log.d(Constants.LOGD,"Count : "+response.getPropertyCount());
	            for (int i = 0; i < response.getPropertyCount(); i++) {
	              Object property = response.getProperty(i);
	              	if (property instanceof SoapObject) {	              		
	              		SoapObject soapObject= (SoapObject) property;
	             		Log.d(Constants.LOGD,"CUST_CODE : "+soapObject.getProperty("ClientData").toString());
	              }
	            }
            }
            */
        }catch(UnknownHostException hostException) {
        	hostException.printStackTrace();
        	//return context.getString(R.string.unknownhost);
        	// "Service is temporarily stopped!, please try later";
        }catch(SocketTimeoutException timeoutException) {
        	timeoutException.printStackTrace();
        	//return context.getString(R.string.sockettimeout);
        	// "Service timeout!, please check the data connection";
        }catch(Exception e) {
            e.printStackTrace();
            //return getClass().getName()+" : "+ e.getClass().getName()+" : "+e.getMessage() +" "+Constants.unknownException;
        }
		return arrList;
	}

	public ArrayList<HashMap<String, String>> CheckInAndOut(CheckInCheckOut dbRecord) throws Exception {
		final String NAMESPACE = "http://tempuri.org/";
		final String SOAP_ACTION = "http://tempuri.org/CheckIn";
		String METHOD_NAME = "CheckInAndOut";
		//inDeliveryDate = "12/09/2014 13:25";

		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

		DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat outputFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = inputFormat.parse(dbRecord.deliveryDate);
		String outputDateStr = outputFormat.format(date);
		System.out.println(outputDateStr);
		String inDeliveryDate = outputDateStr;

		//inDeliveryDate = "20140915";
//			inDeliveryDate = "20160418";
		request.addProperty("inUsername", dbRecord.username);
		request.addProperty("inDeviceID", dbRecord.deviceId);
		request.addProperty("inCompany", dbRecord.companyCode);
		request.addProperty("inCust", dbRecord.customerCode);
		request.addProperty("inBranchSeq", dbRecord.branchCode);
		request.addProperty("inRouteCode", dbRecord.routeCode);
		request.addProperty("inDeliverySeq", dbRecord.deliverySequence);
		request.addProperty("inDeliveryDate", inDeliveryDate);
		request.addProperty("inLat", dbRecord.latitude);
		request.addProperty("inLng", dbRecord.longitude);

		if (Long.parseLong(dbRecord.checkInTime) > 0) {
			request.addProperty("inCheckIn", getFormattedDateTimeInYyyyMmDdHh24Mi(dbRecord.checkInTime));
		} else {
			request.addProperty("inCheckIn", "");
		}

		if (Long.parseLong(dbRecord.checkOutTime) > 0) {
			request.addProperty("inCheckOut", getFormattedDateTimeInYyyyMmDdHh24Mi(dbRecord.checkOutTime));
		} else {
			request.addProperty("inCheckOut", "");
		}

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(Constants.URLJSON);
		androidHttpTransport.debug = true;

		ArrayList<HashMap<String, String>> responseList = new ArrayList<HashMap<String, String>>();
		try {
			androidHttpTransport.call(SOAP_ACTION, envelope);
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
			System.out.println(response);

			String responseJSON=response.toString();

			if (responseJSON != null) {
				try {
					JSONObject parent = new JSONObject(responseJSON);
					JSONArray taskDetails = parent.getJSONArray("result");

					for(int i=0; i < taskDetails.length(); i++) {
						HashMap<String, String> map;
						map = new HashMap<String, String>();
						map.put("FLAG",Utilities.getServiceString(taskDetails.getJSONObject(i).getString("FLAG").toString()));
						map.put("MSG",Utilities.getServiceString(taskDetails.getJSONObject(i).getString("MSG").toString()));
						responseList.add(map);
					}
				} catch (JSONException e) {
					Log.e("Json Error", "Error: " + e.toString());
					e.printStackTrace();
				}
			}

			else if (envelope.bodyIn instanceof SoapFault) { // SoapFault = FAILURE
				SoapFault soapFault = (SoapFault) envelope.bodyIn;
				throw new Exception(soapFault.getMessage());
			}
			return responseList;

		} catch(UnknownHostException e) {
			throw e;
		} catch(Exception e) {
			throw e;
		}
	}

	private String getFormattedDateTimeInYyyyMmDdHh24Mi(String dateStr) {
		Date dateToBeParsed = null;
		if (!Strings.isNullOrEmpty(dateStr) && Long.parseLong(dateStr) > 0) {
			dateToBeParsed = new Date(Long.parseLong(dateStr));
		}

		// if still date object is null then initialise it with current system date
		if (dateToBeParsed == null) {
			dateToBeParsed = Calendar.getInstance().getTime();
		}

		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm");
		String formattedDated = dateFormat.format(dateToBeParsed);
		return formattedDated;
	}
}
