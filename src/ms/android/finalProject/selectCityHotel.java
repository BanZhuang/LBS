package ms.android.finalProject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import ms.android.finalProject.R;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;

public class selectCityHotel extends ListActivity {
	
	String cityName = null;
	private static final String TAG = "FindYour";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.searchhotel);
        
        Log.i(TAG, "b4 city resource");
        //getting the list of cities from the resource
        String[] cities = getResources().getStringArray(R.array.cities_array);
        Log.i(TAG, "b4 set adapter");
        setListAdapter(new ArrayAdapter<String>(this, R.layout.list_cities, cities));
        
        ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			    // When clicked, show a toast with the TextView text
			    //Toast.makeText(getApplicationContext(),
				//((TextView) view).getText(), Toast.LENGTH_SHORT).show();
			    cityName = (String) ((TextView) view).getText();
			    //Toast.makeText(getApplicationContext(), cityName, Toast.LENGTH_SHORT).show();
			    GetHotelsList(cityName);
			}
		});
	}
	
	//function that communicate with server
	//server is hotelsyspk.99k.org
	public void GetHotelsList(String city)
	{
		String res = "";
		InputStream isr = null;
		try{
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost("http://hotelsyspk.99k.org/getHotelsCity.php?c=" + city);
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			isr = entity.getContent();
		}
		catch(Exception e){
			Log.e("Error", "Unable To Connect" + e.toString());
			//result.setText("Can,t Connect To server");
		}
		//
		//now converting the reponse to string
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(isr,"iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while((line=reader.readLine()) != null){
				sb.append(line + "\n");
				
			}
			isr.close();
			res = sb.toString();
		}
		catch(Exception e){
			Log.e("Error", "Unable To Convert to string" + e.toString());
			//result.setText("Can,t Convert to string");
		}
		
		//
		//now parsing json data
		try{
			String hotels[] = new String[]{};
			JSONArray array = new JSONArray(res);
			for(int i=0;i<array.length();i++){
				JSONObject json = array.getJSONObject(i);
				//s = s + "" + json.getString("Name");
				hotels[i] = json.getString("Name");
				//assigning to control
				//result.setText(s);
			}
			
			//showing in the list view the returned hotels
			setListAdapter(new ArrayAdapter<String>(this, R.layout.list_cities,hotels));
			ListView listView = getListView();
			listView.setTextFilterEnabled(true);
		}
		catch(Exception e){
			Log.e("Error", "Unable To parse json data" + e.toString());
			//result.setText("unable to parse data");
		}
	}

}
