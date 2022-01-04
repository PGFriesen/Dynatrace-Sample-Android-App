package com.dynatrace.sampleAndroid;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
     *
     * @param view
     */
    public void onButtonTouch(View view) {
        Intent intent;
        switch(view.getId()){

            // "About" button is pressed
            case R.id.buttonAbout:
                tooltipHelper.showDialog(getSupportFragmentManager(), "about");

            // "Apply Tag" button is pressed
            case R.id.buttonTagUser:
                tagSession(((EditText) findViewById(R.id.textUserTag)).getText().toString());

            // "Instrumentation Sandbox" is pressed - InstrumentationActivity is started
            case R.id.relativeLayoutInstrumentation:
                intent = new Intent(view.getContext(), InstrumentationActivity.class);
                startActivity(intent);
                break;

            // "Concepts and Troubleshooting" is pressed - ConceptsActivity is started
            case R.id.relativeLayoutConcepts:
                intent = new Intent(view.getContext(), ConceptsActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * Check that the user has entered a tag into the field and use it to
     * tag the session with the Dynatrace SDK
     */
    private void tagSession(String tag) {
        if (tag.length() > 0) {

            // Add the code here to tag the user


            toasterHelper.toast(MainActivity.this,"Functionality incomplete" + tag, Toast.LENGTH_LONG);
//            toasterHelper.toast(MainActivity.this,"Session successfully tagged as: " + tag, Toast.LENGTH_LONG);
        }
        else {
            toasterHelper.toast( MainActivity.this,"User tag cannot be empty, please enter a tag", Toast.LENGTH_SHORT);
        }
    }
}