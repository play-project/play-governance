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
package org.ow2.play.governance.bootstrap.api;

import javax.xml.namespace.QName;

import org.ow2.play.governance.api.bean.Subscription;

/**
 * @author chamerling
 * 
 */
public interface TopicManager {

	/**
	 * Subscribe to a notification producer on behalf of the subscriber
	 * 
	 * @param producer
	 *            to send the subscribe to
	 * @param topic
	 *            the topic to subscribe to
	 * @param subscriber
	 *            who is subscribing ie who will receive notifications?
	 * 
	 * @return the subscription object if we subscribed or null if not
	 * 
	 * @throws BootstrapFault
	 */
	Subscription subscribe(String producer, QName topic, String subscriber)
			throws BootstrapFault;

	/**
	 * Unsubscribe from the producer defined in the subscription. All the needed
	 * information are in the subscription. Up to the implementation to get the
	 * right one. For WSN, subscription ID and provider should be enough...
	 */
	void unsubscribe(Subscription subscription) throws BootstrapFault;

}
