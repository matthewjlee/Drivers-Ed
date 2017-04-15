package com.example.tim.driversed;

/**
 * DriveLog Class
 * Object that contains the information pertaining to a given driving log.
 */

public class DriveLog {

    public int id;
    public float hours;
    public String date;
    public String day;
    public String roadType;
    public String weather;

    public DriveLog() {
        // Empty Constructor
    }

    public DriveLog(int id, float hours, String date, String day, String roadType, String weather) {
        this.id = id;
        this.hours = hours;
        this.date = date;
        this.day = day;
        this.roadType = roadType;
        this.weather = weather;
    }

    public float getHours() {
        return this.hours;
    }

    public String getDate() {
        return this.date;
    }

    public String getDay() {
        return this.day;
    }

    public String getRoadType() {
        return this.roadType;
    }

    public String getWeather() {
        return this.weather;
    }

    public void setId(int newId) { this.id = newId; }

}

