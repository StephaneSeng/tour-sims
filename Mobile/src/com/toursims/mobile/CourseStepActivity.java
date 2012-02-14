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
import com.toursims.mobile.model.kml.Placemark;
import com.toursims.mobile.model.kml.Point;
import com.toursims.mobile.ui.utils.CustomItemizedOverlay;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

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
       
        mapView.setBuiltInZoomControls(true);

        mapOverlays = mapView.getOverlays();
        drawable = this.getResources().getDrawable(R.drawable.androidmarker);
        itemizedOverlay = new CustomItemizedOverlay(drawable);
        
        GeoPoint point = new GeoPoint(45759723,4842223);
        OverlayItem overlayItem = new OverlayItem(point, "", "");
        
        itemizedOverlay.addOverlay(overlayItem);
        mapOverlays.add(itemizedOverlay);
	}
    
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
    protected List<Placemark> getPlaceMarks() {
    	List<Placemark> l = new ArrayList<Placemark>();
    	for (int i = 0; i< 10; i++) {
    		Placemark p = new Placemark();
    		p.setPoint(new Point());
    		l.add(p);
    	}
    	
    	return l;
    }

}
