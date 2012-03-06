package com.toursims.mobile.ui;

import com.toursims.mobile.R;

import android.app.AlertDialog;
import android.content.Context;

public class ToolBox {
	public static  AlertDialog.Builder getDialog(Context c) {
       	AlertDialog.Builder dialog = new AlertDialog.Builder(c);
       	dialog.setIcon(R.drawable.ic_launcher);       	
       	return dialog;
	}
}
