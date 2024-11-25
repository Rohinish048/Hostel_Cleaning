package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class activity_studentlogin extends AppCompatActivity {

    public void openApplicationPage(View v) {
        Toast.makeText(this, "Opening Login Page", Toast.LENGTH_SHORT).show();
        Intent intent9 = new Intent(this,
                activity_application.class);
        startActivity(intent9);
    }

    EditText mEmail, mPassword;
    Button mLoginBtn;
    TextView mCreateBtn, mForgotPassword;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentlogin);

        mEmail = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mCreateBtn = findViewById(R.id.createText);
        mLoginBtn = findViewById(R.id.login);
        mForgotPassword = findViewById(R.id.forgotpassword);
        fAuth = FirebaseAuth.getInstance();

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();


                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required.");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is required.");
                    return;
                }
                if (password.length() < 6) {
                    mPassword.setError("Password must contain greater than 6 characters.");
                    return;
                }
                //authenticate the user
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            if (fAuth.getCurrentUser().isEmailVerified()){
                                Toast.makeText(activity_studentlogin.this, "Logged In successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), activity_studentdash.class));
                            }
                            else {
                                Toast.makeText(activity_studentlogin.this, "User not verified! please check your mail for verification", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(activity_studentlogin.this, "Error!"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset password?");
                passwordResetDialog.setMessage("Enter your email to receive the email link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //extract the email link and send the email link

                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(activity_studentlogin.this, "Reset link sent to your Email.", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(activity_studentlogin.this, "Error! Link is not sent"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close the dialogue
                    }
                });
                passwordResetDialog.create().show();
            }
        });

    }
//    public void openStudentDashboard(View v){
//        Toast.makeText(this, "Opening Student Dashboard", Toast.LENGTH_SHORT).show();
//        Intent intent2 = new Intent(this,
//                activity_studentdash.class);
//        startActivity(intent2);
//    }


}