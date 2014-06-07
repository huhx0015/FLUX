package com.radiusnetworks.proximity.androidproximityreference;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.airportvip.app.AVIPFont;
import com.airportvip.app.AVIPFragment;
import com.airportvip.app.AVIPUser;
import com.airportvip.app.AVIPWeather;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.radiusnetworks.ibeacon.IBeacon;
import com.radiusnetworks.ibeacon.IBeaconConsumer;
import com.radiusnetworks.ibeacon.IBeaconData;
import com.radiusnetworks.ibeacon.IBeaconDataNotifier;
import com.radiusnetworks.ibeacon.RangeNotifier;
import com.radiusnetworks.ibeacon.Region;
import com.radiusnetworks.ibeacon.client.DataProviderException;
import com.radiusnetworks.proximity.ibeacon.IBeaconManager;
import com.squareup.picasso.Picasso;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainActivity extends FragmentActivity implements IBeaconConsumer, RangeNotifier, IBeaconDataNotifier {

    /** HUH MODIFICATIONS _____________________________________________________________________  **/

    // SYSTEM VARIABLES
    private SharedPreferences AVIP_temps; // SharedPreferences objects that store settings for the application.
    private SharedPreferences.Editor AVIP_temps_editor; // SharedPreferences.Editor objects that are used for editing preferences.
    private AVIPFragment avipFrag;
    private LinearLayout fragmentDisplay, buttonDisplay;
    private Boolean showFragment = false;
    private String currentFragment = "WELCOME";

    // CLASS VARIABLES
    private AVIPUser vip;
    private AVIPWeather vipWeather;

    TextView departureTime;

    private void chooseLayout(Boolean isDebug) {

        // Use Lance's original layout
        if (isDebug) { setContentView(R.layout.activity_main); }

        // Use modified layout file and set up resources for modified layout.
        else {

            setContentView(R.layout.avip_main);

            // INITIALIZE AVIPUser & AVIPWeather class.
            vip = new AVIPUser();
            vip.initializeVIP(); // Sets the VIP dummy stats.
            vipWeather = new AVIPWeather();
            vipWeather.initializeWeather(); // Sets the dummy weather stats.

            // LAYOUT INITIALIZATION
            setUpLayout(); // Sets up the layout for the activity.
            setUpStickyBar(); // Sets up the top sticky bar.
            setUpButtons(); // Sets up the buttons.
            setUpBackground(currentFragment); // Sets up the background image.

            welcomeVIP(); // Shows the first two frames.

            // Starts countdown timer.
            countDownToFlight();
        }
    }

    /** ACTIVITY EXTENSION FUNCTIONALITY _______________________________________________________ **/

    // Refreshes the fragment contents.
    private void refreshFragment() {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.avip_fragment_container, new AVIPFragment());

        //ft.replace(R.id.avip_fragment, new AVIPFragment(), "avip_fragment");
        ft.addToBackStack(null);

        int count = fm.getBackStackEntryCount();
        fm.popBackStackImmediate(count, 0);
        ft.commit();

    }

    /** LAYOUT FUNCTIONALITY _______________________________________________________ **/

    // displayFragment(): Displays/hides the fragment & button containers.
    private void displayFragment(Boolean isShow, String fragment) {

        // If the fragment is currently being displayed, the fragment is hidden.
        if (isShow == true) {
            fragmentDisplay.setVisibility(View.INVISIBLE); // Hides the fragment.
            buttonDisplay.setVisibility(View.VISIBLE);
            showFragment = false; // Indicates that the fragment is hidden.
        }

        // If the fragment is currently hidden, the fragment is displayed.
        else {
            fragmentDisplay.setVisibility(View.VISIBLE); // Displays the fragment.
            buttonDisplay.setVisibility(View.INVISIBLE);
            showFragment = true; // Indicates that the fragment is currently being displayed.
        }
    }

    private void setUpBackground(String fragment) {

        int activityBackground;

        if (fragment.equals("WELCOME")) {
            activityBackground = R.drawable.bg_parking_v2;
        }

        else {
            activityBackground = R.drawable.bg_club;
        }

        ImageView avip_background = (ImageView) findViewById(R.id.avip_background);
        Picasso.with(this).load(activityBackground).noFade().into(avip_background); // Loads the image into the ImageView object.
    }

    private void setUpLayout() {

        int activityLayout = R.layout.avip_main; // Used to reference the application layout.
        //setContentView(activityLayout); // Sets the layout for the current activity.

        // References the fragment & button containers.
        fragmentDisplay = (LinearLayout) findViewById(R.id.avip_fragment_container);
        buttonDisplay = (LinearLayout) findViewById(R.id.avip_button_subcontainer);

        // References the AVIPFragment class.
        //avipFrag = (AVIPFragment) getSupportFragmentManager().findFragmentById(R.id.avip_fragment);
    }

    private void setUpStickyBar() {

        // Loads the airline icon.
        int flightIcon = R.drawable.upper_left_logo_glass; // American Airlines Logo.
        ImageView avip_flight_icon = (ImageView) findViewById(R.id.avip_sticky_flight_icon);
        Picasso.with(this).load(flightIcon).noFade().into(avip_flight_icon); // Loads the image into the ImageView object.

        // Gate information.
        departureTime = (TextView) findViewById(R.id.avip_sticky_departure);
        TextView gateText = (TextView) findViewById(R.id.avip_sticky_gate);
        TextView flightNumber = (TextView) findViewById(R.id.avip_sticky_flight_number);

        // Sets sticky text properties.
        flightNumber.setText(vip.getFlightNumber());
        gateText.setText(vip.getGateNumber());
        departureTime.setText(vip.getEtaDeparture());

        // Sets custom font properties.
        flightNumber.setTypeface(AVIPFont.getInstance(this).getTypeFace());
        gateText.setTypeface(AVIPFont.getInstance(this).getTypeFace());
        departureTime.setTypeface(AVIPFont.getInstance(this).getTypeFace());
    }

    private void setUpButtons() {

        // Reference the button objects.
        Button flightButton = (Button) findViewById(R.id.avip_flight_button);
        Button wifiButton = (Button) findViewById(R.id.avip_wifi_button);
        Button weatherButton = (Button) findViewById(R.id.avip_weather_button);
        Button dealButton = (Button) findViewById(R.id.avip_deal_button);

        // Set custom text.
        flightButton.setTypeface(AVIPFont.getInstance(this).getTypeFace());
        wifiButton.setTypeface(AVIPFont.getInstance(this).getTypeFace());
        weatherButton.setTypeface(AVIPFont.getInstance(this).getTypeFace());
        dealButton.setTypeface(AVIPFont.getInstance(this).getTypeFace());

        flightButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                currentFragment = "FLIGHT";
                updateFragmentText(currentFragment);
                refreshFragment();
                displayFragment(showFragment, currentFragment);
            }
        });

        wifiButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                currentFragment = "WIFI";
                updateFragmentText(currentFragment);
                refreshFragment();
                displayFragment(showFragment, currentFragment);
            }
        });

        weatherButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                currentFragment = "WEATHER";
                updateFragmentText(currentFragment);
                refreshFragment();
                displayFragment(showFragment, currentFragment);
            }
        });

        dealButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                currentFragment = "WELCOME";
                updateFragmentText(currentFragment);
                refreshFragment();
                displayFragment(showFragment, currentFragment);
            }
        });

    }

    /** PHYSICAL BUTTON FUNCTIONALITY __________________________________________________________ **/

    // BACK KEY:
    // onBackPressed(): Defines the action to take when the physical back button key is pressed.
    @Override
    public void onBackPressed() {

        // Closes the fragment if currently being displayed.
        if (showFragment) {
            displayFragment(showFragment, currentFragment);
        }

        else {
            finish(); // Finishes the activity.
        }
    }

    /** ADDITIONAL FUNCTIONALITY _______________________________________________________________ **/


    private void welcomeVIP() {

        final int waitTime = 1500; // 1500 ms

        // Displays the welcome frame.
        currentFragment = "WELCOME";
        updateFragmentText(currentFragment); // Update the fragment contents.
        refreshFragment(); // Refresh the fragment.
        displayFragment(showFragment, currentFragment); // Displays the fragment.

        // Timer for the initial frame.
        new CountDownTimer(waitTime, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {

                // Displays the ready frame.
                currentFragment = "READY";
                updateFragmentText(currentFragment); // Update the fragment contents.
                refreshFragment(); // Refresh the fragment.
                refreshFragment();
            }

        }.start();

        // Timer for the second frame.
        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

                // Displays the ready frame.
                displayFragment(showFragment, currentFragment); // Displays the fragment.
            }

        }.start();
    }

    private void updateFragmentText(String fragment) {

        // Preference values.
        String avip_line_1, avip_line_2, avip_line_3, avip_weather;
        Boolean isWeather = false;

        // Sets the preferences.
        AVIP_temps = getSharedPreferences("avip_temps", Context.MODE_PRIVATE);
        AVIP_temps_editor = AVIP_temps.edit();  // Sets up shared preferences for editing.

        // Update the fragment texts.
        if (fragment.equals("WELCOME")) {
            avip_line_1 = "Welcome to " + vip.getAirportName() + " Airport,"  ;
            avip_line_2 = vip.getVipName() + ".";
            avip_line_3 = "";

            AVIP_temps_editor.putBoolean("avip_weather_enabled", false); // Disables weather icon.
        }

        // Update the fragment texts.
        else if (fragment.equals("READY")) {
            avip_line_1 = "Please get your";
            avip_line_2 = "boarding pass ready.";
            avip_line_3 = "";

            AVIP_temps_editor.putBoolean("avip_weather_enabled", false); // Disables weather icon.
        }

        else if (fragment.equals("FLIGHT")) {
            avip_line_1 = vip.getDestinationName();
            avip_line_2 = vip.getFlightNumber();
            avip_line_3 = vip.getSeatNumber();

            AVIP_temps_editor.putBoolean("avip_weather_enabled", false); // Disables weather icon.
        }

        else if (fragment.equals("WEATHER")) {
            avip_line_1 = vipWeather.getArrivalName();
            avip_line_2 = vipWeather.getArrivalTemp();
            avip_line_3 = vipWeather.getArriveCurrentWeather();

            AVIP_temps_editor.putBoolean("avip_weather_enabled", true); // Enable weather icon.
        }

        else if (fragment.equals("WIFI")) {
            avip_line_1 = "NETWORK: SAY_guest";
            avip_line_2 = "PASSWORD: sayernet";
            avip_line_3 = "";

            AVIP_temps_editor.putBoolean("avip_weather_enabled", false); // Disables weather icon.
        }

        else {
            avip_line_1 = "Welcome to " + vip.getAirportName() + " Airport,"  ;
            avip_line_2 = vip.getVipName() + ".";
            avip_line_3 = "";

            AVIP_temps_editor.putBoolean("avip_weather_enabled", false); // Disables weather icon.
        }

        AVIP_temps_editor.putString("avip_line_1", avip_line_1);
        AVIP_temps_editor.putString("avip_line_2", avip_line_2);
        AVIP_temps_editor.putString("avip_line_3", avip_line_3);
        AVIP_temps_editor.commit(); // Saves the new preferences.
    }


    // updates the background images. Modification of Lance's updateFields function.
    public void updateBackgrounds(
            final HackathonBeacon foundHackathonBeacon,
            final IBeacon beacon) {
        Log.d(TAG, "updateServerAndFields");
        if ( null == foundHackathonBeacon || null == beacon ) {
            return;
        }

        final boolean sameHackathonBeacon = null != currentBeacon
                && currentBeacon == foundHackathonBeacon;
        final boolean sameDistance = null != previousDistance
                && previousDistance.equals(beacon.getAccuracy());

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!sameHackathonBeacon || !sameDistance) {

                    // Background image reference.
                    int activityBackground = R.drawable.transparent_tile; // BLANK
                    ImageView avip_background = (ImageView) findViewById(R.id.avip_background);

                    if (HackathonBeacon.CHECK_IN == foundHackathonBeacon) {

                        activityBackground = R.drawable.bg_checkin;
                        Picasso.with(MainActivity.this).load(activityBackground).noFade().into(avip_background); // Loads the image into the ImageView object.

                    } else if (HackathonBeacon.PARKING == foundHackathonBeacon) {

                        activityBackground = R.drawable.bg_parking_v2;
                        Picasso.with(MainActivity.this).load(activityBackground).noFade().into(avip_background); // Loads the image into the ImageView object.

                    } else if (HackathonBeacon.GATE_A22 == foundHackathonBeacon) {

                        activityBackground = R.drawable.bg_gate;
                        Picasso.with(MainActivity.this).load(activityBackground).noFade().into(avip_background); // Loads the image into the ImageView object.

                    } else if (HackathonBeacon.SECURITY == foundHackathonBeacon) {

                        activityBackground = R.drawable.bg_security; // BLANK
                        Picasso.with(MainActivity.this).load(activityBackground).noFade().into(avip_background); // Loads the image into the ImageView object.

                    } else if (HackathonBeacon.CLUB == foundHackathonBeacon) {

                        activityBackground = R.drawable.bg_club;
                        Picasso.with(MainActivity.this).load(activityBackground).noFade().into(avip_background); // Loads the image into the ImageView object.
                    }

                    if (!sameHackathonBeacon) {
                        previousLocations.setText(previousLocations.getText() + " " + foundHackathonBeacon);
                        previousLocationsString = previousLocationsString + " " + foundHackathonBeacon;
                    }
                    currentBeacon = foundHackathonBeacon;

                    currentLocation.setText(
                            "Current location: " + foundHackathonBeacon
                                    + "\nDistance: " + beacon.getAccuracy()
                                    + "\nProximity: " + getProximityString(beacon.getProximity()));
                    previousDistance = beacon.getAccuracy();
                }
            }
        });
    }

    // Countdown Timer.
    private void countDownToFlight() {

        new CountDownTimer(1800000, 1000) {

            public void onTick(long millisUntilFinished) {
               departureTime.setText("0:" + millisUntilFinished / 1000 / 60);
            }

            public void onFinish() {
                departureTime.setText("DEPARTED");
            }
        }.start();
    }

    /** INTERFACE FUNCTIONALITY ________________________________________________________________ **/

    public interface OnRefreshSelected {
        public void refreshFragment(boolean flag);
    }

    private OnRefreshSelected onRefreshSelected;


    // END HUH MODIFICATIONS -----------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------


    public static final String TAG = "MainActivity";

    IBeaconManager iBeaconManager;
    Map<String, TableRow> rowMap = new HashMap<String, TableRow>();

    private View container;
    private EditText username;
    private TextView currentLocation;
    private TextView previousLocations;

    private String previousLocationsString = "";

    private HackathonBeacon currentBeacon;

    private Double previousDistance;

    private Handler handler = new Handler();

    private ProgressDialog progressDialog;

    private int updateCount = 1;

    private ScreenWaker screenWaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate hasFocus = " + hasWindowFocus());


        screenWaker = new ScreenWaker(this);
        IBeaconManager.LOG_DEBUG = true;
        super.onCreate(savedInstanceState);


        /** HUH MODIFICATION ___________________________________________________________________ **/
        chooseLayout(false);
        //------------------------------------------------------------------------------------------


        username = (EditText) findViewById(R.id.username);
        currentLocation = (TextView) findViewById(R.id.currentLocation);
        previousLocations = (TextView) findViewById(R.id.previousLocations);
        container = findViewById(R.id.container);


        iBeaconManager = IBeaconManager.getInstanceForApplication(this.getApplicationContext());
        iBeaconManager.bind(this);
    }

    @Override
    public void onIBeaconServiceConnect() {
        Region region = new Region("MainActivityRanging", null, null, null);
        try {
            iBeaconManager.startRangingBeaconsInRegion(region);
            iBeaconManager.setRangeNotifier(this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart hasFocus = " + hasWindowFocus());

        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume hasFocus = " + hasWindowFocus());

        super.onResume();
        screenWaker.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause hasFocus = " + hasWindowFocus());

        super.onPause();
        screenWaker.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop hasFocus = " + hasWindowFocus());

        super.onStop();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        Log.d(TAG, "onWindowFocusChanged hasFocus = " + hasFocus);

        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy hasFocus = " + hasWindowFocus());

        super.onDestroy();
        iBeaconManager.unBind(this);
    }

    //final Gson gson = new Gson();

    @Override
    public void didRangeBeaconsInRegion(Collection<IBeacon> iBeacons, Region region) {

        //final String json = gson.toJson(iBeacons);
        //Log.d(TAG, "didRangeBeaconsInRegion json = " + json);

        Log.d(TAG, "didRangeBeaconsInRegion");

        HackathonBeacon closestBeacon  = null;
        IBeacon closestIBeacon = null;
        final List<DetectedBeacon> detectedBeaconList = new LinkedList<DetectedBeacon>();
        for (IBeacon iBeacon : iBeacons) {
            iBeacon.requestData(this);



            Log.d(TAG, "I see an iBeacon: " + iBeacon.getProximityUuid() + "," + iBeacon.getMajor() + "," + iBeacon.getMinor());

            final HackathonBeacon foundHackathonBeacon = HackathonBeacon.findMatching(iBeacon);
            if ( null != foundHackathonBeacon ) {
                detectedBeaconList.add(new DetectedBeacon(iBeacon));

                if ( null == closestBeacon || iBeacon.getAccuracy() < closestIBeacon.getAccuracy() ) {
                    closestBeacon = foundHackathonBeacon;
                    closestIBeacon = iBeacon;
                }
            }

            String displayString = iBeacon.getProximityUuid() + " " + iBeacon.getMajor() + " " + iBeacon.getMinor()
                    + (null == foundHackathonBeacon ? "" : "\n Hackathon beacon: " + foundHackathonBeacon.name());
            displayTableRow(iBeacon, displayString, false);
        }

        if (null != closestBeacon && null != closestIBeacon) {
            updateFields(closestBeacon, closestIBeacon);

            /** HUH MODIFICATION **/
            updateBackgrounds(closestBeacon, closestIBeacon);

        }

        if (!detectedBeaconList.isEmpty()) {
            Log.d(TAG, "updating server...");
            Toast.makeText(MainActivity.this,
                    "Updating server " + (updateCount++), Toast.LENGTH_LONG).show();
            ServerRemoteClient.updateServer(username.getText().toString(),
                    detectedBeaconList,
                    MainActivity.this);
        } else {
            Log.d(TAG, "nothing found. not updating server...");
        }
    }

    public static String getProximityString(final int value) {
        if (value == IBeacon.PROXIMITY_FAR ) {
            return "FAR";
        } else if (value == IBeacon.PROXIMITY_IMMEDIATE ) {
            return "IMMEDIATE ";
        } else if (value == IBeacon.PROXIMITY_NEAR ) {
            return "NEAR";
        }
        return "UNKNOWN";
    }

    public void updateFields(
            final HackathonBeacon foundHackathonBeacon,
            final IBeacon beacon) {
        Log.d(TAG, "updateServerAndFields");
        if ( null == foundHackathonBeacon || null == beacon ) {
            return;
        }

        final boolean sameHackathonBeacon = null != currentBeacon
                && currentBeacon == foundHackathonBeacon;
        final boolean sameDistance = null != previousDistance
                && previousDistance.equals(beacon.getAccuracy());

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!sameHackathonBeacon || !sameDistance) {

                    if (HackathonBeacon.CHECK_IN == foundHackathonBeacon) {
                        //container.setBackground(null);

                    } else if (HackathonBeacon.PARKING == foundHackathonBeacon) {
                        //container.setBackgroundResource(R.drawable.bg_parking);

                    } else if (HackathonBeacon.GATE_A22 == foundHackathonBeacon) {
                        //container.setBackgroundResource(R.drawable.bg_gate);

                    } else if (HackathonBeacon.SECURITY == foundHackathonBeacon) {
                        //container.setBackground(null);

                    } else if (HackathonBeacon.CLUB == foundHackathonBeacon) {
                        //container.setBackgroundResource(R.drawable.bg_club);
                    }




                    if (!sameHackathonBeacon) {
                        previousLocations.setText(previousLocations.getText() + " " + foundHackathonBeacon);
                        previousLocationsString = previousLocationsString + " " + foundHackathonBeacon;
                    }
                    currentBeacon = foundHackathonBeacon;

                    currentLocation.setText(
                            "Current location: " + foundHackathonBeacon
                            + "\nDistance: " + beacon.getAccuracy()
                                    + "\nProximity: " + getProximityString(beacon.getProximity()));
                    previousDistance = beacon.getAccuracy();
                }
            }
        });
    }

    @Override
    public void iBeaconDataUpdate(IBeacon iBeacon, IBeaconData iBeaconData, DataProviderException e) {
        if (e != null) {
            Log.d(TAG, "data fetch error:" + e);
        }
        if (iBeaconData != null) {

            Log.d(TAG, "I have an iBeacon with data: uuid=" + iBeacon.getProximityUuid() + " major=" + iBeacon.getMajor() + " minor=" + iBeacon.getMinor() + " welcomeMessage=" + iBeaconData.get("welcomeMessage"));

            final HackathonBeacon foundHackathonBeacon = HackathonBeacon.findMatching(iBeacon);
            String displayString = iBeacon.getProximityUuid() + " " + iBeacon.getMajor() + " " + iBeacon.getMinor()
                    + (null == foundHackathonBeacon ? "" : "\n Hackathon beacon: " + foundHackathonBeacon.name())
                    + "\n" + "Welcome message:" + iBeaconData.get("welcomeMessage");

            //updateServerAndFields(foundHackathonBeacon, iBeacon);

            displayTableRow(iBeacon, displayString, true);
        }
    }

    private void displayTableRow(final IBeacon iBeacon, final String displayString, final boolean updateIfExists) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TableLayout table = (TableLayout) findViewById(R.id.beacon_table);
                String key = iBeacon.getProximity() + "-" + iBeacon.getMajor() + "-" + iBeacon.getMinor();
                TableRow tr = (TableRow) rowMap.get(key);
                if (tr == null) {
                    tr = new TableRow(MainActivity.this);
                    tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    rowMap.put(key, tr);
                    table.addView(tr);
                } else {
                    if (updateIfExists == false) {
                        return;
                    }
                }
                tr.removeAllViews();
                TextView textView = new TextView(MainActivity.this);
                textView.setText(displayString);
                tr.addView(textView);

            }
        });

    }

    public void onServerUpdated() {
        Log.d(TAG, "onServerUpdated");
    }

    public void onServerUpdateError() {
        Log.d(TAG, "onServerUpdateError");

    }

}
