/**
 * 
 */
package eu.playproject.governance.service;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.jws.WebMethod;
import javax.xml.namespace.QName;
import javax.xml.ws.wsaddressing.W3CEndpointReference;

import org.petalslink.dsb.notification.client.http.simple.HTTPProducerRPClient;
import org.petalslink.dsb.notification.commons.NotificationException;

import com.ebmwebsourcing.wsstar.basefaults.datatypes.impl.impl.WsrfbfModelFactoryImpl;
import com.ebmwebsourcing.wsstar.basenotification.datatypes.impl.impl.WsnbModelFactoryImpl;
import com.ebmwebsourcing.wsstar.resource.datatypes.impl.impl.WsrfrModelFactoryImpl;
import com.ebmwebsourcing.wsstar.resourcelifetime.datatypes.impl.impl.WsrfrlModelFactoryImpl;
import com.ebmwebsourcing.wsstar.resourceproperties.datatypes.impl.impl.WsrfrpModelFactoryImpl;
import com.ebmwebsourcing.wsstar.topics.datatypes.impl.impl.WstopModelFactoryImpl;
import com.ebmwebsourcing.wsstar.wsnb.services.impl.util.Wsnb4ServUtils;

import eu.playproject.governance.api.EventGovernance;
import eu.playproject.governance.api.GovernanceExeption;
import eu.playproject.governance.api.bean.Topic;

/**
 * @author chamerling
 * 
 */
public class EventGovernanceService implements EventGovernance {

	static Logger logger = Logger.getLogger(EventGovernanceService.class
			.getName());
	
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
	public EventGovernanceService() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.playproject.governance.api.EventGovernance#loadResources(java.io.
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
	 * eu.playproject.governance.api.EventGovernance#createTopic(eu.playproject
	 * .governance.api.bean.Topic)
	 */
	@Override
	public void createTopic(Topic topic) throws GovernanceExeption {
		GovernanceEngine.getInstance().createTopic(topic);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.playproject.governance.api.EventGovernance#getTopics()
	 */
	@Override
	public List<Topic> getTopics() {
		logger.fine("Get topics...");
		
		List<Topic> result = new ArrayList<Topic>();
		Properties props = null;
		try {
			props = new Properties();
			URL url = new URL(eu.playproject.governance.Constants.CONFIG);
			props.load(url.openStream());
		} catch (Exception e1) {
			e1.printStackTrace();
			return result;
		}

		String endpoint = props
				.getProperty(eu.playproject.governance.Constants.TOPIC_ENDPOINT);

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
	 * eu.playproject.governance.api.EventGovernance#findTopicsByElement(javax
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
	 * eu.playproject.governance.api.EventGovernance#findEventProducersByTopics
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
	 * eu.playproject.governance.api.EventGovernance#findEventProducersByElements
	 * (java.util.List)
	 */
	@Override
	public List<W3CEndpointReference> findEventProducersByElements(
			List<QName> element) throws GovernanceExeption {
		throw new GovernanceExeption("Not implemented");
	}

}
