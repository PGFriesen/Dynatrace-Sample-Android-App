package com.dynatrace.sampleAndroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InstrumentationActivity extends AppCompatActivity {

    private Fragment currentFragment;
    private boolean whichFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrumentation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        onReplaceFragment(findViewById(R.id.buttonFragmentAuto));
    }


    /**
     * Click listener for any button pressed on the InstrumentationActivity
     *
     * Calls 'onFragmentButton' for whichever fragment is currently active
     *
     * @param view The view object of the button that was pressed
     */
    public void onFragmentButton(View view) {
        if (whichFragment) {
            ((AutoInstrumentationFragment) currentFragment).onFragmentButton(view);
            return;
        }
        ((ManualInstrumentationFragment) currentFragment).onFragmentButton(view);
    }

    /**
     * Custom click listener for the fragment-selection buttons
     *
     * @param view the view object for the button that was clicked (Automatic/Manual)
     */
    public void onReplaceFragment(View view){
        View otherView;
        if (view.getId() == R.id.buttonFragmentAuto){
            otherView = findViewById(R.id.buttonFragmentManual);
            this.currentFragment = new AutoInstrumentationFragment();
            whichFragment = true;
        } else {
            otherView = findViewById(R.id.buttonFragmentAuto);
//            this.currentFragment = new ManualInstrumentationFragment();
            whichFragment = false;
        }

        // Programatically set button colors
        view.setBackgroundResource(R.drawable.button_teal_pressed);
        ((Button)view).setTextColor(0xFFFFFFFF);
        otherView.setBackgroundResource(R.drawable.button_white_unfocused);
        ((Button)otherView).setTextColor(0xFF000000);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutFragment, currentFragment).commit();
    }
}