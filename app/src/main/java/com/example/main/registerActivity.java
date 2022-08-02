package com.example.main;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class registerActivity extends AppCompatActivity {
    Button registerBtn;
    EditText EditID,EditPW,EditAnswer,EditNickname;
    Spinner s;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        //String name = getIntent().getStringExtra().("name"); 이렇게 받음

        Spinner spn = findViewById(R.id.spn);

        ArrayAdapter spn_adapter = ArrayAdapter.createFromResource(this, R.array.list이름,android.R.layout.simple_spinner_item);

        spn_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(spn_adapter);

        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long I){
                String a = spn.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //여기서부터 회원가입 버튼
        setTitle("ORACLE");

        registerBtn = (Button)findViewById(R.id.regButton);
        EditID = (EditText)findViewById(R.id.EditID);
        EditPW = (EditText)findViewById(R.id.EditPW);
        EditNickname = (EditText)findViewById(R.id.EditNickname);
        EditAnswer = (EditText)findViewById(R.id.EditAnswer);
        s = (Spinner)findViewById(R.id.spn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    String result;
                    String id = EditID.getText().toString();
                    String pw = EditPW.getText().toString();
                    String nick = EditNickname.getText().toString();
                    String ans = EditAnswer.getText().toString();
                    String spn = (String) s.getSelectedItem();

                    if (id != null && pw != null && nick != null && ans != null) {
                        if(id.length()>=6){
                            RegActivity task = new RegActivity();
                            result = task.execute(id, pw, nick, ans, spn).get();
                            if (result.equals("이미 존재하는 아이디 입니다.")) {
                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "회원가입 완료!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "ID는 최소 6자리 이상입니다.",Toast.LENGTH_SHORT).show();
                        }
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
