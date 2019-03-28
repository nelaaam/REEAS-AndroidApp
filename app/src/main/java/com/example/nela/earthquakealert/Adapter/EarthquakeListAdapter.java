package com.example.nela.earthquakealert.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.nela.earthquakealert.Model.EventsData;
import com.example.nela.earthquakealert.R;

import java.util.List;

public class EarthquakeListAdapter extends ArrayAdapter<EventsData> {
    private List<EventsData> dataList;
    private Context mCtx;

    public EarthquakeListAdapter(List<EventsData> D, Context c) {
        super(c, R.layout.earthquake_list, D);
        this.dataList = D;
        this.mCtx = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.earthquake_list,  parent,false);

        TextView date = view.findViewById(R.id.dateStamp);
        TextView location = view.findViewById(R.id.locationStamp);
        TextView hypocenter = view.findViewById(R.id.hypocenterStamp);
        TextView magnitude = view.findViewById(R.id.magnitudeStamp);
        TextView time = view.findViewById(R.id.timeStamp);

        EventsData data = dataList.get(position);
        date.setText(data.getDate());
        location.setText(data.getLocation());
        hypocenter.setText(data.getHypocenter());
        magnitude.setText(data.getMagnitude());
        time.setText(data.getTime());

        return view;

    }
}
