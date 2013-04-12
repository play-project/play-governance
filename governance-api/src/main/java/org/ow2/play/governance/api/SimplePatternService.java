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
import javax.jws.WebService;

import org.ow2.play.governance.api.bean.Pattern;
import org.ow2.play.governance.api.bean.Topic;

/**
 * Simple pattern service just integrate the DCEP stuff but does not create the
 * topics and event clouds.
 * 
 * TODO : Update APIs for use {@link Pattern}
 * 
 * @author chamerling
 * 
 */
@WebService
public interface SimplePatternService {

	/**
	 * Analyze the query, throws an exception is something if bad.
	 * 
	 * @param pattern
	 * @throws GovernanceExeption
	 */
	void analyze(String pattern) throws GovernanceExeption;

	/**
	 * Get input topics from the pattern
	 * 
	 * @param pattern
	 * @return
	 * @throws GovernanceExeption
	 */
	@WebMethod
	List<Topic> getInputTopics(String pattern) throws GovernanceExeption;

	/**
	 * Get output topics from the pattern
	 * 
	 * @param pattern
	 * @return
	 * @throws GovernanceExeption
	 */
	@WebMethod
	List<Topic> getOutputTopics(String pattern) throws GovernanceExeption;

	/**
	 * Deploy a new pattern to the DCEP
	 * 
	 * @param id
	 * @param stattement
	 * @return
	 * @throws GovernanceExeption
	 */
	@WebMethod
	String deploy(String id, String pattern) throws GovernanceExeption;

	/**
	 * Undeploy a pattern from the DCEP
	 * 
	 * @param id
	 * @return
	 * @throws GovernanceExeption
	 */
	@WebMethod
	String undeploy(String id) throws GovernanceExeption;
	
	/**
	 * Get deployed patterns from the DCEP runtime.
	 * 
	 * @return
	 * @throws GovernanceExeption 
	 */
	@WebMethod
	List<Pattern> getPatterns() throws GovernanceExeption;
	
	/**
	 * Get a pattern from its ID
	 * 
	 * @param id
	 * @return the pattern or null if not found
	 * @throws GovernanceExeption
	 */
	@WebMethod
	Pattern getPattern(String id) throws GovernanceExeption;

}
