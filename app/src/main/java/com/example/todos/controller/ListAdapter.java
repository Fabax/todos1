package com.example.todos.controller;

import java.util.HashMap;
import java.util.List;

import com.example.todos.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class ListAdapter extends BaseAdapter {
	
	static final String KEY_ID = "id";
    static final String KEY_TITLE = "title";
    static final String KEY_DESCRIPTION = "content";
    static final String KEY_PRIORITY = "priority";
    static final String KEY_DEADLINE = "deadline";  
    static final String KEY_STATUS = "status";
    static final String KEY_CATEGORY = "category";

    LayoutInflater inflater;
	List<HashMap<String,String>> todoCollection;
	ViewHolder holder;
	
	public ListAdapter(Activity act, List<HashMap<String,String>> map) {		
		this.todoCollection = map;		
		inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.v("listAdapter",this.todoCollection.toString());
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return todoCollection.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.v("get view","getView");
		
		View vi=convertView;
		
	    if(convertView==null){
	    	vi = inflater.inflate(R.layout.list_row, null);
	      holder = new ViewHolder();
	      holder.title = (TextView)vi.findViewById(R.id.todo_list_title_output); // title task
	      holder.description = (TextView)vi.findViewById(R.id.todo_list_description_output); // description task
	      holder.deadline = (TextView)vi.findViewById(R.id.todo_list_deadline_output); // date tas
          holder.category = (ImageView)vi.findViewById(R.id.list_image);
	      vi.setTag(holder);
	    }
	    else{	    	
	    	holder = (ViewHolder)vi.getTag();
	    }
	    // changing background color according to the priority
	   
	    // Setting all values in listview	      
	    holder.title.setText(todoCollection.get(position).get(KEY_TITLE));
	    holder.description.setText(todoCollection.get(position).get(KEY_DESCRIPTION));
	    holder.deadline.setText(todoCollection.get(position).get(KEY_DEADLINE));



	    String myCategory = todoCollection.get(position).get(KEY_CATEGORY);
	    String priority = todoCollection.get(position).get(KEY_PRIORITY);
        String status = todoCollection.get(position).get(KEY_STATUS);
	    Log.v("priority",priority);

        if(myCategory.contains("Travail")){
            holder.category.setImageResource(R.drawable.cabinet);
        }else if(myCategory.contains("Famille")){
            holder.category.setImageResource(R.drawable.home);
        }else if(myCategory.contains("Divers")){
            holder.category.setImageResource(R.drawable.bell);
        }else if(myCategory.contains("Loisirs")){
            holder.category.setImageResource(R.drawable.color_palette);
        }else if(myCategory.contains("Romantique")){
            holder.category.setImageResource(R.drawable.heart);
        }else if(myCategory.contains("Café")){
            holder.category.setImageResource(R.drawable.coffee);
        }else if(myCategory.contains("Jeux")){
            holder.category.setImageResource(R.drawable.game_pad);
        }else if(myCategory.contains("Voyage")){
            holder.category.setImageResource(R.drawable.compass);
        }else if(myCategory.contains("Travaux")){
            holder.category.setImageResource(R.drawable.wrench);
        }else if(myCategory.contains("Docteur")){
            holder.category.setImageResource(R.drawable.oscilloscope);
        }


        if(status.contains("0")){
            holder.category.setBackgroundColor(Color.BLACK);
        }

	    if(priority.contains("0")){
            holder.category.setBackgroundColor(Color.CYAN);
	    }else if (priority.contains("1")){
            holder.category.setBackgroundColor(Color.GREEN);
	    }else if (priority.contains("2")){
            holder.category.setBackgroundColor(Color.MAGENTA);
	    }




	    return vi;
	}
	
	static class ViewHolder{
		TextView title;
		TextView deadline;
		TextView description;
        ImageView category;
	}

}
