package com.example.kinga.trasapolublinie;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.LinkedBlockingDeque;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener,OnMapReadyCallback,GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener {

    private ImageView image;
    private TextView email;
    private TextView name;
    private GoogleApiClient gApi;
    private GoogleMap mMap;
    private FloatingActionButton add;
    private FloatingActionButton delete;
    private LinkedBlockingDeque markerPoints;
    private DatabaseReference mDatabaseReference;
    private Query ref;
    private DatabaseReference mUsers;
    private Marker marker;

    private ChildEventListener mChildEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        add = (FloatingActionButton) findViewById(R.id.add);
        delete = (FloatingActionButton) findViewById(R.id.delete);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NavigationActivity.this, DodawanieActivity.class);
                startActivity(intent);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NavigationActivity.this, UsunActivity.class);
                startActivity(intent);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        //final TextView Name_nav = (TextView) headerView.findViewById(R.id.Name_nav);

        image = (ImageView) headerView.findViewById(R.id.imageView);
        email =(TextView) headerView.findViewById(R.id.email);
        name =(TextView) headerView.findViewById(R.id.name);

        //email.setText("iwef");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gApi = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso )
                .build();


        ChildEventListener mChildEventListener;
    }


    @Override
    protected void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(gApi);
        if(opr.isDone()){
            GoogleSignInResult result = opr.get();
            handleSingnInResult(result);
        }else{
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSingnInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSingnInResult(GoogleSignInResult result) {
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();

            //if(account.getPhotoUrl().isAbsolute()){
            String personPhotoUrl = account.getPhotoUrl().toString();
            //Toast.makeText(getApplicationContext(),personPhotoUrl,Toast.LENGTH_LONG).show();
                Glide.with(getApplicationContext()).load(personPhotoUrl)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }
                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .transition(withCrossFade())
                        .apply(new RequestOptions().transform(new RoundedCorners(100))
                                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE))
                        .into(image);
            //}
            name.setText(account.getDisplayName());
            email.setText(account.getEmail());

            if(account.getEmail().equals("para.dzwig@gmail.com")
                    || account.getEmail().equals("kinga081@gmail.com")){//konto admina
                add.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
            }


            //Glide.with(this).load(account.getPhotoUrl().toString()).into(image);
            //Log.d("MIAPP",account.getPhotoUrl().toString());
        }else {
            goLogInScreen();
        }
    }


    private void goLogInScreen() {
        Intent intent = new Intent(this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        GoogleMapOptions options = new GoogleMapOptions();

        //noinspection SimplifiableIfStatement
        if (id == R.id.domyslny) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            return true;
        }else if(id == R.id.satelita) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            return true;
        }else if(id == R.id.teren) {
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.category_one) {
            dodajMarker("Bezpłatne");
        }else if (id == R.id.category_two) {
            dodajMarker("Zabytki");
        } else if (id == R.id.category_three) {
            dodajMarker("Dla dzieci");

        } else if (id == R.id.category_four) {
            ddd();

        } else if (id == R.id.category_five) {

        }  else if (id == R.id.log_out) {
            Auth.GoogleSignInApi.signOut(gApi).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    if(status.isSuccess()){
                        goLogInScreen();
                    }else{
                        Toast.makeText(getApplicationContext(), "Błąd", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (id == R.id.add) {

            item.setVisible(false);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
       // Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);

        mMap.setOnMarkerClickListener(this);



    }
    public void dodajMarker(String kategoria){

        mUsers= FirebaseDatabase.getInstance().getReference("lokalizacje").child(kategoria);
        mUsers.push().setValue(marker);
        mMap.clear();
        mUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()){
                    Lokalizacje user = s.getValue(Lokalizacje.class);
                    LatLng location=new LatLng(Double.parseDouble(user.getDlugosc()),Double.parseDouble(user.getSzerokosc()));
                    mMap.addMarker(new MarkerOptions()
                            .position(location)
                            .title(user.getNazwa())
                            .snippet(user.getOpis())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                    );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
/*        query.addChildEventListener(new ChildEventListener() {
            // Retrieve new posts as they are added to Firebase
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {

                //Map<String, Object> newPost = (Map<String, Object>) snapshot.getValue();
                for(DataSnapshot data : snapshot.getChildren()){
                    Lokalizacje lokalizacje = data.getValue(Lokalizacje.class);

                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(lokalizacje.getDlugosc()), Double.parseDouble(lokalizacje.getSzerokosc())))
                            .title(lokalizacje.getNazwa())
                            .snippet(lokalizacje.getOpis())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                    );
                }



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
            //... ChildEventListener also defines onChildChanged, onChildRemoved,
            //    onChildMoved and onCanceled, covered in later sections.
        });
*/
}
public void ddd(){
    mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(51.248812, 22.56914340000003))
                .title("Plac Po Farze")
                .snippet("Grodzka 18, 20-400")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
        );

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(51.225193, 22.61422189999996))
                .title("Majdanek")
                .snippet("Majdanek, 20-001")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
        );

    }

    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }



    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Moja lokalizacja", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}