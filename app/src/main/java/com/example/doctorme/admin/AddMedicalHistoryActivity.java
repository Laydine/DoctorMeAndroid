package com.example.doctorme.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.doctorme.Constants;
import com.example.doctorme.R;
import com.example.doctorme.models.MedicalHistoryModel;
import com.example.doctorme.models.UserModel;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddMedicalHistoryActivity extends AppCompatActivity {

    MaterialAutoCompleteTextView dpPatients;

    private String selectedPatient;
    TextInputEditText edIllness, edSymptoms, edMedicines;
    ArrayList<String> patientsList = new ArrayList<>();
    String[] patients;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medical_history);

        edIllness = findViewById(R.id.illness);
        edSymptoms = findViewById(R.id.symptoms);
        edMedicines = findViewById(R.id.medicines);
        dpPatients = findViewById(R.id.dpPatients);

        findViewById(R.id.add).setOnClickListener(v -> addHistory());

    }

    @Override
    protected void onResume() {
        super.onResume();
        getPatients();
    }

    private void addHistory() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        String illness = edIllness.getText().toString().trim();
        String symptoms = edSymptoms.getText().toString().trim();
        String medicines = edMedicines.getText().toString().trim();
        String id = Constants.db().push().getKey();

        if (illness.isEmpty() || symptoms.isEmpty() || medicines.isEmpty()) {
            progressDialog.dismiss();
            Toast.makeText(this, "One of the fields is empty", Toast.LENGTH_SHORT).show();
        } else {
            Query query = Constants.db().child("users").orderByChild("name").equalTo(selectedPatient);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<UserModel> patients = new ArrayList<>();
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            UserModel model = dataSnapshot.getValue(UserModel.class);
                            patients.add(model);
                        }

                        MedicalHistoryModel model = new MedicalHistoryModel(
                                id, patients.get(0).getId(), illness, symptoms, medicines, Constants.getDate());

                        Constants.db().child("histories").child(id).setValue(model)
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(AddMedicalHistoryActivity.this, "Added Successful", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(AddMedicalHistoryActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(AddMedicalHistoryActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }


    private void getPatients() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Patients...");
        progressDialog.show();
        Constants.db().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.dismiss();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        UserModel model = dataSnapshot.getValue(UserModel.class);
                        if (!model.getRole().equals("admin"))
                            patientsList.add(model.getName());
                    }
                    patients = patientsList.toArray(new String[0]); // Convert ArrayList to array
                    dpPatients.setSimpleItems(patients);
                    dpPatients.setText(patients[0], false);
                    selectedPatient = patients[0];
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(AddMedicalHistoryActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
        dpPatients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPatient = patients[position];
            }
        });
    }

}