package com.compass.activity;

/**
 * @author Paresh N. Mayani
 * http://www.technotalkative.com
 */

import com.google.common.base.Strings;

import com.compass.dbhelper.CheckInCheckOut;
import com.compass.dbhelper.CheckInCheckOutRepo;
import com.compass.dbhelper.InvoiceInfo;
import com.compass.dbhelper.InvoiceInfoRepo;
import com.compass.ilogistics.R;
import com.compass.model.CustomerModel;
import com.compass.model.InvoiceModel;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class MenuListActivity extends DashBoardActivity {
	
	private ValueHolder valueHolder;
	private ProgressDialog dialog;
	
	//private ListView lstView;
	private TextView txtRoute,txtDriver,txtLocation,txtCount,txtDocDate, completeStatus,problemStatus,normalStatus;
//	private TextView txtRoute,txtDriver,txtLocation,txtCount,txtDocDate;

	private EditText etDate;
	private GetRoutingTask getRoutingTask;
	
	private static final String SERVER_ERROR = "SERVER_ERROR";
	private static final String NETWORK_ERROR = "NETWORK_ERROR";
	private static final String CANCELLED = "CANCELLED";
	private static final String SUCCESS = "SUCCESS";
	
	ListAdapter dataAdapter = null;
	
	private String serviceResponse = null;
	//private ArrayList<HashMap<String, String>> serviceResponseList;
	private ArrayList<HashMap<String, String>> serviceResponseList;
	private ArrayList<Routing> routingList = new ArrayList<Routing>();
	ArrayList<HashMap<String, String>> MyArrList;
	private String[] items = { "Check Invoice", "View Delivery", "Route Job", "Scan Product"};
			
	private int[] colors = new int[] {0x30ffffff, 0x30f0f0f0};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_list);
        setHeader(getString(R.string.app_name), false, false, true); // to hide home button used false second argument.
        valueHolder = ValueHolder.getSingletonObject();    
//        lstView = (ListView)findViewById(R.id.list);
        txtDocDate = (TextView)findViewById(R.id.docDate);
        //etDate = (EditText)findViewById(R.id.deliveryDate);
        txtRoute = (TextView)findViewById(R.id.route);
        txtDriver = (TextView)findViewById(R.id.driver);
        txtLocation = (TextView)findViewById(R.id.location);
        txtCount = (TextView)findViewById(R.id.count);
        
        completeStatus = (TextView)findViewById(R.id.completeStatus);
        problemStatus = (TextView)findViewById(R.id.problemStatus);
        normalStatus = (TextView)findViewById(R.id.normalStatus);
        /*
        items[0] = getString(R.string.check_invoice);
        items[1] = getString(R.string.view_delivery);
        items[2] = getString(R.string.route_job);
        items[3] = getString(R.string.scan_product);
        */
        /*
        etDate.setText(valueHolder.getDeliveryDate());
        final Drawable x = getResources().getDrawable(R.drawable.ic_input_delete);
		x.setBounds(0, 0, x.getIntrinsicWidth(), x.getIntrinsicHeight());
		etDate.setCompoundDrawables(null, null, x, null);
		etDate.setOnTouchListener(new OnTouchListener() {
		    public boolean onTouch(View v, MotionEvent event) {
		        if (etDate.getCompoundDrawables()[2] == null) {
		            return false;
		        }
		        if (event.getAction() != MotionEvent.ACTION_UP) {
		            return false;
		        }
		        if (event.getX() > etDate.getWidth() - etDate.getPaddingRight() - x.getIntrinsicWidth()) {
		        	//etDate.setText("");
		        }else {
		        	final Calendar c = Calendar.getInstance();
					int y = c.get(Calendar.YEAR);
					int m = c.get(Calendar.MONTH);
					int d = c.get(Calendar.DAY_OF_MONTH);
					//final String[] MONTH = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
					DatePickerDialog dp = new DatePickerDialog(MenuListActivity.this,new DatePickerDialog.OnDateSetListener() {
								public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
									String month = String.valueOf(monthOfYear + 1);
									String erg = "";
									erg = String.valueOf(dayOfMonth);
									erg = (erg.length() < 2) ? "0".concat(erg) : erg;
									month = (month.length() < 2) ? "0".concat(month) : month;
									erg += "/" + month;
									erg += "/" + year;
									((TextView) etDate).setText(erg);
									valueHolder.setDeliveryDate(erg);
									startActivity(new Intent(MenuListActivity.this, MenuListActivity.class));
									finish();
								}
							}, y, m, d);
					dp.setTitle("Calender");
					dp.setMessage("Select Your Delivery Date Please?");
					dp.show();
		        }
		        return false;
		    }
		});
		*/
        MyArrList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map;
		
		map = new HashMap<String, String>();
		map.put("menu_id", "0");
		map.put("menu_name", getString(R.string.check_invoice));
		MyArrList.add(map);
		
		map = new HashMap<String, String>();
		map.put("menu_id", "1");
		map.put("menu_name", getString(R.string.view_delivery));
		MyArrList.add(map);
		
		map = new HashMap<String, String>();
		map.put("menu_id", "2");
		map.put("menu_name", getString(R.string.route_job));
		MyArrList.add(map);
		
		if (!Strings.isNullOrEmpty(valueHolder.getMenuID())
		   && valueHolder.getMenuID().equalsIgnoreCase("menu_admin")) {
			map = new HashMap<String, String>();
			map.put("menu_id", "3");
			map.put("menu_name", getString(R.string.view_invoice));
			MyArrList.add(map);
		}
		
		map = new HashMap<String, String>();
		map.put("menu_id", "4");
		map.put("menu_name", getString(R.string.view_problem));
		MyArrList.add(map);

		/*
		map = new HashMap<String, String>();
		map.put("menu_id", "5");
		map.put("menu_name", getString(R.string.scan_product));
		MyArrList.add(map);
        */

//        lstView.setAdapter(new ListMenuAdapter(this));

//        lstView.setOnItemClickListener(new OnItemClickListener() {
//			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
//				switch (Integer.parseInt(((TextView)view.findViewById(R.id.menuId)).getText().toString())) {
//					case 0:
//						startActivity(new Intent(getApplicationContext(), ScanActivity.class));
//						break;
//					case 1:
//						startActivity(new Intent(getApplicationContext(), CustomerListActivity.class));
//						break;
//					case 2:
//						startActivity(new Intent(getApplicationContext(), RouteJobListActivity.class));
//						break;
//					case 3:
//						startActivity(new Intent(getApplicationContext(), AllInvoiceListActivity.class));
//						break;
//					case 4:
//						startActivity(new Intent(getApplicationContext(), AllProblemListActivity.class));
//						break;	
//					default:
//						break;
//				}
//			}
//		});
       
        getRoutingTask = new GetRoutingTask();
        getRoutingTask.execute();
    }

	@Override protected void onResume() {
		super.onResume();

		if (new ConnectionDetector(this).isConnectingToInternet()) {
			syncLocalDataToServer();
		}

		updateInvoiceStatusFromLocalDatabase();
		updateInvoiceTypeCountViews();
	}

	public void onButtonClicker(View v)
    { 	
    	switch (v.getId()) {
		case R.id.btnDeliverMenu:
			startActivity(new Intent(getApplicationContext(), CustomerListActivity.class));
			break;
		case R.id.btnDocumentMenu:
			startActivity(new Intent(getApplicationContext(), ScanActivity.class));
			break;
		case R.id.btnMapMenu:
	    	Intent intent;
			intent = new Intent(getBaseContext(), RouteMapActivity.class);
			intent.putExtra("map", "route_map");
			startActivity(intent);
			break;
		case R.id.btnSpecialJobMenu:
			startActivity(new Intent(getApplicationContext(), RouteJobListActivity.class));
			break;
		case R.id.btnProblemMenu:
			startActivity(new Intent(getApplicationContext(), AllProblemListActivity.class));
			break;
		case R.id.btnHotlineMenu:
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(MenuListActivity.this);
			alertDialog.setMessage(getString(R.string.hotline_alert)).setCancelable(false)
			.setPositiveButton(getString(R.string.hotline_call), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {			
//					finish();
					dialog.cancel();
					  Intent callIntent = new Intent(Intent.ACTION_CALL);
					    callIntent.setData(Uri.parse("tel:0827905152"));
					    startActivity(callIntent);
				}
			})
			.setNegativeButton(getString(R.string.hotline_cancel), new DialogInterface.OnClickListener() {
			       public void onClick(DialogInterface dialog, int id) {
			            dialog.cancel();
			       }
			   });
//			AlertDialog.Builder alertDialog1 = alertDialog1.create();
			alertDialog.show();	
			break;
		default:
			break;
		}
    }


    public class GetRoutingTask extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			dialog = ProgressDialog.show(MenuListActivity.this, "", getString(R.string.wait));
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
					serviceResponseList = service.getRouting(valueHolder.getUsername(),Constants.DEVICE_ID, valueHolder.getCompanyCode(),valueHolder.getRoutingCode(), valueHolder.getDeliverySeq(), valueHolder.getDeliveryDate(), 1);
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
				if(serviceResponseList.size() > 0)
				{
				
				
				String dateStr = serviceResponseList.get(0).get("DOC_DATE");
				String formattedDate = null;
				Date convertedDate = new Date();
				try
				{
				DateFormat inputFormat = new SimpleDateFormat("yyyyMMdd");
				DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
				convertedDate = inputFormat.parse(dateStr);
				formattedDate = outputFormat.format(convertedDate);
				}
				catch(Exception e)
				{
					 e.printStackTrace();
				}
				
				txtDocDate.setText(getString(R.string.date)+ ":  "+ formattedDate);
//				txtRoute.setText("Route : "+serviceResponseList.get(0).get("ROUTE_CODE") +" - "+serviceResponseList.get(0).get("ROUTE_NAME") +" , Round : "+serviceResponseList.get(0).get("DELIVERY_SEQ"));
				txtRoute.setText("Route : "+serviceResponseList.get(0).get("ROUTE_CODE") +" , Round : "+serviceResponseList.get(0).get("DELIVERY_SEQ"));
				txtDriver.setText("Driver : "+serviceResponseList.get(0).get("DELIVER_NAME")+ "  "+ "Truck : "+serviceResponseList.get(0).get("PLATE_NO"));
//				txtLocation.setText("Location : "+serviceResponseList.get(0).get("LOCATION")+" Time : "+serviceResponseList.get(0).get("PICKUP_TIME"));
				txtLocation.setText("Time : "+serviceResponseList.get(0).get("PICKUP_TIME"));
				txtCount.setText(serviceResponseList.get(0).get("TOTAL_CUST") +  " " +  getString(R.string.customers)+" -  "+serviceResponseList.get(0).get("TOTAL_INVOICE")+  " " + getString(R.string.invoices));									
				}
			}
			else {
				Toast.makeText(getApplicationContext(), "No data found",Toast.LENGTH_SHORT).show();
			}
		}		
		protected void onPostExecute(String result) {}
	}  

/*************/
    public class ListMenuAdapter extends BaseAdapter 
    {
        private Context context;

        public ListMenuAdapter(Context c) 
        {
        	// TODO Auto-generated method stub
            context = c;
        }
 
        public int getCount() {
        	// TODO Auto-generated method stub
            return MyArrList.size();
        }
 
        public Object getItem(int position) {
        	// TODO Auto-generated method stub
            return position;
        }
 
        public long getItemId(int position) {
        	// TODO Auto-generated method stub
            return position;
        }
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub		
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.menu_item, null); 
			}
			
			TextView menuId = (TextView) convertView.findViewById(R.id.menuId);
			menuId.setText(MyArrList.get(position).get("menu_id"));
			TextView txtView1 = (TextView) convertView.findViewById(R.id.textView1);
			txtView1.setText(MyArrList.get(position).get("menu_name"));
			ImageView txtView2 = (ImageView) convertView.findViewById(R.id.textView2);
			switch (Integer.parseInt(MyArrList.get(position).get("menu_id"))) {
				case 0:
					txtView2.setBackgroundResource(R.drawable.stock_task48);
					break;
				case 1:
					txtView2.setBackgroundResource(R.drawable.text_x_log48);
					break;
				case 2:
					txtView2.setBackgroundResource(R.drawable.document_attach48);
					break;
				case 3:
					txtView2.setBackgroundResource(R.drawable.document48);
					break;
				case 4:
					txtView2.setBackgroundResource(R.drawable.comment32);
					break;	
				case 5:
					txtView2.setBackgroundResource(R.drawable.form_input_checkbox48);
					break;
				default:
					break;
			}
			
	        return convertView;				
		}

    }
/*************/    
    
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

	public void syncLocalDataToServer() {
		syncLocalCheckInCheckOutData();
		syncLocalInvoiceStatusData();
	}

	private void syncLocalCheckInCheckOutData() {
		CheckInCheckOutRepo checkInCheckOutRepo = new CheckInCheckOutRepo(this);
		List<CheckInCheckOut> listCheckInCheckOut = checkInCheckOutRepo.getCheckInCheckOutList();
		if (listCheckInCheckOut != null
			&& !listCheckInCheckOut.isEmpty()) {

			for (int i = 0; i < listCheckInCheckOut.size(); i++) {
				CheckInCheckOut checkInCheckOut = listCheckInCheckOut.get(i);
				if (checkInCheckOut != null) {
					CheckInCheckOutThread checkInCheckOutAsyncTask = new CheckInCheckOutThread(checkInCheckOut);
					checkInCheckOutAsyncTask.execute();
				}
			}
		}
	}

	private void syncLocalInvoiceStatusData() {
		InvoiceInfoRepo repo = new InvoiceInfoRepo(this);
		List<InvoiceInfo> uniqueInvoiceList = repo.selectUniqueInvoiceInfo();

		if (uniqueInvoiceList != null && !uniqueInvoiceList.isEmpty()) {
			for (int i = 0; i < uniqueInvoiceList.size(); i++) {
				InvoiceInfo uniqueInvInfo = uniqueInvoiceList.get(i);

				if (uniqueInvInfo != null
					&& !Strings.isNullOrEmpty(uniqueInvInfo.invoiceBook)
					&& !Strings.isNullOrEmpty(uniqueInvInfo.invoiceNumber)) {

					List<InvoiceInfo> problemList = repo.getInvoiceProblemList(uniqueInvInfo.invoiceBook,
							uniqueInvInfo.invoiceNumber);
					if (problemList != null
						&& !problemList.isEmpty()) {
						UploadInvoiceProblemList uploadInvoiceAsyncTask = new UploadInvoiceProblemList(problemList);
						uploadInvoiceAsyncTask.execute();
					}
				}
			}

		}
	}

	private class CheckInCheckOutThread extends AsyncTask<String, String, String> {
		ArrayList<HashMap<String, String>> serviceResponseCheckInAndOut;
		CheckInCheckOut checkInCheckOut;
		boolean isRecordUploaded;

		public CheckInCheckOutThread(CheckInCheckOut checkInCheckOut) {
			this.checkInCheckOut = checkInCheckOut;
		}

		@Override
		protected String doInBackground(String... params) {
			WebService service = new WebService();
			try {
				if (isCancelled()) {
					publishProgress(CANCELLED); //Notify your activity that you had canceled the task
					return (null); // don't forget to terminate this method
				}
				serviceResponseCheckInAndOut = service.CheckInAndOut(checkInCheckOut);

				publishProgress(SUCCESS); //if everything is Okay then publish this message, you may also use onPostExecute() method
			} catch (UnknownHostException e) {
				e.printStackTrace();
				publishProgress(NETWORK_ERROR);
			} catch (TimeoutException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
				publishProgress(SERVER_ERROR);
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(String... errorCode) {
			if (errorCode[0].toString().equalsIgnoreCase(SUCCESS)) {
				int flag = 0 ;
				for(int i = 0; i < serviceResponseCheckInAndOut.size(); i++) {
					flag = Integer.parseInt(serviceResponseCheckInAndOut.get(i).get("FLAG").toString());
				}

				if (flag == 1) {
					isRecordUploaded = true;
				}
			}
		}

		@Override protected void onPostExecute(String s) {
			super.onPostExecute(s);
			if (isRecordUploaded) {
				CheckInCheckOutRepo repo = new CheckInCheckOutRepo(MenuListActivity.this);
				repo.delete(checkInCheckOut);
			}
		}
	}

	private class UploadInvoiceProblemList extends AsyncTask<String, String, String> {
		ArrayList<HashMap<String, String>> uploadResponse;
		boolean isRecordUploaded;
		List<InvoiceInfo> problemList;

		public UploadInvoiceProblemList(List<InvoiceInfo> problemList) {
			this.problemList = problemList;
		}

		@Override
		protected String doInBackground(String... params) {
			WebService service = new WebService();
			try {
				if (isCancelled()) {
					publishProgress(CANCELLED); //Notify your activity that you had canceled the task
					return (null); // don't forget to terminate this method
				}

				uploadResponse = service.updateDeliverStatusFromLocalDatabse(problemList);

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
			if(errorCode[0].toString().equalsIgnoreCase(SUCCESS)) {
				int flag = 0 ;

				for (int i = 0; i < uploadResponse.size(); i++) {
					flag = Integer.parseInt(uploadResponse.get(i).get("FLAG").toString());
				}

				if (flag == 1) {
					isRecordUploaded = true;
				}
			}
		}

		protected void onPostExecute(String result) {
			if (isRecordUploaded) {
				InvoiceInfoRepo invoiceInfoRepo = new InvoiceInfoRepo(MenuListActivity.this);
				invoiceInfoRepo.delete(problemList.get(0));
			}
		}
	}

	private void updateInvoiceStatusFromLocalDatabase() {
		InvoiceInfoRepo repo = new InvoiceInfoRepo(this);
		List<InvoiceInfo> localList = repo.getInvoiceList();
		if (localList != null
			&& !localList.isEmpty()
			&& valueHolder.getCustomerList() != null
			&& !valueHolder.getCustomerList().isEmpty()) {

			for (int i = 0; i < valueHolder.getCustomerList().size(); i++) {
				if (valueHolder.getCustomerList().get(i).getInvoiceList() != null
					&& !valueHolder.getCustomerList().get(i).getInvoiceList().isEmpty()) {
					for (int j = 0; j < valueHolder.getCustomerList().get(i).getInvoiceList().size(); j++) {
						InvoiceStatus invoiceStatus = getInvoiceStatus(localList,
								valueHolder.getCustomerList().get(i).getInvoiceList().get(j));
						if (invoiceStatus == InvoiceStatus.HAS_PROBLEM) {
							valueHolder.getCustomerList().get(i).getInvoiceList().get(j).setINV_STATUS("W");
						} else if (invoiceStatus == InvoiceStatus.COMPLETED) {
							valueHolder.getCustomerList().get(i).getInvoiceList().get(j).setINV_STATUS("Y");
						}
					}
				}
			}
		}
	}

	private InvoiceStatus getInvoiceStatus(List<InvoiceInfo> localList, InvoiceModel invoiceModel) {
		InvoiceStatus invoiceStatus = InvoiceStatus.NOT_FOUND;

		int hasProblem = 0;
		boolean isFound = false;
		for (int i = 0; i < localList.size(); i++) {

			InvoiceInfo localInvoiceInfo = localList.get(i);
			if (localInvoiceInfo != null
				&& localInvoiceInfo.transactionType.equals(invoiceModel.TRANSACTION_TYPE)
				&& localInvoiceInfo.invoiceBook.equals(invoiceModel.INV_BOOK)
				&& localInvoiceInfo.invoiceNumber.equals(invoiceModel.INV_NO)) {

				isFound = true;

				if (!Strings.isNullOrEmpty(localInvoiceInfo.complainCode)) {
					hasProblem ++;
				}
			}
		}

		if (!isFound) {
			invoiceStatus = InvoiceStatus.NOT_FOUND;
		} else if (isFound && hasProblem == 0) {
			invoiceStatus = InvoiceStatus.COMPLETED;
		} else if (isFound && hasProblem > 0) {
			invoiceStatus = InvoiceStatus.HAS_PROBLEM;
		}
		return invoiceStatus;
	}

	private void updateInvoiceTypeCountViews() {
		int invComplete = 0, invProblem = 0, invNormal = 0;
		for (int i = 0; i < valueHolder.getCustomerList().size(); i++) {
			CustomerModel customer = valueHolder.getCustomerList().get(i);
			for (int j = 0; j < customer.getInvoiceList().size(); j++) {
				if (customer.getInvoiceList().get(j).getINV_STATUS().equalsIgnoreCase("Y")) {
					invComplete++;
				} else if (customer.getInvoiceList().get(j).getINV_STATUS().equalsIgnoreCase("W")) {
					invProblem++;
				} else {
					invNormal++;
				}
			}
		}
		completeStatus.setText(String.valueOf(invComplete) + " " + getString(R.string.invoicecomp));
		problemStatus.setText(String.valueOf(invProblem) + " " + getString(R.string.invoiceprob));
		normalStatus.setText(String.valueOf(invNormal) + " " + getString(R.string.invoicenotcom));
	}
}

