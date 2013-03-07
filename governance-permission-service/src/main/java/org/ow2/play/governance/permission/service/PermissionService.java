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
package org.ow2.play.governance.permission.service;

import java.util.List;

import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.permission.api.Constants;
import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.MetadataException;
import org.ow2.play.metadata.api.Resource;
import org.ow2.play.metadata.api.service.MetadataService;

/**
 * @author chamerling
 * 
 */
public class PermissionService implements
		org.ow2.play.governance.permission.api.PermissionService {

	private MetadataService metadataService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.permission.api.PermissionService#getPermissions()
	 */
	@Override
	public List<MetaResource> getPermissions() throws GovernanceExeption {
		try {
			return metadataService.listWhere(Constants.PERMISSION_RESOURCE,
					null);
		} catch (MetadataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new GovernanceExeption(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.permission.api.PermissionService#getPermission
	 * (java.lang.String)
	 */
	@Override
	public MetaResource getPermission(String name) throws GovernanceExeption {
		try {
			List<MetaResource> list = metadataService
					.listWhere(Constants.PERMISSION_RESOURCE,
							Constants.RESOURCE_NS + name);
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		} catch (MetadataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new GovernanceExeption(e);
		}
		// not found
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.permission.api.PermissionService#add(org.ow2.
	 * play.metadata.api.MetaResource)
	 */
	@Override
	public void add(MetaResource meta) throws GovernanceExeption {
		if (meta == null) {
			throw new GovernanceExeption("Null input");
		}

		// permissions
		// TODO check parameters. Will be better to have a Permission object
		// here....
		throw new GovernanceExeption("Not implemented");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.permission.api.PermissionService#remove(java.
	 * lang.String)
	 */
	@Override
	public void remove(String name) throws GovernanceExeption {
		if (name == null) {
			throw new GovernanceExeption("Null input");
		}
		Resource resource = new Resource(Constants.PERMISSION_RESOURCE,
				Constants.RESOURCE_NS + name);
		try {
			metadataService.deleteResource(resource);
		} catch (MetadataException e) {
			throw new GovernanceExeption(e);
		}
	}

	/**
	 * @param metadataService
	 *            the metadataService to set
	 */
	public void setMetadataService(MetadataService metadataService) {
		this.metadataService = metadataService;
	}

}
