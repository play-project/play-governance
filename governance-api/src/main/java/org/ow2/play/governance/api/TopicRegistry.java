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

import org.ow2.play.governance.api.bean.Topic;
import org.ow2.play.metadata.api.Metadata;

/**
 * A raw topic service. Does not create nor query any other resource than topic in the topic registry.
 * 
 * @author chamerling
 *
 */
public interface TopicRegistry {
	
	/**
	 * Create a topic with a given sey of properties
	 * 
	 * @param topic
	 * @param properties
	 * @return
	 */
	boolean create(Topic topic, List<Metadata> properties) throws GovernanceExeption;
	
	/**
	 * Get all the topics
	 * 
	 * @return
	 * @throws GovernanceException
	 */
	List<Topic> getTopics() throws GovernanceExeption;

	/**
	 * Check is the topic is tagged as active.
	 * 
	 * @param topic
	 * @return
	 * @throws GovernanceExeption
	 */
	boolean isActive(Topic topic) throws GovernanceExeption;
	
	/**
	 * Get the properties of the given topic
	 * 
	 * @param topic
	 * @return
	 * @throws GovernanceExeption
	 */
	List<Metadata> getProperties(Topic topic) throws GovernanceExeption;
	
	/**
	 * Set a list of properties to the topic. Override existing values. Does not
	 * delete other ones.
	 * 
	 * @param topic
	 * @param properties
	 * @return
	 * @throws GovernanceExeption
	 *             if the topic does not exist or if something bad occurs.
	 */
	boolean setProperties(Topic topic, List<Metadata> properties) throws GovernanceExeption;
}
