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
package org.ow2.play.governance.platform.user.service.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.ow2.play.governance.platform.user.api.rest.bean.Pattern;

/**
 * TODO 
 * 
 * @author chamerling
 * 
 */
public class PatternService implements
		org.ow2.play.governance.platform.user.api.rest.PatternService {

	public Response patterns() {
		return Response.status(Status.SERVICE_UNAVAILABLE).build();
	}

	public Response pattern(String id) {
		return Response.status(Status.SERVICE_UNAVAILABLE).build();
	}

	public Response deploy(Pattern pattern) {
		return Response.status(Status.SERVICE_UNAVAILABLE).build();
	}

	public Response undeploy(String id) {
		return Response.status(Status.SERVICE_UNAVAILABLE).build();
	}

}
