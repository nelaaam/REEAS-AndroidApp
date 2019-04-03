package com.example.nela.earthquakealert.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nela.earthquakealert.Model.EventsData;
import com.example.nela.earthquakealert.R;

import java.util.ArrayList;

public class EarthquakeEventsAdapter extends RecyclerView.Adapter<EarthquakeEventsAdapter.EventsViewHolder> {
    private ArrayList<EventsData> mEventsData;

    public static class EventsViewHolder extends RecyclerView.ViewHolder {

        public TextView date, time, latitude, longitude, location, magnitude;

        public EventsViewHolder (View mView) {
            super(mView);
            date = mView.findViewById(R.id.dateStamp);
            time = mView.findViewById(R.id.timeStamp);
            latitude = mView.findViewById(R.id.latitudeStamp);
            longitude = mView.findViewById(R.id.longitudeStamp);
            location = mView.findViewById(R.id.locationStamp);
            magnitude = mView.findViewById(R.id.magnitudeStamp);
        }
    }

    public EarthquakeEventsAdapter(ArrayList<EventsData> eventsData) {
        mEventsData = eventsData;
    }
    @Override
    public EventsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.earthquake_list, viewGroup, false);
        EventsViewHolder eventsViewHolder = new EventsViewHolder(view);
        return eventsViewHolder;
    }

    @Override
    public void onBindViewHolder(EventsViewHolder holder, int i) {
        EventsData currentItem = mEventsData.get(i);
        holder.date.setText(currentItem.getDate());
        holder.time.setText(currentItem.getTime());
        holder.latitude.setText(currentItem.getLatitude());
        holder.longitude.setText(currentItem.getLongitude());
        holder.magnitude.setText(currentItem.getMagnitude());
        holder.location.setText(currentItem.getLocation());
    }

    @Override
    public int getItemCount() {
        return mEventsData.size();
    }

}