package com.radiusnetworks.proximity.androidproximityreference;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.radiusnetworks.ibeacon.IBeacon;
import com.radiusnetworks.ibeacon.IBeaconConsumer;
import com.radiusnetworks.ibeacon.IBeaconData;
import com.radiusnetworks.ibeacon.IBeaconDataNotifier;
import com.radiusnetworks.ibeacon.RangeNotifier;
import com.radiusnetworks.ibeacon.Region;
import com.radiusnetworks.ibeacon.client.DataProviderException;
import com.radiusnetworks.proximity.ibeacon.IBeaconManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EnableBluetoothActivity extends Activity {

    public static final String TAG = "EnableBluetoothActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final TextView textView = new TextView(this);
        textView.setText("Enabling Bluetooth...");
        textView.setGravity(Gravity.CENTER);
        setContentView(textView);

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isEnabledboolean = bluetoothAdapter.isEnabled();
        if (!isEnabledboolean) {
            bluetoothAdapter.enable();
        }
    }

}
