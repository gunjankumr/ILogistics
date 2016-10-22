package com.compass.activity;

/**
 * @author Paresh N. Mayani
 * http://www.technotalkative.com
 */

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
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

import com.compass.ilogistics.R;
import com.compass.model.CustomerModel;
import com.compass.model.Invoice;
import com.compass.utils.AlertDialogManager;
import com.compass.utils.Constants;
import com.compass.utils.ValueHolder;

public class AllInvoiceListActivity extends DashBoardActivity {
	
	private ValueHolder valueHolder;
	private ProgressDialog dialog;
	private AlertDialogManager alert = new AlertDialogManager();
	
	private ListView lstView;
	private EditText etFilter;
	private TextView txtLeft,txtRight, completeStatus, problemStatus, normalStatus;
	private Button btnCheck;
	
	private static final String SERVER_ERROR = "SERVER_ERROR";
	private static final String NETWORK_ERROR = "NETWORK_ERROR";
	private static final String CANCELLED = "CANCELLED";
	private static final String SUCCESS = "SUCCESS";
	
	ListAdapter dataAdapter = null;
	
	private String serviceResponse = null;
	private ArrayList<CustomerModel> serviceResponseList;
	private ArrayList<Invoice> routingList = new ArrayList<Invoice>();
	
	private int[] colors = new int[] {0x30ffffff, 0x30f0f0f0};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_invoice_list);
        setHeader(getString(R.string.app_name), true, false, true);
        valueHolder = ValueHolder.getSingletonObject();
        etFilter = (EditText) findViewById(R.id.myFilter);
        lstView = (ListView)findViewById(R.id.list);
        txtLeft = (TextView)findViewById(R.id.left);
        txtRight = (TextView)findViewById(R.id.right);
        /*
        completeStatus = (TextView)findViewById(R.id.completeStatus);
        problemStatus = (TextView)findViewById(R.id.problemStatus);
        normalStatus = (TextView)findViewById(R.id.normalStatus);
        */
        
        int invoiceCount = 0, invComplete = 0, invProblem = 0, invNormal = 0;
        for(int i=0; i<valueHolder.getCustomerList().size(); i++) {
	        CustomerModel customer  = valueHolder.getCustomerList().get(i);
			for(int j=0; j<customer.getInvoiceList().size(); j++) {
				Invoice invoice = new Invoice(
						String.valueOf(j),
						customer.getInvoiceList().get(j).getCOMPANY(),
						customer.getInvoiceList().get(j).getTRANSACTION_TYPE(),
						customer.getInvoiceList().get(j).getINV_BOOK(),
						customer.getInvoiceList().get(j).getINV_NO(),
						customer.getInvoiceList().get(j).getINV_BOOK_NO(),
						customer.getInvoiceList().get(j).getINV_AMOUNT(),
						customer.getInvoiceList().get(j).getPAYMENT_TYPE(),
						customer.getInvoiceList().get(j).getJOB_ORDER(),
						customer.getInvoiceList().get(j).getINV_STATUS(),
						customer.getInvoiceList().get(j).getINV_LATITUDE(),
						customer.getInvoiceList().get(j).getINV_LONGITUDE(),
						customer.getInvoiceList().get(j).getINV_EXPECTDTIME());
				invoice.setCUSTOMER_POSITION(String.valueOf(i));
				invoice.setCUSTOMER(customer.getCUST_CODE()+ " - " +customer.getCUST_NAME()+" "+"<U>"+customer.getBRANCH_NAME()+"</U>");
				invoice.setINV_PROBLEM(customer.getInvoiceList().get(j).getINV_PROBLEM());
				invoice.setINV_EXPECTDTIME(customer.getInvoiceList().get(j).getINV_EXPECTDTIME());
				routingList.add(invoice);
				invoiceCount++;
				if(customer.getInvoiceList().get(j).getINV_STATUS().equalsIgnoreCase("Y")) {
					invComplete++;
				}else if(customer.getInvoiceList().get(j).getINV_STATUS().equalsIgnoreCase("W")) {
					invProblem++;
				}else {
					invNormal++;
				}
			}
        }
        
        txtLeft.setText(valueHolder.getCustomerList().size()+" Customers. - "+ invoiceCount + " Invoices.");
		txtRight.setText(valueHolder.getRoutingCode());
		/*
		completeStatus.setText(String.valueOf(invComplete)+ " Invoice");
		problemStatus.setText(String.valueOf(invProblem)+ " Invoice");
		normalStatus.setText(String.valueOf(invNormal)+ " Invoice");
		*/
		
        dataAdapter = new ListAdapter(getApplicationContext(), R.layout.all_invoice_item, routingList);
        lstView.setAdapter(dataAdapter);
        lstView.setTextFilterEnabled(true);
        /*
        lstView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
        		valueHolder.setInvoiceSelected(position);
        		valueHolder.setChildSelected(false);
        		startActivity(new Intent(getApplicationContext(), ProductListActivity.class));
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
        
    }


/*************/
    private class ListAdapter extends ArrayAdapter<Invoice> {
      	 
    	  private ArrayList<Invoice> originalList;
    	  private ArrayList<Invoice> filterList;
    	  private CustomerFilter filter;
    	 
    	  public ListAdapter(Context context, int textViewResourceId, ArrayList<Invoice> fList) {
    	   super(context, textViewResourceId, fList);
    	   this.filterList = new ArrayList<Invoice>();
    	   this.filterList.addAll(fList);
    	   this.originalList = new ArrayList<Invoice>();
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
			 ImageView text1;
			 TextView text2;
			 TextView text3;
			 TextView text4;
			 TextView text5;
			 TextView textInvoicePosition;
			 TextView textCustomerPosition;
			 //Button   buttonP;
			 Button   buttonV;
		 }
    	 
		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
			  ViewHolder holder = null;
			  if (convertView == null) {
				  LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				  convertView = vi.inflate(R.layout.all_invoice_item, null);
				  holder = new ViewHolder();
				  holder.text1 = (ImageView) convertView.findViewById(R.id.textView1);
				  holder.text2 = (TextView) convertView.findViewById(R.id.textView2);
				  holder.text3 = (TextView) convertView.findViewById(R.id.textView3);
				  holder.text4 = (TextView) convertView.findViewById(R.id.textView4);
				  holder.text5 = (TextView) convertView.findViewById(R.id.textView5);
				  holder.textInvoicePosition = (TextView) convertView.findViewById(R.id.textInvoicePosition);
				  holder.textCustomerPosition = (TextView) convertView.findViewById(R.id.textCustomerPosition);
				  //holder.buttonP = (Button) convertView.findViewById(R.id.btnProblem);
				  holder.buttonV = (Button) convertView.findViewById(R.id.btnView);
				  convertView.setTag(holder);
			  } else {
			   	holder = (ViewHolder) convertView.getTag();
			  }
		 
			  Invoice routing = filterList.get(position);
			  if(routing.getINV_STATUS().equalsIgnoreCase("Y")) {
				  holder.text1.setBackgroundResource(R.drawable.checkbox_full32);
				  //holder.buttonP.setVisibility(View.INVISIBLE);
			  }else if(routing.getINV_STATUS().equalsIgnoreCase("W")) {
				  holder.text1.setBackgroundResource(R.drawable.checkboxred_full32);
				  //holder.buttonP.setVisibility(View.VISIBLE);
			  }else {
				  holder.text1.setBackgroundResource(R.drawable.checkbox_unchecked_32);
				  //holder.buttonP.setVisibility(View.INVISIBLE);
			  }
			  		  
			  holder.text2.setText(Html.fromHtml(routing.getCUSTOMER()));
			  holder.text3.setText(routing.getINV_BOOK_NO() + "  "+routing.getJOB_ORDER().trim()+ "  "+routing.getINV_EXPECTDTIME());
			  holder.text4.setText("");
			  if(routing.getPAYMENT_TYPE().equalsIgnoreCase("CASH")) {
				  holder.text5.setText("CASH : "+routing.getINV_AMOUNT()+"  ");
				  ((LinearLayout)convertView.findViewById(R.id.r3)).setVisibility(View.VISIBLE);
			  }else {
				  ((LinearLayout)convertView.findViewById(R.id.r3)).setVisibility(View.GONE);
				  holder.text5.setText("");
			  }
			  holder.textInvoicePosition.setText(routing.getPOSITION());
			  holder.textCustomerPosition.setText(routing.getCUSTOMER_POSITION());
			  
			  holder.buttonV.setTag(Integer.parseInt(routing.getPOSITION()));
			  holder.buttonV.setOnClickListener(onViewClickListener);
  
			  convertView.setBackgroundColor(colors[position % colors.length]);
			   
		   return convertView;
		 
		  }
		  
		  
		  private OnClickListener onViewClickListener = new OnClickListener() {
		        @Override
		        public void onClick(View v) {
		        	//final int position = (Integer)v.getTag();
		            final int position = lstView.getPositionForView((View) v.getParent());
		            //View rowView = lstView.getChildAt(position);
		            View rowView = lstView.getAdapter().getView(position, null, null);
		            valueHolder.setCustomerSelected(Integer.parseInt(((TextView)rowView.findViewById(R.id.textCustomerPosition)).getText().toString()));
		            valueHolder.setInvoiceSelected((Integer)v.getTag());
	      		  	valueHolder.setChildSelected(false);
	      		  	startActivity(new Intent(getApplicationContext(), ProductListActivity.class));
		        }
		    };
    	 
	private class CustomerFilter extends Filter {
    	   @Override
    	   protected FilterResults performFiltering(CharSequence constraint) {
    		   constraint = constraint.toString().toLowerCase();
    		   FilterResults result = new FilterResults();
    		   if(constraint != null && constraint.toString().length() > 0) {
    			   ArrayList<Invoice> filteredItems = new ArrayList<Invoice>();
    			   for(int i = 0, l = originalList.size(); i < l; i++) {
    				   Invoice country = originalList.get(i);
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
    		   filterList = (ArrayList<Invoice>)results.values;
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
			intent = new Intent(AllInvoiceListActivity.this, RouteMapActivity.class);
			intent.putExtra("map", "route_map");
			startActivity(intent);
			break;
		default:
			break;
		}
    }

    
    @Override
    protected void onDestroy() {
    	Log.i(Constants.LOGI, getClass().getName()+" Destroy lear valueHolder clearLocation");
    	//valueHolder.clearLocation();
        super.onDestroy();
    }
    
}

