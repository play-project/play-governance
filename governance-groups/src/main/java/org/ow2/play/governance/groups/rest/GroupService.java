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
package org.ow2.play.governance.groups.rest;

import java.util.List;

import javax.ws.rs.core.Response;

import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.bean.Group;
import org.ow2.play.governance.groups.rest.bean.GroupList;

/**
 * @author chamerling
 * 
 */
public class GroupService implements
		org.ow2.play.governance.api.rest.GroupService {
	
	private org.ow2.play.governance.api.GroupService groupService;
	
	public Response get() {
		try {
			List<Group> groups = groupService.get();
			return Response.ok(new GroupList(groups)).build();
		} catch (GovernanceExeption e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	public Response group(String name) {
		Group group;
		try {
			group = groupService.getGroupFromName(name);
			return Response.ok(group).build();
		} catch (GovernanceExeption e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	/**
	 * @param groupService the groupService to set
	 */
	public void setGroupService(
			org.ow2.play.governance.api.GroupService groupService) {
		this.groupService = groupService;
	}

}
