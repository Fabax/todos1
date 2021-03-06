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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class DialogCreate extends DialogFragment  {

    DatabaseHandler db;
    Communicator communicator;
    String category;
    int  idTodo;
    DatePicker deadLinePicker;
    EditText todo_title;
    EditText todo_message;
    String dateString;
    boolean update;
    Spinner spinner;



    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Get deb
        db = new DatabaseHandler(getActivity());



        //Creation of the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View promptsView = inflater.inflate(R.layout.todo_form, null);

        //Category
        spinner = (Spinner) promptsView.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //form field text
        todo_title = (EditText) promptsView.findViewById(R.id.todo_name_input);
        todo_message = (EditText) promptsView.findViewById(R.id.todo_message_input);


        final RadioGroup todo_priority = (RadioGroup) promptsView.findViewById(R.id.todo_priority_input);
        // ---- fin ration button

        //DATE PICKER
        deadLinePicker = (DatePicker) promptsView.findViewById(R.id.todo_dead_line_input);
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        final Calendar calendar = Calendar.getInstance();



        //Get BUNDLE
        final Bundle bundle = this.getArguments();
        if(getArguments()!=null){
            idTodo = getArguments().getInt("id");
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
                        //radio button
                        int radioButtonID = todo_priority.getCheckedRadioButtonId();
                        View radioButton = todo_priority.findViewById(radioButtonID);
                        int priority = todo_priority.indexOfChild(radioButton);

                        //spinner
                        category = spinner.getSelectedItem().toString();

                        //date picker
                        calendar.set(deadLinePicker.getYear(), deadLinePicker.getMonth() + 1, deadLinePicker.getDayOfMonth());
                        Date dateFromDatePicker = calendar.getTime();
                        dateString = (dateFormat.format(dateFromDatePicker));


                        if(update){
                            ModelTodos model = db.getTodo(idTodo);

                            model.setTodoTitle(todo_title.getText().toString());
                            model.setTodoCategory(category);
                            model.setPriority(priority);
                            model.setTodoDeadline(dateString);
                            model.setStatusTodo(1);



                            db.updateTodo(model);
                            communicator.updateTodo(model);
                        }else{
                            ModelTodos model = new ModelTodos(priority, todo_title.getText().toString(), todo_message.getText().toString(), dateString, category);
                            communicator.addTodoToDb(model);

                        }


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
        public void addTodoToDb(ModelTodos model);
        public void updateTodo(ModelTodos model);
    }


    public void onAttach(Activity activity){
        super.onAttach(activity);
        communicator = (Communicator)activity;
    }



}
