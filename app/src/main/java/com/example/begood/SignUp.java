package com.example.begood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.begood.databinding.ActivitySignUpBinding;
import com.example.begood.databinding.ActivitySignInBinding;
import com.example.begood.databinding.ActivitySignUpBinding;
import com.example.begood.databinding.ActivitySignInBinding;
import com.example.begood.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ActivitySignUpBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignUp.this, SignIn.class);
                startActivity(intent);
            }
        });

        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerformAuth();
            }
        });
    }

    public void PerformAuth() {

        String email = binding.Email.getText().toString();
        String Username = binding.userName.getText().toString();
        String Password = binding.password.getText().toString();
        String Confirmpassword = binding.confirmPassword.getText().toString();
        ProgressDialog dialog = new ProgressDialog(SignUp.this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (!email.matches(emailPattern)) {
            binding.Email.setError("Enter Valid Email");
            binding.Email.requestFocus();
        } else if (Password.isEmpty() || Password.length() < 6) {
            binding.password.setError("Enter Proper Password");
        } else if (!Password.equals(Confirmpassword)) {
            binding.confirmPassword.setError("Password does not match at both fields");
        } else if (Username.isEmpty()) {
            binding.userName.setError("Enter your name");
        } else {

            dialog.setTitle("Signing Up");
            dialog.setMessage("Please wait");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();


            // Create user account with email and password
            mAuth.createUserWithEmailAndPassword(email, Password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign up success
                            FirebaseUser mUser = mAuth.getCurrentUser();
                            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

                            String userId=usersRef.push().getKey();
                            User user = new User(userId, Username, email, Password);
                            usersRef.child(userId).setValue(user);

                            Toast.makeText(this, "Sign Up Successful", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}