package com.dynatrace.sampleAndroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("MainActivity");
        setClickListeners();
    }


    /**
     * Programmatically set the click listeners for the buttons in the MainActivity
     */
    private void setClickListeners() {

        /**
         * Launch the Automatic Instrumentation Activity
         */
        RelativeLayout automaticInstrumentation = (RelativeLayout) findViewById(R.id.relative_layout_automatic_instrumentation);
        automaticInstrumentation.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), AutomaticInstrumentationActivity.class);
                startActivity(intent);
            }
        });


        /**
         * Launch the Manual Instrumentation Activity
         */
        RelativeLayout manualInstrumentation = (RelativeLayout) findViewById(R.id.relative_layout_manual_instrumentation);
        manualInstrumentation.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), ManualInstrumentationActivity.class);
                startActivity(intent);
            }
        });


        /**
         * Display a dialogue window when a user clicks on "About this app"
         */
        Button buttonAbout = (Button) findViewById(R.id.button_about);
        buttonAbout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle(getResources().getString(R.string.title_about_application));
                builder.setMessage(getResources().getString(R.string.msg_about_application));

                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();
            }
        });
    }
}