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
				return ResponseBuilder.error("User '%s' Not found", id)
						.build();
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
		return ResponseBuilder.error(new Exception("Not implemented")).build();
	}

	private Response ok(User user) {
		logger.info("Returning user " + user);
		return Response.ok(user).build();
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
