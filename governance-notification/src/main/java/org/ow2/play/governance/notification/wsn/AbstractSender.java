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
package org.ow2.play.governance.notification.wsn;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.xml.namespace.QName;

import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.WSNTopicNotificationSender;
import org.ow2.play.governance.api.bean.Topic;
import org.petalslink.dsb.notification.client.http.simple.HTTPConsumerClient;
import org.petalslink.dsb.notification.commons.NotificationException;
import org.w3c.dom.Document;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

/**
 * @author chamerling
 * 
 */
public abstract class AbstractSender implements WSNTopicNotificationSender {

	private LoadingCache<String, HTTPConsumerClient> clients;

	/**
	 * 
	 */
	public AbstractSender() {
		this.clients = CacheBuilder
				.newBuilder()
				.maximumSize(100)
				.expireAfterWrite(10L, TimeUnit.MINUTES)
				.removalListener(
						new RemovalListener<String, HTTPConsumerClient>() {
							public void onRemoval(
									RemovalNotification<String, HTTPConsumerClient> notification) {
								System.out
										.println("Client removed from cache for endpoint "
												+ notification.getKey());
							}
						}).build(new CacheLoader<String, HTTPConsumerClient>() {
					public HTTPConsumerClient load(String endpoint) {
						System.out.println("Getting new client for endpoint "
								+ endpoint);
						return new HTTPConsumerClient(endpoint);
					}
				});
	}

	@Override
	public void notify(String endpoint, Topic topic, String message)
			throws GovernanceExeption {
		this.send(endpoint, topic, translate(message));
	}

	/**
	 * 
	 * @param endpoint
	 * @param topic
	 * @param payload
	 */
	protected void send(String endpoint, Topic topic, Document payload)
			throws GovernanceExeption {
		try {
			this.getClient(endpoint).notify(payload, getQName(topic));
		} catch (NotificationException e) {
			throw new GovernanceExeption("Send message problem", e);
		}
	}

	/**
	 * Translate the message into an XML document. Up to the implementation to
	 * do it the right way according to their own constraints and rules.
	 * 
	 * @param message
	 * @return
	 */
	protected abstract Document translate(String message)
			throws GovernanceExeption;

	/**
	 * Get a WSN client. TODO : Cache it per endpoint (guava)
	 * 
	 * @param endpoint
	 * @return
	 */
	protected HTTPConsumerClient getClient(String endpoint)
			throws GovernanceExeption {
		try {
			return clients.get(endpoint);
		} catch (ExecutionException e) {
			throw new GovernanceExeption("Can not get client from cache", e);
		}
	}

	protected QName getQName(Topic topic) {
		return new QName(topic.getNs(), topic.getName(), topic.getPrefix());
	}

}
