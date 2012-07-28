package ms.android.finalProject;

import java.io.IOException;
import java.util.List;

import ms.android.facebook.FacebookIntegration;
import ms.android.facebookSDK.AsyncFacebookRunner;
import ms.android.facebookSDK.DialogError;
import ms.android.facebookSDK.Facebook;
import ms.android.facebookSDK.Facebook.DialogListener;
import ms.android.facebookSDK.FacebookError;
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
	private static final String TAGf = "FindYour";
	// Your Facebook APP ID
    private static String APP_ID = "414590081908892"; // Replace your App ID here
    public FacebookIntegration mFacebook;
 
    // Instance of Facebook Class
  
    String FILENAME = "AndroidSSO_data";
    private SharedPreferences mPrefs;
	//--------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //showing the available providers
        //final Dialog dialog = new Dialog(getBaseContext());
		//dialog.setContentView(R.layout.selnetworkdialog);
		//dialog.setTitle("Available...");
      /*  
        facebook = new Facebook(APP_ID);
        mAsyncRunner = new AsyncFacebookRunner(facebook);
        */
        mv = (MapView) findViewById(R.id.mapv);
        txtInfo = (TextView) findViewById(R.id.lbluradd);
        locM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        geocoder = new Geocoder(context);
        //getting providers available
        //List<String> providers = locM.getProviders(true);
        //for (String provider : providers) {
        	//Toast.makeText(getBaseContext(), provider, Toast.LENGTH_LONG).show();
        //}
        Log.i(TAG, "b4 if");
        if(locM.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
        	locM.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new GeoUpdateHandler());
        	mv.setBuiltInZoomControls(true);
        	mv.setStreetView(true);
        }
        else
        {
        	Log.i(TAG, "start else");
        	//AlertMessage2Show("Enable GPS Device");
        	//Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        	//startActivity(intent);
        	AlertDialog.Builder alert_box=new AlertDialog.Builder(this);
        	alert_box.setIcon(R.drawable.info);
        	alert_box.setMessage("GPS Location Provider is disabled, do you want to enable ?");
        	alert_box.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
        	@Override
        	public void onClick(DialogInterface dialog, int which) {
        	// TODO Auto-generated method stub
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
        	// TODO Auto-generated method stub
        		Log.i(TAG, "b4 toast");
        		Toast.makeText(getBaseContext(), "Your Current Location will be found using Network Provider", Toast.LENGTH_LONG).show();
        		Log.i(TAG, "b4 criteria");
        		Criteria criteria = new Criteria();
        		Log.i(TAG, "start criteria");
        		//provider = locM.getBestProvider(criteria, false);
        		provider = LocationManager.NETWORK_PROVIDER;
        		Log.i(TAG, "b4 provider");
        		location = locM.getLastKnownLocation(provider);
        		//location = locM.
        		Log.i(TAG, provider);
        		if(location == null)
        		{
        			Toast.makeText(getBaseContext(), "No Location Object", Toast.LENGTH_LONG).show();
        			return;
        		}
        		lat = (int) (location.getLatitude() * 1E6);
    			lng = (int) (location.getLongitude() * 1E6);
    			Log.i(TAG, "b4 gp");
    			gp = new GeoPoint(lat, lng);
    			Log.i(TAG, "map view");
    			mv.getController().setCenter(gp);
    			mv.getController().setZoom(14);
    			mv.getController().animateTo(gp);
        	}
        	});
        	alert_box.show();
        	//using other criteria to search for a location
        	// Get the location manager
    		//locM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    		// Define the criteria how to select the location provider -> use
    		// default
        	
        	
        }
        //mv.setSatellite(true);
        //mv.getController().setZoom(11);
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
					/*Intent sendIntent = new Intent(Intent.ACTION_VIEW);         
					sendIntent.setData(Uri.parse("sms:"));
					sendIntent.putExtra("sms_body", urAddress + "\nYou can check the location on map via link given below\n\nhttp://maps.google.com/maps/?q=" + location.getLatitude() + "," + location.getLongitude());
					startActivity(sendIntent);*/
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
	            //b.putInt("state", Integer.parseInt(txt2.getText().toString()));  
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
			}
			return(super.onOptionsItemSelected(item));
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
			      Log.i(TAG, "b4 for");
			      for (Address address : addresses) {
			        
					this.txtInfo.append("\n" + address.getAddressLine(0));
					Log.i(TAG, "append string");
			      } 
			    } catch (IOException e) {
			      Log.e("LocateMe", "Could not get Geocoder data", e);
			    }
			Log.i(TAG, "b4 return");
			urAddress = txtInfo.getText().toString();
			return urAddress;
			
		}
		
	/*	//--------------------fb functions
		public void loginToFacebook() {
	        mPrefs = getPreferences(MODE_PRIVATE);
	        String access_token = mPrefs.getString("access_token", null);
	        long expires = mPrefs.getLong("access_expires", 0);
	     
	        if (access_token != null) {
	            facebook.setAccessToken(access_token);
	            Log.i(TAG, "access token not null");
	        }
	     
	        if (expires != 0) {
	            facebook.setAccessExpires(expires);
	            Log.i(TAG, "expires not z");
	        }
	     
	        if (!facebook.isSessionValid()) {
	            facebook.authorize(this,
	                    new String[] { "email", "publish_stream" },
	                    new DialogListener() {
	     
	                        //@Override
	                        public void onCancel() {
	                            // Function to handle cancel event
	                        }
	     
	                       // @Override
	                        public void onComplete(Bundle values) {
	                            // Function to handle complete event
	                            // Edit Preferences and update facebook acess_token
	                            SharedPreferences.Editor editor = mPrefs.edit();
	                            editor.putString("access_token",
	                                    facebook.getAccessToken());
	                            editor.putLong("access_expires",
	                                    facebook.getAccessExpires());
	                            editor.commit();
	                            Log.i(TAG, "on complete");
	                        }
	     
	                        //@Override
	                        public void onError(DialogError error) {
	                            // Function to handle error
	     
	                        }
	     
	                       // @Override
	                        public void onFacebookError(FacebookError fberror) {
	                            // Function to handle Facebook errors
	     
	                        }
	     
	                    });
	        }
	    }
	    //-----------post on wall function
	    public void postToWall() {
	        // post on user's wall.
	        facebook.dialog(this, "feed", new DialogListener() {
	     
	            @Override
	            public void onFacebookError(FacebookError e) {
	            }
	     
	            @Override
	            public void onError(DialogError e) {
	            }
	     
	            @Override
	            public void onComplete(Bundle values) {
	            }
	     
	            @Override
	            public void onCancel() {
	            }
	        });
	    }*/
		
}