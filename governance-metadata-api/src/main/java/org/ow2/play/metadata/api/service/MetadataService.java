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
package org.ow2.play.metadata.api.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.Metadata;
import org.ow2.play.metadata.api.MetadataException;
import org.ow2.play.metadata.api.Resource;

/**
 * 
 * @author chamerling
 * 
 */
@WebService(targetNamespace="http://service.metadata.play.ow2.org/")
public interface MetadataService {
	
	/**
	 * Get metadata from its ID
	 * 
	 * @param id
	 * @return
	 * @throws MetadataException
	 */
	@WebMethod
	MetaResource get(String id) throws MetadataException;
	
	/**
	 * Clear all the metadatas
	 * 
	 * @throws MetadataException
	 */
	@WebMethod
	void clear() throws MetadataException;

	/**
	 * Add metadata to a resource. Creates the resource if it do not exists.
	 * 
	 * @param resource
	 * @param metadata
	 * @throws MetadataException
	 */
	@WebMethod
	void addMetadata(Resource resource, Metadata metadata)
			throws MetadataException;

	/**
	 * Set the metadata value to the resource. Create the metadata if it does not exists.
	 * 
	 * @param resource
	 * @param metadata
	 * @throws MetadataException
	 */
	void setMetadata(Resource resource, Metadata metadata) throws MetadataException;
	
	/**
	 * Creates a meta resource ie a resource with all its metadatas...
	 * 
	 * @param metaResource
	 * @return
	 * @throws MetadataException
	 */
	@WebMethod
	boolean create(MetaResource metaResource) throws MetadataException;

	/**
	 * Remove the metadata from the resource
	 * 
	 * @param resource
	 * @param metadata
	 * @throws MetadataException
	 */
	@WebMethod
	void removeMetadata(Resource resource, Metadata metadata)
			throws MetadataException;

	/**
	 * Get all the metadata of the given resource
	 * 
	 * @param resource
	 * @return
	 * @throws MetadataException
	 */
	@WebMethod
	List<Metadata> getMetaData(Resource resource) throws MetadataException;

	/**
	 * Get the metadata value
	 * 
	 * @param resource
	 * @param key
	 * @return
	 * @throws MetadataException
	 */
	@WebMethod
	Metadata getMetadataValue(Resource resource, String key)
			throws MetadataException;

	/**
	 * Delete all the metadata of the given resource
	 * 
	 * @param resource
	 * @return
	 * @throws MetadataException
	 */
	@WebMethod
	boolean deleteMetaData(Resource resource) throws MetadataException;

	/**
	 * Get a list of resources filtered with the metadata entries.
	 * 
	 * @param include
	 *            include this metadata
	 * @return
	 * @throws MetadataException
	 */
	@WebMethod
	List<MetaResource> getResoucesWithMeta(List<Metadata> include)
			throws MetadataException;

	/**
	 * Get all...
	 * 
	 * @return
	 * @throws MetadataException 
	 */
	@WebMethod
	List<MetaResource> list() throws MetadataException;
	
	/**
	 * List all the MetaResource where the resource name is the given one
	 * 
	 * @param name
	 * @return
	 * @throws MetadataException
	 */
	List<MetaResource> listWhere(String name, String url) throws MetadataException;
	
	/**
	 * Check if a resource exists in the repository
	 * 
	 * @param resource
	 * @return
	 * @throws MetadataException
	 */
	@WebMethod
	boolean exists(Resource resource) throws MetadataException;

    /**
     * Delete a resource from the repository
     *
     *
     * @param resource
     * @throws MetadataException
     */
    @WebMethod
    boolean deleteResource(Resource resource) throws MetadataException;
}
