package com.example.todos.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements DialogInterface.OnClickListener, DialogEdition.Communicator, DialogCreate.Communicator, DialogFilter.Communicator{
	
	//Variable constante
    static String KEY_ID = "id";
    static String KEY_TITLE = "title";
    static String KEY_CONTENT = "content";
    static String KEY_PRIORITY = "priority";  
    static String KEY_DEADLINE = "deadline";
    static String KEY_STATUS = "status";
    static String KEY_CATEGORY = "category";

    //Variables
	Button addTodoButton, filter;
	ListView list;
    DatabaseHandler db;
    EditText inputSearch;
    boolean bool = true;
	
	List<HashMap<String,String>> todoCollection;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHandler(this);
        setViews();
        //construct the list
        buildCompleteList();
            
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

        inputSearch = (EditText) findViewById(R.id.search_input);
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String filter = String.valueOf(inputSearch.getText());
                buildSearchedLis(filter);
            }
             });

        filter = (Button) findViewById(R.id.filter_button);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showFilterDialog();
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

    public void showFilterDialog(){
        FragmentManager manager = getSupportFragmentManager();
        DialogFilter editionDialog = new DialogFilter();
        editionDialog.show(manager,"filterDialog");
    }

    public void showCreateDialog(int position, boolean bool){

        String title;
        String content;
        int priority, id;

        FragmentManager manager = getSupportFragmentManager();
        DialogCreate createDialog = new DialogCreate();

        if(bool){
            title = db.getTodo(position).getTodoTitle();
            content = db.getTodo(position).getTodoContent();
            id = db.getTodo(position).getID();


            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            bundle.putString("content", content);
            bundle.putInt("id", id);


            createDialog.setArguments(bundle);
            createDialog.show(manager,"createDialog");
        }else{
            createDialog.show(manager,"createDialog");
        }


    }

    public void buildSearchedLis(String _filter){
        List<ModelTodos> todos = db.getAllTodos();
        List<ModelTodos> todoFiltered = new ArrayList<ModelTodos>();;

        if(_filter != ""){
                for (ModelTodos todo : todos) {
                    if(todo.getTodoCategory().contains(_filter)){
                        todoFiltered.add(todo);
                    }else if(todo.getTodoTitle().contains(_filter)){
                        todoFiltered.add(todo);
                    }
                }

            buildList(todoFiltered);
            }
        else{
            buildCompleteList();
        }

    }

    public void buildFiltedList(String _filter, boolean reverse){
        List<ModelTodos> todos = db.getAllTodos();

        if(_filter.contains("alpha")){
            Collections.sort(todos, new Comparator<ModelTodos>() {
                public int compare(ModelTodos result1, ModelTodos result2) {
                    return result1.getTodoTitle().compareTo(result2.getTodoTitle());
                }
            });
        }else if(_filter.contains("category")){
            Collections.sort(todos, new Comparator<ModelTodos>() {
                public int compare(ModelTodos result1, ModelTodos result2) {
                    return result1.getTodoCategory().compareTo(result2.getTodoCategory());
                }
            });
        }else if(_filter.contains("date")){
            Collections.sort(todos, new Comparator<ModelTodos>() {
                public int compare(ModelTodos result1, ModelTodos result2) {
                    return result1.getTodoDeadline().compareTo(result2.getTodoDeadline());
                }
            });
        }else if(_filter.contains("priority")){
            Collections.sort(todos, new Comparator<ModelTodos>() {
                public int compare(ModelTodos result1, ModelTodos result2) {
                    return Integer.toString(result1.getPriority()).compareTo(Integer.toString(result2.getPriority()));
                }
            });
        }else{
           buildCompleteList();
            return;
        }

        if(reverse){
            Collections.reverse(todos);
        }
        buildList(todos);


    }

    public void buildList(List<ModelTodos> todos){
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
            map.put(KEY_STATUS, Integer.toString(todo.getStatusTodo()));

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
                intentDetail.putExtra("status", todoCollection.get(position).get(KEY_STATUS));
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

	public void buildCompleteList() {
    	List<ModelTodos> todos = db.getAllTodos();
        buildList(todos);
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
        buildCompleteList();
    }

    @Override
    public void onEditEntry(String message, int position) {
        showCreateDialog(position,true);
    }

    @Override
    public void doneTodo(String message, int position) {
        ModelTodos model = db.getTodo(position);
        int test =  model.getStatusTodo();
        Toast.makeText(this,"item updated" + model.getTodoTitle(),Toast.LENGTH_SHORT).show();
        model.setStatusTodo(test);
        buildCompleteList();
    }

    //METHODS OF THE DIALOG CREATE FRAGMENT
    @Override
    public void addTodoToDb(ModelTodos model) {

        db.addTodo(model);
        buildCompleteList();
    }

    @Override
    public void updateTodo(ModelTodos model) {

        String modelString = model.getTodoTitle();
        Toast.makeText(this,"item updated" + modelString,Toast.LENGTH_SHORT).show();
        buildCompleteList();
    }

    @Override
    public void reorderList(String string) {
        buildFiltedList(string,false);
    }
}
