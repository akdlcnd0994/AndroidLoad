package com.example.main;

import android.app.Application;

public class ip extends Application {
    private String address = "192.168.35.12";

    public String getIp(){
        return address;
    }

}
