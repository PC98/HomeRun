package com.example.android.homerun.controllers;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.AlertDialog;

import com.example.android.homerun.R;
import com.example.android.homerun.model.Shelter;
import com.example.android.homerun.model.User;

/**
 * A screen that displays more details about a Shelter
 */
public class ShelterDetailActivity extends AppCompatActivity {
    private Shelter current;
    private User currentUser;
    private int shelterType;
    private Integer shelterFamilyCapacity;
    private Integer shelterIndividualCapacity;
    private TextView shelter_capacity_widget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_detail);

        current = DashboardActivity.shelterMap.get(getIntent().getStringExtra("shelterId"));
        currentUser = LoginActivity.currentUser;

        setTitle(current.getName());

        shelter_capacity_widget = findViewById(R.id.shelter_detail_view_cap);
        shelter_capacity_widget.setText(getString(R.string.capacity, current.getCapacityString()));

        String restrictions = current.getRestrictions();
        TextView shelter_restrictions_widget = findViewById(R.id.shelter_detail_view_restrictions);
        shelter_restrictions_widget.setText(getString(R.string.restricted_to, restrictions));

        String address = current.getAddress();
        TextView shelter_location_widget = findViewById(R.id.shelter_detail_view_location);
        shelter_location_widget.setText(getString(R.string.loc, address));

        String phone_number = current.getPhoneNumber();
        TextView shelter_phone_widget = findViewById(R.id.shelter_detail_view_phone);
        shelter_phone_widget.setText(getString(R.string.phone, phone_number));

        String notes = current.getSpecialNotes();
        TextView shelter_notes_widget = findViewById(R.id.shelter_detail_view_notes);
        shelter_notes_widget.setText(getString(R.string.specNot, notes));

        Button reserveButton = findViewById(R.id.shelter_detail_view_reserve);
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Can't book more than one shelter
                if (currentUser.getClaimedShelterId() != null) {
                    presentAlert("Shelter Allowance Exceeded",
                            "You have already reserved a shelter!");
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
                    types = new String[]{"One Bed - Individual", "Two Beds - Individual",
                            "Three Beds - Family", "Four Beds - Family"};
                    shelterType = 0;
                } else if (isIndividual) {
                    types = new String[]{"One Bed - Individual", "Two Beds - Individual",
                            "Three Beds - Individual", "Four Beds - Individual"};
                    shelterType = 1;
                } else if (isFamily) {
                    types = new String[]{"One Bed - Family", "Two Beds - Family",
                            "Three Beds - Family", "Four Beds - Family"};
                    shelterType = 2;
                } else {
                    presentAlert("Shelter Unsupported!",
                            "You cannot reserve spots at this shelter!");
                    return;
                }

                box.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int spotsClaimed = which + 1;
                        int shelterCapacity;

                        if ((shelterType == 1) || ((shelterType == 0) && (spotsClaimed <= 2))) {
                            shelterCapacity = shelterIndividualCapacity;
                        } else {
                            shelterCapacity = shelterFamilyCapacity;
                        }

                        if (spotsClaimed > shelterCapacity) {
                            presentAlert("Shelter Capacity Exceeded!", "Please try again later.");

                        } else {
                            String spotsData;
                            if ((shelterType == 1) || ((shelterType == 0) && (spotsClaimed <= 2))) {
                                spotsData = "individual/";
                                current.firebaseSetCurrentIndividualCapacity(current
                                        .getCurrentIndividualCapacity() - spotsClaimed);
                            } else {
                                spotsData = "family/";
                                current.firebaseSetCurrentFamilyCapacity(current
                                        .getCurrentFamilyCapacity() - spotsClaimed);
                            }

                            spotsData += spotsClaimed;

                            currentUser.firebaseSetClaimedShelterId(current.getId());
                            currentUser.firebaseSetClaimedSpots(spotsData);

                            shelter_capacity_widget.setText(getString(R.string.capacity,
                                    current.getCapacityString()));

                            presentAlert("Success!",
                                    "You have successfully reserved your spot(s).");
                        }
                        dialog.dismiss();
                    }
                });
                box.show();
            }
        });
    }

    private void presentAlert(CharSequence title, CharSequence message) {
        AlertDialog.Builder alert  = new AlertDialog.Builder(ShelterDetailActivity.this);
        alert.setMessage(message);
        alert.setTitle(title);
        alert.setPositiveButton("OK", null);
        alert.create().show();
    }
}
