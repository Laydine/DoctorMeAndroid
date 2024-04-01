package com.example.doctorme.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.doctorme.Constants;
import com.example.doctorme.EmergencyInformationActivity;
import com.example.doctorme.R;
import com.example.doctorme.SharedPrefManager;
import com.example.doctorme.models.EmergencyInformationModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FirstEmergencyFragment extends Fragment {
    MaterialTextView edBloodGroup,edAllergies,edInfo;
    Context context;
    View view;
    String bloodGroup,allergies,info;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view= inflater.inflate(R.layout.fragment_first_emergency, container, false);

        context=view.getContext();
        edBloodGroup=view.findViewById(R.id.bloodGroup);
        edAllergies=view.findViewById(R.id.allegies);
        edInfo=view.findViewById(R.id.medicalInfo);

        getDetails(SharedPrefManager.getInstance(view.getContext()).getUserId());

        view.findViewById(R.id.fab).setOnClickListener(v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            //title
            alertDialogBuilder.setTitle("Edit Information");
            //message

            View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_emergency_infomation, null);

            TextInputEditText edBloodGroup = dialogView.findViewById(R.id.edBloodGroup);
            TextInputEditText edAllergies = dialogView.findViewById(R.id.edAllergies);
            TextInputEditText edMedical = dialogView.findViewById(R.id.edMedical);

            edBloodGroup.setText(bloodGroup);
            edAllergies.setText(allergies);
            edMedical.setText(info);

            alertDialogBuilder
                    .setView(dialogView)
                    .setCancelable(true)
                    .setPositiveButton("OK", (dialog, which) -> {
                        String bloodGroup = edBloodGroup.getText().toString().trim();
                        String allergies = edAllergies.getText().toString().trim();
                        String medical = edMedical.getText().toString().trim();
                        String id = Constants.db().push().getKey();

                        if (bloodGroup.isEmpty() || allergies.isEmpty() || medical.isEmpty()) {
                            Toast.makeText(context, "One of the fields is empty", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        EmergencyInformationModel model = new EmergencyInformationModel(
                                id,
                                SharedPrefManager.getInstance(context).getUserId(),
                                bloodGroup,
                                allergies,
                                medical,
                                Constants.getDate()
                        );

                        Constants.db().child("emergency_information").child(
                                        SharedPrefManager.getInstance(context).getUserId()
                                ).setValue(model)
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

    private void getDetails(String userId) {
        ProgressDialog progressDialog=new ProgressDialog(view.getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        Constants.db().child("emergency_information").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.dismiss();
                if(snapshot.exists()){
                    EmergencyInformationModel model=snapshot.getValue(EmergencyInformationModel.class);
                    edBloodGroup.setText(model.getBloodGroup());
                    edAllergies.setText(model.getAllergies());
                    edInfo.setText(model.getInfo());

                    bloodGroup=model.getBloodGroup();
                    allergies=model.getAllergies();
                    info=model.getInfo();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(getView().getContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}