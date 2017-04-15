package com.example.tim.driversed;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

import java.util.Calendar;

public class NewLessonFragment extends Fragment {

    protected View rootView;
    protected RadioButton dayButton, nightButton;
    protected Spinner lessonSpinner, weatherSpinner;
    protected TextView dateText;
    protected EditText hoursText;
    protected Button startButton, stopButton, saveButton, cancelButton;
    private Calendar startTime, stopTime;
    private long elapsedTime;
    protected ImageButton datePicker;
    private DBAdapter dbAdapter;

    private int id;
    private float hours;
    private String date;
    private String dayNight;
    private String roadType;
    private String weatherType;

    private boolean dayNightPressed;
    private boolean stopButtonPressed;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_new_lesson, container, false);

        //Get database
        dbAdapter = DBAdapter.getInstance(getActivity().getApplicationContext());
        dbAdapter.open();

        dateText = (TextView) rootView.findViewById(R.id.date);
        hoursText = (EditText) rootView.findViewById(R.id.hours);

        hoursText.setText("");
        dateText.setText("");

        hoursText.setEnabled(false);

        lessonSpinner = (Spinner) rootView.findViewById(R.id.lesson_spinner);
        weatherSpinner = (Spinner) rootView.findViewById(R.id.weather_spinner);

        dayButton = (RadioButton) rootView.findViewById(R.id.day_radio_btn);
        nightButton = (RadioButton) rootView.findViewById(R.id.night_radio_btn);

        datePicker = (ImageButton) rootView.findViewById(R.id.date_picker);
        datePicker.setVisibility(View.GONE);

        datePicker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        startButton = (Button) rootView.findViewById(R.id.start_button);
        stopButton = (Button) rootView.findViewById(R.id.stop_button);
        saveButton = (Button) rootView.findViewById(R.id.save_button);
        cancelButton = (Button) rootView.findViewById(R.id.cancel_button);

        stopButton.setEnabled(false);
        saveButton.setEnabled(false);
        cancelButton.setEnabled(false);

        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                start();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stop();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                save();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cancel();
            }
        });

        radioGroupOnClick();

        final ArrayAdapter<CharSequence> lessonAdapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.lesson_array, R.layout.spinner_item);
        lessonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



        final ArrayAdapter<CharSequence> weatherAdapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.weather_array, R.layout.spinner_item);
        weatherAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        lessonSpinner.setAdapter(lessonAdapter);
        weatherSpinner.setAdapter(weatherAdapter);

        stopButtonPressed = false;
        dayNightPressed = false;

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void start() {
        startTime = Calendar.getInstance();
        stopButton.setEnabled(true);
        startButton.setEnabled(false);
        saveButton.setEnabled(false);
        cancelButton.setEnabled(false);
        dateText.setText(String.format("%02d", startTime.get(Calendar.MONTH) + 1) + "/" +
                String.format("%02d",startTime.get(Calendar.DAY_OF_MONTH)) + "/" + startTime.get(Calendar.YEAR));
        hoursText.setText("");

        this.date = dateText.getText().toString();
    }

    public void stop() {
        stopTime = Calendar.getInstance();
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        saveButton.setEnabled(true);
        cancelButton.setEnabled(true);
        elapsedTime = stopTime.getTimeInMillis() - startTime.getTimeInMillis();
        double hours = (double) elapsedTime / (double) 1000 / (double) 60 / (double) 60;
        hoursText.setText(String.format("%.2f", hours));

        stopButtonPressed = true;
        this.hours = (float) hours;
    }

    public void save() {
        Toast.makeText(getActivity().getApplicationContext(), "New lesson saved!", Toast.LENGTH_SHORT).show();
        resetView();

        if (dayNightPressed == true && stopButtonPressed == true) {
            //get lesson type and weather condition
            int lessonIndexValue = lessonSpinner.getSelectedItemPosition();
            String[] lessonArray = getResources().getStringArray(R.array.lesson_array);
            String lessonType = lessonArray[lessonIndexValue];

            int weatherIndexValue = weatherSpinner.getSelectedItemPosition();
            String[] weatherArray = getResources().getStringArray(R.array.weather_array);
            String weatherType = weatherArray[weatherIndexValue];

            //Update progress bar
            DriveLog driveLog = new DriveLog(this.id, this.hours, this.date, this.dayNight, lessonType, weatherType);
            dbAdapter.insertDriveLog(driveLog);
            ((MainActivity)getActivity()).totalHoursCounter += driveLog.getHours();
        }
    }

    public void cancel() {
        Toast.makeText(getActivity().getApplicationContext(), "Lesson canceled!", Toast.LENGTH_SHORT).show();
        resetView();
    }

    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                dateText.setText(String.format("%02d",(month + 1)) + "/" + String.format("%02d",day) + "/" + year);
            }
        };
        newFragment.show(getActivity().getFragmentManager(), "datePicker");
    }

    public void resetView() {
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        saveButton.setEnabled(false);
        cancelButton.setEnabled(false);
        hoursText.setText("");
        dateText.setText("");
    }

    public void updateDate(String date) {
        dateText.setText(date);
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Empty method - update actual date in New Lesson Fragment showDatePicker()
        }
    }

    private void radioGroupOnClick() {
        RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.radio_buttons);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton rb=(RadioButton)rootView.findViewById(checkedId);
                if (rb.getText().equals("Day")) {
                    dayNight = "day";
                } else {
                    dayNight = "night";
                }

                dayNightPressed = true; //set it to clicked.
            }
        });
    }
}

