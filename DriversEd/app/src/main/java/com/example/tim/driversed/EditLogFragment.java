package com.example.tim.driversed;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;

import static com.example.tim.driversed.DrivingLogFragment.aa;
import static com.example.tim.driversed.DrivingLogFragment.driveLogItems;

/**
 * Created by khochberg on 4/3/17.
 */

public class EditLogFragment extends NewLessonFragment {

    private View rootView;
    private DBAdapter dbAdapter;
    private DriveLog driveLog;

    private int id;

    public EditLogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.id = bundle.getInt("position", id);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = super.onCreateView(inflater, container, savedInstanceState);

        //create the database
        dbAdapter = DBAdapter.getInstance(getActivity().getApplicationContext());
        dbAdapter.open();

        saveButton.setText(R.string.update);
        startButton.setVisibility(View.GONE);
        stopButton.setText(R.string.delete);

        datePicker.setVisibility(View.VISIBLE);

        stopButton.setEnabled(true);
        saveButton.setEnabled(true);
        cancelButton.setEnabled(true);

        populateData();

        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                delete();
            }
        });

        hoursText.setEnabled(true);
        hoursText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String orig = v.getText().toString();
                int hrs;
                try {
                    hrs = Integer.parseInt(hoursText.getText().toString());
                    hoursText.setText(String.format("%.2f", hrs));
                    return true;
                } catch (NumberFormatException bad) {
                    Toast.makeText(getActivity().getApplicationContext(), "ERROR: invalid hours", Toast.LENGTH_LONG).show();
                    hoursText.setText(orig);
                }
                return false;
            }
        });

        return rootView;
    }


    public void populateData() {
        driveLog = dbAdapter.getDriveLog(this.id);

        dateText.setText(driveLog.getDate());
        hoursText.setText(String.format("%.2f", driveLog.getHours()));
        if (driveLog.getDay().equals("day")) {
            RadioButton radioButton = (RadioButton) rootView.findViewById(R.id.day_radio_btn);
            radioButton.setChecked(true);
        } else {
            RadioButton radioButton = (RadioButton) rootView.findViewById(R.id.night_radio_btn);
            radioButton.setChecked(true);
        }

        Spinner roadSpinner = (Spinner) rootView.findViewById(R.id.lesson_spinner);
        String roadType = driveLog.getRoadType();
        String[] roadTypeArray = getResources().getStringArray(R.array.lesson_array);
        for (int i = 0; i < roadTypeArray.length; i++) {
            if (roadTypeArray[i].equals(roadType)) {
                roadSpinner.setSelection(i);
            }
        }

        Spinner weatherSpinner = (Spinner) rootView.findViewById(R.id.weather_spinner);
        String weatherType = driveLog.getWeather();
        String[] weatherTypeArray = getResources().getStringArray(R.array.weather_array);
        for (int i = 0; i < weatherTypeArray.length; i++) {
            if (weatherTypeArray[i].equals(weatherType)) {
                weatherSpinner.setSelection(i);
            }
        }
    }

    public void delete() {
        // Confirmation to delete:
        AlertDialog deleteDialogue = new AlertDialog.Builder(getActivity())
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete from database
                        DriveLog current = dbAdapter.getDriveLog(id);
                        ((MainActivity)getActivity()).totalHoursCounter -= current.getHours();
                        dbAdapter.removeItem(id);
                        dbAdapter.close();

                        getActivity().getFragmentManager().popBackStack();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void cancel() {
        // Nothing should happen on cancels
        getActivity().getFragmentManager().popBackStack();
    }

    @Override
    public void save() {
        // Save updates to database
        AlertDialog updateDialogue = new AlertDialog.Builder(getActivity())
                .setTitle("Update")
                .setMessage("Are you sure you want to update this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        // Update entry in database
                        ((MainActivity)getActivity()).totalHoursCounter -= driveLog.getHours();
                        dbAdapter.updateField(id, 1, hoursText.getText().toString());
                        ((MainActivity)getActivity()).totalHoursCounter += Float.parseFloat(hoursText.getText().toString());

                        dbAdapter.updateField(id, 2, dateText.getText().toString());

                        RadioButton radioButton = (RadioButton) rootView.findViewById(R.id.day_radio_btn);
                        if (radioButton.isChecked()) {
                            dbAdapter.updateField(id, 3, "day");
                        } else {
                            dbAdapter.updateField(id, 3, "night");
                        }

                        int lessonIndexValue = lessonSpinner.getSelectedItemPosition();
                        String[] lessonArray = getResources().getStringArray(R.array.lesson_array);
                        String lessonType = lessonArray[lessonIndexValue];

                        int weatherIndexValue = weatherSpinner.getSelectedItemPosition();
                        String[] weatherArray = getResources().getStringArray(R.array.weather_array);
                        String weatherType = weatherArray[weatherIndexValue];

                        dbAdapter.updateField(id, 4, lessonType);
                        dbAdapter.updateField(id, 5, weatherType);


                        Toast.makeText(getActivity().getApplicationContext(), "Successfully updated entry!.", Toast.LENGTH_SHORT).show();

                        Cursor curse = dbAdapter.getAllItems();
                        driveLogItems.clear();
                        if (curse.moveToFirst())
                            do {
                                DriveLog result = new DriveLog(curse.getInt(0), curse.getFloat(1), curse.getString(2),
                                        curse.getString(3), curse.getString(4), curse.getString(5));
                                driveLogItems.add(0, result);  // puts in reverse order
                            } while (curse.moveToNext());
                        curse.close();
                        aa.notifyDataSetChanged();

                        dbAdapter.close();
                        getActivity().getFragmentManager().popBackStack();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
