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
package org.ow2.play.governance.platform.user.api.rest;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ow2.play.governance.platform.user.api.rest.bean.Subscription;
import org.ow2.play.governance.platform.user.api.rest.bean.SubscriptionResult;

/**
 * Subscription Service is User-contextualized.
 * 
 * @author chamerling
 * 
 */
@Path("/subscriptions")
public interface SubscriptionService {

	/**
	 * Subscribe to the platform.
	 * 
	 * @param subscription
	 * @return a {@link SubscriptionResult} as JSON
	 * 
	 */
	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	Response subscribe(Subscription subscription);

	/**
	 * Send unsubscribe with the subscription ID received in
	 * {@link #subscribe(Subscription)} result.
	 * 
	 * @param subscriptionID
	 * @return
	 */
	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	Response unsubscribe(@PathParam("id") String subscriptionID);

	/**
	 * Get the current user subscriptions
	 * 
	 * @return a list of {@link Subscription} as JSON
	 * 
	 */
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	Response subscriptions();
	
	/**
	 * Get a subscription from its ID
	 * 
	 * @param id
	 * @return a {@link Subscription} as JSON
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	Response subscription(@PathParam("id") String id);
}
