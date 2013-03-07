/**
 *
 * Copyright (c) 2013, Linagora
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

import org.ow2.play.governance.api.bean.Group;
import org.ow2.play.governance.api.bean.Topic;

/**
 * @author chamerling
 * 
 */
@WebService
public interface AccessRightsService {

	/**
	 * Get all the groups for the given topic
	 * 
	 * @param topic
	 * @return
	 * @throws GovernanceExeption
	 */
	@WebMethod
	List<Group> getGroups(Topic topic) throws GovernanceExeption;

	/**
	 * Get all the topics which have the given group
	 * 
	 * @param group
	 * @return
	 * @throws GovernanceExeption
	 */
	@WebMethod
	List<Topic> getTopics(Group group) throws GovernanceExeption;

	/**
	 * Add group to the given topic (if not already set)
	 * 
	 * @param topic
	 * @param group
	 * @throws GovernanceExeption
	 */
	@WebMethod
	void addGroup(Topic topic, Group group) throws GovernanceExeption;

	/**
	 * Remove a group from the topic (is exists)
	 * 
	 * @param topic
	 *            the topic to remove group from
	 * @param group
	 *            the group to add
	 * @throws GovernanceExeption
	 */
	@WebMethod
	void removeGroup(Topic topic, Group group) throws GovernanceExeption;

}
