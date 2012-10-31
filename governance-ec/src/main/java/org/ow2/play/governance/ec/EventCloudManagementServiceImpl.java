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
package org.ow2.play.governance.ec;

import java.util.List;
import java.util.logging.Logger;

import javax.jws.WebParam;

import org.ow2.play.governance.api.EventCloudManagementService;
import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.service.registry.api.Constants;
import org.ow2.play.service.registry.api.Registry;
import org.ow2.play.service.registry.api.RegistryException;
import org.petalslink.dsb.cxf.CXFHelper;

import fr.inria.eventcloud.webservices.api.EventCloudManagementServiceApi;

/**
 * @author chamerling
 * 
 */
public class EventCloudManagementServiceImpl implements
		EventCloudManagementService {

	private Registry registry;

	private static Logger logger = Logger
			.getLogger(EventCloudManagementServiceImpl.class.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.EventCloudManagementService#createEventCloud
	 * (java.lang.String)
	 */
	@Override
	public boolean createEventCloud(
			@WebParam(name = "streamUrl") String streamUrl)
			throws GovernanceExeption {
		logger.info("Create Event Cloud " + streamUrl);
		return getCloudManagementServiceApi().createEventCloud(streamUrl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.EventCloudManagementService#destroyEventCloud
	 * (java.lang.String)
	 */
	@Override
	public boolean destroyEventCloud(
			@WebParam(name = "streamUrl") String streamUrl) throws GovernanceExeption {
		logger.info("Destroy Event Cloud " + streamUrl);
		return getCloudManagementServiceApi().destroyEventCloud(streamUrl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.EventCloudManagementService#isCreated(java
	 * .lang.String)
	 */
	@Override
	public boolean isCreated(@WebParam(name = "streamUrl") String streamUrl) throws GovernanceExeption {
		logger.info("Is Created Event Cloud " + streamUrl);
		return getCloudManagementServiceApi().isCreated(streamUrl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.governance.api.EventCloudManagementService#
	 * getRegistryEndpointUrl()
	 */
	@Override
	public String getRegistryEndpointUrl() throws GovernanceExeption {
		logger.info("getRegistryEndpointUrl");
		return getCloudManagementServiceApi().getRegistryEndpointUrl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.EventCloudManagementService#getEventCloudIds
	 * ()
	 */
	@Override
	public List<String> getEventCloudIds() throws GovernanceExeption {
		logger.info("getEventCloudIds");
		return getCloudManagementServiceApi().getEventCloudIds();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.EventCloudManagementService#createPublishProxy
	 * (java.lang.String)
	 */
	@Override
	public String createPublishProxy(
			@WebParam(name = "streamUrl") String streamUrl) throws GovernanceExeption {
		logger.info("Create Publish Proxy " + streamUrl);
		return getCloudManagementServiceApi().createPublishProxy(streamUrl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.EventCloudManagementService#createSubscribeProxy
	 * (java.lang.String)
	 */
	@Override
	public String createSubscribeProxy(
			@WebParam(name = "streamUrl") String streamUrl) throws GovernanceExeption {
		logger.info("Create SubscribeProxy " + streamUrl);
		return getCloudManagementServiceApi().createSubscribeProxy(streamUrl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.EventCloudManagementService#createPutGetProxy
	 * (java.lang.String)
	 */
	@Override
	public String createPutGetProxy(
			@WebParam(name = "streamUrl") String streamUrl) throws GovernanceExeption {
		logger.info("Create PUTGET Proxy " + streamUrl);
		return getCloudManagementServiceApi().createPutGetProxy(streamUrl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.EventCloudManagementService#destroyPublishProxy
	 * (java.lang.String)
	 */
	@Override
	public boolean destroyPublishProxy(
			@WebParam(name = "publishProxyEndpoint") String publishProxyEndpoint) throws GovernanceExeption {
		logger.info("Destroy publish " + publishProxyEndpoint);
		return getCloudManagementServiceApi().destroyPublishProxy(publishProxyEndpoint);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.EventCloudManagementService#destroySubscribeProxy
	 * (java.lang.String)
	 */
	@Override
	public boolean destroySubscribeProxy(
			@WebParam(name = "subscribeProxyEndpoint") String subscribeProxyEndpoint) throws GovernanceExeption {
		logger.info("Destroy Subscribe " + subscribeProxyEndpoint);
		return getCloudManagementServiceApi().destroySubscribeProxy(subscribeProxyEndpoint);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.EventCloudManagementService#destroyPutGetProxy
	 * (java.lang.String)
	 */
	@Override
	public boolean destroyPutGetProxy(
			@WebParam(name = "publishProxyEndpoint") String putgetProxyEndpoint) throws GovernanceExeption {
		logger.info("Destroy putget " + putgetProxyEndpoint);
		return getCloudManagementServiceApi().destroyPutGetProxy(putgetProxyEndpoint);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.governance.api.EventCloudManagementService#
	 * getPublishProxyEndpointUrls(java.lang.String)
	 */
	@Override
	public List<String> getPublishProxyEndpointUrls(
			@WebParam(name = "streamUrl") String streamUrl) {
		logger.info("Create Event Cloud " + streamUrl);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.governance.api.EventCloudManagementService#
	 * getSubscribeProxyEndpointUrls(java.lang.String)
	 */
	@Override
	public List<String> getSubscribeProxyEndpointUrls(
			@WebParam(name = "streamUrl") String streamUrl) {
		logger.info("Create Event Cloud " + streamUrl);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.governance.api.EventCloudManagementService#
	 * getPutgetProxyEndpointUrls(java.lang.String)
	 */
	@Override
	public List<String> getPutgetProxyEndpointUrls(
			@WebParam(name = "streamUrl") String streamUrl) {
		logger.info("Create Event Cloud " + streamUrl);
		return null;
	}

	public EventCloudManagementServiceApi getCloudManagementServiceApi()
			throws GovernanceExeption {
		try {
			return CXFHelper.getClientFromFinalURL(
					registry.get(Constants.DSB_TO_EC_EC),
					EventCloudManagementServiceApi.class);
		} catch (RegistryException e) {
			throw new GovernanceExeption(e.getMessage());
		}
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

}
