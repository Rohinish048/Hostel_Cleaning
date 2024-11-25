package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class activity_studentrequest extends AppCompatActivity {
    ExpandableListView expandableListView;
    BaseExpandableListAdapter expandableListAdapter;
    ArrayList<String> expandableListTitle;
    HashMap<String,  ArrayList<UserInfo>> expandableListDetail;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String userID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentrequest);
        FirebaseUser sUser = FirebaseAuth.getInstance().getCurrentUser();
         userID = sUser.getUid();
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("request");
        extractdata();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle,  expandableListDetail,true);
        expandableListView.setAdapter(expandableListAdapter);



    }

    public void extractdata(){
        expandableListTitle=new ArrayList<>();
        expandableListDetail=new HashMap<>();

        myRef.addValueEventListener(new ValueEventListener() {
            int counter = 0;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                expandableListTitle.clear();
                expandableListDetail.clear();
                for(DataSnapshot data: snapshot.getChildren()){
                    if(data.child("id").getValue().toString().equals(userID)) {
                        ArrayList<UserInfo> childItem =new ArrayList<>();
                        UserInfo x = new UserInfo(data.child("name").getValue().toString(), data.child("sub").getValue().toString(), data.child("id").getValue().toString(), data.child("con").getValue().toString());
                        expandableListTitle.add(x.getSub());

                        if(expandableListDetail.containsKey(x.getSub())){
                            childItem=expandableListDetail.get(x.getSub());
                            childItem.add(x);

                            expandableListDetail.put(x.getSub(),childItem);

                        }
                        else{
                            childItem.add(x);
                            expandableListDetail.put(x.getSub(),childItem);
                        }

                    }

                }
                                expandableListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public void onCLickAdd(View v) {
        LayoutInflater li = LayoutInflater.from(activity_studentrequest.this);
        View promptsView = li.inflate(R.layout.customview, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                activity_studentrequest.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText sub = (EditText) promptsView
                .findViewById(R.id.rsub);
        final EditText con = (EditText) promptsView
                .findViewById(R.id.rcon);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("SUBMIT",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text


                                if (sub.getText().toString().length() <= 0) {
                                    Toast.makeText(activity_studentrequest.this, "Subject Can't Be empty", Toast.LENGTH_SHORT);
                                    return;
                                }
                                if (con.getText().toString().length() <= 0) {
                                    Toast.makeText(activity_studentrequest.this, "Content Can't Be empty", Toast.LENGTH_SHORT);
                                    return;
                                }
                                DatabaseReference mFirebaseDatabase1 = FirebaseDatabase.getInstance().getReference("userID").child(userID);
                                mFirebaseDatabase1.child("First name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        DatabaseReference mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("request");
                                        HashMap<String,String> mp=new HashMap<>();
                                        mp.put("name",task.getResult().getValue().toString());
                                        mp.put("id",userID);

                                        mp.put("sub",sub.getText().toString());
                                        mp.put("con",con.getText().toString());



                                        mFirebaseDatabase.push().setValue(mp);



                                    }
                                });


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