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
package org.ow2.play.governance.api.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Groups REST API
 * 
 * @author chamerling
 * 
 */
@Path("/groups")
public interface GroupService {

	/**
	 * Get all the groups
	 * 
	 * @return
	 */
	@Path("/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	Response get();

	/**
	 * Get a group
	 * 
	 * @param name
	 * @return
	 */
	@Path("/{name}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	Response group(@PathParam("name") String name);

}
