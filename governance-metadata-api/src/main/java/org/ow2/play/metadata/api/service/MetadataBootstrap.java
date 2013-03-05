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

import org.ow2.play.metadata.api.MetadataException;

/**
 * @author chamerling
 * 
 */
@WebService
public interface MetadataBootstrap {

	/**
	 * Initialize the metadata service from a list of metadata resources located
	 * at the given URLs. Init loads all and overrides any existing entries.
	 * 
	 * @param urls
	 * @throws MetadataException
	 */
	@WebMethod
	void init(List<String> urls) throws MetadataException;
	
	/**
	 * Update the resources with the given input data as already retrieved in {@link #init(List)}.
	 * 
	 * @param urls
	 * @throws MetadataException
	 */
	@WebMethod
	void update(List<String> urls) throws MetadataException;
}
