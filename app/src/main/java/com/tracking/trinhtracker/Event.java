package com.tracking.trinhtracker;

import java.util.ArrayList;

public class Event
{
    public static ArrayList<Event> eventArrayList = new ArrayList<>();
    public static String Event_EDIT_EXTRA =  "eventEdit";

    private int id;
    private String title;
    private String description;
    private String date;
    private String time;


    public Event(int id, String title, String description, String date, String time)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
    }

    //getter and setter
    public static Event getEventForID(int passedEventID)
    {
        for (Event event : eventArrayList)
        {
            if(event.getId() == passedEventID)
                return event;
        }

        return null;
    }


    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

}