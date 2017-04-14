package com.example.tim.driversed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Array adapter for driving log list view
 */

public class DriveLogAdapter extends ArrayAdapter<DriveLog> {

    int res;

    public DriveLogAdapter(Context ctx, int res, List<DriveLog> items)  {
        super(ctx, res, items);
        this.res = res;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout logView;
        DriveLog dLog = getItem(position);

        if (convertView == null) {
            logView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(res, logView, true);
        } else {
            logView = (LinearLayout) convertView;
        }

        TextView dateHours = (TextView) logView.findViewById(R.id.date_hours);
        ImageView roadTypeImage = (ImageView) logView.findViewById(R.id.road_image);
        ImageView weatherImage = (ImageView) logView.findViewById(R.id.weather_image);

        String date = dLog.getDate();
        float hours = dLog.getHours();
        String road = dLog.getRoadType();
        String weather = dLog.getWeather();

        dateHours.setText(date + " - " + String.format("%.2f", hours) + " hours");

        switch (road) {
            case "Residential":
                roadTypeImage.setImageResource(R.drawable.home);
                break;
            case "Commercial":
                roadTypeImage.setImageResource(R.drawable.commercial);
                break;
            case "Highway":
                roadTypeImage.setImageResource(R.drawable.interstate);
                break;
            default:
                break;
        }

        switch (weather) {
            case "Clear":
                weatherImage.setImageResource(R.drawable.sun);
                break;
            case "Rainy":
                weatherImage.setImageResource(R.drawable.rain);
                break;
            case "Snow/Ice":
                weatherImage.setImageResource(R.drawable.snow);
                break;
            default:
                break;
        }

        return logView;
    }

}
