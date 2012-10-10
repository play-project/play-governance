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
package org.ow2.play.governance.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.jws.WebMethod;
import javax.jws.WebParam;

import org.ow2.play.governance.api.SubscriptionRegistry;
import org.ow2.play.governance.api.bean.Subscription;

/**
 * Default in memory implementation
 * 
 * @author chamerling
 * 
 */
@Deprecated
public class InMemorySubscriptionRegistryService implements
		SubscriptionRegistry {

	private static Logger logger = Logger
			.getLogger(InMemorySubscriptionRegistryService.class.getName());

	List<Subscription> subscriptions;

	/**
	 * 
	 */
	public InMemorySubscriptionRegistryService() {
		this.subscriptions = new ArrayList<Subscription>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.playproject.platform.service.bootstrap.api.SubscriptionRegistry#
	 * addSubscription
	 * (eu.playproject.platform.service.bootstrap.api.Subscription)
	 */
	@Override
	public void addSubscription(Subscription subscription) {
		if (subscription != null) {
			subscriptions.add(subscription);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.playproject.platform.service.bootstrap.api.SubscriptionRegistry#
	 * getSubscriptions()
	 */
	@Override
	public List<Subscription> getSubscriptions() {
		return subscriptions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.SubscriptionRegistry#getSubscriptions(org
	 * .ow2.play.governance.api.bean.Subscription)
	 */
	@Override
	@WebMethod
	public List<Subscription> getSubscriptions(
			@WebParam(name = "filter") Subscription filter) {
		// NOT implemented, use mongo implementation....
		return new ArrayList<Subscription>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.playproject.platform.service.bootstrap.api.SubscriptionRegistry#
	 * unsubscribe ()
	 */
	@Override
	public List<Subscription> removeAll() {
		List<Subscription> result = new ArrayList<Subscription>();
		Iterator<Subscription> it = this.subscriptions.iterator();

		while (it.hasNext()) {
			Subscription s = it.next();
			it.remove();
			result.add(s);
		}
		return result;
	}

	@Override
	public boolean remove(Subscription subscription) {
		logger.info("Remove from " + subscription);
		return subscriptions.remove(subscription);
	}

	@Override
	public List<Subscription> removeAllFromConsumer(String consumer) {
		logger.info("Remove from consumer " + consumer);

		List<Subscription> result = new ArrayList<Subscription>();

		Iterator<Subscription> iter = this.subscriptions.iterator();
		while (iter.hasNext()) {
			Subscription subscription = iter.next();

			if (subscription.getSubscriber() != null
					&& subscription.getSubscriber().equals(consumer)) {
				iter.remove();
				result.add(subscription);
			}
		}
		return result;
	}

	@Override
	public List<Subscription> removeAllFromProvider(String provider) {
		logger.info("Remove from provider " + provider);

		List<Subscription> result = new ArrayList<Subscription>();

		Iterator<Subscription> iter = this.subscriptions.iterator();
		while (iter.hasNext()) {
			Subscription subscription = iter.next();

			if (subscription.getSubscriber() != null
					&& subscription.getProvider().equals(provider)) {
				iter.remove();
				result.add(subscription);
			}
		}
		return result;
	}
}
