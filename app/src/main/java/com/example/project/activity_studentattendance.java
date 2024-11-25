package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Collection;

public class activity_studentattendance extends AppCompatActivity {
    private int mYear, mMonth, mDay, mHour, mMinute;
    FirebaseUser user;
    ImageButton img1;
    ImageButton img2;
String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentattendance);
         user = FirebaseAuth.getInstance().getCurrentUser();
            img1=(ImageButton) findViewById(R.id.imageButton1);
        img2=(ImageButton)findViewById(R.id.imageButton2);


    }

    public void onCal1(View v) {


        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {



                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {


                        date=monthOfYear+"-"+dayOfMonth+"-"+year;

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();


    }
    public void fetchData(View  v){
        if(date ==null){
            Toast.makeText(this,"Please Select a Date",Toast.LENGTH_SHORT).show();
            return;
        }
        DatabaseReference mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("attendance").child(date);
        mFirebaseDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                boolean present=false;
                for(DataSnapshot data: task.getResult().getChildren()) {
                    if(data.child("userID").getValue().toString().equals(user.getUid().toString())&&(boolean)data.child("present").getValue()){
                        present=true;

                        img1.setImageResource(R.drawable.tick);
                        img2.setImageResource(R.drawable.studentpresent);
                        break;
                    }
                }
                if(!present){
                    img1.setImageResource(R.drawable.cross);
                    img2.setImageResource(R.drawable.studentabsent);

                }
                img1.setVisibility(View.VISIBLE);
                img2.setVisibility(View.VISIBLE);
            }
        });
//        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                boolean present=false;
//                for(DataSnapshot data: snapshot.getChildren()) {
//                    if(data.child("userID").getValue().toString().equals(user.getUid().toString())&&(boolean)data.child("present").getValue()){
//                        present=true;
//
//                        img1.setImageResource(R.drawable.tick);
//                        img2.setImageResource(R.drawable.studentpresent);
//                        break;
//                    }
//                }
//                if(!present){
//                    img1.setImageResource(R.drawable.cross);
//                    img2.setImageResource(R.drawable.studentabsent);
//
//                }
//                img1.setVisibility(View.VISIBLE);
//                img2.setVisibility(View.VISIBLE);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


    }
}