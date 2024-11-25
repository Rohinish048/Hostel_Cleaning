package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.perfmark.Tag;

import static android.content.ContentValues.TAG;

public class activity_application extends AppCompatActivity {
    EditText mFname, mLname, mEmail, mPassword, mPhone, mPaddress;
    Button registerBtn;
    TextView loginBtn;
    private FirebaseAuth mAuth;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        mFname=findViewById(R.id.firstname);
        mLname=findViewById(R.id.lastname);
        mEmail=findViewById(R.id.email);
        mPassword=findViewById(R.id.password);
        mPhone=findViewById(R.id.phone);
        mPaddress=findViewById(R.id.paddress);
        registerBtn=findViewById(R.id.register);
        loginBtn=findViewById(R.id.loginText);


        mAuth=FirebaseAuth.getInstance();

//        if (mAuth.getCurrentUser() != null){
//            startActivity(new Intent(getApplicationContext(), activity_studentdash.class));
//            finish();
//        }

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String fname = mFname.getText().toString().trim();
                String lname = mLname.getText().toString().trim();
                String phone = mPhone.getText().toString().trim();
                String paddress = mPaddress.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required.");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Password is required.");
                    return;
                }
                if (TextUtils.isEmpty(fname)){
                    mFname.setError("First name is required.");
                    return;
                }if (TextUtils.isEmpty(lname)){
                    mLname.setError("Last name is required.");
                    return;
                }
                if (password.length() < 6 ){
                    mPassword.setError("Password must contain greater than 6 characters.");
                    return;
                }


                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                            Toast.makeText(activity_application.this, "User created successfully, Please verify your Email", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(),activity_login.class));
                                        String userID = user.getUid();
                                        DatabaseReference mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("userID").child(userID);
                                        mFirebaseDatabase.child("First name").setValue(fname);
                                        mFirebaseDatabase.child("Last name").setValue(lname);
                                        mFirebaseDatabase.child("Email").setValue(email);
                                        mFirebaseDatabase.child("Phone number").setValue(phone);
                                        mFirebaseDatabase.child("Postal address").setValue(paddress);
                                        mFirebaseDatabase.child("Postal address").setValue(paddress);

//                                        if (user.isEmailVerified()){
////                                            registerBtn.setVisibility(View.INVISIBLE);
//                                            Toast.makeText(activity_application.this, "User Created Succesfully, please verify your email", Toast.LENGTH_SHORT).show();
////                                            goToLogin.setVisibility(View.VISIBLE);
//
//                                        }
//                                        else {
//                                            Toast.makeText(activity_application.this, "Please verify your Email", Toast.LENGTH_SHORT).show();
//                                        }
                                    }
                                    else {
                                        Toast.makeText(activity_application.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

//                            Toast.makeText(activity_application.this, "User Created", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            Toast.makeText(activity_application.this, "Error!"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    public void openLoginPage(View v) {
        Toast.makeText(this, "Opening Login Page", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,
                activity_login.class);
        startActivity(intent);
    }


}