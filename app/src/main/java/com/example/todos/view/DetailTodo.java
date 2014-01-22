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
	private TextView priorityTodo;
    private ImageView categoryTodo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setViews();
		
        Intent i = getIntent();
        // Receiving the Data
        String title = i.getStringExtra("title");
        String priority = i.getStringExtra("priority");
        String content = i.getStringExtra("content");
        String deadline = i.getStringExtra("deadline");
        String category = i.getStringExtra("category");
         
        // Displaying Received data
        titleTodo.setText(title);
        contentTodo.setText(content);
        deadlineTodo.setText(deadline);
       // priorityTodo.setText(priority);

        if(category.contains("Travail")){
            categoryTodo.setImageResource(R.drawable.cabinet);
        }else if(category.contains("Famille")){
            categoryTodo.setImageResource(R.drawable.home);
        }else if(category.contains("Divers")){
            categoryTodo.setImageResource(R.drawable.bell);
        }else if(category.contains("Loisirs")){
            categoryTodo.setImageResource(R.drawable.color_palette);
        }else if(category.contains("Romantique")){
            categoryTodo.setImageResource(R.drawable.heart);
        }else if(category.contains("Café")){
            categoryTodo.setImageResource(R.drawable.coffee);
        }else if(category.contains("Jeux")){
            categoryTodo.setImageResource(R.drawable.game_pad);
        }else if(category.contains("Voyage")){
            categoryTodo.setImageResource(R.drawable.compass);
        }else if(category.contains("Travaux")){
            categoryTodo.setImageResource(R.drawable.wrench);
        }else if(category.contains("Docteur")){
            categoryTodo.setImageResource(R.drawable.oscilloscope);
        }
	}
	
    public void setViews(){
    	//main view
    	setContentView(R.layout.detail_todo);
    	titleTodo = (TextView) findViewById(R.id.detail_todo_title);
        contentTodo = (TextView) findViewById(R.id.detail_content_output);
        deadlineTodo = (TextView) findViewById(R.id.detail_deadline_output);
        //priorityTodo = (TextView) findViewById(R.id.detail_priority_output);
        categoryTodo = (ImageView) findViewById(R.id.deatail_image_category);
    }
}
