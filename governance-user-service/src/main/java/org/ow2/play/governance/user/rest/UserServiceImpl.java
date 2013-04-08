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

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.ow2.play.commons.rest.error.ResponseBuilder;
import org.ow2.play.governance.user.api.UserException;
import org.ow2.play.governance.user.api.bean.Account;
import org.ow2.play.governance.user.api.bean.Resource;
import org.ow2.play.governance.user.api.bean.User;
import org.ow2.play.governance.user.api.rest.UserService;

/**
 * @author chamerling
 * 
 */
public class UserServiceImpl implements UserService {

	private org.ow2.play.governance.user.UserService userService;

	private static Logger logger = Logger.getLogger(UserServiceImpl.class
			.getName());

	public Response get(String id) {
		logger.info("Get user from ID " + id);

		try {
			User user = userService.getUserFromID(id);
			if (user == null) {
				return ResponseBuilder.error("User '%s' Not found", id).build();
			}
			return ok(user);

		} catch (UserException e) {
			return ResponseBuilder.error(e).build();
		}
	}

	public Response getUser(String login) {
		logger.info("Get user " + login);

		try {
			User user = userService.getUser(login);
			if (user == null) {
				return ResponseBuilder.error("User '%s' Not found", login)
						.build();
			}
			return ok(user);

		} catch (UserException e) {
			return ResponseBuilder.error(e).build();
		}
	}

	public Response getUserFromProvider(String login, String provider) {
		logger.info("Get user " + login + " from Provider " + provider);

		try {
			User user = userService.getUserFromProvider(provider, login);
			if (user == null) {
				return ResponseBuilder.error("User '%s' Not found", login)
						.build();
			}

			return ok(user);

		} catch (UserException e) {
			return ResponseBuilder.error(e).build();
		}
	}

	public Response getUserFromToken(String token) {
		logger.info("Get user from token " + token);

		try {
			User user = userService.getUserFromToken(token);
			if (user == null) {
				return ResponseBuilder.error("User '%s' Not found", token)
						.build();
			}

			return ok(user);

		} catch (UserException e) {
			return ResponseBuilder.error(e).build();
		}
	}

	public Response basicAuth(String login, String password) {
		logger.info("Get user from login and password");
		try {
			User user = userService.authenticate(login, password);
			if (user == null) {
				return ResponseBuilder.error("User '%s' Not found", login)
						.build();
			}

			return ok(user);

		} catch (UserException e) {
			return ResponseBuilder.error(e).build();
		}
	}

	public Response update(User user) {
		if (user == null) {
			return ResponseBuilder.error("User data is null").build();
		}

		logger.info("Updating the user " + user);
		User result = null;
		try {
			result = userService.update(user);
		} catch (UserException e) {
			return ResponseBuilder.error(e).build();
		}
		result.password = "";
		return ok(result);
	}

	public Response register(User user) {
		if (user == null) {
			return ResponseBuilder.error("User data is null").build();
		}

		logger.info("Registering new user " + user);
		User result = null;
		try {
			result = userService.register(user);
		} catch (UserException e) {
			return ResponseBuilder.error(e).build();
		}
		result.password = "";
		// created HTTP status
		return Response.status(201).entity(result).build();
	}

	public Response delete(User user) {
		// TODO : Need rights to do that!
		return ResponseBuilder.error(new Exception("Not implemented")).build();
	}

	public Response addGroup(String id, Resource group) {
		if (id == null) {
			return ResponseBuilder.error("ID is null").build();
		}

		if (group == null || group.uri == null) {
			return ResponseBuilder.error(
					"Bad resource parameters, set group and uri").build();
		}

		logger.info("Adding group to " + id + " : " + group);
		User result = null;
		try {
			result = userService.addGroup(id, group.uri);
		} catch (UserException e) {
			return ResponseBuilder.error(e).build();
		}
		return ok(result);
	}

	public Response deleteGroup(String id, String groupURI) {
		if (id == null || groupURI == null || groupURI.trim().length() == 0) {
			return badRequest();
		}

		try {
			userService.removeGroup(id, groupURI);
			return deleted();
		} catch (UserException e) {
			e.printStackTrace();
			return error();
		}
	}

	public Response getGroups(String id) {
		logger.info("Get groups from " + id);
		try {
			List<Resource> groups = userService.getUserFromID(id).groups;
			return Response.ok(groups.toArray(new Resource[groups.size()]))
					.build();
		} catch (UserException e) {
			return ResponseBuilder.error(e).build();
		}
	}

	public Response addResource(String id, Resource resource) {
		if (id == null) {
			return ResponseBuilder.error("ID is null").build();
		}

		if (resource == null || resource.uri == null) {
			return ResponseBuilder.error("Resource is null").build();
		}

		logger.info("Adding resource to " + id + " : " + resource);
		User result = null;
		try {
			result = userService.addResource(id, resource.uri, resource.name);
		} catch (UserException e) {
			return ResponseBuilder.error(e).build();
		}
		return ok(result);
	}

	public Response deleteResource(String id, String resource, String name) {
		if (id == null || resource == null || resource.trim().length() == 0
				|| name == null || name.trim().length() == 0) {
			return badRequest();
		}

		try {
			userService.removeResource(id, resource, name);
			return deleted();
		} catch (UserException e) {
			e.printStackTrace();
			return error();
		}
	}

	public Response getResources(String id) {
		logger.info("Get resources from " + id);
		try {
			List<Resource> resources = userService.getUserFromID(id).resources;
			return Response.ok(
					resources.toArray(new Resource[resources.size()]))
					.build();
		} catch (UserException e) {
			return ResponseBuilder.error(e).build();
		}
	}

	public Response addAccount(String id, Account account) {
		if (id == null) {
			return ResponseBuilder.error("ID is null").build();
		}

		if (account == null) {
			return ResponseBuilder.error("Account is null").build();
		}

		logger.info("Adding account to " + id + " : " + account);
		User result = null;
		try {
			result = userService.addAccount(id, account);
		} catch (UserException e) {
			return ResponseBuilder.error(e).build();
		}
		return ok(result);
	}

	public Response deleteAccount(String id, String provider) {
		if (id == null || provider == null || provider.trim().length() == 0) {
			return badRequest();
		}
		
		try {
			userService.removeAccount(id, provider);
			return deleted();
		} catch (UserException e) {
			e.printStackTrace();
			return error();
		}
	}

	public Response getAccounts(String id) {
		logger.info("Get accounts from " + id);
		try {
			List<Account> accounts = userService.getUserFromID(id).accounts;
			return Response.ok(accounts.toArray(new Account[accounts.size()]))
					.build();
		} catch (UserException e) {
			return ResponseBuilder.error(e).build();
		}
	}

	protected Response ok(User user) {
		logger.info("Returning user " + user);
		return Response.ok(user).build();
	}

	protected Response deleted() {
		return Response.status(Status.NO_CONTENT).build();
	}

	protected Response created() {
		return Response.status(Status.CREATED).build();
	}

	protected Response badRequest() {
		return Response.status(Status.BAD_REQUEST).build();
	}

	protected Response error() {
		return Response.serverError().build();
	}

	/**
	 * @param userService
	 *            the userService to set
	 */
	public void setUserService(
			org.ow2.play.governance.user.UserService userService) {
		this.userService = userService;
	}

}
