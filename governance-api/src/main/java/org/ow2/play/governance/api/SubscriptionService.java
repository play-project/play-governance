/**
 * 
 */
package org.ow2.play.governance.api;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.ow2.play.governance.api.bean.Subscription;

/**
 * API subscribe/unsubscribe. Up to the implementation to use the right
 * protocol/message ie WS Notification, pubsubhubbub, ...
 * 
 * @author chamerling
 * 
 */
@WebService
public interface SubscriptionService {

	/**
	 * Subscribe to a notification provider.
	 * 
	 * @param subscription : subscriber, provider and topic are required.
	 * @return
	 * @throws GovernanceExeption
	 */
	@WebMethod
	Subscription subscribe(Subscription subscription) throws GovernanceExeption;

	/**
	 * Unsubscribe from a notification provider.
	 * 
	 * @param subscription: The ID is required.
	 * @return
	 * @throws GovernanceExeption
	 */
	@WebMethod
	boolean unsubscribe(Subscription subscription) throws GovernanceExeption;
}
