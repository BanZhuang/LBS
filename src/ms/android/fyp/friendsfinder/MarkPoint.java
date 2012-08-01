package ms.android.fyp.friendsfinder;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MarkPoint  extends ItemizedOverlay<OverlayItem>{
	
	private ArrayList<OverlayItem> pinpoints = new ArrayList<OverlayItem>();

	public MarkPoint(Drawable defaultMarker) {
		super(defaultMarker);
		// TODO Auto-generated constructor stub
	}
	
	public void insertCurrentLoc(OverlayItem item) {
		pinpoints.add(0, item);
		this.populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

}
