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
package org.ow2.play.governance.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ow2.play.governance.Adapter;
import org.ow2.play.governance.Helper;
import org.ow2.play.governance.api.Constants;
import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.TopicRegistry;
import org.ow2.play.governance.api.bean.Topic;
import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.Metadata;
import org.ow2.play.metadata.api.MetadataException;
import org.ow2.play.metadata.api.Resource;
import org.ow2.play.metadata.api.service.MetadataService;
import org.ow2.play.service.registry.api.Registry;
import org.ow2.play.service.registry.api.RegistryException;
import org.petalslink.dsb.cxf.CXFHelper;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 * @author chamerling
 * 
 */
public class TopicRegistryService implements TopicRegistry {

	private static Logger logger = Logger.getLogger(TopicRegistryService.class
			.getName());

	private Registry serviceRegistry;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.TopicRegistry#create(org.ow2.play.governance
	 * .api.bean.Topic, java.util.List)
	 */
	@Override
	public boolean create(Topic topic, List<Metadata> properties)
			throws GovernanceExeption {
		boolean result = true;

		MetaResource mr = getResourceForTopic(topic);
		if (mr == null) {
			mr = Adapter.transform(topic);
			if (properties != null) {
				mr.getMetadata().addAll(properties);
			}
			mr = createMetaResource(mr);
		} else {
			// Do not handle if it already exists...
			throw new GovernanceExeption(
					"Can not create the topic, it already exists. Use #addProperties to update it");
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.governance.api.TopicRegistry#getTopics()
	 */
	@Override
	public List<Topic> getTopics() throws GovernanceExeption {
		logger.fine("Get topics from metadata service...");

		List<Topic> result = new ArrayList<Topic>();
		String endpoint = getEndpoint(org.ow2.play.service.registry.api.Constants.METADATA);

		logger.info("Getting topics from " + endpoint);

		MetadataService client = getMetadataClient(endpoint);
		List<MetaResource> resources = null;
		try {
			resources = client.list();
		} catch (Exception e) {
			throw new GovernanceExeption(e);
		}

		// TODO/FIXME/!!! : Do not get all the resource but only the topic ones!!!
		if (resources != null) {
			for (MetaResource r : resources) {
				if (logger.isLoggable(Level.FINE)) {
					logger.fine("Resource : " + r.getResource());
				}
				result.add(Adapter.transform(r));
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.TopicRegistry#isActive(org.ow2.play.governance
	 * .api.bean.Topic)
	 */
	@Override
	public boolean isActive(Topic topic) throws GovernanceExeption {
		return Collections2.filter(getProperties(topic),
				new Predicate<Metadata>() {
					public boolean apply(Metadata md) {
						return (md != null
								&& md.getName().equals(Constants.TOPIC_ACTIVE)
								&& md.getData().size() == 1 && md.getData()
								.get(0).equals(Constants.BOOLEAN_TRUE));
					}
				}).size() >= 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.TopicRegistry#getProperties(org.ow2.play.
	 * governance.api.bean.Topic)
	 */
	@Override
	public List<Metadata> getProperties(Topic topic) throws GovernanceExeption {
		MetaResource mr = getResourceForTopic(topic);
		if (mr == null) {
			throw new GovernanceExeption(
					"Can not find this topic in the repository");
		}
		return mr.getMetadata();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.TopicRegistry#addProperties(org.ow2.play.
	 * governance.api.bean.Topic, java.util.List)
	 */
	@Override
	public boolean setProperties(Topic topic, List<Metadata> properties)
			throws GovernanceExeption {
		boolean result = true;

		MetaResource mr = getResourceForTopic(topic);
		MetadataService service = getMetadataClient(getEndpoint(org.ow2.play.service.registry.api.Constants.METADATA));
		
		for (Metadata metadata : properties) {
			try {
				service.setMetadata(mr.getResource(), metadata);
			} catch (MetadataException e) {
				if (logger.isLoggable(Level.FINE)) {
					logger.log(Level.WARNING, "Got error while setting metadata", e);
				} else {
					logger.warning("Got error while setting metadata : " + e.getMessage());					
				}
			}
		}
		
		return result;
		
	}

	protected MetaResource getResourceForTopic(Topic topic)
			throws GovernanceExeption {
		String endpoint = null;
		MetaResource result = null;

		try {
			endpoint = serviceRegistry
					.get(org.ow2.play.service.registry.api.Constants.METADATA);
		} catch (RegistryException e1) {
			e1.printStackTrace();
			throw new GovernanceExeption(e1);
		}

		if (endpoint == null) {
			throw new GovernanceExeption(
					"Can not get the metadata provider endpoint from the service registry");
		}

		Resource resource = Helper.getResource(topic);
		MetadataService client = getMetadataClient(endpoint);

		try {
			boolean exists = client.exists(resource);
			if (exists) {
				result = new MetaResource(resource,
						client.getMetaData(resource));
			} else {
				// let's do it...
				logger.warning("Can not find the resource in the repository "
						+ resource);
			}
		} catch (MetadataException e) {
			throw new GovernanceExeption(e);
		}

		return result;
	}

	/**
	 * @param endpoint
	 * @return
	 */
	protected MetadataService getMetadataClient(String endpoint) {
		return CXFHelper.getClientFromFinalURL(endpoint, MetadataService.class);
	}

	protected MetaResource createMetaResource(MetaResource metaresource)
			throws GovernanceExeption {
		String endpoint = getEndpoint(org.ow2.play.service.registry.api.Constants.METADATA);

		MetadataService client = getMetadataClient(endpoint);
		boolean created = false;
		try {
			created = client.create(metaresource);
		} catch (MetadataException e) {
			throw new GovernanceExeption(
					"Can not create the metaresource in the repository", e);
		}

		if (!created) {
			// throw exception
			throw new GovernanceExeption("Can not create the metaresource");
		}
		return metaresource;
	}

	protected String getEndpoint(String id) throws GovernanceExeption {
		String url = null;
		try {
			url = serviceRegistry.get(id);
		} catch (RegistryException e) {
			throw new GovernanceExeption(e);
		}

		if (url == null) {
			throw new GovernanceExeption(
					"Can not find the service associated to " + id);

		}
		return url;
	}

	/**
	 * @param serviceRegistry
	 *            the serviceRegistry to set
	 */
	public void setServiceRegistry(Registry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}
}
