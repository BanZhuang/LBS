package ms.android.finalProject;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class SplashActivity extends Activity {
	
	private long splashdelay = 5000; //5 sec
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		TimerTask task = new TimerTask()
		{

			@Override
			public void run() {
				finish();
				Intent MainIntent = new Intent().setClass(SplashActivity.this, FindYourSelfActivity.class);
				startActivity(MainIntent);
				
			}
			
		};
		
		Timer timer = new Timer();
		timer.schedule(task, splashdelay);
	}
}
