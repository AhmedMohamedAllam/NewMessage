package com.example.allam.newmessage.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.allam.newmessage.Utiles.Utiles;
import com.example.allam.newmessage.model.Message;

import static com.example.allam.newmessage.Utiles.Utiles.LOG_TAG;




public class SMSBroadcastReceiver extends BroadcastReceiver {
    private Message mMessage = null;

    public void onReceive(Context context, Intent intent) {
        if (Utiles.IsNotificationOn(context)) {
            Intent notificationService = NotificationService.newIntent(context);
            String senderNum = null;
            String body = null;
            String date = null;
            final Bundle bundle = intent.getExtras();
            String format = intent.getStringExtra("format");
            try {
                if (bundle != null) {
                    final Object[] pdusObj = (Object[]) bundle.get("pdus");
                    for (int i = 0; i < pdusObj.length; i++) {
                        SmsMessage currentMessage = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i], format);
                        } else {
                            currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        }
                        senderNum = currentMessage.getDisplayOriginatingAddress();
                        body = currentMessage.getDisplayMessageBody();
                        date = Utiles.SIMPLE_DATE_FORMAT.format(currentMessage.getTimestampMillis());
                    }

                    String savedNumber = Utiles.getPhoneNumber(context.getApplicationContext());
                    if(savedNumber != null && senderNum.equals(savedNumber)){
                        mMessage = new Message(senderNum, body, date);
                        notificationService.putExtra("message", mMessage);
                        context.startService(notificationService);
                    }else {
                        Log.i(LOG_TAG, senderNum + " is the real sender");
                    }


                }
            } catch (Exception e) {
                Log.e(LOG_TAG, "Exception smsReceiver" + e);
            }
        }
    }
}


