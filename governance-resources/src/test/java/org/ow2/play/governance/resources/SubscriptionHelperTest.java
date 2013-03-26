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
package org.ow2.play.governance.resources;

import static org.junit.Assert.*;

import org.junit.Test;
import org.ow2.play.governance.api.Constants;

/**
 * @author chamerling
 * 
 */
public class SubscriptionHelperTest {

	@Test
	public void testGenerateURI() {
		assertEquals(Constants.SUBCRIPTION_PREFIX_URL + "123#"
				+ Constants.SUBCRIPTION_RESOURCE_NAME,
				SubscriptionHelper.getSubscriptionURL("123"));
	}
	
	@Test
	public void testGetID() {
		assertEquals("123", SubscriptionHelper.getSubscriptionID(Constants.SUBCRIPTION_PREFIX_URL + "123#"
				+ Constants.SUBCRIPTION_RESOURCE_NAME));
	}
	
	@Test
	public void isSubscription() {
		assertTrue(SubscriptionHelper
				.isSubscription("http://subscriptions.play.ow2.org/e35d8363-5328-435b-b587-d6bb25c19b46#subscription"));
	}
}
