package com.airportvip.app;

import android.content.Context;
import android.graphics.Typeface;
import android.content.Context;
import android.graphics.Typeface;

public class AVIPFont {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    private final Context context;
    private static AVIPFont instance;

    /** CLASS FUNCTIONALITY ____________________________________________________________________ **/

    private AVIPFont(Context context) {
        this.context = context;
    }

    public static AVIPFont getInstance(Context context) {
        synchronized (AVIPFont.class) {
            if (instance == null)
                instance = new AVIPFont(context);
            return instance;
        }
    }

    // getTypeFace(): Retrieves the custom font family from resources.
    public Typeface getTypeFace() {
        return Typeface.createFromAsset(context.getResources().getAssets(),
                "fonts/big_noodle_titling.ttf");
    }
}
