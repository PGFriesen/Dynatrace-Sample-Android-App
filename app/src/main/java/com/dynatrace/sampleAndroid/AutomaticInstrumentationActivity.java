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
import com.dynatrace.android.agent.UserActionModifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class AutomaticInstrumentationActivity extends AppCompatActivity {

    private DynatraceTutorial davis;                    // Reference to Dynatrace Tutorial Class
    private ArrayList<String> listEndpoints;            // List of URLs that are available to choose from
    private int requestDelay = 0;                       // Delay in ms to add for requests
    private int requestCount = 1;                       // Number of requests to send for waterfall
    private Toaster toaster;                            // Helper class that displays toast messages (popups)
    private Tooltip tooltips;                           // Helper class that displays tooltip dialogs

    private static final Random RAND = new Random();    // Random Number Generator

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automatic_instrumentation);

        davis = new DynatraceTutorial(AutomaticInstrumentationActivity.this);
        toaster = new Toaster();
        tooltips = new Tooltip(AutomaticInstrumentationActivity.this);

        initializeSpinners();
        initializeSeekerBars();
    }

    /**
     * Click Listener for all buttons in this activity
     *
     * @param buttonView - View object for the button that was pressed
     */
    public void onButtonTouch(View buttonView){
        String toastMessage = "";

        // Switch statement checks the ID of the button that was pressed to determine which case to run
        switch(buttonView.getId()){
            case R.id.button_user_action: // Basic User Action button was clicked - Change text on buttom
                Dynatrace.modifyUserAction(new UserActionModifier() {
                    @Override
                    public void modify(ModifiableUserAction modifiableUserAction) {
                        modifiableUserAction.reportValue("Original User Action Name", modifiableUserAction.getActionName());
                        modifiableUserAction.setActionName("My new Custom Action");
                    }
                });
                toastMessage = "Basic User Action Created: 'Touch on " + ((Button) findViewById(R.id.button_user_action)).getText().toString() + "'";
                randomizeButtonText(buttonView);
                break;

            case R.id.button_send_web_request: // Send Web Request button was clicked - Send single web request to selected URL
                davis.singleWebRequest(((Spinner) findViewById(R.id.spinner_url)).getSelectedItem().toString(), requestDelay);
                toastMessage = "Sent Request to:\n" + ((Spinner) findViewById(R.id.spinner_url)).getSelectedItem().toString();
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
                tooltips.showInfoDialog(getResources().getString(R.string.title_user_action_monitoring),
                        getResources().getString(R.string.msg_about_user_actions));
                break;

            case R.id.button_about_web_requests: // Display tooltip
                tooltips.showInfoDialog(getResources().getString(R.string.title_web_request_monitoring),
                        getResources().getString(R.string.msg_about_web_requests));
                break;

            case R.id.button_about_crash_reporting: // Display tooltip
                tooltips.showInfoDialog(getResources().getString(R.string.title_crash_reporting),
                        getResources().getString(R.string.msg_about_crash_reporting));
                break;

            case R.id.button_about_lifecycle: // Display tooltip
                tooltips.showInfoDialog(getResources().getString(R.string.title_lifecycle_monitoring),
                        getResources().getString(R.string.msg_about_lifecycle));
                break;
        }

        // After code is executed, present a toast to the user to indicate what was done
        if (toastMessage.length() > 0){
            toaster.toast(AutomaticInstrumentationActivity.this, toastMessage, Toast.LENGTH_LONG);
        }
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
     * Present the User with a Dialog to enter in a URL that gets added to the list of endpoints
     * to choose from
     */
    private void addUrl(){
        tooltips.showUrlDialog("add URL to List of Endpoints", listEndpoints, (Spinner) findViewById(R.id.spinner_url));
    }

    /**
     * Initialize the spinner (dropdown menu) that contains the URLs
     */
    private void initializeSpinners(){
        // Initialize ArrayList of endpoints from predefined URL's in String Resources
        this.listEndpoints = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.url_list)));

        // Create the adapter using the list of endpoints and set the adapter for the Spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(AutomaticInstrumentationActivity.this, android.R.layout.simple_spinner_item, listEndpoints);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((Spinner) findViewById(R.id.spinner_url)).setAdapter(spinnerAdapter);
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