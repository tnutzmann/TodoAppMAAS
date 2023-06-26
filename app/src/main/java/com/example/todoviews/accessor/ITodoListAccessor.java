package com.example.todoviews.accessor;

import com.example.todoviews.adapter.TodoAdapter;
import com.example.todoviews.models.Todo;

import javax.ws.rs.*;
import java.util.List;

@Path("/todoitems")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface ITodoListAccessor {
    @GET
    public List<Todo> readAllTodos();

    @POST
    public void addTodo(Todo item);

    @PUT
    public void updateTodo(Todo item);

    @DELETE
    @Path("/{todoId}")
    public boolean deleteTodo(@PathParam("todoId") long todoId);
}
