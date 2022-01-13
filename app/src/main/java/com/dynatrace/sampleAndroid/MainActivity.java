package com.dynatrace.sampleAndroid;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dynatrace.android.agent.Dynatrace;
import com.dynatrace.android.agent.conf.DataCollectionLevel;
import com.dynatrace.android.agent.conf.UserPrivacyOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TooltipHelper tooltipHelper;
    private Toaster toasterHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.tooltipHelper = new TooltipHelper();
        this.toasterHelper = new Toaster();
        getSupportActionBar().setTitle("MainActivity");

    }

    /**
     * Custom Click Listener that receives button clicks and determines which code should be
     * executed
     *
     * @param view the view object for the button that was pressed
     */
    public void onButtonTouch(View view) {
        Intent intent;
        switch(view.getId()){

            // "About" button is pressed
            case R.id.buttonAbout:
                tooltipHelper.showDialog(getSupportFragmentManager(), "about");

            // "Instrumentation Sandbox" is pressed - InstrumentationActivity is started
            case R.id.relativeLayoutAutomaticInstrumentation:
                intent = new Intent(view.getContext(), AutomaticInstrumentationActivity.class);
                startActivity(intent);
                break;

            // "Concepts and Troubleshooting" is pressed - ConceptsActivity is started
            case R.id.relativeLayoutManualInstrumentation:
                intent = new Intent(view.getContext(), ManualInstrumentationActivity.class);
                startActivity(intent);
                break;
        }
    }

}