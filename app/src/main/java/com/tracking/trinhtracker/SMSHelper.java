package com.tracking.trinhtracker;

import android.content.Context;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SMSHelper {
    private static boolean smsAuthorized = false;

    public static void allowSMS() {
        smsAuthorized = true;
    }

    public static void denySMS() {
        smsAuthorized = false;
    }

    //send SMS method
    public static void sendSMSMessage(Context context, String phoneNumber, String message) {
        // Check permission to send SMS
        if (smsAuthorized) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                Toast.makeText(context, "SMS sent", Toast.LENGTH_LONG).show();
            } catch (Exception ex) {
                Toast.makeText(context, "Failed to send SMS", Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
        } else {
            Toast.makeText(context, "SMS alert disabled", Toast.LENGTH_LONG).show();
        }
    }
}
