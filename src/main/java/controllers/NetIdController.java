package controllers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/netid")
public class NetIdController {
    @GET
    public String netID() {
        return "zz524";
    }
}
