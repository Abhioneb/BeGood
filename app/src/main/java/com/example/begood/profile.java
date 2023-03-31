/* Created by Abhinav Pandey on 31st March, 2023 at 6:15 AM */

package com.example.begood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class profile extends AppCompatActivity {

    Button logOut;
    TextView username, phone;
    CircleImageView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logOut = findViewById(R.id.logOut);
        username = findViewById(R.id.userName);
        phone = findViewById(R.id.phoneNumber);
        profilePic = findViewById(R.id.profilePic);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("users").child(userId);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String profileUri=snapshot.child("profilePic").getValue(String.class);
                String name=snapshot.child("userName").getValue(String.class);
                String Phone=snapshot.child("phone").getValue(String.class);

                Glide.with(profilePic.getContext()).load(profileUri).into(profilePic);
                username.setText(name);
                phone.setText(Phone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(profile.this,SignIn.class));
            }
        });


    }
}