package com.example.doctorme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.doctorme.fragments.FirstEmergencyFragment;
import com.example.doctorme.fragments.SecondEmergencyFragment;
import com.example.doctorme.models.EmergencyInformationModel;
import com.example.doctorme.models.UserModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class EmergencyInformationActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    MaterialToolbar toolbar;
    MaterialTextView txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_information);

        viewPager = findViewById(R.id.viewPager);
        toolbar = findViewById(R.id.toolbar);
        txtName = findViewById(R.id.txtName);
        tabLayout = findViewById(R.id.tablayout);

        toolbar.setNavigationOnClickListener(v -> finish());

        viewPagerAdapter = new ViewPagerAdapter(
                getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        // It is used to join TabLayout with ViewPager.
        tabLayout.setupWithViewPager(viewPager);

        getUSerDetails(SharedPrefManager.getInstance(this).getUserId());

    }

    private void getUSerDetails(String userId) {
        Constants.db().child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    UserModel model=snapshot.getValue(UserModel.class);
                    Toast.makeText(EmergencyInformationActivity.this, "Name: "+model.getName(), Toast.LENGTH_SHORT).show();
                    txtName.setText(model.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EmergencyInformationActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position == 0)
                fragment = new FirstEmergencyFragment();
            else if (position == 1)
                fragment = new SecondEmergencyFragment();

            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = null;
            if (position == 0)
                title = "Info";
            else if (position == 1)
                title = "Contacts";
            return title;
        }
    }


}