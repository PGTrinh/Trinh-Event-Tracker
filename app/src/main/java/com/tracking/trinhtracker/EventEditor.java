package com.tracking.trinhtracker;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class EventEditor extends AppCompatActivity {
    private EditText titleEditText, descEditText, dateEditText, timeEditText;
    private Button deleteButton;
    private Event selectedEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_editor);
        initWidgets();
        checkForEditEvent();
    }

    private void initWidgets() {
        titleEditText = findViewById(R.id.eventTitle);
        descEditText = findViewById(R.id.eventDescription);
        dateEditText = findViewById(R.id.eventDate);
        timeEditText = findViewById(R.id.eventTime);
        deleteButton = findViewById(R.id.deleteEventButton);

        // Set click listeners for date and time EditText fields
        dateEditText.setOnClickListener(v -> showDatePicker());
        timeEditText.setOnClickListener(v -> showTimePicker());
    }

    private void checkForEditEvent() {
        Intent previousIntent = getIntent();

        int passedEventID = previousIntent.getIntExtra(Event.Event_EDIT_EXTRA, -1);
        selectedEvent = Event.getEventForID(passedEventID);

        if (selectedEvent != null) {
            titleEditText.setText(selectedEvent.getTitle());
            descEditText.setText(selectedEvent.getDescription());
            dateEditText.setText(selectedEvent.getDate());
            timeEditText.setText(selectedEvent.getTime());
        } else {
            deleteButton.setVisibility(View.INVISIBLE);
        }
    }

    //save event method
    public void saveEvent(View view) {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        String title = String.valueOf(titleEditText.getText());
        String desc = String.valueOf(descEditText.getText());
        String date = String.valueOf(dateEditText.getText());
        String time = String.valueOf(timeEditText.getText());

        if (selectedEvent == null) {
            int id = Event.eventArrayList.size();
            Event newEvent = new Event(id, title, desc, date, time);
            Event.eventArrayList.add(newEvent);
            sqLiteManager.addEventToDatabase(newEvent);
        } else {
            selectedEvent.setTitle(title);
            selectedEvent.setDescription(desc);
            selectedEvent.setDate(date);
            selectedEvent.setTime(time);
            sqLiteManager.updateEventInDB(selectedEvent);
        }

        finish();
    }

    //delete event method
    public void deleteEvent(View view) {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);

        if (selectedEvent != null) {
            // Remove the event from the list
            Event.eventArrayList.remove(selectedEvent);

            // Mark the event as deleted in the database
            sqLiteManager.deleteEventFromDB(selectedEvent.getId());
        }

        finish();
    }

    //use a date picker to get exact date format to be use in event notification
    private void showDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            String formattedDate = String.format("%02d/%02d/%04d", monthOfYear + 1, dayOfMonth, year);
            dateEditText.setText(formattedDate);
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
        datePickerDialog.show();
    }


    //use a time picker to get exact time format to be use in event notification
    private void showTimePicker() {
        TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
            String formattedTime = String.format("%02d:%02d", hourOfDay, minute);
            timeEditText.setText(formattedTime);
        };

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, timeSetListener, hour, minute, true);
        timePickerDialog.show();
    }
}
