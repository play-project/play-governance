/**
 * 
 */
package org.ow2.play.governance.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jws.WebMethod;
import javax.xml.namespace.QName;
import javax.xml.ws.wsaddressing.W3CEndpointReference;

import org.ow2.play.governance.api.EventGovernance;
import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.bean.Topic;
import org.ow2.play.governance.client.ServiceRegistry;
import org.ow2.play.service.registry.api.Registry;
import org.ow2.play.service.registry.api.RegistryException;
import org.petalslink.dsb.notification.client.http.simple.HTTPProducerRPClient;
import org.petalslink.dsb.notification.commons.NotificationException;

import com.ebmwebsourcing.wsstar.basefaults.datatypes.impl.impl.WsrfbfModelFactoryImpl;
import com.ebmwebsourcing.wsstar.basenotification.datatypes.impl.impl.WsnbModelFactoryImpl;
import com.ebmwebsourcing.wsstar.resource.datatypes.impl.impl.WsrfrModelFactoryImpl;
import com.ebmwebsourcing.wsstar.resourcelifetime.datatypes.impl.impl.WsrfrlModelFactoryImpl;
import com.ebmwebsourcing.wsstar.resourceproperties.datatypes.impl.impl.WsrfrpModelFactoryImpl;
import com.ebmwebsourcing.wsstar.topics.datatypes.impl.impl.WstopModelFactoryImpl;
import com.ebmwebsourcing.wsstar.wsnb.services.impl.util.Wsnb4ServUtils;


/**
 * Get the topics from the DSB runtime (WSN API), old way to get. Please check
 * {@link EventGovernanceService}
 * 
 * @author chamerling
 * 
 */
public class DSBEventGovernanceService implements EventGovernance {

	static Logger logger = Logger.getLogger(DSBEventGovernanceService.class
			.getName());

	private Registry serviceRegistry;

	static {
		// WTF?
		logger.info("Creating WSN factories...");
		Wsnb4ServUtils.initModelFactories(new WsrfbfModelFactoryImpl(),
				new WsrfrModelFactoryImpl(), new WsrfrlModelFactoryImpl(),
				new WsrfrpModelFactoryImpl(), new WstopModelFactoryImpl(),
				new WsnbModelFactoryImpl());
	}

	/**
	 * 
	 */
	public DSBEventGovernanceService() {
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
	public void createTopic(Topic topic) throws GovernanceExeption {
		throw new GovernanceExeption("Not implemented");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.governance.api.EventGovernance#getTopics()
	 */
	@Override
	public List<Topic> getTopics() throws GovernanceExeption {
		logger.fine("Get topics...");

		List<Topic> result = new ArrayList<Topic>();

		if (serviceRegistry == null) {
			throw new GovernanceExeption("Can not get the service regsitry");
		}

		String endpoint = null;
		try {
			endpoint = serviceRegistry
					.get(org.ow2.play.service.registry.api.Constants.TOPIC);
		} catch (RegistryException e1) {
			e1.printStackTrace();

			throw new GovernanceExeption(e1);
		}

		if (endpoint == null) {
			throw new GovernanceExeption(
					"Can not get the topic provider endpoint from the service registry");
		}

		logger.info("Get topics from " + endpoint);

		HTTPProducerRPClient client = new HTTPProducerRPClient(endpoint);
		try {
			List<QName> topics = client.getTopics();
			if (topics != null) {
				for (QName qName : topics) {
					logger.info("Topic : " + qName);

					Topic topic = new Topic();
					topic.setName(qName.getLocalPart());
					topic.setNs(qName.getNamespaceURI());
					topic.setPrefix(qName.getPrefix());
					result.add(topic);
				}
			}
		} catch (NotificationException e) {
			e.printStackTrace();
		}

		return result;
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
	 * @param serviceRegistry
	 *            the serviceRegistry to set
	 */
	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

}
