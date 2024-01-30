package com.tracking.trinhtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

//Event Adapter java use to display events
//on grid by getting event and translate it into the event_cell xml
public class EventAdapter extends ArrayAdapter<Event>
{
    public EventAdapter(Context context, List<Event> listView)
    {
        super(context, 0, listView);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Event event = getItem(position);
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_cell, parent, false);

        TextView title = convertView.findViewById(R.id.CellTitle);
        TextView description = convertView.findViewById(R.id.CellDescription);
        TextView date = convertView.findViewById(R.id.CellDate);
        TextView time = convertView.findViewById(R.id.CellTime);

        //getter from events
        title.setText(event.getTitle().toUpperCase());//convert text to uppercase
        description.setText(event.getDescription());
        date.setText(event.getDate());
        time.setText(event.getTime());

        return convertView;
    }
}
