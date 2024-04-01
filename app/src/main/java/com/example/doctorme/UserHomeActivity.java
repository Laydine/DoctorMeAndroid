package com.example.doctorme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.doctorme.models.RequestModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

public class UserHomeActivity extends AppCompatActivity {
    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v->finish());

        findViewById(R.id.emergencyInformation).setOnClickListener(v->{
            startActivity(new Intent(UserHomeActivity.this, EmergencyInformationActivity.class));
        });
        findViewById(R.id.ambulance).setOnClickListener(v->{
            requestForAmbulance(SharedPrefManager.getInstance(UserHomeActivity.this).getUserId());
        });
        findViewById(R.id.chat).setOnClickListener(v->{
            startActivity(new Intent(UserHomeActivity.this, ChatActivity.class));
        });
    }

    private void requestForAmbulance(String userId) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UserHomeActivity.this);
        //title
        alertDialogBuilder.setTitle("Ambulance Request");
        //message
        View view= LayoutInflater.from(this).inflate(R.layout.dialog_request_ambulance,null);
        TextInputEditText edMessage=view.findViewById(R.id.description);

        alertDialogBuilder.setView(view);

        alertDialogBuilder.setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       String message=edMessage.getText().toString().trim();
                       String id=Constants.db().push().getKey();
                       if(message.isEmpty()){
                           Toast.makeText(UserHomeActivity.this, "Kindly include a description of your condition", Toast.LENGTH_SHORT).show();
                       }else{
                           RequestModel model=new RequestModel(id,userId,message,Constants.getDate(),"",false);
                           Constants.db().child("request").child(id).setValue(model)
                                   .addOnSuccessListener(unused -> {
                                       Toast.makeText(UserHomeActivity.this, "Request sent successful", Toast.LENGTH_SHORT).show();
                                   })
                                   .addOnFailureListener(e -> {
                                       Toast.makeText(UserHomeActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                   });
                       }
                       dialog.cancel();
                    }
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mn_logout) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UserHomeActivity.this);
            //title
            alertDialogBuilder.setTitle("Confirm");
            //message
            alertDialogBuilder.setMessage("Are you sure you want to logout?");

            alertDialogBuilder.setCancelable(true)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPrefManager.getInstance(UserHomeActivity.this).logOut();
                            Toast.makeText(UserHomeActivity.this, "Logged Out Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UserHomeActivity.this, MainActivity.class));
                            finish();
                        }
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
            return true;
        } else {
            return false;
        }
    }
}