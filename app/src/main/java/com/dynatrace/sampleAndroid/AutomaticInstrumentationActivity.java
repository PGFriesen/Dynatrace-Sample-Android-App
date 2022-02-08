package com.dynatrace.sampleAndroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class AutomaticInstrumentationActivity extends AppCompatActivity {

    private ArrayList<String> listEndpoints;
    private DynatraceTutorial davis;    // Reference to Dynatrace Tutorial Class
    private int requestDelay = 0;       // Delay to add for requests
    private int requestCount = 1;       // Number of requests to send for waterfall
    private Spinner spinnerUrls;        // Reference to spinner for URLs
    private String selectedUrl;         // URL selected by spinner for web requests
    private Toaster toaster;            // Used for presenting a Toast notification to user
    private TooltipHelper tooltips;     // Reference to tooltip class for creating dialog windows


    private static final Random RAND = new Random(); // Random Number Generator

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
    public void onButtonTouch(View buttonView){
        String toastMessage = "";

        switch(buttonView.getId()){
            case R.id.buttonUserAction:
                randomizeButtonText(buttonView);
                toastMessage = "Basic User Action Created";
                break;

            case R.id.buttonWebRequest:
                davis.singleWebRequest(selectedUrl, requestDelay);
                toastMessage = "Sent Request to:\n" + selectedUrl;
                break;

            case R.id.buttonWebRequestWaterfall:
                davis.waterfallRequests(requestCount, listEndpoints);
                toastMessage = "Sent " + String.valueOf(requestCount) + " requests";
                break;

            case R.id.buttonAddUrl:
                toastMessage = "Url Added to list of endpoints";
                addUrl();
                break;

            case R.id.buttonCrash:
                davis.crashApplication();
                break;

            case R.id.buttonAboutUserActions:
                showTooltip("automatic_user_action");
                break;

            case R.id.buttonAboutWebRequests:
                showTooltip("automatic_web_request");
                break;

            case R.id.buttonAboutCrashReporting:
                showTooltip("automatic_crash_reporting");
                break;

            case R.id.buttonAboutLifecycle:
                showTooltip("automatic_lifecycle");
                break;

        }

        // Display a toast for the users
        toaster.toast(AutomaticInstrumentationActivity.this, toastMessage, Toast.LENGTH_LONG);
    }



    /**
     * Helper class to show dialog windows for tooltips
     * @param tag the tag to use to get the right tooltip
     */
    private void showTooltip(String tag){
        Pair<String, String> tooltip = tooltips.getTooltip(tag);

        AlertDialog.Builder builder = new AlertDialog.Builder(AutomaticInstrumentationActivity.this);
        builder.setTitle(tooltip.first);
        builder.setMessage(tooltip.second);

        builder.show();
    }

    /**
     * Randomize the text on the basic user action button to highlight how user action names
     * are determined
     * @param buttonView the view object for the button that was pressed
     */
    private void randomizeButtonText(View buttonView){
        ArrayList<String> listTexts = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.user_action_names)));

        String newText = listTexts.get(RAND.nextInt(listTexts.size()));

        ((Button) buttonView).setText(newText);
    }

    /**
     * '+ Add Endpoint' button is pressed
     * Add a URL to the list of URL's to make requests to
     */
    private void addUrl(){
        // Create the Alert Dialog and listeners for adding URLs to spinner
        AlertDialog.Builder addUrlDialog = new AlertDialog.Builder(AutomaticInstrumentationActivity.this);

        // Create the EditText to use for entering in a new URL and set the dialog with it
        EditText newUrl = new EditText(AutomaticInstrumentationActivity.this);
        addUrlDialog.setView(newUrl);

        // Set the listeners for the dialog buttons "Add URL" and "Cancel"
        addUrlDialog.setPositiveButton("Add URL", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){
                listEndpoints.add(newUrl.getText().toString());
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(AutomaticInstrumentationActivity.this, android.R.layout.simple_spinner_item, listEndpoints);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerUrls.setAdapter(spinnerAdapter);
            }
        });
        addUrlDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do nothing
            }
        });

        addUrlDialog.show();
    }

    /**
     * Init function for declaring member variables and setting up any UI components
     */
    private void initializeActivity(){
        // Set private member variables
        this.davis = new DynatraceTutorial(this);
        this.toaster = new Toaster();
        this.tooltips = new TooltipHelper();
        initializeSpinners();
        initializeSeekerBars();
    }

    /**
     * Initialize the spinner that contains the URL's
     */
    private void initializeSpinners(){
        // Spinner for URL selection
        this.spinnerUrls = (Spinner) findViewById(R.id.spinnerUrl); // Reference to Spinner view
        spinnerUrls.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                // Update the URL reference when a new item is selected
                selectedUrl = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> adapterView) { /* Auto-Generated */ }
        });

        this.listEndpoints = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.url_list)));
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(AutomaticInstrumentationActivity.this, android.R.layout.simple_spinner_item, listEndpoints);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUrls.setAdapter(spinnerAdapter);

        // Spinner for User Action Click Listeners
//        Spinner spinnerUserActionListeners = (Spinner) findViewById(R.id.spinnerListeners);
//        spinnerUserActionListeners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
//                // Update the sensor reference when a new sensor type is selected
//                selectedSensor = parent.getItemAtPosition(position).toString();
//            }
//            public void onNothingSelected(AdapterView<?> adapterView) { /* Auto-Generated */ }
//        });
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