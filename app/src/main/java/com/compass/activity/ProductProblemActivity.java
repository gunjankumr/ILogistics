package com.compass.activity;

/**
 * @author Paresh N. Mayani
 * http://www.technotalkative.com
 */

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.compass.ilogistics.R;
import com.compass.model.CustomerModel;
import com.compass.model.ProductModel;
import com.compass.model.Routing;
import com.compass.utils.ValueHolder;

public class ProductProblemActivity extends DashBoardActivity {
	
	private ValueHolder valueHolder;
	private ProgressDialog dialog;
	//private GestureDetector gDetector;
	private AlertDialog alertDialog;
	
	private ListView lstView;
	private TextView invDoc;
	private TextView btnComplete;
	
	private static final String SERVER_ERROR = "SERVER_ERROR";
	private static final String NETWORK_ERROR = "NETWORK_ERROR";
	private static final String CANCELLED = "CANCELLED";
	private static final String SUCCESS = "SUCCESS";
	
	ListAdapter dataAdapter = null;
	
	private String serviceResponse = null;
	private ArrayList<HashMap<String, String>> serviceResponseList;
	private ArrayList<Routing> routingList = new ArrayList<Routing>();
	private CustomerModel customerModel;
	
	private int[] colors = new int[] {0x30ffffff, 0x30f0f0f0};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_problem);
        setHeader(getString(R.string.app_name), true, false, true);
        //gDetector = new GestureDetector(ProductProblemActivity.this,this);
        valueHolder = ValueHolder.getSingletonObject();
        lstView = (ListView)findViewById(R.id.list);
        invDoc = (TextView)findViewById(R.id.invDoc);
        btnComplete = (Button)findViewById(R.id.btnComplete);

        customerModel = valueHolder.getCustomerList().get(valueHolder.customerSelected);
        if(customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getINV_STATUS().length() > 0) {
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
        
        ArrayList<ProductModel> productList = new ArrayList<ProductModel>();
        int c = 0;
        for(int i=0;i<4;i++) {
        	for(int j=0;j<customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getProductList().size();j++) {        	
        		 ProductModel product = new ProductModel();
    			 product.setPOSITION(""+i);
    			 product.setPRODUCT_CODE((customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getProductList().get(j)).getPRODUCT_CODE());
    			 product.setPRODUCT_NAME((customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getProductList().get(j)).getPRODUCT_NAME());
    			 product.setUNIT((customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getProductList().get(j)).getUNIT());
    			 product.setQTY((customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getProductList().get(j)).getQTY());
    			 product.setQTY_PICKED((customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getProductList().get(j)).getQTY_PICKED());
    			 product.setQTY_RETURN("");
    			 productList.add(product);
        		//productList.add(customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getProductList().get(j));
        	}
        }
        
        /*
        ArrayList<ProductModel> productList = new ArrayList<ProductModel>();
		 for(int i=0;i<30;i++) {
			 ProductModel product = new ProductModel();
			 product.setPOSITION(""+i);
			 product.setPRODUCT_CODE("Code"+i);
			 product.setPRODUCT_NAME("Name"+i);
			 product.setUNIT("Unit"+i);
			 product.setQTY("Qty"+i);
			 product.setQTY_PICKED("Picked"+i);
			 product.setQTY_RETURN("");
			 productList.add(product);
		 }
		 */
		 
        lstView.setItemsCanFocus(true);
        dataAdapter = new ListAdapter(getApplicationContext(), R.layout.product_problem_item, productList);
        //dataAdapter = new ListAdapter(getApplicationContext(), R.layout.product_item, customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getProductList());
        lstView.setAdapter(dataAdapter);
        //lstView.setTextFilterEnabled(true);

    }

/*************/
    private class ListAdapter extends ArrayAdapter<ProductModel> {
      	 
    	  //private ArrayList<ProductModel> originalList;
    	  private ArrayList<ProductModel> filterList;
    	  
    	  public ListAdapter(Context context, int textViewResourceId, ArrayList<ProductModel> fList) {
    	   super(context, textViewResourceId, fList);
    	   this.filterList = new ArrayList<ProductModel>();
    	   this.filterList.addAll(fList);
    	   //this.originalList = new ArrayList<ProductModel>();
    	   //this.originalList.addAll(fList);

    	  }
    	  /*
	  	 @Override
	  	 public Filter getFilter() {
	  		 if (filter == null){
	  			 filter  = new CustomerFilter();
	  		 }
	  		 return filter;
	  	 }
	 	*/
		 private class ViewHolder {
			 TextView text1;
			 TextView text2;
			 TextView text3;
			 TextView text4;
			 TextView text5;
			 EditText text6;
		 }
		 
		 
		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
			  ViewHolder holder = null;
			  if (convertView == null) {  	 
				  LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				  convertView = vi.inflate(R.layout.product_problem_item, null);
				  holder = new ViewHolder();
				  holder.text1 = (TextView) convertView.findViewById(R.id.textView1);
				  holder.text2 = (TextView) convertView.findViewById(R.id.textView2);
				  holder.text3 = (TextView) convertView.findViewById(R.id.textView3);
				  holder.text4 = (TextView) convertView.findViewById(R.id.textView4);
				  holder.text5 = (TextView) convertView.findViewById(R.id.textView5);
				  holder.text6 = (EditText) convertView.findViewById(R.id.textView6);
				  convertView.setTag(holder);
				  
				  holder.text6.setOnFocusChangeListener(new OnFocusChangeListener() {
			           public void onFocusChange(View v, boolean hasFocus) {
			               if (!hasFocus) {
			            	   EditText et = (EditText) v ; 
			            	   ProductModel product = (ProductModel) et.getTag();
			            	   product.setQTY_RETURN(et.getText().toString());
			               }
			           }
			       });
				  
			  } else {
			   	holder = (ViewHolder) convertView.getTag();
			  }
			  
			  ProductModel product = filterList.get(position);
			  holder.text1.setText(product.getPRODUCT_CODE());
			  holder.text2.setText("");
			  holder.text3.setText(product.getPRODUCT_NAME());
			  holder.text4.setText("");
			  holder.text5.setText(product.getQTY()+"  "+product.getUNIT());
			  holder.text6.setText(product.getQTY_RETURN());
			  holder.text6.setTag(product);
			  
			  convertView.setBackgroundColor(colors[position % colors.length]);
			   
		   return convertView;
		 
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
            builder = new AlertDialog.Builder(ProductProblemActivity.this);
            builder.setCancelable(false);
            builder.setView(layout);
            alertDialog = builder.create();
            alertDialog.show();
            
            //valueHolder.getCustomerList().get(valueHolder.customerSelected).getInvoiceList().get(valueHolder.getInvoiceSelected()).getPAYMENT_TYPE()
            if(customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getPAYMENT_TYPE().equalsIgnoreCase("CASH")) {
            	((LinearLayout) alertDialog.findViewById(R.id.r1)).setVisibility(View.VISIBLE);
            }
            ((TextView) alertDialog.findViewById(R.id.statusTxt)).setText("Y");
            btnSave = (Button) alertDialog.findViewById(R.id.btnSave);
            btnSave.setOnClickListener(new OnClickListener() {
    			public void onClick(View v) {
    				if(customerModel.getInvoiceList().get(valueHolder.getInvoiceSelected()).getPAYMENT_TYPE().equalsIgnoreCase("CASH")) {
    	            	if(((EditText) alertDialog.findViewById(R.id.cashTxt)).getText().toString().trim().length() > 0) {
    	            		valueHolder.getCustomerList().get(valueHolder.customerSelected).getInvoiceList().get(valueHolder.getInvoiceSelected()).setINV_STATUS("Y");
    	            		alertDialog.dismiss();
    	            		
            			}else {
            				Toast.makeText(getApplicationContext(), getString(R.string.require_cash) ,Toast.LENGTH_SHORT).show();
            			}
    				}else {
    					valueHolder.getCustomerList().get(valueHolder.customerSelected).getInvoiceList().get(valueHolder.getInvoiceSelected()).setINV_STATUS("Y");			
    					alertDialog.dismiss();
    					
    				}
    			}
    		});
    		
			break;		
		default:
			break;
		}
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

