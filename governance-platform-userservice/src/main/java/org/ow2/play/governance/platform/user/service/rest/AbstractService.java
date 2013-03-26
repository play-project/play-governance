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

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.ow2.play.governance.permission.api.PermissionChecker;
import org.ow2.play.governance.platform.user.service.UserResourceAccess;
import org.ow2.play.governance.platform.user.service.oauth.OAuthHelper;
import org.ow2.play.governance.user.api.UserException;
import org.ow2.play.governance.user.api.UserService;
import org.ow2.play.governance.user.api.bean.Resource;
import org.ow2.play.governance.user.api.bean.User;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * @author chamerling
 * 
 */
public abstract class AbstractService {

	protected UserService userService;
	
	protected PermissionChecker permissionChecker;
	
	protected UserResourceAccess userResourceAccess;
	
	/**
	 * Injected by CXF
	 * 
	 */
	@Context
	protected MessageContext mc;

	protected User getUser() {
		String endUserName = OAuthHelper.resolveUserName(mc);
		try {
			// load it each time since we need to reload data...
			return userService.getUser(endUserName);
		} catch (UserException e) {
			throw new WebApplicationException(e, Response.serverError().build());
		}
	}
	
	/**
	 * Get a user resource if present in the profile, or null if not found.
	 * 
	 * @param uri
	 * @return
	 */
	protected Resource getUserResource(final String uri) {
		Optional<Resource> optional = Iterables.tryFind(getUser().resources,
				new Predicate<Resource>() {
					public boolean apply(Resource input) {
						return input.uri != null && input.uri.equals(uri);
					}
				});
		if (!optional.isPresent()) {
			return null;
		}
		return optional.get();
	}

	/**
	 * @param userService
	 *            the userService to set
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	/**
	 * @param permissionChecker the permissionChecker to set
	 */
	public void setPermissionChecker(PermissionChecker permissionChecker) {
		this.permissionChecker = permissionChecker;
	}
	
	/**
	 * @param userResourceAccess the userResourceAccess to set
	 */
	public void setUserResourceAccess(UserResourceAccess userResourceAccess) {
		this.userResourceAccess = userResourceAccess;
	}

}
