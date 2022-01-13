package com.dynatrace.sampleAndroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ManualInstrumentationActivity extends AppCompatActivity {

    private Fragment currentFragment;
    private boolean whichFragment; // true = SDK | false = user data
    private DynatraceTutorial davis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_instrumentation);

        this.davis = new DynatraceTutorial(ManualInstrumentationActivity.this);

        whichFragment = false;
        onReplaceFragment(findViewById(R.id.buttonFragmentSDK));
    }

//    /**
//     *
//     * @param view
//     */
//    public void onFragmentButton(View view) {
//
//    }

    /**
     * Click listener for buttons in the User Data & Privacy Fragment
     * @param view the button view object that was clicked
     */
    public void onClickUserData(View view){
        switch(view.getId()){

        }
    }

    /**
     * Click listener for buttons in the SDK Fragment
     * @param view the button view object that was clicked
     */
    public void onClickSdk(View view){
        switch(view.getId()){

        }
    }

    /**
     * When the user clicks on the button for the non-active fragment, do 2 things
     * 1. Switch out the fragment in the activity
     * 2. Update the buttons to reflect the current state of selected fragment
     *
     * @param selectedView the button that is clicked on to switch the fragment
     */
    public void onReplaceFragment(View selectedView){
        View deactivateView;

        // 1. Switch the fragments
        if (selectedView.getId() == R.id.buttonFragmentSDK){
            deactivateView = findViewById(R.id.buttonFragmentUserData);
            this.currentFragment = new ManualInstrumentationFragment(davis);
        } else {
            deactivateView = findViewById(R.id.buttonFragmentSDK);
            this.currentFragment = new UserDataFragment(davis);
        }

        // 2. Update the buttons
        selectedView.setBackgroundResource(R.drawable.button_teal_pressed);
        ((Button)selectedView).setTextColor(0xFFFFFFFF);
        ((Button)deactivateView).setEnabled(false);

        deactivateView.setBackgroundResource(R.drawable.button_white_unfocused);
        ((Button)deactivateView).setTextColor(0xFF000000);
        ((Button)deactivateView).setEnabled(true);

        // Flip the boolean value and commit the fragment change
        whichFragment = !whichFragment;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutFragment, currentFragment).commit();
    }

}