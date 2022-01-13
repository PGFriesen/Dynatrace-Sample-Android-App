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
     * @param view
     */
    public void onClickUserData(View view){
        switch(view.getId()){
            case R.id.button_apply_tag:
            case R.id.button_about_privacy_options:
            case R.id.button_off:
            case R.id.button_performance:
            case R.id.button_user_behavior:
                davis.tagSession(((EditText)view.findViewById(R.id.text_user_tag)).getText().toString());
        }
    }
}