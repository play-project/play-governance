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
import org.ow2.play.governance.permission.api.Permission;
import org.ow2.play.metadata.api.Data;
import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.Metadata;
import org.ow2.play.metadata.api.MetadataException;
import org.ow2.play.metadata.api.Resource;
import org.ow2.play.metadata.api.service.MetadataService;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * @author chamerling
 * 
 */
public class PermissionService implements
		org.ow2.play.governance.permission.api.PermissionService {

	private MetadataService metadataService;

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

	@Override
	public void add(MetaResource meta) throws GovernanceExeption {
		if (meta == null) {
			throw new GovernanceExeption("Null input");
		}

		// permissions
		// TODO check parameters. Will be better to have a Permission object
		// here....
		throw new GovernanceExeption(
				"Not implemented, use addPermission instaed");
	}

	@Override
	public String addPermission(Permission permission)
			throws GovernanceExeption {
		if (permission == null) {
			throw new GovernanceExeption("Can not add null permission");
		}

		if (permission.name == null) {
			throw new GovernanceExeption("Permission name is required");
		}

		MetaResource mr = ResourceHelper.toMeta(permission);
		try {
			this.metadataService.create(mr);
		} catch (MetadataException e) {
			e.printStackTrace();
			throw new GovernanceExeption(e);
		}

		return permission.name;
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

	public void setMode(String name, List<String> modes)
			throws GovernanceExeption {
		if (name == null || modes == null) {
			throw new GovernanceExeption("Null inputs");
		}

		MetaResource meta = getPermission(name);
		if (meta == null) {
			throw new GovernanceExeption("No such permission " + name);
		}

		try {
			metadataService.setMetadata(meta.getResource(),
					ResourceHelper.toMetaMode(modes));
		} catch (MetadataException e) {
			throw new GovernanceExeption(e);
		}

	}

	public void setAccessTo(String name, List<String> accessTo)
			throws GovernanceExeption {
		if (name == null || accessTo == null) {
			throw new GovernanceExeption("Null inputs");
		}

		MetaResource meta = getPermission(name);
		if (meta == null) {
			throw new GovernanceExeption("No such permission " + name);
		}

		try {
			metadataService.setMetadata(meta.getResource(),
					ResourceHelper.toMetaAccessTo(accessTo));
		} catch (MetadataException e) {
			throw new GovernanceExeption(e);
		}

	}

	/**
	 * @param metadata
	 * @param accessTo
	 * @return
	 */
	protected Metadata get(final List<Metadata> metadata, final String key) {
		return Iterables.tryFind(metadata, new Predicate<Metadata>() {
			public boolean apply(Metadata input) {
				return input != null && input.getName() != null
						&& input.getName().equals(key);
			}
		}).orNull();
	}

	public void setAgent(String name, List<String> agent)
			throws GovernanceExeption {
		if (name == null || agent == null) {
			throw new GovernanceExeption("Null inputs");
		}

		MetaResource meta = getPermission(name);
		if (meta == null) {
			throw new GovernanceExeption("No such permission " + name);
		}

		try {
			metadataService.setMetadata(meta.getResource(),
					ResourceHelper.toMetaAgent(agent));
		} catch (MetadataException e) {
			throw new GovernanceExeption(e);
		}
	}

	public List<MetaResource> getWhereAccessTo(String accessTo)
			throws GovernanceExeption {
		if (accessTo == null) {
			throw new GovernanceExeption("Null accessTo is not alloawed");
		}

		try {
			return metadataService.listWhereData(Constants.PERMISSION_RESOURCE,
					org.ow2.play.governance.permission.api.Constants.ACCESS_TO,
					new Data(org.ow2.play.metadata.api.Type.URI, accessTo));

		} catch (MetadataException e) {
			throw new GovernanceExeption(e);
		}
	}

	public List<MetaResource> getWhereAgent(String agent)
			throws GovernanceExeption {
		if (agent == null) {
			throw new GovernanceExeption("Null agent is not alloawed");
		}

		try {
			return metadataService.listWhereData(Constants.PERMISSION_RESOURCE,
					org.ow2.play.governance.permission.api.Constants.AGENT,
					new Data(org.ow2.play.metadata.api.Type.URI, agent));

		} catch (MetadataException e) {
			throw new GovernanceExeption(e);
		}	
	}

	public List<MetaResource> getWhereMode(String mode)
			throws GovernanceExeption {
		if (mode == null) {
			throw new GovernanceExeption("Null mode is not alloawed");
		}

		try {
			return metadataService.listWhereData(Constants.PERMISSION_RESOURCE,
					org.ow2.play.governance.permission.api.Constants.MODE,
					new Data(org.ow2.play.metadata.api.Type.URI, mode));

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
