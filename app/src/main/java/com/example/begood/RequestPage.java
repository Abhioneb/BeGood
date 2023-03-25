package com.example.begood;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RequestPage extends AppCompatActivity {

    Button locationBtn;
    EditText state,city,landmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_page);

        Spinner mySpinner = findViewById(R.id.request_type_spinner);
        mySpinner.setPrompt("Select the request type");

        state=findViewById(R.id.state);
        city=findViewById(R.id.city);
        landmark=findViewById(R.id.landmark);

        locationBtn = findViewById(R.id.locationBtn);
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(RequestPage.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(RequestPage.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        // Update the text view with the current location
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

//                        textView.setText("Latitude: " + latitude + "\nLongitude: " + longitude);

                        // Get the city name from the location
                        Geocoder geocoder = new Geocoder(RequestPage.this, Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                            String cityName = addresses.get(0).getLocality();
                            city.setText(cityName);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // Stop listening for location updates to conserve battery
                        locationManager.removeUpdates(this);
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        // GPS has been turned on
                        LocationListener.super.onProviderEnabled(provider);
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        // GPS has been turned off, prompt the user to turn it on

                        AlertDialog.Builder builder = new AlertDialog.Builder(RequestPage.this);
                        builder.setMessage("GPS is disabled. Do you want to enable it?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        LocationListener.super.onProviderDisabled(provider);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        // Provider status has changed
                        LocationListener.super.onStatusChanged(provider, status, extras);
                    }
                });


            }
        });


    }
}