package com.dynatrace.sampleAndroid;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Fragment class for controlling the user data layout in the Manual Instrumentation Activity
 */
public class UserDataFragment extends Fragment {

    private View view;
    private DynatraceTutorial davis;


    public UserDataFragment(DynatraceTutorial davis) {
        this.davis = davis;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_user_data, container, false);

        return view;
    }

    /**
     * Custom Click Listener to handle button touches for this fragment
     * @param buttonView the view object for the button that was clicked
     */
    public void onClickUserData(View buttonView){
        switch(buttonView.getId()){
            case R.id.button_apply_tag:
                davis.tagSession(((EditText)view.findViewById(R.id.text_user_tag)).getText().toString());
                break;
            case R.id.button_about_privacy_options:
                // TODO: Implement dialogue section
                break;
            case R.id.button_off:
                davis.setDataCollection(0);
                break;
            case R.id.button_performance:
                davis.setDataCollection(1);
                break;
            case R.id.button_user_behavior:
                davis.setDataCollection(2);
                break;
        }
    }
}