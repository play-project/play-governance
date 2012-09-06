/**
 * 
 */
package org.ow2.play.governance.api;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.ow2.play.governance.api.bean.Subscription;

/**
 * This service is used to manage subscription which are registered somewhere
 * and which need to be created at some time during the platform initialization.
 * Adding or deleting subscriptions from here does not mean that subscriptions
 * will be created in the system... It is just storage, not process.
 * 
 * @author chamerling
 * 
 */
@WebService
public interface BootSubscriptionService {

	/**
	 * Add a subscription for next subscription process iteration.
	 * 
	 * @param subscription
	 * @throws GovernanceExeption
	 */
	@WebMethod
	void add(Subscription subscription) throws GovernanceExeption;

	/**
	 * Remove a subscription from the subscriptions list
	 * 
	 * @param subscription
	 * @throws GovernanceExeption
	 */
	@WebMethod
	void remove(Subscription subscription) throws GovernanceExeption;
	
	/**
	 * Remove all the subscriptions
	 * 
	 */
	@WebMethod
	void removeAll();

	/**
	 * Load subscriptions from a resource URL
	 * 
	 * @param url
	 */
	@WebMethod
	void load(String url) throws GovernanceExeption;

	/**
	 * Get all the registered subscriptions.
	 * 
	 * @return
	 * @throws GovernanceExeption
	 */
	List<Subscription> all() throws GovernanceExeption;

}
