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
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.wsaddressing.W3CEndpointReference;

import org.ow2.play.commons.utils.StreamHelper;
import org.ow2.play.governance.api.Constants;
import org.ow2.play.governance.api.EventGovernance;
import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.SubscriptionRegistry;
import org.ow2.play.governance.api.SubscriptionService;
import org.ow2.play.governance.api.TopicAware;
import org.ow2.play.governance.api.bean.Subscription;
import org.ow2.play.governance.api.bean.Topic;
import org.ow2.play.governance.cxf.CXFHelper;
import org.ow2.play.governance.resources.TopicHelper;
import org.ow2.play.metadata.api.Data;
import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.Metadata;
import org.ow2.play.metadata.api.MetadataException;
import org.ow2.play.metadata.api.Resource;
import org.ow2.play.metadata.api.Type;
import org.ow2.play.metadata.api.service.MetadataService;
import org.ow2.play.service.registry.api.Registry;
import org.ow2.play.service.registry.api.RegistryException;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import fr.inria.eventcloud.webservices.api.EventCloudsManagementWsApi;


/**
 * @author chamerling
 * 
 */
public class EventGovernanceService implements EventGovernance {

    static Logger logger = Logger.getLogger(EventGovernanceService.class.getName());

    /**
     * Uses the registry to get endpoint to reach metadata service
     */
    private Registry serviceRegistry;
    
    private MetadataService metadataService;

    private SubscriptionService subscriptionService;

    private SubscriptionRegistry subscriptionRegistry;
    
    protected WebServiceContext context;
    
    /**
     * 
     */
    public EventGovernanceService() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ow2.play.governance.api.EventGovernance#loadResources(java.io. InputStream)
     */
    @Override
    @WebMethod(exclude = true)
    public void loadResources(InputStream topicNameSpaceInputStream) throws GovernanceExeption {
        throw new GovernanceExeption("Not implemented");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ow2.play.governance.api.EventGovernance#createTopic(eu.playproject
     * .governance.api.bean.Topic)
     */
    @Override
    public String createTopic(Topic topic) throws GovernanceExeption {

        checkRegistry();

        // check if the topic already exists in the registry, so we do not
        // create it
        MetaResource mr = getResourceForTopic(topic);
        if (mr == null) {
            mr = TopicHelper.transform(topic);
            mr = createMetaResource(mr);
        }

        if (mr == null) {
            // we got a serious problem...
            throw new GovernanceExeption(
                "Can not create nor find the topic in the platform, error at the resource level...");
        }

        // add the new topic to the DSB and eventcloud...
        // Do not fail if one operation goes wrong...
        // TODO : Will be better to have a set of listener for this operation
        // each one implementing the createTopic(Topic) operation so we can add more at runtime for example.
        try {
            this.createDSBTopic(topic);
        } catch (Exception e) {
            final String message = "Topic creation failed on DSB '" + topic + "'";
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.WARNING, message, e);
            } else {
                logger.log(Level.WARNING, message);
            }
        }

        try {
            this.createEventCloud(topic);
        } catch (Exception e) {
            final String message = "Topic creation failed on EC '" + topic + "'";
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.WARNING, message, e);
            } else {
                logger.log(Level.WARNING, message);
            }
        }

        // at least we can return the endpoints we created...
        return "FIXME: I just created topic on DSB and new EC instance, that's all...";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ow2.play.governance.api.EventGovernance#createPublisherTopic(org.
     * ow2.play.governance.api.bean.Topic)
     */
    @Override
    public String createPublisherTopic(Topic topic) throws GovernanceExeption {
        checkRegistry();

        QName topicName = new QName(topic.getNs(), topic.getName(), topic.getPrefix());
        String stream = StreamHelper.getStreamName(topicName);

        // check if the topic already exists in the registry, so we do not
        // create it
        MetaResource mr = getResourceForTopic(topic);
        if (mr == null) {
            mr = TopicHelper.transform(topic);
            mr.getMetadata()
                    .add(new Metadata(Constants.TOPIC_MODE, new Data(Type.LITERAL,
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
        this.createDSBTopic(topic);

        // create the topic on the event cloud
        this.createEventCloud(topic);

        // the EC needs to subscribe to the DSB topic ie get the DSB endpoint to
        // subscribe, get the EC which can receive such notification type and
        // send a WSN subscribe to the DSB.

        List<String> publishURLs = getEventCloudClient().getPublishWsnServiceEndpointUrls(stream);

        logger.info("Got some URLs back from the EC : " + publishURLs);

        String subscriber = (publishURLs != null && publishURLs.size() > 0) ? publishURLs.get(0) : null;

        if (subscriber == null) {
            final String message = "Can not find any valid EC endpoint for stream " + stream +
                " even if we created the EC...";
            logger.warning(message);
            throw new GovernanceExeption(message);
        }

        Subscription subscription = new Subscription();
        subscription.setProvider(getEndpoint(org.ow2.play.service.registry.api.Constants.EC_TO_DSB_DSB));
        subscription.setSubscriber(subscriber);
        subscription.setTopic(topic);

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Let's subscribe the EC to DSB for stream " + stream);
            logger.fine("Input Subscription is " + subscription);
        }

        Subscription subscribeResult = subscriptionService.subscribe(subscription);

        logger.fine("EC subscribed to DSB result : " + subscribeResult);

        // store the subscription
        if (subscribeResult != null && subscriptionRegistry != null) {
            subscriptionRegistry.addSubscription(subscribeResult);
        }

        // get the DSB endpoint to subscribe to. For now it is unique but we
        // will have multiple ones when we go distributed...
        return getDSBNotifyEndpoint();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ow2.play.governance.api.EventGovernance#createSubscriberTopic(org
     * .ow2.play.governance.api.bean.Topic)
     */
    @Override
    public String createSubscriberTopic(final Topic topic) throws GovernanceExeption {
        checkRegistry();

        String streamName = StreamHelper.getStreamName(new QName(topic.getNs(), topic.getName(), topic
                .getPrefix()));

        // check if the topic already exists in the registry, so we do not
        // create it again
        MetaResource mr = getResourceForTopic(topic);
        if (mr == null) {
            mr = TopicHelper.transform(topic);
            mr.getMetadata().add(
                    new Metadata(Constants.TOPIC_MODE,
                        new Data(Type.LITERAL, Constants.TOPIC_MODE_SUBSCRIBER)));

            // TODO : Fix constants
            mr.getMetadata().add(
                    new Metadata(org.ow2.play.metadata.api.Constants.COMPLEX_EVENT, new Data(Type.LITERAL,
                        Boolean.toString(true))));
            mr.getMetadata().add(
                    new Metadata(org.ow2.play.metadata.api.Constants.DSB_NEEDS_TO_SUBSCRIBE, new Data(
                        Type.LITERAL, Boolean.toString(true))));

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
            @Override
            public boolean apply(Topic dsbTopic) {
                logger.fine("Checking topic from DSB " + dsbTopic);
                return topic.equals(dsbTopic);
            }
        }).size() > 0) {
            // already have the topic in the DSB
            logger.info("The topic is already available in the DSB, do nto add it " + topic);
        } else {
            // create the topic in the DSB
            logger.info("Adding the topic in the DSB " + topic);
            boolean added = dsbClient.add(topic);
            if (added) {
                //
            }
        }

        // create the topic on the event cloud ie create the event cloud...
        EventCloudsManagementWsApi client = getEventCloudClient();

        logger.fine("Creating the event cloud for stream " + streamName);
        boolean created = client.createEventCloud(streamName);

        // The create operation returns true if the eventcloud has been created,
        // if false it means that it is already created and that we do not need
        // to do it anymore...
        if (created) {
            // creates a default proxy for each interface: pub/sub and put/get
            client.deploySubscribeWsnService(streamName);
            client.deployPublishWsnService(streamName);
            client.deploySubscribeWsProxy(streamName);
            client.deployPublishWsProxy(streamName);
            client.deployPutGetWsProxy(streamName);

            logger.fine("All the proxies have been created on the event cloud");

        } else {
            logger.fine("EventCloud has been already created for stream " + streamName);
        }

        // the DSB needs to subscribe to the EC
        List<String> endpoints = client.getSubscribeWsnServiceEndpointUrls(streamName);
        String dsbEndpoint = getDSBSubscribeToECEndpoint();

        if (endpoints == null || endpoints.size() == 0) {
            logger.fine("Can not find any subscribe endpoint in the EC for stream " + streamName);
        } else {

            Subscription subscription = new Subscription();
            subscription.setProvider(endpoints.get(0));
            subscription.setSubscriber(dsbEndpoint);
            subscription.setTopic(topic);

            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Let's subscribe the DSB to eventcloud for stream " + streamName);
                logger.fine("Subscription is " + subscription);
            }

            // TODO : get the subscription service for EC and not a generic one
            // here...
            Subscription subscribeResult = subscriptionService.subscribe(subscription);

            if (logger.isLoggable(Level.FINE)) {
                logger.fine("DSB subscribed to EC : " + subscribeResult);
            }

            // store the subscription
            if (subscribeResult != null && subscriptionRegistry != null) {
                subscriptionRegistry.addSubscription(subscribeResult);
            }
        }
        // get the DSB endpoint to subscribe to
        return getDSBSubscribeEndpoint();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ow2.play.governance.api.EventGovernance#deleteTopic(org.ow2.play.
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
        
        List<Topic> result = new ArrayList<Topic>();
        List<MetaResource> resources = null;
        try {
            resources = metadataService.listWhere(Constants.STREAM_RESOURCE_NAME, null);
        } catch (Exception e) {
            throw new GovernanceExeption(e);
        }

        if (resources != null) {
            for (MetaResource r : resources) {
                if (logger.isLoggable(Level.INFO)) {
                    logger.info("Resource : " + r.getResource());
                }
				Topic topic = new Topic();
				String ns = r
						.getResource()
						.getUrl()
						.substring(0,
								r.getResource().getUrl().lastIndexOf('/') + 1);
				String name = r
						.getResource()
						.getUrl()
						.substring(
								r.getResource().getUrl().lastIndexOf('/') + 1);
				topic.setName(name);
				topic.setNs(ns);

				List<Metadata> md = new ArrayList<Metadata>(Collections2.filter(r.getMetadata(), new Predicate<Metadata>() {
					@Override
                    public boolean apply(Metadata meta) {
						return meta.getName() != null && meta.getName().equals(Constants.QNAME_PREFIX_URL);
					}
				}));
				
				if (md.size() == 0) {
					topic.setPrefix(org.ow2.play.governance.api.Constants.DEFAULT_PREFIX);
				} else {
					Metadata m = md.get(0);
					if (m != null && m.getData() != null
							&& m.getData().size() == 1
							&& m.getData().get(0).getValue() != null) {
						topic.setPrefix(m.getData().get(0).getValue());
					} else {
						topic.setPrefix(org.ow2.play.governance.api.Constants.DEFAULT_PREFIX);
					}
				}
				result.add(topic);
            }
        }
        return result;
    }
    
    /* (non-Javadoc)
     * @see org.ow2.play.governance.api.EventGovernance#getTopicsFromName(java.lang.String)
     */
    @Override
    public List<Topic> getTopicsFromName(final String topicName) throws GovernanceExeption {
        logger.fine("Get topics with name " + topicName + " from metadata service...");
        List<Topic> result = new ArrayList<Topic>();
       
        // TODO : Real query...
        result.addAll(Collections2.filter(getTopics(), new Predicate<Topic>() {
        	@Override
            public boolean apply(Topic topic) {
        		System.out.println(topic.getName() + " " + topicName);
        		return topic.getName().equals(topicName);
        	}
		}));

    	return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ow2.play.governance.api.EventGovernance#findTopicsByElement(javax
     * .xml.namespace.QName)
     */
    @Override
    public List<QName> findTopicsByElement(QName element) throws GovernanceExeption {
        throw new GovernanceExeption("Not implemented");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ow2.play.governance.api.EventGovernance#findEventProducersByTopics (java.util.List)
     */
    @Override
    public List<W3CEndpointReference> findEventProducersByTopics(List<QName> topics)
            throws GovernanceExeption {
        throw new GovernanceExeption("Not implemented");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ow2.play.governance.api.EventGovernance#findEventProducersByElements
     * (java.util.List)
     */
    @Override
    public List<W3CEndpointReference> findEventProducersByElements(List<QName> element)
            throws GovernanceExeption {
        throw new GovernanceExeption("Not implemented");
    }

    /**
     * Return the resource associated with the given topic if any.
     * 
     * @param topic
     * @return
     * @throws GovernanceExeption
     */
    protected MetaResource getResourceForTopic(Topic topic) throws GovernanceExeption {
        MetaResource result = null;

        Resource resource = TopicHelper.getResource(topic);

        try {
            boolean exists = metadataService.exists(resource);
            if (exists) {
                result = new MetaResource(resource, metadataService.getMetaData(resource));
            } else {
                // let's do it...
                logger.warning("Can not find the resource in the repository " + resource);
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
    protected MetaResource createMetaResource(MetaResource metaresource) throws GovernanceExeption {
        boolean created = false;
        try {
            created = metadataService.create(metaresource);
        } catch (MetadataException e) {
            throw new GovernanceExeption("Can not create the metaresource in the repository", e);
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

    protected String getEndpoint(String id) throws GovernanceExeption {
        String url = null;
        try {
            url = serviceRegistry.get(id);
        } catch (RegistryException e) {
            throw new GovernanceExeption(e);
        }

        if (url == null) {
            throw new GovernanceExeption("Can not find the service associated to " + id);

        }
        return url;
    }

    protected TopicAware getDSBTopicClient() throws GovernanceExeption {
        return CXFHelper.getClientFromFinalURL(
                getEndpoint(org.ow2.play.service.registry.api.Constants.DSB_BUSINESS_TOPIC_MANAGEMENT),
                TopicAware.class);
    }

    /**
     * Where the user needs to subscribe...
     * 
     * @return
     * @throws GovernanceExeption
     */
    protected String getDSBSubscribeEndpoint() throws GovernanceExeption {
        return getEndpoint(org.ow2.play.service.registry.api.Constants.DSB_PRODUCER);
    }

    protected String getDSBNotifyEndpoint() throws GovernanceExeption {
        return getEndpoint(org.ow2.play.service.registry.api.Constants.DSB_CONSUMER);
    }

    protected String getDSBSubscribeToECEndpoint() throws GovernanceExeption {
        return getEndpoint(org.ow2.play.service.registry.api.Constants.DSB_CONSUMER);
    }

    /**
     * FIXME : DSB_TO_EC_EC or EC_TO_DSB, same endpoint in the current case...
     * 
     * @return
     * @throws GovernanceExeption
     */
    protected EventCloudsManagementWsApi getEventCloudClient() throws GovernanceExeption {
        return CXFHelper.getClientFromFinalURL(
                getEndpoint(org.ow2.play.service.registry.api.Constants.DSB_TO_EC_EC),
                EventCloudsManagementWsApi.class);
    }

    /**
     * Create a DSB topic if it does not exists. Note that this operation is not
     * synchronized and that the topic can be added by someone else. But it
     * should not happen...
     * 
     * @param topic
     * @throws GovernanceExeption
     */
    protected void createDSBTopic(final Topic topic) throws GovernanceExeption {
        TopicAware dsbClient = getDSBTopicClient();
        if (Collections2.filter(dsbClient.get(), new Predicate<Topic>() {
            @Override
            public boolean apply(Topic dsbTopic) {
                logger.fine("Checking topic from DSB " + dsbTopic);
                return topic.equals(dsbTopic);
            }
        }).size() > 0) {
            // already have the topic in the DSB
            logger.info("The topic is already available in the DSB, do nto add it " + topic);
        } else {
            // create the topic in the DSB
            logger.info("Adding the topic in the DSB " + topic);
            boolean added = dsbClient.add(topic);
            logger.info("DSB topic creation return result :" + added + " for topic " + topic);
            if (added) {
                //
            }
        }
    }

    /**
     * Create a new event cloud if it does not exists
     * 
     * @param topic
     * @throws GovernanceExeption
     */
    protected void createEventCloud(Topic topic) throws GovernanceExeption {
        String streamName = StreamHelper.getStreamName(new QName(topic.getNs(), topic.getName(), topic
                .getPrefix()));

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Creating the event cloud for stream " + streamName);
        }

        // create the topic on the event cloud ie create the event cloud...
        EventCloudsManagementWsApi client = getEventCloudClient();

        boolean created = client.createEventCloud(streamName);

        // The create operation returns true if the eventcloud has been created,
        // if false it means that it is already created and that we do not need
        // to do it anymore...
        if (created) {
            // creates a default proxy for each interface: pub/sub and put/get
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Creating proxy for the event cloud");
            }

            client.deploySubscribeWsnService(streamName);
            client.deployPublishWsnService(streamName);
            client.deploySubscribeWsProxy(streamName);
            client.deployPublishWsProxy(streamName);
            client.deployPutGetWsProxy(streamName);

        } else {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("EventCloud has been already created for stream " + streamName);
            }
        }
    }

    /**
     * @param serviceRegistry
     *            the serviceRegistry to set
     */
    public void setServiceRegistry(Registry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    public void setSubscriptionService(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    public void setSubscriptionRegistry(SubscriptionRegistry subscriptionRegistry) {
        this.subscriptionRegistry = subscriptionRegistry;
    }
    
    /**
	 * @param metadataService the metadataService to set
	 */
	public void setMetadataService(MetadataService metadataService) {
		this.metadataService = metadataService;
	}

}
