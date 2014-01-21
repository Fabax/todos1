package com.example.todos.view;

import com.example.todos.R;
import com.example.todos.model.DatabaseHandler;
import com.example.todos.model.ModelTodos;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class DialogEdition extends DialogFragment  {
	
	Button cancel, edit, remove, done;
	View currentView ;
    DatabaseHandler db;
    ModelTodos modelTodo;
    Communicator communicator;
    int position = 0;

    public void onAttach(Activity activity){
        super.onAttach(activity);
        communicator = (Communicator)activity;
    }


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        db = new DatabaseHandler(getActivity());
        Bundle bundle = this.getArguments();
        if(getArguments()!=null)
        {
            position = getArguments().getInt("position");
        }


		currentView = inflater.inflate(R.layout.dialog_manage, null);

		
		cancel = (Button) currentView.findViewById(R.id.dialog_manage_cancel);
		edit = (Button) currentView.findViewById(R.id.dialog_manage_editer);
		remove = (Button) currentView.findViewById(R.id.dialog_manage_delete);
		done = (Button) currentView.findViewById(R.id.dialog_manage_done);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dismiss();
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                communicator.onDeleteEntry("Entrée supprimé ", position);
                dismiss();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                communicator.onEditEntry("Edition de la todo ", position);
                dismiss();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                communicator.doneTodo("tache réalisée ", position);
                dismiss();
            }
        });

		return currentView;
	}

   public interface Communicator{
        public void onDeleteEntry(String message, int position);
        public void onEditEntry(String message, int position);
        public void doneTodo(String message, int position);
    }



}
