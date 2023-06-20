package com.example.todoviews.accessor;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.example.todoviews.R;
import com.example.todoviews.adapter.TodoAdapter;
import com.example.todoviews.models.Todo;
import com.example.todoviews.utils.AppDatabase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TodoListFromRoomAccessor implements ITodoListAccessor{
    private int sortMode = 0; // 0 = Favourite first, 1 = dueDate first
    private TodoAdapter adapter;
    protected static final String logger = TodoListFromRoomAccessor.class.getName();

    private Context context;

    public TodoListFromRoomAccessor(Context context) {
        this.context = context;
    }

    @Override
    public void addTodo(Todo item) {
        Thread th = new Thread(() -> {
                AppDatabase.getInstance(context).todoDao().insert(item);
                Log.i(logger, "INSERT: "+ item);

        });
        th.start();
        this.adapter.getTodoList().add(item);
        this.sortTodoList();
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public TodoAdapter getAdapter() {
        List<Todo> todoList = new ArrayList<>();
        Thread th = new Thread(() -> {
            for(Todo t : AppDatabase.getInstance(context).todoDao().getAll()) {
                todoList.add(t);
            }
            Log.i(logger, "GET ALL: "+ todoList);
            this.sortTodoList();
            this.adapter.notifyDataSetChanged();
        });
        th.start();
        this.adapter = new TodoAdapter(this.context, todoList, this);
        return this.adapter;
    }

    @Override
    public void updateItem(Todo newTodo) {
        Todo oldTodo = this.adapter.lookupItem(newTodo);
        oldTodo.update(newTodo);
        Thread th = new Thread(() ->{
            Log.i(logger, "UPDATE todo: " + oldTodo);
            AppDatabase.getInstance(this.context).todoDao().updateTodo(oldTodo);
        });
        th.start();
        this.sortTodoList();
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void deleteItem(Todo item) {
        Thread th = new Thread(() -> {
            Log.i(logger, "DELETE todo: " + item);
            AppDatabase.getInstance(this.context).todoDao().deleteTodo(item);
        });
        th.start();
        this.adapter.getTodoList().remove(this.adapter.lookupItem(item));
        this.adapter.notifyDataSetChanged();
    }


    @Override
    public void close() {

    }

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
