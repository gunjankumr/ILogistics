package com.compass.activity;

/**
 * @author Paresh N. Mayani
 * http://www.technotalkative.com
 */

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;

import com.compass.ilogistics.R;
import com.compass.model.CustomerModel;
import com.compass.model.ProblemSet;
import com.compass.model.ProductModel;
import com.compass.utils.ValueHolder;

public class AllProblemListActivity extends DashBoardActivity {
	
	private ValueHolder valueHolder;
	private ProgressDialog dialog;
	
	private ListView lstView;
	private TextView txtRight;
	
	private static final String SERVER_ERROR = "SERVER_ERROR";
	private static final String NETWORK_ERROR = "NETWORK_ERROR";
	private static final String CANCELLED = "CANCELLED";
	private static final String SUCCESS = "SUCCESS";
	
	ListAdapter dataAdapter = null;
	String[] planets;
	
	private int[] colors = new int[] {0x30ffffff, 0x30f0f0f0};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_problem_list);
        setHeader(getString(R.string.app_name), true, false,true);
        valueHolder = ValueHolder.getSingletonObject();
        lstView = (ListView)findViewById(R.id.list);
        txtRight = (TextView)findViewById(R.id.right);
        planets = getResources().getStringArray(R.array.problem_array);

        ArrayList<ProductModel> productList = new ArrayList<ProductModel>();
        for(int i=0; i<valueHolder.getCustomerList().size(); i++) {
	        CustomerModel customer  = valueHolder.getCustomerList().get(i);
			for(int j=0; j<customer.getInvoiceList().size(); j++) {
				for(int k=0; k<customer.getInvoiceList().get(j).getProductList().size(); k++) {
					ProductModel product = customer.getInvoiceList().get(j).getProductList().get(k);
					
					if(product.getPROBLEM_COMPLAIN() != null && !product.getPROBLEM_COMPLAIN().isEmpty() && product.getPROBLEM_COMPLAIN().length() > 0) {
						productList.add(product);
					}
				}				
			}
        }
        
        txtRight.setText(valueHolder.getRoutingCode());
        dataAdapter = new ListAdapter(getApplicationContext(), R.layout.all_problem_item, productList);
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
			 TextView textCustomer;
			 TextView textInvoice;
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
			  ViewHolder holder = null;
			  if (convertView == null) {  	 
				  LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				  convertView = vi.inflate(R.layout.all_problem_item, null);
				  holder = new ViewHolder();
				  holder.textCustomer = (TextView) convertView.findViewById(R.id.textCustomer);
				  holder.textInvoice = (TextView) convertView.findViewById(R.id.textInvoice);
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
			  holder.textCustomer.setText(product.getCUSTOMER());
			  SpannableString content = new SpannableString(product.getINVOICE());
			  content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
			  holder.textInvoice.setText(content);
			  holder.text1.setText(product.getPRODUCT_CODE() +" : "+product.getPRODUCT_NAME() );
			  holder.text2.setText("");
			  holder.text3.setText("");
			  holder.text4.setText("");
			  holder.text5.setText(product.getQTY()+"  "+product.getUNIT());
			  holder.text6.setText(product.getQTY_RETURN()+"  "+product.getUNIT());
			  
			  holder.textComplain.setText(getComplainDescription(product.getPROBLEM_COMPLAIN()));

//			  if(product.getPROBLEM_COMPLAIN().equalsIgnoreCase("PR-001")) {
//				  holder.textComplain.setText(planets[0]);
//			  }else if(product.getPROBLEM_COMPLAIN().equalsIgnoreCase("PR-014")) {
//				  holder.textComplain.setText(planets[1]);
//			  }else if(product.getPROBLEM_COMPLAIN().equalsIgnoreCase("LG-008")) {
//				  holder.textComplain.setText(planets[2]);
//			  }  
			  holder.textRemarks.setText(product.getPROBLEM_REMARK());
			  
			  convertView.setBackgroundColor(colors[position % colors.length]);
			   
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
    protected void onDestroy() {
        super.onDestroy();
    }

}