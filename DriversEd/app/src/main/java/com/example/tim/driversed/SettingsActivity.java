package com.example.tim.driversed;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences myPrefs;
    private EditText totalHours;
    private EditText dayHours;
    private EditText nightHours;
    private EditText residentialHours;
    private EditText commercialHours;
    private EditText highwayHours;
    private EditText clearHours;
    private EditText rainyHours;
    private EditText snowHours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent));
        toolbar.setTitle("Settings");
        setSupportActionBar(toolbar);

        Context context = getApplicationContext(); // app level storage
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        totalHours = (EditText) findViewById(R.id.total_settings);
        dayHours = (EditText) findViewById(R.id.day_settings);
        nightHours = (EditText) findViewById(R.id.night_settings);
        residentialHours = (EditText) findViewById(R.id.residential_settings);
        commercialHours = (EditText) findViewById(R.id.commercial_settings);
        highwayHours = (EditText) findViewById(R.id.highway_settings);
        clearHours = (EditText) findViewById(R.id.clear_settings);
        rainyHours = (EditText) findViewById(R.id.rainy_settings);
        snowHours = (EditText) findViewById(R.id.snow_settings);

        totalHours.setText(String.format("%.0f", myPrefs.getFloat("totalHours", (float) 10.00)));
        dayHours.setText(String.format("%.0f", myPrefs.getFloat("dayHours", (float) 10.00)));
        nightHours.setText(String.format("%.0f", myPrefs.getFloat("nightHours", (float) 10.00)));
        residentialHours.setText(String.format("%.0f", myPrefs.getFloat("residentialHours", (float) 10.00)));
        commercialHours.setText(String.format("%.0f", myPrefs.getFloat("commercialHours", (float) 10.00)));
        highwayHours.setText(String.format("%.0f", myPrefs.getFloat("highwayHours", (float) 10.00)));
        clearHours.setText(String.format("%.0f", myPrefs.getFloat("clearHours", (float) 10.00)));
        rainyHours.setText(String.format("%.0f", myPrefs.getFloat("rainyHours", (float) 10.00)));
        snowHours.setText(String.format("%.0f", myPrefs.getFloat("snowHours", (float) 10.00)));
    }

    public void onSave(View v) {

        SharedPreferences.Editor peditor = myPrefs.edit();

        peditor.putFloat("totalHours", Float.parseFloat(totalHours.getText().toString()));
        peditor.putFloat("dayHours", Float.parseFloat(dayHours.getText().toString()));
        peditor.putFloat("nightHours", Float.parseFloat(nightHours.getText().toString()));
        peditor.putFloat("residentialHours", Float.parseFloat(residentialHours.getText().toString()));
        peditor.putFloat("commercialHours", Float.parseFloat(commercialHours.getText().toString()));
        peditor.putFloat("highwayHours", Float.parseFloat(highwayHours.getText().toString()));
        peditor.putFloat("clearHours", Float.parseFloat(clearHours.getText().toString()));
        peditor.putFloat("rainyHours", Float.parseFloat(rainyHours.getText().toString()));
        peditor.putFloat("snowHours", Float.parseFloat(snowHours.getText().toString()));

        peditor.commit();

        Context context = getApplicationContext();
        CharSequence text = "Settings Successfully Updated!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
