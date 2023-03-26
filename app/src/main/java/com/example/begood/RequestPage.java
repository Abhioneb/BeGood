package com.example.begood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RequestPage extends AppCompatActivity {

    FusedLocationProviderClient fusedLocationProviderClient;
    EditText state, city, address;
    Button locationBtn, continueBtn;
    Spinner requestType;
    Double latitude, longitude;
    private final static int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_page);

        state = findViewById(R.id.state);
        city = findViewById(R.id.city);
        address = findViewById(R.id.address);
        requestType = findViewById(R.id.request_type_spinner);
        String request = requestType.getSelectedItem().toString();

        continueBtn = findViewById(R.id.continueBtn);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RequestPage.this, ImageUpload.class);
                intent.putExtra("state", state.getText().toString());
                intent.putExtra("city", city.getText().toString());
                intent.putExtra("address", address.getText().toString());
                intent.putExtra("requestType", request);

                intent.putExtra("latitudeSent",latitude);
                intent.putExtra("longitudeSent",longitude);


                startActivity(intent);
            }
        });

        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();

                if (length < 30) {
                    address.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                } else {
                    address.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                }
            }
        });

        locationBtn = findViewById(R.id.locationBtn);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
            }
        });
    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                Geocoder geocoder = new Geocoder(RequestPage.this, Locale.getDefault());
                                List<Address> addresses = null;
                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                                    longitude = addresses.get(0).getLongitude();
                                    latitude = addresses.get(0).getLatitude();

                                    address.setText(addresses.get(0).getAddressLine(0));
                                    city.setText(addresses.get(0).getLocality());
                                    state.setText(addresses.get(0).getAdminArea());

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }

                        }
                    });


        } else {

            askPermission();

        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(RequestPage.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Required Permission", Toast.LENGTH_SHORT).show();
            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}