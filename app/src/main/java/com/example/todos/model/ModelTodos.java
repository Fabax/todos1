package com.example.todos.model;

public class ModelTodos {
	   //private variables
    int _id;
    int _priority;
    int _statusTodo; // 1 = on going , 0 = done
    String _todoTitle;
    String _todoContent;
    String _todoDeadline;
     
    // Empty constructor
    public ModelTodos(){
         
    }
    // constructor
    public ModelTodos(int id, int _priority, String _todoTitle, String _todoContent){
        this._id = id;
        this._priority = _priority;
        this._todoTitle = _todoTitle;
        this._todoContent = _todoContent;
        this._statusTodo = 1;
    }

    public ModelTodos(int _priority, String _todoTitle, String _todoContent, String _deadline){
        this._priority = _priority;
        this._todoTitle = _todoTitle;
        this._todoContent = _todoContent;
        this._statusTodo = 1;
        this._todoDeadline = _deadline;
    }
     


    // Getters ---------
    public int getID(){
        return this._id;
    }
    
 	public int getPriority(){
        return this._priority;
    }

    public int getStatusTodo(){
        return this._statusTodo;
    }

    public String getTodoTitle(){
        return this._todoTitle;
    }

    public String getTodoContent(){
        return this._todoContent;
    }
    
    public String getTodoDeadline(){
        return this._todoDeadline;
    }

    // Setters ------ 
    public void setID(int id){
        this._id = id;
    }
     
    public void setPriority(int priority){
        this._priority = priority;
    }
     
    public void setStatusTodo(int statusTodo){
        this._statusTodo = statusTodo;
    }

    public void setTodoTitle(String todoTitle){
        this._todoTitle = todoTitle;
    }

     public void setTodoContent(String todoContent){
        this._todoContent = todoContent;
    }
     
     public void setTodoDeadline(String todoDeadLine){
         this._todoDeadline = todoDeadLine;
     }
	
}
