package com.compass.activity;

/**
 * @author Paresh N. Mayani
 * http://www.technotalkative.com
 */

import com.google.common.base.Strings;

import com.compass.ilogistics.R;
import com.compass.model.CustomerModel;
import com.compass.model.InvoiceModel;
import com.compass.model.ProblemSet;
import com.compass.model.ProductModel;
import com.compass.model.Routing;
import com.compass.utils.ValueHolder;
import com.compass.utils.WebService;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

public class ProductProblemTableActivity extends DashBoardActivity {
	
	private ValueHolder valueHolder;
	private ProgressDialog dialog;
	//private GestureDetector gDetector;
	private AlertDialog alertDialog;
	
	//private ListView lstView;
	private TextView invDoc;
	private TextView btnComplete;
	private ProblemTask problemTask;
	private Spinner textComplain;
	
	private static final String SERVER_ERROR = "SERVER_ERROR";
	private static final String NETWORK_ERROR = "NETWORK_ERROR";
	private static final String CANCELLED = "CANCELLED";
	private static final String SUCCESS = "SUCCESS";
	
	private String serviceResponse = null;
	private ArrayList<HashMap<String, String>> serviceResponseList;
	private ArrayList<Routing> routingList = new ArrayList<Routing>();
	private CustomerModel customerModel;
	private InvoiceModel invoiceModel;

	private ArrayList<HashMap<String, String>> problemList = new ArrayList<HashMap<String, String>>();
	
	private String[] problemArr = {"PR-001", "PR-014","LG-008",""};
//	ArrayList<String> problemArr = new ArrayList<String>();


	private int[] colors = new int[] {0x30ffffff, 0x30f0f0f0};
	int compareVal;
	ArrayAdapter spinnerArrayAdapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_problem_table);
        setHeader(getString(R.string.app_name), true, false, true);
		btnHomeClickStatus(false);
        valueHolder = ValueHolder.getSingletonObject();
        invDoc = (TextView)findViewById(R.id.invDoc);
        btnComplete = (Button)findViewById(R.id.btnComplete);
        
        for(int i=0;i<valueHolder.getProblemList().size();i++) {
        	ProblemSet problemSet  = valueHolder.getProblemList().get(i); 
//        	problemArr.add(problemSet.getCOMPLAIN_DESC());
        }
        customerModel = valueHolder.getCustomerList().get(valueHolder.customerSelected);
        invoiceModel = (InvoiceModel) customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected());
        String status = customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getINV_STATUS();
        //if(customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getINV_STATUS().length() > 0) {
        if(status.equalsIgnoreCase("Y") || status.equalsIgnoreCase("W")) {
        	btnComplete.setVisibility(View.INVISIBLE);
        }
        if(valueHolder.getInvoiceSelected() == -1) {
        	invDoc.setText(customerModel.getInvoiceList().get(0).getINV_BOOK_NO());
        	valueHolder.setInvoiceSelected(0);
        }else {
        	invDoc.setText(customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getINV_BOOK_NO());
        }
        
        if(valueHolder.getLatitude().length() == 0) {
        	btnComplete.setVisibility(View.INVISIBLE);
        }
        
        String[] spinnerArray = new String[valueHolder.getProblemList().size()];
		for (int i = 0; i < spinnerArray.length; i++) {
			spinnerArray[i] = valueHolder.getProblemList().get(i).COMPLAIN_DESC;
		}
		spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        
        ArrayList<ProductModel> productList = new ArrayList<ProductModel>();
        //for(int i=0;i<6;i++) {
        	for(int j=0;j<customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getProductList().size();j++) {        	
        		 ProductModel product = new ProductModel();
    			 //product.setPOSITION(""+i);
        		 product.setPOSITION(""+j);
    			 product.setPRODUCT_CODE((customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getProductList().get(j)).getPRODUCT_CODE());
    			 product.setPRODUCT_NAME((customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getProductList().get(j)).getPRODUCT_NAME());
    			 product.setWARE_CODE((customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getProductList().get(j)).getWARE_CODE());
    			 product.setWARE_ZONE((customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getProductList().get(j)).getWARE_ZONE());
    			 product.setUNIT_CODE((customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getProductList().get(j)).getUNIT_CODE());
    			 product.setUNIT((customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getProductList().get(j)).getUNIT());
    			 product.setQTY((customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getProductList().get(j)).getQTY());
    			 product.setQTY_PICKED((customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getProductList().get(j)).getQTY_PICKED());
    			 product.setQTY_RETURN("");
    			 product.setPRODUCT_SEQ((customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getProductList().get(j)).getPRODUCT_SEQ());
    			 productList.add(product);
        	}
        //}
        
        genTable(productList);
        //getAllConvertView();

    }
    
    private class ViewHolder {
   	 	TextView text1;
		CheckBox text2;
		TextView text3;
		TextView text4;
		TextView text5;
		TextView text6;
   }
    
    public void genTable(ArrayList<ProductModel> productList) {
    	for(int i=0;i<productList.size();i++) {
    		//View convertView =  ((LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.product_problem_table_item, null);	
    		LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		View convertView = vi.inflate(R.layout.product_problem_table_item, null);
    		/*
    		ViewHolder holder = new ViewHolder();
    		holder.text1 = (TextView) convertView.findViewById(R.id.textView1);
    		holder.text2 = (CheckBox) convertView.findViewById(R.id.textView2);
    		holder.text3 = (TextView) convertView.findViewById(R.id.textView3);
    		holder.text4 = (TextView) convertView.findViewById(R.id.textView4);
    		holder.text5 = (TextView) convertView.findViewById(R.id.textView5);
    		holder.text6 = (EditText) convertView.findViewById(R.id.textView6);
    		convertView.setTag(holder);
    		*/
    		//convertView.setId(i);
    		
    		((TextView) convertView.findViewById(R.id.textView1)).setText(productList.get(i).getPRODUCT_CODE());
    		((CheckBox) convertView.findViewById(R.id.textView2)).setChecked(false);
    		((TextView) convertView.findViewById(R.id.textView3)).setText(productList.get(i).getPRODUCT_NAME());
    		((TextView) convertView.findViewById(R.id.textView5)).setText(productList.get(i).getQTY()+"  "+productList.get(i).getUNIT());
    		((EditText) convertView.findViewById(R.id.textView6)).setText("");
    		((TextView) convertView.findViewById(R.id.textUnit)).setText(productList.get(i).getUNIT_CODE());
    		((TextView) convertView.findViewById(R.id.textSeq)).setText(productList.get(i).getPRODUCT_SEQ());
    		((TextView) convertView.findViewById(R.id.textWareCode)).setText(productList.get(i).getWARE_CODE());
    		((TextView) convertView.findViewById(R.id.textWareZone)).setText(productList.get(i).getWARE_ZONE());
    		((TextView) convertView.findViewById(R.id.textPosition)).setText(productList.get(i).getPOSITION());
    		
    		
    		((CheckBox) convertView.findViewById(R.id.textView2)).setOnClickListener(new OnClickListener() {
	        	public void onClick(View v) {
	        		final int viewId = v.getId();
	        		//final int position = viewId-col8;
                    //final TextView txtProduct = (TextView)findViewById(col1+position);
	        			LinearLayout parentView = findConvertView(v);
	        			CheckBox cb = (CheckBox) v;
	        			if(cb.isChecked()) {
	        				((TextView) parentView.findViewById(R.id.textViewQty)).setVisibility(View.VISIBLE);
	        				((EditText) parentView.findViewById(R.id.textView6)).setVisibility(View.VISIBLE);
	        				((Spinner) parentView.findViewById(R.id.textComplain)).setVisibility(View.VISIBLE);
	        				((TextView) parentView.findViewById(R.id.textViewRemark)).setVisibility(View.VISIBLE);
	        				((EditText) parentView.findViewById(R.id.textRemarks)).setVisibility(View.VISIBLE);
	        				((EditText) parentView.findViewById(R.id.textView6)).setText("");
	        				
	        				Spinner textComplain = (Spinner) parentView.findViewById(R.id.textComplain);
	        				textComplain.setAdapter(spinnerArrayAdapter);
	        				
	        			}else {
	        				((EditText) parentView.findViewById(R.id.textView6)).setText("");
	        				((TextView) parentView.findViewById(R.id.textViewQty)).setVisibility(View.INVISIBLE);
	        				((EditText) parentView.findViewById(R.id.textView6)).setVisibility(View.INVISIBLE);
	        				((Spinner) parentView.findViewById(R.id.textComplain)).setVisibility(View.INVISIBLE);
	        				((TextView) parentView.findViewById(R.id.textViewRemark)).setVisibility(View.INVISIBLE);
	        				((EditText) parentView.findViewById(R.id.textRemarks)).setVisibility(View.INVISIBLE);
	        			}
	        			//ViewHolder holder1 = (ViewHolder) parentView.getTag();
	        			//Toast.makeText(getApplicationContext(), holder1.text6.getText().toString() , Toast.LENGTH_SHORT).show();
                    }
			});
    		
    		
    		/*
    		((EditText) convertView.findViewById(R.id.textView6)).setOnFocusChangeListener(new OnFocusChangeListener() {
    	           public void onFocusChange(View v, boolean hasFocus) {
    	               if (!hasFocus) {
    	            	   final int viewId = v.getId();
    	            	   EditText et = (EditText) v ; 
    	            	   LinearLayout parentView = findConvertView(v);
    	            	   //ViewHolder holder1 = (ViewHolder) parentView.getTag();
    	            	   //holder1.text6.setText(et.getText());
    	               }
    	           }
    	       });
			*/
    		
    		LinearLayout tab  = (LinearLayout)findViewById(R.id.tableLayout); 
    		convertView.setBackgroundColor(colors[(i % colors.length)]);
    		tab.addView(convertView);
    	}
    }
    
    
    public LinearLayout findConvertView(View v) {
        if(v==null) {
            return null;
        } else if(v instanceof LinearLayout) {        	
        	if(v.getId() == R.id.convertView) {
        		//Toast.makeText(getApplicationContext(), "Please insert receive quantity : "+v.getId() , Toast.LENGTH_SHORT).show();
        		return (LinearLayout) v;
        	}
        	return findConvertView((View)v.getParent());
        } else {
            return findConvertView((View)v.getParent());
        }
    }
    
    
    public void getAllConvertView() {
    	LinearLayout topView = ((LinearLayout)findViewById(R.id.tableLayout));   
    	int childcount = topView.getChildCount();
    	//Toast.makeText(getApplicationContext(), "Please insert receive quantity : "+childcount , Toast.LENGTH_SHORT).show();
    	problemList = new ArrayList<HashMap<String, String>>();
    	
    	
    	for (int i=0; i < childcount; i++) { 
    	      View v = topView.getChildAt(i);
    	      if(v.getId() == R.id.convertView) {   	    	
    	    	if(((CheckBox)v.findViewById(R.id.textView2)).isChecked()) {
    	    		// check whether QTY_RETURN is less than or not. If not return and empty problemList
	    	    	int positionCount = Integer.parseInt(((TextView)v.findViewById(R.id.textPosition)).getText().toString());
	    	    	float qty = 0;
	    	    	float qtyReturn = 0;
	    	    	try{
	    	    		qty = Float.valueOf(valueHolder.getCustomerList().get(valueHolder.customerSelected).getInvoiceList().get(valueHolder.getInvoiceSelected()).getProductList().get(positionCount).getQTY().toString());
	    	    	    qtyReturn = Float.valueOf(((EditText)v.findViewById(R.id.textView6)).getText().toString());
	    	   	    	} catch (NumberFormatException e) {
	    	    		   e.printStackTrace();
	    	    	}
	    	    	compareVal = Float.compare(qty,qtyReturn);
	    	    	System.out.println("qtyReturn " + qtyReturn + "qty " + qty);

	    	    	
	    	    	if(compareVal < 0){
	    	    	      System.out.println("First is grater");
	    	    	      if(problemList !=null && problemList.size() > 0 )
	    	    	      {
	    	    	    	  problemList.clear();
	    	    	      }
	  	        		valueHolder.getCustomerList().get(valueHolder.customerSelected).getInvoiceList().get(valueHolder.getInvoiceSelected()).setINV_STATUS("");

//		        		Toast.makeText(getApplicationContext(),R.string.qty_alert  , Toast.LENGTH_LONG).show();
		        		Toast toast = Toast.makeText(getApplicationContext(),R.string.qty_alert  , Toast.LENGTH_LONG);
		        		toast.setGravity(Gravity.CENTER, 0, 0);
		        		toast.show();

	    	    	      return;
	    	    	    }

    	    	    
	    	    	HashMap<String, String> map = new HashMap<String, String>();
	           		map.put("ROUTE_CODE", valueHolder.getRoutingCode());
	    	    	map.put("CUST_CODE", customerModel.getCUST_CODE());
	    	    	map.put("COMPANY", invoiceModel.getCOMPANY());
	    	    	map.put("TRANSACTION_TYPE", invoiceModel.getTRANSACTION_TYPE());
	    	    	map.put("INV_BOOK", invoiceModel.getINV_BOOK());
	    	    	map.put("INV_NO", invoiceModel.getINV_NO());
	    	    	map.put("STAFF_CODE", invoiceModel.getSTAFF_CODE());
	    	    	map.put("PRODUCT_SEQ", ((TextView)v.findViewById(R.id.textSeq)).getText().toString());
	    	    	map.put("PRODUCT_CODE", ((TextView)v.findViewById(R.id.textView1)).getText().toString());
	    	    	map.put("WARE_CODE", ((TextView)v.findViewById(R.id.textWareCode)).getText().toString());
	    	    	map.put("WARE_ZONE", ((TextView)v.findViewById(R.id.textWareZone)).getText().toString());
	    	    	map.put("QTY_RETURN", ((EditText)v.findViewById(R.id.textView6)).getText().toString());

					String unit = ((TextView)v.findViewById(R.id.textView5)).getText().toString();
					if (!Strings.isNullOrEmpty(unit) && unit.contains(" ")) {
						unit = unit.split(" ")[1];
					}
	    	    	map.put("UNIT", unit);
	    	    	//Toast.makeText(getApplicationContext(), "" +((Spinner)v.findViewById(R.id.textComplain)).getSelectedItemPosition(), Toast.LENGTH_SHORT).show();	    	    	
	    	    	ProblemSet selectedProbSet = valueHolder.getProblemList().get(((Spinner)v.findViewById(R.id.textComplain)).getSelectedItemPosition()); 
	    	    	String complain = selectedProbSet.COMPLAIN_DESC;
	    	    	String complainCodeRem = selectedProbSet.getCOMPLAIN_CODE();
	    			String remarkId = selectedProbSet.getREM_ID();
	    			remarkId = remarkId.substring(remarkId.indexOf("-"));
	    			complainCodeRem = complainCodeRem + remarkId;

	    	    	map.put("COMPLAIN_DESC", complain);
	    	    	map.put("COMPLAIN_CODE", selectedProbSet.COMPLAIN_CODE);
	    	    	map.put("REM_ID", selectedProbSet.REM_ID);
	    	    	map.put("REMARKS", ((EditText)v.findViewById(R.id.textRemarks)).getText().toString());
	    	    	
	    	    	
	    	    	int position = Integer.parseInt(((TextView)v.findViewById(R.id.textPosition)).getText().toString());
	    	    	valueHolder.getCustomerList().get(valueHolder.customerSelected).getInvoiceList().get(valueHolder.getInvoiceSelected()).getProductList().get(position).setQTY_RETURN(((EditText)v.findViewById(R.id.textView6)).getText().toString());
	    	    	valueHolder.getCustomerList().get(valueHolder.customerSelected).getInvoiceList().get(valueHolder.getInvoiceSelected()).getProductList().get(position).setPROBLEM_COMPLAIN(complainCodeRem);
	    	    	valueHolder.getCustomerList().get(valueHolder.customerSelected).getInvoiceList().get(valueHolder.getInvoiceSelected()).getProductList().get(position).setPROBLEM_REMARK(((TextView)v.findViewById(R.id.textRemarks)).getText().toString());
	    	    	
	    	    	problemList.add(map);
	    	    	
	    	    	// DELIVERY_DATE
	    	    	// DELIVERY_SEQ
	    	    	// CASH_AMOUNT
    	    	}
    	      }
    	}
    }
    
    
    
    private class ProblemTask extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			dialog = ProgressDialog.show(ProductProblemTableActivity.this, "", getString(R.string.wait));
			dialog.setCancelable(false);
		}
		@Override
		protected String doInBackground(String... params) {
				WebService service = new WebService();
				try {
					if (isCancelled()) {
	                    publishProgress(CANCELLED); //Notify your activity that you had canceled the task
	                    return (null); // don't forget to terminate this method
	                }		
					serviceResponse = service.updateInvoiceProblem(problemList);					
					publishProgress(SUCCESS); //if everything is Okay then publish this message, you may also use onPostExecute() method  
				} catch (UnknownHostException e) {
	                e.printStackTrace();
	                publishProgress(NETWORK_ERROR);
	            } catch (Exception e) {
	                e.printStackTrace();
	                publishProgress(SERVER_ERROR);
	            }
				return null;
		}
		@Override
		protected void onProgressUpdate(String... errorCode) {
			if(errorCode[0].toString().equalsIgnoreCase(CANCELLED)) {
				dialog.dismiss();
			}else if(errorCode[0].toString().equalsIgnoreCase(NETWORK_ERROR)) {
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "Network connection error",Toast.LENGTH_SHORT).show();
			}else if(errorCode[0].toString().equalsIgnoreCase(SERVER_ERROR)) {	
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "Server error",Toast.LENGTH_SHORT).show();
			}else if(errorCode[0].toString().equalsIgnoreCase(SUCCESS)) {		
				dialog.dismiss();
				if(serviceResponse.equalsIgnoreCase("true")) {
					//Toast.makeText(getApplicationContext(), R.string.serviceSuccess ,Toast.LENGTH_SHORT).show();
					UpdateStatus updateStatus = new UpdateStatus();
    				updateStatus.execute();
				}else if(serviceResponse.equalsIgnoreCase("false")) {
					Toast.makeText(getApplicationContext(), R.string.serviceFail, Toast.LENGTH_SHORT).show();
				}else {
					
				}
			}
		}
		protected void onPostExecute(String result) {
		}
	}
    
    
    
    private class UpdateStatus extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			dialog = ProgressDialog.show(ProductProblemTableActivity.this, "", getString(R.string.wait));
			dialog.setCancelable(false);
		}		
		@Override
		protected String doInBackground(String... params) {
//				WebService service = new WebService();
//				try {
//					if (isCancelled()) {
//	                    publishProgress(CANCELLED); //Notify your activity that you had canceled the task
//	                    return (null); // don't forget to terminate this method
//	                }
//					serviceResponse = service.updateDeliverStatus(customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getCOMPANY()
//																,customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getTRANSACTION_TYPE()
//																,customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getINV_BOOK()
//																,customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getINV_NO()
//																,"W"
//																,""
//																,""
//																,customerModel.getCUST_CODE()
//																,customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getINV_BRANCH()
//																,valueHolder.getLatitude()
//																,valueHolder.getLongitude()
//																,valueHolder.getCheckInTime());
//					
//					publishProgress(SUCCESS); //if everything is Okay then publish this message, you may also use onPostExecute() method  
//				} catch (UnknownHostException e) {
//	                e.printStackTrace();
//	                publishProgress(NETWORK_ERROR);
//	            } catch (Exception e) {
//	                e.printStackTrace();
//	                publishProgress(SERVER_ERROR);
//	            }
				return null;
		}
		@Override
		protected void onProgressUpdate(String... errorCode) {			
			if(errorCode[0].toString().equalsIgnoreCase(CANCELLED)) {
				dialog.dismiss();
			}else if(errorCode[0].toString().equalsIgnoreCase(NETWORK_ERROR)) {
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "Network connection error",Toast.LENGTH_SHORT).show();
			}else if(errorCode[0].toString().equalsIgnoreCase(SERVER_ERROR)) {	
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "Server error",Toast.LENGTH_SHORT).show();
			}else if(errorCode[0].toString().equalsIgnoreCase(SUCCESS)) {
				dialog.dismiss();
				if(serviceResponse.equalsIgnoreCase("true")) {
					btnComplete.setVisibility(View.INVISIBLE);
					Toast.makeText(getApplicationContext(), R.string.serviceSuccess ,Toast.LENGTH_SHORT).show();
				}else if(serviceResponse.equalsIgnoreCase("update")) {
					valueHolder.getCustomerList().get(valueHolder.customerSelected).getInvoiceList().get(valueHolder.getInvoiceSelected()).setINV_LATITUDE(valueHolder.getLatitude());
					valueHolder.getCustomerList().get(valueHolder.customerSelected).getInvoiceList().get(valueHolder.getInvoiceSelected()).setINV_LONGITUDE(valueHolder.getLongitude());
					btnComplete.setVisibility(View.INVISIBLE);
					Toast.makeText(getApplicationContext(), R.string.serviceSuccess ,Toast.LENGTH_SHORT).show();
				}else if(serviceResponse.equalsIgnoreCase("false")) {
					Toast.makeText(getApplicationContext(), R.string.serviceFail, Toast.LENGTH_SHORT).show();
				}else {
					
				}
				onBackPressed();
			}
		}
		protected void onPostExecute(String result) {
		}
	}
    
    
    public void onButtonClicker(View v)
    {
    	AlertDialog.Builder builder;
    	LayoutInflater inflater;
    	View layout;
    	ImageView close_dialog;
    	Button btnSave;
    	switch (v.getId()) {
		case R.id.btnComplete:
    		getAllConvertView();
    		if(compareVal < 0){
    			return;
    		}
    		if(problemList.size() <= 0) {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProductProblemTableActivity.this);
				alertDialog.setMessage(getString(R.string.selectProblem)).setCancelable(false)
//				alertDialog.setMessage("Please select any problem first").setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {			
//						finish();
						dialog.cancel();
		    			return;
					}
				});
				alertDialog.show();	
    		}
    		else {
    			inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
    			layout = inflater.inflate(R.layout.accept_dialog,(ViewGroup) findViewById(R.id.layout_root));
    			close_dialog = (ImageView) layout.findViewById(R.id.xClose);
    			close_dialog.setOnClickListener(new View.OnClickListener() {
    				public void onClick(View v) {
    					alertDialog.dismiss();
    				}
    			});
    			builder = new AlertDialog.Builder(ProductProblemTableActivity.this);
    			builder.setCancelable(false);
    			builder.setView(layout);
    			alertDialog = builder.create();
    			alertDialog.show();

    			btnSave = (Button) alertDialog.findViewById(R.id.btnSave);
    			btnSave.setOnClickListener(new OnClickListener() {
    				public void onClick(View v) {
    					valueHolder.getCustomerList().get(valueHolder.customerSelected).getInvoiceList().get(valueHolder.getInvoiceSelected()).setINV_STATUS("W");
    					alertDialog.dismiss();
//	        			getAllConvertView();
    					if(problemList.size() > 0) {
//	            			problemTask = new ProblemTask();
//	            			problemTask.execute();
    						//	valueHolder.setIsFromProductProblemTable("True");
    						ILogisticsApplication.problemListArr = problemList;
    						setResult(RESULT_OK);
    						valueHolder.setIsProblemExist("Y");
//	    					prdListObj.problemHolder();
//	    					startActivity(new Intent(ProductProblemTableActivity.this, ProductListActivity.class));
    						finish();
    					}
    					else
    					{
    						valueHolder.setIsProblemExist("N");
    						finish();
    					}

    				}
    			});
    		}
			break;		
		default:
			break;
		}
    }
    
    
    @Override
    public void onBackPressed() {
		//startActivity(new Intent(ProductProblemTableActivity.this, ProductListActivity.class));
		finish();
	    super.onBackPressed();
	}
    
    @Override
    protected void onDestroy() {
        //you may call the cancel() method but if it is not handled in doInBackground() method
    	/*
        if (getRoutingTask != null && getRoutingTask.getStatus() != AsyncTask.Status.FINISHED)
        	getRoutingTask.cancel(true);
        */	
        super.onDestroy();
    }
    
}

