/* Created by Abhinav Pandey on 27 March,2023 at 2:14AM */

package com.example.begood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                FirebaseAuth mAuth=FirebaseAuth.getInstance();
                FirebaseUser user=mAuth.getCurrentUser();

                if(user!=null){
                    startActivity(new Intent(splashScreen.this,MainActivity.class));
                }
                else{
                    startActivity(new Intent(splashScreen.this,SignUp.class));
                }
                finish();
            }
        },2500);
    }
}