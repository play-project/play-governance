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
package org.ow2.play.governance.groups;

import java.util.ArrayList;
import java.util.List;

import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.GroupService;
import org.ow2.play.governance.api.bean.Group;
import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.MetadataException;
import org.ow2.play.metadata.api.Resource;
import org.ow2.play.metadata.api.service.MetadataService;

/**
 * @author chamerling
 * 
 */
public class GroupServiceImpl implements GroupService {

	private MetadataService metadataService;

	@Override
	public Group create(Group group) throws GovernanceExeption {
		if (group == null) {
			throw new GovernanceExeption("Null input");
		}

		if (group.name == null) {
			throw new GovernanceExeption("Null name is not allowed");
		}

		// check if the group already exists
		try {
			if (metadataService.exists(ResourceHelper.getGroupResource(group))) {
				throw new GovernanceExeption("This group already exists");
			}
		} catch (MetadataException e) {
			e.printStackTrace();
			throw new GovernanceExeption(e);
		}

		MetaResource mr = ResourceHelper.toMeta(group);
		try {
			metadataService.create(mr);
		} catch (MetadataException e) {
			e.printStackTrace();
			throw new GovernanceExeption(e);
		}
		return group;
	}

	@Override
	public List<Group> get() throws GovernanceExeption {
		// get all the resources with the group name

		List<MetaResource> list = null;
		try {
			list = metadataService.listWhere(Constants.RESOURCE_NAME, null);
		} catch (MetadataException e) {
			throw new GovernanceExeption(e);
		}

		List<Group> result = new ArrayList<Group>();
		for (MetaResource metaResource : list) {
			result.add(ResourceHelper.toGroup(metaResource));
		}
		return result;
	}

	@Override
	public void delete(Group group) throws GovernanceExeption {
		throw new GovernanceExeption("Not implemented");
	}

	@Override
	public Group getGroupFromID(String id) throws GovernanceExeption {
		throw new GovernanceExeption("Not implemented");
	}
	
	@Override
	public Group getGroupFromURI(String uri) throws GovernanceExeption {
		Resource resource = ResourceHelper.getResource(uri);
		try {
			List<MetaResource> list = metadataService.listWhere(resource.getName(), resource.getUrl());
			
			if (list != null && list.size() > 0) {
				return ResourceHelper.toGroup(list.get(0));
			}
		} catch (MetadataException e) {
			e.printStackTrace();
			throw new GovernanceExeption(e);
		}
		throw new GovernanceExeption("Not found");
	}

	@Override
	public Group getGroupFromName(String name) throws GovernanceExeption {
		try {
			List<MetaResource> list = metadataService.listWhere(
					Constants.RESOURCE_NAME, Constants.PREFIX + name);
			
			if (list != null && list.size() > 0) {
				return ResourceHelper.toGroup(list.get(0));
			}
		} catch (MetadataException e) {
			e.printStackTrace();
			throw new GovernanceExeption(e);
		}
		throw new GovernanceExeption("Not found");
	}

	/**
	 * @param metadataService
	 *            the metadataService to set
	 */
	public void setMetadataService(MetadataService metadataService) {
		this.metadataService = metadataService;
	}

}
