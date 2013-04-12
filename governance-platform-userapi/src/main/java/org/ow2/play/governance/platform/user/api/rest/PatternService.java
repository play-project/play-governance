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

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ow2.play.governance.platform.user.api.rest.bean.Pattern;
import org.ow2.play.governance.platform.user.api.rest.bean.PatternResult;

/**
 * @author chamerling
 * 
 */
@Path(PatternService.PATH)
public interface PatternService {

	public static final String PATH = "/patterns";

	/**
	 * Get all the patterns the user deployed.
	 * 
	 * @return {@link Pattern} list as JSON array
	 */
	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	Response patterns();

	/**
	 * Get pattern from its ID
	 * 
	 * @param id
	 *            the pattern id
	 * @return {@link Pattern} as JSON if available and if user has access to
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	Response pattern(@PathParam("id") String id);

	/**
	 * Deploy a new pattern to the platform
	 * 
	 * @param the
	 *            pattern to deploy
	 * @return the pattern deployment result as {@link Pattern}
	 */
	@POST
	@Path("")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	Response deploy(@FormParam("pattern") String pattern);

	/**
	 * Analyze a pattern
	 * 
	 * @param pattern
	 * @return JSON response of the pattern analysis
	 */
	@POST
	@Path("/analyze")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	Response analyze(@FormParam("pattern") String pattern);

	/**
	 * Undeploy a pattern from its ID
	 * 
	 * @param id
	 *            the pattern ID to undeploy
	 * @return {@link PatternResult} as JSON
	 */
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	Response undeploy(@PathParam("id") String id);
}
