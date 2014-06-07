package com.airportvip.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.radiusnetworks.proximity.androidproximityreference.R;
import com.squareup.picasso.Picasso;

public class AVIPMain extends FragmentActivity  {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    // SYSTEM VARIABLES
    private SharedPreferences AVIP_temps; // SharedPreferences objects that store settings for the application.
    private SharedPreferences.Editor AVIP_temps_editor; // SharedPreferences.Editor objects that are used for editing preferences.
    private AVIPFragment avipFrag;
    private LinearLayout fragmentDisplay;
    private Boolean showFragment = false;
    private String currentFragment = "WELCOME";

    // CLASS VARIABLES
    private AVIPUser vip;

    /** ACTIVITY LIFECYCLE FUNCTIONALITY _______________________________________________________ **/

    // onCreate(): The initial function that is called when the activity is run. onCreate() only runs
    // when the activity is first started.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // INITIALIZE AVIPUser class.
        vip = new AVIPUser();
        vip.initializeVIP(); // Sets the VIP dummy stats.

        // LAYOUT INITIALIZATION
        setUpLayout(); // Sets up the layout for the activity.
        setUpStickyBar(); // Sets up the top sticky bar.
        setUpButtons(); // Sets up the buttons.
        setUpBackground(currentFragment); // Sets up the background image.

        welcomeVIP(); // Shows the first two frames.
    }

    // onResume(): This function runs immediately after onCreate() finishes and is always re-run
    // whenever the activity is resumed from an onPause() state.
    @Override
    public void onResume() {
        super.onResume();
    }

    // onPause(): This function is called whenever the current activity is suspended or another
    // activity is launched.
    @Override
    public void onPause() {
        super.onPause();
    }

    // onStop(): This function runs when screen is no longer visible and the activity is in a
    // state prior to destruction.
    @Override
    public void onStop() {
        super.onStop();
    }

    // onDestroy(): This function runs when the activity has terminated and is being destroyed.
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /** ACTIVITY EXTENSION FUNCTIONALITY _______________________________________________________ **/

    // Refreshes the fragment contents.
    private void refreshFragment() {

        /*
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.avip_fragment, new AVIPFragment(), "avip_fragment");
        ft.commit();
        */
    }

    /** LAYOUT FUNCTIONALITY _______________________________________________________ **/

    // displayFragment(): Displays/hides the fragment object.
    private void displayFragment(Boolean isShow, String fragment) {

        // If the fragment is currently being displayed, the fragment is hidden.
        if (isShow == true) {
            fragmentDisplay.setVisibility(View.INVISIBLE); // Hides the fragment.
            showFragment = false; // Indicates that the fragment is hidden.
        }

        // If the fragment is currently hidden, the fragment is displayed.
        else {
            fragmentDisplay.setVisibility(View.VISIBLE); // Displays the fragment.
            showFragment = true; // Indicates that the fragment is currently being displayed.
        }
    }

    private void setUpBackground(String fragment) {

        int activityBackground;

        if (fragment.equals("WELCOME")) {
            activityBackground = R.drawable.bg_parking;
        }

        else {
            activityBackground = R.drawable.bg_club;
        }

        ImageView avip_background = (ImageView) findViewById(R.id.avip_background);
        Picasso.with(this).load(activityBackground).noFade().into(avip_background); // Loads the image into the ImageView object.
    }

    private void setUpLayout() {

        int activityLayout = R.layout.avip_main; // Used to reference the application layout.
        setContentView(activityLayout); // Sets the layout for the current activity.

        // References the fragment container.
        fragmentDisplay = (LinearLayout) findViewById(R.id.avip_fragment_container);
        //avipFrag = (AVIPFragment) getSupportFragmentManager().findFragmentById(R.id.avip_fragment);
    }

    private void setUpStickyBar() {

        // Loads the airline icon.
        int flightIcon = R.drawable.aa_icon; // American Airlines Logo.
        ImageView avip_flight_icon = (ImageView) findViewById(R.id.avip_sticky_flight_icon);
        Picasso.with(this).load(flightIcon).noFade().into(avip_flight_icon); // Loads the image into the ImageView object.

        // Gate information.
        TextView gateText = (TextView) findViewById(R.id.avip_sticky_gate);
        TextView departureTime = (TextView) findViewById(R.id.avip_sticky_departure);

        // Sets sticky text properties.
        gateText.setText(vip.getGateNumber());
        departureTime.setText(vip.getEtaDeparture());

        // Sets custom font properties.
        //gateText.setTypeface(AVIPFont.getInstance(this).getTypeFace());
        //departureTime.setTypeface(AVIPFont.getInstance(this).getTypeFace());
    }

    private void setUpButtons() {

        // Reference the button objects.
        Button flightButton = (Button) findViewById(R.id.avip_flight_button);
        Button wifiButton = (Button) findViewById(R.id.avip_wifi_button);
        Button weatherButton = (Button) findViewById(R.id.avip_weather_button);
        Button dealButton = (Button) findViewById(R.id.avip_deal_button);

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

                Intent i = new Intent("com.radiusnetworks.proximity.androidproximityreference.MainActivity");
                startActivity(i);
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

        String avip_line_1, avip_line_2, avip_line_3;

        // Update the fragment texts.
        if (fragment.equals("WELCOME")) {
            avip_line_1 = "Welcome to " + vip.getAirportName() + " Airport,"  ;
            avip_line_2 = vip.getVipName() + ".";
            avip_line_3 = "";
        }

        // Update the fragment texts.
        else if (fragment.equals("READY")) {
            avip_line_1 = "Please get your";
            avip_line_2 = "boarding pass ready.";
            avip_line_3 = "";
        }

        else if (fragment.equals("FLIGHT")) {
            avip_line_1 = "CHICAGO, IL";
            avip_line_2 = "FL 12345";
            avip_line_3 = "SEAT 24B";
        }

        else if (fragment.equals("WEATHER")) {
            avip_line_1 = "CHICAGO, IL";
            avip_line_2 = "48°";
            avip_line_3 = "";
        }

        else if (fragment.equals("WIFI")) {
            avip_line_1 = "NETWORK: SAY_guest";
            avip_line_2 = "PASSWORD: sayernet";
            avip_line_3 = "";
        }

        else {
            avip_line_1 = "Welcome to " + vip.getAirportName() + " Airport,"  ;
            avip_line_2 = vip.getVipName() + ".";
            avip_line_3 = "";
        }

        // Sets the preferences.
        AVIP_temps = getSharedPreferences("avip_temps", Context.MODE_PRIVATE);
        AVIP_temps_editor = AVIP_temps.edit();  // Sets up shared preferences for editing.
        AVIP_temps_editor.putString("avip_line_1", avip_line_1);
        AVIP_temps_editor.putString("avip_line_2", avip_line_2);
        AVIP_temps_editor.putString("avip_line_3", avip_line_3);
        AVIP_temps_editor.commit(); // Saves the new preferences.
    }

    /** INTERFACE FUNCTIONALITY ________________________________________________________________ **/

    public interface OnRefreshSelected {
        public void refreshFragment(boolean flag);
    }

    private OnRefreshSelected onRefreshSelected;
}
