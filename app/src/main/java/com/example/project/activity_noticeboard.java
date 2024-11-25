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

public class activity_noticeboard extends AppCompatActivity {
    ExpandableListView expandableListView;
    BaseExpandableListAdapter expandableListAdapter;
    ArrayList<String> expandableListTitle;
    HashMap<String,  ArrayList<UserInfo>> expandableListDetail;
    FirebaseDatabase database;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticeboard);


        expandableListView = (ExpandableListView) findViewById(R.id.exviewnotice);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("notice");
        extractdata();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle,  expandableListDetail,false,true);
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

                        ArrayList<UserInfo> childItem =new ArrayList<>();
                        UserInfo x = new UserInfo(data.child("title").getValue().toString(), data.child("url").getValue().toString());
                        expandableListTitle.add(x.getName());

                        if(expandableListDetail.containsKey(x.getName())){
                            childItem=expandableListDetail.get(x.getName());
                            childItem.add(x);

                            expandableListDetail.put(x.getName(),childItem);

                        }
                        else{
                            childItem.add(x);
                            expandableListDetail.put(x.getName(),childItem);
                        }

                    }


                expandableListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

}