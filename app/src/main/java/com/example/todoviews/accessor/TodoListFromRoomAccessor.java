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
    private List<Todo> todoList;
    private TodoAdapter adapter;
    protected static final String logger = ITodoListAccessor.class.getName();

    private Context context;

    public TodoListFromRoomAccessor(Context context) {
        this.context = context;
        //adapter = new TodoAdapter(this.context, this.todoList);
    }

    @Override
    public void addTodo(Todo item) {
        Thread th = new Thread(() -> {
                AppDatabase.getInstance(context).todoDao().insert(item);
                Log.i("Room", "INSERT: "+ item);

        });
        th.start();
        this.todoList.add(item);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public TodoAdapter getAdapter() {
        this.todoList = new ArrayList<>();
        Thread th = new Thread(() -> {
            for(Todo t : AppDatabase.getInstance(context).todoDao().getAll()) {
                this.todoList.add(t);
            }
            //this.todoList = AppDatabase.getInstance(context).todoDao().getAll();
            Log.i("Room", "GET ALL: "+ todoList);
            this.adapter.notifyDataSetChanged();
        });
        th.start();
        this.sortTodoList();
        this.adapter = new TodoAdapter(this.context, this.todoList);
        return this.adapter;
    }

    @Override
    public void updateItem(Todo item) {
        Log.i(logger, "updating todo: " + item);
        lookupItem(item).update(item);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void deleteItem(Todo item) {
        AppDatabase.getInstance(this.context).todoDao().deleteTodo(item);
        this.todoList.remove(lookupItem(item));
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public Todo getSelectedItem(int position, long id) {
        return null;
    }

    private Todo lookupItem(Todo item) {
        for (Todo current : this.todoList) {
            if (current.getId() == item.getId()) {
                return current;
            }
        }
        return null;
    }

    @Override
    public void close() {

    }

    public void sortTodoList() {
        this.todoList.sort((Comparator<Todo>) (t1, t2) -> {
            return t1.compareTo(t2);
        });
    }
}
