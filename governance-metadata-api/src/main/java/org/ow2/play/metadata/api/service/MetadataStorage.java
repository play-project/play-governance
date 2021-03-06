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
import java.util.Properties;

import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.MetadataException;

/**
 * @author chamerling
 * 
 */
public interface MetadataStorage {

	/**
	 * Initialize the storage
	 * 
	 * @throws MetadataException
	 */
	void init(Properties props) throws MetadataException;
	
	void store(MetaResource metaResource) throws MetadataException;
	
	MetaResource get(String name, String url) throws MetadataException;
	
	List<MetaResource> list() throws MetadataException;
	
}
