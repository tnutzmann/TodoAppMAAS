package com.example.todoviews.accessor;

import android.content.Context;
import android.util.Log;
import com.example.todoviews.R;
import com.example.todoviews.adapter.TodoAdapter;
import com.example.todoviews.models.Todo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Read out a list of items from a (static) resource file, which does not allow
 * for write access
 *
 * @author Joern Kreutel (inspiration)
 *
 */

public class TodoListFromStaticAccessor implements ITodoListAccessor{
    private List<Todo> todoList;
    private TodoAdapter adapter;
    protected static final String logger = ITodoListAccessor.class.getName();

    private Context context;

    public TodoListFromStaticAccessor(Context context) {
        this.context = context;
    }

    @Override
    public void addTodo(Todo item) {
        this.todoList.add(item);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public TodoAdapter getAdapter() {
        String[] strings = this.context.getResources().getStringArray(R.array.todoItems);
        todoList = new ArrayList<>();
        for(String s : strings) {
            todoList.add(new Todo(s, "Testing!!!", 0));
        }
        this.sortTodoList();
        this.adapter = new TodoAdapter(this.context, this.todoList, this);
        return adapter;
    }

    @Override
    public void updateItem(Todo item) {
        Log.i(logger, "updating todo: " + item);
        lookupItem(item).update(item);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void deleteItem(Todo item) {
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
        // nothing to do
    }

    public void sortTodoList() {
        this.todoList.sort((Comparator<Todo>) (t1, t2) -> {
            return t1.compareTo(t2);
        });
    }
}
