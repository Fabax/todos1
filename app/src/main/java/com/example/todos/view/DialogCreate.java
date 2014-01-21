package com.example.todos.view;

import com.example.todos.R;
import com.example.todos.model.DatabaseHandler;
import com.example.todos.model.ModelTodos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class DialogCreate extends DialogFragment  {

    DatabaseHandler db;
    Communicator communicator;
    int position = 0;
    String title,content;
    int priority;
    DatePicker deadLinePicker;
    EditText todo_title;
    EditText todo_message;
    String dateString;
    boolean update;


    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Get deb
        db = new DatabaseHandler(getActivity());

        //Creation of the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View promptsView = inflater.inflate(R.layout.todo_form, null);

        //form field text
        todo_title = (EditText) promptsView.findViewById(R.id.todo_name_input);
        todo_message = (EditText) promptsView.findViewById(R.id.todo_message_input);

        //radio button
        RadioGroup todo_priority = (RadioGroup) promptsView.findViewById(R.id.todo_priority_input);
        int radioButtonID = todo_priority.getCheckedRadioButtonId();
        View radioButton = todo_priority.findViewById(radioButtonID);
        final int idx = todo_priority.indexOfChild(radioButton);
        // ---- fin ration button

        //DATE PICKER
        deadLinePicker = (DatePicker) promptsView.findViewById(R.id.todo_dead_line_input);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.set(deadLinePicker.getYear(), deadLinePicker.getMonth() + 1, deadLinePicker.getDayOfMonth());
        Date dateFromDatePicker = calendar.getTime();
        dateString = (dateFormat.format(dateFromDatePicker));


        //Get BUNDLE
        final Bundle bundle = this.getArguments();
        if(getArguments()!=null){
            title = getArguments().getString("title");
            content = getArguments().getString("content");
            priority = getArguments().getInt("priority");

            Toast.makeText(getActivity(),title,Toast.LENGTH_SHORT).show();

            todo_title.setText(title);
            todo_message.setText(content);
            todo_priority.check(priority);

            update = true;
        }else{
            update = false;
        }
        //----- fin date picker

        builder.setView(promptsView);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        if(update){
                            ModelTodos model = new ModelTodos(idx, todo_title.getText().toString(), todo_message.getText().toString(), dateString);
                            communicator.updateTodo(model);

                        }else{
                            ModelTodos model = new ModelTodos(idx, todo_title.getText().toString(), todo_message.getText().toString(), dateString);
                            communicator.addTodoToDb(model);
                        }


                    }
                });
        builder.setNegativeButton("Cancel",
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
        public void addTodoToDb(ModelTodos model);
        public void updateTodo(ModelTodos model);
    }


    public void onAttach(Activity activity){
        super.onAttach(activity);
        communicator = (Communicator)activity;
    }



}
