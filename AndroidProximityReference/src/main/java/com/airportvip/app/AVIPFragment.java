package com.airportvip.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.radiusnetworks.proximity.androidproximityreference.MainActivity;
import com.radiusnetworks.proximity.androidproximityreference.R;
import com.squareup.picasso.Picasso;

public class AVIPFragment extends Fragment implements MainActivity.OnRefreshSelected {

    /**
     * CLASS VARIABLES ________________________________________________________________________ *
     */

    private SharedPreferences AVIP_temps; // SharedPreferences objects that store settings for the application.
    private SharedPreferences.Editor AVIP_temps_editor; // SharedPreferences.Editor objects that are used for editing preferences.

    // LAYOUT VARIABLES
    private View avip_fragment_view; // References the layout for the fragment.
    private String currentFragment; // Current fragment.

    // SYSTEM VARIABLES
    private Activity currentActivity; // Used to attach activity to this fragment.

    /**
     * FRAGMENT LIFECYCLE FUNCTIONALITY _______________________________________________________ *
     */

    // creates and returns the view hierarchy associated with the fragment.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        avip_fragment_view = inflater.inflate(R.layout.avip_fragment, container, false);
        setUpText();
        return avip_fragment_view;
    }

    // tells the fragment that its activity has completed its own Activity.onCreate().
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // Attaches activity to this fragment.
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.currentActivity = activity;
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

    /**
     * FRAGMENT EXTENSION FUNCTIONALITY _______________________________________________________ *
     */

    @Override
    public void refreshFragment(boolean flag) {
        setUpText();
    }

    /**
     * LAYOUT FUNCTIONALITY ___________________________________________________________________ *
     */

    private void setUpText() {

        // References the TextView objects.
        TextView avip_fragment_line_1 = (TextView) avip_fragment_view.findViewById(R.id.avip_line_1);
        TextView avip_fragment_line_2 = (TextView) avip_fragment_view.findViewById(R.id.avip_line_2);
        TextView avip_fragment_line_3 = (TextView) avip_fragment_view.findViewById(R.id.avip_line_3);

        // Get the preferences.
        AVIP_temps = this.getActivity().getSharedPreferences("avip_temps", Context.MODE_PRIVATE);
        currentFragment = AVIP_temps.getString("avip_fragment", "");
        String avip_line_1 = AVIP_temps.getString("avip_line_1", "");
        String avip_line_2 = AVIP_temps.getString("avip_line_2", "");
        String avip_line_3 = AVIP_temps.getString("avip_line_3", "");

        // Sets the text.
        avip_fragment_line_1.setText(avip_line_1);
        avip_fragment_line_2.setText(avip_line_2);
        avip_fragment_line_3.setText(avip_line_3);
    }

    private void setUpImages() {

        // Loads the weather icon.
        int weatherImage = R.drawable.aa_icon;
        ImageView weatherIcon = (ImageView) avip_fragment_view.findViewById(R.id.avip_fragment_weather_icon);
        Picasso.with(this.getActivity()).load(weatherImage).noFade().into(weatherIcon);
    }

    private void updateBackground(String beacon) {

        int backgroundID = R.drawable.bg_checkin;
        ImageView backgroundImage = (ImageView) avip_fragment_view.findViewById(R.id.avip_fragment_background);
        Picasso.with(this.getActivity()).load(backgroundID).noFade().into(backgroundImage);

    }

    /** INTERFACE FUNCTIONALITY ________________________________________________________________ **/

    /*
    public interface OnFragmentSelectedListener{

        public void updateActivity();
    }
    */
}