package com.example.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import java.security.MessageDigest;

public class MainActivity extends AppCompatActivity {

    String nick = null;
    Button map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        nick = getIntent().getStringExtra("nickname");
        String po = getIntent().getStringExtra("point");


        if(nick!=null){
            Toast.makeText(getApplicationContext(), "nick = "+nick+"점수 = "+po, Toast.LENGTH_SHORT).show();
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
    void initializeView(){
        map = (Button)findViewById(R.id.map);
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