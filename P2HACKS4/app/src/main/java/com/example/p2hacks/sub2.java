package com.example.p2hacks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.content.Intent;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class sub2 extends AppCompatActivity {

    private ImageButton ImageButton1;
    private int mes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub2);

        // to get message from MainActivity
        final Intent intent = getIntent();
        mes = intent.getIntExtra(MainActivity.EXTRA_MESSAGE, 0);


        ImageButton1 = findViewById(R.id.returnButton);
        ImageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup group = (RadioGroup)findViewById(R.id.radioGroup);
                int checkedId = group.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(checkedId);

                //ラジオボタンのテキストを取得
                String text = radioButton.getText().toString();

                intent.putExtra(MainActivity.EXTRA_MESSAGE, text);
                setResult(RESULT_OK, intent);

                finish();
            }
        });
    }
}
