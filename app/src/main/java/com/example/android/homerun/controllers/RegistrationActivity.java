package com.example.android.homerun.controllers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.SpinnerAdapter;

import com.example.android.homerun.R;
import com.example.android.homerun.model.AccountType;
import com.example.android.homerun.model.FirebaseConstants;
import com.example.android.homerun.model.User;
import com.example.android.homerun.model.UtilityMethods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A registration screen that lets a user register to use the app.
 */
public class RegistrationActivity extends AppCompatActivity {

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
        mUsernameView = findViewById(R.id.register_username);
        mNameView = findViewById(R.id.register_name);
        mPasswordView = findViewById(R.id.register_password);
        mAccountView = findViewById(R.id.user_admin_spinner);

        Button cancelButton = findViewById(R.id.register_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegistration();

                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        SpinnerAdapter adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, AccountType.values());
        mAccountView.setAdapter(adapter);

        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);
    }

    private void attemptRegistration() {

        // Reset errors.
        mUsernameView.setError(null);
        mNameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String name = mNameView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (UtilityMethods.isUsernameValid(username)) {
            if (UtilityMethods.isEmailValid(username)) {
                throw new AssertionError();
            }
            username += FirebaseConstants.EMAIL_DOMAIN;
        }

        @Nullable View focusView;

        focusView = isPasswordValid(password) ? null : mPasswordView;
        focusView = isUsernameValid(username) ? focusView : mUsernameView;
        focusView = isNameValid(name) ? focusView : mNameView;

        if (focusView != null) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            User user = new User(name, username, password,
                    (AccountType)mAccountView.getSelectedItem());
            firebaseRegisterUser(user);
        }
    }

    private boolean isPasswordValid(CharSequence password){
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            return false;
        } else if (!UtilityMethods.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            return false;
        }
        return true;
    }

    private boolean isUsernameValid(CharSequence username) {
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            return false;
        } else if (!UtilityMethods.isEmailValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            return false;
        }
        return true;
    }

    private boolean isNameValid(CharSequence name){
        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            return false;
        } else if (!UtilityMethods.isNameValid(name)) {
            mNameView.setError(getString(R.string.error_invalid_name));
            return false;
        }
        return true;
    }

    private void firebaseRegisterUser(final User user) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.getUsername(),
                user.getPassword()).addOnCompleteListener(RegistrationActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        showProgress(false);

                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(
                                RegistrationActivity.this);

                        if (task.isSuccessful()) {
                            // Alert user and go to sign-in page
                            // Reset values
                            mNameView.setText("");
                            mUsernameView.setText("");
                            mPasswordView.setText("");
                            mAccountView.setSelection(0);

                            dlgAlert.setMessage("You have successfully registered. You will" +
                                    " be logged in now.");
                            dlgAlert.setTitle("Registration Successful");
                            dlgAlert.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    });
                            FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();
                            assert authUser!= null;
                            user.setId(authUser.getUid());
                            FirebaseDatabase.getInstance()
                                    .getReference(FirebaseConstants.DATABASE_USERS)
                                    .child(user.getId())
                                    .setValue(user);
                        } else {
                            // Alert user
                            Log.w("Firebase", "Registration failed." +
                                    task.getException());
                            dlgAlert.setMessage("Something went wrong. Please try again.");
                            dlgAlert.setTitle("Registration Failed");
                            dlgAlert.setPositiveButton("OK", null);
                        }

                        dlgAlert.create().show();
                    }
                });
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
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
    }
}
