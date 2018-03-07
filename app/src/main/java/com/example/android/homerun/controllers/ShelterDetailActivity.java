package com.example.android.homerun.controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.homerun.R;
import com.example.android.homerun.model.Shelter;


public class ShelterDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_detail);

        Shelter current = (Shelter) getIntent().getSerializableExtra("ShelterData");

        setTitle(current.getName());

        String name = current.getName();
        TextView shelter_name_widget = findViewById(R.id.shelter_detail_view_name);
        shelter_name_widget.setText(name);

        String capacity = current.getCapacity();
        TextView shelter_capacity_widget = findViewById(R.id.shelter_detail_view_cap);
        shelter_capacity_widget.setText("Capacity: " + capacity);

        String restrictions = current.getRestrictions();
        TextView shelter_restrictions_widget = findViewById(R.id.shelter_detail_view_restrictions);
        shelter_restrictions_widget.setText("Restricted to: " + restrictions);

        String address = current.getAddress();
        TextView shelter_location_widget = findViewById(R.id.shelter_detail_view_location);
        shelter_location_widget.setText("Location: " + address);

        String phone_number = current.getPhoneNumber();
        TextView shelter_phone_widget = findViewById(R.id.shelter_detail_view_phone);
        shelter_phone_widget.setText("Phone Number: " + phone_number);

        String notes = current.getSpecialNotes();
        TextView shelter_notes_widget = findViewById(R.id.shelter_detail_view_notes);
        shelter_notes_widget.setText("Special Notes: " + notes);

        Button reserveButton = findViewById(R.id.shelter_detail_view_reserve);
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
