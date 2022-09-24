package com.example.ToFix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class Screen2 extends AppCompatActivity {


    final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;
    LinearLayout liveLocation,speedometer,batteryStatus,endRide;
    int chargeLevel=100;
    Timer t = new Timer();
    private static final String TAG = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen2);
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                chargeLevel-=1;
                if(chargeLevel<=0){
                    t.cancel();
                }
            } },30000,30000);
        liveLocation = findViewById(R.id.liveLocation);
        liveLocation.setEnabled(false);
        if(checkPermission(Manifest.permission.SEND_SMS)){
            liveLocation.setEnabled(true);
        }else{
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
            liveLocation.setEnabled(true);
        }
        liveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //to send coordinated trough sms
                /*if(checkPermission(Manifest.permission.SEND_SMS)){
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("03155799969", null, "33.689069 73.021922", null, null);
                    Toast.makeText(getApplicationContext(), "Receiving coordinates from GPS", Toast.LENGTH_LONG).show();
                }*/
                Intent intent = new Intent(Screen2.this,LocationScreen.class);
                intent.putExtra("lat",33.689046);
                intent.putExtra("long",73.021883);
                startActivity(intent);
            }
        });
        speedometer = findViewById(R.id.speedometer);
        speedometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Screen2.this,Speedometer.class);
                startActivity(intent);
            }
        });
        batteryStatus = findViewById(R.id.batteryStatus);
        batteryStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Screen2.this, chargeLevel, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Screen2.this,Battery.class);
                intent.putExtra("chargeLevel", chargeLevel);
                startActivity(intent);
            }
        });
        endRide = findViewById(R.id.endRide);
        endRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }
}
