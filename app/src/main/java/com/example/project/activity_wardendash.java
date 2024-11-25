package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_wardendash extends AppCompatActivity {

    TextView sname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wardendash);

        sname=findViewById(R.id.wardenName);

        FirebaseUser sUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID = sUser.getUid();
        DatabaseReference mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("userID").child(userID);
        mFirebaseDatabase.child("First name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                sname.setText(String.valueOf(task.getResult().getValue()));
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;

    }

    public void logout(MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "You've successfully logged out", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(),activity_login.class));
        finish();
    }

    public void updateNoticeBoard(View v){
        Toast.makeText(this, "Opening Update Notice Board", Toast.LENGTH_SHORT).show();
        Intent intent1 = new Intent(this,
                activity_updatenotice.class);
        startActivity(intent1);
    }

    public void takeattendance(View v){
        Toast.makeText(this, "Opening Attendance", Toast.LENGTH_SHORT).show();
        Intent intent2 = new Intent(this,
                activity_wardenattendance.class);
        startActivity(intent2);
    }

    public void openWardentaff(View v){
        Toast.makeText(this, "Opening Staff Status", Toast.LENGTH_SHORT).show();
        Intent intent3 = new Intent(this,
                activity_wardenstaff.class);
        startActivity(intent3);
    }

    public void openWardenRequests(View v){
        Toast.makeText(this, "Opening students requests", Toast.LENGTH_SHORT).show();
        Intent intent4 = new Intent(this,
                activity_wardenrequests.class);
        startActivity(intent4);
    }

    public void openWardenProfile(View v){
        Toast.makeText(this, "Opening profile", Toast.LENGTH_SHORT).show();
        Intent intent5 = new Intent(this,
                activity_wardenprofile.class);
        startActivity(intent5);
    }

}