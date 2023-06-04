/* Created by Abhinav Pandey on 29 March at 5:20 AM */


package com.example.begood;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class pickUp extends AppCompatActivity {

    TextView Date, Time;
    EditText Quantity, Food, Location;
    FusedLocationProviderClient fusedLocationProviderClient;
    private final static int REQUEST_CODE = 100;
    Double latitude, longitude;
    Button sendOffer, locationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // component declaration
        {
            Date = findViewById(R.id.date);
            Time = findViewById(R.id.time);
            Quantity = findViewById(R.id.quantity);
            Food = findViewById(R.id.food);
            Location = findViewById(R.id.location);
            sendOffer = findViewById(R.id.continueBtn);
            locationBtn = findViewById(R.id.locationBtn);
        }

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
            }
        });
        Location.addTextChangedListener(new TextWatcher() {
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
                    Location.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                } else {
                    Location.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                }
            }
        });

        // extracting strings from components.
        {
            String date = Date.getText().toString();
            String time = Time.getText().toString();
            String quantity = Quantity.getText().toString();
            String food = Food.getText().toString();
            String location = Location.getText().toString();
        }


        sendOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });
    }

    public void send() {

        String donorId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

        mDatabase.getReference("users").child(donorId).child("userName")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        // Get the username value from the snapshot
                        String username = dataSnapshot.getValue(String.class);
                        String notificationId = mDatabase.getReference("users").child("requestId").child("notifications").push().getKey();
                        String requestID = getIntent().getStringExtra("requestId");


                        donationOffers offer = new donationOffers(notificationId, donorId, requestID,getIntent().getStringExtra("postId")
                                , username, Date.getText().toString(), Time.getText().toString(), Food.getText().toString(),
                                Quantity.getText().toString(), Location.getText().toString(),System.currentTimeMillis(),getIntent().getStringExtra("imageUri"));


                        mDatabase.getReference("users").child(requestID).child("notifications").child(notificationId).setValue(offer);
                        Toast.makeText(pickUp.this, "Notification Sent", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(pickUp.this,newsFeed.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }


    public void showDatePickerDialog(View v) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                        Date.setText(date);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    public void showTimePickerDialog(View v) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                        Time.setText(time);
                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                Geocoder geocoder = new Geocoder(pickUp.this, Locale.getDefault());
                                List<Address> addresses = null;
                                try {

                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    longitude = addresses.get(0).getLongitude();
                                    latitude = addresses.get(0).getLatitude();
                                    Location.setText(addresses.get(0).getAddressLine(0));

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                Toast.makeText(pickUp.this, "Turn on your GPS, please", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {

            askPermission();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(pickUp.this,newsFeed.class));
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(pickUp.this, new String[]
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
