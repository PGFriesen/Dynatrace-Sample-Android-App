package com.dynatrace.sampleAndroid;

import android.content.Context;
import android.widget.Toast;

public class Toaster {
    private Toast currentToast;

    /**
     * Spell Roast once
     * Say "Roast" once
     *
     * Spell Roast twice
     * Say "Roast" twice
     *
     * What goes into a toaster?
     * If you said "toast", then it's probably burnt, and if you said "bread" you'd be right any other time except now
     *
     * @param c - context for the current activity
     * @param message - The string message to display to the user
     * @param duration - How long the toast message should be displayed for
     */
    public void toast(Context c, String message, int duration){
        if (currentToast != null){
            currentToast.cancel();
        }
        currentToast = Toast.makeText(c, message, duration);
        currentToast.show();
    }
}
