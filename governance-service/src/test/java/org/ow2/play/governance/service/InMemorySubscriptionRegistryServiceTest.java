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

import org.ow2.play.governance.api.bean.Subscription;
import org.ow2.play.governance.api.bean.Topic;

import junit.framework.TestCase;

/**
 * @author chamerling
 * 
 */
public class InMemorySubscriptionRegistryServiceTest extends TestCase {

	public void testAdd() throws Exception {
		InMemorySubscriptionRegistryService service = new InMemorySubscriptionRegistryService();
		service.addSubscription(new Subscription("id", "subscriber",
				"provider", new Topic(), System.currentTimeMillis()));

		assertNotNull(service.subscriptions);
		assertEquals(1, service.subscriptions.size());
	}

	public void testAddNull() throws Exception {
		InMemorySubscriptionRegistryService service = new InMemorySubscriptionRegistryService();
		service.addSubscription(null);

		assertNotNull(service.subscriptions);
		assertEquals(0, service.subscriptions.size());
	}

	public void testRemoveAll() throws Exception {
		InMemorySubscriptionRegistryService service = new InMemorySubscriptionRegistryService();
		service.addSubscription(new Subscription("id", "subscriber",
				"provider", new Topic(), System.currentTimeMillis()));

		service.removeAll();

		assertEquals(0, service.subscriptions.size());
	}

	public void testRemoveGoodValue() throws Exception {

		Subscription s = new Subscription("id", "subscriber", "provider",
				new Topic(), System.currentTimeMillis());

		InMemorySubscriptionRegistryService service = new InMemorySubscriptionRegistryService();
		service.addSubscription(s);

		service.remove(s);

		assertEquals(0, service.subscriptions.size());

	}

	public void testRemoveBadValue() throws Exception {

		Subscription s = new Subscription("id", "subscriber1", "provider",
				new Topic(), System.currentTimeMillis());
		Subscription s2 = new Subscription("id", "subscriber2", "provider",
				new Topic(), System.currentTimeMillis());

		InMemorySubscriptionRegistryService service = new InMemorySubscriptionRegistryService();
		service.addSubscription(s);

		service.remove(s2);

		assertEquals(1, service.subscriptions.size());
	}

	public void testRemoveAllFromConsumer() throws Exception {
		Subscription s = new Subscription("id", "subscriber1", "provider1",
				new Topic(), System.currentTimeMillis());
		Subscription s2 = new Subscription("id", "subscriber2", "provider2",
				new Topic(), System.currentTimeMillis());

		InMemorySubscriptionRegistryService service = new InMemorySubscriptionRegistryService();
		service.addSubscription(s);
		service.addSubscription(s2);

		service.removeAllFromConsumer("subscriber1");

		assertEquals(1, service.subscriptions.size());
	}

	public void testRemoveAllFromProvider() throws Exception {

		Subscription s = new Subscription("id", "subscriber1", "provider1",
				new Topic(), System.currentTimeMillis());
		Subscription s2 = new Subscription("id", "subscriber2", "provider2",
				new Topic(), System.currentTimeMillis());

		InMemorySubscriptionRegistryService service = new InMemorySubscriptionRegistryService();
		service.addSubscription(s);
		service.addSubscription(s2);

		service.removeAllFromProvider("provider2");

		assertEquals(1, service.subscriptions.size());

	}
}
