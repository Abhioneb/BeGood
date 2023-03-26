package com.example.begood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.begood.databinding.ActivitySignInBinding;
import com.example.begood.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;

import java.util.Objects;

public class SignIn extends AppCompatActivity {

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    Button Login;
    EditText Email,Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Login=findViewById(R.id.Login);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PerformAuth();
            }
        });
    }

    private void PerformAuth() {
        Email=findViewById(R.id.Email);
        Password=findViewById(R.id.Password);
        String email = Email.getText().toString();
        String password = Password.getText().toString();

        if (!email.matches(emailPattern)) {
            Email.setError("Enter Valid Email");
            Email.requestFocus();
        } else if (password.isEmpty() || password.length() < 6) {
            Password.setError("Enter Proper Password");
        } else {


            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(SignIn.this, "Log In Successful", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(SignIn.this, "Log In failed", Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });


        }
    }
    private void updateUI(FirebaseUser user) {
        Intent intent;
        if (user != null) {
            intent = new Intent(SignIn.this, MainActivity.class);
        } else intent = new Intent(SignIn.this, SignUp.class);
        startActivity(intent);
    }
}
