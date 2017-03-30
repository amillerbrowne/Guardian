package com.example.nikig.logintest;

/**
 * Created by NikiG on 3/28/17.
 */

public class UserInformation {

    public String n;
    public String l;
    public String d;
    public String a;


    public UserInformation(String name, String last, String dob, String age) {
        this.n = name;
        this.l = last;
        this.d = dob;
        this.a = age;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }
}
