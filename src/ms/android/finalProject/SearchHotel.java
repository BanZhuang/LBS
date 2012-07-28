package ms.android.finalProject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SearchHotel extends Activity {
	
	TextView result;
	AutoCompleteTextView cityName;
	Button btnSearch;
	String city;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchhotel);
        
        result = (TextView) findViewById(R.id.lblShowResult);
        //auto cpmplete city name
        //from city array in string resources
        cityName = (AutoCompleteTextView) findViewById(R.id.autocomplete_city);
        String[] cities = getResources().getStringArray(R.array.cities_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_cities, cities);
        cityName.setAdapter(adapter);

        btnSearch = (Button) findViewById(R.id.btnHotelSearch);
        result.setText("Before Resut");
        //StrictMode.enableDefaults();
        
        
        btnSearch.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				city = cityName.getText().toString();
				GetHotelsList(city);
			}
        	
        });
	}
	
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
			result.setText("Can,t Connect To server");
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
			String s = "";
			JSONArray array = new JSONArray(res);
			for(int i=0;i<array.length();i++){
				JSONObject json = array.getJSONObject(i);
				s = s + "" + json.getString("Name");
				
				//assigning to control
				result.setText(s);
			}
		}
		catch(Exception e){
			Log.e("Error", "Unable To parse json data" + e.toString());
			//result.setText("unable to parse data");
		}
	}

}
