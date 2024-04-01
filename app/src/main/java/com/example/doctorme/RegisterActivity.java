package com.example.doctorme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.doctorme.models.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {
    MaterialAutoCompleteTextView dpSchools;

    //Holds the value of the selected category
    private String selectedSchool;
    TextInputEditText edRegNo, edName, edAddress, edPassword, edConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        edRegNo = findViewById(R.id.edRegNo);
        edName = findViewById(R.id.edName);
        edAddress = findViewById(R.id.edAddress);
        edPassword = findViewById(R.id.edPassword);
        edConfirmPassword = findViewById(R.id.edConfirmPassword);
        dpSchools = findViewById(R.id.dpSchools);

        findViewById(R.id.login).setOnClickListener(v -> finish());

        findViewById(R.id.register).setOnClickListener(v -> register());

        String[] schools = {"SCIT", "SOHES", "SESS", "SOBE", "SOSCI"};
        dpSchools.setSimpleItems(schools);
        // Initialising the selected category value
        dpSchools.setText(schools[0], false);
        selectedSchool = schools[0];

        dpSchools.setOnItemClickListener((parent, view, position, id) -> {
            selectedSchool = schools[position];
        });
    }

    private void register() {
        String regNo = edRegNo.getText().toString().trim();
        String name = edName.getText().toString().trim();
        String address = edAddress.getText().toString().trim();
        String password = edPassword.getText().toString().trim();
        String confirmPassword = edConfirmPassword.getText().toString().trim();
        String id = Constants.db().push().getKey();


        if (regNo.isEmpty() || name.isEmpty() || address.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "One of the fields is Empty", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "The passwords don't match", Toast.LENGTH_SHORT).show();
        } else {
            UserModel model = new UserModel(id, regNo, name, address, selectedSchool, password, Constants.getDate(), "student");

            Constants.db().child("users").child(id).setValue(model)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });

        }

    }
}