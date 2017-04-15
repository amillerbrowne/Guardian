package com.example.nikig.logintest;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by cconsi on 3/31/17.
 */

public class CreateEContactDialog extends DialogFragment {

    EditText nameEditText, numberEditText;
    String contactName, contactPhone;

    public interface contactDialogListener {
        void onContactDialogPositiveClick(DialogFragment dialog, String name, String number);
        void onContactDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    CreateEContactDialog.contactDialogListener dialogListener;

    // Override the Fragment.onAttach() method to instantiate the DialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the settingsDialogListener so we can send events to the host
            dialogListener = (CreateEContactDialog.contactDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement contactDialogListener");
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
        View v = inflater.inflate(R.layout.contact_dialog, null);
        builder.setView(v);

        nameEditText = (EditText) v.findViewById(R.id.contactName);
        numberEditText = (EditText) v.findViewById(R.id.phoneNo);

        builder.setTitle("Enter Emergency Contact Info");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogListener.onContactDialogPositiveClick(CreateEContactDialog.this,
                        nameEditText.getText().toString(), numberEditText.getText().toString());
            }
        });

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }
}
