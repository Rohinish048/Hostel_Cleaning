package com.example.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AttendanceAdapter extends  RecyclerView.Adapter<AttendanceAdapter.ViewHolder>{
    private ArrayList<UserData> names;

    public AttendanceAdapter(ArrayList<UserData> names) {
        this.names = names;
    }

    @NonNull
    @Override
    public AttendanceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceAdapter.ViewHolder holder, int position) {
        holder.sl.setText(this.names.get(position).getId());
        holder.fname.setText(this.names.get(position).getFirst());
        holder.lname.setText(this.names.get(position).getLast());

    }

    @Override
    public int getItemCount() {
        return names.size();
    }
    public void filterList(ArrayList<UserData> filterdNames) {
        this.names = filterdNames;
        notifyDataSetChanged();
    }
    public void removeItem(int position) {
        this.names.remove(position);
        notifyItemRemoved(position);
    }
    public ArrayList<UserData> getData() {
        return this.names;
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {
        private TextView sl;
        private TextView fname;
        private TextView lname;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sl=itemView.findViewById(R.id.slno);
            fname=itemView.findViewById(R.id.fname);
            lname=itemView.findViewById(R.id.lname);
        }
    }
}
