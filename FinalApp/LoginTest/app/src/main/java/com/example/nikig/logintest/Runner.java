package com.example.nikig.logintest;

/**
 * Created by NikiG on 3/28/17.
 */

public class Runner {

    private String type;
    private String firstName;
    private String lastName;
    private String age;
    private String dob;
    private String emergencyid;
    private double latitude, longitude;

    public String getType() {
        return type;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAge() {
        return age;
    }

    public String getDob() {
        return dob;
    }

    public String getEmergencyid() {
        return emergencyid;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }




    public Runner (){};
    public Runner(String type, String fname, String lname, String age, String dob, String emergencyid, double latitude, double longitude) {
        this.type = "runner";
        this.firstName = fname;
        this.lastName = lname;
        this.age = age;
        this.dob = dob;
        this.emergencyid = emergencyid;
        this.latitude = getLatitude();
        this.longitude = getLongitude();

    }

}
