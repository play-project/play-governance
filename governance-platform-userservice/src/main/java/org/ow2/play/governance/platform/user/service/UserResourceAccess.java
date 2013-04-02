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
package org.ow2.play.governance.platform.user.service;

import java.util.Collection;
import java.util.List;

import org.ow2.play.governance.api.EventGovernance;
import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.bean.Topic;
import org.ow2.play.governance.permission.api.PermissionChecker;
import org.ow2.play.governance.resources.TopicHelper;
import org.ow2.play.governance.user.api.bean.User;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * @author chamerling
 * 
 */
public class UserResourceAccess {

	private EventGovernance eventGovernance;

	private PermissionChecker permissionChecker;

	/**
	 * 
	 */
	public UserResourceAccess() {
	}

	/**
	 * Get topics which are available for the given user
	 * 
	 * @param user
	 * @return
	 * @throws GovernanceExeption
	 */
	public List<Topic> getTopicsForUser(final User user)
			throws GovernanceExeption {
		Collection<Topic> filtered = Collections2.filter(eventGovernance.getTopics(),
				new Predicate<Topic>() {
					public boolean apply(Topic topic) {
						// get the topic as resource to check permissions since
						// groups are resources in the platform...
						return permissionChecker
								.checkResource(
										user.login,
										TopicHelper
												.get(topic));
					}
				});
		// send back removing duplicates...
		return Lists.newArrayList(Sets.newHashSet(filtered));
	}

	/**
	 * @param eventGovernance
	 *            the eventGovernance to set
	 */
	public void setEventGovernance(EventGovernance eventGovernance) {
		this.eventGovernance = eventGovernance;
	}

	/**
	 * @param permissionChecker
	 *            the permissionChecker to set
	 */
	public void setPermissionChecker(PermissionChecker permissionChecker) {
		this.permissionChecker = permissionChecker;
	}

}
