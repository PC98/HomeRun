package com.example.android.homerun.controllers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.app.AlertDialog;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.homerun.R;
import com.example.android.homerun.model.FirebaseConstants;
import com.example.android.homerun.model.User;
import com.example.android.homerun.model.UtilityMethods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends AppCompatActivity {

    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    public static User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mUsernameView = findViewById(R.id.username);
        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if ((id == EditorInfo.IME_ACTION_DONE) || (id == EditorInfo.IME_NULL)) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mUsernameSignInButton = findViewById(R.id.username_sign_in_button);
        mUsernameSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();

                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));

                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        Button cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mUsernameView.setText("");
                mPasswordView.setText("");
                mPasswordView.clearFocus();

                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.putExtra("userId", firebaseUser.getUid());
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mUsernameView.setText("");
        mPasswordView.setText("");
        mPasswordView.clearFocus();

        showProgress(false);
    }

    private void presentAlert(CharSequence title, CharSequence message) {
        showProgress(false);

        AlertDialog.Builder alert  = new AlertDialog.Builder(LoginActivity.this);
        alert.setMessage(message);
        alert.setTitle(title);
        alert.setPositiveButton("OK", null);
        alert.create().show();
    }

    private void attemptLogin() {
        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
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

        if (focusView != null) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            firebaseLoginUser(username, password);
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

    private void firebaseLoginUser(final String username, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(LoginActivity.this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success!
                                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                    assert firebaseUser != null;

                                    Query userQuery = FirebaseDatabase.getInstance()
                                            .getReference(FirebaseConstants.DATABASE_USERS)
                                            .child(firebaseUser.getUid());

                                    ValueEventListener userQueryEventListener = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            currentUser = dataSnapshot.getValue(User.class);

                                            assert currentUser != null;
                                            if (currentUser.isLockedOut()) {
                                                FirebaseAuth.getInstance().signOut();
                                                presentAlert("Account Locked",
                                                        "You have been temporarily locket " +
                                                                "out. Try again later.");
                                            } else {
                                                LoginActivity.this.startActivity(new Intent(
                                                        LoginActivity.this, DashboardActivity.class));
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {}
                                    };

                                    userQuery.addListenerForSingleValueEvent(
                                            userQueryEventListener);
                                } else {
                                    Query userQuery = FirebaseDatabase.getInstance()
                                            .getReference(FirebaseConstants.DATABASE_USERS)
                                            .orderByChild(FirebaseConstants.DATABASE_USERNAME)
                                            .equalTo(username);

                                    ValueEventListener userQueryEventListener = new
                                            ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.getValue() == null) {
                                                presentAlert("Invalid Credentials",
                                                        "Wrong Password or Username.");
                                                return;
                                            }

                                            for (DataSnapshot item: dataSnapshot.getChildren()) {
                                                currentUser = item.getValue(User.class);
                                            }

                                            assert currentUser != null;

                                            if (!currentUser.isLockedOut()) {
                                                currentUser.handleIncorrectLoginAttempt();
                                            }

                                            if (currentUser.isLockedOut()) {
                                                presentAlert("Account Locked",
                                                        "You have been temporarily locket " +
                                                                "out. Try again later.");
                                            } else {
                                                presentAlert("Invalid Credentials",
                                                        "Wrong Password or Username." +
                                                                (currentUser.getIncorrectLoginAttempts() == 2 ?
                                                                "You will be locked out after one more incorrect attempt." : ""));
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {}
                                    };

                                    userQuery.addListenerForSingleValueEvent(userQueryEventListener);
                                }

                                mUsernameView.setText("");
                                mPasswordView.setText("");
                                mUsernameView.requestFocus();
                            }
                        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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

