package com.tracking.trinhtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView eventListView;

    //load all necessary java at start
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        loadFromDBToMemory();
        setEventAdapter();
        setOnClickListener();
        checkAndSendEventNotifications();
    }

    private void initWidgets() {
        eventListView = findViewById(R.id.listView);
    }

    private void loadFromDBToMemory() {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.populateEventListArray();
    }

    private void setEventAdapter() {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        ArrayList<Event> allEvents = Event.eventArrayList;
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), allEvents);
        eventListView.setAdapter(eventAdapter);
    }

    private void setOnClickListener() {
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Event selectedEvent = (Event) eventListView.getItemAtPosition(position);
                Intent editEventIntent = new Intent(getApplicationContext(), EventEditor.class);
                editEventIntent.putExtra(Event.Event_EDIT_EXTRA, selectedEvent.getId());
                startActivity(editEventIntent);
            }
        });
    }

    private void checkAndSendEventNotifications() {
        // Check the state of SMS_alert in SharedPreferences
        boolean smsAlertEnabled = SharedPreferencesHelper.getSmsAlertPreference(this);

        if (smsAlertEnabled) {
            // SMS alerts are enabled, handle upcoming events
            EventNotificationHandler.handleUpcomingEvents(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.event_menu, menu);
        return true;
    }

    public void onSmsAlertStateChanged(boolean isSmsAlertEnabled) {
        String message = isSmsAlertEnabled ? "SMS is enabled" : "SMS is disabled";
        showToastMessage(message);
    }

    private void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void newEvent(View view) {
        Intent newEventIntent = new Intent(this, EventEditor.class);
        startActivity(newEventIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setEventAdapter();

        // Check the state of SMS_alert in SharedPreferences
        boolean smsAlertEnabled = SharedPreferencesHelper.getSmsAlertPreference(this);
        if (smsAlertEnabled) {
            // SMS alerts are enabled
            SMSHelper.allowSMS();
        } else {
            // SMS alerts are disabled
            SMSHelper.denySMS();
        }
    }
}