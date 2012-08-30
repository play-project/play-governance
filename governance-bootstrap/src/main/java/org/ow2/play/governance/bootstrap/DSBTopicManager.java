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
package org.ow2.play.governance.bootstrap;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.SubscriptionRegistry;
import org.ow2.play.governance.api.bean.Subscription;
import org.ow2.play.governance.api.bean.Topic;
import org.ow2.play.governance.bootstrap.api.BootstrapFault;
import org.ow2.play.governance.bootstrap.api.TopicManager;
import org.petalslink.dsb.notification.client.http.simple.HTTPProducerClient;
import org.petalslink.dsb.notification.client.http.simple.HTTPSubscriptionManagerClient;
import org.petalslink.dsb.notification.commons.NotificationException;
import org.springframework.beans.factory.access.BootstrapException;

import com.ebmwebsourcing.wsstar.basefaults.datatypes.impl.impl.WsrfbfModelFactoryImpl;
import com.ebmwebsourcing.wsstar.basenotification.datatypes.impl.impl.WsnbModelFactoryImpl;
import com.ebmwebsourcing.wsstar.resource.datatypes.impl.impl.WsrfrModelFactoryImpl;
import com.ebmwebsourcing.wsstar.resourcelifetime.datatypes.impl.impl.WsrfrlModelFactoryImpl;
import com.ebmwebsourcing.wsstar.resourceproperties.datatypes.impl.impl.WsrfrpModelFactoryImpl;
import com.ebmwebsourcing.wsstar.topics.datatypes.impl.impl.WstopModelFactoryImpl;
import com.ebmwebsourcing.wsstar.wsnb.services.impl.util.Wsnb4ServUtils;


/**
 * 
 * @author chamerling
 * 
 */
public class DSBTopicManager implements TopicManager {

	static Logger logger = Logger.getLogger(DSBTopicManager.class.getName());

	static {
		// WTF?
		logger.info("Creating WSN factories...");
		Wsnb4ServUtils.initModelFactories(new WsrfbfModelFactoryImpl(),
				new WsrfrModelFactoryImpl(), new WsrfrlModelFactoryImpl(),
				new WsrfrpModelFactoryImpl(), new WstopModelFactoryImpl(),
				new WsnbModelFactoryImpl());
	}

	private SubscriptionRegistry subscriptionRegistry;

	@Override
	public Subscription subscribe(String producer, QName topic,
			String subscriber) throws BootstrapFault {
		Subscription subscription = null;

		logger.info("Subscribe to topic '" + topic + "' on producer '"
				+ producer + "' for subscriber '" + subscriber + "'");

		HTTPProducerClient client = new HTTPProducerClient(producer);
		try {
			String id = client.subscribe(topic, subscriber);
			logger.info("Subscribed to topic " + topic + " and ID is " + id);

			subscription = new Subscription();
			subscription.setDate(System.currentTimeMillis());
			subscription.setId(id);
			subscription.setProvider(producer);
			subscription.setSubscriber(subscriber);
			Topic t = new Topic();
			t.setName(topic.getLocalPart());
			t.setNs(topic.getNamespaceURI());
			t.setPrefix(topic.getPrefix());
			subscription.setTopic(t);

			this.subscriptionRegistry.addSubscription(subscription);

		} catch (NotificationException e) {
			logger.log(Level.SEVERE, "Problem while subscribing", e);
			throw new BootstrapFault(e);
		} catch (GovernanceExeption e) {
			logger.log(Level.WARNING, "Problem while saving subscription", e);
			//throw new BootstrapFault(e);
		}
		return subscription;
	}

	@Override
	public void unsubscribe(Subscription subscription) throws BootstrapFault {
		logger.fine("Unsubscribe from " + subscription);
		if (subscription == null) {
			throw new BootstrapFault("Bad parameters, null subscription");
		}

		if (subscription.getProvider() == null || subscription.getId() == null) {
			throw new BootstrapFault(
					"Bad parameters, null provider or subscription id");
		}

		HTTPSubscriptionManagerClient client = new HTTPSubscriptionManagerClient(
				subscription.getProvider());
		try {
			boolean result = client.unsubscribe(subscription.getId());
			if (result) {
				this.subscriptionRegistry.remove(subscription);
			}
		} catch (NotificationException e) {
			e.printStackTrace();
		} catch (GovernanceExeption e) {
			e.printStackTrace();
		}

		throw new BootstrapException("Not implemented");
	}

	public void setSubscriptionRegistry(
			SubscriptionRegistry subscriptionRegistry) {
		this.subscriptionRegistry = subscriptionRegistry;
	}
}
