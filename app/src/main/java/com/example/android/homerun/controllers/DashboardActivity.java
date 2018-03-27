package com.example.android.homerun.controllers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.android.homerun.R;
import com.example.android.homerun.model.FirebaseConstants;
import com.example.android.homerun.model.FilterCategories;
import com.example.android.homerun.model.Shelter;
import com.example.android.homerun.model.User;
import com.example.android.homerun.model.UtilityMethods;
import com.example.android.homerun.view.ShelterAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class DashboardActivity extends AppCompatActivity {

    private View mProgressView;
    private ListView mListView;
    private EditText mEditTextView;
    private Spinner mFilterCategories;
    private View mView;
    public static User currentUser;
    public static HashMap<String, Shelter> shelterMap;
    public static ShelterAdapter shelterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        setTitle("Shelters");
        String currentUserId = (String) getIntent().getSerializableExtra("userId");
        mProgressView = findViewById(R.id.dashboard_progress);
        mListView = (ListView) findViewById(R.id.shelter_list);
        mEditTextView = (EditText) findViewById(R.id.filter_string);
        mFilterCategories = (Spinner) findViewById(R.id.filter_category_spinner);
        mView = findViewById(R.id.filter_layout);

        final Toast mToastToShow = Toast.makeText(getApplicationContext(), "Login successful. Fetching Data.", Toast.LENGTH_LONG);
        mToastToShow.show();
        showProgress(true);

        Query userQuery = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseConstants.DATABASE_USERS).child(currentUserId);
        ValueEventListener userQueryEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DashboardActivity.currentUser = (User) dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        userQuery.addListenerForSingleValueEvent(userQueryEventListener);

        DatabaseReference shelterRef = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseConstants.DATABASE_SHELTERS);
        ValueEventListener shelterQueryEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DashboardActivity.shelterMap = new HashMap<>();
                ArrayList<Shelter> shelterList;

                if(!dataSnapshot.exists()) {
                    InputStream inputStream = getResources().openRawResource(R.raw.shelter);
                    UtilityMethods.createShelterDatabase(inputStream, DashboardActivity.shelterMap);
                } else {
                    for (DataSnapshot shelterDataSnapshot : dataSnapshot.getChildren()) {
                        Shelter shelter = shelterDataSnapshot.getValue(Shelter.class);
                        DashboardActivity.shelterMap.put(shelter.getId(), shelter);
                    }
                }
                shelterList = new ArrayList<>(shelterMap.values());

                shelterList.sort(new Comparator<Shelter>() {
                    @Override
                    public int compare(Shelter shelter, Shelter t1) {
                        return shelter.getName().compareTo(t1.getName());
                    }
                });

                shelterAdapter = new ShelterAdapter(DashboardActivity.this, shelterList);

                assert mListView != null;
                mListView.setAdapter(shelterAdapter);

                mToastToShow.cancel();
                showProgress(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "loadPost:onCancelled", databaseError.toException());
            }
        };
        shelterRef.addListenerForSingleValueEvent(shelterQueryEventListener);

        // Add Text Change Listener to EditText
        mEditTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                shelterAdapter.setSearchCategory((FilterCategories) mFilterCategories.getSelectedItem());
                shelterAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                Shelter shelter = (Shelter) adapter.getItemAtPosition(position);
                Intent intent = new Intent(DashboardActivity.this, ShelterDetailActivity.class);
                intent.putExtra("shelterId", shelter.getId());
                startActivity(intent);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, FilterCategories.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFilterCategories.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ShelterDetailActivity.reservationMade) {
            shelterAdapter.notifyDataSetChanged();
            ShelterDetailActivity.reservationMade = false;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);

        dlgAlert.setMessage("Are you sure you want to logout?");
        dlgAlert.setTitle("Logout Confirmation");
        dlgAlert.setNegativeButton("LOGOUT",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        finish();
                    }
                });
        dlgAlert.create().show();
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custom_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.user_action) {
            if(DashboardActivity.currentUser == null) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

                dlgAlert.setTitle("Hi!");
                dlgAlert.setNegativeButton("LOGOUT",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();
                                finish();
                            }
                        });
                dlgAlert.setMessage("We are still fetching your details. You may choose to logout.");
                dlgAlert.create().show();

            } else {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                final Shelter claimedShelter =
                        currentUser.getClaimedShelterId() == null ? null : shelterMap.get(currentUser.getClaimedShelterId());
                String message;

                dlgAlert.setTitle(String.format("Hi %s!", DashboardActivity.currentUser.getName()));

                if (claimedShelter == null) {
                    message = "You currently hold no spots.";
                } else {
                    String[] spots = DashboardActivity.currentUser.getClaimedSpots().split("/");
                    message = String.format("You currently hold %s %s spots at %s",
                            spots[1], spots[0], claimedShelter.getName());
                }

                dlgAlert.setNeutralButton("VACATE SPOTS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String[] spots = DashboardActivity.currentUser.getClaimedSpots().split("/");
                                UtilityMethods.updateUser(DashboardActivity.currentUser, null, null);

                                if(spots[0].equalsIgnoreCase("family")) {
                                    UtilityMethods.updateShelter(claimedShelter,
                                            claimedShelter.getCurrentFamilyCapacity() + Integer.parseInt(spots[1]),
                                            null);
                                } else {
                                    UtilityMethods.updateShelter(claimedShelter, null,
                                            claimedShelter.getCurrentIndividualCapacity() + Integer.parseInt(spots[1]));
                                }

                                final Toast vacateSuccess = Toast.makeText(getApplicationContext(), "You have successful vacated your spot(s).", Toast.LENGTH_LONG);
                                vacateSuccess.show();

                                shelterAdapter.notifyDataSetChanged();
                            }
                        });

                dlgAlert.setNegativeButton("LOGOUT",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();
                                finish();
                            }
                        });

                dlgAlert.setMessage(message);
                AlertDialog dlg = dlgAlert.create();
                dlg.show();
                dlg.getButton(AlertDialog.BUTTON_NEUTRAL).setEnabled(
                        claimedShelter != null);
            }
        } else if (id == R.id.map_action) {
            if (DashboardActivity.shelterMap == null) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

                dlgAlert.setTitle("Hi!");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setMessage("We are still fetching shelter details.");
                dlgAlert.create().show();
            }
            startActivity(new Intent(this, MapsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mListView.setVisibility(show ? View.GONE : View.VISIBLE);
            mListView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mListView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mView.setVisibility(show ? View.GONE : View.VISIBLE);
            mView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mListView.setVisibility(show ? View.GONE : View.VISIBLE);
            mView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}