/**
 *
 * Copyright (c) 2012, PetalsLink
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
package org.ow2.play.service.registry.api.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Registry REST API
 * 
 * @author chamerling
 * 
 */
@Path("/registry/")
public interface Registry {

	/**
	 * Load registry from an URL 
	 * 
	 * @param url
	 * @return
	 */
	@GET
	@Path("load")
	@Produces(MediaType.APPLICATION_JSON)
	Response load(@QueryParam("url") String url);

	/**
	 * Clear the registry data
	 * 
	 * @return
	 */
	@GET
	@Path("clear")
	@Produces(MediaType.APPLICATION_JSON)
	Response clear();
	
	/**
	 * Get all the values ie a key->value list
	 * 
	 * @return
	 */
	@GET
	@Path("entries")
	@Produces(MediaType.APPLICATION_JSON)
	Response entries();
}
