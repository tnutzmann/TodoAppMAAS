package de.thb.fbi.msr.maus.einkaufsliste.model;

import javax.ws.rs.*;

@Path("/users")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface UserCRUDAccessor {
    @POST
    public boolean checkPassword(User user);
}
