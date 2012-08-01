package ms.android.fyp.friendsfinder;

import java.util.List;
import java.util.Locale;

import ms.android.finalProject.R;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class FFMainActivity extends MapActivity implements LocationListener {
	
	private MapView mapView;
	MyLocationOverlay compas;
	MapController controller;
	GeoPoint touchedPoint, currentLoc;
	LocationManager locManager;
	
	List<Overlay> overlayList;
	MarkPoint currentLocPinpoint;
	
	String towers;
	int x, y, curr_lati, curr_longi, moveX, moveY;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.ffmain);
        mapView = (MapView) findViewById(R.id.mapViewMain);
        mapView.setBuiltInZoomControls(true);
        currentLocPinpoint = new MarkPoint(getResources().getDrawable(R.drawable.ic_launcher));
        
        overlayList = mapView.getOverlays();
        compas = new MyLocationOverlay(FFMainActivity.this, mapView);
        overlayList.add(compas);
        
        controller = mapView.getController();
        
     // placing pin point at current location
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria crit = new Criteria();
        
        towers = locManager.getBestProvider(crit, false);
        Location location = new Location(towers);
        
        if (location != null) {
        	curr_lati = (int) (location.getLatitude() * 1E6);
        	curr_longi = (int) (location.getLongitude() * 1E6);

	        currentLoc = new GeoPoint(curr_lati, curr_longi);
	        
	        controller.animateTo(currentLoc);
	        controller.setZoom(2);
	        
	        OverlayItem overLayItem = new OverlayItem(currentLoc, "title", "text");
	        currentLocPinpoint.insertCurrentLoc(overLayItem);
		}
	}
	
	public String getAddress () {
		String display = "";
		Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
		try {
			List<Address> address = geocoder.getFromLocation(touchedPoint.getLatitudeE6() / 1E6, touchedPoint.getLongitudeE6() / 1E6, 1);
			if (address.size() > 0) {
				for (int i = 0; i < address.get(0).getMaxAddressLineIndex(); i++) {
					display += address.get(0).getAddressLine(i) + "\n";
				}
			}
		} catch (Exception e) {
			AlertHelper.showAlert(FFMainActivity.this, "Error...", "Address not found.");
		}
		
		return display;
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		compas.disableCompass();
		super.onPause();
		locManager.removeUpdates(this);
	}

	@Override
	protected void onResume() {
		compas.enableCompass();
		super.onResume();
		locManager.requestLocationUpdates(towers, 500, 0, this);
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
