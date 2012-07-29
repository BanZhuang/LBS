package ms.android.finalProject;


import java.util.ArrayList;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.telephony.gsm.SmsManager;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
 
public class smsit extends Activity 
{
    Button btnSendSMS, btnSelCont, btnClearCont;
    EditText txtPhoneNo;
    EditText txtMessage;
    String message;
	private BroadcastReceiver sendReciever;
	private BroadcastReceiver deliverReciever;
	private ArrayList<PendingIntent> sendApii = new ArrayList<PendingIntent>();
	private ArrayList<PendingIntent> devliverApii = new ArrayList<PendingIntent>();
	public static final int PICK_CONTACT_REQUEST    = 1;
	private final ContactAccessor mContactAccessor = ContactAccessor.getInstance();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smsloc);        
 
        btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
        btnSelCont = (Button) findViewById(R.id.btnSelContact);
        btnClearCont = (Button) findViewById(R.id.btnDelContact);
        txtPhoneNo = (EditText) findViewById(R.id.txtPhoneNo);
        txtMessage = (EditText) findViewById(R.id.txtMessage);
        
        //retrieving the data
        Bundle b = getIntent().getExtras();
		message = b.getString("msg_body");
		
		
		txtMessage.setText(message);
		
		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";

		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
				SENT), 0);
		sendApii.add(sentPI);
		
		

		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
				new Intent(DELIVERED), 0);

		devliverApii.add(deliveredPI);
		
		

		//selecting a contact from the phone contact
		btnClearCont.setOnClickListener(new View.OnClickListener() 
		{

			@Override
			public void onClick(View arg0) {
				txtPhoneNo.setText("");
			}
		});

		
		//selecting a contact from the phone contact
		btnSelCont.setOnClickListener(new View.OnClickListener() 
        {

			@Override
			public void onClick(View arg0) {
				pickContact();
				
			}
			
        });
		
		
 
        btnSendSMS.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {                
                String phoneNo = txtPhoneNo.getText().toString();
                String message = txtMessage.getText().toString();                 
                if (phoneNo.length()>0 && message.length()>0)                
                    sendSMS(phoneNo, message);                
                else
                    Toast.makeText(getBaseContext(), 
                        "Please enter both phone number and message.", 
                        Toast.LENGTH_SHORT).show();
            }
        });
        
        sendReciever = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				 switch (getResultCode())
	                {
	                    case Activity.RESULT_OK:
	                        Toast.makeText(getBaseContext(), "SMS sent", 
	                                Toast.LENGTH_SHORT).show();
	                        break;
	                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
	                        Toast.makeText(getBaseContext(), "Generic failure", 
	                                Toast.LENGTH_SHORT).show();
	                        break;
	                    case SmsManager.RESULT_ERROR_NO_SERVICE:
	                        Toast.makeText(getBaseContext(), "No service", 
	                                Toast.LENGTH_SHORT).show();
	                        break;
	                    case SmsManager.RESULT_ERROR_NULL_PDU:
	                        Toast.makeText(getBaseContext(), "Null PDU", 
	                                Toast.LENGTH_SHORT).show();
	                        break;
	                    case SmsManager.RESULT_ERROR_RADIO_OFF:
	                        Toast.makeText(getBaseContext(), "Radio off", 
	                                Toast.LENGTH_SHORT).show();
	                        break;
	                }
				
			}
		};
		
		
		 deliverReciever = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				 switch (getResultCode())
	                {
	                    case Activity.RESULT_OK:
	                        Toast.makeText(getBaseContext(), "SMS delivered", 
	                                Toast.LENGTH_SHORT).show();
	                        break;
	                    case Activity.RESULT_CANCELED:
	                        Toast.makeText(getBaseContext(), "SMS not delivered", 
	                                Toast.LENGTH_SHORT).show();
	                        break;                        
	                }
				
			}
		};
		
		
		registerReceiver(sendReciever, new IntentFilter(SENT));
		registerReceiver(deliverReciever, new IntentFilter(DELIVERED));
    }
    
    //contact selection
    
    protected void pickContact() {
        startActivityForResult(mContactAccessor.getPickContactIntent(), PICK_CONTACT_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT_REQUEST && resultCode == RESULT_OK) {
            loadContactInfo(data.getData());
        }
    }
    /**
     * Load contact information on a background thread.
     */
    private void loadContactInfo(Uri contactUri) {

        /*
         * We should always run database queries on a background thread. The database may be
         * locked by some process for a long time.  If we locked up the UI thread while waiting
         * for the query to come back, we might get an "Application Not Responding" dialog.
         */
        AsyncTask<Uri, Void, ContactInfo> task = new AsyncTask<Uri, Void, ContactInfo>() {

            @Override
            protected ContactInfo doInBackground(Uri... uris) {
                return mContactAccessor.loadContact(getContentResolver(), uris[0]);
            }

            @Override
            protected void onPostExecute(ContactInfo result) {
                bindView(result);
            }
        };

        task.execute(contactUri);
    }
    
    /**
     * Displays contact information: name and phone number.
     */
    protected void bindView(ContactInfo contactInfo) {
        txtPhoneNo.setText(contactInfo.getPhoneNumber());
    }




  //---sends an SMS message to another device---
    private void sendSMS(String phoneNumber, String message)
    {        
		
		// ---when the SMS has been delivered---
		

		SmsManager sms = SmsManager.getDefault();
		// sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
		ArrayList<String> parts = sms.divideMessage(message);
		sms.sendMultipartTextMessage(phoneNumber, null, parts, sendApii,
				devliverApii);
        
    }
    
    
    
    @Override
    public void onBackPressed() {
    	unregisterReceiver(sendReciever);
    	unregisterReceiver(deliverReciever);
    	super.onBackPressed();
    }
    
}