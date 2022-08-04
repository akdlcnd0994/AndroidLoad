package com.example.main;

import android.text.Editable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class geoActivity {

    private static String GEOCODE_URL =  "https://dapi.kakao.com/v2/local/search/address.json?query=";
    private static String GEOCODE_USER_INFO = "KakaoAK " + "7957e36ee6a611a01e2038370c4749e5";
    String add;
    String addName,x,y;

    geoActivity(Editable add) {
        this.add = String.valueOf(add);

        new Thread() {
            public void run() {
                searchMap();
            }
        }.start();

    }

    String[] getInfo(){
        String[] info = new String[3];
        info[0] = addName;
        info[1] = y;
        info[2] = x;
        return info;
    }

    void searchMap() {
        URL obj;

        try {
            //인코딩한 String을 넘겨야 원하는 데이터를 받을 수 있다.
            String address = URLEncoder.encode(add, "UTF-8");

            obj = new URL(GEOCODE_URL + address);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", GEOCODE_USER_INFO);
            con.setRequestProperty("content-type", "application/json");
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setDefaultUseCaches(false);

            Charset charset = Charset.forName("UTF-8");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), charset));

            String inputLine;
            String response = new String();
            String temp[] = new String[30];

            while ((inputLine = in.readLine()) != null) {
                response+=inputLine;
            }

            //response 객체를 출력해보자
            if(response.contains("x")){
                //주소지 찾기
                temp = response.split("\"address_name\":");
                temp = temp[2].split("\"");
                addName = temp[1];
                
                //x,y 위경도 좌표찾기
                temp = response.split("\"x\":");
                temp = temp[1].split("\"");
                x = temp[1];

                temp = response.split("\"y\":");
                temp = temp[1].split("\"");
                y = temp[1];


            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}