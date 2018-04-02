package com.thenaviapp.weatherapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LocationListener {
    Button allowLayoutButton;
    final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    LocationManager locationManager;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Please Wait..");

        allowLayoutButton = findViewById(R.id.allowButton);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        checkLocationPermission();
    }
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            dialog.show();
            locationManager.requestLocationUpdates("gps", 400, 1, this);
        }

        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        dialog.show();

                        //Request location updates:
                        locationManager.requestLocationUpdates("gps", 400, 1, this);
                        findViewById(R.id.locationDenied).setVisibility(View.INVISIBLE);
                        findViewById(R.id.locationAllowed).setVisibility(View.VISIBLE);
                    }

                } else {
                    findViewById(R.id.locationDenied).setVisibility(View.VISIBLE);
                    findViewById(R.id.locationAllowed).setVisibility(View.INVISIBLE);
                }
                return;
            }

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Double lat = location.getLatitude();
        Double lon = location.getLongitude();

        new GetData(this).execute("http://api.openweathermap.org/data/2.5/weather?lat="+String.valueOf(lat)+"&lon="
                +String.valueOf(lon)+"&appid=6c0e2e6aada7af6fb36cae49642ab20c&units=Metric");
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
    public void buttonClicked (View v){
        checkLocationPermission();
    }


    public void updateUI(String t, String pn, String maxt, String mint, String wi, String id, String pres, String humid){
        dialog.dismiss();

        ((TextView)findViewById(R.id.placeNameTextView)).setText(pn);
        int temp = (int) Double.parseDouble(t);
        ((TextView)findViewById(R.id.temperatureTextView)).setText(temp+"°C");
        ((TextView)findViewById(R.id.maxTemperatureTextView)).setText("Max Temp: "+maxt+"°C");
        ((TextView)findViewById(R.id.minTemperatureTextView)).setText("Min Temp: "+mint+"°C");
        ((TextView)findViewById(R.id.weatherInfoTextView)).setText(wi);
        ((TextView)findViewById(R.id.pressureTextView)).setText("Pressure: "+pres+" hPa");
        ((TextView)findViewById(R.id.humidityTextView)).setText("Humidity: "+humid+"%");
        int id1 = Integer.parseInt(id);

        if(id1>=200 && id1<=232){
            ((ImageView)findViewById(R.id.imageView)).setImageResource(R.drawable.cloud_thunderstorm);

        }
        if(id1>=300 && id1<=321){
            ((ImageView)findViewById(R.id.imageView)).setImageResource(R.drawable.cloud_rain);
        }
        if(id1>=500 && id1<=531){
            ((ImageView)findViewById(R.id.imageView)).setImageResource(R.drawable.cloud_thunderstorm_rain);
        }
        if(id1>=600 && id1<=622){
            ((ImageView)findViewById(R.id.imageView)).setImageResource(R.drawable.snow);
        }
        if(id1>=701 && id1<=781){
            ((ImageView)findViewById(R.id.imageView)).setImageResource(R.drawable.mist);
        }
        if(id1==800){
            ((ImageView)findViewById(R.id.imageView)).setImageResource(R.drawable.sun);
        }
        if(id1>=801 && id1<=802){
            ((ImageView)findViewById(R.id.imageView)).setImageResource(R.drawable.few_cloud);
        }
        if(id1>=803 && id1<=804){
            ((ImageView)findViewById(R.id.imageView)).setImageResource(R.drawable.dark_cloud);
        }
        if(id1>=951 && id1<=962){
            ((ImageView)findViewById(R.id.imageView)).setImageResource(R.drawable.windy);
        }
    }
}
