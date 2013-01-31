/**
 *
 * Copyright (c) 2013, Linagora
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA 
 *
 */
package org.ow2.play.governance.user.api.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ow2.play.governance.user.api.User;

/**
 * @author chamerling
 * 
 */
@Path("users")
public interface UserService {

	/**
	 * Get a user from its login
	 * @param login
	 * @return
	 */
	@GET
	@Path("{login}")
	@Produces(MediaType.APPLICATION_JSON)
	Response getUser(@PathParam("login") String login);

	/**
	 * Get a user from its provider information
	 * 
	 * @param login
	 * @param provider
	 * @return
	 */
	@GET
	@Path("query")
	@Produces(MediaType.APPLICATION_JSON)
	Response getUserFromProvider(@QueryParam("login") String login,
			@QueryParam("provider") String provider);

	/**
	 * Update a user. Received data is user as JSON.
	 * 
	 * @param user
	 * @return
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	Response update(User user);

	/**
	 * Register a user. Input data is User to register as JSON
	 * 
	 * @param user
	 * @return
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	Response register(User user);

	/**
	 * Delete a user
	 * 
	 * @param user
	 * @return
	 */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	Response delete(User user);
}
