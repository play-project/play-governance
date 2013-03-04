/**
 *
 * Copyright (c) 2012, PetalsLink
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
package org.ow2.play.metadata.client;

import java.util.List;

import javax.jws.WebMethod;

import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.Metadata;
import org.ow2.play.metadata.api.MetadataException;
import org.ow2.play.metadata.api.Resource;
import org.ow2.play.metadata.api.service.MetadataService;
import org.petalslink.dsb.cxf.CXFHelper;

/**
 * A CXF-based client for the metadata service
 * 
 * @author chamerling
 * 
 */
public class MetadataClient implements MetadataService {

	private String url;

	private MetadataService client;

	/**
	 * 
	 */
	public MetadataClient(String endpoint) {
		if (endpoint == null) {
			throw new IllegalArgumentException("Endpoint can not be null");
		}
		this.url = endpoint;
	}

	protected synchronized MetadataService getClient() {
		if (this.client == null) {
			this.client = CXFHelper.getClientFromFinalURL(url,
					MetadataService.class);
		}
		return this.client;
	}

	@Override
	@WebMethod
	public void clear() throws MetadataException {
		getClient().clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.metadata.api.service.MetadataService#addMetadata(org.ow2
	 * .play.metadata.api.Resource, org.ow2.play.metadata.api.Metadata)
	 */
	@Override
	@WebMethod
	public void addMetadata(Resource resource, Metadata metadata)
			throws MetadataException {
		getClient().addMetadata(resource, metadata);
	}
	
	/* (non-Javadoc)
	 * @see org.ow2.play.metadata.api.service.MetadataService#create(org.ow2.play.metadata.api.MetaResource)
	 */
	@Override
	@WebMethod
	public boolean create(MetaResource metaResource) throws MetadataException {
		return getClient().create(metaResource);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.metadata.api.service.MetadataService#removeMetadata(org.
	 * ow2.play.metadata.api.Resource, org.ow2.play.metadata.api.Metadata)
	 */
	@Override
	@WebMethod
	public void removeMetadata(Resource resource, Metadata metadata)
			throws MetadataException {
		getClient().removeMetadata(resource, metadata);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.metadata.api.service.MetadataService#getMetaData(org.ow2
	 * .play.metadata.api.Resource)
	 */
	@Override
	@WebMethod
	public List<Metadata> getMetaData(Resource resource)
			throws MetadataException {
		return getClient().getMetaData(resource);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.metadata.api.service.MetadataService#getMetadataValue(org
	 * .ow2.play.metadata.api.Resource, java.lang.String)
	 */
	@Override
	@WebMethod
	public Metadata getMetadataValue(Resource resource, String key)
			throws MetadataException {
		return getClient().getMetadataValue(resource, key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.metadata.api.service.MetadataService#deleteMetaData(org.
	 * ow2.play.metadata.api.Resource)
	 */
	@Override
	@WebMethod
	public boolean deleteMetaData(Resource resource) throws MetadataException {
		return getClient().deleteMetaData(resource);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.metadata.api.service.MetadataService#getResoucesWithMeta
	 * (java.util.List)
	 */
	@Override
	@WebMethod
	public List<MetaResource> getResoucesWithMeta(List<Metadata> include)
			throws MetadataException {
		return getClient().getResoucesWithMeta(include);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.metadata.api.service.MetadataService#list()
	 */
	@Override
	@WebMethod
	public List<MetaResource> list() throws MetadataException {
		return getClient().list();
	}
	
	/* (non-Javadoc)
	 * @see org.ow2.play.metadata.api.service.MetadataService#exists(org.ow2.play.metadata.api.Resource)
	 */
	@Override
	@WebMethod
	public boolean exists(Resource resource) throws MetadataException {
		return getClient().exists(resource);
	}
	
	/* (non-Javadoc)
	 * @see org.ow2.play.metadata.api.service.MetadataService#get(java.lang.String)
	 */
	@Override
	@WebMethod
	public MetaResource get(String id) throws MetadataException {
		return getClient().get(id);
	}

    /**
     * Delete a resource from the repository
     *
     * @param resource
     * @throws org.ow2.play.metadata.api.MetadataException
     *
     */
    @Override
    public boolean deleteResource(Resource resource) throws MetadataException {
        return getClient().deleteResource(resource);
    }

    /* (non-Javadoc)
     * @see org.ow2.play.metadata.api.service.MetadataService#setMetadata(org.ow2.play.metadata.api.Resource, org.ow2.play.metadata.api.Metadata)
     */
	@Override
	public void setMetadata(Resource resource, Metadata metadata)
			throws MetadataException {
		getClient().setMetadata(resource, metadata);
	}

}
