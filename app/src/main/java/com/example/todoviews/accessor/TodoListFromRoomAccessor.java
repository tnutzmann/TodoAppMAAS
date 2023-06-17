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
    //private List<Todo> todoList;
    private TodoAdapter adapter;
    protected static final String logger = ITodoListAccessor.class.getName();

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
        Thread th = new Thread(() ->{
            Log.i(logger, "UPDATE todo: " + oldTodo);
            AppDatabase.getInstance(this.context).todoDao().updateTodo(oldTodo);
        });
        th.start();
        oldTodo.update(newTodo);
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
    public Todo getSelectedItem(int position, long id) {
        return null;
    }


    @Override
    public void close() {

    }

    public void sortTodoList() {
        this.adapter.getTodoList().sort((Comparator<Todo>) (t1, t2) -> {
            return t1.compareTo(t2);
        });
    }
}
