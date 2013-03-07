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
package org.ow2.play.governance.permission.service.rest;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.service.rest.MetadataContainer;

/**
 * @author chamerling
 * 
 */
public class PermissionService implements
		org.ow2.play.governance.permission.api.rest.PermissionService {

	private org.ow2.play.governance.permission.api.PermissionService permissionService;

	public Response getPermissions() throws GovernanceExeption {
		List<MetaResource> list = permissionService.getPermissions();
		return Response.ok(new MetadataContainer(list)).build();
	}

	public Response getPermission(String name) throws GovernanceExeption {
		MetaResource result = permissionService.getPermission(name);
		return Response.ok(result).build();
	}

	public Response add(MetaResource meta) throws GovernanceExeption {
		if (meta == null) {
			// TODO
			return Response.serverError().build();
		}
		permissionService.add(meta);
		// TODO
		return Response.status(Status.CREATED).build();
	}

	public Response remove(String name) throws GovernanceExeption {
		if (name == null) {
			// TODO
			return Response.serverError().build();
		}
		permissionService.remove(name);
		// TODO
		return Response.ok().build();

	}

	/**
	 * @param permissionService
	 *            the permissionService to set
	 */
	public void setPermissionService(
			org.ow2.play.governance.permission.api.PermissionService permissionService) {
		this.permissionService = permissionService;
	}

}
