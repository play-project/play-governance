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
package org.ow2.play.governance.bootstrap.api.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST service for the registry
 * 
 * @author chamerling
 * 
 */
@Path("/registry/")
public interface SubscriptionRegistryService {

	/**
	 * Get all the subscriptions
	 * 
	 * @return
	 */
	@GET
	@Path("subscriptions")
	@Produces(MediaType.APPLICATION_JSON)
	Response all();

	/**
	 * Clears all the subscriptions
	 * 
	 * @return
	 */
	@GET
	@Path("clear")
	@Produces(MediaType.APPLICATION_JSON)
	Response clear();

	@GET
	@Path("clear/provider")
	@Produces(MediaType.APPLICATION_JSON)
	Response clearAllFromProvider(@QueryParam("url") String url);

	@GET
	@Path("clear/subscriber")
	@Produces(MediaType.APPLICATION_JSON)
	Response clearAllFromSubscriber(@QueryParam("url") String url);

}
