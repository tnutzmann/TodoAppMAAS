package com.example.todoviews.accessor;

import android.content.Context;
import android.util.Log;
import com.example.todoviews.R;
import com.example.todoviews.adapter.TodoAdapter;
import com.example.todoviews.models.Todo;
import com.example.todoviews.utils.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class TodoListFromRoomAccessor implements ITodoListAccessor{
    private int sortMode = 0; // 0 = Favourite first, 1 = dueDate first
    private TodoAdapter adapter;
    private TodoItemCRUDAccessor restClient;
    protected static final String logger = TodoListFromRoomAccessor.class.getName();

    private Context context;
    private boolean isWebservieAvailable;

    public TodoListFromRoomAccessor(Context context, boolean isWebserviceAvailable) {
        this.context = context;
        this.restClient = new TodoItemCRUDAccessor(context.getString(R.string.WebServiceURL));
        this.isWebservieAvailable = isWebserviceAvailable;
    }

    @Override
    public List<Todo> readAllTodos() {
        List<Todo> todoList = new ArrayList<>();
        Thread th = new Thread(() -> {
            for(Todo t : AppDatabase.getInstance(context).todoDao().getAll()) {
                todoList.add(t);
            }
            if(isWebservieAvailable) {
                Log.i(logger, "GET ALL: "+ todoList);
                if(todoList.size() != 0) {
                    // Case: local todos from db exists, web todos need to be deleted, and todos from db are transferred to webservice
                    Log.i(logger, "Local Todos are used.");
                    for(Todo t : restClient.readAllTodos()) {
                        Log.i(logger, "DELETE TODO: " + t );
                        restClient.deleteTodo(t.getId());
                    }
                    for(Todo t : todoList) {
                        restClient.addTodo(t);
                    }
                } else {
                    // Case: no local todos, todos from Webservice need to be transferred to db
                    Log.i(logger, "Remote Todos are used.");
                    for(Todo t : restClient.readAllTodos()) {
                        todoList.add(t);
                        AppDatabase.getInstance(context).todoDao().insert(t);
                    }
                }
            }
            this.sortTodoList();
            //this.adapter.notifyDataSetChanged();
        });
        th.start();
        return todoList;
    }

    @Override
    public void addTodo(Todo item) {
        Thread th = new Thread(() -> {
            if(isWebservieAvailable) {
                restClient.addTodo(item);
            }

            AppDatabase.getInstance(context).todoDao().insert(item);
            Log.i(logger, "INSERT: "+ item);

        });
        th.start();
        this.adapter.getTodoList().add(item);
        this.sortTodoList();
        this.adapter.notifyDataSetChanged();
    }

    public TodoAdapter getAdapter() {
        if(adapter == null) {
            this.adapter = new TodoAdapter(this.context, this.readAllTodos(), this);
        }
        return this.adapter;
    }

    @Override
    public void updateTodo(Todo newTodo) {
        Todo oldTodo = this.adapter.lookupItem(newTodo);
        oldTodo.update(newTodo);
        Thread th = new Thread(() ->{
            if(isWebservieAvailable) {
                restClient.updateTodo(newTodo);
            }

            Log.i(logger, "UPDATE todo: " + oldTodo);
            AppDatabase.getInstance(this.context).todoDao().updateTodo(oldTodo);
        });
        th.start();
        this.sortTodoList();
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public boolean deleteTodo(long todoId) {
        Todo todo = adapter.lookupItem(todoId);
        Boolean isDeleted = this.adapter.getTodoList().remove(todo);
        if(isDeleted) {
            Thread th = new Thread(() -> {
                if(isWebservieAvailable) {
                    restClient.deleteTodo(todoId);
                }
                Log.i(logger, "DELETE todo: " + todo);
                AppDatabase.getInstance(this.context).todoDao().deleteTodo(todo);
            });
            th.start();
            this.adapter.notifyDataSetChanged();
        }
        return isDeleted;
    }

    // Sorting stuff
    public void setFavouriteFirstSorting() {
        this.sortMode = 0;
        sortTodoList();
        this.adapter.notifyDataSetChanged();
    }

    public void setDateFirstSorting() {
        this.sortMode = 1;
        sortTodoList();
        this.adapter.notifyDataSetChanged();
    }

    public void sortTodoList() {
        if(this.sortMode == 0) {
            this.adapter.getTodoList().sort((t1, t2) -> {
                if(t1.isDone() == t2.isDone()) {
                    if(t1.isFavourite() == t2.isFavourite()) {
                        return new Long(t1.getDueDate()).compareTo(new Long(t2.getDueDate()));
                    }
                    if(t1.isFavourite()) return -1;
                    return 1;
                }
                if(t1.isDone()) return 1;
                return -1;
            });
        } else {
            this.adapter.getTodoList().sort((t1, t2) -> {
                if(t1.isDone() == t2.isDone()) {
                    if(t1.getDueDate() == t2.getDueDate()) {
                        Log.i(logger, "Due Date are the same!");
                        if(t1.isFavourite() == t2.isFavourite())  return 0;
                        if(t1.isFavourite()) return -1;
                        return 1;
                    }
                    return new Long(t1.getDueDate()).compareTo(new Long(t2.getDueDate()));
                }
                if(t1.isDone()) return 1;
                return -1;
            });
        }
    }
}
