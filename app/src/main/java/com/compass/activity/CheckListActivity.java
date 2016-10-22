package com.compass.activity;

/**
 * @author Paresh N. Mayani
 * http://www.technotalkative.com
 */
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.compass.ilogistics.R;
import com.compass.model.Country;
 
public class CheckListActivity extends Activity {
 
 MyCustomAdapter dataAdapter = null;
 
 @Override
 public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  //setContentView(R.layout.check_list);
  setContentView(R.layout.check_list);
 
  //Generate list View from ArrayList
  displayListView();
 
  checkButtonClick();
 
 }
 
 private void displayListView() {
 
  //Array list of countries
  ArrayList<Country> countryList = new ArrayList<Country>();
  
  for(int i=0;i<30;i++) {
	  Country country = new Country("AFG","Afghanistan : "+i, ""+i, false);
	  countryList.add(country);
  }
  
  /*
  Country country = new Country("AFG","Afghanistan",false);
  countryList.add(country);
  country = new Country("ALB","Albania",true);
  countryList.add(country);
  country = new Country("DZA","Algeria",false);
  countryList.add(country);
  country = new Country("ASM","American Samoa",true);
  countryList.add(country);
  country = new Country("AND","Andorra",true);
  countryList.add(country);
  country = new Country("AGO","Angola",false);
  countryList.add(country);
  country = new Country("AIA","Anguilla",false);
  countryList.add(country);
  */
  
  //create an ArrayAdaptar from the String Array
  dataAdapter = new MyCustomAdapter(this, R.layout.check_item, countryList);
  ListView listView = (ListView) findViewById(R.id.listView1);
  // Assign adapter to ListView
  listView.setAdapter(dataAdapter);
 
 
  listView.setOnItemClickListener(new OnItemClickListener() {
   public void onItemClick(AdapterView<?> parent, View view,
     int position, long id) {
    // When clicked, show a toast with the TextView text
    Country country = (Country) parent.getItemAtPosition(position);
    Toast.makeText(getApplicationContext(),
      "Clicked on Row: " + country.getName(),
      Toast.LENGTH_LONG).show();
   }
  });
 
 }
 
 private class MyCustomAdapter extends ArrayAdapter<Country> {
 
  private ArrayList<Country> countryList;
 
  public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<Country> countryList) {
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
   Log.v("ConvertView", String.valueOf(position));
 
   if (convertView == null) {
	   LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	   convertView = vi.inflate(R.layout.check_item, null); 
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
 
 private void checkButtonClick() {
 
 
  Button myButton = (Button) findViewById(R.id.findSelected);
  myButton.setOnClickListener(new OnClickListener() {
 
   @Override
   public void onClick(View v) {
 
    StringBuffer responseText = new StringBuffer();
    responseText.append("The following were selected...\n");
 
    ArrayList<Country> countryList = dataAdapter.countryList;
    for(int i=0;i<countryList.size();i++){
     Country country = countryList.get(i);
     if(country.isSelected()){
      responseText.append("\n" + country.getName());
     }
    }
 
    Toast.makeText(getApplicationContext(),
      responseText, Toast.LENGTH_LONG).show();
 
   }
  });
 
 }
 
}

