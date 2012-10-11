/**
 * 
 */
package org.ow2.play.governance.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jws.WebMethod;
import javax.xml.namespace.QName;
import javax.xml.ws.wsaddressing.W3CEndpointReference;

import org.ow2.play.governance.Adapter;
import org.ow2.play.governance.Helper;
import org.ow2.play.governance.api.EventGovernance;
import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.SubscriptionRegistry;
import org.ow2.play.governance.api.SubscriptionService;
import org.ow2.play.governance.api.TopicAware;
import org.ow2.play.governance.api.bean.Subscription;
import org.ow2.play.governance.api.bean.Topic;
import org.ow2.play.governance.client.ServiceRegistry;
import org.ow2.play.metadata.api.Data;
import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.Metadata;
import org.ow2.play.metadata.api.MetadataException;
import org.ow2.play.metadata.api.Resource;
import org.ow2.play.metadata.api.Type;
import org.ow2.play.metadata.api.service.MetadataService;
import org.ow2.play.service.registry.api.Registry;
import org.ow2.play.service.registry.api.RegistryException;
import org.petalslink.dsb.cxf.CXFHelper;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import eu.playproject.commons.utils.StreamHelper;
import fr.inria.eventcloud.webservices.api.EventCloudManagementServiceApi;

/**
 * @author chamerling
 * 
 */
public class EventGovernanceService implements EventGovernance {

	static Logger logger = Logger.getLogger(EventGovernanceService.class
			.getName());

	/**
	 * Uses the registry to get endpoint to reach metadata service
	 */
	private Registry serviceRegistry;
	
	private SubscriptionService subscriptionService;
	
	private SubscriptionRegistry subscriptionRegistry;

	/**
	 * 
	 */
	public EventGovernanceService() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.governance.api.EventGovernance#loadResources(java.io.
	 * InputStream)
	 */
	@Override
	@WebMethod(exclude = true)
	public void loadResources(InputStream topicNameSpaceInputStream)
			throws GovernanceExeption {
		throw new GovernanceExeption("Not implemented");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.EventGovernance#createTopic(eu.playproject
	 * .governance.api.bean.Topic)
	 */
	@Override
	public String createTopic(Topic topic) throws GovernanceExeption {

		checkRegistry();

		// check if the topic already exists in the registry, so we do not
		// create it
		MetaResource mr = getResourceForTopic(topic);
		if (mr == null) {
			mr = Adapter.transform(topic);
			// TODO : add more metadata
			mr = createMetaResource(mr);
		}

		if (mr == null) {
			// we got a serious problem...
			throw new GovernanceExeption(
					"Can not create nor find the topic in the platform, error at the resource level...");
		}

		// add the new topic to the DSB and eventcloud...

		// get the DSB subscribe endpoint and send it back. This endpoint can be
		// used directly or in the SubscriptionService

		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.EventGovernance#createPublisherTopic(org.
	 * ow2.play.governance.api.bean.Topic)
	 */
	@Override
	public String createPublisherTopic(Topic topic) throws GovernanceExeption {
		checkRegistry();

		// check if the topic already exists in the registry, so we do not
		// create it
		MetaResource mr = getResourceForTopic(topic);
		if (mr == null) {
			mr = Adapter.transform(topic);
			mr.getMetadata().add(
					new Metadata(Constants.TOPIC_CREATED_AT, new Data(
							Type.LITERAL, "" + System.currentTimeMillis())));
			mr.getMetadata().add(
					new Metadata(Constants.TOPIC_MODE, new Data(Type.LITERAL,
							Constants.TOPIC_MODE_PUBLISHER)));
			mr = createMetaResource(mr);
		}

		if (mr == null) {
			// we got a serious problem, we can not create nor get the
			// resource...
			throw new GovernanceExeption(
					"Can not create nor find the topic in the platform, error at the resource level...");
		}

		// create the topic on the DSB

		// create the topic on the event cloud

		// the EC needs to subscribe to the DSB

		// get the DSB endpoint to notify to

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.EventGovernance#createSubscriberTopic(org
	 * .ow2.play.governance.api.bean.Topic)
	 */
	@Override
	public String createSubscriberTopic(final Topic topic)
			throws GovernanceExeption {
		checkRegistry();

		QName topicName = new QName(topic.getNs(), topic.getName(),
				topic.getPrefix());
		String streamName = StreamHelper.getStreamName(topicName);

		// check if the topic already exists in the registry, so we do not
		// create it again
		MetaResource mr = getResourceForTopic(topic);
		if (mr == null) {
			mr = Adapter.transform(topic);
			mr.getMetadata().add(
					new Metadata(Constants.TOPIC_CREATED_AT, new Data(
							Type.LITERAL, "" + System.currentTimeMillis())));
			mr.getMetadata().add(
					new Metadata(Constants.TOPIC_MODE, new Data(Type.LITERAL,
							Constants.TOPIC_MODE_SUBSCRIBER)));

			// TODO : Fix constants
			mr.getMetadata().add(
					new Metadata(
							"http://www.play-project.eu/xml/ns/complexEvents",
							new Data(Type.LITERAL, "true")));
			mr.getMetadata()
					.add(new Metadata(
							"http://www.play-project.eu/xml/ns/dsbneedstosubscribe",
							new Data(Type.LITERAL, "true")));

			mr = createMetaResource(mr);
		}

		if (mr == null) {
			// we got a serious problem...
			throw new GovernanceExeption(
					"Can not create nor find the topic in the platform, error at the resource level...");
		}

		// create the topic on the DSB if needed
		TopicAware dsbClient = getDSBTopicClient();
		if (Collections2.filter(dsbClient.get(), new Predicate<Topic>() {
			public boolean apply(Topic dsbTopic) {
				logger.fine("Checking topic from DSB " + dsbTopic);
				return topic.equals(dsbTopic);
			}
		}).size() > 0) {
			// already have the topic in the DSB
			logger.info("The topic is already available in the DSB, do nto add it "
					+ topic);
		} else {
			// create the topic in the DSB
			logger.info("Adding the topic in the DSB " + topic);
			boolean added = dsbClient.add(topic);
			if (added) {
				//
			}
		}

		// create the topic on the event cloud ie create the event cloud...
		EventCloudManagementServiceApi client = getEventCloudClient();

		logger.fine("Creating the event cloud for stream " + streamName);
		boolean created = client.createEventCloud(streamName);

		// The create operation returns true if the eventcloud has been created,
		// if false it means that it is already created and that we do not need
		// to do it anymore...
		if (created) {
			// creates a default proxy for each interface: pub/sub and put/get
			String subscribeEndpoint = client.createSubscribeProxy(streamName);
			client.createPublishProxy(streamName);
			client.createPutGetProxy(streamName);

			logger.fine("All the proxies have been created on the event cloud");

		} else {
			logger.fine("EventCloud has been already created for stream "
					+ streamName);
		}

		// the DSB needs to subscribe to the EC
		List<String> endpoints = client
				.getSubscribeProxyEndpointUrls(streamName);
		String dsbEndpoint = getDSBSubscribeToECEndpoint();

		if (endpoints == null || endpoints.size() == 0) {
			logger.fine("Can not find any subscribe endpoint in the EC for stream "
					+ streamName);
		} else {

			Subscription subscription = new Subscription();
			subscription.setProvider(endpoints.get(0));
			subscription.setSubscriber(dsbEndpoint);
			subscription.setTopic(topic);

			if (logger.isLoggable(Level.FINE)) {
				logger.fine("Let's subscribe the DSB to eventcloud for stream "
						+ streamName);
				logger.fine("Subscription is " + subscription);
			}

			Subscription subscribeResult = subscriptionService
					.subscribe(subscription);
			
			logger.fine("DSB subscribed to EC : " + subscribeResult);
			
			// store the subscription
			if (subscribeResult != null && subscriptionRegistry != null) {
				subscriptionRegistry.addSubscription(subscription);
			}
		}
		// get the DSB endpoint to subscribe to
		return getDSBSubscribeEndpoint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.EventGovernance#deleteTopic(org.ow2.play.
	 * governance.api.bean.Topic)
	 */
	@Override
	public boolean deleteTopic(Topic topic) throws GovernanceExeption {
		// this means that we need to delete it from the platform, then delete
		// from the topic registry.
		logger.fine("Delete topic " + topic);
		throw new GovernanceExeption("Not implemented");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.governance.api.EventGovernance#getTopics()
	 */
	@Override
	public List<Topic> getTopics() throws GovernanceExeption {
		logger.fine("Get topics from metadata service...");

		checkRegistry();

		List<Topic> result = new ArrayList<Topic>();

		String endpoint = null;
		try {
			endpoint = serviceRegistry
					.get(org.ow2.play.service.registry.api.Constants.METADATA);
		} catch (RegistryException e1) {
			e1.printStackTrace();
			throw new GovernanceExeption(e1);
		}

		if (endpoint == null) {
			throw new GovernanceExeption(
					"Can not get the metadata provider endpoint from the service registry");
		}

		logger.info("Getting topics from " + endpoint);

		MetadataService client = getMetadataClient(endpoint);
		// FIXME : Get all for now, we need to only get the resources which are
		// topics
		List<MetaResource> resources = null;
		try {
			resources = client.list();
		} catch (Exception e) {
			throw new GovernanceExeption(e);
		}

		if (resources != null) {
			for (MetaResource r : resources) {
				logger.info("Resource : " + r.getResource());
				// TODO : Get the prefix from the metaresources, for now we only
				// get the topic from the resource name where the name is
				// 'stream'

				if (Constants.STREAM_RESOURCE_NAME.equals(r.getResource()
						.getName())) {
					Topic topic = new Topic();
					String ns = r
							.getResource()
							.getUrl()
							.substring(
									0,
									r.getResource().getUrl().lastIndexOf('/') + 1);
					String name = r
							.getResource()
							.getUrl()
							.substring(
									r.getResource().getUrl().lastIndexOf('/') + 1);
					topic.setName(name);
					topic.setNs(ns);

					Metadata md = null;
					try {
						md = client.getMetadataValue(r.getResource(),
								Constants.QNAME_PREFIX_URL);
					} catch (Exception e) {
						logger.info("Get not get metadata from service");
					}
					if (md != null && md.getData() != null
							&& md.getData().size() == 1
							&& md.getData().get(0).getValue() != null) {
						topic.setPrefix(md.getData().get(0).getValue());
					} else {
						topic.setPrefix("s");
					}
					result.add(topic);
				} else {
					logger.info("Not a topic");
				}
			}
		}
		return result;
	}

	/**
	 * @param endpoint
	 * @return
	 */
	protected MetadataService getMetadataClient(String endpoint) {
		return CXFHelper.getClientFromFinalURL(endpoint, MetadataService.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.EventGovernance#findTopicsByElement(javax
	 * .xml.namespace.QName)
	 */
	@Override
	public List<QName> findTopicsByElement(QName element)
			throws GovernanceExeption {
		throw new GovernanceExeption("Not implemented");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.EventGovernance#findEventProducersByTopics
	 * (java.util.List)
	 */
	@Override
	public List<W3CEndpointReference> findEventProducersByTopics(
			List<QName> topics) throws GovernanceExeption {
		throw new GovernanceExeption("Not implemented");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.EventGovernance#findEventProducersByElements
	 * (java.util.List)
	 */
	@Override
	public List<W3CEndpointReference> findEventProducersByElements(
			List<QName> element) throws GovernanceExeption {
		throw new GovernanceExeption("Not implemented");
	}

	/**
	 * Return the resource associated with the given topic if any.
	 * 
	 * @param topic
	 * @return
	 * @throws GovernanceExeption
	 */
	protected MetaResource getResourceForTopic(Topic topic)
			throws GovernanceExeption {
		String endpoint = null;
		MetaResource result = null;

		try {
			endpoint = serviceRegistry
					.get(org.ow2.play.service.registry.api.Constants.METADATA);
		} catch (RegistryException e1) {
			e1.printStackTrace();
			throw new GovernanceExeption(e1);
		}

		if (endpoint == null) {
			throw new GovernanceExeption(
					"Can not get the metadata provider endpoint from the service registry");
		}

		Resource resource = Helper.getResource(topic);
		MetadataService client = getMetadataClient(endpoint);

		try {
			boolean exists = client.exists(resource);
			if (exists) {
				result = new MetaResource(resource,
						client.getMetaData(resource));
			} else {
				// let's do it...
				logger.warning("Can not find the resource in the repository "
						+ resource);
			}
		} catch (MetadataException e) {
			throw new GovernanceExeption(e);
		}

		return result;
	}

	/**
	 * Create the resource for a topic
	 * 
	 * @param topic
	 * @return
	 * @throws GovernanceExeption
	 */
	protected MetaResource createMetaResource(MetaResource metaresource)
			throws GovernanceExeption {
		String endpoint = null;

		// register the resource in the platform
		try {
			endpoint = serviceRegistry
					.get(org.ow2.play.service.registry.api.Constants.METADATA);
		} catch (RegistryException e1) {
			throw new GovernanceExeption(e1);
		}

		if (endpoint == null) {
			throw new GovernanceExeption(
					"Can not get the metadata provider endpoint from the service registry");
		}

		MetadataService client = getMetadataClient(endpoint);
		boolean created;
		try {
			created = client.create(metaresource);
		} catch (MetadataException e) {
			throw new GovernanceExeption(
					"Can not create the metaresource in the repository", e);
		}

		if (!created) {
			// throw exception
			throw new GovernanceExeption("Can not create the metaresource");
		}
		return metaresource;
	}

	protected void checkRegistry() throws GovernanceExeption {
		if (serviceRegistry == null) {
			throw new GovernanceExeption("Can not get the service regsitry");
		}
	}

	protected TopicAware getDSBTopicClient() throws GovernanceExeption {
		String url = null;
		try {
			url = serviceRegistry
					.get(org.ow2.play.service.registry.api.Constants.DSB_BUSINESS_TOPIC_MANAGEMENT);
		} catch (RegistryException e) {
			throw new GovernanceExeption(e);
		}
		if (url == null) {
			throw new GovernanceExeption(
					"Can not find the service associated to "
							+ org.ow2.play.service.registry.api.Constants.DSB_BUSINESS_TOPIC_MANAGEMENT);

		}
		return CXFHelper.getClientFromFinalURL(url, TopicAware.class);
	}

	/**
	 * Where the user needs to subscribe...
	 * 
	 * @return
	 * @throws GovernanceExeption
	 */
	protected String getDSBSubscribeEndpoint() throws GovernanceExeption {
		String url = null;
		try {
			url = serviceRegistry
					.get(org.ow2.play.service.registry.api.Constants.DSB_PRODUCER);
		} catch (RegistryException e) {
			throw new GovernanceExeption(e);
		}
		if (url == null) {
			throw new GovernanceExeption(
					"Can not find the service associated to "
							+ org.ow2.play.service.registry.api.Constants.DSB_PRODUCER);

		}
		return url;
	}

	protected String getDSBSubscribeToECEndpoint() throws GovernanceExeption {
		String url = null;
		try {
			url = serviceRegistry
					.get(org.ow2.play.service.registry.api.Constants.DSB_TO_EC_EC_SUBSCRIBER);
		} catch (RegistryException e) {
			throw new GovernanceExeption(e);
		}
		if (url == null) {
			throw new GovernanceExeption(
					"Can not find the service associated to "
							+ org.ow2.play.service.registry.api.Constants.DSB_TO_EC_EC_SUBSCRIBER);

		}
		return url;
	}

	protected EventCloudManagementServiceApi getEventCloudClient()
			throws GovernanceExeption {
		String url = null;
		try {
			url = serviceRegistry
					.get(org.ow2.play.service.registry.api.Constants.DSB_TO_EC_EC);
		} catch (RegistryException e) {
			throw new GovernanceExeption(e);
		}
		if (url == null) {
			throw new GovernanceExeption(
					"Can not find the service associated to "
							+ org.ow2.play.service.registry.api.Constants.DSB_TO_EC_EC);

		}
		return CXFHelper.getClientFromFinalURL(url,
				EventCloudManagementServiceApi.class);
	}
	
	/**
	 * @param serviceRegistry
	 *            the serviceRegistry to set
	 */
	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}
	
	public void setSubscriptionService(SubscriptionService subscriptionService) {
		this.subscriptionService = subscriptionService;
	}
	
	public void setSubscriptionRegistry(SubscriptionRegistry subscriptionRegistry) {
		this.subscriptionRegistry = subscriptionRegistry;
	}

}
