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
package org.ow2.play.governance.bootstrap.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jws.WebMethod;
import javax.jws.WebParam;

import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.SubscriptionRegistry;
import org.ow2.play.governance.api.bean.Subscription;
import org.ow2.play.service.registry.api.RegistryException;
import org.petalslink.dsb.cxf.CXFHelper;

/**
 * A Web service client to the registry
 * 
 * @author chamerling
 * 
 */
public class SubscriptionRegistryClient implements SubscriptionRegistry {

	private static Logger logger = Logger
			.getLogger(SubscriptionRegistryClient.class.getName());

	protected ServiceRegistry registry;

	private SubscriptionRegistry client;

	protected synchronized SubscriptionRegistry getClient()
			throws GovernanceExeption {
		if (client == null) {

			// get URL from the registry
			if (registry == null) {
				logger.warning("Can not get the registry!");
				throw new GovernanceExeption("Can not get the registry");
			}

			String url = null;
			try {
				url = registry
						.get(org.ow2.play.service.registry.api.Constants.GOVERNANCE_SUBSCRIPTION_REGISTRY);
			} catch (RegistryException e) {
				throw new GovernanceExeption(e);
			}

			if (url == null) {
				logger.warning("Can not get the GOVERNANCE_SUBSCRIPTION_REGISTRY URL from the registry!");
				throw new GovernanceExeption(
						"Can not get the URL from the registry!");
			}

			logger.info("Building SubscriptionRegistry service client for service at "
					+ url);
			client = CXFHelper.getClientFromFinalURL(url,
					SubscriptionRegistry.class);
		}
		return client;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.SubscriptionRegistry#addSubscription(org.
	 * ow2.play.governance.api.bean.Subscription)
	 */
	@Override
	@WebMethod
	public void addSubscription(Subscription s) throws GovernanceExeption {
		if (logger.isLoggable(Level.INFO)) {
			logger.info("Adding subscription to registry : " + s);
		}
		getClient().addSubscription(s);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.governance.api.SubscriptionRegistry#getSubscriptions()
	 */
	@Override
	@WebMethod
	public List<Subscription> getSubscriptions() throws GovernanceExeption {
		if (logger.isLoggable(Level.INFO)) {
			logger.info("Get Subscriptions");
		}
		return getClient().getSubscriptions();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.SubscriptionRegistry#getSubscriptions(org
	 * .ow2.play.governance.api.bean.Subscription)
	 */
	@Override
	@WebMethod(operationName = "getFilterSubscriptions")
	public List<Subscription> getSubscriptions(
			@WebParam(name = "filter") Subscription filter) throws GovernanceExeption {
		if (logger.isLoggable(Level.INFO)) {
			logger.info("Get subscriptions with filter : " + filter);
		}
		return getClient().getSubscriptions(filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.SubscriptionRegistry#remove(org.ow2.play.
	 * governance.api.bean.Subscription)
	 */
	@Override
	@WebMethod
	public boolean remove(Subscription s) throws GovernanceExeption {
		if (logger.isLoggable(Level.INFO)) {
			logger.info("Remove subscription : " + s);
		}
		return getClient().remove(s);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.governance.api.SubscriptionRegistry#removeAll()
	 */
	@Override
	@WebMethod
	public List<Subscription> removeAll() throws GovernanceExeption {
		if (logger.isLoggable(Level.INFO)) {
			logger.info("Remove all subscriptions");
		}
		return getClient().removeAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.SubscriptionRegistry#removeAllFromConsumer
	 * (java.lang.String)
	 */
	@Override
	@WebMethod
	public List<Subscription> removeAllFromConsumer(String c)
			throws GovernanceExeption {
		if (logger.isLoggable(Level.INFO)) {
			logger.info("Remove all subscriptions from the consumer = " + c);
		}
		return getClient().removeAllFromConsumer(c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.SubscriptionRegistry#removeAllFromProvider
	 * (java.lang.String)
	 */
	@Override
	@WebMethod
	public List<Subscription> removeAllFromProvider(String p) throws GovernanceExeption {
		if (logger.isLoggable(Level.INFO)) {
			logger.info("Remove all subscriptions from the provider = " + p);
		}
		return getClient().removeAllFromProvider(p);
	}

	/**
	 * @param serviceRegistry
	 *            the serviceRegistry to set
	 */
	public void setRegistry(ServiceRegistry registry) {
		this.registry = registry;
	}

}
