package de.thb.fbi.msr.maus.einkaufsliste.model;

import javax.ws.rs.*;
import java.util.List;

@Path("/todoitems")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface TodoItemCRUDAccessor {
    @GET
    public List<TodoItem> readAllTodoItems();

    @POST
    public TodoItem createTodoItem(TodoItem todoItem);

    @DELETE
    @Path("/{todoItemId}")
    public boolean deleteTodoItem(@PathParam("todoItemId") long todoItemId);

    @PUT
    public TodoItem updateTodoItem(TodoItem todoItem);
}
