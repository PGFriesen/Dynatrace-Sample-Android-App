package com.dynatrace.sampleAndroid;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DynatraceTutorial {

    private Context c;
    private OkHttpClient client;
    private Toaster toaster;

    /**
     * List of methods provided by this class
     * --------------------------------------
     * handleException - Catch a malformed URL Exception
     * crashApplication - Crash application
     * singleWebRequest - Make a web request
     */

    public DynatraceTutorial(Context context){
        this.c = context;
        this.client = new OkHttpClient();
        this.toaster = new Toaster();
    }

    /**
     * Throw an malformed URL exception
     */
    public void handleException(){
        try {
            URL url = new URL("httpSUPERSECRET::::::://////");
        } catch (MalformedURLException m) {
            m.printStackTrace();
        }
    }

    /**
     * Crash the application with an unhandled exception
     */
    public void crashApplication(){
        System.out.println(2/0);
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
                    // Sleep thread for specified delay
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
                    toaster.toast(c, "Exception occurred: " + e.toString(), Toast.LENGTH_LONG);
                }

            }});
        thread.start();
    }


}
