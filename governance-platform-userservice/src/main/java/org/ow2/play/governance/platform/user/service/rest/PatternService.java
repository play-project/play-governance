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

import static org.ow2.play.governance.platform.user.api.rest.helpers.Response.created;
import static org.ow2.play.governance.platform.user.api.rest.helpers.Response.deleted;
import static org.ow2.play.governance.platform.user.api.rest.helpers.Response.error;
import static org.ow2.play.governance.platform.user.api.rest.helpers.Response.ok;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.ow2.play.governance.platform.user.api.rest.bean.Pattern;

import com.google.common.collect.Lists;

/**
 * 
 * @author chamerling
 * 
 */
public class PatternService extends AbstractService implements
		org.ow2.play.governance.platform.user.api.rest.PatternService {
	
	@Context
	private MessageContext mc;

	public Response patterns() {
		Pattern pattern = new Pattern();
		pattern.data = "ABC";
		pattern.id = UUID.randomUUID().toString();
		pattern.resourceUrl = getResourceURI(pattern);
		
		Pattern pattern2 = new Pattern();
		pattern2.data = "AZE";
		pattern2.id = UUID.randomUUID().toString();
		pattern2.resourceUrl = getResourceURI(pattern2);

		// to array to get a JSON array as output
		List<Pattern> result = Lists.newArrayList(pattern, pattern2);
		return ok(result.toArray(new Pattern[result.size()]));
	}

	public Response pattern(String id) {
		Pattern pattern = new Pattern();
		pattern.data = "ABC";
		pattern.id = id;
		pattern.resourceUrl = getResourceURI(pattern);
		return ok(pattern);
	}

	public Response deploy(Pattern pattern) {
		if (pattern.data == null) {
			return error(Status.BAD_REQUEST, "Pattern data is missing");
		}
		pattern.id = UUID.randomUUID().toString();
		return created(pattern);
	}

	public Response undeploy(String id) {
		return deleted();
	}
	
	protected String getResourceURI(Pattern pattern) {
		URI uri = mc.getUriInfo().getBaseUri();
		return uri.toString() + "patterns/" + pattern.id;
	}

}
