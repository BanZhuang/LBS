package ms.android.facebook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import ms.android.facebookSDK.AsyncFacebookRunner.RequestListener;
import ms.android.facebookSDK.FacebookError;
import android.util.Log;

/**
 * Skeleton base class for RequestListeners, providing default error handling.
 * Applications should handle these error conditions.
 * 
 */
public abstract class BaseRequestListener implements RequestListener {

	public void onFacebookError(FacebookError e, final Object state) {
		Log.e("Facebook", e.getMessage());
		e.printStackTrace();
	}

	public void onFileNotFoundException(FileNotFoundException e,
			final Object state) {
		Log.e("Facebook", e.getMessage());
		e.printStackTrace();
	}

	public void onIOException(IOException e, final Object state) {
		Log.e("Facebook", e.getMessage());
		e.printStackTrace();
	}

	public void onMalformedURLException(MalformedURLException e,
			final Object state) {
		Log.e("Facebook", e.getMessage());
		e.printStackTrace();
	}

}
