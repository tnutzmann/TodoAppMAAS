package com.example.todoviews.accessor;

import android.util.Log;
import com.example.todoviews.models.Todo;
import com.example.todoviews.utils.AppDatabase;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;

import java.util.List;

public class TodoItemCRUDAccessor implements ITodoListAccessor{
    protected static String logger = TodoItemCRUDAccessor.class.getName();
    private ITodoListAccessor restClient;

    public TodoItemCRUDAccessor(String url) {
        Log.i(logger,"initialising restClient for URL: " + url);

        this.restClient = ProxyFactory.create(ITodoListAccessor.class, url, new ApacheHttpClient4Executor());
        Log.i(logger,"initialised restClient: " + restClient + " of class " + restClient.getClass());
    }

    @Override
    public List<Todo> readAllTodos() {
        Log.i(logger, "readAllItems()");
        return restClient.readAllTodos();
    }

    @Override
    public void addTodo(Todo todo) {
        Log.i(logger, "createTodo()");
        restClient.addTodo(todo);
    }

    @Override
    public boolean deleteTodo(long todoId) {
        Log.i(logger, "deleteTodo()");
        return restClient.deleteTodo(todoId);
    }

    @Override
    public void updateTodo(Todo todo) {
        Log.i(logger, "updateTodo()");
        restClient.updateTodo(todo);
    }
}
