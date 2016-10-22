package com.compass.activity;


import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.Toast;

import com.compass.ilogistics.R;
import com.compass.model.CustomerModel;
import com.compass.model.Invoice;
import com.compass.utils.ValueHolder;
import com.compass.utils.WebService;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanActivity extends DashBoardActivity {
    
	public static Camera cam = null;
	private ValueHolder valueHolder;
	private ProgressDialog dialog;
	
	private ListView lstView;
	private EditText etFilter;
	private TextView txtLeft,txtRight;
	
	private static final String SERVER_ERROR = "SERVER_ERROR";
	private static final String NETWORK_ERROR = "NETWORK_ERROR";
	private static final String CANCELLED = "CANCELLED";
	private static final String SUCCESS = "SUCCESS";
	
	ListAdapter dataAdapter = null;
	
	private String serviceResponse = null;
	private ArrayList<CustomerModel> serviceResponseList;
	private ArrayList<Invoice> routingList = new ArrayList<Invoice>();
	private String barcode = "";
	
	private int[] colors = new int[] {0x30ffffff, 0x30f0f0f0};
	//List<String> scan = new ArrayList<String>();
	//String[] arr = scan.toArray(new String[scan.size()]);
	HashMap<String,String> scan = new HashMap<String,String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan);
        setHeader(getString(R.string.app_name), true, false, true);
        valueHolder = ValueHolder.getSingletonObject();
        etFilter = (EditText) findViewById(R.id.myFilter);
        lstView = (ListView)findViewById(R.id.list);
        txtLeft = (TextView)findViewById(R.id.left);
        txtRight = (TextView)findViewById(R.id.right);
        
        int total_invoice = 0;
        for(int i=0;i<valueHolder.getCustomerList().size();i++) {
        	CustomerModel customer  = valueHolder.getCustomerList().get(i);
			for(int j=0;j<customer.getInvoiceList().size();j++) {
				Invoice invoice = new Invoice(
						String.valueOf(i),
						customer.getInvoiceList().get(j).getCOMPANY(),
						customer.getInvoiceList().get(j).getTRANSACTION_TYPE(),
						customer.getInvoiceList().get(j).getINV_BOOK(),
						customer.getInvoiceList().get(j).getINV_NO(),
						customer.getInvoiceList().get(j).getINV_BOOK_NO()+ " - "+customer.getCUST_NAME(),
						customer.getInvoiceList().get(j).getINV_AMOUNT(),
						customer.getInvoiceList().get(j).getPAYMENT_TYPE(),
						customer.getInvoiceList().get(j).getJOB_ORDER(),
						customer.getInvoiceList().get(j).getINV_STATUS(),
						customer.getInvoiceList().get(j).getINV_LATITUDE(),
						customer.getInvoiceList().get(j).getINV_LONGITUDE(),
						customer.getInvoiceList().get(j).getINV_EXPECTDTIME());
				invoice.setINV_BARCODE(customer.getInvoiceList().get(j).getINV_BARCODE());
				invoice.setINV_SCAN(customer.getInvoiceList().get(j).getINV_SCAN());
				routingList.add(invoice);
				total_invoice = total_invoice + 1;
			}
        }
		
        txtLeft.setText(total_invoice +   " " + getString(R.string.invoices));
		txtRight.setText(valueHolder.getRoutingCode());
		
        dataAdapter = new ListAdapter(getApplicationContext(), R.layout.invoice_item, routingList);
        lstView.setAdapter(dataAdapter);
        lstView.setTextFilterEnabled(true);
        
        etFilter.addTextChangedListener(new TextWatcher() {
        	public void afterTextChanged(Editable s) { }			 
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				dataAdapter.getFilter().filter(s.toString());
			}
		 });
        
        
        
//        try {
//            Button scanner = (Button)findViewById(R.id.scanner);
//            scanner.setOnClickListener(new OnClickListener() {
//                public void onClick(View v) {
//                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//                    //intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
//                    intent.putExtra("SCAN_FORMATS", "QR_CODE");
//                    startActivityForResult(intent, 0);
//                }
//
//            });
//            
//            Button scanner2 = (Button)findViewById(R.id.scanner2);
//            scanner2.setOnClickListener(new OnClickListener() {
//                public void onClick(View v) {
//                	/*
//                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//                    //intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
//                    intent.putExtra("SCAN_FORMATS", "CODE_39");
//                    startActivityForResult(intent, 0);
//                    */
//                	IntentIntegrator integrator = new IntentIntegrator(ScanActivity.this);
//                    integrator.initiateScan();
//
//                }
// 
//            });
//            
//        } catch (ActivityNotFoundException anfe) {
//            Log.e("onCreate", "Scanner Not Found", anfe);
//        }
        
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
      IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
      if (resultCode == RESULT_OK) {
          String contents = intent.getStringExtra("SCAN_RESULT");
          String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
          barcode = contents;
          if(!scan.containsKey(barcode)) {
        	  scan.put(barcode, barcode);
          }          
          Intent intent1 = new Intent("com.google.zxing.client.android.SCAN");
          intent1.putExtra("SCAN_FORMATS", "CODE_39");
          startActivityForResult(intent, 0);
          //refreshList();
      } else if (resultCode == RESULT_CANCELED) {
          // Handle cancel
          Toast toast = Toast.makeText(this, "Scan was Cancelled!", Toast.LENGTH_SHORT);
          toast.setGravity(Gravity.TOP, 25, 400);
          toast.show();
          refreshList();
      }
      /*
      if (result != null) {
        String contents = result.getContents();
        if (contents != null) {
        	Toast toast = Toast.makeText(this, result.toString() , Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();
        } else {
        	Toast toast = Toast.makeText(this, "Scan was Cancelled!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();
        }
      }
      */
    }
    
    /*
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                // Handle successful scan
                barcode = contents;
                refreshList();
                
                //Toast toast = Toast.makeText(this, "Content:" + contents + " Format:" + format , Toast.LENGTH_SHORT);
                //toast.setGravity(Gravity.TOP, 25, 400);
                //toast.show();
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                Toast toast = Toast.makeText(this, "Scan was Cancelled!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
                
            }
        }
    }
    */
    
    
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
			 Button   buttonV;
		 }
  	 
		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
			  ViewHolder holder = null;
			  if (convertView == null) {
				  LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				  convertView = vi.inflate(R.layout.invoice_item, null);
				  holder = new ViewHolder();
				  holder.text1 = (ImageView) convertView.findViewById(R.id.textView1);
				  holder.text2 = (TextView) convertView.findViewById(R.id.textView2);
				  holder.text3 = (TextView) convertView.findViewById(R.id.textView3);
				  holder.text4 = (TextView) convertView.findViewById(R.id.textView4);
				  holder.buttonV = (Button) convertView.findViewById(R.id.btnView);
				  holder.buttonV.setVisibility(View.GONE);
				  ((LinearLayout) convertView.findViewById(R.id.r1)).setPadding(0, 10, 0, 10);
				  convertView.setTag(holder);
			  } else {
			   	holder = (ViewHolder) convertView.getTag();
			  }
		 
			  Invoice routing = filterList.get(position);
			  /*
			  if(routing.getINV_BARCODE().equalsIgnoreCase("Y")) {
				  holder.text1.setBackgroundResource(R.drawable.checkbox_full32);
			  }else if(routing.getINV_BARCODE().equalsIgnoreCase("N")) {
				  holder.text1.setBackgroundResource(R.drawable.checkboxred_full32);
			  }else {
				  holder.text1.setBackgroundResource(R.drawable.checkbox_unchecked_32);
			  }
			  */
			  if(routing.getINV_SCAN().equalsIgnoreCase("Y")) {
				  holder.text1.setBackgroundResource(R.drawable.checkbox_full32);
			  }else if(routing.getINV_SCAN().equalsIgnoreCase("N")) {
				  holder.text1.setBackgroundResource(R.drawable.checkbox_unchecked_32);
			  }else {
				  holder.text1.setBackgroundResource(R.drawable.checkbox_unchecked_32);
			  }
			  
			  
			  if(routing.getJOB_ORDER().equalsIgnoreCase("No Job Order")) {
				  holder.text2.setText(routing.getINV_BOOK_NO());
			  }
			  else {
				  holder.text2.setText(routing.getINV_BOOK_NO() + "  "+routing.getJOB_ORDER().trim());
			  }

			  
			  if(routing.getPAYMENT_TYPE().equalsIgnoreCase("CASH")) {
				  holder.text3.setText("CASH : "+routing.getINV_AMOUNT());
				  ((LinearLayout)convertView.findViewById(R.id.r2)).setVisibility(View.VISIBLE);
			  }else {
				  ((LinearLayout)convertView.findViewById(R.id.r2)).setVisibility(View.GONE);
				  holder.text3.setText("");
			  }
			  
			  //holder.buttonV.setTag(Integer.parseInt(routing.getPOSITION()));
			  //holder.buttonV.setOnClickListener(onViewClickListener);

			  convertView.setBackgroundColor(colors[position % colors.length]);
			   
		   return convertView;
		 
		  }
		  
		  private OnClickListener onViewClickListener = new OnClickListener() {
		        @Override
		        public void onClick(View v) {
		            final int position = lstView.getPositionForView((View) v.getParent());
	      		  	//valueHolder.setInvoiceSelected(position);
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
    
    

    private class UpdateInvoiceScan extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			dialog = ProgressDialog.show(ScanActivity.this, "", getString(R.string.wait));
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
					serviceResponse = service.updateScan(valueHolder.getDeliveryDate(), valueHolder.getRoutingCode(), valueHolder.getDeliverySeq(), scan);
					//serviceResponse = service.updateInvoiceScan("JB",params[0].toString());
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
					Toast.makeText(getApplicationContext(), R.string.serviceSuccess ,Toast.LENGTH_SHORT).show();
				}else if(serviceResponse.equalsIgnoreCase("false")) {
					Toast.makeText(getApplicationContext(), R.string.serviceFail, Toast.LENGTH_SHORT).show();
				}else {
					
				}
			}
		}
		protected void onPostExecute(String result) {
			scan.clear();
		}
	}
    
   
    
    public void refreshList() {
    	String scanFound = "";
    	String tempInv = "";
    	if(!scan.isEmpty()) {
	    	dataAdapter.clear();
	    	for(int i=0;i<valueHolder.getCustomerList().size();i++) {
	        	CustomerModel customer  = valueHolder.getCustomerList().get(i);
				for(int j=0;j<customer.getInvoiceList().size();j++) {
					Invoice invoice = new Invoice(
							String.valueOf(i),
							customer.getInvoiceList().get(j).getCOMPANY(),
							customer.getInvoiceList().get(j).getTRANSACTION_TYPE(),
							customer.getInvoiceList().get(j).getINV_BOOK(),
							customer.getInvoiceList().get(j).getINV_NO(),
							customer.getInvoiceList().get(j).getINV_BOOK_NO()+ " - "+customer.getCUST_NAME(),
							customer.getInvoiceList().get(j).getINV_AMOUNT(),
							customer.getInvoiceList().get(j).getPAYMENT_TYPE(),
							customer.getInvoiceList().get(j).getJOB_ORDER(),
							customer.getInvoiceList().get(j).getINV_STATUS(),
							customer.getInvoiceList().get(j).getINV_LATITUDE(),
							customer.getInvoiceList().get(j).getINV_LONGITUDE(),
							customer.getInvoiceList().get(j).getINV_EXPECTDTIME());
					invoice.setINV_BARCODE(customer.getInvoiceList().get(j).getINV_BARCODE());
					invoice.setINV_SCAN(customer.getInvoiceList().get(j).getINV_SCAN());
					tempInv = (customer.getInvoiceList().get(j).getINV_BOOK()+customer.getInvoiceList().get(j).getINV_NO());
					if(scan.containsKey(tempInv)) {
					//if(tempInv.equalsIgnoreCase(barcode)) {
						invoice.setINV_BARCODE("Y");
						valueHolder.getCustomerList().get(i).getInvoiceList().get(j).setINV_BARCODE("Y");
						valueHolder.getCustomerList().get(i).getInvoiceList().get(j).setINV_SCAN("Y");	
						invoice.setINV_SCAN("Y");					
					}
					routingList.add(invoice);
				}
	        }
	        dataAdapter = new ListAdapter(getApplicationContext(), R.layout.invoice_item, routingList);
	        lstView.setAdapter(dataAdapter);
	        dataAdapter.getFilter().filter(etFilter.getText().toString());
	        
	        new UpdateInvoiceScan().execute();
    	}
    }
    
    
    
    
    public void flashLightOn() {

        try {
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                cam = Camera.open();
                Parameters p = cam.getParameters();
                p.setFlashMode(Parameters.FLASH_MODE_TORCH);
                cam.setParameters(p);
                cam.startPreview();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Exception flashLightOn()",Toast.LENGTH_SHORT).show();
        }
    }

    public void flashLightOff() {
        try {
            if (getPackageManager().hasSystemFeature( PackageManager.FEATURE_CAMERA_FLASH)) {
                cam.stopPreview();
                cam.release();
                cam = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Exception flashLightOff", Toast.LENGTH_SHORT).show();
        }
    }
 
}