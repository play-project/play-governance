/**
 * 
 */
package org.ow2.play.governance.api;

import java.io.InputStream;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.wsaddressing.W3CEndpointReference;

import org.ow2.play.governance.api.bean.Topic;


/**
 * easiergov/event-api
 * 
 * The Core Governance API of the PLAY platform
 * 
 * @author chamerling
 * 
 */
@WebService
public interface EventGovernance {

	/**
	 * Load topicNamespace into the governance platform. The topic namespace
	 * input stream must be well formed.
	 * 
	 * @param topicNameSpaceInputStream
	 * @throws GovernanceExeption
	 */
	void loadResources(InputStream topicNameSpaceInputStream)
			throws GovernanceExeption;

	/**
	 * Create a topic. This will register it in the governance, deploy it in the
	 * platform (ie topic in DSB and new EC instance)
	 * 
	 * @param topic
	 *            the topic to create
	 * @return the {@link SubscriptionService} endpoint for the created topic.
	 *         You can subscribe and unsubscribe from this endpoint.
	 * @throws GovernanceExeption
	 *             if something is bad...
	 */
	@WebMethod
	String createTopic(Topic topic) throws GovernanceExeption;
	
	/**
	 * Create a topic used to pusblish to. ie we will send notification to the
	 * returned endpoint. In the current version, it creates a DSB topic, an
	 * Event Cloud and then the EC subscribes to the DSB topic by giving where
	 * it want to receive messages published to the DSB topic.
	 * 
	 * @param topic
	 *            the topic to create
	 * @return the endpoint to send notifications to
	 * @throws GovernanceExeption
	 */
	@WebMethod
	String createPublisherTopic(Topic topic) throws GovernanceExeption;
	
	/**
	 * Create a topic used to subscribe to. In the current implementation, it
	 * creates a DSB topic, an Event Cloud and the DSB subscribes to the Event
	 * Cloud.
	 * 
	 * @param topic
	 *            the topic to create
	 * @return the endpoint to subscribe to to receive notifications
	 * @throws GovernanceExeption
	 */
	@WebMethod
	String createSubscriberTopic(Topic topic) throws GovernanceExeption;
	
	/**
	 * Delete a Topic, use me with care...
	 * 
	 * @param topic the topic to delete
	 * @return
	 * @throws GovernanceExeption
	 */
	@WebMethod
	boolean deleteTopic(Topic topic) throws GovernanceExeption;

	/**
	 * Get all the topics...
	 * 
	 * @return
	 */
	@WebMethod
	List<Topic> getTopics() throws GovernanceExeption;

	/**
	 * 
	 * @param element
	 * @return a list of all topics (defined by a QName) which produces an
	 *         element with the given name.
	 * @throws GovernanceExeption
	 */
	@WebMethod
	List<QName> findTopicsByElement(QName element) throws GovernanceExeption;

	/**
	 * 
	 * @param topics
	 * @return a list of endpoints of all event producers which support the
	 *         given topics.
	 * @throws GovernanceExeption
	 */
	@WebMethod
	List<W3CEndpointReference> findEventProducersByTopics(List<QName> topics)
			throws GovernanceExeption;

	/**
	 * 
	 * @param element
	 * @return a list of endpoints of all events producers which support the
	 *         given element.
	 * @throws GovernanceExeption
	 */
	@WebMethod
	List<W3CEndpointReference> findEventProducersByElements(List<QName> element)
			throws GovernanceExeption;

}
