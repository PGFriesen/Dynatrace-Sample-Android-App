package com.dynatrace.sampleAndroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class AutomaticInstrumentationActivity extends AppCompatActivity {

    // Helpers
    private DynatraceTutorial davis; // Tutorial class that handles functionality
    private Toaster toaster; // Used for presenting a Toast notification to user

    private String selectedUrl; // URL selected by spinner for web requests
    private String selectedSensor; // Sensor selected by spinner for user actions

    private int requestDelay = 0; // Delay to add for requests
    private int requestCount = 1; // Number of requests to send for waterfall


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automatic_instrumentation);

        initializeActivity();
    }


    /**
     * Click Listener for all buttons in this activity with switch statement to handle logic
     * for the button that was touched
     */
    public void onButtonTouch(View button){
        String toastMessage = "";

        switch(button.getId()){
            case R.id.buttonUserAction:
                davis.handleClickListener(selectedSensor);
                toastMessage = "Basic User Action Created using " + selectedSensor + " sensor";
                break;

            case R.id.buttonWebRequest:
                davis.singleWebRequest(selectedUrl, requestDelay);
                toastMessage = "Sent Request to:\n" + selectedUrl;
                break;

            case R.id.buttonWebRequestWaterfall:
                davis.waterfallRequests(requestCount);
                toastMessage = "Sent " + String.valueOf(requestCount) + " requests";
                break;

            case R.id.buttonAddUrl:
                // TODO: Add dialog to add a URL to list
                break;

            case R.id.buttonCrash:
                davis.crashApplication();
                break;

        }

        // Display a toast for the users
        toaster.toast(AutomaticInstrumentationActivity.this, toastMessage, Toast.LENGTH_LONG);
    }

    /**
     * Init function for declaring member variables and setting up any UI components
     */
    private void initializeActivity(){
        // Set private member variables
        this.davis = new DynatraceTutorial(this);
        this.toaster = new Toaster();

        initializeSpinners();
        initializeSeekerBars();
    }

    /**
     * Initialize the spinner that contains the URL's
     */
    private void initializeSpinners(){
        // Spinner for URL selection
        Spinner spinnerURLs = (Spinner) findViewById(R.id.spinnerUrl); // Reference to Spinner view
        spinnerURLs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                // Update the URL reference when a new item is selected
                selectedUrl = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> adapterView) { /* Auto-Generated */ }
        });

        // Spinner for User Action Click Listeners
        Spinner spinnerUserActionListeners = (Spinner) findViewById(R.id.spinnerListeners);
        spinnerUserActionListeners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                // Update the sensor reference when a new sensor type is selected
                selectedSensor = parent.getItemAtPosition(position).toString();
               // TODO: Create the views for each listener and then add logic here to switch them out
            }
            public void onNothingSelected(AdapterView<?> adapterView) { /* Auto-Generated */ }
        });
    }

    /**
     * Initialize the seeker bars used for request delays and number of requests
     */
    private void initializeSeekerBars(){
        // Configure the requestDelay slider for values between 0 - 5,000 ms and increment by 250
        SeekBar requestDelaySeeker = (SeekBar) findViewById(R.id.seekbarRequestDelay);
        requestDelaySeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int stepSize = 250;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = (progress/stepSize)*stepSize;
                seekBar.setProgress(progress);
                ((TextView) findViewById(R.id.textViewDelayIndicator)).setText(String.valueOf(progress) + " ms");
                requestDelay = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { /* Auto-Generated */ }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { /* Auto-Generated */ }
        });

        // Configure the requestCount slider for values between 1-15 requests increment by 1
        SeekBar requestCountSeeker = (SeekBar) findViewById(R.id.seekbarRequestCount);
        requestCountSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int stepSize = 1;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = (progress/stepSize)*stepSize;
                seekBar.setProgress(progress);
                ((TextView) findViewById(R.id.textViewRequestCountIndicator)).setText(String.valueOf(progress) + " request(s)");
                requestCount = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { /* Auto-Generated */ }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { /* Auto-Generated */ }
        });
    }
}