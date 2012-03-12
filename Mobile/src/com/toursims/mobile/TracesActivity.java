package com.toursims.mobile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.toursims.mobile.controller.CourseBDD;
import com.toursims.mobile.model.Trace;
import com.toursims.mobile.model.kml.Point;
import com.toursims.mobile.ui.FlagAdapter;
import com.toursims.mobile.ui.TraceAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class TracesActivity extends Activity{
	
	private static List<Trace> items = new ArrayList<Trace>();
	private static List<Point> items2 = new ArrayList<Point>();
	private TraceAdapter adapter;
	private FlagAdapter adapter2;
	private ListView lv;
	private ListView lv2;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.traces);
		
	    CourseBDD datasource;
		try {
			datasource = new CourseBDD(this);
			datasource.open();
			items = datasource.getAllTraces();
			items2 = datasource.getAllPoints();
		    datasource.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		adapter = new TraceAdapter(this, items);
	    lv = (ListView) findViewById(R.id.lvListe);
	    lv.setTextFilterEnabled(true);
	    lv.setAdapter(adapter);
	    	    
		adapter2 = new FlagAdapter(this, items2);
	    lv2 = (ListView) findViewById(R.id.lvListe2);
	    lv2.setTextFilterEnabled(true);
	    lv2.setAdapter(adapter2);
	}
}
