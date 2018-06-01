package com.example.surya.rtctracker;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog
        ;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//
    Timer autoUpdate;
    public Snackbar snackbar;
    public DrawerLayout drawer;
    public Double currentlattitude, currentlongitude;
    public static String name, lastname, Firstnamee, Lastnamee, phone;
    LocationRequest mlocationRequest;
    Marker marker, markerlong, friendmarker;
    GoogleMap mGoogleMap;
    GoogleApiClient mGoogleApiClient;
    private boolean searrrch = false, Hostingg = false;
    SwitchCompat hosting;
    Location mCurrentLocation;

    JSONObject hosters;

    EditText busno, fromaddress, toaddress;
    /////>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//
    AlertDialog alertDialog, hostdailog;
    AlertDialog.Builder alert, hosteddailog;
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//
    public int range = 0;

    HashMap<String, String> user;
    HashMap<String, String> route,ranging;
    UserSessionManager session;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private TextView Usernamee;
    private String fullname;
    private ArrayList<Hoster> personList;
    private int count = 0;
    public String hostaddress;
    public boolean hoster = false;
    private boolean internetConnected = false;
    String internetfailure = "PLEAE CONEECT TO INTERNET";
    String locationfailure = "PLEASE TURN ON LOCATION";
    ///>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>//
    Internetcheck internet = new Internetcheck();
    boolean gps_enabled = false;
    boolean network_enabled = false;
    private String TAG;
    public static double lattitudes;
    public static double longitudes;

    private RadioGroup radioGroup;
    public boolean mapis = false;
    public boolean verified;
    SaveHostdata saveroute;
    //??????????????????>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
public static final String ROOT_URL =Url.URL;
    public ProgressDialog uploading;
    public int counting=0,countings=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        personList = new ArrayList<Hoster>();
        saveroute=new SaveHostdata(getApplicationContext());

        if (googleServicesAvailable()) {
            initMap();

        }


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        // distance = (DrawerLayout) findViewById(R.id.sliding_layout);
//        ActionBarDrawerToggle togglee = new ActionBarDrawerToggle(
//                                    this, distance, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        distance.addDrawerListener(togglee);
//           togglee.syncState();

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
// TODO: Get info about the selected place.

                Log.i(TAG,  "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        NavigationView mainview = (NavigationView) findViewById(R.id.Mainmenu);
        navigationView.setNavigationItemSelectedListener(this);

        Usernamee =  header.findViewById(R.id.Username);

        session = new UserSessionManager(getApplicationContext());

        if (session.checkLogin())
            finish();
/*

Toast.makeText(getApplicationContext(),
"User Login Status: " + session.isUserLoggedIn(),
Toast.LENGTH_LONG).show();
*/
        user = session.getUserDetails();

        // get name
        name = user.get(UserSessionManager.KEY_NAME);
        lastname = user.get(UserSessionManager.KEY_LASTNAME);
        Usernamee.setText(name + " " + lastname);
        //  locationchecking(MainActivity.this);

        Usernamee.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Clear the User session data
                // and redirect user to LoginActivity
                Intent i = new Intent(MainActivity.this, Accountdetails.class);
                startActivity(i);
//                session.logoutUser();
            }
        });


        hosting = (SwitchCompat) findViewById(R.id.switch1);
      //  hosting.setVisibility(View.INVISIBLE);
        hosting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {

                    locationchecking(MainActivity.this);

                    if (currentlongitude != null && currentlattitude != null) {

                        if (internetConnected) {

                            if (!saveroute.dataissuccess()) {
                                hosting.setChecked(false);
                                Toast.makeText(MainActivity.this, "please enter route details", Toast.LENGTH_SHORT).show();

                            } if(saveroute.dataissuccess()) {
                                hosting.setChecked(true);
                                counting=1;
                                route=saveroute.gethostdetails();
                                hostaddress = "BusNo: " + " " + route.get(SaveHostdata.Bus) + " " +
                                        "From: " + " " + route.get(SaveHostdata.Fromm) + " " + "To: " + route.get(SaveHostdata.to);
                                //Toast.makeText(MainActivity.this, hostaddress, Toast.LENGTH_SHORT).show();
                                Hostingg = true;
                                onResume();
                            }
                        } else {
                            hosting.setChecked(false);
                            SnackbarMessagee(locationfailure);
                        }
                    } else {

                        hosting.setChecked(false);
                        onResume();

                    }

                } else {


                    if (internetConnected) {

                            Hostingg = false;
                            deleteUser();

                    } else
                        hosting.setChecked(true);


                }

            }
        });
        navigationView.getMenu().findItem(R.id.stopnear).setVisible(false);


    }

    private void Range() {
        //Creating a LayoutInflater object for the dialog box
        // verifyOTP=OTP;

        LayoutInflater li = LayoutInflater.from(this);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.distance, null);
        // AppCompatButton buttonConfirm = (AppCompatButton) confirmDialog.findViewById(R.id.buttonConfirm);

        radioGroup = (RadioGroup) confirmDialog.findViewById(R.id.radioGroup);

        radioGroup.clearCheck();
        alert = new AlertDialog.Builder(this);
        alert.setView(confirmDialog);


        alertDialog = alert.create();
        alertDialog.show();


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    String s = rb.getText().toString();
                    switch (s) {
                        case "5km":

                            saveroute.Savingrange("5");
                            alertDialog.dismiss();
                            break;
                        case "10km":

                            saveroute.Savingrange("10");
                            alertDialog.dismiss();
                            break;
                        case "15km":

                            saveroute.Savingrange("15");
                            alertDialog.dismiss();
                            break;
                        case "20km":

                            saveroute.Savingrange("20");
                            alertDialog.dismiss();
                            break;
                        default:
                            alertDialog.dismiss();
                            saveroute.Savingrange("0");
                            break;

                    }

                }

            }
        });



    }

    private void hostingdetails() {
        //Creating a LayoutInflater object for the dialog box
        // verifyOTP=OTP;
        LayoutInflater li = LayoutInflater.from(this);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.hostdetails, null);
        //  AppCompatButton buttonConfirm = (AppCompatButton) confirmDialog.findViewById(R.id.buttonConfirm);
        busno = (EditText) confirmDialog.findViewById(R.id.Busno);
        fromaddress = (EditText) confirmDialog.findViewById(R.id.From);
        toaddress = (EditText) confirmDialog.findViewById(R.id.TO);

        hosteddailog = new AlertDialog.Builder(this);
        hosteddailog.setView(confirmDialog);


        hostdailog = hosteddailog.create();
        hostdailog.show();


    }

    public void confirmhostdeatils(View view) {
        String bus = busno.getText().toString().trim();
        String from = fromaddress.getText().toString().trim();
        String to = toaddress.getText().toString().trim();

        if (hostdailog.isShowing()) {
            if (verify(bus, from, to)) {
                saveroute.Savinghost(bus,from,to);



                hostdailog.dismiss();

            } else {
                hoster = false;
            }

            // hostdailog.dismiss();
        }


    }

    private boolean verify(String bus, String from, String to) {
        boolean valid = true;
        if (bus.isEmpty()) {
            busno.setError("please enter bus no");
            valid = false;
        }
        if (from.isEmpty()) {
            fromaddress.setError("please enter valid buss");
            valid = false;
        }
        if (to.isEmpty()) {
            toaddress.setError("please enter to address");
            valid = false;
        }
        return valid;

    }

    public void Cancel(View view) {
        if (alertDialog.isShowing()) {
            radioGroup.clearCheck();
            alertDialog.dismiss();
        }


    }


    public void locationchecking(final Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            assert lm != null;
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ignored) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ignored) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setMessage(context.getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(context.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton(context.getString(R.string.Cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                }
            });
            dialog.show();
        }

    }

    private void uploadhost(double lat, double lon) {
        String fullname=name+" "+lastname;
       // Toast.makeText(MainActivity.this,fullname,Toast.LENGTH_LONG).show();
        //Here we will handle the http request to insert user to mysql db
        //Creating a RestAdapter

            //Here we will handle the http request to insert user to mysql db
            //Creating a RestAdapter
        //uploading = ProgressDialog.show(this, "Please wait...", "Fetching...", false, false);
            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(ROOT_URL) //Setting the Root URL
                    .build(); //Finally building the adapter

            //Creating object for our interface
        HostUpload api = adapter.create(HostUpload.class);

            //Defining the method insertuser of our interface
            api.insertUser(

                    //Passing the values by getting it from editTexts
                    fullname,
                   hostaddress,
                    Double.toString(lat),
                    Double.toString(lon),
                    //Creating an anonymous callback
                    new Callback<Response>() {
                        @Override
                        public void success(Response result, Response response) {
                            //On success we will read the server's output using bufferedreader
                            //Creating a bufferedreader object
                            BufferedReader reader;

                            //An string to store output from the server
                            String output = "";

                            try {
                                //Initializing buffered reader
                                reader = new BufferedReader(new InputStreamReader(result.getBody().in()));

                                //Reading the output in the string
                                output = reader.readLine();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                           // uploading.dismiss();
                           // Toast.makeText(getApplicationContext(),output,Toast.LENGTH_LONG).show();

                           setting(output);

                        }
                        @Override
                        public void failure(RetrofitError error) {
                            //If any error occured displaying the error as toast

                           // Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                        }
                    }
            );

    }

    public void setting(String output) {
        int checck = Integer.parseInt(output);
        if (checck == 11){
            if(counting==1)
            {
            Toast.makeText(getApplicationContext(), "succesfully hosted", Toast.LENGTH_LONG).show();
            counting++;}
        }
        if (checck == 403) {
             Toast.makeText(getApplicationContext(), "something wrong", Toast.LENGTH_LONG).show();
        }
        if (checck == 408) {
            Toast.makeText(getApplicationContext(), " wrong ", Toast.LENGTH_LONG).show();
        }


    }

    private void deleteUser() {

        Firstnamee = name;
        Lastnamee = lastname;
        //Here we will handle the http request to insert user to mysql db
        //Creating a RestAdapter
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Url.URL) //Setting the Root URL
                .build(); //Finally building the adapter

        //Creating object for our interface
        Remove api = adapter.create(Remove.class);

        //Defining the method insertuser of our interface
        api.insertUser(

                //Passing the values by getting it from editTexts
                Firstnamee+" "+Lastnamee,


                //Creating an anonymous callback
                new Callback<retrofit.client.Response>() {
                    @Override
                    public void success(retrofit.client.Response result, retrofit.client.Response response) {
                        //On success we will read the server's output using bufferedreader
                        //Creating a bufferedreader object
                        BufferedReader reader;

                        //An string to store output from the server
                        String output = "";

                        try {
                            //Initializing buffered reader
                            reader = new BufferedReader(new InputStreamReader(result.getBody().in()));

                            //Reading the output in the string
                            output = reader.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (output.equals("11"))
                        {
                        SnackbarMessagee("Hosted location turned off");}
                        if(output.equals("403")||output.equals("403"))
                        {
                            SnackbarMessagee("something wrong");
                        }


                    }

                    @Override
                    public void failure(RetrofitError error) {
                        //If any error occured displaying the error as toast
                        SnackbarMessagee(error.toString());

                    }
                 }
        );
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    public boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dailog = api.getErrorDialog(this, isAvailable, 0);
            dailog.show();
        } else {
            //Toast.makeText(this, "canot connect play service", Toast.LENGTH_LONG).show();
            SnackbarMessagee("canot connect play service");
        }
        return false;
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (id == R.id.near) {
            locationchecking(MainActivity.this);
            if (currentlongitude != null && currentlattitude != null) {
                checkLocationPermission();


                if (internetConnected) {
                    if (!saveroute.isrange()) {
                        Toast.makeText(MainActivity.this,"please choose range",Toast.LENGTH_LONG).show();
                    } if(saveroute.isrange()){
                        ranging=saveroute.setrange();
                        ranging.get(SaveHostdata.RANGE);
                        range=Integer.parseInt(ranging.get(SaveHostdata.RANGE));
                        //Toast.makeText(MainActivity.this, ranging.get(SaveHostdata.RANGE),Toast.LENGTH_LONG).show();
                        if(range>0){
                              navigationView.getMenu().findItem(R.id.stopnear).setVisible(true);
                              navigationView.getMenu().findItem(R.id.near).setVisible(false);
                             searrrch = true;
                             onResume();
                        }

                    }

                } else {
                    SnackbarMessagee(internetfailure);
                }


            } else {//Toast.makeText(MainActivity.this,"please make sure turnon your location",Toast.LENGTH_LONG).show();}
                SnackbarMessagee("please turn on the location OR restat the app ");
            }

        } else if (id == R.id.stopnear)

        {  if (friendmarker != null) {
            mGoogleMap.clear();
            }
            if (internetConnected) {
                navigationView.getMenu().findItem(R.id.near).setVisible(true);
                navigationView.getMenu().findItem(R.id.stopnear).setVisible(false);
                countings=1;
                searrrch = false;

            } else {
                SnackbarMessagee(internetfailure);
            }
        }    else if (id == R.id.Range) {
            Range();
        }
        else if (id == R.id.HostData) {
            hostingdetails();
        }
        else if (id == R.id.terrain) {
            terrain();
        } else if (id == R.id.Satilite) {
            Hybrid();
        } else if (id == R.id.normal) {
            setNormal();
        } else if (id == R.id.nav_share) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void putlogindetails(double lat, double lon) {
            count=0;
        String name = user.get(UserSessionManager.KEY_NAME);
        String lastname = user.get(UserSessionManager.KEY_LASTNAME);
        fullname = name + " " + lastname;
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(Url.URL).build(); //Finally building the adapter
        Nearby api = adapter.create(Nearby.class);
        api.insertUser(
                fullname,
                String.valueOf(lat),
                String.valueOf(lon),
                new Callback<retrofit.client.Response>() {
                    @Override
                    public void success(retrofit.client.Response result, retrofit.client.Response response) {
                        //On success we will read the server's output using bufferedreader
                        //Creating a bufferedreader object
                        BufferedReader reader;

                        //An string to store output from the server
                        String output = "";

                        try {
                            //Initializing buffered reader
                            reader = new BufferedReader(new InputStreamReader(result.getBody().in()));

                            //Reading the output in the string
                            output = reader.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //Displaying the output as a toast
                        //Toast.makeText(MainActivity.this, output, Toast.LENGTH_LONG).show();
                        friendslist(fullname);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        //If any error occured displaying the error as toast
                       // Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

                        //Toast.makeText(getApplicationContext(), "PLEASE CHECK INTERNET CONNECTION", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void terrain() {
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }

    public void setNormal() {

        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public void Hybrid() {
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    private void friendslist(String username) {

        //  loading = ProgressDialog.show(this, "Please wait...", "Fetching...", false, false);
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Url.URL) //Setting the Root URL
                .build(); //Finally building the adapter

        //Creating object for our interface
        friendlist api = adapter.create(friendlist.class);

        //Defining the method insertuser of our interface
        api.insertUser(
                username,
                Integer.toString(range),
                //Creating an anonymous callback
                new Callback<retrofit.client.Response>() {
                    @Override
                    public void success(retrofit.client.Response result, retrofit.client.Response response) {
                        //On success we will read the server's output using bufferedreader
                        //Creating a bufferedreader object
                        BufferedReader reader;

                        //An string to store output from the server
                        String output = "";

                        try {
                            //Initializing buffered reader
                            reader = new BufferedReader(new InputStreamReader(result.getBody().in()));

                            //Reading the output in the string
                            output = reader.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                         //Toast.makeText(getApplicationContext(), output,Toast.LENGTH_LONG).show();
                        showJSONS(output);

                        //switching(output);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        //If any error occured displaying the error as toast

                        //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

                      //  Toast.makeText(getApplicationContext(), "PLEASE CHECK INTERNET CONNECTION", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private void showJSONS(String response) {
        String hostname;
        String lattitude;
        String longitude;


        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");
            String nullable="[]";


            if(nullable.equals(result.toString()))
            {
                if(countings==1)
                {
                    Toast.makeText(getApplicationContext(),"No hosted persons",Toast.LENGTH_LONG).show();
                    mGoogleMap.clear();
                    countings++;}
            }

            int retval = personList.size();
            if (retval == 0) {
                for (int i = 0; i < result.length(); i++) {

                    hosters = result.getJSONObject(i);
                    hostname = hosters.getString(Findfriends.KEY_NAME);
                    lattitude = hosters.getString(Findfriends.KEY_LATTITUDE);
                    longitude = hosters.getString(Findfriends.KEY_LONGITUDE);
                    Hoster s = new Hoster(hostname, lattitude, longitude);

                    personList.add(s);


                }
            } else {
                personList.clear();
                for (int i = 0; i < result.length(); i++) {

                    hosters = result.getJSONObject(i);
                    hostname = hosters.getString(Findfriends.KEY_NAME);
                    lattitude = hosters.getString(Findfriends.KEY_LATTITUDE);
                    longitude = hosters.getString(Findfriends.KEY_LONGITUDE);
                    Hoster s = new Hoster(hostname, lattitude, longitude);
                    personList.add(s);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Toast.makeText(MainActivity.this,response,Toast).show();
        placingthemarker();


    }

    private class Hoster {
        String Hostname;
        String lattitude;
        String longitude;

        Hoster(String Hostname, String lattitude, String longitude) {
            this.Hostname = Hostname;
            this.lattitude = lattitude;
            this.longitude = longitude;
        }
    }

    private void placingthemarker() {
        String name;
        String lat;
        String lng;
        //  String markers = "true";
        int siz = personList.size();
        Iterator<Hoster> itr = personList.iterator();
        mGoogleMap.clear();
        while (itr.hasNext()) {
            Hoster hst = itr.next();
            name = hst.Hostname;
            lat = hst.lattitude;
            lng = hst.longitude;
            setmarkers(name, Double.parseDouble(lat), Double.parseDouble(lng), siz);
            // drawMarker(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)), name);

            // mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))));

        }
        //  Toast.makeText(this,"values are"+name,Toast.LENGTH_LONG).show();
    }

    private void setmarkers(String locality, double lat, double lon, int size) {

        LatLng ll = new LatLng(lat, lon);
        String info = getAddress(ll);


        MarkerOptions options = new MarkerOptions().title(locality)
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.busmarkeer))
                .position(new LatLng(lat, lon))
                .snippet(info);
        friendmarker = mGoogleMap.addMarker(options);

        friendmarker.showInfoWindow();
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(ll));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mlocationRequest = LocationRequest.create();

        mlocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mlocationRequest.setInterval(1000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                checkLocationPermission();
                return;
            }
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mlocationRequest, this);
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // checkLocationPermission();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;


        if (CheckInternet.isInternetAvailable(MainActivity.this))  //if connection available
        {

            if (mGoogleMap != null) {
                if (checkLocationPermission()) {
                    //  mGoogleMap.setOnMapLongClickListener(new );


                    mGoogleMap.setMyLocationEnabled(true);

                    mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            if (markerlong != null)
                                markerlong.remove();
                            if (marker != null)
                                marker.remove();

                        }
                    });

                    mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

                        @Override
                        public void onMapLongClick(LatLng latLng) {
                            if (internetConnected) {
                                try {
                                    if (markerlong.isVisible()) {
                                        markerlong.remove();
                                    }
                                } catch (Exception e) {

                                    e.printStackTrace();
                                }
                                markerlong = mGoogleMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));


                                LatLng ll = markerlong.getPosition();
                                String string = getAddress(ll);
                                markerlong.setSnippet(string);
                                markerlong.showInfoWindow();
                            } else {
                                SnackbarMessagee(internetfailure);
                            }

                        }
                    });


                    mGoogleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                        @Override
                        public void onMarkerDragStart(Marker marker) {

                        }

                        @Override
                        public void onMarkerDrag(Marker marker) {

                        }

                        @Override
                        public void onMarkerDragEnd(Marker marker) {
                            if (internetConnected) {
                                Geocoder gc = new Geocoder(MainActivity.this);
                                LatLng ll = marker.getPosition();
                                double lat = ll.latitude;
                                double lon = ll.longitude;
                                List<Address> list = null;
                                try {
                                    list = gc.getFromLocation(lat, lon, 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Address add = list.get(0);
                                marker.setTitle(add.getLocality());
                                marker.showInfoWindow();
                            } else {
                                SnackbarMessagee(internetfailure);
                            }
                        }


                    });


                    mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                        @Override
                        public View getInfoWindow(Marker marker) {
                            return null;
                        }

                        @Override
                        public View getInfoContents(Marker marker) {
                            View v = getLayoutInflater().inflate(R.layout.infowindow, null);
                            TextView tvlocality = (TextView) v.findViewById(R.id.tvlocality);
                            TextView tvsnippet = (TextView) v.findViewById(R.id.tvsnippet);
                            tvlocality.setText(marker.getTitle());
                            tvsnippet.setText(marker.getSnippet());
                            return v;
                        }
                    });

                } else {

                    mGoogleMap.setMyLocationEnabled(false);
                }
            }
            buildGoogleApiClient();
            mapis = true;
        } else {
            SnackbarMessagee(internetfailure);

        }


    }

    public String getAddress(LatLng point) {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this);
            if (point.latitude != 0 || point.longitude != 0) {
                addresses = geocoder.getFromLocation(point.latitude,
                        point.longitude, 1);
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getAddressLine(1);
                String country = addresses.get(0).getAddressLine(2);

                return address + " - " + city + " - " + country;

            } else {
                Toast.makeText(this, "latitude and longitude are null",
                        Toast.LENGTH_LONG).show();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        if (location == null) {
            Toast.makeText(this, "connot get current location", Toast.LENGTH_LONG).show();

        } else {
            LatLng latlngg = new LatLng(location.getLatitude(), location.getLongitude());
            currentlattitude = location.getLatitude();
            currentlongitude = location.getLongitude();

            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }

            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latlngg, 15);
            mGoogleMap.animateCamera(update);

        }

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

    }

    private void setmarker(String locality, double lat, double lon) {
        if (marker != null) {
            marker.remove();
        }

        MarkerOptions options = new MarkerOptions().title(locality)
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .position(new LatLng(lat, lon));
        marker = mGoogleMap.addMarker(options);
        marker.showInfoWindow();
    }

    /////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////

    public void geolocate(View view) throws IOException {
        EditText searchhint = (EditText) findViewById(R.id.hints);

        String locationn = searchhint.getText().toString();
        if (CheckInternet.isInternetAvailable(MainActivity.this))  //if connection available
        {


            if (locationn.isEmpty()) {
                //  Snackbar.make(view, "NO RESULTS FOUND", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                SnackbarMessagee("NO RESULTS FOUND");
            } else {
                List<Address> list = geoDecode(MainActivity.this, locationn);
                if (list == null) {
                    //Snackbar.make(view, "NO RESULTS FOUND", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    SnackbarMessagee("NO RESULTS FOUND");
                } else {
                    String s = list.get(0).getAddressLine(0);
                    String city = list.get(0).getAddressLine(1);

                    String locality = s + " " + city;
                    double lat = list.get(0).getLatitude();
                    double lon = list.get(0).getLongitude();

                    goToLocationZoom(lat, lon, 15);
                    setmarker(locality, lat, lon);
                }
            }
        } else {
            SnackbarMessagee(internetfailure);


        }

    }

    private void goToLocationZoom(Double lat, Double lon, float Zoom) {
        LatLng ll = new LatLng(lat, lon);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(ll));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(Zoom));
    }

    public static List<Address> geoDecode(Context context, String searchedLocation) {
        try {
            Geocoder geo = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geo.getFromLocationName(searchedLocation, 10);
            if (addresses != null && addresses.size() > 0) {
                return addresses;
            }
        } catch (Exception ex) {
        }

        return null;
    }

    @Override
    protected void onDestroy() {
        // call the superclass method first


        super.onDestroy();

    }


    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String status = Internetcheck.getConnectivityStatusString(context);
            setSnackbarMessage(status);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        registerInternetCheckReceiver();
        // startLocationUpdates();

        autoUpdate = new Timer();
        autoUpdate.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (searrrch) {
                            if(verified) {
                                locationupdates();
                                lattitudes = mCurrentLocation.getLatitude();
                                longitudes = mCurrentLocation.getLongitude();

//Toast.makeText(MainActivity.this,Double.toString(lattitude)+" and"+Double.toString(longitude),Toast.LENGTH_LONG).show();
                                putlogindetails(lattitudes, longitudes);
                            }

                        } else {
                            searrrch = false;
                        }
                        if (Hostingg) {
                            if(verified) {

                                locationupdates();
                                double lattitude = mCurrentLocation.getLatitude();
                                double longitude = mCurrentLocation.getLongitude();
                                //Toast.makeText(MainActivity.this,Double.toString(lattitude)+" and"+Double.toString(longitude),Toast.LENGTH_LONG).show();

                                uploadhost(lattitude, longitude);

                            }
                        } else {
                            Hostingg = false;
                        }
                        // updateHTML();

                    }
                });
            }
        }, 0, 30000);
        // updates each 40 secs
    }

    protected void locationupdates() {
        mlocationRequest = LocationRequest.create();

        mlocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mlocationRequest.setInterval(1000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                checkLocationPermission();
                return;
            }
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mlocationRequest, MainActivity.this);

    }

    private void registerInternetCheckReceiver() {
        IntentFilter internetFilter = new IntentFilter();
        internetFilter.addAction("android.net.wifi.STATE_CHANGE");
        internetFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadcastReceiver, internetFilter);
    }

    public void setSnackbarMessage(String status) {
        String internetStatus = "";
        if (status.equalsIgnoreCase("Wifi enabled") || status.equalsIgnoreCase("Mobile data enabled")) {
            internetStatus = "Connecton established";
        } else {
            internetStatus = "Could not connect to internet";
        }

        snackbar = Snackbar.make(drawer, internetStatus, Snackbar.LENGTH_LONG);
        // Changing message text color
        snackbar.setActionTextColor(Color.WHITE);

        // Changing action button text color
        View sbView = snackbar.getView();

        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        if (internetStatus.equalsIgnoreCase("Could not connect to internet")) {
            if (internetConnected) {

                sbView.setBackgroundColor(Color.RED);
             verified=false;
                snackbar.show();
                internetConnected = false;
            }
        } else {
            if (!internetConnected) {

                sbView.setBackgroundColor(Color.rgb(0, 100, 0));
                verified=true;
                internetConnected = true;
                snackbar.show();
            }
        }

    }
    public void SnackbarMessagee(String status) {
        String internetStatus = "";
        snackbar = Snackbar.make(drawer, status, Snackbar.LENGTH_LONG);
        // Changing message text color
        snackbar.setActionTextColor(Color.WHITE);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        sbView.setBackgroundColor(Color.RED);
        snackbar.show();
    }

}
