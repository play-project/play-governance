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
package org.ow2.play.governance.bootstrap.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.SubscriptionRegistry;
import org.ow2.play.governance.bootstrap.api.rest.SubscriptionRegistryService;
import org.ow2.play.governance.bootstrap.api.rest.beans.Subscriptions;


/**
 * @author chamerling
 * 
 */
public class SubscriptionRegistryServiceImpl implements
		SubscriptionRegistryService {

	SubscriptionRegistry subscriptionRegistry;

	@Override
	public Response all() {
		try {
			return Response.ok(
					new Subscriptions(subscriptionRegistry.getSubscriptions()))
					.build();
		} catch (GovernanceExeption e) {
			e.printStackTrace();
			return Response.status(Status.SERVICE_UNAVAILABLE).build();
		}
	}

	@Override
	public Response clear() {
		try {
			return Response.ok(
					new Subscriptions(subscriptionRegistry.removeAll()))
					.build();
		} catch (GovernanceExeption e) {
			e.printStackTrace();
			return Response.status(Status.SERVICE_UNAVAILABLE).build();
		}
	}

	@Override
	public Response clearAllFromSubscriber(String url) {
		try {
			return Response.ok(
					new Subscriptions(subscriptionRegistry
							.removeAllFromConsumer(url))).build();
		} catch (GovernanceExeption e) {
			e.printStackTrace();
			return Response.status(Status.SERVICE_UNAVAILABLE).build();
		}
	}

	@Override
	public Response clearAllFromProvider(String url) {
		try {
			return Response.ok(
					new Subscriptions(subscriptionRegistry
							.removeAllFromProvider(url))).build();
		} catch (GovernanceExeption e) {
			e.printStackTrace();
			return Response.status(Status.SERVICE_UNAVAILABLE).build();
		}
	}

	public void setSubscriptionRegistry(
			SubscriptionRegistry subscriptionRegistry) {
		this.subscriptionRegistry = subscriptionRegistry;
	}

}
