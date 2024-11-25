package com.example.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

//import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<String> expandableListTitle;
    private HashMap<String, ArrayList<UserInfo>> expandableListDetail;
    private boolean student;
    private boolean notice;

    public CustomExpandableListAdapter(Context context, ArrayList<String> expandableListTitle,
                                       HashMap<String, ArrayList<UserInfo>> expandableListDetail,boolean student) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
        this.student=student;
        this.notice=false;
    }

    public CustomExpandableListAdapter(Context context, ArrayList<String> expandableListTitle, HashMap<String, ArrayList<UserInfo>> expandableListDetail, boolean student, boolean notice) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
        this.student = student;
        this.notice = notice;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final UserInfo expandedListText = (UserInfo) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            if(!this.notice) {
                LayoutInflater layoutInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.list_item, null);
            }
            else{
                LayoutInflater layoutInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.notice_image_view, null);
            }
        }
        if(!notice) {
            TextView expandedListTextView = (TextView) convertView
                    .findViewById(R.id.expandedListItem);
            if (student) {
                TextView x = (TextView) convertView
                        .findViewById(R.id.name);
                expandedListTextView.setVisibility(View.GONE);
                x.setVisibility(View.GONE);
            } else
                expandedListTextView.setText(expandedListText.getName());

            TextView expand = (TextView) convertView.findViewById((R.id.expandedListItem2));
            expand.setText(expandedListText.getCun());
        }
        else{
            ImageView im=(ImageView) convertView.findViewById(R.id.noticeImageView1);
//            Glide.with(context).load().placeholder(R.mipmap.ic_launcher_round).dontAnimate().into(im);;
            Picasso.with(context)
                    .load(expandedListText.getSub()).placeholder(R.mipmap.ic_launcher_round)
                    .into(im);
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }

        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

}