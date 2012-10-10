/**
 * 
 */
package org.ow2.play.governance.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jws.WebMethod;

import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.SubscriptionManagement;
import org.ow2.play.governance.api.SubscriptionRegistry;
import org.ow2.play.governance.api.SubscriptionService;
import org.ow2.play.governance.api.bean.Subscription;

/**
 * 
 * @author chamerling
 * 
 */
public class SubscriptionManagementService implements SubscriptionManagement {

	private static Logger logger = Logger
			.getLogger(SubscriptionManagementService.class.getName());

	private SubscriptionService subscriptionService;
	
	private SubscriptionRegistry subscriptionRegistry;

	/**
	 * Subscribe and save the subscriptions into the subscription registry
	 * 
	 */
	@Override
	@WebMethod
	public List<Subscription> subscribe(List<Subscription> subscriptions)
			throws GovernanceExeption {
		logger.fine("Create subscriptions from list");
		
		if (subscriptions == null) {
			throw new GovernanceExeption("Can not create null subscription");
		}

		List<Subscription> result = new ArrayList<Subscription>();

		for (Subscription subscription : subscriptions) {
			Subscription s = null;
			logger.fine("Create subscription for " + subscription);
			
			try {
				s = this.subscriptionService.subscribe(subscription);
				
				// persist the subscription in the system...
				if (subscriptionRegistry != null && s != null) {
					subscriptionRegistry.addSubscription(s);
				}
				
				if (s != null) {
					s.setStatus("created");
					result.add(s);
				}
			} catch (Exception e) {
				subscription.setStatus(e.getMessage());
				result.add(subscription);
			}
		}

		return result;
	}

	@Override
	@WebMethod
	public boolean unsubscribe(List<Subscription> subscriptions)
			throws GovernanceExeption {
		throw new GovernanceExeption("unsubscribe :: Not implemented");

	}

	@Override
	@WebMethod
	public void requestNew(List<Subscription> subscriptions)
			throws GovernanceExeption {
		throw new GovernanceExeption("requestNew :: Not implemented");
	}

	@Override
	public List<Subscription> replay(List<Subscription> subscriptions)
			throws GovernanceExeption {
		throw new GovernanceExeption("replay :: Not implemented");
	}

	public void setSubscriptionService(SubscriptionService subscriptionService) {
		this.subscriptionService = subscriptionService;
	}

	public SubscriptionService getSubscriptionService() {
		return this.subscriptionService;
	}
	
	public void setSubscriptionRegistry(
			SubscriptionRegistry subscriptionRegistry) {
		this.subscriptionRegistry = subscriptionRegistry;
	}
	
	public SubscriptionRegistry getSubscriptionRegistry() {
		return subscriptionRegistry;
	}

}
