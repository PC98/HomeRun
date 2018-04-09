package com.example.android.homerun.controllers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.android.homerun.R;
import com.example.android.homerun.model.AccountType;
import com.example.android.homerun.model.FirebaseConstants;
import com.example.android.homerun.model.User;
import com.example.android.homerun.model.UtilityMethods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserRegisterTask mRegisterTask = null;

    // UI references.
    private AutoCompleteTextView mUsernameView;
    private AutoCompleteTextView mNameView;
    private EditText mPasswordView;
    private Spinner mAccountView;
    private View mProgressView;
    private View mRegisterFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Set up the login form.
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.regpage_username);
        mNameView = (AutoCompleteTextView) findViewById(R.id.regpage_name);
        // TODO populateAutoComplete()

        mPasswordView = (EditText) findViewById(R.id.regpage_password);
        mAccountView = (Spinner) findViewById(R.id.user_admin_spinner);

        Button cancelButton = (Button) findViewById(R.id.regpage_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        Button registerButton = (Button) findViewById(R.id.regpage_register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegistration();

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, AccountType.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAccountView.setAdapter(adapter);

        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);
    }

    private void attemptRegistration() {
        if (mRegisterTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mNameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String name = mNameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!UtilityMethods.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!UtilityMethods.isEmailValid(username)) {
            if (UtilityMethods.isUsernameValid(username)) {
                username += FirebaseConstants.EMAIL_DOMAIN;
            } else {
                mUsernameView.setError(getString(R.string.error_invalid_username));
                focusView = mUsernameView;
                cancel = true;
            }
        }

        // Check for valid name.
        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        } else if (!UtilityMethods.isNameValid(name)) {
            mNameView.setError(getString(R.string.error_invalid_name));
            focusView = mNameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mRegisterTask = new RegistrationActivity.UserRegisterTask(new User(name, username, password, (AccountType)mAccountView.getSelectedItem()));
            mRegisterTask.execute((Void) null);
        }
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

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private User user;

        UserRegisterTask(User user) {
            this.user = user;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.getUsername(), user.getPassword())
                    .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mRegisterTask = null;
                            showProgress(false);

                            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(RegistrationActivity.this);

                            if (task.isSuccessful()) {
                                // Alert user and go to sign-in page
                                // Reset values
                                mNameView.setText("");
                                mUsernameView.setText("");
                                mPasswordView.setText("");
                                mAccountView.setSelection(0);

                                dlgAlert.setMessage("You have successfully registered. You will be logged in now.");
                                dlgAlert.setTitle("Registration Successful");
                                dlgAlert.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        });

                                user.setId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                FirebaseDatabase.getInstance().getReference()
                                        .child(FirebaseConstants.DATABASE_USERS)
                                        .child(user.getId())
                                        .setValue(user);
                            } else {
                                // Alert user
                                Log.w("Firebase", "Registration failed." + task.getException().getMessage());
                                dlgAlert.setMessage("Something went wrong. Please try again.");
                                dlgAlert.setTitle("Registration Failed");
                                dlgAlert.setPositiveButton("OK", null);
                            }

                            dlgAlert.create().show();
                        }
                    });

            // TODO: ensure this user doesn't exist already
            return false;
        }

        @Override
        protected void onCancelled() {
            mRegisterTask = null;
            showProgress(false);
        }
    }
}
