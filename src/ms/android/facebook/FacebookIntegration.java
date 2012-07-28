package ms.android.facebook;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class FacebookIntegration {

	public static final String CLASS_TAG = "FacebookSample";

	public static final String FACEBOOK_APPID = "414590081908892";
	public static final String FACEBOOK_PERMISSION = "publish_stream";
	public static final String FACEBOOK_EMAIL_PERMISSION = "email";
	public static String MSG = "S";

	private final Handler mFacebookHandler = new Handler();
	private TextView loginStatus;
	public FacebookConnector facebookConnector;
	private Context context;

	final Runnable mUpdateFacebookNotification = new Runnable() {
		public void run() {
			Toast.makeText(context, "Facebook updated !", Toast.LENGTH_LONG)
					.show();
		}
	};

	public FacebookIntegration(Activity activity, Context context) {
		this.context = context;
		this.facebookConnector = new FacebookConnector(FACEBOOK_APPID,
				activity, context, new String[] { FACEBOOK_PERMISSION,FACEBOOK_EMAIL_PERMISSION,"read_stream","read_friendlists","friends_online_presence" });
		//clearCredentials();
	}

	public void updateLoginStatus() {
		loginStatus.setText("Logged into Facebook : "
				+ facebookConnector.getFacebook().isSessionValid());
	}

	private String getFacebookMsg() {
		return MSG + " at " + new Date().toLocaleString();
	}

	public void postMessage() {

		if (facebookConnector.getFacebook().isSessionValid()) {
			postMessageInThread();
		} else {
			SessionEvents.AuthListener listener = new SessionEvents.AuthListener() {

				// @Override
				public void onAuthSucceed() {
					postMessageInThread();
				}

				// @Override
				public void onAuthFail(String error) {

				}
			};
			SessionEvents.addAuthListener(listener);
			facebookConnector.login();
		}
	}

	private void postMessageInThread() {
		Thread t = new Thread() {
			public void run() {

				try {
					facebookConnector.postMessageOnWall(getFacebookMsg());
					mFacebookHandler.post(mUpdateFacebookNotification);
				} catch (Exception ex) {
					Log.e(CLASS_TAG, "Error sending msg", ex);
				}
			}
		};
		t.start();
	}

	public void clearCredentials() {
		try {
			facebookConnector.getFacebook().logout(context);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}