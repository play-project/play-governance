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

import java.util.List;

import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.metadata.api.MetaResource;

/**
 * @author chamerling
 * 
 */
public interface PermissionService {

	/**
	 * 
	 * @return
	 * @throws GovernanceExeption
	 */
	List<MetaResource> getPermissions() throws GovernanceExeption;

	/**
	 * 
	 * @return
	 * @throws GovernanceExeption
	 */
	MetaResource getPermission(String name) throws GovernanceExeption;

	/**
	 * 
	 * @param meta
	 * @throws GovernanceExeption
	 */
	void add(MetaResource meta) throws GovernanceExeption;

	/**
	 * 
	 * @param name
	 * @throws GovernanceExeption
	 */
	void remove(String name) throws GovernanceExeption;

}
