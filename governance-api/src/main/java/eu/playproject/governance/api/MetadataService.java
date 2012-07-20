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
package eu.playproject.governance.api;

import java.util.List;

import javax.jws.WebMethod;

import eu.playproject.governance.api.bean.Metadata;
import eu.playproject.governance.api.bean.Resource;

/**
 * @author chamerling
 * 
 */
public interface MetadataService {

	@WebMethod
	void addMetadata(Resource resource, Metadata metadata)
			throws GovernanceExeption;

	@WebMethod
	void removeMetadata(Resource resource, Metadata metadata)
			throws GovernanceExeption;

	@WebMethod
	List<Metadata> getMetaData(Resource resource) throws GovernanceExeption;

	@WebMethod
	Metadata getMetadataValue(Resource resource, String key)
			throws GovernanceExeption;

	@WebMethod
	boolean deleteMetaData(Resource resource) throws GovernanceExeption;

	/**
	 * Get a list of resources filtered with the metadata entries.
	 * 
	 * @param include
	 *            include this metadata
	 * @return
	 * @throws GovernanceExeption
	 */
	@WebMethod
	public List<Resource> getResourcesWithMeta(List<Metadata> include)
			throws GovernanceExeption;

}
