package com.compass.activity;

/**
 * @author Paresh N. Mayani
 * http://www.technotalkative.com
 */

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.compass.ilogistics.R;
import com.compass.model.Customer;
import com.compass.model.CustomerModel;
import com.compass.utils.AlertDialogManager;
import com.compass.utils.SessionManager;
import com.compass.utils.ValueHolder;

public class CustomerListActivity extends DashBoardActivity {
	
	public static Activity activityInstance = null;
	private ValueHolder valueHolder;
	private SessionManager session;
	private ProgressDialog dialog;
	private AlertDialogManager alert = new AlertDialogManager();
	
	private ListView lstView;
	private EditText etFilter;
	private TextView txtLeft,txtRight;
	
	private static final String SERVER_ERROR = "SERVER_ERROR";
	private static final String NETWORK_ERROR = "NETWORK_ERROR";
	private static final String CANCELLED = "CANCELLED";
	private static final String SUCCESS = "SUCCESS";	
	ListAdapter dataAdapter = null;
	private ArrayList<CustomerModel> serviceResponseList;
	private ArrayList<Customer> routingList = new ArrayList<Customer>();	
	private int[] colors = new int[] {0x30ffffff, 0x30f0f0f0};
	ArrayList<Customer> originalList;
	ArrayList<Customer> filterList;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_list);
        setHeader(getString(R.string.app_name), true, false,true);
        CustomerListActivity.activityInstance = this;
        session = new SessionManager(getApplicationContext());
        valueHolder = ValueHolder.getSingletonObject();
        etFilter = (EditText) findViewById(R.id.myFilter);
        lstView = (ListView)findViewById(R.id.list);
        txtLeft = (TextView)findViewById(R.id.left);
        txtRight = (TextView)findViewById(R.id.right);
        
        
        for(int i=0;i<valueHolder.getCustomerList().size();i++) {
        	boolean cust_status = true;
        	boolean cust_problem = false;
        	CustomerModel customer  = valueHolder.getCustomerList().get(i); 	
        	if((customer.getCUST_CODE()+"-"+customer.getBRANCH_CODE()).equalsIgnoreCase(session.getPreferenceVal("CHECKIN"))) {
        		valueHolder.getCustomerList().get(i).setCHECKIN("Y");
			}else {
				valueHolder.getCustomerList().get(i).setCHECKIN("N");
			}
        	
			for(int j=0;j<customer.getInvoiceList().size();j++) {
				if(customer.getInvoiceList().get(j).getINV_STATUS().equalsIgnoreCase("")) {
					cust_status = false;
				}
				if(customer.getInvoiceList().get(j).getINV_STATUS().equalsIgnoreCase("N")) {
					cust_status = false;
				}
				if(customer.getInvoiceList().get(j).getINV_STATUS().equalsIgnoreCase("W")) {
					cust_problem = true;
				}
			}			
			if(cust_status) {
				if(cust_problem) {
					valueHolder.getCustomerList().get(i).setCOMPLETE_STATUS("W");
				}else {
					valueHolder.getCustomerList().get(i).setCOMPLETE_STATUS("Y");
				}
			}
        }
        	
		
        serviceResponseList = valueHolder.getCustomerList();
        routingList = new ArrayList<Customer>();
		for(int i=0;i<serviceResponseList.size();i++) {
			Customer customer = new Customer(
				String.valueOf(i),
				serviceResponseList.get(i).getCUST_CODE(),
				serviceResponseList.get(i).getCUST_NAME(),
				serviceResponseList.get(i).getTOTAL_INVOICE(),
				serviceResponseList.get(i).getCOMPLETE_STATUS(),
				serviceResponseList.get(i).getTOTAL_REMARK());
			customer.setBRANCH_CODE(serviceResponseList.get(i).getBRANCH_CODE());
			customer.setBRANCH_NAME(serviceResponseList.get(i).getBRANCH_NAME());
			customer.setEXPECTDTIME(serviceResponseList.get(i).getEXPECTDTIME());
			customer.setCHECKIN(serviceResponseList.get(i).getCHECKIN());
			customer.setCASH_STATUS(serviceResponseList.get(i).getCASH_STATUS());
			routingList.add(customer);
		}
		txtLeft.setText(valueHolder.getCustomerList().size() +  " " +  getString(R.string.customers));
		txtRight.setText(valueHolder.getRoutingCode());
        dataAdapter = new ListAdapter(getApplicationContext(), R.layout.routing_item, routingList);
        lstView.setAdapter(dataAdapter);
        lstView.setTextFilterEnabled(true);
        
        /*
        lstView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
        		//valueHolder.setCustomerSelected(position);
				valueHolder.setCustomerSelected(Integer.parseInt(((TextView)view.findViewById(R.id.textPosition)).getText().toString()));
        		valueHolder.setInvoiceSelected(-1);
        		valueHolder.setChildSelected(false);
        		Button status = (Button) findViewById(R.id.text5);
        		if(status.getText().toString().equalsIgnoreCase("check in")) {
        			//status.setText("Check Out");
        			//valueHolder.getCustomerList().get(position).setSTATUS("Check Out");
        		}else {
        			//status.setText("Check In");
        			//valueHolder.getCustomerList().get(position).setSTATUS("Check In");
        		}
			}
		});
        */
        
        etFilter.addTextChangedListener(new TextWatcher() {
        	public void afterTextChanged(Editable s) { }			 
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				dataAdapter.getFilter().filter(s.toString());
			}
		 });
		 
        //getRoutingTask = new GetRoutingTask();
        //getRoutingTask.execute();
    }


/*************/
    private class ListAdapter extends ArrayAdapter<Customer> implements OnClickListener {
      	  
    	  //private ArrayList<Customer> originalList;
    	  //private ArrayList<Customer> filterList;
    	  private CustomerFilter filter;
    	
    	  public ListAdapter(Context context, int textViewResourceId, ArrayList<Customer> fList) {
    	   super(context, textViewResourceId, fList);
    	   filterList = new ArrayList<Customer>();
    	   filterList.addAll(fList);
    	   originalList = new ArrayList<Customer>();
    	   originalList.addAll(fList);
    	   /*
    	   this.filterList = new ArrayList<Customer>();
    	   this.filterList.addAll(fList);
    	   this.originalList = new ArrayList<Customer>();
    	   this.originalList.addAll(fList);
    	   */
    	  }

	  	 @Override
	  	 public Filter getFilter() {
	  		 if (filter == null){
	  			 filter  = new CustomerFilter();
	  		 }
	  		 return filter;
	  	 }
	 
		 private class ViewHolder {
			 TextView textPosition;
			 ImageView text1;
			 TextView text2;
			 TextView text3;
			 TextView text4;
			 ImageView textCash;
			 Button   buttonV;
			 //Button   buttonC;		
		 }
    	 
		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
			  ViewHolder holder = null;
			  if (convertView == null) {
				  LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				  convertView = vi.inflate(R.layout.customer_item, null);
				  holder = new ViewHolder();
				  holder.textPosition = (TextView) convertView.findViewById(R.id.textPosition);
				  holder.text1 = (ImageView) convertView.findViewById(R.id.textView1);
				  holder.text2 = (TextView) convertView.findViewById(R.id.textView2);
				  holder.text3 = (TextView) convertView.findViewById(R.id.textView3);
				  holder.text4 = (TextView) convertView.findViewById(R.id.textView4);
				  holder.textCash = (ImageView) convertView.findViewById(R.id.textCash);
				  holder.buttonV = (Button) convertView.findViewById(R.id.btnView);
				  //holder.buttonC = (Button) convertView.findViewById(R.id.text5);
				  convertView.setTag(holder);
			  } else {
			   	holder = (ViewHolder) convertView.getTag();
			  }
			  
			  Customer routing = filterList.get(position);
			  holder.textPosition.setText(routing.getPOSITION());
			  if(routing.getCOMPLETE_STATUS().equalsIgnoreCase("Y")) {
				  holder.text1.setBackgroundResource(R.drawable.checkbox_full32);
			  }else if(routing.getCOMPLETE_STATUS().equalsIgnoreCase("W")) {
				  holder.text1.setBackgroundResource(R.drawable.checkbox_checkedred_32);
			  }else {
				  holder.text1.setBackgroundResource(R.drawable.checkbox_unchecked_32);
			  }

//			  holder.text2.setText(Html.fromHtml(routing.getCUST_CODE()+" - "+routing.getCUST_NAME()+" "+"<U>"+routing.getBRANCH_NAME()+"</U>"+ " " +routing.getEXPECTDTIME()), TextView.BufferType.SPANNABLE);			  

			  String text = routing.getCUST_CODE()+" - "+routing.getCUST_NAME()+"</font>"+" "+"<U>"+routing.getBRANCH_NAME()+"</U>"+ " " +"<font color=#720000>"+routing.getEXPECTDTIME();
//			  holder.text2.setText(Html.fromHtml(text));
			  
			  if(routing.getEXPECTDTIME().equalsIgnoreCase("All Day")) {
				  holder.text2.setText(Html.fromHtml(routing.getCUST_CODE()+" - "+routing.getCUST_NAME()+" "+"<U>"+routing.getBRANCH_NAME()+"</U>"), TextView.BufferType.SPANNABLE);			  
			  }
			  else {
				  holder.text2.setText(Html.fromHtml(text));
			  }
			  holder.text3.setText(routing.getTOTAL_INVOICE()+  " " + getString(R.string.invoices));
//			  if(!(routing.getTOTAL_REMARK().equalsIgnoreCase("0"))) {
//				  holder.text4.setText(routing.getTOTAL_REMARK()+" Remarks ");
//			  }else {
//				  holder.text4.setText("");
//			  }
			  
			  holder.text4.setText("");
			  
			  if(routing.getCASH_STATUS().equalsIgnoreCase("Y")) {
				  holder.textCash.setVisibility(View.VISIBLE);
			  }else {
				  holder.textCash.setVisibility(View.GONE);
			  }
			  //holder.buttonC.setText(routing.getSTAUS());
			  /*
			  if(routing.getSTAUS().equalsIgnoreCase("Check In")) {
				  holder.buttonC.setBackgroundResource(R.drawable.btn_blue_glossy);
			  }else {
				  holder.buttonC.setBackgroundResource(R.drawable.btn_pink_glossy);  
			  }
			  */
			  //holder.buttonV.setTag(position);
			  //holder.buttonV.setOnClickListener(this);
			  if(routing.getCHECKIN().equalsIgnoreCase("Y")) {
				  Drawable drawableLeft = getResources().getDrawable(R.drawable.flag_red32);
				  holder.buttonV.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null , null, null);
			  }else {
				  holder.buttonV.setCompoundDrawablesWithIntrinsicBounds(null, null , null, null);
			  }
			  holder.buttonV.setTag(Integer.parseInt(routing.getPOSITION()));
			  holder.buttonV.setOnClickListener(onViewClickListener);
			  //holder.buttonC.setTag(position);
			  //holder.buttonC.setOnClickListener(this);
			  
			  convertView.setBackgroundColor(colors[position % colors.length]);
			   
		   return convertView;
		 
		  }
		  
		  private OnClickListener onViewClickListener = new OnClickListener() {
		        @Override
		        public void onClick(View v) {
		            final int position = lstView.getPositionForView((View) v.getParent());
		            //valueHolder.setCustomerSelected(position);		            
		            valueHolder.setCustomerSelected((Integer)v.getTag());
	      		  	valueHolder.setInvoiceSelected(-1);
	      		  	valueHolder.setChildSelected(false);
	      		  	startActivity(new Intent(getApplicationContext(), InvoiceListActivity.class));		            
		        }
		    };
		    
		  public void onClick(View v) {
			  int position=(Integer)v.getTag();
			  valueHolder.setCustomerSelected(position);
      		  valueHolder.setInvoiceSelected(-1);
      		  valueHolder.setChildSelected(false);
      		  /*
      		  //Toast.makeText(getApplicationContext(), "Network connection error"+ v.getId(),Toast.LENGTH_SHORT).show();
      		  if(((Button)v.findViewById(R.id.text5)).getText().toString().equalsIgnoreCase("Check In")) {
      			((Button)v.findViewById(R.id.text5)).setText("Check Out");
      			((Button)v.findViewById(R.id.text5)).setBackgroundResource(R.drawable.btn_pink_glossy);
      			filterList.get(position).setSTAUS("Check Out");
      			startActivity(new Intent(getApplicationContext(), ProductListActivity.class));
      		  }else {
      			((Button)v.findViewById(R.id.text5)).setText("Check In");
      			((Button)v.findViewById(R.id.text5)).setBackgroundResource(R.drawable.btn_blue_glossy);
      			filterList.get(position).setSTAUS("Check In");
      		  }
      		  */
		  }
    	 
	private class CustomerFilter extends Filter {
    	   @Override
    	   protected FilterResults performFiltering(CharSequence constraint) {
    		   constraint = constraint.toString().toLowerCase();
    		   FilterResults result = new FilterResults();
    		   if(constraint != null && constraint.toString().length() > 0) {
    			   ArrayList<Customer> filteredItems = new ArrayList<Customer>();
    			   for(int i = 0, l = originalList.size(); i < l; i++) {
    				   Customer country = originalList.get(i);
    				   if(country.toString().toLowerCase().contains(constraint))
    					   filteredItems.add(country);
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
    		   filterList = (ArrayList<Customer>)results.values;
    		   notifyDataSetChanged();
    		   clear();
    		   for(int i = 0, l = filterList.size(); i < l; i++) {
    			   add(filterList.get(i));
    		   }
    		   notifyDataSetInvalidated();
    	   	}
		}
    }
/*************/    
    
    public void onButtonClicker(View v)
    { 	
    	Intent intent;
    	switch (v.getId()) {
		case R.id.btnMap:            
			intent = new Intent(getBaseContext(), RouteMapActivity.class);
			intent.putExtra("map", "route_map");
			startActivity(intent);
			break;

		case R.id.btnJob:		
			intent = new Intent(getBaseContext(), RouteJobListActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
    }

    
    /*
    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        mListState = state.getParcelable(LIST_STATE);
    }
	*/
    
    @Override
    protected void onRestart() {
        super.onRestart();
        for(int i=0;i<valueHolder.getCustomerList().size();i++) {
        	boolean cust_status = true;
        	boolean cust_problem = false;
        	CustomerModel customer  = valueHolder.getCustomerList().get(i);
			for(int j=0;j<customer.getInvoiceList().size();j++) {
				if(customer.getInvoiceList().get(j).getINV_STATUS().equalsIgnoreCase("")) {
					cust_status = false;
				}
				if(customer.getInvoiceList().get(j).getINV_STATUS().equalsIgnoreCase("N")) {
					cust_status = false;
				}
				if(customer.getInvoiceList().get(j).getINV_STATUS().equalsIgnoreCase("W")) {
					cust_problem = true;
				}
			}
			if(cust_status) {
				if(cust_problem) {
					valueHolder.getCustomerList().get(i).setCOMPLETE_STATUS("W");
				}else {
					valueHolder.getCustomerList().get(i).setCOMPLETE_STATUS("Y");
				}
			}
        }
        
        for(int i=0;i<valueHolder.getCustomerList().size();i++) {
        	for(int j=0;j<filterList.size();j++) {
        		if(valueHolder.getCustomerList().get(i).getCUST_CODE().equalsIgnoreCase(filterList.get(j).getCUST_CODE()) && valueHolder.getCustomerList().get(i).getBRANCH_CODE().equalsIgnoreCase(filterList.get(j).getBRANCH_CODE())) { 
        			filterList.get(j).setCOMPLETE_STATUS(valueHolder.getCustomerList().get(i).getCOMPLETE_STATUS());
        			filterList.get(j).setCHECKIN(valueHolder.getCustomerList().get(i).getCHECKIN());
        		}
        	}
        }
       
		dataAdapter.notifyDataSetChanged();
    }
    
    
    /*
    @Override
    protected void onRestart() {
    	Log.i(Constants.LOGI, getClass().getName()+" RESTART");
    	dataAdapter.clear();
    	for(int i=0;i<valueHolder.getCustomerList().size();i++) {
        	boolean cust_status = true;
        	boolean cust_problem = false;
        	CustomerModel customer  = valueHolder.getCustomerList().get(i);
			for(int j=0;j<customer.getInvoiceList().size();j++) {
				customer.getInvoiceList().get(j).getINV_STATUS();
				if(customer.getInvoiceList().get(j).getINV_STATUS().equalsIgnoreCase("")) {
					cust_status = false;
				}
				if(customer.getInvoiceList().get(j).getINV_STATUS().equalsIgnoreCase("N")) {
					cust_status = false;
				}
				if(customer.getInvoiceList().get(j).getINV_STATUS().equalsIgnoreCase("W")) {
					cust_problem = true;
				}
			}
			if(cust_status) {
				if(cust_problem) {
					valueHolder.getCustomerList().get(i).setCOMPLETE_STATUS("W");
				}else {
					valueHolder.getCustomerList().get(i).setCOMPLETE_STATUS("Y");
				}
			}
        }

        serviceResponseList = valueHolder.getCustomerList();       
        routingList = new ArrayList<Customer>();
		for(int i=0;i<serviceResponseList.size();i++) {
			Customer customer = new Customer(
					String.valueOf(i),
					serviceResponseList.get(i).getCUST_CODE(),
					serviceResponseList.get(i).getCUST_NAME(),
					serviceResponseList.get(i).getTOTAL_INVOICE(),
					serviceResponseList.get(i).getCOMPLETE_STATUS(),
					serviceResponseList.get(i).getTOTAL_REMARK());
			customer.setEXPECTDTIME(serviceResponseList.get(i).getEXPECTDTIME());
			customer.setCHECKIN(serviceResponseList.get(i).getCHECKIN());
			routingList.add(customer);
		}
		dataAdapter = new ListAdapter(getApplicationContext(), R.layout.routing_item, routingList);
        lstView.setAdapter(dataAdapter);
        dataAdapter.getFilter().filter(etFilter.getText().toString());
    	super.onRestart();
    }
    */
 
}

