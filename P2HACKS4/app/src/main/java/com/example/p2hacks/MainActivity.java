package com.example.p2hacks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.p2hacks.R.id.onButton;

public class MainActivity extends AppCompatActivity {

    private TextView textView1;//テキスト↑
    private TextView textView2;//テキスト↓
    private Button  button1;//ON, OFFボタン
    private boolean flag = false;//ON, OFFボタンのフラグ

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*-----onボタン押したらtextView, ImageView変更-----*/
        button1 = findViewById(onButton);

        textView1 = findViewById(R.id.ueText);
        textView2 = findViewById(R.id.sitaText);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // flagがtrueの時
                if (flag) {
                    textView1.setText("居眠り監視アラート");
                    textView2.setText("ボタンを押してStart");
                    button1.setText("Start");

                    flag = false;
                }
                // flagがfalseの時
                else {
                    textView1.setText("WATCHING…");
                    textView2.setText("居眠りを監視中…");
                    button1.setText("Stop");

                    flag = true;
                }
            }
        });

        /*-----設定ボタン-----*/
        ImageButton sendButton = findViewById(R.id.setButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), sub2.class);
                startActivity(intent);
            }
        });

    }
}
