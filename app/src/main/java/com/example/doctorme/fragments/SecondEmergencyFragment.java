package com.example.doctorme.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.doctorme.Constants;
import com.example.doctorme.R;
import com.example.doctorme.SharedPrefManager;
import com.example.doctorme.adapters.CallAdapter;
import com.example.doctorme.models.EmergencyCallsModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SecondEmergencyFragment extends Fragment {
    View view;
    Context context;
    RecyclerView recyclerView;
    CallAdapter adapter;
    ArrayList<EmergencyCallsModel>list=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_second_emergency, container, false);
        context = view.getContext();

        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter=new CallAdapter(context,list);
        recyclerView.setAdapter(adapter);

        getCalls(SharedPrefManager.getInstance(context).getUserId());

        view.findViewById(R.id.fab).setOnClickListener(v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            //title
            alertDialogBuilder.setTitle("Add Contact");
            //message

            View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_emergency_call, null);

            TextInputEditText edName = dialogView.findViewById(R.id.edName);
            TextInputEditText edPhone = dialogView.findViewById(R.id.edPhone);

            alertDialogBuilder
                    .setView(dialogView)
                    .setCancelable(true)
                    .setPositiveButton("OK", (dialog, which) -> {
                        String name = edName.getText().toString().trim();
                        String contact = edPhone.getText().toString().trim();
                        String id = Constants.db().push().getKey();

                        if (name.isEmpty() || contact.isEmpty()) {
                            Toast.makeText(context, "One of the fields is empty", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        EmergencyCallsModel model = new EmergencyCallsModel(
                                id,
                                name,
                                contact,
                                SharedPrefManager.getInstance(context).getUserId(),
                                Constants.getDate()
                        );

                        Constants.db().child("emergency_calls").child(id).setValue(model)
                                .addOnSuccessListener(unused -> {
                                    dialog.cancel();
                                    Toast.makeText(context, "Added Successfully", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    dialog.cancel();
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            // create the alertDialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            //show the dialog
            alertDialog.show();
        });

        return view;
    }

    private void getCalls(String userId) {

      Query query= Constants.db().child("emergency_calls").orderByChild("userId").equalTo(userId);

      query.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
              list.clear();
             if(snapshot.exists()){
                 for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                     EmergencyCallsModel model=dataSnapshot.getValue(EmergencyCallsModel.class);
                     list.add(model);
                     adapter.notifyDataSetChanged();
                 }
             }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {
              Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
          }
      });
    }
}