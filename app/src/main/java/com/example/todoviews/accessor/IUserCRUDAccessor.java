package com.example.todoviews.accessor;

import com.example.todoviews.models.User;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/users")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface IUserCRUDAccessor {
    @POST
    public boolean checkPassword(User user);
}
