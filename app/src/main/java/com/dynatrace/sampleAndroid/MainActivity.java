package com.dynatrace.sampleAndroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.dynatrace.android.agent.Dynatrace;
import com.dynatrace.android.agent.conf.DataCollectionLevel;
import com.dynatrace.android.agent.conf.UserPrivacyOptions;
import com.dynatrace.android.api.Configuration;
import com.dynatrace.android.api.DynatraceSessionReplay;
import com.dynatrace.android.api.privacy.MaskingConfiguration;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("MainActivity");
    }

    /**
     * Start a new Activity using the intent created for Automatic or Manual Instrumentation Activity
     * Called when the user clicks on one of two main layouts
     *
     * @param view The Button (or Layout) that was clicked to start the new Activity
     */
    public void onStartActivity(View view){
        Intent intent;
        switch(view.getId()){
            case R.id.relative_layout_automatic_instrumentation: // Launch Automatic Instrumentation Activity
                intent = new Intent(getApplicationContext(), AutomaticInstrumentationActivity.class);
                startActivity(intent);
                break;
            case R.id.relative_layout_manual_instrumentation: // Launch Manual Instrumentation Activity
                intent = new Intent(getApplicationContext(), ManualInstrumentationActivity.class);
                startActivity(intent);
                break;
        }
    }


    /**
     * Display a dialog window with a quick message about the application
     * Called when the user clicks on "About the Application"
     *
     * @param view The view object for the "About the application" button
     */
    public void onAboutApplication(View view){
        AlertDialog.Builder dialogWindow = new AlertDialog.Builder(MainActivity.this);

        dialogWindow.setTitle(getResources().getString(R.string.title_about_application));
        dialogWindow.setMessage(getResources().getString(R.string.msg_about_application));

        dialogWindow.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialogWindow.show(); // Display the dialogue window
    }

    /**
     * Display a dialog window with options for DataPrivacy and User Tagging
     * Called when the user clicks on Data Privacy button
     *
     * @param view The view object for the "Data Privacy" button
     */
    public void onDataPrivacyPopup(View view){
        // Create the dialog and set the view as dialog_data_privacy.xml
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_data_privacy);

        // Get the User Privacy Options to determine what the current settings are
        UserPrivacyOptions privacyOptions = Dynatrace.getUserPrivacyOptions();

        // Configure the Button View for User Tagging
        configureUserTagging(dialog);

        // Configure the DataCollectionLevel Switches
        configureDataCollectionLevel(dialog, privacyOptions);

        // Configure Session Replay Switches
        configureSessionReplay(dialog, privacyOptions);

        // Configure tooltips for "How it works" buttons
        configureTooltips(dialog);

        // Apply config changes with Privacy Options builder
        Button applyChanges = (Button) dialog.findViewById(R.id.button_apply_changes);
        applyChanges.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

                // Determine which switch is toggled on for the newLevel
                DataCollectionLevel newLevel = DataCollectionLevel.USER_BEHAVIOR;
                if(((Switch)(dialog.findViewById(R.id.switch_performance))).isChecked()){
                    newLevel = DataCollectionLevel.PERFORMANCE;
                } else if (((Switch)(dialog.findViewById(R.id.switch_off))).isChecked()){
                    newLevel = DataCollectionLevel.OFF;
                }

                // Determine if SessionReplay is toggled on or not
                boolean sessionReplayEnabled = false;
                if(((Switch)(dialog.findViewById(R.id.switch_enable_session_replay))).isChecked()){
                    sessionReplayEnabled = true;
                }

                // Determine if masking is toggled for safe or safest
                MaskingConfiguration newMaskingLevel = new MaskingConfiguration.Safest();
                if (((Switch) dialog.findViewById(R.id.switch_safe)).isChecked()){
                    newMaskingLevel = new MaskingConfiguration.Safe();

                }

                // Apply privacy options and set state for sessionReplay
                Dynatrace.applyUserPrivacyOptions(UserPrivacyOptions.builder()
                        .withDataCollectionLevel(newLevel)
                        .withCrashReportingOptedIn(true)
                        .withCrashReplayOptedIn(sessionReplayEnabled)
                        .build()
                );

                // Apply masking configs
                DynatraceSessionReplay.setConfiguration(Configuration.builder()
                        .withMaskingConfiguration(newMaskingLevel)
                        .build());

                dialog.dismiss();
            }
        });

        // Dismiss dialog if user clicks "cancel"
        Button cancel = (Button) dialog.findViewById(R.id.button_cancel);
        cancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * Set the click listener on the Tag User Button that applies the specified tag if its length
     * is greater than 0
     *
     * @param dialog Dialog Object for the xml layout "dialog_data_privacy.xml"
     */
    private void configureUserTagging(Dialog dialog){
        Button buttonTagUser = (Button) dialog.findViewById(R.id.button_tag_user);
        buttonTagUser.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                String userTag = ((EditText) dialog.findViewById(R.id.text_user_tag)).getText().toString();
                if (userTag.length() > 0){
                    Dynatrace.identifyUser(userTag);
                }
            }
        });
    }

    /**
     * Configure the initial state for the three DataCollectionLevelSwitches
     *
     * Only one can be enabled at any time, and we determine the initial state from current privacy
     * options, and we set a click listener on all three switches such that any time one of them is
     * turned on, the others are turned off
     *
     * @param dialog Dialog Object for the xml layout "dialog_data_privacy.xml"
     * @param options The User Privacy Options object to determine current DataCollectionLevel config
     */
    private void configureDataCollectionLevel(Dialog dialog, UserPrivacyOptions options){
        Switch switchUserBehavior = (Switch) dialog.findViewById(R.id.switch_user_behavior);
        Switch switchPerformance = (Switch) dialog.findViewById(R.id.switch_performance);
        Switch switchOff = (Switch) dialog.findViewById(R.id.switch_off);

        // Initial State for the Switches determined by current privacy options
        switch(options.getDataCollectionLevel()){
            case USER_BEHAVIOR:
                switchUserBehavior.setChecked(true);
                break;
            case PERFORMANCE:
                switchPerformance.setChecked(true);
                break;
            case OFF:
                switchOff.setChecked(true);
                break;
        }

        // Create a click listener for the Switches to get called when any dataCollectionLevel Switches are toggled
        CompoundButton.OnCheckedChangeListener switchListener = new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){

                if (isChecked) {
                    // Set all Switches as "un-checked"
                    switchUserBehavior.setChecked(false);
                    switchPerformance.setChecked(false);
                    switchOff.setChecked(false);

                    // Check the switch that was clicked
                    buttonView.setChecked(true);
                }
            }
        };

        // Set the click listener on DataCollectionLevel Switches
        switchUserBehavior.setOnCheckedChangeListener(switchListener);
        switchPerformance.setOnCheckedChangeListener(switchListener);
        switchOff.setOnCheckedChangeListener(switchListener);
    }

    /**
     * Configure the switches that control masking and whether or not session replay is enabled
     *
     * @param dialog The dialog object for the XML Layout View
     * @param options The Privacy Options object
     */
    private void configureSessionReplay(Dialog dialog, UserPrivacyOptions options){
        // Configure Switches for Session Replay and Masking
        Switch switchSessionReplayToggle = (Switch) dialog.findViewById(R.id.switch_enable_session_replay);
        Switch switchMaskingSafest = (Switch) dialog.findViewById(R.id.switch_safest);
        Switch switchMaskingSafe = (Switch) dialog.findViewById(R.id.switch_safe);

        // Set initial state for switches
        if (options.isCrashReplayOptedIn()){
            switchSessionReplayToggle.setChecked(true);
            switchMaskingSafest.setChecked(true);
        }

        // Create click listener for switches
        CompoundButton.OnCheckedChangeListener switchListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Switch otherSwitch = switchMaskingSafe;
                    if (buttonView.getText().toString().equals("Masking: Safe")){
                        otherSwitch = switchMaskingSafest;
                    }

                    otherSwitch.setChecked(false);
                    buttonView.setChecked(true);
                }
            }
        };

        switchMaskingSafest.setOnCheckedChangeListener(switchListener);
        switchMaskingSafe.setOnCheckedChangeListener(switchListener);
    }

    private void configureTooltips(Dialog dialog){
        Button buttonTaggingTooltip = dialog.findViewById(R.id.button_about_user_tagging);
        buttonTaggingTooltip.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogWindow = new AlertDialog.Builder(dialog.getContext());
                dialogWindow.setTitle(getResources().getString(R.string.title_user_tagging));
                dialogWindow.setMessage(getResources().getString(R.string.msg_user_tagging));

                dialogWindow.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialogWindow.show(); // Display the dialogue window
            }
        });

        Button buttonDataCollectionTooltip = dialog.findViewById(R.id.button_about_data_collection);
        buttonDataCollectionTooltip.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogWindow = new AlertDialog.Builder(dialog.getContext());
                dialogWindow.setTitle(getResources().getString(R.string.title_data_collection_level));
                dialogWindow.setMessage(getResources().getString(R.string.msg_data_collection_level));

                dialogWindow.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialogWindow.show(); // Display the dialogue window
            }
        });

        Button buttonSessionReplayTooltip = dialog.findViewById(R.id.button_about_session_replay);
        buttonSessionReplayTooltip.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogWindow = new AlertDialog.Builder(dialog.getContext());
                dialogWindow.setTitle(getResources().getString(R.string.title_session_replay));
                dialogWindow.setMessage(getResources().getString(R.string.msg_session_replay));

                dialogWindow.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialogWindow.show(); // Display the dialogue window
            }
        });

    }

}