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
package org.ow2.play.governance.platform.user.service.rest;

import static org.ow2.play.governance.platform.user.api.rest.helpers.Response.error;
import static org.ow2.play.governance.platform.user.api.rest.helpers.Response.notFound;
import static org.ow2.play.governance.platform.user.api.rest.helpers.Response.ok;
import static org.ow2.play.governance.platform.user.api.rest.helpers.Response.unauthorized;

import java.net.URI;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.ow2.play.governance.api.EventGovernance;
import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.bean.Topic;
import org.ow2.play.governance.user.api.bean.User;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

/**
 * @author chamerling
 * 
 */
public class TopicService extends AbstractService implements
		org.ow2.play.governance.platform.user.api.rest.TopicService {

	@Context
	private MessageContext mc;

	private EventGovernance eventGovernance;
	
	@Override
	public Response topics() {

		// filter the topics based on the user permissions
		// TODO : Topics per user can be found before instead of calling N times the permission checker...
		List<Topic> topics = null;
		try {
			topics = userResourceAccess.getTopicsForUser(getUser(mc));
		} catch (GovernanceExeption e) {
			e.printStackTrace();
			return error(Status.INTERNAL_SERVER_ERROR, "Can not get topics");

		}

		Collection<org.ow2.play.governance.platform.user.api.rest.bean.Topic> out = Collections2
				.transform(
						topics,
						new Function<Topic, org.ow2.play.governance.platform.user.api.rest.bean.Topic>() {
							public org.ow2.play.governance.platform.user.api.rest.bean.Topic apply(
									Topic input) {
								org.ow2.play.governance.platform.user.api.rest.bean.Topic topic = new org.ow2.play.governance.platform.user.api.rest.bean.Topic();
								topic.name = input.getName();
								topic.ns = input.getNs();
								topic.prefix = input.getPrefix();
								topic.resourceUrl = getResourceURI(input);
								return topic;
							}
						});
		
		return ok(out
				.toArray(new org.ow2.play.governance.platform.user.api.rest.bean.Topic[out
						.size()]));
	}

	@Override
	public Response topic(String id) {
		
		List<Topic> topics = null;
		try {
			topics = eventGovernance.getTopicsFromName(id);
		} catch (GovernanceExeption e) {
			return error(Status.BAD_REQUEST, "Can not get topic with ID " + id);
		}
		
		if (topics.size() == 0) {
			return notFound();
		}
		
		// get the first one...
		Topic input = topics.get(0);
		
		// check access
		final User user = getUser(mc);
		if (permissionChecker.checkResource(user.login, org.ow2.play.governance.api.helpers.ResourceHelper.get(input))) {
			org.ow2.play.governance.platform.user.api.rest.bean.Topic topic = new org.ow2.play.governance.platform.user.api.rest.bean.Topic();
			topic.name = input.getName();
			topic.ns = input.getNs();
			topic.prefix = input.getPrefix();
			topic.resourceUrl = getResourceURI(input);
			return ok(topic);
		} else {
			return unauthorized();
		}
	}

	protected String getResourceURI(Topic topic) {
		URI uri = mc.getUriInfo().getBaseUri();
		return uri.toString() + "topics/" + topic.getName();
	}

	/**
	 * @param eventGovernance
	 *            the eventGovernance to set
	 */
	public void setEventGovernance(EventGovernance eventGovernance) {
		this.eventGovernance = eventGovernance;
	}

}
