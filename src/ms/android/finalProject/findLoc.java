package ms.android.finalProject;


import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.GeoPoint;

import android.location.Location;
import android.location.Geocoder;
import android.location.Address;

import java.util.List;
import java.io.IOException;

import android.app.AlertDialog;
import android.app.Dialog;

import android.util.Log;

public class findLoc extends MapActivity {
	Button btnSearch;
	EditText userText;
	MapView mapv;
	GeoPoint gp;
	Location loc;
	Geocoder gc;
    private static final String TAG = "FindYour";  
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findlocation);
        Log.i(TAG, "After View");
        btnSearch = (Button) findViewById(R.id.btnSearch);
        userText = (EditText) findViewById(R.id.edtxtLocation);
        Log.i(TAG, "After Fields");
        //showing the map
        mapv = (MapView) findViewById(R.id.mapvew);
        //getting values from bundle instance
        Bundle b = getIntent().getExtras();
        //double lat = Double.parseDouble("31.5497222");
        //double lng = Double.parseDouble("74.3436111");
        double lat = b.getDouble("lat");
        double lng = b.getDouble("lng");
        
        gp = new GeoPoint((int)(lat*1E6),(int)(lng*1E6));
        
        mapv.getController().setCenter(gp);
        //mapv.getController().setZoom(13);
        mapv.getController().animateTo(gp);
        
        gc = new Geocoder(this);
        Log.i(TAG, "B4 Listener");
        btnSearch.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(userText.length()==0)
				{
					//Toast.makeText(getBaseContext(), "Enter Location !", Toast.LENGTH_LONG).show();
					AlertMessage2Show("Enter Location Name To Search");
				}
				else
				{
					String locn = userText.getText().toString();
					try
					{
						List<Address> addressList = gc.getFromLocationName(locn, 5);
						if(addressList != null && addressList.size() > 0)
						{
							
							double lat = addressList.get(0).getLatitude();
							double lng = addressList.get(0).getLongitude();
							Toast.makeText(getBaseContext(), "New Latitude : " + lat + " && Longitude : " + lng, Toast.LENGTH_LONG).show();
							gp = new GeoPoint((int)(lat*1E6),(int)(lng*1E6));
							mapv.getController().animateTo(gp);
						}
						else
						{
							
							AlertMessage2Show("No Location Found !");
						}
					}
					catch(IOException e)
					{
						e.printStackTrace();
					}
				}
			}
        	
        });
        
	}
	
	private void AlertMessage2Show(String text)
	{
		Dialog foundNothing = new AlertDialog.Builder(findLoc.this).setIcon(R.drawable.info)
				.setTitle("Search Result").setPositiveButton("OK", null).setMessage(text)
				.create();
		foundNothing.show();
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
		case R.id.about:
			Toast.makeText(getBaseContext(), "Activity is Closing . . . .", Toast.LENGTH_LONG).show();
			finish();
			break;
			
		case R.id.findDistances:
			Toast.makeText(getBaseContext(), "Button enabled", Toast.LENGTH_LONG).show();
			break;
		}
		return(super.onOptionsItemSelected(item));
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}

