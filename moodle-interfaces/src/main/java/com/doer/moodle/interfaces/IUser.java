package com.doer.moodle.interfaces;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.doer.moodle.interfaces.entity.UserInfo;

@Path("/user")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
public interface IUser {

	@Path("/getUsers")
	@GET
	List<UserInfo> getUsers();

	@Path("/saveUser")
	@POST
	void saveUser(String userName, String password);

	@Path("/serach")
	@POST
	List<UserInfo> serach(String key);

}
