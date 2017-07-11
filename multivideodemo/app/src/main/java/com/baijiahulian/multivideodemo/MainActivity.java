package com.baijiahulian.multivideodemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baijiahulian.multivideodemo.multi.MultiVideoActivity;

public class MainActivity extends AppCompatActivity {
    private EditText etCode;
    private EditText etNickname;
    private Button btnEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListeners();
    }

    private void initView() {
        etCode = (EditText) findViewById(R.id.et_code);
        etNickname = (EditText) findViewById(R.id.et_nickname);
        btnEnter = (Button) findViewById(R.id.btn_enter);
    }

    private void initListeners() {
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterRoom();
            }
        });
    }

    private void enterRoom() {
        String code = etCode.getText().toString().trim();
        String name = etNickname.getText().toString().trim();
        if (TextUtils.isEmpty(code) || TextUtils.isEmpty(name)) {
            Toast.makeText(this, "参加码或昵称为空，请检查", Toast.LENGTH_LONG).show();
            return;
        }
        Intent i = new Intent(MainActivity.this, MultiVideoActivity.class);
        i.putExtra("enter_code", code);
        i.putExtra("enter_name", name);
        startActivity(i);
    }
}
