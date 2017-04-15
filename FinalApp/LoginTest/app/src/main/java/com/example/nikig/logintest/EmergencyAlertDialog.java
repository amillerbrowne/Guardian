package com.example.nikig.logintest;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by cconsi on 3/29/17.
 */

public class EmergencyAlertDialog extends DialogFragment
{
    public interface emergencyDialogListener {
        void onEmergencyDialog911Click(DialogFragment dialog);
        void onEmergencyDialogPersonalContactClick(DialogFragment dialog);
        void onEmergencyDialogCancelClick(DialogFragment dialog);
    }

    // create an instance of our listener
    emergencyDialogListener dialogListener;

    // Override the Fragment.onAttach() method to instantiate the DialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the settingsDialogListener so we can send events to the host
            dialogListener = (EmergencyAlertDialog.emergencyDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement settingsDialogListener");
        }
    }

    /** Because of the way AlertDialog positions its buttons, we are going to define the
     * 'negative' button as the 'emergency contact only' trigger, and the
     * 'neutral' button, as the 'cancel' trigger, so that the positioning makes sense.
     * once passed back to MapActivity, it shouldn't be a problem because it will be handled
     * inside this class, and passed by name back to the calling activity.
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v = inflater.inflate(R.layout.emergency_dialog,null);
        builder.setView(v);

        //builder.setTitle("Confirm Emergency Trigger?");
        //builder.setMessage("Confirm Emergency Alert to 911 or Emergency Contact.");
        builder.setPositiveButton("911", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogListener.onEmergencyDialog911Click(EmergencyAlertDialog.this);
            }
        });

        // says neutral button but we are using as cancel
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do nothing, dismiss dialog
                dialog.dismiss();
            }
        });

        // says negative but we are using as the 'emergency contact only' button
        builder.setNegativeButton("Personal Contact", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogListener.onEmergencyDialogPersonalContactClick(EmergencyAlertDialog.this);
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
