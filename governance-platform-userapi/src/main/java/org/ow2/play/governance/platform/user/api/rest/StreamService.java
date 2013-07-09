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
package org.ow2.play.governance.platform.user.api.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Access to the stream recources. The implementation will take care about auth
 * and permissions i.e. only topics a user can access will be available in
 * different operations.
 * 
 * @author chamerling
 * 
 */
@Path(StreamService.PATH)
public interface StreamService {
	
	public static final String PATH = "/streams";

	/**
	 * Get all the streams the user can access to as JSON array.
	 * 
	 * @return the list of topics
	 */
	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	Response streams();
	
	/**
	 * Get a stream from its ID
	 * 
	 * @param id
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	Response stream(@PathParam("id") String id);

    /**
     * Check is the user can access to the stream in the given mode.
     *
     * @param id the stream ID to check access to
     * @param mode the mode to check access to
     * @return HTTP 200 OK if access is authorized, else HTTP 403 unauthorized
     */
    @GET
    @Path("/{id}/access/{mode}")
    @Produces(MediaType.APPLICATION_JSON)
    Response checkAccess(@PathParam("id") String id, @PathParam("mode") String mode);
}
