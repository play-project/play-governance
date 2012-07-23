/**
 * 
 */
package eu.playproject.governance.api;

import java.io.InputStream;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.wsaddressing.W3CEndpointReference;

import eu.playproject.governance.api.bean.Topic;

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
	 * 
	 * @param topic
	 * @throws GovernanceExeption
	 */
	@WebMethod
	void createTopic(Topic topic) throws GovernanceExeption;

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
