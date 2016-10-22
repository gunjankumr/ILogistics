package com.compass.utils;

import android.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;


public class AlertDialogManager {
    /**
     * Function to display simple Alert Dialog
     * @param context - application context
     * @param title - alert dialog title
     * @param message - alert message
     * @param status - success/failure (used to set icon)
     *               - pass null if you don't want icon
     * */
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        //AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(title)
                .setIcon((status) ? context.getResources().getIdentifier("success", "drawable", context.getPackageName()) : R.drawable.ic_menu_info_details)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("Close",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
         
                
        /*
        if(status != null) {
            // Setting alert dialog icon
            alertDialog.setIcon((status) ? R.drawable.ic_menu_info_details : R.drawable.ic_menu_info_details);
        }
        */
                
    }
}