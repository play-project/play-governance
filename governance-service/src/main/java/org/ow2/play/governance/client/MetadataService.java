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
package org.ow2.play.governance.client;

import java.util.List;

import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.Metadata;
import org.ow2.play.metadata.api.MetadataException;
import org.ow2.play.metadata.api.Resource;
import org.ow2.play.service.registry.api.RegistryException;
import org.petalslink.dsb.cxf.CXFHelper;

/**
 * @author chamerling
 * 
 */
public class MetadataService implements
		org.ow2.play.metadata.api.service.MetadataService {

	private ServiceRegistry serviceRegistry;

	public org.ow2.play.metadata.api.service.MetadataService getClient() throws MetadataException {
		try {
			return CXFHelper.getClientFromFinalURL(serviceRegistry
					.get(org.ow2.play.service.registry.api.Constants.METADATA),
					org.ow2.play.metadata.api.service.MetadataService.class);
		} catch (RegistryException e) {
			throw new MetadataException(e);
		}
	}

	@Override
	public MetaResource get(String id) throws MetadataException {
		return getClient().get(id);
	}

	@Override
	public void clear() throws MetadataException {
		getClient().clear();
	}

	@Override
	public void addMetadata(Resource resource, Metadata metadata)
			throws MetadataException {
		getClient().addMetadata(resource, metadata);
	}

	@Override
	public void setMetadata(Resource resource, Metadata metadata)
			throws MetadataException {
		getClient().setMetadata(resource, metadata);
	}

	@Override
	public boolean create(MetaResource metaResource) throws MetadataException {
		return getClient().create(metaResource);
	}

	@Override
	public void removeMetadata(Resource resource, Metadata metadata)
			throws MetadataException {
		getClient().removeMetadata(resource, metadata);
	}

	@Override
	public List<Metadata> getMetaData(Resource resource) throws MetadataException {
		return getClient().getMetaData(resource);
	}

	@Override
	public Metadata getMetadataValue(Resource resource, String key)
			throws MetadataException {
		return getClient().getMetadataValue(resource, key);
	}

	@Override
	public boolean deleteMetaData(Resource resource) throws MetadataException {
		return getClient().deleteMetaData(resource);
	}

	@Override
	public List<MetaResource> getResoucesWithMeta(List<Metadata> include)
			throws MetadataException {
		return getClient().getResoucesWithMeta(include);
	}

	@Override
	public List<MetaResource> list() throws MetadataException {
		return getClient().list();
	}

	@Override
	public List<MetaResource> listWhere(String name, String url)
			throws MetadataException {
		return getClient().listWhere(name, url);
	}

	@Override
	public boolean exists(Resource resource) throws MetadataException {
		return getClient().exists(resource);
	}

	@Override
	public boolean deleteResource(Resource resource) throws MetadataException {
		return getClient().deleteResource(resource);
	}

	/**
	 * @param serviceRegistry
	 *            the serviceRegistry to set
	 */
	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

}
