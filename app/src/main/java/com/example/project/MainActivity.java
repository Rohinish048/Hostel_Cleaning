package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void openLoginPage(View v){
        Toast.makeText(this, "Opening Login Page", Toast.LENGTH_SHORT).show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            if(user.getUid().toString().equals("6chKDM5zOObZHk3x5pY0ORJynTv2")){
                Intent intent = new Intent(this,
                        activity_wardendash.class);
                startActivity(intent);

            }
            else {
                Intent intent = new Intent(this,
                        activity_studentdash.class);
                startActivity(intent);
            }
        }
        else {
            Intent intent = new Intent(this,
                    activity_login.class);
            startActivity(intent);
        }
    }
    public void openKnowmorePage(View v){
        Toast.makeText(this, "Opening Know More Page", Toast.LENGTH_SHORT).show();
        Intent intent1 = new Intent(this,
                activity_knowmore.class);
        startActivity(intent1);
    }
}