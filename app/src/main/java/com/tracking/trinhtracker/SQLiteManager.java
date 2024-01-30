package com.tracking.trinhtracker;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SQLiteManager extends SQLiteOpenHelper {
    private static SQLiteManager sqLiteManager;

    //create an event database to save events to
    private static final String DATABASE_NAME = "Event.db";
    private static final int DATABASE_VERSION = 1;

    //create variables
    private static final class EventTable {
        private static final String TABLE = "Event";
        private static final String ID_FIELD = "id";
        private static final String TITLE_FIELD = "title";
        private static final String DESC_FIELD = "description";
        private static final String DATE_FIELD = "date";
        private static final String TIME_FIELD = "time";
    }

    //getUpcomingEvent to be use in EventnotificationHandler
    public ArrayList<Event> getUpcomingEvents() {
        ArrayList<Event> upcomingEvents = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Date currentDate = new Date();

        try (Cursor cursor = db.rawQuery("SELECT * FROM " + EventTable.TABLE +
                " WHERE " + EventTable.DATE_FIELD + " > date('now')", null)) {
            if (cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(EventTable.ID_FIELD));
                    @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(EventTable.TITLE_FIELD));
                    @SuppressLint("Range") String desc = cursor.getString(cursor.getColumnIndex(EventTable.DESC_FIELD));
                    @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(EventTable.DATE_FIELD));
                    @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(EventTable.TIME_FIELD));

                    Event event = new Event(id, title, desc, date, time);
                    upcomingEvents.add(event);
                }
            }
        } catch (Exception e) {
            Log.e("SQLiteManager", "Error while fetching upcoming events", e);
        }

        return upcomingEvents;
    }
    @SuppressLint("SimpleDateFormat")
    private static final DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

    public static SQLiteManager instanceOfDatabase(Context context) {
        if (sqLiteManager == null)
            sqLiteManager = new SQLiteManager(context);

        return sqLiteManager;
    }

    private SQLiteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //create EventTable to save user input
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + EventTable.TABLE + " (" +
                EventTable.ID_FIELD + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                EventTable.TITLE_FIELD + " TEXT, " +
                EventTable.DESC_FIELD + " TEXT, " +
                EventTable.DATE_FIELD + " TEXT, " +
                EventTable.TIME_FIELD + " TEXT) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EventTable.TABLE);
        onCreate(db);
    }

    //add event method
    public void addEventToDatabase(Event event) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(EventTable.TITLE_FIELD, event.getTitle());
        contentValues.put(EventTable.DESC_FIELD, event.getDescription());
        contentValues.put(EventTable.DATE_FIELD, event.getDate());
        contentValues.put(EventTable.TIME_FIELD, event.getTime());

        sqLiteDatabase.insert(EventTable.TABLE, null, contentValues);
    }

    //populating event to an array
    public void populateEventListArray() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + EventTable.TABLE, null)) {
            if (result.getCount() != 0) {
                while (result.moveToNext()) {
                    int id = result.getInt(0);
                    String title = result.getString(1);
                    String desc = result.getString(2);
                    String date = result.getString(3);
                    String time = result.getString(4);

                    Event event = new Event(id, title, desc, date, time);
                    com.tracking.trinhtracker.Event.eventArrayList.add(event);
                }
            }
        }
    }

    //update event method
    public void updateEventInDB(Event event) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EventTable.TITLE_FIELD, event.getTitle());
        contentValues.put(EventTable.DESC_FIELD, event.getDescription());
        contentValues.put(EventTable.DATE_FIELD, event.getDate());
        contentValues.put(EventTable.TIME_FIELD, event.getTime());

        sqLiteDatabase.update(EventTable.TABLE, contentValues, EventTable.ID_FIELD + " =? ",
                new String[]{String.valueOf(event.getId())});
    }

    //delete event method
    public void deleteEventFromDB(int eventId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(EventTable.TABLE, EventTable.ID_FIELD + " =? ",
                new String[]{String.valueOf(eventId)});
    }

}
