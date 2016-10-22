package com.compass.activity;

/**
 * @author Paresh N. Mayani
 * http://www.technotalkative.com
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.compass.ilogistics.PhoneNumberActivity;
import com.compass.ilogistics.R;
import com.compass.utils.Utilities;
import com.compass.utils.ValueHolder;

public class HomeAdminActivity extends Activity {
    /** Called when the activity is first created. */
	
	private ValueHolder valueHolder;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home_admin);
        //setHeader(getString(R.string.app_name), false, false);
        valueHolder = ValueHolder.getSingletonObject();
    }
    
    /**
     * Button click handler on Main activity
     * @param v
     */
    public void onButtonClicker(View v)
    {
    	Intent intent;
    	
    	switch (v.getId()) {
		case R.id.menu_assign:
			valueHolder.setMenuID("menu_assign");
			intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.menu_route:
			valueHolder.setMenuID("menu_admin");
			valueHolder.setRoutingCode("%");
			valueHolder.setDeliveryDate(Utilities.getCurrenceDate());
			intent = new Intent(this, RoutingListActivity.class);
			//intent = new Intent(this, SelectParamActivity.class);
			startActivity(intent);
			break;
		case R.id.menu_phone:
			//valueHolder.setMenuID("menu_contractor");
//			valueHolder.setRoutingCode("%");
//			valueHolder.setDeliveryDate(Utilities.getCurrenceDate());
//			intent = new Intent(this, RoutingListActivity.class);
//			//intent = new Intent(this, SelectParamActivity.class);
//			startActivity(intent);
			intent = new Intent(this, PhoneNumberActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
    }
}

