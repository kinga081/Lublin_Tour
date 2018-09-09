package com.example.kinga.trasapolublinie;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;
    private SignInButton sib;
    public static final int SIGN_IN_CODE = 777;
    public static final String TAG = "GoogleActivity";
    private static final int RECORD_REQUEST_CODE =101 ;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener muAuthListener;
    private SensorManager manager;


    //MapsActivity
    //MainActivity
    //activity_main

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        int permissionCheck = ContextCompat.checkSelfPermission(this,//pozwolenie
                android.Manifest.permission.ACCESS_FINE_LOCATION);;
        int permissionCheck2 = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck3 = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED || permissionCheck2 !=
                PackageManager.PERMISSION_GRANTED || permissionCheck3 !=
                PackageManager.PERMISSION_GRANTED  ) {
            Log.i(TAG, "Permission to record denied");
            makeRequest();
        }

        mAuth = FirebaseAuth.getInstance();
        muAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    Log.d("Auth",user.getEmail());
                    //Log.d("Auth",user.getUid());
                    //Toast.makeText(getApplicationContext(),"Zalogowano",Toast.LENGTH_LONG).show();
                }
            }
        };


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        sib = (SignInButton)findViewById(R.id.sign);
        sib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,SIGN_IN_CODE);
            }
        });



    }

    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(muAuthListener);
    }

    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(muAuthListener);
    }
/*
    public void Anonimowo(View view){
        mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Log.d("Auth", String.valueOf(task.getException()));
                }
            }
        });
    }*/

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_IN_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignResult(result);
        }
    }

    private void handleSignResult(GoogleSignInResult result) {
        if (result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            firebaseAuthGoogle(account);
            goMainScreen();
    }
        }

    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                RECORD_REQUEST_CODE);
    }




    private void firebaseAuthGoogle(GoogleSignInAccount account) {
        Log.d(TAG,"firebaseAuthGoogle:"+account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG,"singinInWithCredential:onComplete:"+task.isSuccessful());
                        if(!task.isSuccessful()){
                            Log.w(TAG,"singinInWithCredential:"+task.getException());
                            Toast.makeText(getApplicationContext(),"Błąd autoryzacji", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void goMainScreen() {
        Intent intent = new Intent(this, NavigationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
