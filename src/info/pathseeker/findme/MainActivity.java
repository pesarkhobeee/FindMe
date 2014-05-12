package info.pathseeker.findme;


import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {
	static final int PICK_CONTACT_REQUEST = 1;  // The request code
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button savepreferences =  (Button) findViewById(R.id.btn_show_location);
		savepreferences.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				LocationManager mlocManager=null;  
				LocationListener mlocListener;  
				mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);  
				mlocListener = new MyLocationListener();  
				mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);  
				TextView fullname_TextView = (TextView)findViewById(R.id.textView1);

				if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {  
					
					if(MyLocationListener.latitude>0)  
					{  
						String location = "Latitude:- " + MyLocationListener.latitude + '\n' + "Longitude:- " + MyLocationListener.longitude + '\n';

						fullname_TextView.setText(location); 
					}  
					else  
					{   
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
						alertDialogBuilder.setTitle("Wait");
						String message = "GPS in progress, please wait.";
						alertDialogBuilder.setMessage(message);
						// set positive button: Yes message
						alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// go to a new activity of the app

							}
						});

						AlertDialog alertDialog = alertDialogBuilder.create();
						// show alert
						alertDialog.show();

					}
					
				} else {   
					fullname_TextView.setText("GPS is not turned on...");
				}  

			}  
		});	
		Button sendsms =  (Button) findViewById(R.id.sendsms);
		sendsms.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
			    Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
			    pickContactIntent.setType(Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
			    startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);

			}
			
			
		});
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // Check which request it is that we're responding to
	    if (requestCode == PICK_CONTACT_REQUEST) {
	        // Make sure the request was successful
	        if (resultCode == RESULT_OK) {
	            // Get the URI that points to the selected contact
	            Uri contactUri = data.getData();
	            // We only need the NUMBER column, because there will be only one row in the result
	            String[] projection = {Phone.NUMBER};

	            // Perform the query on the contact to get the NUMBER column
	            // We don't need a selection or sort order (there's only one result for the given URI)
	            // CAUTION: The query() method should be called from a separate thread to avoid blocking
	            // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
	            // Consider using CursorLoader to perform the query.
	            Cursor cursor = getContentResolver()
	                    .query(contactUri, projection, null, null, null);
	            cursor.moveToFirst();

	            // Retrieve the phone number from the NUMBER column
	            int column = cursor.getColumnIndex(Phone.NUMBER);
	            String number = cursor.getString(column);
				TextView fullname_TextView = (TextView)findViewById(R.id.textView1);
				fullname_TextView.setText(number); 
				
	            // Do something with the phone number...
				
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(number, null, "FindMe: 36.67228898 48.48115444", null, null);
	        }
	    }
	}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.main, menu);
	return true;
}


@Override
public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
        case R.id.action_settings:
        	startActivity(new Intent(this, CompassActivity.class));
        	return true;
        default:
        return super.onOptionsItemSelected(item);
    }
}



}
