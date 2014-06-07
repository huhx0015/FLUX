package com.radiusnetworks.proximity.androidproximityreference;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

public class DisableBluetoothActivity extends Activity {

    public static final String TAG = "DisableBluetoothActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final TextView textView = new TextView(this);
        textView.setText("Disabling Bluetooth...");
        textView.setGravity(Gravity.CENTER);
        setContentView(textView);

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isEnabledboolean = bluetoothAdapter.isEnabled();
        if (isEnabledboolean) {
            bluetoothAdapter.disable();
        }
    }

}
