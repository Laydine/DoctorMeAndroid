package com.example.doctorme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorme.Constants;
import com.example.doctorme.R;
import com.example.doctorme.models.EmergencyCallsModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.MyViewHolder> {
    final Context context;
    final ArrayList<EmergencyCallsModel>list;

    public CallAdapter(Context context, ArrayList<EmergencyCallsModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CallAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.single_call_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CallAdapter.MyViewHolder holder, int position) {
        EmergencyCallsModel currentItem=list.get(position);
        holder.name.setText(currentItem.getName());
        holder.phone.setText(currentItem.getContact());
        holder.delete.setOnClickListener(v->{
            Constants.db().child("emergency_calls").child(currentItem.getId()).removeValue()
                    .addOnSuccessListener(unused -> Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show());
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView phone,name;
        MaterialButton delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            phone=itemView.findViewById(R.id.phone);
            name=itemView.findViewById(R.id.name);
            delete=itemView.findViewById(R.id.delete);
        }
    }
}
