package com.example.todoviews.accessor;

import com.example.todoviews.models.Todo;

import javax.ws.rs.*;
import java.util.List;

@Path("/todoitems")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface ITodoItemCRUDAccessor {
    @GET
    public List<Todo> readAllTodos();

    @POST
    public Todo createTodo(Todo todo);

    @DELETE
    @Path("/{todoId}")
    public boolean deleteTodo(@PathParam("todoId") long todoId);

    @PUT
    public Todo updateTodo(Todo todo);
}
