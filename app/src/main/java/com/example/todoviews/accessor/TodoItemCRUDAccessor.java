package com.example.todoviews.accessor;

import android.util.Log;
import com.example.todoviews.models.Todo;
import com.example.todoviews.utils.AppDatabase;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;

import java.util.List;

public class TodoItemCRUDAccessor implements ITodoItemCRUDAccessor{
    protected static String logger = TodoItemCRUDAccessor.class.getName();
    private ITodoItemCRUDAccessor restClient;

    public TodoItemCRUDAccessor(String url) {
        Log.i(logger,"initialising restClient for URL: " + url);

        this.restClient = ProxyFactory.create(ITodoItemCRUDAccessor.class, url, new ApacheHttpClient4Executor());
        Log.i(logger,"initialised restClient: " + restClient + " of class " + restClient.getClass());
    }

    @Override
    public List<Todo> readAllTodos() {
        Log.i(logger, "readAllItems()");
        return restClient.readAllTodos();
    }

    @Override
    public Todo createTodo(Todo todo) {
        Log.i(logger, "createTodo()");
        return restClient.createTodo(todo);
    }

    @Override
    public boolean deleteTodo(long todoId) {
        Log.i(logger, "deleteTodo()");
        return restClient.deleteTodo(todoId);
    }

    @Override
    public Todo updateTodo(Todo todo) {
        Log.i(logger, "updateTodo()");
        return restClient.updateTodo(todo);
    }
}
