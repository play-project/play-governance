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

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.ow2.play.governance.api.EventGovernance;
import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.bean.Topic;
import org.ow2.play.governance.resources.StreamHelper;
import org.ow2.play.governance.resources.TopicHelper;
import org.ow2.play.governance.user.api.bean.User;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

/**
 * @author chamerling
 *
 */
public class StreamService extends AbstractService implements
		org.ow2.play.governance.platform.user.api.rest.StreamService {

	protected EventGovernance eventGovernance;

	public Response streams() {
		// streams are topics... Get the topics then change them to streams
		
		List<Topic> topics = null;
		try {
			topics = userResourceAccess.getTopicsForUser(getUser());
		} catch (GovernanceExeption e) {
			e.printStackTrace();
			return error(Status.INTERNAL_SERVER_ERROR, "Can not get streams");
		}
		
		Collection<org.ow2.play.governance.platform.user.api.rest.bean.Stream> out = Collections2
				.transform(
						topics,
						new Function<Topic, org.ow2.play.governance.platform.user.api.rest.bean.Stream>() {
							public org.ow2.play.governance.platform.user.api.rest.bean.Stream apply(
									Topic input) {
								
								org.ow2.play.governance.platform.user.api.rest.bean.Stream stream = new org.ow2.play.governance.platform.user.api.rest.bean.Stream();
								stream.id = StreamHelper.getStreamID(input);
								stream.resourceUrl = getResourceURI(input);
								return stream;
							}
						});
		return ok(out
				.toArray(new org.ow2.play.governance.platform.user.api.rest.bean.Stream[out
						.size()]));
	}

	public Response stream(String id) {
		List<Topic> topics = null;
		try {
			topics = eventGovernance.getTopicsFromName(id);
		} catch (GovernanceExeption e) {
			return error(Status.BAD_REQUEST, "Can not get stream with ID " + id);
		}
		
		if (topics.size() == 0) {
			return notFound();
		}
		
		// get the first one...
		Topic input = topics.get(0);
		
		// check access
		final User user = getUser();
		if (permissionChecker.checkResource(user.login, TopicHelper.get(input))) {
			org.ow2.play.governance.platform.user.api.rest.bean.Stream stream = new org.ow2.play.governance.platform.user.api.rest.bean.Stream();
			stream.id = StreamHelper.getStreamID(input);
			stream.resourceUrl = getResourceURI(input);
			return ok(stream);
		} else {
			return unauthorized();
		}
	}
	

	protected String getResourceURI(Topic topic) {
		URI uri = mc.getUriInfo().getBaseUri();
		return uri.toString() + "streams/" + topic.getName();
	}

	/**
	 * @param eventGovernance
	 *            the eventGovernance to set
	 */
	public void setEventGovernance(EventGovernance eventGovernance) {
		this.eventGovernance = eventGovernance;
	}

}
