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
package org.ow2.play.governance.bootstrap.client;

import java.util.List;
import java.util.logging.Logger;

import javax.jws.WebMethod;

import org.ow2.play.governance.cxf.CXFHelper;
import org.ow2.play.metadata.api.Data;
import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.Metadata;
import org.ow2.play.metadata.api.MetadataException;
import org.ow2.play.metadata.api.Resource;
import org.ow2.play.metadata.api.service.MetadataService;
import org.ow2.play.service.registry.api.Registry;
import org.ow2.play.service.registry.api.RegistryException;

/**
 * @author chamerling
 * 
 */
public class MetadataServiceClient implements MetadataService {

	private MetadataService client;

	private Registry registry;

	private static Logger logger = Logger.getLogger(MetadataServiceClient.class
			.getName());

	protected synchronized MetadataService getClient() throws MetadataException {
		if (client == null) {

			// get URL from the registry
			if (registry == null) {
				logger.warning("Can not get the registry!");
				throw new MetadataException("Can not get the registry");
			}

			String url = null;
			try {
				url = registry
						.get(org.ow2.play.service.registry.api.Constants.METADATA);
			} catch (RegistryException e) {
				throw new MetadataException(e);
			}

			if (url == null) {
				logger.warning("Can not get the URL from the registry!");
				throw new MetadataException(
						"Can not get the URL from the registry!");
			}

			logger.info("Building service client for service at " + url);
			client = CXFHelper
					.getClientFromFinalURL(url, MetadataService.class);
		}
		return client;
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
	public void addMetadata(Resource arg0, Metadata arg1)
			throws MetadataException {
		logger.info("Add Metadata");
		getClient().addMetadata(arg0, arg1);
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
	public boolean deleteMetaData(Resource arg0) throws MetadataException {
		logger.info("Delete Metadata");

		return getClient().deleteMetaData(arg0);
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
	public List<Metadata> getMetaData(Resource arg0) throws MetadataException {
		logger.info("Get Metadata");

		return getClient().getMetaData(arg0);
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
	public Metadata getMetadataValue(Resource arg0, String arg1)
			throws MetadataException {
		logger.info("Get MetadataValue");

		return getClient().getMetadataValue(arg0, arg1);
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
	public List<MetaResource> getResoucesWithMeta(List<Metadata> arg0)
			throws MetadataException {
		logger.info("Add Metadata");

		return getClient().getResoucesWithMeta(arg0);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.metadata.api.service.MetadataService#removeMetadata(org.
	 * ow2.play.metadata.api.Resource, org.ow2.play.metadata.api.Metadata)
	 */
	@Override
	@WebMethod
	public void removeMetadata(Resource arg0, Metadata arg1)
			throws MetadataException {
		getClient().removeMetadata(arg0, arg1);
	}
	
	/* (non-Javadoc)
	 * @see org.ow2.play.metadata.api.service.MetadataService#create(org.ow2.play.metadata.api.MetaResource)
	 */
	@Override
	@WebMethod
	public boolean create(MetaResource arg0) throws MetadataException {
		return getClient().create(arg0);
	}

	@Override
	@WebMethod
	public void clear() throws MetadataException {
		getClient().clear();
	}

	/* (non-Javadoc)
	 * @see org.ow2.play.metadata.api.service.MetadataService#exists(org.ow2.play.metadata.api.Resource)
	 */
	@Override
	@WebMethod
	public boolean exists(Resource arg0) throws MetadataException {
		return getClient().exists(arg0);
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
     * @see org.ow2.play.metadata.api.service.MetadataService#listWhere(java.lang.String, java.lang.String)
     */
    @Override
    public List<MetaResource> listWhere(String name, String url)
    		throws MetadataException {
    	return getClient().listWhere(name, url);
    }

    /* (non-Javadoc)
     * @see org.ow2.play.metadata.api.service.MetadataService#setMetadata(org.ow2.play.metadata.api.Resource, org.ow2.play.metadata.api.Metadata)
     */
	@Override
	public void setMetadata(Resource arg0, Metadata arg1)
			throws MetadataException {
		getClient().setMetadata(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see org.ow2.play.metadata.api.service.MetadataService#listWhereData(java.lang.String, java.lang.String, org.ow2.play.metadata.api.Data)
	 */
	@Override
	@WebMethod
	public List<MetaResource> listWhereData(String resourceName, String metadataName,
			Data data) throws MetadataException {
		return getClient().listWhereData(resourceName, metadataName, data);
	}
	
	/**
	 * @param registry
	 *            the registry to set
	 */
	public void setRegistry(Registry registry) {
		this.registry = registry;
	}
}
