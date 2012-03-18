package com.toursims.mobile.ui;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import com.toursims.mobile.CustomPreferences;
import com.toursims.mobile.R;
import com.toursims.mobile.controller.CourseBDD;
import com.toursims.mobile.model.Course;
import com.toursims.mobile.model.Trace;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ToolBox {
	public static AlertDialog.Builder getDialog(Context c) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(c);
		dialog.setIcon(R.drawable.ic_launcher);
		return dialog;
	}

	public static AlertDialog.Builder getDialogOK(Context c) {
		AlertDialog.Builder dialog = getDialog(c);
		dialog.setPositiveButton("OK", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		return dialog;
	}

	public static String cacheFile(String fileURL, String cachePath) {
		try {
			String fileName = cachePath + "/"
					+ fileURL.replaceAll("[.|/|:]", "_");
			File file = new File(fileName);

			if (!file.exists()) {
				URL url = new URL(fileURL);
				URLConnection ucon = url.openConnection();

				InputStream is = ucon.getInputStream();
				BufferedInputStream bis = new BufferedInputStream(is);

				ByteArrayBuffer baf = new ByteArrayBuffer(50);
				int current = 0;
				while ((current = bis.read()) != -1) {
					baf.append((byte) current);
				}

				/* Convert the Bytes read to a String. */
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(baf.toByteArray());
				fos.close();
			}
			if (file.exists()) {
				return file.getAbsolutePath();
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
				MeasureSpec.AT_MOST);
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	public static void deleteItem(Trace item, Context context) {
		CourseBDD datasource = null;
		try {
			datasource = new CourseBDD(context);
			datasource.open();
			datasource.delete(item);
			datasource.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (datasource != null) {
			datasource.close();
		}
	}
}
