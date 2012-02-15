package com.toursims.mobile;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.toursims.mobile.controller.CourseBDD;
import com.toursims.mobile.controller.KmlParser;
import com.toursims.mobile.model.Course;
import com.toursims.mobile.model.kml.Placemark;
import com.toursims.mobile.model.kml.Point;
import com.toursims.mobile.ui.utils.CustomItemizedOverlay;

import android.graphics.drawable.Drawable;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class CourseStepActivity extends MapActivity{
    /** Called when the activity is first created. */
    private MapController mapController;
    List<Overlay> mapOverlays;
    Drawable drawable;
    CustomItemizedOverlay itemizedOverlay;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coursestep);
        
        MapView mapView = (MapView) findViewById(R.id.map);
		mapView.setBuiltInZoomControls(true);
		//mapView.setStreetView(true);
		mapController = mapView.getController();
		mapController.setZoom(13); // Zoom 1 is world view
		mapController.animateTo(new GeoPoint(45759723,4842223));

        mapOverlays = mapView.getOverlays();
        drawable = this.getResources().getDrawable(R.drawable.maps_icon);
        itemizedOverlay = new CustomItemizedOverlay(drawable);
        
        for(Placemark placemark: getPlaceMarks()){
        	String[] lL = placemark.getPoint().getCoordinates().split(",");
        	int l = (new Double(Double.parseDouble(lL[1])* 1000000)).intValue();
        	int L = (new Double(Double.parseDouble(lL[0])* 1000000)).intValue();
        	Log.d(getLocalClassName(), String.valueOf(l) + " " + String.valueOf(L));
        	GeoPoint point = new GeoPoint(l,L);
        	OverlayItem overlayItem = new OverlayItem(point, "", "");
        
        	itemizedOverlay.addOverlay(overlayItem);
        }
        mapOverlays.add(itemizedOverlay);
	}
    
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
    protected List<Placemark> getPlaceMarks() {
        Bundle bundle = getIntent().getExtras();
        int course_id = bundle.getInt("COURSE_ID");
        
        CourseBDD datasource = new CourseBDD(this);
    	datasource.open();
    	   	
    	List<Placemark> l = datasource.getAllPlacemarksWithCourseId(course_id);
    	
    	datasource.close();
    	return l;
    }
}
