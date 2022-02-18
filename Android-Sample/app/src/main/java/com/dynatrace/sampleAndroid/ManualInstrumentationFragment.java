package com.dynatrace.sampleAndroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import com.dynatrace.android.agent.DTXAction;
import java.util.ArrayList;
import java.util.Arrays;


public class ManualInstrumentationFragment extends Fragment {

    private ArrayList<DTXAction> listChildrenActions;   // List of child actions that are open
    private ArrayList<String> listEndpoints;            // List of URLs available for spinner
    private ArrayList<View> listButtonViews;            // List of button view objects for fragment
    private DTXAction parentAction;                     // Reference to current open custom action
    private String selectedUrl;                         // Reference to URL selected by spinner
    private Spinner spinner;                            // Reference to spinner object for URLs
    private View view;                                  // View reference for fragment
    private final Toaster toaster;                      // Reference to toaster for displaying toasts
    private final DynatraceTutorial davis;              // Reference to Dynatrace tutorial class
    private TooltipHelper tooltips;


    // Constructor - takes reference to DynatraceTutorial class
    public ManualInstrumentationFragment(DynatraceTutorial davis, Toaster toaster, TooltipHelper tooltips){
        this.davis = davis;
        this.tooltips = tooltips;
        this.toaster = toaster;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_instrumentation_manual, container, false);

        initializeFragment();

        return view;
    }

    /**
     * Click listener that handles logic when a given button is pressed to then execute the respective
     * methods
     *
     * @param buttonView the view object for the button that was clicked
     */
    public void onClickSdk(View buttonView){
        String toastMessage = "";

        switch(buttonView.getId()){
            case R.id.button_create_action:
                onCreateAction();
                updateButtonStates(buttonView);
                toastMessage = "Custom User Action created with name: " + ((EditText)view.findViewById(R.id.text_user_action_name)).getText().toString();
                break;
            case R.id.button_close_action:
                onCloseAction();
                updateButtonStates(buttonView);
                toastMessage = "Action closed";
                break;
            case R.id.button_create_child_action:
                onCreateChildAction();
                updateButtonStates(buttonView);
                toastMessage = "Created Child Action";
                break;
            case R.id.button_close_child_action:
                onCloseChildAction();
                updateButtonStates(buttonView);
                toastMessage = "Closed most recently opened child action";
                break;
            case R.id.button_send_web_request:
                onWebRequest();
                toastMessage = "Web request made to endpoint: " + selectedUrl;
                break;
            case R.id.button_report_value:
                onReportValue();
                toastMessage = "Reported value for custom action";
                break;
            case R.id.button_report_event:
                onReportEvent();
                toastMessage = "Reported event for custom action";
                break;
            case R.id.button_report_error:
                onReportError();
                toastMessage = "Reported error for custom action";
                break;
            case R.id.button_add_url:
                onAddUrl();
                break;
            case R.id.button_about_custom_events:
                tooltips.showDialog(getParentFragmentManager(), "About Custom Actions");
                break;
        }

        toaster.toast(view.getContext(),toastMessage, Toast.LENGTH_LONG);
    }

    /** 'Enter Action' button is pressed
    Manually Create a User Action with provided name */
    private void onCreateAction(){
        String userActionName = ((EditText) view.findViewById(R.id.text_user_action_name)).getText().toString();

        // If a zero-length string was provided, give it a basic custom action name
        if (userActionName.length() <= 0){
            userActionName = "Custom User Action";
        }

        parentAction = davis.createCustomAction(userActionName);
    }


    /** 'Leave Action' button is pressed
    Manually leave the currently open action and clean up references */
    private void onCloseAction(){
        davis.closeCustomAction(parentAction);

        // Clear the reference to the parentAction and the list of children actions
        parentAction = null;
        listChildrenActions.clear();
    }


    /** 'Create Child Action' button is pressed
    Add a child action to the open parent action */
    private void onCreateChildAction(){
        DTXAction childAction = davis.createChildAction(parentAction);

        // Add the child action to the list and increment the counter
        listChildrenActions.add(childAction);
    }


    /** 'Close Child Action' button is pressed
    Leave the most recently added child action, then remove it from the list */
    private void onCloseChildAction(){
        davis.closeCustomAction(listChildrenActions.get(listChildrenActions.size() - 1));
        listChildrenActions.remove(listChildrenActions.size() - 1);
    }


    /** 'Web Request' button is pressed
    Manually tag a web request to be associated with the currently open action and time it */
    private void onWebRequest() {
        davis.manualWebRequest(selectedUrl, parentAction);
    }


    /** 'Event' button is pressed
    Report an event for the parent action */
    private void onReportEvent(){
        davis.reportEvent(parentAction);
    }


    /** 'Value' button is pressed
    Report a value for the parent action */
    private void onReportValue(){
        davis.reportValue(parentAction);
    }


    /** 'Error' button is pressed
    Report a handled error for the parent action */
    private void onReportError(){
        davis.reportError(parentAction);
    }


    /**
     * '+ Add Endpoint' button is pressed
     * Add a URL to the list of URL's to make requests to
     */
    private void onAddUrl(){
        // Create the Alert Dialog and listeners for adding URLs to spinner
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        // Create the EditText to use for entering in a new URL and set the dialog with it
        EditText newUrl = new EditText(view.getContext());
        builder.setView(newUrl);

        // Set the listeners for the dialog buttons "Add URL" and "Cancel"
        builder.setPositiveButton("Add URL", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){
                listEndpoints.add(newUrl.getText().toString());
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, listEndpoints);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerAdapter);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do nothing
            }
        });

        builder.show();
    }


    /**
     * Initialize variable references for fragment in activity
     */
    private void initializeFragment(){
        // Initialize array for children actions
        this.listChildrenActions = new ArrayList<DTXAction>();

        // Store buttons that could change state into an array for easier usage
        this.listButtonViews = new ArrayList<View>();
        listButtonViews.add(view.findViewById(R.id.button_create_action));
        listButtonViews.add(view.findViewById(R.id.button_close_action));
        listButtonViews.add(view.findViewById(R.id.button_create_child_action));
        listButtonViews.add(view.findViewById(R.id.button_close_child_action));
        listButtonViews.add(view.findViewById(R.id.button_send_web_request));
        listButtonViews.add(view.findViewById(R.id.button_add_url));
        listButtonViews.add(view.findViewById(R.id.button_report_value));
        listButtonViews.add(view.findViewById(R.id.button_report_event));
        listButtonViews.add(view.findViewById(R.id.button_report_error));
        listButtonViews.add(view.findViewById(R.id.text_user_action_name));

        // Initialize spinner for URLs
        this.spinner = view.findViewById(R.id.spinnerUrl);
        this.listEndpoints = new ArrayList<String>(Arrays.asList(view.getContext().getResources().getStringArray(R.array.url_list)));
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, listEndpoints);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                // Update the URL reference when a new item is selected
                selectedUrl = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> adapterView) { /* Auto-Generated */ }
        });
    }


    /**
     * Any time the user clicks a button, we need to check all other buttons in the fragment
     * and update them accordingly as only certain buttons should be enabled at any given time
     *
     * @param buttonView The view object for the button that was clicked
     */
    private void updateButtonStates(View buttonView){

        // Loop through all buttons to compare with the button that was clicked
        for (View v : listButtonViews){

            // There are only 4 buttons that have an effect on other buttons in the fragment when clicked
            // create_action - create_child_action - leave_action - leave_child_action
            switch(buttonView.getId()) {

                // All buttons should be enabled except "Create Action" and "Create child action"
                case R.id.button_create_action:
                    if (v.getId() != buttonView.getId()
                            && v.getId() != R.id.button_close_child_action
                            && v.getId() != R.id.text_user_action_name){
                        v.setEnabled(true);
                    } else {
                        v.setEnabled(false);
                    }
                    break;

                // Only "Create Action" should be enabled
                case R.id.button_close_action:
                    if (v.getId() == R.id.button_create_action || v.getId() == R.id.text_user_action_name){
                        v.setEnabled(true);
                    } else {
                        v.setEnabled(false);
                    }
                    break;

                // Enable the "Close Child Action" button
                case R.id.button_create_child_action:
                    if (v.getId() == R.id.button_close_child_action){
                        v.setEnabled(true);
                    }
                    break;

                // Disable "Close child actions" if there are no open child actions
                case R.id.button_close_child_action:
                    if (v.getId() == R.id.button_close_child_action && listChildrenActions.size() == 0){
                        v.setEnabled(false);
                    }
                    break;
            }
        }
    }

}

