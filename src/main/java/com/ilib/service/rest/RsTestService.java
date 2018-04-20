package com.ilib.service.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.ilib.model.Notice;

@Path("rstest")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface RsTestService {
	@POST
	@Path("/testpost")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response selectBy(String titleJson)throws Exception;
	@PUT  
	@Path("/testput")
	@Consumes(MediaType.APPLICATION_JSON)  
	@Produces(MediaType.APPLICATION_JSON)
	public String create(final Notice notice)throws Exception ;
	
	@GET
	@Path("/testget")
	@Produces(MediaType.APPLICATION_JSON)
	public Notice getById(@QueryParam("id") Long id)throws Exception;
	
	@GET
	@Path("/testpathget/{title}")
	@Produces(MediaType.APPLICATION_JSON)
	public Object getMap(@PathParam("title") String title)throws Exception;
}
