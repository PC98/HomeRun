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
import com.example.android.homerun.model.FirebaseWrapper;
import com.example.android.homerun.model.Shelter;
import com.example.android.homerun.model.UtilityMethods;
import com.example.android.homerun.view.ShelterAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    private View mProgressView;
    private ListView mListView;
    private EditText mEditTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        setTitle("Shelters");

        mProgressView = findViewById(R.id.dashboard_progress);
        mListView = (ListView) findViewById(R.id.shelter_list);
        mEditTextView = (EditText) findViewById(R.id.shelter_search);

        final Toast mToastToShow = Toast.makeText(getApplicationContext(),"Login successful. Fetching Data", Toast.LENGTH_LONG);
        mToastToShow.show();

        DatabaseReference shelterRef = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseWrapper.DATABASE_SHELTERS);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    InputStream inputStream = getResources().openRawResource(R.raw.shelter);
                    UtilityMethods.createShelterDatabase(inputStream);
                }
                ArrayList<Shelter> shelterList = new ArrayList<>();
                for (DataSnapshot shelterDataSnapshot: dataSnapshot.getChildren()) {
                    shelterList.add(shelterDataSnapshot.getValue(Shelter.class));
                }

                final ArrayAdapter<Shelter> shelterAdapter = new ShelterAdapter(DashboardActivity.this, shelterList);

                assert mListView != null;
                mListView.setAdapter(shelterAdapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View v, int position,
                                            long arg3)
                    {
                        Shelter shelter = (Shelter) adapter.getItemAtPosition(position);
                        Intent intent = new Intent(DashboardActivity.this, ShelterDetailActivity.class);
                        intent.putExtra("ShelterData", shelter);
                        startActivity(intent);
                    }
                });

                // Add Text Change Listener to EditText
                mEditTextView.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // Call back the Adapter with current character to Filter
                        shelterAdapter.getFilter().filter(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                mToastToShow.cancel();
                showProgress(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase", "loadPost:onCancelled", databaseError.toException());
            }
        };
        shelterRef.addListenerForSingleValueEvent(eventListener);


    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);

        dlgAlert.setMessage("Are you sure you want to logout?");
        dlgAlert.setTitle("Logout Confirmation");
        dlgAlert.setPositiveButton("OK",
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

        if (id == R.id.logout_action) {
            FirebaseAuth.getInstance().signOut();
            finish();
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

            mEditTextView.setVisibility(show ? View.GONE : View.VISIBLE);
            mEditTextView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mEditTextView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mEditTextView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}