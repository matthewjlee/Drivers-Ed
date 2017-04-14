package com.example.tim.driversed;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import static com.example.tim.driversed.DrivingLogFragment.driveLogItems;

public class StatisticsFragment extends Fragment {

    private View rootView;
    private ProgressBar totalProgress;
    private ProgressBar dayProgress;
    private ProgressBar nightProgress;
    private ProgressBar residentialProgress;
    private ProgressBar highwayProgress;
    private ProgressBar commercialProgress;
    private ProgressBar clearProgress;
    private ProgressBar rainProgress;
    private ProgressBar snowProgress;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_statistics, container, false);

        totalProgress = (ProgressBar) rootView.findViewById(R.id.total_progress);
        dayProgress = (ProgressBar) rootView.findViewById(R.id.day_progress);
        nightProgress = (ProgressBar) rootView.findViewById(R.id.night_progress);
        residentialProgress = (ProgressBar) rootView.findViewById(R.id.residential_progress);
        highwayProgress = (ProgressBar) rootView.findViewById(R.id.highway_progress);
        commercialProgress = (ProgressBar) rootView.findViewById(R.id.commercial_progress);
        clearProgress = (ProgressBar) rootView.findViewById(R.id.clear_progress);
        rainProgress = (ProgressBar) rootView.findViewById(R.id.rain_progress);
        snowProgress = (ProgressBar) rootView.findViewById(R.id.snow_progress);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        populateProgress();
    }

    // Method to set progress bar values
    public void populateProgress() {
        DBAdapter dbAdapter = DBAdapter.getInstance(getActivity().getApplicationContext());
        dbAdapter.open();

        //Get currently set total hours from settings
        Context context = getActivity().getApplicationContext(); // app level storage
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        float totalHoursSetting = myPrefs.getFloat("totalHours", 0.00f);
        totalProgress.setProgress((int) ((((MainActivity)getActivity()).totalHoursCounter / totalHoursSetting)*100));
        TextView totalHoursText = (TextView) rootView.findViewById(R.id.total_hours);
        String formattedHours = String.format("%.2f", ((MainActivity)getActivity()).totalHoursCounter);
        totalHoursText.setText(formattedHours + "/" + totalHoursSetting + " total hours trained");


        float totalDaySetting = myPrefs.getFloat("dayHours", 0.00f);
        float dayHoursCounter = 0;
        Cursor curse = dbAdapter.getAllItems();
        if (curse.moveToFirst())
            do {
                DriveLog result = new DriveLog(curse.getInt(0), curse.getFloat(1), curse.getString(2),
                        curse.getString(3), curse.getString(4), curse.getString(5));
                if (result.getDay().equals("day")) {
                    dayHoursCounter += result.getHours();
                }
            } while (curse.moveToNext());
        curse.close();
        dayProgress.setProgress((int) ((dayHoursCounter / totalDaySetting)*100));
        TextView dayHoursText = (TextView) rootView.findViewById(R.id.day_hours);
        String formatDayHours = String.format("%.2f", (dayHoursCounter));
        dayHoursText.setText("Day: " + formatDayHours + "/" + totalDaySetting + " hours");


        float totalNightSetting = myPrefs.getFloat("nightHours", 0.00f);
        float nightHoursCounter = 0;
        curse = dbAdapter.getAllItems();
        if (curse.moveToFirst())
            do {
                DriveLog result = new DriveLog(curse.getInt(0), curse.getFloat(1), curse.getString(2),
                        curse.getString(3), curse.getString(4), curse.getString(5));
                if (result.getDay().equals("night")) {
                    nightHoursCounter += result.getHours();
                }
            } while (curse.moveToNext());
        curse.close();
        nightProgress.setProgress((int) ((nightHoursCounter / totalNightSetting)*100));
        TextView nightHoursText = (TextView) rootView.findViewById(R.id.night_hours);
        String formatNightHours = String.format("%.2f", (nightHoursCounter));
        nightHoursText.setText("Night: " + formatNightHours + "/" + totalNightSetting + " hours");


        float totalResidentialSetting = myPrefs.getFloat("residentialHours", 0.00f);
        float residentialHoursCounter = 0;
        curse = dbAdapter.getAllItems();
        if (curse.moveToFirst())
            do {
                DriveLog result = new DriveLog(curse.getInt(0), curse.getFloat(1), curse.getString(2),
                        curse.getString(3), curse.getString(4), curse.getString(5));
                if (result.getRoadType().equals("Residential")) {
                    residentialHoursCounter += result.getHours();
                }
            } while (curse.moveToNext());
        curse.close();
        residentialProgress.setProgress((int) ((residentialHoursCounter / totalResidentialSetting)*100));
        TextView residentialHoursText = (TextView) rootView.findViewById(R.id.residential_hours);
        String formatResidentialHours = String.format("%.2f", (residentialHoursCounter));
        residentialHoursText.setText("Residential: " + formatResidentialHours + "/" + totalResidentialSetting + " hours");


        float totalHighwaySetting = myPrefs.getFloat("highwayHours", 0.00f);
        float highwayHoursCounter = 0;
        curse = dbAdapter.getAllItems();
        if (curse.moveToFirst())
            do {
                DriveLog result = new DriveLog(curse.getInt(0), curse.getFloat(1), curse.getString(2),
                        curse.getString(3), curse.getString(4), curse.getString(5));
                if (result.getRoadType().equals("Highway")) {
                    highwayHoursCounter += result.getHours();
                }
            } while (curse.moveToNext());
        curse.close();
        highwayProgress.setProgress((int) ((highwayHoursCounter / totalHighwaySetting)*100));
        TextView highwayHoursText = (TextView) rootView.findViewById(R.id.highway_hours);
        String formatHighwayHours = String.format("%.2f", (highwayHoursCounter));
        highwayHoursText.setText("Highway: " + formatHighwayHours + "/" + totalHighwaySetting + " hours");


        float totalCommercialSetting = myPrefs.getFloat("commercialHours", 0.00f);
        float commercialHoursCounter = 0;
        curse = dbAdapter.getAllItems();
        if (curse.moveToFirst())
            do {
                DriveLog result = new DriveLog(curse.getInt(0), curse.getFloat(1), curse.getString(2),
                        curse.getString(3), curse.getString(4), curse.getString(5));
                if (result.getRoadType().equals("Commercial")) {
                    commercialHoursCounter += result.getHours();
                }
            } while (curse.moveToNext());
        curse.close();
        commercialProgress.setProgress((int) ((commercialHoursCounter / totalCommercialSetting)*100));
        TextView commercialHoursText = (TextView) rootView.findViewById(R.id.commercial_hours);
        String formatCommercialHours = String.format("%.2f", (commercialHoursCounter));
        commercialHoursText.setText("Commercial: " + formatCommercialHours + "/" + totalCommercialSetting + " hours");

        float totalClearHours = myPrefs.getFloat("clearHours", 0.00f);
        float clearHoursCounter = 0;
        curse = dbAdapter.getAllItems();
        if (curse.moveToFirst())
            do {
                DriveLog result = new DriveLog(curse.getInt(0), curse.getFloat(1), curse.getString(2),
                        curse.getString(3), curse.getString(4), curse.getString(5));
                if (result.getWeather().equals("Clear")) {
                    clearHoursCounter += result.getHours();
                }
            } while (curse.moveToNext());
        curse.close();
        clearProgress.setProgress((int) ((clearHoursCounter / totalClearHours)*100));
        TextView clearHoursText = (TextView) rootView.findViewById(R.id.clear_hours);
        String formatClearHours = String.format("%.2f", (clearHoursCounter));
        clearHoursText.setText("Clear: " + formatClearHours + "/" + totalClearHours + " hours");


        float totalRainHours = myPrefs.getFloat("rainyHours", 0.00f);
        float rainHoursCounter = 0;
        curse = dbAdapter.getAllItems();
        if (curse.moveToFirst())
            do {
                DriveLog result = new DriveLog(curse.getInt(0), curse.getFloat(1), curse.getString(2),
                        curse.getString(3), curse.getString(4), curse.getString(5));
                if (result.getWeather().equals("Rainy")) {
                    rainHoursCounter += result.getHours();
                }
            } while (curse.moveToNext());
        curse.close();
        rainProgress.setProgress((int) ((rainHoursCounter / totalRainHours)*100));
        TextView rainHoursText = (TextView) rootView.findViewById(R.id.rain_hours);
        String formatRainHours = String.format("%.2f", (rainHoursCounter));
        rainHoursText.setText("Rainy: " + formatRainHours + "/" + totalRainHours + " hours");


        float totalSnowHours = myPrefs.getFloat("snowHours", 0.00f);
        float snowHoursCounter = 0;
        curse = dbAdapter.getAllItems();
        if (curse.moveToFirst())
            do {
                DriveLog result = new DriveLog(curse.getInt(0), curse.getFloat(1), curse.getString(2),
                        curse.getString(3), curse.getString(4), curse.getString(5));
                if (result.getWeather().equals("Snow/Ice")) {
                    snowHoursCounter += result.getHours();
                }
            } while (curse.moveToNext());
        curse.close();
        snowProgress.setProgress((int) ((snowHoursCounter / totalSnowHours)*100));
        TextView snowHoursText = (TextView) rootView.findViewById(R.id.snow_hours);
        String formatSnowHours = String.format("%.2f", (snowHoursCounter));
        snowHoursText.setText("Snowy/Icy: " + formatSnowHours + "/" + totalSnowHours + " hours");

        dbAdapter.close();
    }
}
