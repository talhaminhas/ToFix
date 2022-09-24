package com.example.ToFix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.github.anastr.speedviewlib.SpeedView;
import com.github.capur16.digitspeedviewlib.DigitSpeedView;

public class Speedometer extends AppCompatActivity implements LocationListener {

    DigitSpeedView tv_speed;
    SwitchCompat sw_matrics;
    SpeedView speedometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speedometer);
        tv_speed = findViewById(R.id.tv_speed);
        tv_speed.hideUnit();
        sw_matrics = findViewById(R.id.sw_matrics);
        speedometer = findViewById(R.id.speedView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        } else {
            doStuff();
        }
        this.updateSpeed(null);
        sw_matrics.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Speedometer.this.updateSpeed(null);
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            CLocation myLocation = new CLocation(location, this.useMatricUnits());
            this.updateSpeed(myLocation);
        }
        //Toast.makeText(this, "Location Changed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void doStuff() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            Toast.makeText(this, "Waiting for GPS connection!", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean useMatricUnits (){
        return sw_matrics.isChecked();
    }
    private void updateSpeed(CLocation location){
        float nCurrentSpeed = 0;
        if (location != null){
            location.setUseMetricUnits(this.useMatricUnits());
            nCurrentSpeed = location.getSpeed();
        }
        /*Formatter fmt = new Formatter(new StringBuilder());
        fmt.format(Locale.US,"%5.1f",nCurrentSpeed);
        String strCurrentSpeed = fmt.toString();
        strCurrentSpeed = strCurrentSpeed.replace(" ","0");*/
        tv_speed.updateSpeed((int) nCurrentSpeed);
        speedometer.speedTo((int) nCurrentSpeed);
        if(this.useMatricUnits()){
            sw_matrics.setText("Km/h");
        }else {
            sw_matrics.setText("MPH");
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1000){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                doStuff();
            }else {
                finish();
            }
        }
    }
}
