package com.doer.moodle.dubbo.interfaces;

import java.util.List;

import com.doer.moodle.dubbo.interfaces.entity.UserInfo;

//@Path("/user")
//@Consumes({ MediaType.APPLICATION_JSON })
//@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
public interface IUser {

	// @Path("/getUsers")
	// @GET
	List<UserInfo> getUsers();

	// @Path("/saveUser")
	// @POST
	void saveUser(String userName, String password);

	// @Path("/serach")
	// @POST
	List<UserInfo> serach(String key);

}
