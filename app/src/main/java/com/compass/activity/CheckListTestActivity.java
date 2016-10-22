package com.compass.activity;

/**
 * @author Paresh N. Mayani
 * http://www.technotalkative.com
*/

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.compass.ilogistics.R;
import com.compass.model.CustomerModel;
import com.compass.model.ProductModel;
import com.compass.utils.ValueHolder;
 
public class CheckListTestActivity extends DashBoardActivity {
	
	private ValueHolder valueHolder;
	
	private ListView lstView;
	private TextView invDoc;
	private TextView btnComplete;
	
	ListAdapter dataAdapter = null;
	
	private CustomerModel customerModel;
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.check_list_test);
		 setHeader(getString(R.string.app_name), true, false, true);		 
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
         	}
         }
         
         /*
		 ArrayList<Country> countryList = new ArrayList<Country>();
		 for(int i=0;i<30;i++) {
			 Country country = new Country("AFG","Afghanistan : "+i, ""+i, false);
			 countryList.add(country);
		 }
         
		 dataAdapter = new ListAdapter(this, R.layout.check_item_test, countryList);
		 ListView listView = (ListView) findViewById(R.id.listView1);
		 listView.setAdapter(dataAdapter);
	  	*/
         
         
         dataAdapter = new ListAdapter(this, R.layout.product_problem_item, productList);
		 ListView listView = (ListView) findViewById(R.id.listView1);
		 listView.setAdapter(dataAdapter);
		 
	 }
	 
	 
	 
	 private class ListAdapter extends ArrayAdapter<ProductModel> {
		 
		 private ArrayList<ProductModel> filterList;
	 
		  public ListAdapter(Context context, int textViewResourceId, ArrayList<ProductModel> fList) {
		   super(context, textViewResourceId, fList);
		   this.filterList = new ArrayList<ProductModel>();
		   this.filterList.addAll(fList);
		  }
		 
		  private class ViewHolder {
			 TextView text1;
			 TextView text2;
			 TextView text3;
			 TextView text4;
			 TextView text5;
			 EditText text6;
			 
			 CheckBox name;
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
			   //holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
			   convertView.setTag(holder);
			   
			   /*
			   holder.name.setOnClickListener( new View.OnClickListener() { 
			     public void onClick(View v) { 
			      CheckBox cb = (CheckBox) v ; 
			      Country country = (Country) cb.getTag(); 
			      Toast.makeText(getApplicationContext(), "Clicked on Checkbox: " + cb.getText() +" is " + cb.isChecked(),Toast.LENGTH_LONG).show();
			      country.setSelected(cb.isChecked());
			     } 
			   }); 
			   */
			   
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

		   return convertView;
	 
	  }
	  
	 }
	 
	 
	 /*
	 private class ListAdapter extends ArrayAdapter<Country> {
	 
		 private ArrayList<Country> countryList;
	 
		  public ListAdapter(Context context, int textViewResourceId, ArrayList<Country> countryList) {
		   super(context, textViewResourceId, countryList);
		   this.countryList = new ArrayList<Country>();
		   this.countryList.addAll(countryList);
		  }
		 
		  private class ViewHolder {
		   TextView code;
		   EditText text;
		   CheckBox name;
		  }
		 
		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
		 
		   ViewHolder holder = null;
		 
		   if (convertView == null) {
			   LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			   convertView = vi.inflate(R.layout.check_item_test, null); 
			   holder = new ViewHolder();
			   holder.code = (TextView) convertView.findViewById(R.id.code);
			   holder.text = (EditText) convertView.findViewById(R.id.text);
			   holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
			   convertView.setTag(holder);
			 
			   holder.name.setOnClickListener( new View.OnClickListener() { 
			     public void onClick(View v) { 
			      CheckBox cb = (CheckBox) v ; 
			      Country country = (Country) cb.getTag(); 
			      Toast.makeText(getApplicationContext(), "Clicked on Checkbox: " + cb.getText() +" is " + cb.isChecked(),Toast.LENGTH_LONG).show();
			      country.setSelected(cb.isChecked());
			     } 
			   }); 
			   
			   holder.text.setOnFocusChangeListener(new OnFocusChangeListener() {
		           public void onFocusChange(View v, boolean hasFocus) {
		               if (!hasFocus) {
		            	   EditText et = (EditText) v ; 
		            	   Country country = (Country) et.getTag();
		            	   country.setText(et.getText().toString());
		               }
		           }
		       });
			   
		   } else {
			   holder = (ViewHolder) convertView.getTag();
		   }
	 
	   
		   Country country = countryList.get(position);
		   holder.code.setText(" (" +  country.getCode() + ")");
		   holder.name.setText(country.getName());
		   holder.name.setChecked(country.isSelected());
		   holder.name.setTag(country);
		   holder.text.setText(country.getText());
		   holder.text.setTag(country);
		   
		   return convertView;
	 
	  }
	  
	 }
	 */

 
}

