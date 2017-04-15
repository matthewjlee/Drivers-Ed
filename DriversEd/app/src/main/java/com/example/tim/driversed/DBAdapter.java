package com.example.tim.driversed;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

    private SQLiteDatabase db;
    private static DBAdapter dbInstance = null;
    private DBhelper dbHelper;
    private final Context context;

    private static final String DB_NAME = "DriveLog.db";
    private static int dbVersion = 1;

    private static final String LESSONS_TABLE = "lessons";
    public static final String LESSON_ID = "lesson_id";   // column 0
    public static final String LESSON_HOURS = "lesson_hours";
    public static final String LESSON_DATE = "lesson_date";
    public static final String LESSON_DAYNIGHT = "lesson_daynight";
    public static final String LESSON_TYPE = "lesson_type";
    public static final String LESSON_CONDITION = "lesson_condition";

    public static final String[] LESSON_COLS = {LESSON_ID, LESSON_HOURS,
            LESSON_DATE, LESSON_DAYNIGHT, LESSON_TYPE, LESSON_CONDITION};

    public static synchronized DBAdapter getInstance(Context context) {
        if (dbInstance == null) {
            dbInstance = new DBAdapter(context.getApplicationContext());
        }
        return dbInstance;
    }

    private DBAdapter(Context ctx) {
        context = ctx;
        dbHelper = new DBhelper(context, DB_NAME, null, dbVersion);
    }

    public void open() throws SQLiteException {
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            db = dbHelper.getReadableDatabase();
        }
    }

    public void close() {
        db.close();
    }

    public void clear() {
        dbHelper.onUpgrade(db, dbVersion, dbVersion+1);  // change version to dump old data
        dbVersion++;
    }

    // database update methods

    public long insertDriveLog(DriveLog driveLog) {
        // create a new row of values to insert
        ContentValues cvalues = new ContentValues();
        // assign values for each col
        cvalues.put(LESSON_HOURS, driveLog.getHours());
        cvalues.put(LESSON_DATE, driveLog.getDate());
        cvalues.put(LESSON_DAYNIGHT, driveLog.getDay());
        cvalues.put(LESSON_TYPE, driveLog.getRoadType());
        cvalues.put(LESSON_CONDITION, driveLog.getWeather());
        // add to course table in database
        return db.insert(LESSONS_TABLE, null, cvalues);
    }

    public boolean removeItem(int id) {
        return db.delete(LESSONS_TABLE, "LESSON_ID="+id, null) > 0;
    }

    public boolean updateField(long id, int field, String wh) {
        ContentValues cvalue = new ContentValues();
        cvalue.put(LESSON_COLS[field], wh);
        return db.update(LESSONS_TABLE, cvalue, LESSON_ID +"="+id, null) > 0;
    }

    // database query methods
    public Cursor getAllItems() {
        return db.query(LESSONS_TABLE, LESSON_COLS, null, null, null, null, null);
    }

    public Cursor getItemCursor(long id) throws SQLException {
        Cursor result = db.query(true, LESSONS_TABLE, LESSON_COLS, LESSON_ID +"="+id, null, null, null, null, null);
        if ((result.getCount() == 0) || !result.moveToFirst()) {
            throw new SQLException("No items found for row: " + id);
        }
        return result;
    }

    public DriveLog getDriveLog(int id) throws SQLException {
        Cursor cursor = db.query(true, LESSONS_TABLE, LESSON_COLS, LESSON_ID +"="+id, null, null, null, null, null);
        if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
            throw new SQLException("No items found for row: " + id);
        }
        // must use column indices to get column values
        int hoursIndex = cursor.getColumnIndex(LESSON_HOURS);
        float hours = cursor.getFloat(hoursIndex);
        int dateIndex = cursor.getColumnIndex(LESSON_DATE);
        String date = cursor.getString(dateIndex);
        int dayNightIndex = cursor.getColumnIndex(LESSON_DAYNIGHT);
        String dayNight = cursor.getString(dayNightIndex);
        int typeIndex = cursor.getColumnIndex(LESSON_TYPE);
        String lessonType = cursor.getString(typeIndex);
        int conditionIndex = cursor.getColumnIndex(LESSON_CONDITION);
        String condition = cursor.getString(conditionIndex);
        return new DriveLog(id, hours, date, dayNight, lessonType, condition);
    }


    private static class DBhelper extends SQLiteOpenHelper {

        // SQL statement to create a new database. figure out to how to properly create?
        private static final String DB_CREATE = "CREATE TABLE " + LESSONS_TABLE
                + " (" + LESSON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + LESSON_HOURS + " TEXT, " +
                LESSON_DATE + " TEXT, " + LESSON_DAYNIGHT + " TEXT, " + LESSON_TYPE + " TEXT, " +
                LESSON_CONDITION + " TEXT);";

        public DBhelper(Context context, String name, SQLiteDatabase.CursorFactory fct, int version) {
            super(context, name, fct, version);
        }

        @Override
        public void onCreate(SQLiteDatabase adb) {
            adb.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase adb, int oldVersion, int newVersion) {
            Log.w("ItemDB", "upgrading from version " + oldVersion + " to "
                    + newVersion + ", destroying old data");
            // drop old table if it exists, create new one
            // better to migrate existing data into new table
            adb.execSQL("DROP TABLE IF EXISTS " + LESSONS_TABLE);
            onCreate(adb);
        }
    }

}