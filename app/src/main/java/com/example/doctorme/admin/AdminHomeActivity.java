package com.example.doctorme.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.doctorme.R;

public class AdminHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        findViewById(R.id.medical_history).setOnClickListener(v->{
            startActivity(new Intent(AdminHomeActivity.this, ManageMedicalHistory.class));
        });
    }
}