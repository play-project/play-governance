/**
 *
 * Copyright (c) 2012, PetalsLink
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA 
 *
 */
package org.ow2.play.governance.api;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.ow2.play.governance.api.bean.Subscription;

/**
 * Some management operations on the subscriptions
 * 
 * @author chamerling
 * 
 */
@WebService
public interface SubscriptionManagement {

	/**
	 * Subscribe to producers on behalf of the consumers
	 * 
	 * @param subscription
	 * @return
	 * @throws GovernanceExeption
	 */
	@WebMethod
	List<Subscription> subscribe(List<Subscription> subscriptions)
			throws GovernanceExeption;

	/**
	 * Unsubscribe
	 * 
	 * @param subscription
	 * @return
	 * @throws GovernanceExeption
	 */
	@WebMethod
	boolean unsubscribe(List<Subscription> subscriptions)
			throws GovernanceExeption;

	/**
	 * Request a new Subscription between a consumer and a provider. This does
	 * not means that the subscription is effective but that the request has
	 * been potentially transmitted to manager.
	 * 
	 * @param subscription
	 * @throws GovernanceExeption
	 */
	@WebMethod
	void requestNew(List<Subscription> subscriptions) throws GovernanceExeption;

	/**
	 * Replay all the given subscriptions. It means that all that is done is
	 * just subscribing and registering the subscription into the registry.
	 * 
	 * @param a
	 *            list of subscription to replay
	 * @return a list of subscription with all the required information (ID,
	 *         time, ...)
	 * @throws GovernanceExeption
	 */
	List<Subscription> replay(List<Subscription> subscriptions)
			throws GovernanceExeption;

}