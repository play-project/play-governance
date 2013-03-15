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

import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.ow2.play.governance.api.EventGovernance;
import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.bean.Topic;
import org.ow2.play.governance.platform.user.api.rest.bean.Topics;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

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
		
		// get the user from the context and its topics
		// topics from public streams are also available
		// TODO
		
		List<Topic> topics = null;
		try {
			topics = eventGovernance.getTopics();
		} catch (GovernanceExeption e) {
			org.ow2.play.governance.platform.user.api.rest.bean.Error error = new org.ow2.play.governance.platform.user.api.rest.bean.Error();
			error.message = "Can not get topics";
			return Response.serverError().entity(error).build();
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
								return topic;
							}
						});
		return Response.ok(new Topics(Lists.newArrayList(out))).build();
	}

	@Override
	public Response topic(String id) {
		org.ow2.play.governance.platform.user.api.rest.bean.Error error = new org.ow2.play.governance.platform.user.api.rest.bean.Error();
		error.message = "Can not get topic " + id;
		error.cause = "Not implemented";
		return Response.serverError().entity(error).build();
	}

	/**
	 * @param eventGovernance
	 *            the eventGovernance to set
	 */
	public void setEventGovernance(EventGovernance eventGovernance) {
		this.eventGovernance = eventGovernance;
	}
}
