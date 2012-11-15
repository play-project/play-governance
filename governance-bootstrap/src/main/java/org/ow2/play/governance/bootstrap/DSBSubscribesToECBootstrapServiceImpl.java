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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.ow2.play.commons.utils.StreamHelper;
import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.SubscriptionRegistry;
import org.ow2.play.governance.api.bean.Subscription;
import org.ow2.play.governance.api.bean.Topic;
import org.ow2.play.governance.bootstrap.api.BootstrapFault;
import org.ow2.play.governance.bootstrap.api.BootstrapService;
import org.ow2.play.governance.bootstrap.api.EventCloudClientFactory;
import org.ow2.play.governance.bootstrap.api.GovernanceClient;
import org.ow2.play.governance.bootstrap.api.LogService;
import org.ow2.play.governance.bootstrap.api.TopicManager;
import org.ow2.play.metadata.api.Data;
import org.ow2.play.metadata.api.MetadataException;
import org.ow2.play.metadata.api.Resource;
import org.ow2.play.metadata.api.service.MetadataService;

import fr.inria.eventcloud.webservices.api.EventCloudManagementServiceApi;

/**
 * Creates all the subscriptions between the DSB and the EventCloud. The DSB
 * subscribes to all complex 'topics' provided by the EventCloud so it receives
 * WSN notifications when new messages are published in the EventCloud by CEP
 * for example.
 * 
 * @author chamerling
 * @author lpellegr
 */
public class DSBSubscribesToECBootstrapServiceImpl implements BootstrapService {

	private static Logger logger = Logger
			.getLogger(DSBSubscribesToECBootstrapServiceImpl.class.getName());

	private TopicManager topicManager;

	private EventCloudClientFactory eventCloudClientFactory;

	private GovernanceClient governanceClient;

	private SubscriptionRegistry subscriptionRegistry;

	private MetadataService metadataServiceClient;

	@Override
	public List<Subscription> bootstrap(String eventCloudEndpoint,
			String subscriberEndpoint) throws BootstrapFault {
		List<Subscription> result = new ArrayList<Subscription>();

		if (eventCloudEndpoint == null) {
			throw new BootstrapFault(
					"Can not find any EventCloud endpoint, please check the settings");
		}

		if (subscriberEndpoint == null) {
			throw new BootstrapFault(
					"Can not find any subscriber endpoint, please check the settings");
		}

		List<Topic> topics;
		try {
			topics = governanceClient.getTopics();
		} catch (GovernanceExeption e) {
			throw new BootstrapFault("Can not get topics", e);
		}

		if (topics == null || topics.size() == 0) {
			throw new BootstrapFault(
					"Can not get any topic from the governance");
		}

		for (Topic topic : topics) {
			try {
				Subscription subscription = createResources(topic,
						eventCloudEndpoint, subscriberEndpoint);
				if (subscription != null) {
					result.add(subscription);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	protected Subscription createResources(Topic topic,
			String eventCloudEndpoint, String subscriberEndpoint)
			throws BootstrapFault {
		Subscription result = null;

		LogService log = MemoryLogServiceImpl.get();
		QName topicName = new QName(topic.getNs(), topic.getName(),
				topic.getPrefix());

		logger.info("Let's do it for topic " + topic);

		EventCloudManagementServiceApi client = eventCloudClientFactory
				.getClient(eventCloudEndpoint);

		// creates an eventcloud...
		String streamName = StreamHelper.getStreamName(topicName);

		boolean created = client.createEventCloud(streamName);

		// The create operation returns true if the eventcloud has been created,
		// if false it means that it is already created and that we do not need
		// to do it anymore...
		if (created) {
			// creates a default proxy for each interface: pub/sub and put/get
			String subscribeEndpoint = client.createSubscribeProxy(streamName);
			client.createPublishProxy(streamName);
			client.createPutGetProxy(streamName);
			log.log("EventCloud has been created for stream %s", streamName);
		} else {
			log.log("EventCloud has been already created for stream %s",
					streamName);
		}

		// check if it is necessary to subscribe to the eventcloud...
		if (needsToSubscribe(streamName)) {
			// check if we already subscribed...
			if (alreadySubscribed(topic, eventCloudEndpoint, subscriberEndpoint)) {
				log.log("DSB already subscribed to EC for topic %s", topic);
				logger.info(String.format("Already subscribed to topic %s",
						topic));
				return result;
			} else {
				// let's subscribe on behalf of the DSB if it is a topic for
				// complex events
				// let's get the subscribe proxy endpoint
				List<String> endpoints = client
						.getSubscribeProxyEndpointUrls(streamName);
				if (endpoints == null || endpoints.size() == 0) {
					log.log("Can not find any subscribe endpoint in the EC for stream %s",
							streamName);
				} else {
					log.log("Let's subscribe to eventcloud for stream %s",
							streamName);

					result = topicManager.subscribe(endpoints.get(0),
							topicName, subscriberEndpoint);
					log.log("DSB subscribed to EC : " + result);
				}
			}
		} else {
			log.log("Do not need to subscribe to eventcloud for stream %s",
					streamName);
		}

		return result;
	}

	/**
	 * Have a look to the current subscriptions if someone already exists... For
	 * the DSB we just check that we have a subscription where the topic and the
	 * subscriber (dsb) are already in the list.
	 * 
	 * @param topic
	 * @param eventCloudEndpoint
	 * @param subscriberEndpoint
	 * @return
	 */
	protected boolean alreadySubscribed(Topic topic, String eventCloudEndpoint,
			String subscriberEndpoint) {

		List<Subscription> subscriptions = null;
		try {
			subscriptions = this.subscriptionRegistry
					.getSubscriptions();
		} catch (GovernanceExeption e) {
			if (logger.isLoggable(Level.FINE)) {
				logger.log(Level.WARNING, "Got an error while getting subscriptions", e);
			} else {
				logger.warning("Got an error while getting subscriptions");
			}
			return false;
		}
		
		if (subscriptions == null) {
			return false;
		}
		
		Iterator<Subscription> iter = subscriptions.iterator();
		boolean found = false;
		while (iter.hasNext() && !found) {
			Subscription subscription = iter.next();
			found = subscription.getTopic().equals(topic)
					&& subscription.getSubscriber().equals(subscriberEndpoint);
		}

		return found;
	}

	/**
	 * Subscribes only if the dsbneedstosubscribe meta is not null and set to
	 * true.
	 * 
	 * @param stream
	 * @return
	 */
	protected boolean needsToSubscribe(String stream) {
		if (stream == null) {
			return false;
		}
		
		// Note: We have a stream which is composed by the topic name + #stream...
		// let's project into resource...

		Resource r = new Resource("stream", stream);
		org.ow2.play.metadata.api.Metadata metadata = null;
		try {
			metadata = metadataServiceClient.getMetadataValue(r,
					"http://www.play-project.eu/xml/ns/dsbneedstosubscribe");
		} catch (MetadataException e) {
			return false;
		}
		return metadata != null
				&& metadata.getData().contains(new Data("literal", "true"));
	}

	public void setTopicManager(TopicManager topicManager) {
		this.topicManager = topicManager;
	}

	public void setEventCloudClientFactory(
			EventCloudClientFactory eventCloudClientFactory) {
		this.eventCloudClientFactory = eventCloudClientFactory;
	}

	public void setGovernanceClient(GovernanceClient governanceClient) {
		this.governanceClient = governanceClient;
	}

	public void setSubscriptionRegistry(
			SubscriptionRegistry subscriptionRegistry) {
		this.subscriptionRegistry = subscriptionRegistry;
	}

	/**
	 * @param metadataServiceClient
	 *            the metadataServiceClient to set
	 */
	public void setMetadataServiceClient(MetadataService metadataServiceClient) {
		this.metadataServiceClient = metadataServiceClient;
	}

}
