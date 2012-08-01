package ms.android.communication;

import java.util.ArrayList;

import ms.android.beans.Field;
import ms.android.beans.MainBean;
import ms.android.finalProject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class FriendFinder implements Communication {

	WebCommunication webCom = new WebCommunication();
	Drawable drawable = null;
	OverlayItem[] items;
	
	@Override
	public OverlayItem[] getPinPoints(Context context) throws Exception {
		
		
		/*
	
		items = new OverlayItem[2];
		GeoPoint point = new GeoPoint((int)(51.5174723*1E6),(int)(-0.0899537*1E6));
		OverlayItem overlayItem = new OverlayItem(point, "Tomorrow Never Dies (1997)", 
				"(M gives Bond his mission in Daimler car)");
		items[0] = overlayItem;
		
		GeoPoint point2 = new GeoPoint((int)(51.515259*1E6),(int)(-0.086623*1E6));
		OverlayItem overlayItem2 = new OverlayItem(point2, "GoldenEye (1995)", 
				"(Interiors Russian defence ministry council chambers in St Petersburg)");		
		items[1] = overlayItem2;
		
		*/
		MainBean[] bean = getJasonArray("http://msfriendsfinder.99k.org/", "POST");
		items = new OverlayItem[bean.length];
		
		for(int i = 0 ;i < bean.length; i++){
			String lat = bean[i].fields.get("lat").value;
			String lng = bean[i].fields.get("lng").value;
			GeoPoint point = new GeoPoint((int)(Double.valueOf(lat)*1E6),(int)(Double.valueOf(lng)*1E6));
			OverlayItem overlayItem = new OverlayItem(point, "Example Tittle", 
					"Example Snipet");
			items[i] = overlayItem;

		}
		
		return items;
	}

	@Override
	public MainBean[] getJasonArray(String url, String method) throws Exception {
		
		String pid = "";
		JSONArray array = webCom.getJsonArray(url, method);
		
		ArrayList<MainBean> mainbean = new ArrayList<MainBean>();
		MainBean bean[] = null;
		//parsing the json array
		String id = "";
		String Lat = "";
		String Lng = "";
		String Status = "";
			try {
				for(int i=0;i<array.length();i++){
					JSONObject json = array.getJSONObject(i);
					id = json.getString("ID");
					Lat = json.getString("Latitude");
					Lng = json.getString("Longitude");
					Status =  json.getString("Visible");
					MainBean b = new MainBean();
					b.fields.put("id", new Field("id", id));
					b.fields.put("lat", new Field("lat", Lat));
					b.fields.put("lng", new Field("lng", Lng));
					b.fields.put("status", new Field("status", Status));
					
					
					mainbean.add(b);
				}
				bean = new MainBean[mainbean.size()];
				mainbean.toArray(bean);
				
				return bean;
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		return null;
	}

}
