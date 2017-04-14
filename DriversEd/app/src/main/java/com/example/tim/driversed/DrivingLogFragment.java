package com.example.tim.driversed;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class DrivingLogFragment extends Fragment {

    private DBAdapter dbAdapter;
    private ListView logListView;
    protected static ArrayList<DriveLog> driveLogItems;
    protected static DriveLogAdapter aa;
    private Context context;
    private View rootView;

    public DrivingLogFragment() {
        driveLogItems = new ArrayList<DriveLog>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_driving_log, container, false);

        logListView = (ListView) rootView.findViewById(R.id.log_list_view);

        logListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = logListView.getItemAtPosition(position);
                System.out.println(listItem);

                Bundle bundle = new Bundle();
                bundle.putInt("position", driveLogItems.size() - (position));
                EditLogFragment editLogFragment = new EditLogFragment();
                editLogFragment.setArguments(bundle);

                dbAdapter.close();

                getActivity().getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, editLogFragment)
                        .addToBackStack(null)
                        .commit();
                getActivity().setTitle("Edit Log");
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        dbAdapter = DBAdapter.getInstance(getActivity().getApplicationContext());
        dbAdapter.open();

        aa = new DriveLogAdapter(getActivity().getApplicationContext(), R.layout.drive_log_item, driveLogItems);
        logListView.setAdapter(aa);
        updateArray();

        getActivity().setTitle(R.string.log);
    }

    public void updateArray() {
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
    }
}
