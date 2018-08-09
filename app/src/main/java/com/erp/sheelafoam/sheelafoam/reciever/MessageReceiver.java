package com.erp.sheelafoam.sheelafoam.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by twigzhp on 23/10/17.
 */

public class MessageReceiver extends BroadcastReceiver {

    final SmsManager smsManager = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String originalMessage = currentMessage.getDisplayMessageBody();
                    //String message = currentMessage.getDisplayMessageBody().toString();
                   // str = str.replaceAll("\\D+","");
                    if (originalMessage.contains("One Time Password is")) {
                        if (originalMessage.contains("for greatplus App")) {
                            String message = currentMessage.getDisplayMessageBody().replaceAll("\\D+", "");

                            //message = message.substring(0, message.length()-1);
                            Log.i("receiver", "sender: " + senderNum + "; message: " + message);

                            Intent myIntent = new Intent("otp");
                            myIntent.putExtra("message",message);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
                        }

                    }


                    // Show Alert

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("receiver", "Exception" +e);

        }
    }
}
