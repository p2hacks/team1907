package com.example.acecam;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity
        implements SensorEventListener {

    private SensorManager sensorManager;
    private TextView textView, textinfo, textdef, textct;
    private float bufx, bufy, bufz;
    private float ctX = 0, ctY = 0, ctZ = 0, c = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get on instance of the SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        textinfo = findViewById(R.id.text_info);

        //Get an instance of the TextView
        textView = findViewById(R.id.text_view);

        //Get an instance of the TextView
        textdef = findViewById(R.id.text_def);

        //Get on instance of the Textmx;
        textct = findViewById(R.id.text_ct);

        //bufs = 0
        bufx = 0;
        bufy = 0;
        bufz = 0;

    }

    @Override
    protected void onResume(){
        super.onResume();
        // Listenerの登録
        Sensor accel = sensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(this,accel, SensorManager.SENSOR_DELAY_NORMAL);
        //sensorManager.registerListener(this,accel, SensorManager.SENSOR_DELAY_FASTEST);
        //sensorManager.registerListener(this,accel, SensorManager.SENSOR_DELAY_GAME);

    }

    //解除するコードも入れる
    @Override
    protected void onPause(){
        super.onPause();
        // Listenerの解除
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        float sensorX, sensorY, sensorZ;
        float defx, defy, defz;

        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            sensorX = event.values[0];
            sensorY = event.values[1];
            sensorZ = event.values[2];

            String strtmp = "加速度センサー\n"
                    + " X: " + sensorX + "\n"
                    + " Y: " + sensorY + "\n"
                    + " Z: " + sensorZ;
            textView.setText(strtmp);

            defx = Math.abs(sensorX - bufx);
            defy = Math.abs(sensorY - bufy);
            defz = Math.abs(sensorZ - bufz);

            bufx = sensorX;
            bufy = sensorY;
            bufz = sensorZ;

            String strtap = "加速度の変化量\n"
                    + " X: " + defx + "\n"
                    + " Y: " + defy + "\n"
                    + " Z: " + defz;
            textdef.setText(strtap);

            if(sensorX > 23) ctX++;
            if(sensorY > 23) ctY++;
            if(sensorZ > 23) ctZ++;

            String strct = "回数\n"
                    + " X: " + ctX + "\n"
                    + " Y: " + ctY + "\n"
                    + " Z: " + ctZ;
            textct.setText(strct);



            showInfo(event);
        }
    }

    private void showInfo(SensorEvent event){
        //センサー名
        StringBuffer info = new StringBuffer("Name :");
        info.append(event.sensor.getName());
        info.append("\n");

        //ベンダー名
        info.append("Vender: ");
        info.append(event.sensor.getVendor());
        info.append("\n");

        //型番
        info.append("Type: ");
        info.append(event.sensor.getType());
        info.append("\n");

        //最小遅れ
        int data = event.sensor.getMinDelay();
        info.append("Mindelay: ");
        info.append(String.valueOf(data));
        info.append(" usec\n");

        //最大遅れ
        //data = event.sensor.getMaxDelay();
        //info.append("Maxdelay:");
        //info.append(String.valueOf(data));
        //info.append(" usec\n");

        textinfo.setText(info);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }
}
