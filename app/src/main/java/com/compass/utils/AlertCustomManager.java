package com.compass.utils;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.compass.ilogistics.R;


public class AlertCustomManager {

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
    	/*
    	 View customTitle = View.inflate(mContext, R.layout.alert_dialog_title, null);
         mTitle = (TextView) customTitle.findViewById(R.id.alertTitle);
         mIcon = (ImageView) customTitle.findViewById(R.id.icon);
         setCustomTitle(customTitle);
    	*/
    	View av = LayoutInflater.from(context).inflate(R.layout.dialog_custom,null);
    	TextView text = (TextView) av.findViewById(R.id.TextView01);
        text.setText("section 1 XX");
        
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(title)
                //.setView(LayoutInflater.from(context).inflate(R.layout.dialog_custom,null))
                .setView(av)
                //.setIcon((status) ? context.getResources().getIdentifier("success", "drawable", context.getPackageName()) : R.drawable.ic_menu_info_details)
                //.setMessage(message)
                .setCancelable(false)
                .setNegativeButton("Close",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();            
                alert.show();
                
                
                
                /*
                TextView text = (TextView) dialog.findViewById(R.id.TextView01);
                text.setText(R.string.lots_of_text);
 
                //set up image view
                ImageView img = (ImageView) dialog.findViewById(R.id.ImageView01);
                img.setImageResource(R.drawable.nista_logo);
 
                //set up button
                Button button = (Button) dialog.findViewById(R.id.Button01);
                button.setOnClickListener(new OnClickListener() {
                @Override
                    public void onClick(View v) {
                        finish();
                    }
                }); 
                */
    }
}