package com.example.tim.driversed;

import android.app.FragmentManager;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SlidingDrawer;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String[] mDrawerItems;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private ActionBarDrawerToggle mDrawerToggle;

    private static NewLessonFragment newLessonFragment = new NewLessonFragment();
    private static DrivingLogFragment drivingLogFragment = new DrivingLogFragment();
    private static StatisticsFragment statisticsFragment = new StatisticsFragment();

    private static int currentTitle = R.string.lesson;
    private static Fragment currentFragment = newLessonFragment;

    private TextView totalHoursText;
    private ProgressBar totalProgress;

    private DBAdapter dbAdapter;
    float totalHoursCounter = 0;

    private SharedPreferences myPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbAdapter = DBAdapter.getInstance(getApplicationContext());
        dbAdapter.open();

        System.out.println("###############");


        //Get current saved hours
        Cursor curse = dbAdapter.getAllItems();
        if (curse.moveToFirst())
            do {
                float lessonHour = new Float(curse.getFloat(1));
                totalHoursCounter += lessonHour;
                DriveLog result = new DriveLog(curse.getInt(0), curse.getFloat(1), curse.getString(2),
                        curse.getString(3), curse.getString(4), curse.getString(5));
                System.out.println(result.id + " ---- " + result.getHours());
            } while (curse.moveToNext());

        //Get currently set total hours from settings
        Context context = getApplicationContext(); // app level storage
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        float totalHoursSetting = myPrefs.getFloat("totalHours", 0.00f);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent));
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        android.support.v7.app.ActionBarDrawerToggle toggle = new android.support.v7.app.ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_closed);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        totalHoursText = (TextView) headerView.findViewById(R.id.total_hours_nav);
        totalProgress = (ProgressBar) headerView.findViewById(R.id.total_progress_nav);

        String formattedHours = String.format("%.2f", totalHoursCounter);

        totalHoursText.setText(formattedHours + "/" + totalHoursSetting + " total hours trained");
        //totalProgress.setProgress((int)(totalHoursCounter / totalHoursSetting)*100);
        totalProgress.setMax((int)totalHoursSetting);
        totalProgress.setProgress((int)totalHoursCounter);

        getFragmentManager().beginTransaction().replace(R.id.content_frame, currentFragment).commit();
        setTitle(currentTitle);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //Get currently set total hours from settings
        Context context = getApplicationContext(); // app level storage
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        float totalHoursSetting = myPrefs.getFloat("totalHours", 0.00f);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        totalHoursText = (TextView) headerView.findViewById(R.id.total_hours_nav);
        totalProgress = (ProgressBar) headerView.findViewById(R.id.total_progress_nav);

        String formattedHours = String.format("%.2f", totalHoursCounter);

        totalHoursText.setText(formattedHours + "/" + totalHoursSetting + " total hours trained");
        totalProgress.setMax((int)totalHoursSetting);
        totalProgress.setProgress((int)totalHoursCounter);

        FragmentManager fragmentManager = getFragmentManager();


        if (id == R.id.lesson_frag) {
            currentFragment = newLessonFragment;
            currentTitle = R.string.lesson;
        } else if (id == R.id.log_frag) {
            currentFragment = drivingLogFragment;
            currentTitle = R.string.log;
        } else if (id == R.id.statistics_frag) {
            currentFragment = statisticsFragment;
            currentTitle = R.string.statistics;
        }

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, currentFragment)
                .commit();
        setTitle(currentTitle);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_new_lesson) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, newLessonFragment)
                    .commit();
            setTitle(R.string.lesson);
        }

        return super.onOptionsItemSelected(item);
    }

}