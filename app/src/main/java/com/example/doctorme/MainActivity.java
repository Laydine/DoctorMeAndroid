package com.example.doctorme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.doctorme.admin.AdminHomeActivity;
import com.example.doctorme.models.UserModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    TextInputEditText edRegNo, edPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edRegNo = findViewById(R.id.edRegNo);
        edPassword = findViewById(R.id.edPassword);

        //Escape to the Dashboard Activity if the user is already loggedin
        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            if(SharedPrefManager.getInstance(this).getRole().equals("admin")){
                startActivity(new Intent(this,AdminHomeActivity.class));
                finish();
            }else{
                startActivity(new Intent(this,UserHomeActivity.class));
                finish();
            }
        }

        findViewById(R.id.register).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        });


        findViewById(R.id.login).setOnClickListener(v -> login());
    }

    private void login() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String regNo = edRegNo.getText().toString().trim();
        String password = edPassword.getText().toString().trim();

        if (regNo.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "One of the fields is empty", Toast.LENGTH_SHORT).show();
        } else {
            Query query = Constants.db().child("users").orderByChild("regNo").equalTo(regNo);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            UserModel userModel = dataSnapshot.getValue(UserModel.class);

                            if (password.equals(userModel.getPassword())) {
                                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                                // Now get the user id and the role and store them using the Shared Preference Manager
                                String getId = dataSnapshot.child("id").getValue(String.class);
                                String getRole = dataSnapshot.child("role").getValue(String.class);

                                SharedPrefManager.getInstance(MainActivity.this).userLogin(getId, getRole);
                                if (getRole.equals("admin")) {
                                    startActivity(new Intent(MainActivity.this, AdminHomeActivity.class));
                                    finish();
                                } else {
                                    startActivity(new Intent(MainActivity.this, UserHomeActivity.class));
                                    finish();
                                }
                                progressDialog.dismiss();
                            }
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "The user doesn't exist. Kindly register first", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}