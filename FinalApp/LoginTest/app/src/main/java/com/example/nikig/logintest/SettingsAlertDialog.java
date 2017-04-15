package com.example.nikig.logintest;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Created by cconsi on 3/28/17.
 */

public class SettingsAlertDialog extends DialogFragment
{

    ToggleButton mapStyleToggle, mapZoomToggle, autoTextToggle;
    TextView mapStyleTV, mapZoomTV, autoTextTV;
    boolean isSatellite, zoomOut, autoTextContact;

    public static SettingsAlertDialog newInstance(boolean isSat, boolean zoomOut, boolean autoText)
    {
        SettingsAlertDialog dialog = new SettingsAlertDialog();
        Bundle args = new Bundle();
        args.putBoolean("isSat", isSat);
        args.putBoolean("zoomOut", zoomOut);
        args.putBoolean("autoText", autoText);
        dialog.setArguments(args);
        return dialog;
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */

    public interface settingsDialogListener {
        void onSettingsDialogPositiveClick(DialogFragment dialog, boolean isSatellite, boolean zoomOut, boolean autoText);
        void onSettingsDialogNegativeClick(DialogFragment dialog);
    }
    // Use this instance of the interface to deliver action events
    settingsDialogListener dialogListener;

    // Override the Fragment.onAttach() method to instantiate the DialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the settingsDialogListener so we can send events to the host
            dialogListener = (settingsDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement settingsDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v = inflater.inflate(R.layout.settings_dialog, null);
        builder.setView(v);

        isSatellite = getArguments().getBoolean("isSat");
        zoomOut = getArguments().getBoolean("zoomOut");
        autoTextContact = getArguments().getBoolean("autoText");

        mapStyleToggle = (ToggleButton) v.findViewById(R.id.mapStyleToggle);
        mapZoomToggle = (ToggleButton) v.findViewById(R.id.mapZoomToggle);
        autoTextToggle = (ToggleButton) v.findViewById(R.id.autoTextToggle);

        if(isSatellite) {
            mapStyleToggle.setChecked(true);
        }
        if(zoomOut) {
            mapZoomToggle.setChecked(true);
        }
        if(autoTextContact) {
            autoTextToggle.setChecked(true);
        }

        mapStyleTV = (TextView) v.findViewById(R.id.mapStyleTextView);
        mapStyleTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar sb = Snackbar.make(v, "This button toggles the style of the map " +
                        "from normal navigation mode to satellite mode.", Snackbar.LENGTH_LONG);
                sb.show();
            }
        });

        mapZoomTV = (TextView) v.findViewById(R.id.mapZoomTextView);
        mapZoomTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar sb = Snackbar.make(v, "This switch toggles between a 'near' and" +
                        "'far' zoom setting when the camera is locked.", Snackbar.LENGTH_LONG);
                sb.show();
            }
        });

        autoTextTV = (TextView) v.findViewById(R.id.autoTextTextView);
        autoTextTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar sb = Snackbar.make(v, "WARNING: Setting to 'NO' will prevent a " +
                        "text from automatically being sent to 911.", Snackbar.LENGTH_LONG);
                sb.show();
            }
        });

        builder.setMessage("Settings")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action accepted, interface with calling class
                        isSatellite = mapStyleToggle.isChecked();
                        zoomOut = mapZoomToggle.isChecked();
                        autoTextContact = autoTextToggle.isChecked();
                        dialogListener.onSettingsDialogPositiveClick(SettingsAlertDialog.this, isSatellite, zoomOut, autoTextContact);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
