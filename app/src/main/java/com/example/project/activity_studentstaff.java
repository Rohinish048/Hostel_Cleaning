package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class activity_studentstaff extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<UserData> names;
    FirebaseDatabase database;
    DatabaseReference myRef;
    AttendanceAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentstaff);
        recyclerView=(RecyclerView) findViewById(R.id.studentstaff);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("staff");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getData();
        adapter = new AttendanceAdapter(names);

        recyclerView.setAdapter(adapter);
    }
    public void getData(){
        names=new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                names.clear();
                for(DataSnapshot data: snapshot.getChildren()) {
                    UserData x=new UserData(data.child("name").getValue().toString(),data.child("phone").getValue().toString(),data.child("sl").getValue().toString());
                    names.add(x);
                }
                adapter.filterList(names);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}