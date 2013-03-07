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
package org.ow2.play.governance.permission.api.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.metadata.api.MetaResource;

/**
 * CRUD API using Metadata stuff
 * 
 * @author chamerling
 * 
 */
@Path("/permissions")
public interface PermissionService {

	/**
	 * 
	 * @return
	 * @throws GovernanceExeption
	 */
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	Response getPermissions() throws GovernanceExeption;

	/**
	 * Get the permission from its ID
	 * 
	 * @return
	 * @throws GovernanceExeption
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	Response getPermission(@PathParam("id") String name)
			throws GovernanceExeption;

	/**
	 * 
	 * @param meta
	 * @throws GovernanceExeption
	 */
	@PUT
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	Response add(MetaResource meta) throws GovernanceExeption;

	/**
	 * 
	 * @param name
	 * @return 
	 * @throws GovernanceExeption
	 */
	@DELETE
	@Path("/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	Response remove(@PathParam("name") String name) throws GovernanceExeption;

}
