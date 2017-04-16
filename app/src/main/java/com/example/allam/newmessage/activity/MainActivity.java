package com.example.allam.newmessage.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.allam.newmessage.R;
import com.example.allam.newmessage.Utiles.Utiles;

public class MainActivity extends AppCompatActivity {

    private Button mDoneButton, mPickContactButton;
    private EditText mEnterNumberEditText;
    private boolean mGetNumber;
    private String mPhoneNumber;

    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Choose Number");
        mDoneButton = (Button) findViewById(R.id.main_button_done);
        mPickContactButton = (Button) findViewById(R.id.pick_contact_button);
        mEnterNumberEditText = (EditText) findViewById(R.id.edit_text_phone_number);

        mPickContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mEnterNumberEditText.getText().toString())) {
                    Utiles.setPhoneNumber(getApplicationContext(), mEnterNumberEditText.getText().toString());
                    Utiles.setUserName(getApplicationContext(), mEnterNumberEditText.getText().toString());
                    Intent intent = new Intent(MainActivity.this, MessagesActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    mEnterNumberEditText.setError("This field is required or pick from contacts.");
                    mEnterNumberEditText.requestFocus();
                }
            }
        });

        //check if the SMS read and receive permission are granted for Android Marshmello or not
        if (!Utiles.checkPermession(getBaseContext(), Manifest.permission.READ_SMS) ||
                !Utiles.checkPermession(getBaseContext(), Manifest.permission.RECEIVE_SMS) ||
                !Utiles.checkPermession(getBaseContext(), Manifest.permission.READ_CONTACTS)) {
            //request permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.READ_CONTACTS},
                    Utiles.SMS_PERMESSION_REQUEST_CODE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
            return;
        if (requestCode == 0 && data != null) {
            Uri UriContact = data.getData();

            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER};

            Cursor cursor = getContentResolver().query(UriContact, projection, null, null, null);

            int indexName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int indexNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);


            try {
                if (cursor.getCount() == 0) {
                    return;
                }
                cursor.moveToFirst();
                String contactName = cursor.getString(indexName);
                String mobileNumber = cursor.getString(indexNumber);
                if (mobileNumber.substring(0, 2) != "+2") {
                    mobileNumber = "+2" + mobileNumber;
                }
                Utiles.setPhoneNumber(getApplicationContext(), mobileNumber);
                Utiles.setUserName(getApplicationContext(), contactName);
                Toast.makeText(this, contactName + " (" + mobileNumber + ") is choosed", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, MessagesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } finally {
                cursor.close();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Utiles.SMS_PERMESSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission successfully allowed.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "You need to accept read sms permission first," +
                            " \n go to app settings and allow them.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.notification);
        boolean isOn = Utiles.IsNotificationOn(getBaseContext());
        String title = isOn ? getString(R.string.turn_notification_off) : getString(R.string.turn_notification_on);
        menuItem.setTitle(title);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notification:
                boolean isOn = Utiles.IsNotificationOn(getApplicationContext());
                Toast.makeText(this, "Notification turned " + (isOn ? "OFF" : "ON"), Toast.LENGTH_SHORT).show();
                Utiles.setNotificationOn(getApplicationContext(), !isOn);
                invalidateOptionsMenu();
                return true;
            case R.id.change_number:
                return mEnterNumberEditText.requestFocus();
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}

