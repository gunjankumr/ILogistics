package com.compass.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.compass.ilogistics.R;
import com.compass.utils.AlertDialogManager;
import com.compass.utils.ConnectionDetector;
import com.compass.utils.SessionManager;
import com.compass.utils.WebService;

public class MainActivity1 extends Activity {
	
	Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;
	SessionManager session;
	
	AlertDialogManager alert = new AlertDialogManager();
	private ProgressDialog dialog;
	ArrayList<HashMap<String, String>> incomeList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main1);

        Button btn_find = (Button) findViewById(R.id.btn_find);			
		btn_find.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				EditText etLocation = (EditText) findViewById(R.id.et_location);				
				String location = etLocation.getText().toString();
				new CustomerStaffThread().execute(location);
			}
		});		
		
		
	}// end onCreate

	
	
	private class CustomerStaffThread extends AsyncTask<String, String, String> {
		
		protected void onPreExecute() {
			dialog = ProgressDialog.show(MainActivity1.this, "", "");
			dialog.setCancelable(true);
		}

		@Override
		protected String doInBackground(String... params) {
				WebService service = new WebService();
				//arrList = service.getCustomerStaff1(getApplicationContext());
				incomeList = service.getIncomingInvoice1(getApplicationContext());
			return null;
		}
		
		protected void onPostExecute(String result) {
			dialog.dismiss();
		}
	}
	
}//end MainClass

