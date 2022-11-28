package com.example.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    String nick = null;
    LinearLayout list;
    TextView titleday, cont;
    ConstraintLayout listLayout, paper;
    Button map, review;
    ImageButton x1, x2;
    ArrayList<String[]> dbinfo = new ArrayList<String[]>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        nick = getIntent().getStringExtra("nickname");
        String po = getIntent().getStringExtra("point");




        if(nick!=null){

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            TextView textNick = (TextView)findViewById(R.id.nickname);
            TextView textPoint = (TextView)findViewById(R.id.point);
            textNick.setText(nick);
            textPoint.setText("보유 포인트는 "+po+"점입니다.");
            initializeView();
        }
        else {
            Intent intent = new Intent(getApplicationContext(), loginActivity.class);
            startActivity(intent);
            finish();
        }


    }
    void marking() {
        String[] sp;
        String[] temp;

        String result;
        reviewListActivity task = new reviewListActivity();


        try {
            result = task.execute().get();
            dbinfo.clear();
            if(!result.equals("작성된 글이 없습니다.")) {
                sp = result.split("@#@~#!#@#~#%");
                for (int i = 0; i < sp.length; i++) {
                    temp = sp[i].split("§§§§§§§%~#");

                    dbinfo.add(new String[]{temp[0],temp[1],temp[2],temp[3],temp[4], temp[5], temp[6], temp[7], temp[8]});

                    int n = Integer.parseInt(temp[0]); // 리뷰번호
                    String title = temp[1]; //제목
                    String content = temp[2]; //내용
                    Double lat = Double.valueOf(temp[3]); //위도
                    Double lon = Double.valueOf(temp[4]); //경도
                    String nickT = temp[5];
                    String add = temp[6];
                    String fNow = temp[7];
                    int up = Integer.parseInt(temp[8]); //추천수

                    if(nick.equals(nickT)){
                        list.removeAllViewsInLayout();
                        for(int j=0;j<dbinfo.size();j++) {
                                LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(800,200);
                                pa.setMarginStart(70);
                                listLayout.bringToFront();
                                listLayout.setVisibility(View.VISIBLE);
                                Button b = new Button(this);
                                b.setText("제목 : " + dbinfo.get(j)[1] + "\n작성자 : " + dbinfo.get(j)[5] + "\t추천 :" + dbinfo.get(j)[8] +"\n작성일 : " + dbinfo.get(j)[7]);
                                int tempI = i;
                                b.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String revNum = String.valueOf(dbinfo.get(tempI)[0]);
                                        titleday.setText("제목 : " + dbinfo.get(tempI)[1] + "\n작성자 : " + dbinfo.get(tempI)[5] + "\n작성일 : " + dbinfo.get(tempI)[7]);
                                        cont.setText(dbinfo.get(tempI)[2]);
                                        paper.bringToFront();
                                        paper.setVisibility(View.VISIBLE);
                                    }
                                });
                                list.addView(b, pa);

                            }
                        }
                    }

                }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void initializeView(){
        listLayout = (ConstraintLayout) findViewById(R.id.listLayout);
        paper = (ConstraintLayout) findViewById(R.id.paper);
        titleday = (TextView) findViewById(R.id.titleday);
        cont = (TextView) findViewById(R.id.cont);
        list = (LinearLayout) findViewById(R.id.list);
        review = (Button)findViewById(R.id.reviewCheck);
        map = (Button)findViewById(R.id.map);
        x1 = (ImageButton) findViewById(R.id.XB1);
        x2 = (ImageButton) findViewById(R.id.XB2);
        listLayout.setVisibility(View.INVISIBLE);
        paper.setVisibility(View.INVISIBLE);


        review.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                listLayout.bringToFront();
                listLayout.setVisibility(View.VISIBLE);
                marking();
            }
        });

        x1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                paper.setVisibility(View.INVISIBLE);

            }
        });

        x2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                listLayout.setVisibility(View.INVISIBLE);
                paper.setVisibility(View.INVISIBLE);
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), mapActivity.class);
                intent.putExtra("nickname",nick);
                startActivity(intent);
            }
        });
    }
}