package com.example.todos.view;

import com.example.todos.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DetailTodo extends Activity {
	private TextView titleTodo;
	private TextView contentTodo;
	private TextView deadlineTodo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setViews();
		
        Intent i = getIntent();
        // Receiving the Data
        String title = i.getStringExtra("title");
        String content = i.getStringExtra("content");
        String deadline = i.getStringExtra("deadline");
         
        // Displaying Received data
        titleTodo.setText(title);
        contentTodo.setText(content);
        deadlineTodo.setText(deadline);
       // priorityTodo.setText(priority);


	}
	
    public void setViews(){
    	//main view
    	setContentView(R.layout.detail_todo);
    	titleTodo = (TextView) findViewById(R.id.detail_todo_title);
        contentTodo = (TextView) findViewById(R.id.detail_content_output);
        deadlineTodo = (TextView) findViewById(R.id.detail_deadline_output);
        //priorityTodo = (TextView) findViewById(R.id.detail_priority_output);
    }
}
