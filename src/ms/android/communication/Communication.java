package ms.android.communication;

import ms.android.beans.MainBean;

import com.google.android.maps.OverlayItem;

import android.content.Context;

public abstract interface Communication {

	
	abstract OverlayItem[] getPinPoints(Context context) throws Exception;
	
	abstract MainBean[] getJasonArray(String url, String method) throws Exception ;
	
}
