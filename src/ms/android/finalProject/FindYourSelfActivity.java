package ms.android.finalProject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import ms.android.facebook.FacebookIntegration;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class FindYourSelfActivity extends MapActivity {
	MapView mv;
	GeoPoint gp;
	LocationManager locM;
	Location location;
	TextView txtInfo, txtadd;
	Drawable marker;
	String provider;
	int lat,lng;
	final Context context = this;
	Geocoder geocoder;
	String urAddress;
	private static final String TAG = "FindYour";
	//----------------fb data
    public FacebookIntegration mFacebook;
 
    // Instance of Facebook Class
  
    String FILENAME = "AndroidSSO_data";
	//--------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
      
        mv = (MapView) findViewById(R.id.mapv);
        txtInfo = (TextView) findViewById(R.id.lbluradd);
        locM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        geocoder = new Geocoder(context);
       
       
        if(locM.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
        	locM.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new GeoUpdateHandler());
        	mv.setBuiltInZoomControls(true);
        	mv.setStreetView(true);
        }
        else
        {
        	Log.i(TAG, "start else");
        
        	AlertDialog.Builder alert_box=new AlertDialog.Builder(this);
        	alert_box.setIcon(R.drawable.info);
        	alert_box.setMessage("GPS Location Provider is disabled, do you want to enable ?");
        	alert_box.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
        	@Override
        	public void onClick(DialogInterface dialog, int which) {
        	
        		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        		startActivity(intent);
        		locM.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new GeoUpdateHandler());
            	mv.setBuiltInZoomControls(true);
            	mv.setStreetView(true);
        	}
        	});
        	alert_box.setNegativeButton("No", new DialogInterface.OnClickListener() {
        	@Override
        	public void onClick(DialogInterface dialog, int which) {

        		Toast.makeText(getBaseContext(), "Your Current Location will be found using Network Provider", Toast.LENGTH_LONG).show();

        		provider = LocationManager.NETWORK_PROVIDER;
        		
        		location = locM.getLastKnownLocation(provider);
        		//location = locM.
        		
        		if(location == null)
        		{
        			Toast.makeText(getBaseContext(), "No Location Object", Toast.LENGTH_LONG).show();
        			return;
        		}
        		lat = (int) (location.getLatitude() * 1E6);
    			lng = (int) (location.getLongitude() * 1E6);
    		
    			gp = new GeoPoint(lat, lng);
    			
    			mv.getController().setCenter(gp);
    			mv.getController().setZoom(14);
    			mv.getController().animateTo(gp);
        	}
        	});
        	alert_box.show();
        	
        }
       
        mv.setBuiltInZoomControls(true);
    }
    
    private void addMarker()
    {
    	Log.i(TAG, "add marker");
    	marker=getResources().getDrawable(R.drawable.marker);
        int markerWidth = marker.getIntrinsicWidth();
        int markerHeight = marker.getIntrinsicHeight();
        marker.setBounds(0, markerHeight, markerWidth, 0);
        myItemOverlay myItemizedOverlay = new myItemOverlay(marker,this);
        mv.getOverlays().add(myItemizedOverlay);
        myItemizedOverlay.addItem(gp, "myPoint1", "Its Multan Coordinates");
    }
    
    
    
    public class GeoUpdateHandler implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			Log.i(TAG, "loc changed");
			lat = (int) (location.getLatitude() * 1E6);
			lng = (int) (location.getLongitude() * 1E6);
			gp = new GeoPoint(lat, lng);
			mv.getController().setCenter(gp);
			
			mv.getController().animateTo(gp);
			
			//marking the location with the marker
			addMarker();
		}

		@Override
		public void onProviderDisabled(String provider) {
			
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	//menu from file omenus.xml
	
		@Override
		public boolean onCreateOptionsMenu(Menu menu)
		{
			new MenuInflater(getApplication()).inflate(R.menu.optmenu, menu);
			return (super.onCreateOptionsMenu(menu));
			
		}
		//when a menu item is selected actions below
		@Override
		public boolean onOptionsItemSelected(MenuItem item)
		{
			switch(item.getItemId())
			{
			case R.id.findAddress:
				if(location != null)
				{
					// custom dialog
					Log.i(TAG, "start dialog");
					final Dialog dialog = new Dialog(context);
					dialog.setContentView(R.layout.custdialogaddress);
					dialog.setTitle("Address...");
					Log.i(TAG, "set text address");
		 
					// set the custom dialog components - text, image and button
					txtadd = (TextView) dialog.findViewById(R.id.lblAddress);
					//finding the address of the current geopoints
					String urloc = "";
					urloc = findAddress(location);
					
					
					txtadd.setText(urloc);
					ImageView image = (ImageView) dialog.findViewById(R.id.image);
					image.setImageResource(R.drawable.address);
					Log.i(TAG, "btn");
		 
					Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
					// if button is clicked, close the custom dialog
					dialogButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
		 
					dialog.show();
				}
				else
				{
					Toast.makeText(getBaseContext(), "Location object is empty !", Toast.LENGTH_LONG).show();
				}
				break;
				
			case R.id.findDistances:
				if(location != null)
				{
					Intent disloc = new Intent(FindYourSelfActivity.this,calcDistance.class);
					Bundle disbun = new Bundle();
		            //adding data of geopoint (latitude and longitude and address of the current address
					disbun.putDouble("lat", location.getLatitude());
					disbun.putDouble("lng", location.getLongitude());
					disbun.putString("address", urAddress);
					disloc.putExtras(disbun);
					startActivity(disloc);
				}
				else
				{
					Toast.makeText(getBaseContext(), "Location object is empty !", Toast.LENGTH_LONG).show();
				}
				break;
				
			case R.id.searchLoc:
				Intent sloc = new Intent(FindYourSelfActivity.this,findLoc.class);
				Bundle bun = new Bundle();
	            //adding data of geopoint (latitude and longitude
	            bun.putDouble("lat", location.getLatitude());
	            bun.putDouble("lng", location.getLongitude());
	            sloc.putExtras(bun);
				startActivity(sloc);
				break;
				
			case R.id.smsLoc:
				if(location != null)
				{
					//---------------------default sms of system
				
					Intent smsloc = new Intent(FindYourSelfActivity.this,smsit.class);
					Bundle smsbun = new Bundle();
		            //adding data of message body in form of address
					smsbun.putString("msg_body", urAddress + "\nYou can check the location on map via link given below\n\nhttp://maps.google.com/maps/?q=" + location.getLatitude() + "," + location.getLongitude());
		            smsloc.putExtras(smsbun);
					startActivity(smsloc);
				}
				else
				{
					Toast.makeText(getBaseContext(), "Location object is empty !", Toast.LENGTH_LONG).show();
				}
				break;
				
			case R.id.myposition:
				mv.getController().setCenter(gp);
				mv.getController().animateTo(gp);
				break;
				
			case R.id.email:
				Intent mailIntent = new Intent(FindYourSelfActivity.this, emailit.class); 
	            Bundle b = new Bundle();
	            //adding data
	            b.putString("email_message", urAddress + "\nYou can check the location on map via link given below\n\nhttp://maps.google.com/maps/?q=" + location.getLatitude() + "," + location.getLongitude());
	            
	            //Add the set of extended data to the intent and start it
	            mailIntent.putExtras(b);
	            startActivity(mailIntent); 
				break;
				
			case R.id.fbLoc:
				sendOnFacebook();
				break;
				
			case R.id.about:
				// custom dialog
				Log.i(TAG, "start dialog");
				final Dialog adialog = new Dialog(context);
				adialog.setContentView(R.layout.about);
				adialog.setTitle("Dept. Of Computer Science, BZU Multan");
				Log.i(TAG, "set text address");
	 
				// set the custom dialog components - text, image and button
				TextView txtabout = (TextView) adialog.findViewById(R.id.lblabout);
				
				
				
				txtabout.setText("Muhammad Shahbaz Mushtaq\n BS(CS)-08-10");
				ImageView aimage = (ImageView) adialog.findViewById(R.id.imageabout);
				aimage.setImageResource(R.drawable.mesajjad);
				Log.i(TAG, "btn");
	 
				Button adialogButton = (Button) adialog.findViewById(R.id.dialogButtonClose);
				// if button is clicked, close the custom dialog
				adialogButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						adialog.dismiss();
					}
				});
	 
				adialog.show();
				break;
				
				//hotels activity based on location
			case R.id.findHotel:
				Intent hotelsearch = new Intent(FindYourSelfActivity.this,selectCityHotel.class);
				startActivity(hotelsearch);
				break;
			case R.id.list:
				getUserList();
				break;
			}
			return(super.onOptionsItemSelected(item));
		}
		
		private void getUserList() {
			try {
				if(mFacebook != null){
				String response = mFacebook.facebookConnector.facebook.request("me/friends");
				Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		private void sendOnFacebook() {
			mFacebook = new FacebookIntegration(this, getApplicationContext());
			mFacebook.postMessage();
			
		}

		public String findAddress(Location loc)
		{
			urAddress = "";
			this.txtInfo.setText(urAddress);
			Log.i(TAG, "func find address");
			try {
			      List<Address> addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 10); //<10>
			     
			      for (Address address : addresses) {
			        
					this.txtInfo.append("\n" + address.getAddressLine(0));
					
			      } 
			    } catch (IOException e) {
			      Log.e("LocateMe", "Could not get Geocoder data", e);
			    }
			
			urAddress = txtInfo.getText().toString();
			FacebookIntegration.MSG = urAddress;
			return urAddress;
			
		}
		
	
}