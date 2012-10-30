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
package org.ow2.play.governance.api;

import java.util.List;

import javax.jws.WebMethod;

import org.ow2.play.metadata.api.Metadata;

/**
 * Service to manage CEP patterns.
 * 
 * @author chamerling
 * 
 */
public interface ComplexPatternService {

	/**
	 * Deploy a new pattern. This will create all that is needed in the platform
	 * ie subscriptions, topics, event clouds, etc...
	 * 
	 * @param id
	 *            the pattern id. Generated if null.
	 * @param pattern
	 *            the pattern as string
	 * @param metadata
	 *            attached metadata, can be null
	 * @return a list of metadata representing the resources created by the
	 *         pattern deployment
	 * 
	 * @throws GovernanceExeption
	 */
	@WebMethod
	List<Metadata> deploy(String id, String pattern, List<Metadata> metadata)
			throws GovernanceExeption;

	/**
	 * Undeploy a pattern
	 * 
	 * @param id
	 *            the pattern ID to undeploy. Linked with the pattern ID of
	 *            {@link #deploy(String, String, List)}
	 * @return
	 * @throws GovernanceExeption
	 */
	@WebMethod
	List<Metadata> undeploy(String id) throws GovernanceExeption;
}
