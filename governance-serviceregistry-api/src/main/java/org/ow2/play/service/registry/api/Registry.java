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
package org.ow2.play.service.registry.api;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * A simple key/value registry
 * 
 * @author chamerling
 * 
 */
@WebService
public interface Registry {
	
	/**
	 * Intialize the registry
	 * 
	 * @throws RegistryException
	 */
	@WebMethod
	void init() throws RegistryException;

	/**
	 * Get an URL from its key
	 * 
	 * @param key
	 * @return
	 */
	@WebMethod
	String get(String key) throws RegistryException;

	/**
	 * Put a key/value pair. Create entry of key does not already exists, update
	 * if exists
	 * 
	 * @param key
	 * @param value
	 */
	@WebMethod
	void put(String key, String value) throws RegistryException;

	/**
	 * Get all the keys
	 * 
	 * @return
	 */
	@WebMethod
	List<String> keys() throws RegistryException;
	
	/**
	 * Get all the entries of the registry
	 * 
	 * @return
	 * @throws RegistryException
	 */
	@WebMethod
	List<Entry> entries() throws RegistryException;

	/**
	 * Clear all the entries
	 */
	@WebMethod
	void clear() throws RegistryException;

	/**
	 * Load from a properties file...
	 * 
	 * @param url
	 */
	@WebMethod
	void load(String url) throws RegistryException;
}
