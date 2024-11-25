package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class activity_wardenstaff extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<UserData> names;
    FirebaseDatabase database;
    DatabaseReference myRef;
    AttendanceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wardenstaff);
        recyclerView=(RecyclerView) findViewById(R.id.wardenstaff);

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

    public void onCLickAddS(View v) {
        LayoutInflater li = LayoutInflater.from(activity_wardenstaff.this);
        View promptsView = li.inflate(R.layout.add_staff, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                activity_wardenstaff.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText sl = (EditText) promptsView
                .findViewById(R.id.sl);
        final EditText sname = (EditText) promptsView
                .findViewById(R.id.sname);
        final EditText ph = (EditText) promptsView
                .findViewById(R.id.ph);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("SUBMIT",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text


                                if (sl.getText().toString().length() <= 0) {
                                    Toast.makeText(activity_wardenstaff.this, "Serial No. Can't Be empty", Toast.LENGTH_SHORT);
                                    return;
                                }
                                if (sname.getText().toString().length() <= 0) {
                                    Toast.makeText(activity_wardenstaff.this, "Name Can't Be empty", Toast.LENGTH_SHORT);
                                    return;
                                }
                                if (ph.getText().toString().length() <= 0) {
                                    Toast.makeText(activity_wardenstaff.this, "Phone No. Can't Be empty", Toast.LENGTH_SHORT);
                                    return;
                                }
                                DatabaseReference mFirebaseDatabase1 = FirebaseDatabase.getInstance().getReference("staff");

                                HashMap<String,String> mp=new HashMap<>();
                                mp.put("name",sname.getText().toString());
                                mp.put("sl",sl.getText().toString());

                                mp.put("phone",ph.getText().toString());
                                mFirebaseDatabase1.push().setValue(mp);


                            }

                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
}