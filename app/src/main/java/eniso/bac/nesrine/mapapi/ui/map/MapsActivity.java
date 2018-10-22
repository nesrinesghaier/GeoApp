package eniso.bac.nesrine.mapapi.ui.map;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import eniso.bac.nesrine.mapapi.R;
import eniso.bac.nesrine.mapapi.model.User;
import eniso.bac.nesrine.mapapi.ui.login.LoginActivity;
import es.dmoral.toasty.Toasty;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, Runnable {
    Menu menuLogOut;
    FirebaseAuth mAuth;
    String userEmail, userName;
    LatLng myLocation, otherLocation;
    private GoogleMap mMap;
    DatabaseReference myRef;
    FirebaseDatabase database;
    double latitude, longitude;
    LocationManager locationManager;
    MarkerOptions otherMarker, myMarker = null;
    FloatingActionButton boyB, girlB;
    Criteria criteria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        userEmail = getIntent().getStringExtra("email");
        if (userEmail != null && userEmail.equals("nesrinesghaier10@gmail.com"))
            userName = "Nesrine";
        else if (userEmail != null && userEmail.equals("nasreddine.bacali95@gmail.com"))
            userName = "Nasreddine";
        myRef = database.getReference("users");
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        //DB.addUser(new User(userName,userEmail,new LatLng(0,0)));
        setContentView(R.layout.activity_maps);
        boyB = (FloatingActionButton) findViewById(R.id.boy);
        girlB = (FloatingActionButton) findViewById(R.id.girl);
        menuLogOut = (Menu) findViewById(R.id.menuLogOut);
        mAuth = FirebaseAuth.getInstance();
        final MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boyB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraUpdate cameraUpdate = null;
                if (userName.equals("Nasreddine")) {
                    if (myMarker != null) {
                        cameraUpdate = CameraUpdateFactory.newLatLngZoom(myMarker.getPosition(), mMap.getCameraPosition().zoom); //15
                    }
                } else if (userName.equals("Nesrine")) {

                    if (otherMarker != null) {
                        cameraUpdate = CameraUpdateFactory.newLatLngZoom(otherMarker.getPosition(), mMap.getCameraPosition().zoom); //15
                    }
                }
                if (cameraUpdate != null) {
                    mMap.animateCamera(cameraUpdate);
                }
            }
        });
        girlB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraUpdate cameraUpdate = null;
                if (userName.equals("Nesrine")) {
                    if (myMarker != null) {
                        cameraUpdate = CameraUpdateFactory.newLatLngZoom(myMarker.getPosition(), mMap.getCameraPosition().zoom); //15
                    }
                } else if (userName.equals("Nasreddine")) {
                    if (otherMarker != null) {
                        cameraUpdate = CameraUpdateFactory.newLatLngZoom(otherMarker.getPosition(), mMap.getCameraPosition().zoom); //15
                    }
                }
                if (cameraUpdate != null) {
                    mMap.animateCamera(cameraUpdate);
                }
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        if (locationManager.isProviderEnabled(locationManager.getBestProvider(criteria, true))) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, new LocationListener() {
                @Override
                public void onLocationChanged(final Location location) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    myLocation = new LatLng(latitude, longitude);
                    if (userName.equals("Nasreddine")) {
                        User user = new User(userName, userEmail, myLocation);
                        //DB.updateLocation(userEmail,myLocation.latitude,myLocation.longitude);
                        Map<String, Object> nasMap = new HashMap<>();
                        nasMap.put("Nasreddine", user);
                        myRef.updateChildren(nasMap);
                        myMarker = new MarkerOptions().position(myLocation).title("Me");
                        myMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.boy));
                    } else if (userName.equals("Nesrine")) {
                        User user = new User(userName, userEmail, myLocation);
                        //DB.updateLocation(userEmail,myLocation.latitude,myLocation.longitude);
                        Map<String, Object> nasMap = new HashMap<>();
                        nasMap.put("Nesrine", user);
                        myRef.updateChildren(nasMap);
                        myMarker = new MarkerOptions().position(myLocation).title("Me");
                        myMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.girl));
                    }
                    mMap.clear();
                    if (otherMarker != null) mMap.addMarker(otherMarker);
                    if (myMarker != null) mMap.addMarker(myMarker);


                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {
                }

                @Override
                public void onProviderEnabled(String s) {
                }

                @Override
                public void onProviderDisabled(String s) {
                }
            });
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User us1 = null;
                    if (dataSnapshot != null) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (userName.equals("Nesrine")) {
                                Map<String, Double> get = dataSnapshot.child("Nasreddine").child("coordinates").getValue(new GenericTypeIndicator<Map<String, Double>>() {
                                });
                                otherLocation = new LatLng(get.get("latitude"), get.get("longitude"));
                                otherMarker = new MarkerOptions().position(otherLocation).title("Love");
                                otherMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.boy));
                            } else if (userName.equals("Nasreddine")) {
                                Map<String, Double> get = dataSnapshot.child("Nesrine").child("coordinates").getValue(new GenericTypeIndicator<Map<String, Double>>() {
                                });
                                otherLocation = new LatLng(get.get("latitude"), get.get("longitude"));
                                otherMarker = new MarkerOptions().position(otherLocation).title("Love");
                                otherMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.girl));
                            }
                            mMap.clear();
                            if (otherMarker != null) mMap.addMarker(otherMarker);
                            if (myMarker != null) mMap.addMarker(myMarker);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, new LocationListener() {
                @Override
                public void onLocationChanged(final Location location) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    myLocation = new LatLng(latitude, longitude);
                    if (userName.equals("Nasreddine")) {
                        User user = new User(userName, userEmail, myLocation);
                        Map<String, Object> nasMap = new HashMap<>();
                        nasMap.put("Nasreddine", user);
                        myRef.updateChildren(nasMap);
                        myMarker = new MarkerOptions().position(myLocation).title("Me");
                        myMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.boy));
                    } else if (userName.equals("Nesrine")) {
                        User user = new User(userName, userEmail, myLocation);
                        Map<String, Object> nasMap = new HashMap<>();
                        nasMap.put("Nesrine", user);
                        myRef.updateChildren(nasMap);
                        myMarker = new MarkerOptions().position(myLocation).title("Me");
                        myMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.girl));
                    }
                    mMap.clear();
                    if (otherMarker != null) mMap.addMarker(otherMarker);
                    if (myMarker != null) mMap.addMarker(myMarker);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(myLocation, mMap.getCameraPosition().zoom); //15
                    mMap.animateCamera(cameraUpdate);
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {
                }

                @Override
                public void onProviderEnabled(String s) {
                }

                @Override
                public void onProviderDisabled(String s) {
                    Toast.makeText(getApplicationContext(), "Provider disabled", Toast.LENGTH_LONG).show();
                }
            });
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User us1 = null;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (userName.equals("Nesrine")) {
                            Map<String, Double> get = dataSnapshot.child("Nasreddine").child("coordinates").getValue(new GenericTypeIndicator<Map<String, Double>>() {
                            });
                            otherLocation = new LatLng(get.get("latitude"), get.get("longitude"));
                            otherMarker = new MarkerOptions().position(otherLocation).title("Love");
                            otherMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.boy));
                        } else if (userName.equals("Nasreddine")) {
                            Map<String, Double> get = dataSnapshot.child("Nesrine").child("coordinates").getValue(new GenericTypeIndicator<Map<String, Double>>() {
                            });
                            otherLocation = new LatLng(get.get("latitude"), get.get("longitude"));
                            otherMarker = new MarkerOptions().position(otherLocation).title("Love");
                            otherMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.girl));
                        }
                        mMap.clear();
                        if (otherMarker != null) mMap.addMarker(otherMarker);
                        if (myMarker != null) mMap.addMarker(myMarker);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(
                    "Your GPS module is disabled. Would you like to enable it ?")
                    .setCancelable(false)
                    .setPositiveButton("Yeah",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    Intent callGPSSettingIntent = new Intent(
                                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    MapsActivity.this.startActivity(callGPSSettingIntent);
                                    startActivityForResult(callGPSSettingIntent, 1);
                                    dialog.dismiss();
                                }
                            })
                    .setNegativeButton("Nope",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                    Toasty.warning(getApplicationContext(), "You should enable your GPS \n Or you won't know my location \n                      " + ("\ud83d\ude08"), Toast.LENGTH_LONG).show();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menuLogOut) {
        getMenuInflater().inflate(R.menu.main2, menuLogOut);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuLogOut) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            Toast.makeText(getApplicationContext(), "log out done", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(35.833645, 10.589511), 15); //15
        mMap.animateCamera(cameraUpdate);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            finish();
            startActivity(getIntent());
        }

    }

    @Override
    public void run() {

    }
}

