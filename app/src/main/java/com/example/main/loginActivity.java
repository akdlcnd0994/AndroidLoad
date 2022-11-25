package com.example.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class loginActivity extends AppCompatActivity {
    Button login, find;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        initializeView();
    }

    /* public void setLogin(){ //login액티비티에 대한 view를 만드는 함수
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.login, null);
    } */

    public void initializeView(){
        Button btn = (Button)findViewById(R.id.register);
        Button find = (Button)findViewById(R.id.find);
        login = (Button)findViewById(R.id.login);
        EditText EditID = (EditText)findViewById(R.id.ID);
        EditText EditPW = (EditText)findViewById(R.id.PW);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), registerActivity.class);
                //intent.putExtra("email",email); << 이렇게 사용하면 키 값과 데이터 저장 가능
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    String result;
                    String[] nickname = new String[3];
                    String ID = EditID.getText().toString();
                    String PW = EditPW.getText().toString();

                    if (!ID.equals("") && !PW.equals("")) {
                        loginProActivity task = new loginProActivity();
                        result = task.execute(ID, PW).get();

                        nickname = result.split("!");
                        if (nickname[0].equals("로그인 성공")) {

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("nickname",nickname[1]);
                            intent.putExtra("point",nickname[2]);
                            startActivity(intent);

                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                        }


                    }
                    else{
                       Toast.makeText(getApplicationContext(), "ID 또는 비밀번호가 입력되지 않았습니다.",Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    Log.i("DBtest", ".....ERROR.....!");
                    Toast.makeText(getApplicationContext(), "DB연결 에러발생", Toast.LENGTH_SHORT).show();
                }
            }
        });

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), findActivity.class);
                startActivity(intent);
            }
        });
    }
}
