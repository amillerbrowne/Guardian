package com.example.nikig.logintest;

/**
 * Created by NikiG on 3/28/17.
 */

public class Runner {

    private String firstName;
    private String lastName;
    private String age;
    private String dob;


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

    public Runner(String fname, String lname, String age, String dob) {
        this.firstName = fname;
        this.lastName = fname;
        this.age = age;
        this.dob = dob;

    }

}
