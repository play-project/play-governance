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
package org.ow2.play.governance.platform.user.api.rest.helpers;

import javax.ws.rs.core.Response.Status;

/**
 * @author chamerling
 * 
 */
public class Response {
	
	public static final javax.ws.rs.core.Response error(Status status,
			String pattern, Object... args) {
		return error(status, String.format(pattern, args));
	}

	public static final javax.ws.rs.core.Response error(Status status,
			String message) {
		return javax.ws.rs.core.Response
				.status(status)
				.entity(new org.ow2.play.governance.platform.user.api.rest.bean.Error(
						message)).build();
	}

	public static final javax.ws.rs.core.Response ok() {
		return javax.ws.rs.core.Response.ok().build();
	}

	public static final javax.ws.rs.core.Response ok(Object entity) {
		return javax.ws.rs.core.Response.ok(entity).build();
	}
	
	public static final javax.ws.rs.core.Response created(Object entity) {
		return javax.ws.rs.core.Response.status(Status.CREATED).entity(entity)
				.build();
	}

	public static final javax.ws.rs.core.Response notFound() {
		return javax.ws.rs.core.Response.status(Status.NOT_FOUND).build();
	}

	public static final javax.ws.rs.core.Response deleted() {
		return javax.ws.rs.core.Response.status(Status.NO_CONTENT).build();
	}
	
	public static final javax.ws.rs.core.Response unauthorized() {
		return javax.ws.rs.core.Response.status(Status.UNAUTHORIZED).build();
	}

    public static final javax.ws.rs.core.Response forbidden() {
        return javax.ws.rs.core.Response.status(Status.FORBIDDEN).build();
    }

}
