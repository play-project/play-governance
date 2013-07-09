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
package org.ow2.play.governance.permission.api;

import java.util.Set;

/**
 * @author chamerling
 * 
 */
public interface PermissionChecker {

	/**
	 * Check if the user has access to the group ie user object contains the
	 * group
	 * 
	 * @param user
	 * @param group
	 * @return
	 */
	boolean checkGroup(String user, String group);

	/**
	 * Check if the user has access to resource. This means that we check if the
	 * user belongs to the groups where the resource is accessible. It does not
	 * check the resource access mode.
	 * 
	 * @param user
	 * @param resource
	 * @return
	 */
	boolean checkResource(String user, String resource);

	/**
	 * Check if the role is valid for a user to access a resource
	 * 
	 * @param user
	 * @param resource
	 * @param role
	 * @return
     *
     * @deprecated use checkMode
	 */
	boolean checkRole(String user, String resource, String role);

    /**
     * Check if the user have access to the resource in the given mode
     *
     * @param user
     * @param resource resource URI
     * @param mode mode URI
     * @return true if access is OK with the given mode
     */
	boolean checkMode(String user, String resource, String mode);


	/**
	 * Get the list of group IDs where the resource is available
	 * 
	 * @param resource
	 * @return
	 */
	Set<String> getGroupsForResource(String resource);

    /**
     * Get all the groups where the resource is accessible with the given mode
     *
     * @param resource
     * @param mode
     * @return
     */
    Set<String> getGroupsForResourceInMode(final String resource, final String mode);

}