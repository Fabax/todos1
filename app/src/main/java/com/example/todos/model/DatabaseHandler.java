package com.example.todos.model;

import java.util.ArrayList;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.todos.view.MainActivity;

public class DatabaseHandler extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 4;
 
    // Database Name
    private static final String DATABASE_NAME = "todos_db";
 
    // Contacts table name
    private static final String TABLE_TODOS = "todos";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_PRIORITY = "priority";
    private static final String KEY_STATUS = "status";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_DATE = "deadline";
    private static final String KEY_CATEGORY = "category";
    
    private static final String CREATE_TODOS_TABLE = "CREATE TABLE IF NOT EXISTS " 
    		+ TABLE_TODOS + "("
            + KEY_ID + " integer primary key autoincrement," 
    		+ KEY_PRIORITY + " INTEGER,"
            + KEY_STATUS + " INTEGER," 
    		+ KEY_TITLE + " TEXT,"
    		+ KEY_DATE + " DATETIME,"
            + KEY_CONTENT + " TEXT,"
            + KEY_CATEGORY + " TEXT )";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.v("DatabaseHandler","DatabaseHandler()");
        Log.v("DatabaseHandler",CREATE_TODOS_TABLE);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v("DatabaseHandler onCreate",CREATE_TODOS_TABLE);
        db.execSQL(CREATE_TODOS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOS);
 
        // Create tables again
        onCreate(db);
    }

	    // Adding new contact
	public void addTodo(ModelTodos modelTodos) {
		SQLiteDatabase db = this.getWritableDatabase();
		
	    ContentValues values = new ContentValues();
	    values.put(KEY_PRIORITY, modelTodos.getPriority()); 
	    values.put(KEY_STATUS, modelTodos.getStatusTodo()); 
	    values.put(KEY_TITLE, modelTodos.getTodoTitle()); 
	    values.put(KEY_DATE, modelTodos.getTodoDeadline()); 
	    values.put(KEY_CONTENT, modelTodos.getTodoContent());
        values.put(KEY_CATEGORY, modelTodos.getTodoCategory());


	    Log.v("add todo", values.toString());

	    // Inserting Row
	    if (db == null) {
	        Log.v("custom error","db is not set");
	     }else{
	    	 db.insert(TABLE_TODOS, "nullColumnHack", values);
	     }
	    
	    db.close(); // Closing database connection,

	}
	 
	// Getting single ModelTodos
	public ModelTodos getTodo(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

	    Cursor cursor = db.query(TABLE_TODOS, new String[] { 
	    	KEY_ID, KEY_PRIORITY, KEY_STATUS,KEY_TITLE,KEY_CONTENT,KEY_CATEGORY }, KEY_ID + "=?",
	        new String[] { String.valueOf(id) }, null, null, null, null);
	    
	    if (cursor != null){
	        cursor.moveToFirst();
	    }
	 
	    ModelTodos modelTodos = new ModelTodos(Integer.parseInt(cursor.getString(0)),
                Integer.parseInt(cursor.getString(1)), cursor.getString(2),cursor.getString(3),cursor.getString(4));
	    // return contact
	    return modelTodos;
	}
	 
	// Getting All ModelTodoss
	public List<ModelTodos> getAllTodos() {
		 List<ModelTodos> todoList = new ArrayList<ModelTodos>();
	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_TODOS;
 
    	SQLiteDatabase db = this.getWritableDatabase();
    	Cursor cursor = db.rawQuery(selectQuery, null);
 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	            ModelTodos todos = new ModelTodos();
	            todos.setID(Integer.parseInt(cursor.getString(0)));
	            todos.setPriority(Integer.parseInt(cursor.getString(1)));
	            todos.setStatusTodo(Integer.parseInt(cursor.getString(2)));
	            todos.setTodoTitle(cursor.getString(3));
	            todos.setTodoDeadline(cursor.getString(4));
	            todos.setTodoContent(cursor.getString(5));
                todos.setTodoCategory(cursor.getString(6));
	            // Adding todos to list
	            todoList.add(todos);
	        } while (cursor.moveToNext());
	    }
	    // return contact list
	    return todoList;
	}
	 
	// Getting ModelTodoss Count
	public int getTodosCount() {
		String countQuery = "SELECT  * FROM " + TABLE_TODOS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
	}

	// Updating single ModelTodos
	public int updateTodo(ModelTodos modelTodos) {
		SQLiteDatabase db = this.getWritableDatabase();
 
	    ContentValues values = new ContentValues();
	    values.put(KEY_PRIORITY, modelTodos.getPriority()); 
	    values.put(KEY_STATUS, modelTodos.getStatusTodo()); 
	    values.put(KEY_TITLE, modelTodos.getTodoTitle()); 
	    values.put(KEY_CONTENT, modelTodos.getTodoContent());
        values.put(KEY_CATEGORY, modelTodos.getTodoCategory());

        // updating row
	    return db.update(TABLE_TODOS, values, KEY_ID + " = ?",
            new String[] { String.valueOf(modelTodos.getID()) });
	}
	 
	// Deleting single ModelTodos
	public void deleteTodo(ModelTodos modelTodos) {
		SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_TODOS, KEY_ID + " = ?",
	    new String[] { String.valueOf(modelTodos.getID()) });
	    db.close();
	}
}