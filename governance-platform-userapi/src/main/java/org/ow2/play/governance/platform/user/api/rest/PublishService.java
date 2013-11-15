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

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Push a notification to the system
 * 
 * @author chamerling
 * 
 */
@Path(PublishService.PATH)
public interface PublishService {

	public static final String PATH = "/publish";

	/**
	 * Push a new message into the platform.
	 * 
	 * Input JSON message is a multipart form data to be able to push large and multiple format data:
	 * 
	 * @param resource : The resource to send notification to
	 * @param message : The message to send to the resource
	 * 
	 * @return HTTP Status 200 if data has been published
	 */
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	Response notify(@FormParam("resource") String resource,
			@FormParam("message") String message);

}
