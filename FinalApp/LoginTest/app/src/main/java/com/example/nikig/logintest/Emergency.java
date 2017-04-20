package com.example.nikig.logintest;

/**
 * Created by NikiG on 3/30/17.
 */

public class Emergency {

    private String type;
    private String efirstName;
    private String elastName;
    private String phone;
    private String relation;
    private String runnerid;

    public String getType() {
        return type;
    }

    public String getEfirstName() {
        return efirstName;
    }

    public String getElastName() {
        return elastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getRelation() {
        return relation;
    }

    public String getRunnerID() {
        return runnerid;
    }

    public void setRunnerID(String id) {
        runnerid = id;
    }



    public Emergency() {};

    public Emergency(String type, String efirstName, String elastName, String phone, String relation, String runnerid) {
        this.type = "contact";
        this.efirstName = efirstName;
        this.elastName = elastName;
        this.phone = phone;
        this.relation = relation;
        this.runnerid = runnerid;
    }

}

