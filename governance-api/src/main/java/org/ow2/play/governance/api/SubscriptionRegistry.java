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
import javax.jws.WebParam;
import javax.jws.WebService;

import org.ow2.play.governance.api.bean.Subscription;

/**
 * A simple subscription registry
 * 
 * @author chamerling
 * 
 */
@WebService
public interface SubscriptionRegistry {

	/**
	 * Add a subscription. This method does not subscribes, it just cache
	 * subscription for future use
	 * 
	 * @param subscription
	 */
	@WebMethod
	void addSubscription(Subscription subscription) throws GovernanceExeption;

	/**
	 * Get all the current subscriptions which have been done by this component
	 * 
	 * @return
	 */
	@WebMethod
	List<Subscription> getSubscriptions() throws GovernanceExeption;

	/**
	 * Get all the subscriptions which match the given subcription filter. ie we
	 * use all the filter information to create a valid query to the storage
	 * layer.
	 * 
	 * @param filter
	 * @return all values matching the viven filter, all if filter is null.
	 */
	@WebMethod(operationName="getFilterSubscriptions")
	List<Subscription> getSubscriptions(
			@WebParam(name = "filter") Subscription filter) throws GovernanceExeption;

	/**
	 * Unsubscribe to all the current subscriptions...
	 * 
	 * @return the list of subscriptions which has been removed
	 */
	@WebMethod
	List<Subscription> removeAll() throws GovernanceExeption;

	/**
	 * Remove a subscription
	 * 
	 * @param subscription
	 * @return
	 */
	@WebMethod
	boolean remove(Subscription subscription) throws GovernanceExeption;

	/**
	 * Remove all the subscriptions from the registry where the provider is
	 * equal to the input one.
	 * 
	 * @param provider
	 * @return the list of removed subscriptions
	 */
	@WebMethod
	List<Subscription> removeAllFromProvider(String provider) throws GovernanceExeption;

	/**
	 * Remove all the subscriptions where the consumer if equal to the input one
	 * 
	 * @param consumer
	 * @return the list of removed subscriptions
	 */
	@WebMethod
	List<Subscription> removeAllFromConsumer(String consumer) throws GovernanceExeption;

}
