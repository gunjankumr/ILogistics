package com.compass.activity;

/**
 * @author Paresh N. Mayani
 * http://www.technotalkative.com
 */

import com.google.common.collect.Lists;

import com.compass.dbhelper.InvoiceInfo;
import com.compass.dbhelper.InvoiceInfoRepo;
import com.compass.ilogistics.R;
import com.compass.model.CustomerModel;
import com.compass.model.ProblemSet;
import com.compass.model.ProductModel;
import com.compass.model.Routing;
import com.compass.utils.ConnectionDetector;
import com.compass.utils.Constants;
import com.compass.utils.ValueHolder;
import com.compass.utils.WebService;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductListActivity extends DashBoardActivity implements OnGestureListener {
	
	private ValueHolder valueHolder;
	private ProgressDialog dialog;
	private GestureDetector gDetector;
	private AlertDialog alertDialog;
	
	private ListView lstView;
	private TextView invDoc;
	private TextView btnComplete, btnProblem;
	
	private static final String SERVER_ERROR = "SERVER_ERROR";
	private static final String NETWORK_ERROR = "NETWORK_ERROR";
	private static final String CANCELLED = "CANCELLED";
	private static final String SUCCESS = "SUCCESS";
	private static int PRODUCT_PROBLEM_TABLE_ACTIVITY_REQUEST_CODE = 100;
	
	ListAdapter dataAdapter = null;
	
	private String serviceResponse = null;
	private ArrayList<HashMap<String, String>> serviceResponseList;
	private ArrayList<Routing> routingList = new ArrayList<Routing>();
	private CustomerModel customerModel;
	private ArrayList<HashMap<String, String>> problemListArr = new ArrayList<HashMap<String, String>>();

	String[] planets;
	
	private int[] colors = new int[] {0x30ffffff, 0x30f0f0f0};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);
        setHeader(getString(R.string.app_name), true, false, true);
        gDetector = new GestureDetector(ProductListActivity.this,this);
        valueHolder = ValueHolder.getSingletonObject();
        lstView = (ListView)findViewById(R.id.list);
        invDoc = (TextView)findViewById(R.id.invDoc);
        btnComplete = (Button)findViewById(R.id.btnComplete);
        btnProblem = (Button)findViewById(R.id.btnProblem);
        planets = getResources().getStringArray(R.array.problem_array);
        //View footerView =  ((LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_check, null, false);
        //lstView.addFooterView(footerView);
        //Toast.makeText(getApplicationContext(), valueHolder.getCustomerList().get(valueHolder.customerSelected).getInvoiceList().get(valueHolder.getInvoiceSelected()).getINV_STATUS(),Toast.LENGTH_SHORT).show();
        customerModel = valueHolder.getCustomerList().get(valueHolder.customerSelected);
        String status = customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getINV_STATUS();  
        //if(customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getINV_STATUS().length() > 0) {
        if(status.equalsIgnoreCase("Y") || status.equalsIgnoreCase("W")) {
        	btnComplete.setVisibility(View.INVISIBLE);
        	btnProblem.setVisibility(View.INVISIBLE);
			btnHomeClickStatus(true);
        }
        else {
			btnHomeClickStatus(false);
        }
        
        for(int i=0;i<valueHolder.getCustomerList().size();i++) {
        	if((valueHolder.getCustomerList().get(i).getCHECKIN().equalsIgnoreCase("Y")) && (!valueHolder.getCustomerList().get(i).getCUST_CODE().equalsIgnoreCase(customerModel.getCUST_CODE()))) {
        		btnComplete.setVisibility(View.INVISIBLE);
            	btnProblem.setVisibility(View.INVISIBLE);
				btnHomeClickStatus(true);
            	break;
        	}
        }
        
        
        if(valueHolder.getInvoiceSelected() == -1) {
        	invDoc.setText(customerModel.getInvoiceList().get(0).getINV_BOOK_NO());
        	valueHolder.setInvoiceSelected(0);
        }else {
        	invDoc.setText(customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getINV_BOOK_NO());
        }
        
        if(valueHolder.isChildSelected()) {
        	((ImageView)findViewById(R.id.invleft)).setVisibility(View.INVISIBLE);
        	((ImageView)findViewById(R.id.invright)).setVisibility(View.INVISIBLE);
        }else {
        	if((customerModel.getInvoiceList().size()-1) == valueHolder.getInvoiceSelected()) {
            	((ImageView)findViewById(R.id.invright)).setVisibility(View.INVISIBLE);
            }
            if(valueHolder.getInvoiceSelected() == 0) {
            	((ImageView)findViewById(R.id.invleft)).setVisibility(View.INVISIBLE);
            }
        }
        
        if(valueHolder.getLatitude().length() == 0) {
        	btnComplete.setVisibility(View.INVISIBLE);
        	btnProblem.setVisibility(View.INVISIBLE);
			btnHomeClickStatus(true);
        }
        
//        System.out.print(valueHolder.getIsFromProductProblemTable());
//        if(valueHolder.getIsFromProductProblemTable().equalsIgnoreCase("True"))
//        {
//        	btnComplete.setVisibility(View.VISIBLE);
//        	btnProblem.setVisibility(View.VISIBLE);
//        }
        
        dataAdapter = new ListAdapter(getApplicationContext(), R.layout.product_item, customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getProductList());
        lstView.setAdapter(dataAdapter);
        lstView.setTextFilterEnabled(true);

    }
    
    private String getComplainDescription(String probCode) {
    	if (valueHolder.getProblemList() != null 
    			&& valueHolder.getProblemList().size() > 0
    			&& probCode != null && probCode.length() > 0) {
    		for (int i = 0; i < valueHolder.getProblemList().size(); i++) {
    			ProblemSet problemSet = valueHolder.getProblemList().get(i);
    			String code = problemSet.getCOMPLAIN_CODE();
    			String remarkId = problemSet.getREM_ID();
    			remarkId = remarkId.substring(remarkId.indexOf("-"));
    			code = code + remarkId;
    			if (probCode.equals(code)) {
    				return valueHolder.getProblemList().get(i).COMPLAIN_DESC;
    			}
    		}
    		return "";
    	}
    	return "";
    }

/*************/
    private class ListAdapter extends ArrayAdapter<ProductModel> {
      	 
    	  private ArrayList<ProductModel> originalList;
    	  private ArrayList<ProductModel> filterList;
    	  private CustomerFilter filter;
    	 
    	  public ListAdapter(Context context, int textViewResourceId, ArrayList<ProductModel> fList) {
    	   super(context, textViewResourceId, fList);
    	   this.filterList = new ArrayList<ProductModel>();
    	   this.filterList.addAll(fList);
    	   this.originalList = new ArrayList<ProductModel>();
    	   this.originalList.addAll(fList);
    	  }

	  	 @Override
	  	 public Filter getFilter() {
	  		 if (filter == null){
	  			 filter  = new CustomerFilter();
	  		 }
	  		 return filter;
	  	 }
	 
		 private class ViewHolder {
			 TextView text1;
			 TextView text2;
			 TextView text3;
			 TextView text4;
			 TextView text5;
			 TextView text6;
			 TextView textComplain;
			 TextView textRemarks;
		 }
    	 
		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
			  problemListArr.clear();
			  ViewHolder holder = null;
			  if (convertView == null) {  	 
				  LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				  convertView = vi.inflate(R.layout.product_item, null);
				  holder = new ViewHolder();
				  holder.text1 = (TextView) convertView.findViewById(R.id.textView1);
				  holder.text2 = (TextView) convertView.findViewById(R.id.textView2);
				  holder.text3 = (TextView) convertView.findViewById(R.id.textView3);
				  holder.text4 = (TextView) convertView.findViewById(R.id.textView4);
				  holder.text5 = (TextView) convertView.findViewById(R.id.textView5);
				  holder.text6 = (TextView) convertView.findViewById(R.id.textView6);
				  holder.textComplain = (TextView) convertView.findViewById(R.id.textComplain);
				  holder.textRemarks = (TextView) convertView.findViewById(R.id.textRemarks);
				  convertView.setTag(holder);
			  } else {
			   	holder = (ViewHolder) convertView.getTag();
			  }
		 
			  ProductModel product = filterList.get(position);
			  holder.text1.setText(product.getPRODUCT_CODE());
			  holder.text2.setText("");
			  holder.text3.setText(product.getPRODUCT_NAME());
			  holder.text4.setText("");
			  holder.text5.setText(product.getQTY()+"  "+product.getUNIT());
			  if(product.getQTY_RETURN().length() > 0) {
				  holder.text6.setText(getString(R.string.quantity)+ "   " +product.getQTY_RETURN());
			  }else {
				  holder.text6.setText(product.getQTY_RETURN());
			  }
			  			  
			  holder.textComplain.setText(getComplainDescription(product.getPROBLEM_COMPLAIN()));
//			  if(product.getPROBLEM_COMPLAIN().equalsIgnoreCase("PR-001")) {
//				  holder.textComplain.setText(planets[0]);
//			  }else if(product.getPROBLEM_COMPLAIN().equalsIgnoreCase("PR-014")) {
//				  holder.textComplain.setText(planets[1]);
//			  }else if(product.getPROBLEM_COMPLAIN().equalsIgnoreCase("LG-008")) {
//				  holder.textComplain.setText(planets[2]);
//			  }
			  
			  if(product.getPROBLEM_COMPLAIN().length() == 0) {
				  ((TextView) convertView.findViewById(R.id.textView6)).setVisibility(View.GONE);
				  ((LinearLayout) convertView.findViewById(R.id.r4)).setVisibility(View.GONE);
			  }
			  
			  holder.textRemarks.setText(product.getPROBLEM_REMARK());
			  
			  convertView.setBackgroundColor(colors[position % colors.length]);
			 
//				if(isFromProductProblemTable)
//		        {
//		        	btnComplete.setVisibility(View.VISIBLE);
//		        	btnProblem.setVisibility(View.VISIBLE);
//		        }
//		  
			 	 HashMap<String, String> map = new HashMap<String, String>();
			     map.put("ROUTE_CODE", valueHolder.getRoutingCode());
			     map.put("CUST_CODE", customerModel.getCUST_CODE());
			     map.put("PRODUCT_SEQ", product.getPRODUCT_SEQ());
			     map.put("PRODUCT_CODE", product.getPRODUCT_CODE());
			     map.put("WARE_CODE", product.getWARE_CODE());
			     map.put("WARE_ZONE", product.getWARE_ZONE());
			     map.put("QTY_RETURN", product.getQTY_RETURN());
			     map.put("UNIT", product.getUNIT());
			     problemListArr.add(map);
			     customerModel = valueHolder.getCustomerList().get(valueHolder.customerSelected);
			        String status = customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getINV_STATUS();  
			        //if(customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getINV_STATUS().length() > 0) {
			        if(status.equalsIgnoreCase("Y") || status.equalsIgnoreCase("W")) {
			        	//btnComplete.setVisibility(View.INVISIBLE);
			        	btnProblem.setVisibility(View.INVISIBLE);
			        }
			   
		   return convertView;
		 
		  }

	private class CustomerFilter extends Filter {
    	   @Override
    	   protected FilterResults performFiltering(CharSequence constraint) {
    		   constraint = constraint.toString().toLowerCase();
    		   FilterResults result = new FilterResults();
    		   if(constraint != null && constraint.toString().length() > 0) {
    			   ArrayList<ProductModel> filteredItems = new ArrayList<ProductModel>();
    			   for(int i = 0, l = originalList.size(); i < l; i++) {
    				   ProductModel product = originalList.get(i);
    				   if(product.toString().toLowerCase().contains(constraint))
    					   filteredItems.add(product);
    			   }
    			   result.count = filteredItems.size();
    			   result.values = filteredItems;
    		   } else {
    			   synchronized(this) {
    				   result.values = originalList;
    				   result.count = originalList.size();
    			   }
    		   }
    		   return result;
    	   }
    	 
    	   @SuppressWarnings("unchecked")
    	   @Override
    	   protected void publishResults(CharSequence constraint,FilterResults results) {
    		   filterList = (ArrayList<ProductModel>)results.values;
    		   notifyDataSetChanged();
    		   clear();
    		   for(int i = 0, l = filterList.size(); i < l; i++)
    			   add(filterList.get(i));
    		   notifyDataSetInvalidated();
    	   	}
		}
    }
/*************/    
    
    
    @Override
    public boolean onDown(MotionEvent arg0) {
    	return false;
    }
    /*
    @Override
    public boolean onFling(MotionEvent start, MotionEvent finish, float xVelocity, float yVelocity) {
    	if (start.getRawY() < finish.getRawY()) {
    		Toast.makeText(getApplicationContext(), "right",Toast.LENGTH_SHORT).show();
        } else {
        	Toast.makeText(getApplicationContext(), "left",Toast.LENGTH_SHORT).show();
        }
        return true;
    }
	*/
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // Check movement along the Y-axis. If it exceeds SWIPE_MAX_OFF_PATH, then dismiss the swipe.
        if (Math.abs(e1.getY() - e2.getY()) > Constants.SWIPE_MAX_OFF_PATH)
            return false;
        // Swipe from right to left.
        // The swipe needs to exceed a certain distance (SWIPE_MIN_DISTANCE) and a certain velocity (SWIPE_THRESHOLD_VELOCITY).
        if (e1.getX() - e2.getX() > Constants.SWIPE_MIN_DISTANCE && Math.abs(velocityX) > Constants.SWIPE_THRESHOLD_VELOCITY) {
        	if(!valueHolder.isChildSelected()) {
	        	if(valueHolder.getInvoiceSelected()+1 < customerModel.getInvoiceList().size()) {
            		if(ILogisticsApplication.problemListArr != null && ILogisticsApplication.problemListArr.size() > 0) {
//            			ILogisticsApplication.problemListArr.clear();
						AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProductListActivity.this);
						alertDialog.setMessage(getString(R.string.finish_invoice)).setCancelable(false)
						.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {			
//								finish();
								dialog.cancel();
								return;
							}
						});
						alertDialog.show();
            		}
            		else {

            			valueHolder.setInvoiceSelected(valueHolder.getInvoiceSelected()+1);
            			startActivity(new Intent(getApplicationContext(), ProductListActivity.class));       	
            			overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_left );
            			finish();
            		}
	        	}
        	}
            return true;
        }
        // Swipe from left to right.
        // The swipe needs to exceed a certain distance (SWIPE_MIN_DISTANCE) and a certain velocity (SWIPE_THRESHOLD_VELOCITY).
        if (e2.getX() - e1.getX() > Constants.SWIPE_MIN_DISTANCE && Math.abs(velocityX) > Constants.SWIPE_THRESHOLD_VELOCITY) {
        	if(!valueHolder.isChildSelected()) {
	        	if(valueHolder.getInvoiceSelected()-1 >= 0) {
            		if(ILogisticsApplication.problemListArr != null && ILogisticsApplication.problemListArr.size() > 0) {
//            			ILogisticsApplication.problemListArr.clear();
						AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProductListActivity.this);
						alertDialog.setMessage(getString(R.string.finish_invoice)).setCancelable(false)
						.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {			
//								finish();
								dialog.cancel();
								return;
							}
						});
						alertDialog.show();
            		}
            		else {
            			valueHolder.setInvoiceSelected(valueHolder.getInvoiceSelected()-1);
            			startActivity(new Intent(getApplicationContext(), ProductListActivity.class));
            			overridePendingTransition( R.anim.slide_in_right, R.anim.slide_out_right );
            			finish();
            		}
	        	}
        	}

        	
            return true;
        }
        return false;
    }
    
    @Override
    public void onLongPress(MotionEvent arg0) {}

    @Override
    public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
    	return false;
    }
    
    @Override
    public void onShowPress(MotionEvent arg0) {}
    
    @Override
    public boolean onSingleTapUp(MotionEvent arg0) {
    	return false;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent me) {
    	return gDetector.onTouchEvent(me);
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (gDetector != null) {
            if (gDetector.onTouchEvent(ev))
                return true;
        }
        return super.dispatchTouchEvent(ev);
    }
    


    
    private class UpdateStatus extends AsyncTask<String, String, String> {
		String cash = "0";
		protected void onPreExecute() {
			dialog = ProgressDialog.show(ProductListActivity.this, "", getString(R.string.wait));
			dialog.setCancelable(false);
			if(((EditText) alertDialog.findViewById(R.id.cashTxt)).getText().toString().trim().length() > 0)
			{
				cash = ((EditText) alertDialog.findViewById(R.id.cashTxt)).getText().toString().trim();
			}
		}		
		@Override
		protected String doInBackground(String... params) {
				WebService service = new WebService();
				try {
					if (isCancelled()) {
	                    publishProgress(CANCELLED); //Notify your activity that you had canceled the task
	                    return (null); // don't forget to terminate this method
	                }		
					
					
					
					/*serviceResponse = service.updateDeliverStatus(customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getCOMPANY()
																,customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getTRANSACTION_TYPE()
																,customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getINV_BOOK()
																,customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getINV_NO()
																,"Y"
																,((EditText) alertDialog.findViewById(R.id.cashTxt)).getText().toString().trim()
																,((EditText) alertDialog.findViewById(R.id.problemTxt)).getText().toString().trim()
																,customerModel.getCUST_CODE()
																//,customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getINV_BRANCH()
																,customerModel.getBRANCH_CODE()
																,valueHolder.getLatitude()
																,valueHolder.getLongitude()
																,valueHolder.getCheckInTime());*/

					serviceResponseList = service.updateDeliverStatus(valueHolder.getUsername(), Constants.DEVICE_ID, valueHolder.getCompanyCode(), problemListArr.size(), valueHolder.getDeliveryDateNew(),valueHolder.getRoutingCode(),
																		valueHolder.getDeliverySeqNew(), customerModel.getCUST_CODE(), customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getTRANSACTION_TYPE()
																		,customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getINV_BOOK()
																		,customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getINV_NO()
																		,problemListArr, cash);
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
//				if(serviceResponse.equalsIgnoreCase("true")) {
//					btnComplete.setVisibility(View.INVISIBLE);
//                	btnProblem.setVisibility(View.INVISIBLE);
//					Toast.makeText(getApplicationContext(), R.string.serviceSuccess ,Toast.LENGTH_SHORT).show();
//				}else if(serviceResponse.equalsIgnoreCase("update")) {
//					valueHolder.getCustomerList().get(valueHolder.customerSelected).getInvoiceList().get(valueHolder.getInvoiceSelected()).setINV_LATITUDE(valueHolder.getLatitude());
//					valueHolder.getCustomerList().get(valueHolder.customerSelected).getInvoiceList().get(valueHolder.getInvoiceSelected()).setINV_LONGITUDE(valueHolder.getLongitude());
//					btnComplete.setVisibility(View.INVISIBLE);
//                	btnProblem.setVisibility(View.INVISIBLE);
//					Toast.makeText(getApplicationContext(), R.string.serviceSuccess ,Toast.LENGTH_SHORT).show();
//				}
				
//				problemListArr = null;
				
            	if (valueHolder.getIsProblemExist().equalsIgnoreCase("Y")) {
            		valueHolder.getCustomerList().get(valueHolder.customerSelected).getInvoiceList().get(valueHolder.getInvoiceSelected()).setINV_STATUS("W");
            		valueHolder.getCustomerList().get(valueHolder.customerSelected).setCOMPLETE_STATUS("W");
            		valueHolder.setIsProblemExist("N");
            	}

				int flag = 0 ;
				String msg = null;
				
				for(int i=0;i<serviceResponseList.size();i++) {
					
					flag = Integer.parseInt(serviceResponseList.get(i).get("FLAG").toString());
					msg = serviceResponseList.get(i).get("MSG");
				}
				System.out.println(msg);
				//for 1
				if(flag == 1)
				{
					btnComplete.setVisibility(View.INVISIBLE);
					btnProblem.setVisibility(View.INVISIBLE);
					btnHomeClickStatus(false);
            		if(ILogisticsApplication.problemListArr != null && ILogisticsApplication.problemListArr.size() > 0) {
            			ILogisticsApplication.problemListArr.clear();
            		}

					Toast.makeText(getApplicationContext(), R.string.serviceSuccess ,Toast.LENGTH_SHORT).show();

				}
				else
				{
					btnComplete.setVisibility(View.VISIBLE);
					btnProblem.setVisibility(View.VISIBLE);
					btnHomeClickStatus(false);

					Toast.makeText(getApplicationContext(), R.string.serviceFail,Toast.LENGTH_SHORT).show();
				}
//			}else if(serviceResponse.equalsIgnoreCase("false")) {
//					Toast.makeText(getApplicationContext(), R.string.serviceFail, Toast.LENGTH_SHORT).show();
//				}else {
//					
//				}
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
			
            inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.accept_dialog,(ViewGroup) findViewById(R.id.layout_root));
            close_dialog = (ImageView) layout.findViewById(R.id.xClose);
            close_dialog.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                	alertDialog.dismiss();
                }
            });
            builder = new AlertDialog.Builder(ProductListActivity.this);
            builder.setCancelable(false);
            builder.setView(layout);
            alertDialog = builder.create();
            alertDialog.show();
            
            //valueHolder.getCustomerList().get(valueHolder.customerSelected).getInvoiceList().get(valueHolder.getInvoiceSelected()).getPAYMENT_TYPE()
            if(customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getPAYMENT_TYPE().equalsIgnoreCase("CASH")) {
            	((LinearLayout) alertDialog.findViewById(R.id.r1)).setVisibility(View.VISIBLE);
            	((LinearLayout) alertDialog.findViewById(R.id.r0)).setVisibility(View.INVISIBLE);
            }
            ((TextView) alertDialog.findViewById(R.id.statusTxt)).setText("Y");
            btnSave = (Button) alertDialog.findViewById(R.id.btnSave);
            btnSave.setOnClickListener(new OnClickListener() {
    			public void onClick(View v) {
    				if(customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getPAYMENT_TYPE().equalsIgnoreCase("CASH")) {
    	            	if(((EditText) alertDialog.findViewById(R.id.cashTxt)).getText().toString().trim().length() > 0) {
    	            		valueHolder.getCustomerList().get(valueHolder.customerSelected).getInvoiceList().get(valueHolder.getInvoiceSelected()).setINV_STATUS("Y");
    	            		alertDialog.dismiss();

							if (new ConnectionDetector(ProductListActivity.this).isConnectingToInternet()) {
								UpdateStatus updateStatus = new UpdateStatus();
								updateStatus.execute();
							} else {
								initiateDeliveryStatusUpdateForLocalDatabase();
							}
            			}else {
            				Toast.makeText(getApplicationContext(), getString(R.string.require_cash) ,Toast.LENGTH_SHORT).show();
            			}
    				}else {
    					valueHolder.getCustomerList().get(valueHolder.customerSelected).getInvoiceList().get(valueHolder.getInvoiceSelected()).setINV_STATUS("Y");			
    					alertDialog.dismiss();

						if (new ConnectionDetector(ProductListActivity.this).isConnectingToInternet()) {
							UpdateStatus updateStatus = new UpdateStatus();
							updateStatus.execute();
						} else {
							initiateDeliveryStatusUpdateForLocalDatabase();
						}
    				}
    			}
    		});
    		
			break;
		case R.id.btnProblem:
			/*
            inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.accept_dialog,(ViewGroup) findViewById(R.id.layout_root));
            close_dialog = (ImageView) layout.findViewById(R.id.xClose);
            close_dialog.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                	alertDialog.dismiss();
                }
            });
            builder = new AlertDialog.Builder(ProductListActivity.this);
            builder.setCancelable(false);
            builder.setView(layout);
            alertDialog = builder.create();
            alertDialog.show();
            
            ((LinearLayout) alertDialog.findViewById(R.id.r2)).setVisibility(View.VISIBLE);
            ((TextView) alertDialog.findViewById(R.id.statusTxt)).setText("W");
            btnSave = (Button) alertDialog.findViewById(R.id.btnSave);
            btnSave.setOnClickListener(new OnClickListener() {
    			public void onClick(View v) {
    				if(((EditText) alertDialog.findViewById(R.id.problemTxt)).getText().toString().trim().length() > 0) {
    					valueHolder.getCustomerList().get(valueHolder.customerSelected).getInvoiceList().get(valueHolder.getInvoiceSelected()).setINV_STATUS("W");
    					valueHolder.getCustomerList().get(valueHolder.customerSelected).getInvoiceList().get(valueHolder.getInvoiceSelected()).setINV_PROBLEM(((EditText) alertDialog.findViewById(R.id.problemTxt)).getText().toString().trim());
        				alertDialog.dismiss();
        				UpdateStatus updateStatus = new UpdateStatus();
        				updateStatus.execute();
        			}else {
        				Toast.makeText(getApplicationContext(), getString(R.string.require_problem),Toast.LENGTH_SHORT).show();
        			}
    			}
    		});
    		*/
			//startActivity(new Intent(getApplicationContext(), ProductProblemActivity.class));
			//startActivity(new Intent(getApplicationContext(), CheckListActivity.class));
			//startActivity(new Intent(getApplicationContext(), CheckListTestActivity.class));
			startActivityForResult(new Intent(getApplicationContext(), ProductProblemTableActivity.class), PRODUCT_PROBLEM_TABLE_ACTIVITY_REQUEST_CODE);
			break;
		default:
			break;
		}
    }

	private void initiateDeliveryStatusUpdateForLocalDatabase() {
		String cash = "0";
		if (alertDialog != null) {
			if(((EditText) alertDialog.findViewById(R.id.cashTxt)).getText().toString().trim().length() > 0) {
				cash = ((EditText) alertDialog.findViewById(R.id.cashTxt)).getText().toString().trim();
			}
		}

		updateDeliverStatusInDatabase(valueHolder.getUsername(),
				Constants.DEVICE_ID,
				valueHolder.getCompanyCode(),
				problemListArr.size(),
				valueHolder.getDeliveryDateNew(),
				valueHolder.getRoutingCode(),
				valueHolder.getDeliverySeqNew(),
				customerModel.getCUST_CODE(),
				customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getTRANSACTION_TYPE(),
				customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getINV_BOOK(),
				customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getINV_NO(),
				problemListArr,
				cash);
	}

	private void updateDeliverStatusInDatabase(String username, String deviceID, String inCompany, int size,
		String inDeliveryDateNew, String inRoutingCode, String inDeliverySeqNew, String inCustCode, String inTRANSACTION_TYPE
				,String inINV_BOOK, String inINV_NO, ArrayList<HashMap<String, String>> problemListArr, String cash) {

		List<InvoiceInfo> invoiceInfoList = Lists.newArrayList();
		if (ILogisticsApplication.problemListArr != null && ILogisticsApplication.problemListArr.size() > 0) {
			for (int i = 0; i < ILogisticsApplication.problemListArr.size(); i++) {
				InvoiceInfo invoiceInfo = new InvoiceInfo();

				invoiceInfo.routeCode = ILogisticsApplication.problemListArr.get(i).get("ROUTE_CODE");
				invoiceInfo.customerCode = ILogisticsApplication.problemListArr.get(i).get("CUST_CODE");
				invoiceInfo.transactionType = ILogisticsApplication.problemListArr.get(i).get("TRANSACTION_TYPE");
				invoiceInfo.invoiceBook = ILogisticsApplication.problemListArr.get(i).get("INV_BOOK");
				invoiceInfo.invoiceNumber = ILogisticsApplication.problemListArr.get(i).get("INV_NO");
				invoiceInfo.productSeq = ILogisticsApplication.problemListArr.get(i).get("PRODUCT_SEQ");
				invoiceInfo.productCode = ILogisticsApplication.problemListArr.get(i).get("PRODUCT_CODE");
				invoiceInfo.wareCode = ILogisticsApplication.problemListArr.get(i).get("WARE_CODE");
				invoiceInfo.wareZone = ILogisticsApplication.problemListArr.get(i).get("WARE_ZONE");
				invoiceInfo.qtyReturn = ILogisticsApplication.problemListArr.get(i).get("QTY_RETURN");
				invoiceInfo.unit = ILogisticsApplication.problemListArr.get(i).get("UNIT");
				invoiceInfo.complainCode = ILogisticsApplication.problemListArr.get(i).get("COMPLAIN_CODE");
				invoiceInfo.remarkId = ILogisticsApplication.problemListArr.get(i).get("REM_ID");
				invoiceInfo.remarks = ILogisticsApplication.problemListArr.get(i).get("REMARKS");
				invoiceInfo.deliveryDate = inDeliveryDateNew;
				invoiceInfo.deliverySeq = inDeliverySeqNew;
				invoiceInfo.cash = cash;
				invoiceInfo.isCompleted = true;

				invoiceInfoList.add(invoiceInfo);
			}
		} else {
			for (int i = 0; i < problemListArr.size(); i++) {
				InvoiceInfo invoiceInfo = new InvoiceInfo();

				invoiceInfo.routeCode = inRoutingCode;
				invoiceInfo.customerCode = inCustCode;
				invoiceInfo.transactionType = inTRANSACTION_TYPE;
				invoiceInfo.invoiceBook = inINV_BOOK;
				invoiceInfo.invoiceNumber = inINV_NO;
				invoiceInfo.productSeq = problemListArr.get(i).get("PRODUCT_SEQ");
				invoiceInfo.productCode = problemListArr.get(i).get("PRODUCT_CODE");
				invoiceInfo.wareCode = problemListArr.get(i).get("WARE_CODE");
				invoiceInfo.wareZone = problemListArr.get(i).get("WARE_ZONE");
				invoiceInfo.qtyReturn = problemListArr.get(i).get("QTY_RETURN");
				invoiceInfo.unit = problemListArr.get(i).get("UNIT");
				invoiceInfo.complainCode = "";
				invoiceInfo.remarkId = "";
				invoiceInfo.remarks = "";
				invoiceInfo.deliveryDate = inDeliveryDateNew;
				invoiceInfo.deliverySeq = inDeliverySeqNew;
				invoiceInfo.cash = cash;
				invoiceInfo.isCompleted = true;

				invoiceInfoList.add(invoiceInfo);
			}
		}

		InvoiceInfoRepo invoiceInfoRepo = new InvoiceInfoRepo(this);
		if (invoiceInfoList != null && !invoiceInfoList.isEmpty()) {
			for (int i = 0; i < invoiceInfoList.size(); i++) {
				InvoiceInfo recToInsert = invoiceInfoList.get(i);
				if (recToInsert != null) {
					if (invoiceInfoRepo.isRecordAvailable(recToInsert)) {
						invoiceInfoRepo.update(recToInsert);
					} else {
						invoiceInfoRepo.insert(recToInsert);
					}
				}
			}
		}

		// update UI and other variables after inserting the record in local database
		if (valueHolder.getIsProblemExist().equalsIgnoreCase("Y")) {
			valueHolder.getCustomerList().get(valueHolder.customerSelected).getInvoiceList().get(valueHolder.getInvoiceSelected()).setINV_STATUS("W");
			valueHolder.getCustomerList().get(valueHolder.customerSelected).setCOMPLETE_STATUS("W");
			valueHolder.setIsProblemExist("N");
		}

		btnComplete.setVisibility(View.INVISIBLE);
		btnProblem.setVisibility(View.INVISIBLE);
		btnHomeClickStatus(false);
		if(ILogisticsApplication.problemListArr != null && ILogisticsApplication.problemListArr.size() > 0) {
			ILogisticsApplication.problemListArr.clear();
		}
	}
    
    /*
    @Override
    protected void onRestart() {
    	if(customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getINV_STATUS().length() > 0) {
        	btnComplete.setVisibility(View.INVISIBLE);
        	btnProblem.setVisibility(View.INVISIBLE);
        }
    	super.onRestart();
    }
    */
    @Override
    protected void onDestroy() {
        //you may call the cancel() method but if it is not handled in doInBackground() method
    	/*
        if (getRoutingTask != null && getRoutingTask.getStatus() != AsyncTask.Status.FINISHED)
        	getRoutingTask.cancel(true);
        */	
        super.onDestroy();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	super.onActivityResult(requestCode, resultCode, data);
    	if (requestCode == PRODUCT_PROBLEM_TABLE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
        		btnComplete.setVisibility(View.VISIBLE);
            	btnProblem.setVisibility(View.VISIBLE);
				btnHomeClickStatus(false);

            	if (ILogisticsApplication.problemListArr != null && ILogisticsApplication.problemListArr.size() > 0) {
                    lstView.setAdapter(null);
            		dataAdapter = new ListAdapter(getApplicationContext(), R.layout.product_item, customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getProductList());
                    lstView.setAdapter(dataAdapter);
                    lstView.setTextFilterEnabled(true);					
				}
			}
		}
    }
    
}