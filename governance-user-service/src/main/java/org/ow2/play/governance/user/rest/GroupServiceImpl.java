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
package org.ow2.play.governance.user.rest;

import java.util.logging.Logger;

import javax.ws.rs.core.Response;

import org.ow2.play.commons.rest.error.ResponseBuilder;
import org.ow2.play.governance.user.api.UserException;
import org.ow2.play.governance.user.api.bean.Group;
import org.ow2.play.governance.user.api.rest.GroupService;

/**
 * @author chamerling
 * 
 */
public class GroupServiceImpl implements GroupService {

	private org.ow2.play.governance.user.GroupService groupService;

	private static Logger logger = Logger.getLogger(UserServiceImpl.class
			.getName());

	public Response create(Group group) {
		logger.info("Create group " + group);
		if (group == null) {
			return ResponseBuilder.error("Group data is null").build();
		}

		Group result = null;
		try {
			result = groupService.create(group);
		} catch (UserException e) {
			return ResponseBuilder.error(e).build();
		}
		return Response.status(201).entity(result).build();
	}

	public Response update(Group group) {
		logger.info("update group " + group);
		return null;
	}

	public Response getGroupFromID(String id) {
		logger.info("Get group " + id);

		try {
			Group group = groupService.getGroupFromID(id);
			if (group == null) {
				return ResponseBuilder.error("Group '%s' Not found", id)
						.build();
			}
			return ok(group);

		} catch (UserException e) {
			return ResponseBuilder.error(e).build();
		}
	}

	public Response getGroupFromName(String name) {
		logger.info("Get group from name" + name);

		try {
			Group group = groupService.getGroupFromName(name);
			if (group == null) {
				return ResponseBuilder.error("Group '%s' Not found", name)
						.build();
			}
			return ok(group);

		} catch (UserException e) {
			return ResponseBuilder.error(e).build();
		}
	}

	private Response ok(Group group) {
		logger.info("Returning group " + group);
		return Response.ok(group).build();
	}

	/**
	 * @param userService
	 *            the userService to set
	 */
	public void setGroupService(
			org.ow2.play.governance.user.GroupService groupService) {
		this.groupService = groupService;
	}

}
