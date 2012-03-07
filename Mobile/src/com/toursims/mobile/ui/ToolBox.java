package com.toursims.mobile.ui;

import com.toursims.mobile.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class ToolBox {
	public static  AlertDialog.Builder getDialog(Context c) {
       	AlertDialog.Builder dialog = new AlertDialog.Builder(c);
       	dialog.setIcon(R.drawable.ic_launcher);       	
       	return dialog;
	}
	
	public static AlertDialog.Builder getDialogOK(Context c){
		AlertDialog.Builder dialog = getDialog(c);
		dialog.setPositiveButton("OK", new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		
		return dialog;
	}
}
