package com.tracking.trinhtracker;

import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class EventNotificationHandler {

    //get upcoming event from SQlitemanager
    public static void handleUpcomingEvents(Context context) {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(context);
        List<Event> upcomingEvents = sqLiteManager.getUpcomingEvents();

        for (Event event : upcomingEvents) {
            sendEventNotification(context, event);
        }
    }

    //sending event method
    private static void sendEventNotification(Context context, Event event) {
        // Format the event date for comparison
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String eventDate = dateFormat.format(event.getDate());

        // Get today's date for comparison
        Calendar calendar = Calendar.getInstance();
        String currentDate = dateFormat.format(calendar.getTime());

        // Check if the event date is today
        if (eventDate.equals(currentDate)) {
            // Check if permissions are granted
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                // Get phone number
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                String phoneNumber = telephonyManager.getLine1Number();
                if (phoneNumber != null && !phoneNumber.isEmpty()) {
                    // Send SMS notification
                    String notificationMessage = "Upcoming event: " + event.getTitle();
                    SMSHelper.sendSMSMessage(context, phoneNumber, notificationMessage);
                } else {
                    // Handle null or empty phone number
                    Toast.makeText(context, "Unable to retrieve phone number", Toast.LENGTH_SHORT).show();
                    // You might want to log this issue or take appropriate action
                }

                // Send SMS notification
                String notificationMessage = "Upcoming event: " + event.getTitle();
                SMSHelper.sendSMSMessage(context, phoneNumber, notificationMessage);
            }
        }
    }
}
