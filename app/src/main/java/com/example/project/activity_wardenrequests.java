package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class activity_wardenrequests extends AppCompatActivity {
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
        setContentView(R.layout.activity_wardenrequests);

        FirebaseUser sUser = FirebaseAuth.getInstance().getCurrentUser();
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListViewWarden);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("request");
        extractdata();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle,  expandableListDetail,false);
        expandableListView.setAdapter(expandableListAdapter);
    }
    public void extractdata(){
        expandableListTitle=new ArrayList<>();
        expandableListDetail=new HashMap<>();

        myRef.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                expandableListTitle.clear();
                expandableListDetail.clear();
                for(DataSnapshot data: snapshot.getChildren()){
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
//                Log.e("TAG", "counter :" + counter);
//                counter++;
                expandableListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}