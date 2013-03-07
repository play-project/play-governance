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
import org.ow2.play.governance.platform.user.api.rest.bean.SubscriptionResult;
import org.ow2.play.governance.platform.user.api.rest.bean.Subscriptions;
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

		// let's subscribe, store the subscription and send back a response

		SubscriptionResult result = new SubscriptionResult();
		result.initialSubscription = subscription;
		result.subscriptionID = "TODO-" + UUID.randomUUID().toString();

		subscriptions.put(result.subscriptionID, subscription);

		return Response.ok("subscribed").build();
	}

	@Override
	public Response unsubscribe(String subscriptionID) {
		Subscription removed = subscriptions.remove(subscriptionID);
		if (removed == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(removed).build();
	}

	@Override
	public Response subscriptions() {

		// TODO

		List<Subscription> result = new ArrayList<Subscription>();
		result.addAll(subscriptions.values());
		String endUserName = OAuthHelper.resolveUserName(mc);

		return Response.ok(new Subscriptions(result)).build();
	}

	@Override
	public Response subscription(String id) {
		Subscription result = subscriptions.get(id);
		if (result == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(result).build();
	}

}
