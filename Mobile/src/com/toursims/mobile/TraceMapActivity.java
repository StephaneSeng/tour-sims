package com.toursims.mobile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;
import com.toursims.mobile.controller.CourseLoader;
import com.toursims.mobile.model.Course;
import com.toursims.mobile.model.kml.Placemark;
import com.toursims.mobile.model.places.Road;
import com.toursims.mobile.ui.utils.CustomItemizedOverlay;
import com.toursims.mobile.ui.utils.RoadProvider;

public class TraceMapActivity extends MapActivity {

	private MapView mapView;
	private MapController mapController;
	private List<Overlay> mapOverlays;
	private Drawable drawable;
	private Drawable drawable2;
	private CustomItemizedOverlay itemizedOverlay1;
	private CustomItemizedOverlay itemizedOverlay2;
	private Course course;
	private static List<GeoPoint> bounds;

	/**
	 * Called when the activity is first created
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trace_map);

		// Initialize the share attributes
		mapView = (MapView) findViewById(R.id.traceMapMapView);
		mapController = mapView.getController();
		mapOverlays = mapView.getOverlays();

		// Set the MapView properties
		mapView.setBuiltInZoomControls(true);

		mapOverlays = mapView.getOverlays();

		Bundle bundle = getIntent().getExtras();
		course = CourseLoader.getInstance().parse(bundle.getString("TRACE"));

		drawable = this.getResources().getDrawable(R.drawable.ic_start_pos);
		itemizedOverlay1 = new CustomItemizedOverlay(drawable, this);
		drawable2 = this.getResources().getDrawable(R.drawable.ic_finish_pos);
		itemizedOverlay2 = new CustomItemizedOverlay(drawable2, this);
		GeoPoint formerPoint = null;
		int i = 0;

		for (Placemark placemark : course.getPlacemarks()) {
			String[] lL = placemark.getPoint().getCoordinates().split(",");
			int l = (new Double(Double.parseDouble(lL[1]) * 1000000))
					.intValue();
			int L = (new Double(Double.parseDouble(lL[0]) * 1000000))
					.intValue();
			Log.d(getLocalClassName(),
					String.valueOf(l) + " " + String.valueOf(L));
			GeoPoint point = new GeoPoint(l, L);

			if (bounds == null) {
				bounds = new ArrayList<GeoPoint>();
			}
			bounds.add(point);

			OverlayItem overlayItem = new OverlayItem(point, "", "");

			// itemizedOverlay.addOverlay(overlayItem);

			/***** load routes *****/
			if (formerPoint != null) {
				mapOverlays.add(new MySimplePathOverlay(formerPoint, point,
						mapView.getProjection()));
			} else {
				itemizedOverlay1.addOverlay(overlayItem);
			}
			formerPoint = point;
			if (i == course.getPlacemarks().size() - 1) {
				itemizedOverlay2.addOverlay(overlayItem);
			}
			i++;
		}
		mapOverlays.add(itemizedOverlay1);
		mapOverlays.add(itemizedOverlay2);
		zoomInBounds();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private void zoomInBounds() {

		int minLat = Integer.MAX_VALUE;
		int minLong = Integer.MAX_VALUE;
		int maxLat = Integer.MIN_VALUE;
		int maxLong = Integer.MIN_VALUE;

		for (GeoPoint point : bounds) {
			minLat = Math.min(point.getLatitudeE6(), minLat);
			minLong = Math.min(point.getLongitudeE6(), minLong);
			maxLat = Math.max(point.getLatitudeE6(), maxLat);
			maxLong = Math.max(point.getLongitudeE6(), maxLong);
		}

		mapController.zoomToSpan(Math.abs(minLat - maxLat),
				Math.abs(minLong - maxLong));
		mapController.animateTo(new GeoPoint((maxLat + minLat) / 2,
				(maxLong + minLong) / 2));

		bounds.clear();
	}

}

class MySimplePathOverlay extends Overlay {
	private GeoPoint gP1;
	private GeoPoint gP2;
	private Projection projection;

	public MySimplePathOverlay(GeoPoint gP1, GeoPoint gP2, Projection projection) {
		this.gP1 = gP1;
		this.gP2 = gP2;
		this.projection = projection;
	}

	public void draw(Canvas canvas, MapView mapv, boolean shadow) {
		super.draw(canvas, mapv, shadow);

		Paint mPaint = new Paint();
		mPaint.setDither(true);
		mPaint.setColor(Color.RED);
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(2);

		android.graphics.Point p1 = new android.graphics.Point();
		android.graphics.Point p2 = new android.graphics.Point();
		Path path = new Path();

		projection.toPixels(gP1, p1);
		projection.toPixels(gP2, p2);

		path.moveTo(p2.x, p2.y);
		path.lineTo(p1.x, p1.y);

		canvas.drawPath(path, mPaint);
	}
}