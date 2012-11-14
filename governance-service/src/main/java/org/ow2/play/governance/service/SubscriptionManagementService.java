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
import org.ow2.play.service.registry.api.Constants;
import org.ow2.play.service.registry.api.Registry;
import org.ow2.play.service.registry.api.RegistryException;

/**
 * The Subscription Management Service only handles subscriptions for DSB.
 * EC must be another service since endpoints may vary.
 * 
 * @author chamerling
 * 
 */
public class SubscriptionManagementService implements SubscriptionManagement {

	private static Logger logger = Logger
			.getLogger(SubscriptionManagementService.class.getName());

	private SubscriptionService subscriptionService;
	
	private SubscriptionRegistry subscriptionRegistry;
	
	private Registry serviceRegistry;

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
	public List<Subscription> unsubscribe(List<Subscription> subscriptions)
			throws GovernanceExeption {
		if (subscriptions == null) {
			throw new GovernanceExeption("Can not unsubscribe null subscriptions");
		}
		
		// FIXME = This will change according to subscriptions : DSB, EC, ...
		String subcriptionManagementEndpoint = null;
		try {
			subcriptionManagementEndpoint = serviceRegistry.get(Constants.DSB_SUBSCRIPTION);
		} catch (RegistryException e) {
			logger.warning(e.getMessage());
			throw new GovernanceExeption(
					"Can not get the subcription management endpoint : "
							+ e.getMessage());
		}

		List<Subscription> result = new ArrayList<Subscription>();

		for (Subscription subscription : subscriptions) {
			logger.fine("Unsubscribe for " + subscription);
			
			try {
				boolean unsubscribe = this.subscriptionService.unsubscribe(
						subscription, subcriptionManagementEndpoint);
				if (unsubscribe) {
					logger.info("Unsubscription OK for : " + subscription);
					result.add(subscription);
					subscription.setStatus("Unsubscribed");
					// remove the subscription from the registry
					// TODO : Just update its state
					// persist the subscription in the system...
					if (subscriptionRegistry != null) {
						subscriptionRegistry.remove(subscription);
					}
				} else {
					logger.info("Unsubscription KO for : " + subscription);
					subscription.setStatus("Unable to unsubscribe");
					result.add(subscription);

					// TODO : Update the registry with failure
					// TODO : Notify monitoring
				}
			} catch (Exception e) {
				logger.warning(e.getMessage());
				subscription.setStatus(e.getMessage());
				result.add(subscription);
			}
		}
		return result;
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
	
	/* (non-Javadoc)
	 * @see org.ow2.play.governance.api.SubscriptionManagement#unsubscribeAllFrom(java.lang.String)
	 */
	@Override
	@WebMethod
	public List<Subscription> unsubscribeAllForSubscriber(String subscriber)
			throws GovernanceExeption {
		List<Subscription> result = new ArrayList<Subscription>();
		
		Subscription filter = new Subscription();
		filter.setSubscriber(subscriber);
		List<Subscription> subscriptions = subscriptionRegistry.getSubscriptions(filter);
		
		if (subscriptions == null || subscriptions.size() == 0) {
			logger.fine("Can not find any subscription for subscriber " + subscriber);
			return result;
		}
		return unsubscribe(subscriptions);
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
	
	public void setServiceRegistry(Registry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

}
