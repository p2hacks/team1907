package com.example.p2hacks;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.SoundPool;
import android.os.Vibrator;
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

import android.media.MediaPlayer;

import static android.hardware.Sensor.TYPE_ACCELEROMETER;
import static com.example.p2hacks.R.id.onButton;

public class MainActivity extends AppCompatActivity
        implements SensorEventListener {

    private TextView textView1;//テキスト↑
    private TextView textView2;//テキスト↓
    private Button  button1;//ON, OFFボタン
    private boolean flag = false;//ON, OFFボタンのフラグ
    private boolean outflag = false;
    private SensorManager sensorManager; //センサのマネージャ
    private float bufx = 0, bufy = 0, bufz = 0; //センサの値のバッファ
    private float ct = 0;    //センサのカウンタ
    private int limit = 4;   //傾き変化量の制限
    private int hedlim = 10;    //ヘドバンが始まるまでの値
    private int hedend = 300;   //ヘドバン要求回数


    //バイブレーターオブジェクト
    Vibrator vibrator;
    private long vibPatter[] = {1000,2000};//{なり続ける時間,休憩時間}

    //MediaPlayer
    private MediaPlayer start = null;
    private MediaPlayer donpafu = null;

    private MediaPlayer stop = null;


    //private boolean move = false;
    public  boolean move = false;

    //SoundPool
    private SoundPool soundPool;
    private int soundOne, soundTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ///////////////////////////////////////////////
        donpafu  = MediaPlayer.create(this, R.raw.heavy);//メディアプレイヤー出来上がり
        start  = MediaPlayer.create(this, R.raw.push);//メディアプレイヤー出来上がり
        stop = MediaPlayer.create(this, R.raw.push);//メディアプレイヤー出来上がり
        //donpafu.start();

        ///////////////////////////////////////////////

        /*-----onボタン押したらtextView, ImageView変更-----*/
        button1 = findViewById(onButton);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        //バイブレーターの初期化
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);


        textView1 = findViewById(R.id.ueText);
        textView2 = findViewById(R.id.sitaText);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!outflag) {
                    start.start();//ボタン音声
                    flag = !flag;
                    textset(flag, outflag);
                    ct = 0;
                }
            }
        });

        /*-----設定ボタン (設定画面への遷移）-----*/
        ImageButton sendButton = findViewById(R.id.setButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), sub2.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        // Listenerの登録
        Sensor accel = sensorManager.getDefaultSensor(
                TYPE_ACCELEROMETER);

        sensorManager.registerListener(this,accel, SensorManager.SENSOR_DELAY_NORMAL);
    }

    //解除コードも入れる
    @Override
    protected void onPause(){
        super.onPause();
        //Listenerの解除
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        float sensorX, sensorY, sensorZ;
        float defx, defy, defz;

        if(event.sensor.getType() == TYPE_ACCELEROMETER){
            sensorX = event.values[0];
            sensorY = event.values[1];
            sensorZ = event.values[2];

            defx = Math.abs(sensorX - bufx);
            defy = Math.abs(sensorY - bufy);
            defz = Math.abs(sensorZ - bufz);

            bufx = sensorX;
            bufy = sensorY;
            bufz = sensorZ;

            if(defx + defy + defz > limit) ct++;

            String strct = " ct: " + ct + "\n";

            if(flag || outflag) textView2.setText(strct);//カウント確認用なのでコメントアウト

            //stopボタンのとき，ctが20を超えたらヘドバン
            if(flag && !outflag && ct > hedlim) {
                outflag = !outflag;
                textset(flag, outflag);
                ct = 0;
                vibrator.vibrate(vibPatter,1);
                /////////////////////////////////////
                donpafu.setLooping(true);
                //donpafu.setlooping(boolean looping);
                donpafu.seekTo(0);
                donpafu.start();//Start?
                //////////////////////////////////////////
                //このあたりで音声鳴らしてもよか？
            //ヘドバンのとき，ctが60を超えたとき最初の画面へ
            }else if(outflag && ct > hedend){
                outflag = !outflag;
                flag = !flag;
                textset(flag,outflag);
                ct = 0;
                vibrator.cancel();
                //////////////////////////////////////////////
                if(donpafu.isPlaying()) {
                    //donpafu.stop();
                    donpafu.pause();

                }
                ///////////////////////////////
            }

            if(outflag){
                //このときアラームを鳴らす
               // vibrator.vibrate(vibPatter,1);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }

    public void textset(boolean a, boolean b){
        if(!b){
            if(!a){
                textView1.setText("居眠り監視アラート");
                textView2.setText("ボタンを押してStart");
                button1.setText("Start");
            }else{
                textView1.setText("WATCHING…");
                //textView2.setText("居眠りを監視中…");
                button1.setText("Stop");
            }
        }else{
            textView1.setText("Warning!!!!!!!!");
            //textView1.setTextColor(Color.RED);
            textView2.setText("ヘドバンの時間だ！！！");
            //textView2.setTextColor(Color.RED);
            button1.setText("Wake Up");
        }
    }
}
