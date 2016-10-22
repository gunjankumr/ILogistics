package com.compass.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.compass.model.ClientData;
import com.compass.model.InvoiceData;
import com.compass.model.ProductData;

import android.app.Activity;
import android.os.Bundle;

public class TestActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//this is how we can fetch the current lat and long of the device wherever its required
		String currentLat = ""+ILogisticsApplication.currentLatitude;
		String currentLongitude = ""+ILogisticsApplication.currentLongitude;
		
		//This is how given json can be parsed and stored
		String strJSON = "{\"result\":{\"ClientData\":[{\"CUS_ID\":\"JB_20140822_EB1F_1_FFL2902\",\"CUST_CODE\":\"FFL2902\",\"CUST_NAME\":\"คุณ ปนัดดา  พุ่มพวง\",\"BRANCH_CODE\":\"0\",\"BRANCH_NAME\":\"ปนัดดา  พุ่มพวง\",\"ROUTE_CODE\":\"EB1F\",\"DELIVERY_SEQ\":\"1\",\"TOTAL_INVOICE\":\"1\",\"ArrayOfInvoiceData\":[{\"CUS_ID\":\"JB_20140822_EB1F_1_FFL2902\",\"INV_ID\":\"JB_IN_IN57_139697\",\"COMPANY\":\"JB\",\"TRANSACTION_TYPE\":\"IN\",\"INV_BOOK\":\"IN57\",\"INV_NO\":\"139697\",\"INV_BOOK_NO\":\"IN57/139697\",\"DELIVERY_SEQ\":\"1\",\"INV_SCAN\":\"N\",\"DELIVER_STATUS\":\"Y\",\"CASH_RECEIVED\":\"0.00\",\"STAFF_CODE\":\"F1904\",\"DOC_DATE\":\"20140822\",\"PAYMENT_TYPE\":\"TR\",\"BRANCH_CODE\":\"0\",\"INV_AMOUNT\":\"2300.50\",\"DELIVERY_TO\":\"โลตัส บ้านฉาง คุณดาว 01-4525262 คุณอุ้ย 09-5127879 01-4525262,09-5127879\",\"EXPECTED_TIME\":\"All Day\",\"ArrayOfProductData\":[{\"INV_ID\":\"JB_IN_IN57_139697\",\"PRODUCT_SEQ\":\"1\",\"PRODUCT_CODE\":\"PLCUNB05210018\",\"PRODUCT_NAME\":\"Chicken Nugget (1kgx10pk / Box) (718,722)\",\"WARE_CODE\":\"SP\",\"WARE_ZONE\":\"001\",\"UNIT_CODE\":\"BX\",\"UNIT\":\"BOX\",\"QTY\":\"1.00\",\"QTY_PICKED\":\"1.00\",\"QTY_RETURN\":\"0\"},{\"INV_ID\":\"JB_IN_IN57_139697\",\"PRODUCT_SEQ\":\"2\",\"PRODUCT_CODE\":\"PLCUNB05210068\",\"PRODUCT_NAME\":\"Fried Chicken Drum 10kg/bag (713)\",\"WARE_CODE\":\"SP\",\"WARE_ZONE\":\"001\",\"UNIT_CODE\":\"BG\",\"UNIT\":\"BAG\",\"QTY\":\"1.00\",\"QTY_PICKED\":\"1.00\",\"QTY_RETURN\":\"0\"}]}]}]}}";
		try {
			ArrayList<ClientData> arrListClient = parseJSONResponse(strJSON);
			System.out.println("put the break point here and expect the arrListClient client above "+arrListClient.size());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public ArrayList<ClientData> parseJSONResponse(String strJSON) throws JSONException {
		ArrayList<ClientData> arrListClients = new ArrayList<ClientData>();
		JSONObject job = new JSONObject(strJSON);
		JSONArray jaClients = job.getJSONObject("result").getJSONArray("ClientData");
		if (jaClients != null && jaClients.length() > 0) {
			for (int i = 0; i < jaClients.length(); i++) {
				JSONObject jobClient = jaClients.getJSONObject(i);
				ClientData objClientData = new ClientData();
				objClientData.setCustID(jobClient.getString("CUS_ID"));
				objClientData.setCustCode(jobClient.getString("CUST_CODE"));
				objClientData.setCustName(jobClient.getString("CUST_NAME"));
				objClientData.setBranchCode(jobClient.getString("BRANCH_CODE"));
				objClientData.setBranchName(jobClient.getString("BRANCH_NAME"));
				objClientData.setRouteCode(jobClient.getString("ROUTE_CODE"));
				objClientData.setDeliverySeq(jobClient.getString("DELIVERY_SEQ"));
				objClientData.setTotalInvoice(jobClient.getString("TOTAL_INVOICE"));
				
				ArrayList<InvoiceData> arrListInvoice = new ArrayList<InvoiceData>();
				JSONArray jaInvoice = jobClient.getJSONArray("ArrayOfInvoiceData");
				if (jaInvoice != null && jaInvoice.length() > 0) {
					for (int j = 0; j < jaInvoice.length(); j++) {
						JSONObject jobInvoice = jaInvoice.getJSONObject(j);
						InvoiceData objInvoice = new InvoiceData();
						objInvoice.setCustomerID(jobInvoice.getString("CUS_ID"));
						objInvoice.setInvoiceID(jobInvoice.getString("INV_ID"));
						objInvoice.setCompany(jobInvoice.getString("COMPANY"));
						objInvoice.setTransactionType(jobInvoice.getString("TRANSACTION_TYPE"));
						objInvoice.setInvoiceBook(jobInvoice.getString("INV_BOOK"));
						objInvoice.setInvoiceNo(jobInvoice.getString("INV_NO"));
						objInvoice.setInvoiceBookNo(jobInvoice.getString("INV_BOOK_NO"));
						objInvoice.setDeliverySeq(jobInvoice.getString("DELIVERY_SEQ"));
						objInvoice.setInvoiceScan(jobInvoice.getString("INV_SCAN"));
						objInvoice.setDeliverStatus(jobInvoice.getString("DELIVER_STATUS"));
						objInvoice.setCashReceived(jobInvoice.getString("CASH_RECEIVED"));
						objInvoice.setStaffCode(jobInvoice.getString("STAFF_CODE"));
						objInvoice.setDocDate(jobInvoice.getString("DOC_DATE"));
						objInvoice.setPaymentType(jobInvoice.getString("PAYMENT_TYPE"));
						objInvoice.setBranchCode(jobInvoice.getString("BRANCH_CODE"));
						objInvoice.setInvoiceAmount(jobInvoice.getString("INV_AMOUNT"));
						objInvoice.setDeliveryTo(jobInvoice.getString("DELIVERY_TO"));
						objInvoice.setExpectedTime(jobInvoice.getString("EXPECTED_TIME"));
						
						ArrayList<ProductData> arrListProducts = new ArrayList<ProductData>();
						JSONArray jaProducts = jobInvoice.getJSONArray("ArrayOfProductData");
						if (jaProducts != null && jaProducts.length() > 0) {
							for (int k = 0; k < jaProducts.length(); k++) {
								JSONObject jobProduct = jaProducts.getJSONObject(k);
								ProductData objProduct = new ProductData();
								objProduct.setInvoiceID(jobProduct.getString("INV_ID"));
								objProduct.setProductSeq(jobProduct.getString("PRODUCT_SEQ"));
								objProduct.setProductCode(jobProduct.getString("PRODUCT_CODE"));
								objProduct.setProductName(jobProduct.getString("PRODUCT_NAME"));
								objProduct.setWareHouseCode(jobProduct.getString("WARE_CODE"));
								objProduct.setWareHouseZone(jobProduct.getString("WARE_ZONE"));
								objProduct.setUnitCode(jobProduct.getString("UNIT_CODE"));
								objProduct.setUnit(jobProduct.getString("UNIT"));
								objProduct.setQuantity(jobProduct.getString("QTY"));
								objProduct.setQuantityPicked(jobProduct.getString("QTY_PICKED"));
								objProduct.setQuantityReturn(jobProduct.getString("QTY_RETURN"));
								
								arrListProducts.add(objProduct);
							}
						}		
								
						objInvoice.setArrListProduct(arrListProducts);
						arrListInvoice.add(objInvoice);
					}
				}
				
				objClientData.setArrListInvoice(arrListInvoice);
				arrListClients.add(objClientData);
			}
		}
		
		return arrListClients;
	}

}
