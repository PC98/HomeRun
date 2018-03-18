package com.example.android.homerun.controllers;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.homerun.R;
import com.example.android.homerun.model.Shelter;
import com.example.android.homerun.model.User;


public class ShelterDetailActivity extends AppCompatActivity {
    Shelter current;
    int shelterCapacity;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_detail);

        current = (Shelter) getIntent().getSerializableExtra("ShelterData");
        currentUser = DashboardActivity.currentUser;

        setTitle(current.getName());

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

        try {
            shelterCapacity =  Integer.parseInt(current.getCapacity());
        } catch(Exception e) {
            // TODO: Normalize Capacity Information
            // So some capacity data is not parsable to an integer
            // Also there are different capacities for adults and kids?? it seems unnecessary complex to
            // deal with reservations based on type..
            // this is just to make the app not crash now, obv we need to figure out
            // what to do here
            shelterCapacity = 500;
        }
        Button reserveButton = findViewById(R.id.shelter_detail_view_reserve);
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userShelter = currentUser.getShelterId();
                // Can't book more than one shelter
                if (userShelter != null) {
                    android.app.AlertDialog.Builder rebookError  = new android.app.AlertDialog.Builder(ShelterDetailActivity.this);
                    rebookError.setMessage("You have already reserved a shelter!");
                    rebookError.setTitle("Shelter Allowance Exceeded");
                    rebookError.setPositiveButton("OK", null);
                    rebookError.create().show();
                    return;
                }
                AlertDialog.Builder box = new AlertDialog.Builder(ShelterDetailActivity.this);
                box.setTitle("How many spots? [Capacity = " + shelterCapacity + "]");
                String[] types = {"One Bed", "Two Beds", "Three Beds", "Four Beds"};
                box.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int spotsClaimed = which + 1;
                        if (spotsClaimed > shelterCapacity) {
                            // Exceeding Shelter Capacity
                            android.app.AlertDialog.Builder errorCap = new android.app.AlertDialog.Builder(ShelterDetailActivity.this);
                            errorCap.setTitle("Shelter Capacity Exceeded");
                            errorCap.setMessage("Please Try Again!");
                            errorCap.setPositiveButton("OK", null);
                            errorCap.create().show();

                        } else {
                            // Assign Shelter to User (at least locally, for now)
                            currentUser.setShelterId(current.getId());
                            android.app.AlertDialog.Builder success  = new android.app.AlertDialog.Builder(ShelterDetailActivity.this);
                            success.setMessage("You have successfully reserved a spot!");
                            success.setTitle("Success!");
                            success.setPositiveButton("OK", null);
                            success.create().show();
                            // TODO: Update Shelter Capacity
                            // TODO: Add Cancelling Functionality
                            // Since we have to support cancelling reservations too,
                            // we need to store the reservation information somewhere.
                            // Seems like we can store this in the User class, i.e.
                            // for each User we can store a Shelter (at-most one)
                            // and make sure they never over-reserve.
                            // Also, we should have a cancel reservation button too
                            // which should function iff that shelter was associated with
                            // that user..
                            dialog.dismiss();
                        }
                    }
                });
                box.show();
            }
        });
    }
}
