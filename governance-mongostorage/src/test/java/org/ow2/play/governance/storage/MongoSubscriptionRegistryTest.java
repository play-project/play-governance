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
package org.ow2.play.governance.storage;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

import junit.framework.TestCase;

import org.ow2.play.governance.api.bean.Subscription;
import org.ow2.play.governance.api.bean.Topic;

/**
 * @author chamerling
 * 
 */
public class MongoSubscriptionRegistryTest extends TestCase {

	MongoSubscriptionRegistry registry;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		registry = new MongoSubscriptionRegistry();
		Properties props = new Properties();
		props.setProperty("mongo.collection", "subscriptionstoragetest");
		registry.setProperties(props);
		registry.init();
		registry.clearCollection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		registry.clearCollection();
	}

	public void testAdd() throws Exception {

		Topic topic = new Topic();
		topic.setName("foo");
		topic.setNs("http://bar");
		topic.setPrefix("s");
		Subscription subscription = new Subscription(UUID.randomUUID()
				.toString(), "http://subscriber", "http://provider", topic,
				System.currentTimeMillis());
		registry.addSubscription(subscription);

		// TODO
		// get with mongo api...
	}

	public void testGetSubscriptions() throws Exception {
		Topic topic = new Topic();
		topic.setName("foo");
		topic.setNs("http://bar");
		topic.setPrefix("s");

		for (int i = 0; i < 10; i++) {
			Subscription subscription = new Subscription(UUID.randomUUID()
					.toString() + "---" + i, "http://subscriber",
					"http://provider", topic, System.currentTimeMillis());

			registry.addSubscription(subscription);
		}
		List<Subscription> subscriptions = registry.getSubscriptions();
		assertNotNull(subscriptions);

		for (Subscription subscription : subscriptions) {
			System.out.println(subscription);
		}
		assertEquals(10, subscriptions.size());
	}

	public void testRemoveAll() throws Exception {
		Topic topic = new Topic();
		topic.setName("foo");
		topic.setNs("http://bar");
		topic.setPrefix("s");

		for (int i = 0; i < 100; i++) {
			Subscription subscription = new Subscription(UUID.randomUUID()
					.toString() + "---" + i, "http://subscriber",
					"http://provider", topic, System.currentTimeMillis());

			registry.addSubscription(subscription);
		}
		List<Subscription> subscriptions = registry.getSubscriptions();
		assertNotNull(subscriptions);
		assertTrue(subscriptions.size() > 0);

		registry.removeAll();

		assertEquals(0, registry.getSubscriptions().size());
	}

	public void testRemoveFromSubscriber() throws Exception {

		Topic topic = new Topic();
		topic.setName("foo");
		topic.setNs("http://bar");
		topic.setPrefix("s");

		for (int i = 0; i < 3; i++) {
			Subscription subscription = new Subscription(UUID.randomUUID()
					.toString(), "http://subscriber", "http://provider" + i,
					topic, System.currentTimeMillis());
			registry.addSubscription(subscription);
		}

		for (int i = 0; i < 10; i++) {
			Subscription subscription = new Subscription(UUID.randomUUID()
					.toString(), "http://subscriber" + i, "http://provider",
					topic, System.currentTimeMillis());
			registry.addSubscription(subscription);
		}

		assertEquals(13, registry.getSubscriptions().size());
		registry.removeAllFromConsumer("http://subscriber");
		assertEquals(10, registry.getSubscriptions().size());

		List<Subscription> remain = registry.getSubscriptions();
		for (Subscription subscription : remain) {
			if ("http://subcriber".equals(subscription.getProvider())) {
				fail("It remains a subscription in the collection!!!");
			}
		}

	}

	public void testRemoveFromProducer() throws Exception {

		Topic topic = new Topic();
		topic.setName("foo");
		topic.setNs("http://bar");
		topic.setPrefix("s");

		for (int i = 0; i < 3; i++) {
			Subscription subscription = new Subscription(UUID.randomUUID()
					.toString(), "http://subscriber", "http://provider" + i,
					topic, System.currentTimeMillis());
			registry.addSubscription(subscription);
		}

		for (int i = 0; i < 10; i++) {
			Subscription subscription = new Subscription(UUID.randomUUID()
					.toString(), "http://subscriber" + i, "http://provider",
					topic, System.currentTimeMillis());
			registry.addSubscription(subscription);
		}

		assertEquals(13, registry.getSubscriptions().size());
		registry.removeAllFromProvider("http://provider");
		assertEquals(3, registry.getSubscriptions().size());

		// TODO : Guava should have the stuff to handle that

		List<Subscription> remain = registry.getSubscriptions();
		for (Subscription subscription : remain) {
			if ("http://provider".equals(subscription.getProvider())) {
				fail("It remains a subscription in the collection!!!");
			}
		}
	}

	public void testGetFilteredSubscriptions() throws Exception {
		Topic topic = new Topic();
		topic.setName("foo");
		topic.setNs("http://bar");
		topic.setPrefix("s");

		for (int i = 0; i < 20; i++) {
			Subscription subscription = new Subscription(UUID.randomUUID()
					.toString(), "http://subscriber" + i, "http://provider",
					topic, System.currentTimeMillis());
			registry.addSubscription(subscription);
		}

		List<Subscription> filtered = registry.getSubscriptions(null);
		assertEquals(20, filtered.size());

		// get all from provider
		Subscription filter = new Subscription();
		filter.setProvider("http://provider");

		filtered = registry.getSubscriptions(filter);
		assertEquals(20, filtered.size());
		
		// get all from provider and consumer
		filter = new Subscription();
		filter.setProvider("http://provider");
		filter.setSubscriber("http://subscriber0");

		filtered = registry.getSubscriptions(filter);
		assertEquals(1, filtered.size());
		
		// get all from bad provider
		filter = new Subscription();
		filter.setProvider("http://fooooo");

		filtered = registry.getSubscriptions(filter);
		assertEquals(0, filtered.size());
		
		// get all from a provider and bad topic
		filter = new Subscription();
		filter.setProvider("http://provider");
		Topic t = new Topic();
		t.setName("123");
		filter.setTopic(t);

		filtered = registry.getSubscriptions(filter);
		assertEquals(0, filtered.size());
		
		// get all from topic
		filter = new Subscription();
		Topic tt = new Topic();
		tt.setName("foo");
		filter.setTopic(tt);

		filtered = registry.getSubscriptions(filter);
		assertEquals(20, filtered.size());
		
		topic = new Topic();
		topic.setName("fooo");
		topic.setNs("http://bar");
		topic.setPrefix("s");

		Subscription subscription = new Subscription(UUID.randomUUID()
				.toString(), "http://subscriberFOO", "http://provider",
				topic, System.currentTimeMillis());
		registry.addSubscription(subscription);
		
		filter = new Subscription();
		Topic ttt = new Topic();
		ttt.setName("fooo");
		filter.setTopic(ttt);

		filtered = registry.getSubscriptions(filter);
		assertEquals(1, filtered.size());
		
		// filter on NS
		filter = new Subscription();
		Topic tttt = new Topic();
		tttt.setNs("http://bar");
		filter.setTopic(tttt);

		filtered = registry.getSubscriptions(filter);
		assertEquals(21, filtered.size());
		
	}
}
