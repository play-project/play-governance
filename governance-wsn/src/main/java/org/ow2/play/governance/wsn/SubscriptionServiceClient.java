/**
 * 
 */
package org.ow2.play.governance.wsn;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jws.WebMethod;
import javax.xml.namespace.QName;

import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.SubscriptionService;
import org.ow2.play.governance.api.bean.Subscription;
import org.ow2.play.governance.api.bean.Topic;
import org.petalslink.dsb.notification.client.http.simple.HTTPProducerClient;
import org.petalslink.dsb.notification.commons.NotificationException;

import com.ebmwebsourcing.wsstar.basefaults.datatypes.impl.impl.WsrfbfModelFactoryImpl;
import com.ebmwebsourcing.wsstar.basenotification.datatypes.impl.impl.WsnbModelFactoryImpl;
import com.ebmwebsourcing.wsstar.resource.datatypes.impl.impl.WsrfrModelFactoryImpl;
import com.ebmwebsourcing.wsstar.resourcelifetime.datatypes.impl.impl.WsrfrlModelFactoryImpl;
import com.ebmwebsourcing.wsstar.resourceproperties.datatypes.impl.impl.WsrfrpModelFactoryImpl;
import com.ebmwebsourcing.wsstar.topics.datatypes.impl.impl.WstopModelFactoryImpl;
import com.ebmwebsourcing.wsstar.wsnb.services.impl.util.Wsnb4ServUtils;

/**
 * WSN client for pubsub. Can also be exposed as web service.
 * 
 * @author chamerling
 * 
 */
public class SubscriptionServiceClient implements SubscriptionService {

	static Logger logger = Logger.getLogger(SubscriptionServiceClient.class
			.getName());

	static {
		// WTF?
		logger.info("Creating WSN factories...");
		Wsnb4ServUtils.initModelFactories(new WsrfbfModelFactoryImpl(),
				new WsrfrModelFactoryImpl(), new WsrfrlModelFactoryImpl(),
				new WsrfrpModelFactoryImpl(), new WstopModelFactoryImpl(),
				new WsnbModelFactoryImpl());
	}

	@Override
	@WebMethod
	public Subscription subscribe(Subscription subscription)
			throws GovernanceExeption {
		if (subscription == null) {
			throw new GovernanceExeption(
					"Subscription information can not be null");
		}

		Subscription result = null;

		logger.info("Subscribe to topic '" + subscription.getTopic()
				+ "' on producer '" + subscription.getProvider()
				+ "' for subscriber '" + subscription.getSubscriber() + "'");
		
		if (subscription.getProvider() == null || subscription.getSubscriber() == null) {
			throw new GovernanceExeption("Can not subscribe with null provider or subcriber");
		}
		
		if (subscription.getTopic() == null) {
			throw new GovernanceExeption("Can not subscribe to null topic");
		}
		
		QName topic = new QName(subscription.getTopic().getNs(), subscription.getTopic().getName(), subscription.getTopic().getPrefix());

		HTTPProducerClient client = new HTTPProducerClient(subscription.getProvider());
		try {
			
			String id = client.subscribe(topic, subscription.getSubscriber());
			logger.info("Subscribed to topic " + topic + " and ID is " + id);

			result = new Subscription();
			result.setDate(System.currentTimeMillis());
			result.setId(id);
			result.setProvider(subscription.getProvider());
			result.setSubscriber(subscription.getSubscriber());
			Topic t = new Topic();
			t.setName(topic.getLocalPart());
			t.setNs(topic.getNamespaceURI());
			t.setPrefix(topic.getPrefix());
			result.setTopic(t);
			result.setStatus("active");

			// FIXME : Need to push suscription to the registry
			//this.subscriptionRegistry.addSubscription(subscription);

		} catch (NotificationException e) {
			logger.log(Level.SEVERE, "Problem while subscribing", e);
			throw new GovernanceExeption(e);
		}
		return result;
	}

	@Override
	@WebMethod
	public boolean unsubscribe(Subscription subscription)
			throws GovernanceExeption {
		if (subscription == null || subscription.getId() == null) {
			throw new GovernanceExeption(
					"Subscription information can not be null");
		}
		throw new GovernanceExeption("unsubscribe :: Not implemented");
	}

}
