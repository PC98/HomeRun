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
import com.google.firebase.database.ChildEventListener;
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
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * The main activity for the app that displays a list of shelters, supports filtering the list and
 * allows the user to navigate to other parts of the app.
 */
public class DashboardActivity extends AppCompatActivity {

    private View mProgressView;
    private ListView mListView;
    private Spinner mFilterCategories;
    private View mView;
    private User currentUser;
    public static Map<String, Shelter> shelterMap;
    public static ShelterAdapter shelterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        setTitle("Shelters");
        String currentUserId = (String) getIntent().getSerializableExtra("userId");
        mProgressView = findViewById(R.id.dashboard_progress);
        mListView = findViewById(R.id.shelter_list);
        mFilterCategories = findViewById(R.id.filter_category_spinner);
        mView = findViewById(R.id.filter_layout);
        EditText mEditTextView = findViewById(R.id.filter_string);

        final Toast mToastToShow = Toast.makeText(getApplicationContext(),
                "Login successful. Fetching Data.", Toast.LENGTH_LONG);
        mToastToShow.show();
        showProgress(true);

        if (currentUserId != null) {
            Query userQuery = FirebaseDatabase.getInstance()
                    .getReference(FirebaseConstants.DATABASE_USERS).child(currentUserId);
            ValueEventListener userQueryEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    currentUser = dataSnapshot.getValue(User.class);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            userQuery.addListenerForSingleValueEvent(userQueryEventListener);
        } else {
            currentUser = LoginActivity.currentUser;
        }

        DatabaseReference shelterRef = FirebaseDatabase.getInstance()
                .getReference(FirebaseConstants.DATABASE_SHELTERS);
        ValueEventListener shelterQueryEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                shelterMap = new HashMap<>();
                ArrayList<Shelter> shelterList;

                if(!dataSnapshot.exists()) {
                    InputStream inputStream = getResources().openRawResource(R.raw.shelter);
                    UtilityMethods.createShelterDatabase(inputStream, shelterMap);
                } else {
                    for (DataSnapshot shelterDataSnapshot : dataSnapshot.getChildren()) {
                        Shelter shelter = shelterDataSnapshot.getValue(Shelter.class);
                        assert shelter != null;
                        shelterMap.put(shelter.getId(), shelter);
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
        shelterRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                Shelter newShelter = dataSnapshot.getValue(Shelter.class);
                assert newShelter != null;
                Shelter oldShelter = shelterMap.get(newShelter.getId());

                oldShelter.setCurrentFamilyCapacity(newShelter.getCurrentFamilyCapacity());
                oldShelter.setCurrentIndividualCapacity(newShelter.getCurrentIndividualCapacity());

                shelterAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        // Add Text Change Listener to EditText
        mEditTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                shelterAdapter.setSearchCategory((FilterCategories)
                        mFilterCategories.getSelectedItem());
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

        SpinnerAdapter adapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, FilterCategories.values());
        mFilterCategories.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);

        dlgAlert.setMessage("Are you sure you want to logout?");
        dlgAlert.setTitle("Logout Confirmation");
        dlgAlert.setNegativeButton("LOGOUT",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        finish();
                    }
                });
        dlgAlert.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custom_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.user_action) {
            if(currentUser == null) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

                dlgAlert.setTitle("Hi!");
                dlgAlert.setNegativeButton("LOGOUT",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();
                                finish();
                            }
                        });
                dlgAlert.setMessage("We are still fetching your details. " +
                        "You may choose to logout.");
                dlgAlert.create().show();

            } else {
                presentUserProfile();
            }
        } else if (id == R.id.map_action) {
            if (shelterMap == null) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

                dlgAlert.setTitle("Hi!");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setMessage("We are still fetching shelter details.");
                dlgAlert.create().show();
            } else {
                startActivity(new Intent(this, MapsActivity.class));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void presentUserProfile() {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        final Shelter claimedShelter = (currentUser.getClaimedShelterId() == null) ? null :
                shelterMap.get(currentUser.getClaimedShelterId());
        String message;

        dlgAlert.setTitle(String.format("Hi %s!", currentUser.getName()));

        if (claimedShelter == null) {
            message = "You currently hold no spots.";
        } else {
            String[] spots = currentUser.getClaimedSpots().split("/");
            message = String.format("You currently hold %s %s spots at %s",
                    spots[1], spots[0], claimedShelter.getName());
        }

        dlgAlert.setNeutralButton("VACATE SPOTS",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] spots = currentUser.getClaimedSpots().split("/");
                        currentUser.firebaseSetClaimedShelterId(null);
                        currentUser.firebaseSetClaimedSpots(null);

                        if("family".equalsIgnoreCase(spots[0])) {
                            assert claimedShelter != null;
                            claimedShelter.firebaseSetCurrentFamilyCapacity(
                                    claimedShelter.getCurrentFamilyCapacity() +
                                    Integer.parseInt(spots[1]));
                        } else {
                            assert claimedShelter != null;
                            claimedShelter.firebaseSetCurrentIndividualCapacity(
                                    claimedShelter.getCurrentIndividualCapacity() +
                                            Integer.parseInt(spots[1]));
                        }

                        final Toast vacateSuccess = Toast.makeText(getApplicationContext(),
                                "You have successful vacated your spot(s).",
                                Toast.LENGTH_LONG);
                        vacateSuccess.show();
                    }
                });

        dlgAlert.setNegativeButton("LOGOUT",
                new DialogInterface.OnClickListener() {
                    @Override
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
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
    }
}