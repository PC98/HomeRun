package com.example.android.homerun.controllers;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.homerun.R;
import com.example.android.homerun.model.Shelter;


public class ShelterDetailActivity extends AppCompatActivity {
    Shelter current;
    int shelterCapacity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_detail);

        current = (Shelter) getIntent().getSerializableExtra("ShelterData");

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
                AlertDialog.Builder box = new AlertDialog.Builder(ShelterDetailActivity.this);
                box.setTitle("How many spots? [Capacity = " + shelterCapacity + "]");
                String[] types = {"One Bed", "Two Beds", "Three Beds", "Four Beds"};
                box.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int spotsClaimed = which + 1;
                        if (spotsClaimed > shelterCapacity) {
                            // Dialog Box tested locally
                            AlertDialog errorCap = new AlertDialog.Builder(ShelterDetailActivity.this).create();
                            errorCap.setTitle("Shelter Capacity Exceeded");
                            errorCap.setMessage("Please Try Again!");
                            errorCap.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog2, int which2) {
                                            dialog2.dismiss();
                                        }
                                    });
                            errorCap.show();

                        } else {
                            // Successful Reservation
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
