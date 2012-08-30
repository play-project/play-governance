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
package org.ow2.play.governance.bootstrap.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;

import org.ow2.play.governance.api.bean.Subscription;
import org.ow2.play.governance.bootstrap.MemoryLogServiceImpl;
import org.ow2.play.governance.bootstrap.api.BootstrapFault;
import org.ow2.play.governance.bootstrap.api.BootstrapService;
import org.ow2.play.governance.bootstrap.api.rest.InitService;
import org.ow2.play.governance.bootstrap.api.rest.beans.Subscriptions;
import org.ow2.play.service.registry.api.Registry;
import org.ow2.play.service.registry.api.RegistryException;


/**
 * Main init service which gets default values from configuration and create all
 * the subscriptions in the platform.
 * 
 * @author chamerling
 * 
 */
public class InitServiceImpl implements InitService {

	protected static Logger logger = Logger.getLogger(InitServiceImpl.class
			.getName());

	private BootstrapService dsbSubscribesBootstrapService;

	private BootstrapService ecSubscribesBootstrapService;

	private Registry endpointRegistry;

	@Override
	public Response go() {
		MemoryLogServiceImpl.get().log("Call to bootstrap service");
		List<Subscription> result = new ArrayList<Subscription>();
		result.addAll(dsbSubscribes2EC());
		result.addAll(ecSubscribes2DSB());
		return Response.ok(new Subscriptions(result)).build();
	}

	@Override
	public Response ecToDSB() {
		return Response.ok(new Subscriptions(ecSubscribes2DSB())).build();
	}

	@Override
	public Response dsbToEC() {
		return Response.ok(new Subscriptions(dsbSubscribes2EC())).build();
	}

	protected List<Subscription> dsbSubscribes2EC() {
		MemoryLogServiceImpl.get().log("DSB subscribes to EC");

		List<Subscription> result = new ArrayList<Subscription>();

		String eventCloudEndpoint = null;
		String subscriberEndpoint = null;
		try {
		    eventCloudEndpoint = endpointRegistry
		            .get(org.ow2.play.service.registry.api.Constants.DSB_TO_EC_EC);
			subscriberEndpoint = endpointRegistry
					.get(org.ow2.play.service.registry.api.Constants.DSB_TO_EC_EC_SUBSCRIBER);
		} catch (RegistryException e1) {
			logger.warning(e1.getMessage());
			if (logger.isLoggable(Level.FINE)) {
				logger.log(Level.WARNING, "Can not get data from the registry",
						e1);
			}
		}

		logger.info(String.format("Initializing with ec %s and subscriber %s",
				eventCloudEndpoint, subscriberEndpoint));

		if (eventCloudEndpoint == null || subscriberEndpoint == null) {
			logger.warning("Can not initialize with null values....");
			return result;
		}

		try {
			result.addAll(dsbSubscribesBootstrapService.bootstrap(
					eventCloudEndpoint, subscriberEndpoint));

			if (logger.isLoggable(Level.FINE)) {
				for (Subscription s : result) {
					logger.fine("Subscribed : " + s);
				}
			}
		} catch (BootstrapFault e) {
			logger.warning(e.getMessage());
			if (logger.isLoggable(Level.FINE)) {
				logger.log(Level.WARNING, "Can not boot", e);
			}
		}

		return result;
	}

	protected List<Subscription> ecSubscribes2DSB() {
		MemoryLogServiceImpl.get().log("EC subscribes to DSB");

		List<Subscription> result = new ArrayList<Subscription>();

		String eventCloudEndpoint = null;
		String dsbEndpoint = null;
		try {
			eventCloudEndpoint = endpointRegistry
			        .get(org.ow2.play.service.registry.api.Constants.EC_TO_DSB_EC);
			dsbEndpoint = endpointRegistry
					.get(org.ow2.play.service.registry.api.Constants.EC_TO_DSB_DSB);
		} catch (RegistryException e1) {
			logger.warning(e1.getMessage());
			if (logger.isLoggable(Level.FINE)) {
				logger.log(Level.WARNING, "Can not get data from the registry",
						e1);
			}
		}

		logger.info(String.format("Initializing with ec %s and dsb %s",
				eventCloudEndpoint, dsbEndpoint));

		if (eventCloudEndpoint == null || dsbEndpoint == null) {
			logger.warning("Can not initialize with null values....");
			return result;
		}

		try {
			result.addAll(ecSubscribesBootstrapService.bootstrap(dsbEndpoint,
					eventCloudEndpoint));

			if (logger.isLoggable(Level.FINE)) {
				for (Subscription s : result) {
					logger.fine("Subscribed : " + s);
				}
			}

		} catch (BootstrapFault e) {
			logger.warning(e.getMessage());
			if (logger.isLoggable(Level.FINE)) {
				logger.log(Level.WARNING, "Can not boot", e);
			}
		}
		return result;
	}

	public void setDsbSubscribesBootstrapService(
			BootstrapService dsbSubscribesBootstrapService) {
		this.dsbSubscribesBootstrapService = dsbSubscribesBootstrapService;
	}

	public void setEcSubscribesBootstrapService(
			BootstrapService ecSubscribesBootstrapService) {
		this.ecSubscribesBootstrapService = ecSubscribesBootstrapService;
	}

	/**
	 * @param endpointRegistry
	 *            the endpointRegistry to set
	 */
	public void setEndpointRegistry(Registry endpointRegistry) {
		this.endpointRegistry = endpointRegistry;
	}

}
