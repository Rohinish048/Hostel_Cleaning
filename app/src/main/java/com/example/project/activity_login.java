package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class activity_login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void openStudentLoginPage(View v){
        Toast.makeText(this, "Opening Student Login Page", Toast.LENGTH_SHORT).show();
        Intent intent1 = new Intent(this,
                activity_studentlogin.class);
        startActivity(intent1);
    }
    public void openWardenLoginPage(View v){
        Toast.makeText(this, "Opening Warden Login Page", Toast.LENGTH_SHORT).show();
        Intent intent2 = new Intent(this,
                activity_wardenlogin.class);
        startActivity(intent2);
    }
}