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
package org.ow2.play.metadata.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.jws.WebMethod;

import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.Metadata;
import org.ow2.play.metadata.api.MetadataException;
import org.ow2.play.metadata.api.Resource;

/**
 * @author chamerling
 * @deprecated use Mongo-based version
 * 
 */
public class InMemoryMetadataServiceImpl implements
		org.ow2.play.metadata.api.service.MetadataService {

	private static Logger logger = Logger
			.getLogger(InMemoryMetadataServiceImpl.class.getName());

	Map<Resource, Set<Metadata>> metadata;

	/**
	 * 
	 */
	public InMemoryMetadataServiceImpl() {
		this.metadata = new ConcurrentHashMap<Resource, Set<Metadata>>();
	}
	
	/* (non-Javadoc)
	 * @see org.ow2.play.metadata.api.service.MetadataService#get(java.lang.String)
	 */
	@Override
	@WebMethod
	public MetaResource get(String id) throws MetadataException {
		return null;
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
		logger.info("Add meta to " + resource);

		if (!resourceExists(resource)) {
			create(resource);
		}

		this.metadata.get(resource).add(metadata);
	}
	
	/* (non-Javadoc)
	 * @see org.ow2.play.metadata.api.service.MetadataService#create(org.ow2.play.metadata.api.MetaResource)
	 */
	@Override
	@WebMethod
	public boolean create(MetaResource metaResource) throws MetadataException {
		logger.info("Create metaresource " + metaResource);
		if (!resourceExists(metaResource.getResource())) {
			create(metaResource.getResource());
		}
		
		for (Metadata md : metaResource.getMetadata()) {
			addMetadata(metaResource.getResource(), md);
		}
		return true;
	}
	
	@Override
	@WebMethod
	public void clear() throws MetadataException {
		this.metadata = new ConcurrentHashMap<Resource, Set<Metadata>>();
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
	public synchronized void removeMetadata(Resource resource, Metadata metadata)
			throws MetadataException {
		logger.info("Remove metadata from " + resource);

		if (!resourceExists(resource)) {
			return;
		}
		this.metadata.get(resource).remove(metadata);
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
		logger.info("Get metadatas from " + resource);

		if (resourceExists(resource))
			return new ArrayList<Metadata>(metadata.get(resource));

		return new ArrayList<Metadata>();
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
		logger.info("Get metadata value for resource " + resource
				+ " and metakey " + key);

		Metadata result = null;
		if (resourceExists(resource)) {
			Set<Metadata> set = metadata.get(resource);
			Iterator<Metadata> iter = set.iterator();
			boolean found = false;
			while (iter.hasNext() && !found) {
				Metadata meta = iter.next();
				found = key.equals(meta.getName());
				if (found) {
					result = meta;
				}
			}
		}
		return result;
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
		logger.info("Delete metadata from resource " + resource);

		if (resourceExists(resource)) {
			synchronized (this.metadata) {
				this.metadata.remove(resource);
			}
			return true;
		}
		return false;
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
		logger.info("Get resources with meta " + include);

		throw new MetadataException("Not implemented");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.metadata.api.service.MetadataService#list()
	 */
	@Override
	@WebMethod
	public List<MetaResource> list() {
		logger.info("Get list of resources");
		List<MetaResource> result = new ArrayList<MetaResource>();
		if (metadata != null) {
			for (Entry<Resource, Set<Metadata>> metaResource : metadata
					.entrySet()) {
				result.add(new MetaResource(metaResource.getKey(),
						new ArrayList<Metadata>(metaResource.getValue())));

			}
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.ow2.play.metadata.api.service.MetadataService#setMetadata(org.ow2.play.metadata.api.Resource, org.ow2.play.metadata.api.Metadata)
	 */
	@Override
	public void setMetadata(Resource resource, Metadata metadata)
			throws MetadataException {
		// not implemented	
	}
	
	/* (non-Javadoc)
	 * @see org.ow2.play.metadata.api.service.MetadataService#exists(org.ow2.play.metadata.api.Resource)
	 */
	@Override
	@WebMethod
	public boolean exists(Resource resource) throws MetadataException {
		return resourceExists(resource);
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
        return false;
    }

    protected boolean resourceExists(Resource r) {
		return r != null && metadata.containsKey(r);
	}

	protected synchronized void create(Resource resource) {
		if (!resourceExists(resource)) {
			metadata.put(resource, new HashSet<Metadata>());
		}
	}

	/* (non-Javadoc)
	 * @see org.ow2.play.metadata.api.service.MetadataService#listWhereName(java.lang.String)
	 */
	@Override
	public List<MetaResource> listWhere(String name, String url)
			throws MetadataException {
		// TODO Auto-generated method stub
		return null;
	}
}
