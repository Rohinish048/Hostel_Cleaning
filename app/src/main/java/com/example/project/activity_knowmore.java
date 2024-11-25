package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class activity_knowmore extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowmore);
    }
    public void do_register(View v) {
        Toast.makeText(this, "Apply", Toast.LENGTH_SHORT).show();
        Intent intent1 = new Intent(this,
                activity_application.class);
        startActivity(intent1);
    }
}