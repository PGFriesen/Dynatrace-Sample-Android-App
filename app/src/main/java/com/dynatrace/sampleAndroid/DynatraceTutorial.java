package com.dynatrace.sampleAndroid;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dynatrace.android.agent.DTXAction;
import com.dynatrace.android.agent.Dynatrace;
import com.dynatrace.android.agent.conf.DataCollectionLevel;
import com.dynatrace.android.agent.conf.UserPrivacyOptions;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DynatraceTutorial {

    private Context context;
    private OkHttpClient client;
//    private Toaster toaster;

    // Data Collection Levels
    private final int OFF = 0;
    private final int PERFORMANCE = 1;
    private final int USER_BEHAVIOR = 2;

    private static final Random RAND = new Random();

    /**
     * List of methods provided by this class
     * --------------------------------------
     * handleException - Catch a malformed URL Exception
     * crashApplication - Crash application
     * singleWebRequest - Make a web request
     */

    public DynatraceTutorial(Context c){
        this.context = c;
        this.client = new OkHttpClient();
    }

    /**
     * The only different between them is that onOptionItemSelected() is a direct method
     * from the Activity class which gives you direct access to the selected MenuItem.
     *
     * On the other hand, setOnMenuItemClickListener() is a method that allows you to register
     * probably a custom listener that will be notified whenever one of the MenuItem is clicked,
     * and the listener must implement the interface Toolbar.onMenuItemClickListener().
     * @param sensor
     */
    public void handleClickListener(String sensor){
        switch(sensor){
            case "android.view.View$OnClickListener": // Basic Button
                break;
            case "android.app.Activity.onOptionsItemSelected": // Option menu at top of activity
                break;
            case "android.view.MenuItem$OnMenuItemClickListener":
                break;

            case "android.widget.AdapterView$OnItemClickListener":
                break;
            case "android.widget.AdapterView$OnItemSelectedListener":
                break;

        }
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
    public void waterfallRequests(int numberOfRequests) {
        String [] urls = context.getResources().getStringArray(R.array.url_list);

        for (int i = 0; i < numberOfRequests; i++){
            String url = urls[RAND.nextInt(urls.length)]; // Randomly select a URL
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
     * Create a custom user action
     * @param actionName string name to use for the custom action
     */
    public void customUserAction(String actionName){

    }

    /**
     * Web Request to be manually instrumented with Dynatrace SDK
     * @param url endpoint to send request to
     */
    public void manualWebRequest(String url) {
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

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }});
        thread.start();
    }

    /**
     * Report values and events for a user action
     * @param userAction action to report values and events to
     */
    public void reportWithSDK(DTXAction userAction) {

        handleException(userAction);
    }

    /**
     * Throw an malformed URL exception
     * @param userAction action to report error to
     */
    public void handleException(DTXAction userAction){
        try {
            URL url = new URL("httpSUPERSECRET::::::://////");
        } catch (MalformedURLException m) {
            m.printStackTrace();

            // Report the error for the given userAction

        }
    }


    /**
     * Tag the user session with the SDK
     * @param userTag string to use for the tag
     */
    public void tagSession(String userTag){

    }


    /**
     * Set the data collection level for the current session.
     * @param level the integer value of the new level to use
     */
    public void setDataCollection(int level){

//        DataCollectionLevel newLevel = DataCollectionLevel.USER_BEHAVIOR;
//        switch(level){
//            case OFF:
//                newLevel = DataCollectionLevel.OFF;
//                break;
//            case PERFORMANCE:
//                newLevel = DataCollectionLevel.PERFORMANCE;
//                break;
//            case USER_BEHAVIOR:
//                newLevel = DataCollectionLevel.USER_BEHAVIOR;
//                break;
//        }

        // Use the SDK to Apply the newLevel for data collection

    }
}














