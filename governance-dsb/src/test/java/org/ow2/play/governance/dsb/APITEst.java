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
package org.ow2.play.governance.dsb;

import junit.framework.TestCase;

import org.petalslink.dsb.cxf.CXFHelper;
import org.petalslink.dsb.jbi.se.wsn.api.ManagementService;
import org.petalslink.dsb.jbi.se.wsn.api.Subscription;
import org.petalslink.dsb.jbi.se.wsn.api.SubscriptionManagementService;
import org.petalslink.dsb.jbi.se.wsn.api.Topic;

/**
 * @author chamerling
 * 
 */
public class APITEst extends TestCase {

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetTopics() throws Exception {
		SubscriptionManagementService client = CXFHelper
				.getClientFromFinalURL(
						"http://localhost:8079/play-dsb-wsn-package/SubscriptionManagementService",
						SubscriptionManagementService.class);

		//System.out.println(client.getSubscriptions());
		Topic topic = new Topic();
		topic.name = "TaxiUCGeoLocation";
		topic.ns = "http://streams.event-processing.org/ids/";
		topic.prefix = "s";
		System.out.println(client.getSubscriptionsForTopic(topic));
		for (Subscription s : client.getSubscriptionsForTopic(topic)) {
			System.out.println(s.subscriber);
			System.out.println(s.uuid);
			System.out.println(s.topic);
		}

		ManagementService ms = CXFHelper.getClientFromFinalURL(
				"http://localhost:8079/play-dsb-wsn-package/ManagementService",
				ManagementService.class);
		System.out.println(ms.getTopics());
		
		//System.out.println(client.getSubscriptions());
	}
}
