package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

public class activity_wardenattendance extends AppCompatActivity {
    private int mYear, mMonth, mDay, mHour, mMinute;

    RecyclerView recyclerView;
    EditText editTextSearch;
    ArrayList<UserData> names;
FirebaseDatabase database;
    DatabaseReference myRef;
    HashMap<String,UserData>m;
String date;
    AttendanceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wardenattendance);
        recyclerView=(RecyclerView) findViewById(R.id.attendace);
        editTextSearch = (EditText) findViewById(R.id.searchbar);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("userID");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getData();
        adapter = new AttendanceAdapter(names);

        recyclerView.setAdapter(adapter);

        enableSwipeToDeleteAndUndo();







        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ArrayList<UserData> d=new ArrayList<>();
                String f=s.toString();
                for(UserData data:names){
                    if(((data.getFirst().toLowerCase().contains(f.toLowerCase()))||(data.getLast().toLowerCase().contains(f.toLowerCase())))&&(data.isPresent())){
                        d.add(data);
                    }
                }
                adapter.filterList(d);

            }
        });



    }
    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                UserData x=m.get(names.get(position).getId());
                x.setPresent(false);
                m.put(names.get(position).getId(),x);
                adapter.removeItem(position);






            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }
    public void getData(){
        names=new ArrayList<>();
        m=new HashMap<>();
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    names.clear();
                    m.clear();
                    int counter=1;
                    for(DataSnapshot data: snapshot.getChildren()) {
                            UserData x=new UserData(data.child("First name").getValue().toString(),data.child("Last name").getValue().toString(),(String.valueOf(counter)),data.getKey());
                            names.add(x);
                            counter++;
                            m.put(x.getId(),x);
                    }
                    adapter.filterList(names);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

    public void onCal(View v) {


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
    public void onSubmit(View v){
        if(date ==null){
            Toast.makeText(this,"Please Select a Date",Toast.LENGTH_SHORT).show();
            return;
        }
        DatabaseReference mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("attendance").child(date);
        Collection<UserData> values = m.values();

        // Creating an ArrayList of values
        ArrayList<UserData> listOfValues
                = new ArrayList<>(values);
        mFirebaseDatabase.setValue(listOfValues);
        Toast.makeText(this,"Attendance Updated",Toast.LENGTH_SHORT).show();

    }
}