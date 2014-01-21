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
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements DialogInterface.OnClickListener, DialogEdition.Communicator, DialogCreate.Communicator{
	
	//Variable constante
    static String KEY_ID = "id";
    static String KEY_TITLE = "title";
    static String KEY_CONTENT = "content";
    static String KEY_PRIORITY = "priority";  
    static String KEY_DEADLINE = "deadline";
    static String KEY_CATEGORY = "category";

    //Variables
	Button addTodoButton;
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
    	addTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showCreateDialog(1, false);
            }
        });
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

    public void showCreateDialog(int position, boolean bool){

        String title;
        String content;
        int priority;

        FragmentManager manager = getSupportFragmentManager();
        DialogCreate createDialog = new DialogCreate();

        if(bool){
            title = db.getTodo(position).getTodoTitle();
            content = db.getTodo(position).getTodoContent();
            priority = db.getTodo(position).getPriority();

            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            bundle.putString("content", content);
            bundle.putInt("priority", priority);

            Toast.makeText(this,"show create : " + title +" "+content +" "+ priority,Toast.LENGTH_SHORT).show();

            createDialog.setArguments(bundle);
            createDialog.show(manager,"createDialog");
        }else{
            createDialog.show(manager,"createDialog");
        }


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
            map.put(KEY_CATEGORY, todo.getTodoCategory());
			                                                                
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
				intentDetail.putExtra("category", todoCollection.get(position).get(KEY_CATEGORY));
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
        showCreateDialog(position,true);
        Toast.makeText(this,message + position,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void doneTodo(String message, int position) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        db.getTodo(position).setStatusTodo(0);
    }

    //METHODS OF THE DIALOG CREATE FRAGMENT
    @Override
    public void addTodoToDb(ModelTodos model) {
        String modelString = model.getTodoTitle();
        db.addTodo(model);
        buildList();
    }

    @Override
    public void updateTodo(ModelTodos model) {

        String modelString = model.getTodoTitle();
        Toast.makeText(this,"item updated" + modelString,Toast.LENGTH_SHORT).show();
        db.updateTodo(model);
        buildList();
    }
}
