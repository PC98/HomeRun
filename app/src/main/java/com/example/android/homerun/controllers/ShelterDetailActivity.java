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
import com.example.android.homerun.model.UtilityMethods;


public class ShelterDetailActivity extends AppCompatActivity {
    Shelter current;
    User currentUser;
    int shelterType;
    Integer shelterFamilyCapacity;
    Integer shelterIndividualCapacity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_detail);

        current = (Shelter) getIntent().getSerializableExtra("ShelterData");
        currentUser = DashboardActivity.currentUser;

        setTitle(current.getName());

        String capacity = current.getCapacityString();
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
                Shelter userShelter = currentUser.getClaimedShelter();
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
                box.setTitle("How many spots would you like?");
                String[] types;
                shelterIndividualCapacity = current.getCurrentIndividualCapacity();
                shelterFamilyCapacity = current.getCurrentFamilyCapacity();
                boolean isIndividual = shelterIndividualCapacity != null;
                boolean isFamily =  shelterFamilyCapacity != null;
                if (isIndividual && isFamily) {
                    types = new String[]{"One Bed - Individual", "Two Beds - Individual", "Three Beds - Family", "Four Beds - Family"};
                    shelterType = 0;
                } else if (isIndividual) {
                    types = new String[]{"One Bed - Individual", "Two Beds - Individual", "Three Beds - Individual", "Four Beds - Individual"};
                    shelterType = 1;
                } else if (isFamily) {
                    types = new String[]{"One Bed - Family", "Two Beds - Family", "Three Beds - Family", "Four Beds - Family"};
                    shelterType = 2;
                } else {
                    android.app.AlertDialog.Builder failure  = new android.app.AlertDialog.Builder(ShelterDetailActivity.this);
                    failure.setMessage("You cannot reserve spots at this shelter!");
                    failure.setTitle("Shelter Unsupported!");
                    failure.setPositiveButton("OK", null);
                    failure.create().show();
                    return;
                }
                box.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int spotsClaimed = which + 1;
                        int shelterCapacity = 0;
                        if (shelterType == 0) {
                            if (spotsClaimed >= 3) {
                                shelterCapacity = shelterFamilyCapacity;
                            } else {
                                shelterCapacity = shelterIndividualCapacity;
                            }
                        } else if (shelterType == 1) {
                            shelterCapacity = shelterIndividualCapacity;
                        } else {
                            shelterCapacity = shelterFamilyCapacity;
                        }
                        if (spotsClaimed > shelterCapacity) {
                            // Exceeding Shelter Capacity
                            android.app.AlertDialog.Builder errorCap = new android.app.AlertDialog.Builder(ShelterDetailActivity.this);
                            errorCap.setTitle("Shelter Capacity Exceeded");
                            errorCap.setMessage("Please Try Again!");
                            errorCap.setPositiveButton("OK", null);
                            errorCap.create().show();

                        } else {
                            // Assign Shelter to User (at least locally, for now)
                            String spotsData = "";
                            if ((shelterType == 1) || (shelterType == 0 && spotsClaimed <= 2)) {
                                spotsData += "individual/";
                                UtilityMethods.updateShelter(current, null,
                                        current.getCurrentIndividualCapacity() - spotsClaimed);
                            } else {
                                spotsData += "family/";
                                UtilityMethods.updateShelter(current,
                                        current.getCurrentFamilyCapacity() - spotsClaimed, null);
                            }
                            spotsData += spotsClaimed;
                            UtilityMethods.updateUser(currentUser, current, spotsData);
                            android.app.AlertDialog.Builder success  = new android.app.AlertDialog.Builder(ShelterDetailActivity.this);
                            success.setMessage("You have successfully reserved your spot(s)!");
                            success.setTitle("Success!");
                            success.setPositiveButton("OK", null);
                            success.create().show();
                            dialog.dismiss();
                        }
                    }
                });
                box.show();
            }
        });
    }
}
