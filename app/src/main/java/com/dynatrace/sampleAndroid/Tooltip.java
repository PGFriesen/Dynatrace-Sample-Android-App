package com.dynatrace.sampleAndroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class Tooltip {

    private static final String OK = "Ok";
    private static final String CANCEL = "Cancel";
    private static final String ADD_URL = "Add Url";

    private Context context;


    public Tooltip(Context c){
        this.context = c;
    }

    /**
     * Display a simple dialog with a title and message with a button
     * to dismiss the dialog
     *
     * @param title - Title for the dialog
     * @param message - Message for the dialog
     */
    public void showInfoDialog(String title, String message){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        dialog.setTitle(title);
        dialog.setMessage(message);

        dialog.setPositiveButton(OK, new DialogInterface.OnClickListener(){
           @Override
           public void onClick(DialogInterface d, int which){
               d.dismiss();
           }
        });

        dialog.create().show();
    }

    /**
     * @param title
     * @param endpoints
     * @param spinner
     */
    public void showUrlDialog(String title, ArrayList<String> endpoints, Spinner spinner){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);

        EditText textAddUrl = new EditText(context);
        dialog.setView(textAddUrl);

        dialog.setPositiveButton(ADD_URL, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface d, int which){
                endpoints.add(textAddUrl.getText().toString());

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, endpoints);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setAdapter(adapter);
            }
        });

        dialog.setNegativeButton(CANCEL, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int which) {
                d.dismiss();
            }
        });

        dialog.create().show();

    }
}
