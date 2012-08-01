package ms.android.communication;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import android.util.Log;

public class WebCommunication {
	public static final String friendFinder = "http://msfriendsfinder.99k.org/";
	public static final String hotelsys = "http://hotelsyspk.99k.org/";
	private JSONArray jArray = null;
	
	public JSONArray getJsonArray(String url,String Method)
	{
		String result = "";
		InputStream isr = null;
		try{
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(url);
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			isr = entity.getContent();
		}
		catch(Exception e){
			Log.e("Error", "Unable To Connect" + e.toString());
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
			result = sb.toString();
		}
		catch(Exception e){
			Log.e("Error", "Unable To Convert to string" + e.toString());
			
		}
		
		//
		//now parsing json data
		try{
			jArray = new JSONArray(result);
			return jArray;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
