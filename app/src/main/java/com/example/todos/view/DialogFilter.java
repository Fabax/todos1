package com.example.todos.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.todos.R;
import com.example.todos.model.DatabaseHandler;
import com.example.todos.model.ModelTodos;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by fabien on 23/01/2014.
 */
public class DialogFilter extends DialogFragment {

    Communicator communicator;


    public Dialog onCreateDialog(Bundle savedInstanceState){


        //Creation of the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View promptsView = inflater.inflate(R.layout.dialog_filtre, null);

        final RadioGroup todo_priority = (RadioGroup) promptsView.findViewById(R.id.radio_filtres);

        builder.setTitle("Selectionner filtre");
        builder.setView(promptsView);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String returnedString = "";
                        int radioButtonID = todo_priority.getCheckedRadioButtonId();
                        View radioButton = todo_priority.findViewById(radioButtonID);
                        int idx = todo_priority.indexOfChild(radioButton);

                        if(idx == 0){
                            returnedString = "alphabetique";
                        }else if(idx == 1){
                            returnedString = "category";
                        }else if(idx == 2){
                            returnedString = "priority";
                        }else if(idx == 3){
                            returnedString = "date";
                        }

                        communicator.reorderList(returnedString);
                    }
                });
        builder.setNegativeButton("Annuler",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        Dialog todoForm = builder.create();
        return todoForm;
    }

    public interface Communicator{
        public void reorderList(String string);
    }


    public void onAttach(Activity activity){
        super.onAttach(activity);
        communicator = (Communicator)activity;
    }

}
