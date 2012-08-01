package ms.android.fyp.friendsfinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class AlertHelper {

	public static void showAlert(Activity act,String title, String msg){
		AlertDialog dialog = new AlertDialog.Builder(act).create();
		dialog.setCancelable(true);
		
		if(title != null) dialog.setTitle(title);
		if(msg != null) dialog.setMessage(msg);
		
		dialog.setButton("Ok", new DialogInterface.OnClickListener() {
		  @Override
		  public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		  }
		});
		dialog.show();
}

}
