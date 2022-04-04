package com.dynatrace.sampleAndroid;

import android.content.Context;
import android.util.Log;
import com.dynatrace.android.agent.DTXAction;
import com.dynatrace.android.agent.Dynatrace;
import com.dynatrace.android.agent.conf.DataCollectionLevel;
import com.dynatrace.android.agent.conf.UserPrivacyOptions;
import com.dynatrace.android.api.Configuration;
import com.dynatrace.android.api.DynatraceSessionReplay;
import com.dynatrace.android.api.privacy.MaskingConfiguration;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DynatraceTutorial {

    private OkHttpClient client;

    // Data Collection Levels
    private final int OFF = 0;
    private final int PERFORMANCE = 1;
    private final int USER_BEHAVIOR = 2;

    private static final Random RAND = new Random();

    /**
     * Constructor
     * @param c context for
     */
    public DynatraceTutorial(Context c){
        this.client = new OkHttpClient();
    }

    /**
     * Send web request(s) in a new thread with an optional induced delay
     * @param url - Endpoint where request is made to
     * @param requestDelay - Delay to wait before request is fired off
     */
    public void singleWebRequest(String url, int requestDelay) {
        // Create a new thread for Web Request - Networking not allowed on main thread

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    // Sleep thread for specified delay in ms
                    Thread.sleep(requestDelay);

                    // Create the Request object with the URL
                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    // Send the request
                    Response response = client.newCall(request).execute();

                    // Handle the response
                    if (response.isSuccessful()){
                        Log.i("WEB_REQUEST", "Web request to " + url + " was successful: " + response.message());
                    }
                    else {
                        Log.i("WEB_REQUEST", "Web request to " + url + " failed: " + response.message());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }});
        thread.start();
    }

    /**
     * Send several web requests as the result of a single button click
     * @param numberOfRequests the number of requests to make
     */
    public void waterfallRequests(int numberOfRequests, ArrayList<String> endpoints) {

        for (int i = 0; i < numberOfRequests; i++){
            String url = endpoints.get(RAND.nextInt(endpoints.size())); // Randomly select a URL
            int delay = (RAND.nextInt(6) * 50); // Randomly select a delay between 0-300ms
            singleWebRequest(url, delay); // send the request to url after delay ms
        }

    }

    /**
     * Crash the application with an unhandled exception
     *
     * "Wait. That's illegal"
     */
    public void crashApplication(){
        System.out.println(2/0);
    }


    /**
     * *********************************************************************************************
     * All of the below methods are used for Manually Instrumenting with the SDK
     * *********************************************************************************************
     */

    /**
     * Create a custom user action
     * @param actionName string name to use for the custom action
     */
    public DTXAction createCustomAction(String actionName){
        DTXAction customAction = null;

        customAction = Dynatrace.enterAction(actionName);

        return customAction;
    }

    /**
     * Close out a custom user action
     * @param action the user action to be closed
     */
    public void closeCustomAction(DTXAction action){

    }

    /**
     * Create a custom child action
     * @param parentAction the parent action of the child action to be created
     */
    public DTXAction createChildAction(DTXAction parentAction){
        DTXAction childAction = null;

        return childAction;
    }

    /**
     * Tag the user session with the SDK
     * @param userTag string to use for the tag
     */
    public void tagSession(String userTag){
        Dynatrace.identifyUser(userTag);
    }


    /**
     * Web Request to be manually instrumented with Dynatrace SDK
     * @param url endpoint to send request to
     */
    public void manualWebRequest(String url, DTXAction webRequestAction) {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {

                // Create the Request object with the URL and add our header tag
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                try {

                    // Send the request
                    Response response = client.newCall(request).execute();

                    if (response.isSuccessful()){
                        // Handle response
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }});
        thread.start();
    }


    /**
     * Report events for a user action
     * @param userAction action to report events to
     */
    public void reportEvent(DTXAction userAction){
        String event = "String value to report as a standalone event";
    }


    /**
     * Report values for a user action
     * @param userAction action to report values to
     */
    public void reportValue(DTXAction userAction) {
        int randomInteger = RAND.nextInt(); // use a randomly generated integer to report a value
    }


    /**
     * Throw an malformed URL exception and report the error
     * @param userAction action to report error to
     */
    public void reportError(DTXAction userAction){
        try {
            URL url = new URL("httpSUPERSECRET::::::://////");
        } catch (MalformedURLException m) {
            m.printStackTrace();

        }
    }


    /**
     * Set the data collection level for the current session.
     * @param level the integer value of the new level to use
     */
    public void setDataCollection(int level){

        DataCollectionLevel newLevel = DataCollectionLevel.USER_BEHAVIOR;
        boolean crashReporting = false;
        boolean replayOnCrashes = false;

        MaskingConfiguration config = new MaskingConfiguration.Safest();  // .Safe or .Custom

        switch(level){
            case OFF:
                newLevel = DataCollectionLevel.OFF;
                break;
            case PERFORMANCE:
                newLevel = DataCollectionLevel.PERFORMANCE;
                crashReporting = true;

                config = new MaskingConfiguration.Safest();  // .Safe or .Custom
                replayOnCrashes = true;
                break;
            case USER_BEHAVIOR:
                newLevel = DataCollectionLevel.USER_BEHAVIOR;
                crashReporting = true;

                config = new MaskingConfiguration.Safe();  // .Safest or .Custom
                replayOnCrashes = true;
                break;
        }

        Dynatrace.applyUserPrivacyOptions(UserPrivacyOptions.builder()
                .withDataCollectionLevel(newLevel)
                .withCrashReportingOptedIn(crashReporting)
                .withCrashReplayOptedIn(replayOnCrashes)
                .build());

        DynatraceSessionReplay.setConfiguration(Configuration.builder()
                .withMaskingConfiguration(config)
                .build());
    }
}














