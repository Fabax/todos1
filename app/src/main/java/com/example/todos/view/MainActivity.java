package com.example.todos.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.example.todos.R;
import com.example.todos.model.DatabaseHandler;
import com.example.todos.model.ModelTodos;
import com.example.todos.controller.ListAdapter;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements View.OnClickListener,DialogInterface.OnClickListener, DialogEdition.Communicator{
	
	//Variable constante
    static String KEY_ID = "id";
    static String KEY_TITLE = "title";
    static String KEY_CONTENT = "content";
    static String KEY_PRIORITY = "priority";  
    static String KEY_DEADLINE = "deadline";       

	//Variables 
	Button addTodoButton;
	DatePicker deadLinePicker;
	EditText todo_title;
	EditText todo_message;
	String dateString;
	View promptsView;
	ListView list;
    DatabaseHandler db;
	
	List<HashMap<String,String>> todoCollection;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHandler(this);
        setViews();
        //construct the list
        buildList();
            
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void setViews(){
    	//main view
    	setContentView(R.layout.activity_main);
    	
    	//Buttons
    	addTodoButton = (Button) findViewById(R.id.addTodoButton);
    	addTodoButton.setOnClickListener(this);
    	list = (ListView) findViewById(R.id.listView1);			
    }

	public void showEditionDialog(int position){
		FragmentManager manager = getSupportFragmentManager();
        DialogEdition editionDialog = new DialogEdition();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        editionDialog.setArguments(bundle);
		editionDialog.show(manager,"editionDialog");
	}


	public void buildList() {
    	Log.v("constructList()", "constructList()");
    	List<ModelTodos> todos = db.getAllTodos();      
    	
    	
    	todoCollection = new ArrayList<HashMap<String,String>>();	                        
		HashMap<String,String> map = null;
			
		for (ModelTodos todo : todos) {
			map = new HashMap<String,String>(); 

            map.put(KEY_ID,Integer.toString(todo.getID()));
			map.put(KEY_TITLE, todo.getTodoTitle());                	                                 
			map.put(KEY_PRIORITY, Integer.toString(todo.getPriority()));
			map.put(KEY_CONTENT, todo.getTodoContent());
			map.put(KEY_DEADLINE, todo.getTodoDeadline());
			                                                                
            //Add to the Arraylist
			todoCollection.add(map);            
		}
		
		
		ListAdapter bindingData = new ListAdapter(this,todoCollection);						
		list.setAdapter(bindingData);

		// bind the click in the item
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intentDetail = new Intent(MainActivity.this, DetailTodo.class);
				intentDetail.putExtra("title", todoCollection.get(position).get(KEY_TITLE));
				//intentDetail.putExtra("category", todoCollection.get(position).get(KEY_CATEGORY));
				intentDetail.putExtra("priority", todoCollection.get(position).get(KEY_PRIORITY));
				intentDetail.putExtra("content", todoCollection.get(position).get(KEY_CONTENT));
				intentDetail.putExtra("deadline", todoCollection.get(position).get(KEY_DEADLINE));
				startActivity(intentDetail);
			}
		});
		
		list.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int position, long id) {
                int i = Integer.parseInt(todoCollection.get(position).get(KEY_ID));
            	showEditionDialog(i);
            	return true;
            }
        }); 
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// init inflated view
		LayoutInflater li = LayoutInflater.from(this);
		promptsView = li.inflate(R.layout.todo_form, null);
		//init dialog alert
		AlertDialog.Builder todoForm = new AlertDialog.Builder(this);
		// set prompts.xml to alertdialog builder
		todoForm.setView(promptsView);
		
		//form field text
		todo_title = (EditText) promptsView.findViewById(R.id.todo_name_input);
		todo_message = (EditText) promptsView.findViewById(R.id.todo_message_input);
		
		todoForm.setCancelable(false)
		.setPositiveButton("OK",
				  new DialogInterface.OnClickListener() {
				    @Override
					public void onClick(DialogInterface dialog,int id) {

				    	//radio button
				    	RadioGroup todo_priority = (RadioGroup)promptsView.findViewById( R.id.todo_priority_input );
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
						dateString =  (dateFormat.format(dateFromDatePicker));
						//----- fin date picker
						
				        ModelTodos model = new ModelTodos(idx, todo_title.getText().toString(), todo_message.getText().toString(),dateString); 
				        db.addTodo(model);
				        buildList();
				    }
				  })
				.setNegativeButton("Cancel",
				  new DialogInterface.OnClickListener() {
				    @Override
					public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				    }
				  }).create();

			// show it
			todoForm.show();
		
	}
	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}

    //METHODS OF THE DIALOG EDITION FRAGMENT
    @Override
    public void onDeleteEntry(String message, int position) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        db.deleteTodo(db.getTodo(position));
        buildList();
    }

    @Override
    public void onEditEntry(String message, int position) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void doneTodo(String message, int position) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        db.getTodo(position).setStatusTodo(0);
    }
}
