package com.example.main;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class findActivity extends AppCompatActivity {
    Button findBtn, findPW, findID;
    EditText EditInfo,EditAnswer;
    TextView info;
    Spinner s;

    int check = 0;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find);
        //String name = getIntent().getStringExtra().("name"); 이렇게 받음

        Spinner spn2 = findViewById(R.id.spn2);

        ArrayAdapter spn_adapter = ArrayAdapter.createFromResource(this, R.array.list이름,android.R.layout.simple_spinner_item);

        spn_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn2.setAdapter(spn_adapter);

        spn2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long I){
                String a = spn2.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //여기서부터 회원가입 버튼
        setTitle("ORACLE");

        info = (TextView)findViewById(R.id.info);
        findBtn = (Button)findViewById(R.id.findButton);
        findID = (Button)findViewById(R.id.findID);
        findPW = (Button)findViewById(R.id.findPW);
        EditInfo = (EditText)findViewById(R.id.EditInfo);
        EditAnswer = (EditText)findViewById(R.id.EditAnswer2);
        s = (Spinner)findViewById(R.id.spn2);

        findPW.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                info.setText("아이디");
                EditInfo.setHint("ID");
                check=1;
                findPW.setBackgroundColor(Color.WHITE);
                findID.setBackgroundColor(Color.MAGENTA);
            }
        });

        findID.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                info.setText("닉네임");
                EditInfo.setHint("Nickname");
                check=0;
                findID.setBackgroundColor(Color.WHITE);
                findPW.setBackgroundColor(Color.MAGENTA);
            }
        });

        findBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                System.out.println("aaaaaaaa");
                try {
                    String result;
                    String inf = EditInfo.getText().toString();
                    String ans = EditAnswer.getText().toString();
                    String spn = (String) s.getSelectedItem();
                    String ch = String.valueOf(check);

                    if (inf != null && ans != null) {
                        findingActivity task = new findingActivity();
                        result = task.execute(inf, ans, spn, ch).get();
                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "비어있는 칸을 채워주세요.",Toast.LENGTH_SHORT).show();
                    }
                } catch(Exception e){
                    Log.i("DBtest", ".....ERROR.....!");
                }
            }
        });
    }

}
