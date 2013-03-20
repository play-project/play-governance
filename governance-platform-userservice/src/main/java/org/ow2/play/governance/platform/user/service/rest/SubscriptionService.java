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
import static org.ow2.play.governance.platform.user.api.rest.helpers.Response.notFound;
import static org.ow2.play.governance.platform.user.api.rest.helpers.Response.ok;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.ow2.play.governance.platform.user.api.rest.bean.Subscription;
import org.ow2.play.governance.platform.user.service.oauth.OAuthHelper;

/**
 * @author chamerling
 * 
 */
public class SubscriptionService extends AbstractService implements
		org.ow2.play.governance.platform.user.api.rest.SubscriptionService {

	/**
	 * Tmp, use storage instead
	 */
	Map<String, Subscription> subscriptions = new HashMap<String, Subscription>();

	@Context
	private MessageContext mc;

	@Override
	public Response subscribe(Subscription subscription) {
		
		if (subscription == null || subscription.resource == null || subscription.subscriber == null) {
			return error(Status.BAD_REQUEST, "Wrong input parameters");
		}

		// let's subscribe, store the subscription and send back a response

		subscription.subscriptionID = "TODO-" + UUID.randomUUID().toString();
		subscription.resourceUrl = getResourceURI(subscription);
		subscriptions.put(subscription.subscriptionID, subscription);
		
		return created(subscription);
	}

	@Override
	public Response unsubscribe(String subscriptionID) {
		if (subscriptionID == null) {
			return error(Status.BAD_REQUEST, "subcription ID is mandatary");
		}
		
		Subscription removed = subscriptions.remove(subscriptionID);
		if (removed == null) {
			return notFound();
		}
		return deleted();
	}

	@Override
	public Response subscriptions() {

		// TODO

		List<Subscription> result = new ArrayList<Subscription>();
		result.addAll(subscriptions.values());
		String endUserName = OAuthHelper.resolveUserName(mc);

		return ok(result.toArray(new Subscription[result.size()]));
	}

	@Override
	public Response subscription(String id) {
		Subscription result = subscriptions.get(id);
		if (result == null) {
			return notFound();
		}
		return ok(result);
	}
	
	protected String getResourceURI(Subscription subscription) {
		URI uri = mc.getUriInfo().getBaseUri();
		return uri.toString() + "subcriptions/" + subscription.subscriptionID;
	}

}
