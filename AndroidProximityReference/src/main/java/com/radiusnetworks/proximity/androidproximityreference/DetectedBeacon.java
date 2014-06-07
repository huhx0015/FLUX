package com.radiusnetworks.proximity.androidproximityreference;

import com.radiusnetworks.ibeacon.IBeacon;

/**
 * Created by lnanek on 6/6/14.
 */
public class DetectedBeacon {

    public Double distanceMeters;

    public String proximity;

    public String hackathonLocation;

    public DetectedBeacon(IBeacon iBeacon) {

        distanceMeters = iBeacon.getAccuracy();

        proximity = getProximityString(iBeacon.getProximity());

        final HackathonBeacon foundHackathonBeacon = HackathonBeacon.findMatching(iBeacon);
        if (null != foundHackathonBeacon) {
            hackathonLocation = foundHackathonBeacon.name();
        }
    }

    public static String getProximityString(final int value) {
        if (value == IBeacon.PROXIMITY_FAR) {
            return "FAR";
        } else if (value == IBeacon.PROXIMITY_IMMEDIATE) {
            return "IMMEDIATE ";
        } else if (value == IBeacon.PROXIMITY_NEAR) {
            return "NEAR";
        }
        return "UNKNOWN";
    }

}
