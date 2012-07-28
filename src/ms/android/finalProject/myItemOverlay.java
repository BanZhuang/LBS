package ms.android.finalProject;


import java.util.ArrayList;
 

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.widget.Toast;
 
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
 
public class myItemOverlay extends ItemizedOverlay<OverlayItem>{
 
	private ArrayList<OverlayItem> overlayItemList = new ArrayList<OverlayItem>();
	private Context mContext;
 
	public myItemOverlay(Drawable marker) {
		super(boundCenterBottom(marker));
		// TODO Auto-generated constructor stub
 
		//populate();
	}

	public myItemOverlay(Drawable marker, Context contxt) {
		super(boundCenterBottom(marker));
		mContext = contxt;
		//populate();
	}
 
	public void addItem(GeoPoint p, String title, String snippet){
		OverlayItem newItem = new OverlayItem(p, title, snippet);
		overlayItemList.add(newItem);
		populate();
	}
 
	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return overlayItemList.get(i);
	}
 
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return overlayItemList.size();
	}
 
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		// TODO Auto-generated method stub
		super.draw(canvas, mapView, shadow);
		//boundCenterBottom(marker);
	}

	@Override
	protected boolean onTap(int i)
	{
		Toast.makeText(mContext, overlayItemList.get(i).getSnippet(), Toast.LENGTH_LONG).show();
		/*AlertDialog.Builder build = new AlertDialog.Builder(mContext);
		build.setTitle("Location Info !");
		build.setMessage(overlayItemList.get(i).getSnippet());
		build.show();*/
		return true;
	}
 
}
