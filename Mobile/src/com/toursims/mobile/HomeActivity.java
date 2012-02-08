package com.toursims.mobile;

//import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HomeActivity extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setListAdapter(new ArrayAdapter<String>(this, R.layout.list_courses, COURSES));
        
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        
        
    }
    
    static final String[] COURSES = new String[] {
    	"LaDoua", "INSA", "Lyon1", "IUT-Feyssine"
    };
}

