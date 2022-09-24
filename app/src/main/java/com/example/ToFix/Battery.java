package com.example.ToFix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import eo.view.batterymeter.BatteryMeterView;

public class Battery extends AppCompatActivity {

    TextView tv_percent;
    BatteryMeterView batteryMeterView;
    int chargeLevel;
    Timer t = new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery);
        tv_percent = findViewById(R.id.tv_percent);
        batteryMeterView = findViewById(R.id.batteryMeterView);

        Intent intent = this.getIntent();
        chargeLevel = intent.getIntExtra("chargeLevel",0);
        tv_percent.setText(chargeLevel +" %");
        batteryMeterView.setChargeLevel(chargeLevel);
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                chargeLevel-=1;
                tv_percent.setText(chargeLevel +" %");
                batteryMeterView.setChargeLevel(chargeLevel);
                if(chargeLevel<=0){
                    t.cancel();
                }
            } },30000,30000);
        //Toast.makeText(this, chargeLevel, Toast.LENGTH_LONG).show();
    }

}


