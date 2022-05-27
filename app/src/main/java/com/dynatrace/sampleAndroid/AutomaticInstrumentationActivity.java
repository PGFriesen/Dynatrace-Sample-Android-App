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

import com.dynatrace.android.agent.Dynatrace;
import com.dynatrace.android.agent.ModifiableUserAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class AutomaticInstrumentationActivity extends AppCompatActivity {

    private ArrayList<String> listEndpoints;            // List of URLs that are available to choose from
    private DynatraceTutorial davis;                    // Reference to Dynatrace Tutorial Class
    private int requestDelay = 0;                       // Delay to add for requests
    private int requestCount = 1;                       // Number of requests to send for waterfall
    private Spinner spinnerUrls;                        // Reference to spinner for URLs
    private String selectedUrl;                         // URL selected by spinner for web requests
    private Toaster toaster;                            // Used for presenting a Toast notification to user

    private static final Random RAND = new Random();    // Random Number Generator

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
            case R.id.button_user_action: // Basic User Action button was clicked - Change text on buttom
                toastMessage = "Basic User Action Created: 'Touch on " + ((Button) findViewById(R.id.button_user_action)).getText().toString() + "'";
                randomizeButtonText(buttonView);
                break;

            case R.id.button_send_web_request: // Send Web Request button was clicked - Send single web request to selected URL
                davis.singleWebRequest(selectedUrl, requestDelay);
                toastMessage = "Sent Request to:\n" + selectedUrl;
                break;

            case R.id.button_web_request_waterfall: // Waterfall Request button was clicked - Send multiple requests randomly to any URL in list
                davis.waterfallRequests(requestCount, listEndpoints);
                toastMessage = "Sent " + String.valueOf(requestCount) + " requests";
                break;

            case R.id.button_add_url: // Add Endpoint button was clicked - Present Dialogue with text to add a URL to list
                toastMessage = "Url Added to list of endpoints";
                addUrl();
                break;

            case R.id.button_crash_application: // Crash Application was pressed - Crash the application
                davis.crashApplication();
                break;

            case R.id.button_about_user_actions: // Display tooltip
                showTooltip(getResources().getString(R.string.title_user_action_monitoring),
                        getResources().getString(R.string.msg_about_user_actions));
                break;

            case R.id.button_about_web_requests: // Display tooltip
                showTooltip(getResources().getString(R.string.title_web_request_monitoring),
                        getResources().getString(R.string.msg_about_web_requests));
                break;

            case R.id.button_about_crash_reporting: // Display tooltip
                showTooltip(getResources().getString(R.string.title_crash_reporting),
                        getResources().getString(R.string.msg_about_crash_reporting));
                break;

            case R.id.button_about_lifecycle: // Display tooltip
                showTooltip(getResources().getString(R.string.title_lifecycle_monitoring),
                        getResources().getString(R.string.msg_about_lifecycle));
                break;
        }

        // After code is executed, present a toast to the user to indicate what was done
        if (toastMessage.length() > 0){
            toaster.toast(AutomaticInstrumentationActivity.this, toastMessage, Toast.LENGTH_LONG);
        }
    }

    /**
     * Helper class to show dialog windows for tooltips
     * @param title - title to display for dialog window
     * @param message - message that dialog will display
     */
    private void showTooltip(String title, String message){
        // Create the dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(AutomaticInstrumentationActivity.this);

        // Set the message and title
        builder.setTitle(title);
        builder.setMessage(message);

        // Set a "Ok" button that does nothing
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){
                dialog.dismiss();
            }
        });

        builder.show();
    }


    /**
     * Randomize the text on the basic user action button when pressed
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AutomaticInstrumentationActivity.this);
        dialogBuilder.setTitle("Add URL to List of Endpoints");

        // Create the EditText to use for entering in a new URL and set the dialog with it
        EditText editTextNewUrl = new EditText(AutomaticInstrumentationActivity.this);
        dialogBuilder.setView(editTextNewUrl);

        // Set the listeners for the "Add URL" dialog button to confirm that addition of a new URL to the list
        dialogBuilder.setPositiveButton("Add URL", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){
                listEndpoints.add(editTextNewUrl.getText().toString());
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(AutomaticInstrumentationActivity.this, android.R.layout.simple_spinner_item, listEndpoints);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerUrls.setAdapter(spinnerAdapter);
            }
        });

        // Set the listener for "Cancel" dialog button - do nothing when the user cancels adding a new URL
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        // display the dialog
        dialogBuilder.create().show();
    }


    /**
     * Init function for declaring member variables and setting up UI components
     */
    private void initializeActivity(){
        this.davis = new DynatraceTutorial(this);
        this.toaster = new Toaster();

        initializeSpinners();
        initializeSeekerBars();
    }


    /**
     * Initialize the spinner (dropdown menu) that contains the URLs
     */
    private void initializeSpinners(){
        // Initialize ArrayList of endpoints from predefined URL's in String Resources
        this.listEndpoints = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.url_list)));

        // Get the reference to the spinner view from the Activity
        this.spinnerUrls = (Spinner) findViewById(R.id.spinner_url);

        // Create the adapter using the list of endpoints and set the adapter for the Spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(AutomaticInstrumentationActivity.this, android.R.layout.simple_spinner_item, listEndpoints);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUrls.setAdapter(spinnerAdapter);

        // Create the click listener for the spinner when an item is selected
        spinnerUrls.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                // Update the URL reference when a new item is selected
                selectedUrl = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> adapterView) { /* Auto-Generated */ }
        });
    }


    /**
     * Initialize the seeker bars used for request delays and number of requests
     */
    private void initializeSeekerBars(){
        // Configure the requestDelay slider for values between 0 - 5,000 ms and increment by 250
        SeekBar seekBarRequestDelay = (SeekBar) findViewById(R.id.seekbar_request_delay);
        seekBarRequestDelay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int stepSize = 250;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = (progress/stepSize)*stepSize;
                seekBar.setProgress(progress);
                ((TextView) findViewById(R.id.text_request_delay)).setText(String.valueOf(progress) + " ms");
                requestDelay = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { /* Auto-Generated */ }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { /* Auto-Generated */ }
        });

        // Configure the requestCount slider for values between 1-15 requests increment by 1
        SeekBar seekBarRequestCount = (SeekBar) findViewById(R.id.seekbar_request_count);
        seekBarRequestCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int stepSize = 1;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = (progress/stepSize)*stepSize;
                seekBar.setProgress(progress);
                ((TextView) findViewById(R.id.text_request_count)).setText(String.valueOf(progress) + " request(s)");
                requestCount = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { /* Auto-Generated */ }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { /* Auto-Generated */ }
        });
    }
}