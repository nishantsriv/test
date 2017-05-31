package com.self.clothchooser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.self.clothchooser.LoginMethodClass.callbackManager;
import static com.self.clothchooser.LoginMethodClass.firebaseAuthwithgoogle;
import static com.self.clothchooser.LoginMethodClass.googleApiClient;
import static com.self.clothchooser.LoginMethodClass.loginmethod;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private SignInButton signInButton;
    public static LoginButton loginbtn;
    private static int RC_SIGN_IN = 1;
    public static FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authlistener;
    private DatabaseReference databaseReference;
    private ArrayList<String> stringArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        loginmethod(this);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                stringArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String value = snapshot.getValue(String.class);
                    stringArrayList.add(value);
                }
                Log.d("TEST", "" + stringArrayList.size());
                authlistener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            if (stringArrayList.size() != 0) {
                                for (int i = 0; i < stringArrayList.size(); i++) {
                                    if (stringArrayList.get(i).equals(user.getEmail())) {
                                        startActivity(new Intent(LoginActivity.this, WearTodayActivity.class));
                                        finish();
                                    } else {
                                        Intent intent = new Intent(LoginActivity.this, Add_clothActivity.class);
                                        intent.putExtra("userid", user.getEmail());
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            } else {
                                Intent intent = new Intent(LoginActivity.this, Add_clothActivity.class);
                                intent.putExtra("userid", user.getEmail());
                                startActivity(intent);
                                finish();

                            }
                        }
                    }
                };
                auth.addAuthStateListener(authlistener);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void init() {
        auth = FirebaseAuth.getInstance();
        loginbtn = (LoginButton) findViewById(R.id.fbLoginbtn);
        signInButton = (SignInButton) findViewById(R.id.googleSingInbtn);
        signInButton.setOnClickListener(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("exist");
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RC_SIGN_IN) {
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (googleSignInResult.isSuccess()) {
                GoogleSignInAccount account = googleSignInResult.getSignInAccount();
                firebaseAuthwithgoogle(account, this);
            } else {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authlistener);
    }


    @Override
    public void onClick(View v) {
        if (v == signInButton) {
            signIn();
        }
    }
}
