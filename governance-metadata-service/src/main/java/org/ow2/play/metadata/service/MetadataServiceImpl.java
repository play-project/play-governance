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

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jws.WebMethod;

import org.ow2.play.metadata.api.Constants;
import org.ow2.play.metadata.api.Data;
import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.Metadata;
import org.ow2.play.metadata.api.MetadataException;
import org.ow2.play.metadata.api.Resource;
import org.ow2.play.metadata.api.Type;
import org.ow2.play.metadata.api.service.MetadataService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

/**
 * @author chamerling
 * 
 */
public class MetadataServiceImpl implements MetadataService {

	private MongoTemplate mongoTemplate;

	private static Logger logger = Logger.getLogger(MetadataServiceImpl.class
			.getName());

	/**
	 * 
	 */
	public MetadataServiceImpl() {
	}

	@Override
	public void clear() throws MetadataException {
		logger.info("Got a clear call");
		throw new MetadataException("Deprecated, kept for API compatibility");
	}

	@Override
	@WebMethod
	public void addMetadata(Resource resource, Metadata metadata)
			throws MetadataException {

		if (logger.isLoggable(Level.INFO))
			logger.info(String.format("Adding metdata %s to resource %s",
					metadata, resource));

		if (resource == null || resource.getName() == null
				|| resource.getUrl() == null) {
			throw new MetadataException("Null resource");
		}

		if (metadata == null || metadata.getData() == null
				|| metadata.getName() == null) {
			throw new MetadataException("Null metadata");
		}

		Update update = new Update();
		update.addToSet("metadata", metadata);
		mongoTemplate.updateFirst(
				query(where("resource.name").is(resource.getName())
						.and("resource.url").is(resource.getUrl())), update,
				org.ow2.play.metadata.service.document.MetaResource.class);

	}

	@Override
	public void setMetadata(Resource resource, Metadata metadata)
			throws MetadataException {
		if (logger.isLoggable(Level.INFO))
			logger.info(String.format("Setting metdata %s to resource %s",
					metadata, resource));

		logger.warning("FIXME : Set the metadata value");
		Update update = new Update();
		update.addToSet("metadata", metadata);
		mongoTemplate.updateFirst(
				query(where("resource.name").is(resource.getName())
						.and("resource.url").is(resource.getUrl())), update,
				org.ow2.play.metadata.service.document.MetaResource.class);
	}

	@Override
	public boolean create(MetaResource metaResource) throws MetadataException {
		boolean result = true;

		if (logger.isLoggable(Level.INFO)) {
			logger.info("Create metaresource " + metaResource);
		}

		if (metaResource == null || metaResource.getResource() == null) {
			logger.warning("Can not create a null resource");
			throw new MetadataException("Can not create a null resource...");
		}

		// add creation date
		metaResource.getMetadata().add(
				new Metadata(Constants.CREATED_AT, new Data(Type.LITERAL, ""
						+ System.currentTimeMillis())));

		// will create the resource if it does not exists, else add metas

		mongoTemplate.save(fromAPI(metaResource));

		return result;
	}

	@Override
	@WebMethod
	public void removeMetadata(Resource resource, Metadata metadata)
			throws MetadataException {
		Update update = new Update();
		update.pull("metadata", metadata);
		mongoTemplate.updateFirst(
				query(where("resource.name").is(resource.getName())
						.and("resource.url").is(resource.getUrl())), update,
				org.ow2.play.metadata.service.document.MetaResource.class);
	}

	@Override
	@WebMethod
	public List<Metadata> getMetaData(Resource resource)
			throws MetadataException {
		org.ow2.play.metadata.service.document.MetaResource mr = mongoTemplate
				.findOne(
						query(where("resource.name").is(resource.getName())
								.and("resource.url").is(resource.getUrl())),
						org.ow2.play.metadata.service.document.MetaResource.class);
		if (mr == null) {
			throw new MetadataException("No such resource");
		}
		if (mr.metadata == null) {
			return new ArrayList<Metadata>(0);
		} else {
			return mr.metadata;
		}
	}

	@Override
	@WebMethod
	public Metadata getMetadataValue(Resource resource, String key)
			throws MetadataException {
		throw new MetadataException("Not implemented");
	}

	@Override
	@WebMethod
	public boolean deleteMetaData(Resource resource) throws MetadataException {
		Update update = new Update();
		update.unset("metadata");
		mongoTemplate.updateFirst(
				query(where("resource.name").is(resource.getName())
						.and("resource.url").is(resource.getUrl())), update,
				org.ow2.play.metadata.service.document.MetaResource.class);
		return true;
	}

	@Override
	@WebMethod
	public List<MetaResource> getResoucesWithMeta(List<Metadata> include)
			throws MetadataException {
		throw new MetadataException("Not implemented");
	}

	@Override
	@WebMethod
	public List<MetaResource> list() throws MetadataException {
		List<org.ow2.play.metadata.service.document.MetaResource> all = mongoTemplate
				.findAll(org.ow2.play.metadata.service.document.MetaResource.class);

		if (all != null) {
			return new ArrayList<MetaResource>(
					Collections2
							.transform(
									all,
									new Function<org.ow2.play.metadata.service.document.MetaResource, MetaResource>() {
										public MetaResource apply(
												org.ow2.play.metadata.service.document.MetaResource input) {
											return toAPI(input);
										}
									}));
		}
		return null;
	}
	
	@Override
	public List<MetaResource> listWhere(String name, String url) throws MetadataException {
		if (name == null && url == null) {
			throw new MetadataException("Null parameters");
		}
		
		Query query = null;
		if (name != null && url == null) {
			query = query(where("resource.name").is(name));
		}
		if (name == null && url != null) {
			query = query(where("resource.url").is(url));
		}
		if (name != null && url != null) {
			query = query(where("resource.name").is(name).and("resource.url").is(url));
		}
		
		List<org.ow2.play.metadata.service.document.MetaResource> mr = mongoTemplate
				.find(query,
						org.ow2.play.metadata.service.document.MetaResource.class);
		
		List<MetaResource> result = new ArrayList<MetaResource>();
		for (org.ow2.play.metadata.service.document.MetaResource metaResource : mr) {
			result.add(toAPI(metaResource));
		}
		return result;
	}

	@Override
	public boolean exists(Resource resource) throws MetadataException {
		if (resource == null || resource.getName() == null
				|| resource.getUrl() == null) {
			throw new MetadataException("Can not search a null object...");
		}
		org.ow2.play.metadata.service.document.MetaResource mr = mongoTemplate
				.findOne(
						query(where("resource.name").is(resource.getName()).and("resource.url")
										.is(resource.getUrl())),
						org.ow2.play.metadata.service.document.MetaResource.class);
		return mr != null;
	}

	/**
	 * Delete a resource from the repository
	 * 
	 * 
	 * @param resource
	 * @throws org.ow2.play.metadata.api.MetadataException
	 * 
	 */
	@Override
	public boolean deleteResource(Resource resource) throws MetadataException {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Removing resource : " + resource);
		}
		mongoTemplate
				.remove(query(where("resource.name").is(resource.getName()).and("resource.url")
						.is(resource.getUrl())), org.ow2.play.metadata.service.document.MetaResource.class);
		return true;
	}

	@Override
	public MetaResource get(String id) throws MetadataException {
		org.ow2.play.metadata.service.document.MetaResource mr = mongoTemplate.findOne(
				query(where("_id").is(id)),
						org.ow2.play.metadata.service.document.MetaResource.class);
		if (mr == null) {
			throw new MetadataException("Resource not found");
		}
		return toAPI(mr);
	}

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	/**
	 * @param metaResource
	 * @return
	 */
	private org.ow2.play.metadata.service.document.MetaResource fromAPI(
			MetaResource metaResource) {
		org.ow2.play.metadata.service.document.MetaResource result = new org.ow2.play.metadata.service.document.MetaResource();
		if (metaResource.getMetadata() != null) {
			result.metadata.addAll(metaResource.getMetadata());
		}
		result.resource = metaResource.getResource();
		return result;
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	private MetaResource toAPI(
			org.ow2.play.metadata.service.document.MetaResource input) {
		MetaResource result = new MetaResource();
		result.setId(input.id.toStringBabble());
		if (input.metadata != null)
			result.setMetadata(input.metadata);
		result.setResource(input.resource);
		return result;
	}

}
