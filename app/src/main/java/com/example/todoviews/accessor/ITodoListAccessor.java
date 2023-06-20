package com.example.todoviews.accessor;

import com.example.todoviews.adapter.TodoAdapter;
import com.example.todoviews.models.Todo;

public interface ITodoListAccessor {
    // add an Todo to the list
    public void addTodo(Todo item);

    // get an adapter for the TodoList
    public TodoAdapter getAdapter();

    // pdate an existing Todo
    public void updateItem(Todo item);

    // delete Todo
    public void deleteItem(Todo item);
}
