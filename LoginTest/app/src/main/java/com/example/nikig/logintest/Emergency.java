package com.example.nikig.logintest;

/**
 * Created by NikiG on 3/30/17.
 */

public class Emergency {
    private String efirstName;
    private String elastName;
    private String phone;
    private String relation;

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

    public Emergency() {};

    public Emergency(String efirstName, String elastName, String phone, String relation) {
        this.efirstName = efirstName;
        this.elastName = elastName;
        this.phone = phone;
        this.relation = relation;
    }

}

