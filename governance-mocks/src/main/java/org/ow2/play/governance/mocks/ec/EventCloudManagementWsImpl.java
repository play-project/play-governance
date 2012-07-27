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
package org.ow2.play.governance.mocks.ec;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import fr.inria.eventcloud.webservices.api.EventCloudManagementWsApi;

/**
 * @author chamerling
 * 
 */
public class EventCloudManagementWsImpl implements EventCloudManagementWsApi {

	private static Logger logger = Logger
			.getLogger(EventCloudManagementWsImpl.class.getName());

	private List<String> ids = new ArrayList<String>();

	private List<String> putgetEndpoints = new ArrayList<String>();

	private List<String> publishEndpoints = new ArrayList<String>();

	private List<String> subscribeEndpoints = new ArrayList<String>();

	@Resource
	WebServiceContext context;

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.inria.eventcloud.webservices.api.EventCloudManagementWsApi#
	 * createEventCloud(java.lang.String)
	 */
	@Override
	@WebMethod
	public boolean createEventCloud(
			@WebParam(name = "streamUrl") String streamURL) {
		logger.info("Create Event Cloud for stream " + streamURL);
		ids.add(streamURL);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.inria.eventcloud.webservices.api.EventCloudManagementWsApi#
	 * createPublishProxy(java.lang.String)
	 */
	@Override
	@WebMethod
	public String createPublishProxy(
			@WebParam(name = "streamUrl") String streamURL) {
		logger.info("CreatePublishProxy for stream " + streamURL);
		HttpServletRequest request = (HttpServletRequest) context
				.getMessageContext().get(MessageContext.SERVLET_REQUEST);
		String str = request.getRequestURL().toString();
		str = str.substring(0, str.lastIndexOf('/'));
		str = str + "/ec/publish/" + streamURL;

		publishEndpoints.add(str);
		return str;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.inria.eventcloud.webservices.api.EventCloudManagementWsApi#
	 * createPutGetProxy(java.lang.String)
	 */
	@Override
	@WebMethod
	public String createPutGetProxy(
			@WebParam(name = "streamUrl") String streamURL) {
		logger.info("createPutGetProxy for stream " + streamURL);
		System.out.println(context);

		HttpServletRequest request = (HttpServletRequest) context
				.getMessageContext().get(MessageContext.SERVLET_REQUEST);
		String str = request.getRequestURL().toString();
		str = str.substring(0, str.lastIndexOf('/'));
		str = str + "/ec/putget/" + streamURL;

		putgetEndpoints.add(str);

		return str;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.inria.eventcloud.webservices.api.EventCloudManagementWsApi#
	 * createSubscribeProxy(java.lang.String)
	 */
	@Override
	@WebMethod
	public String createSubscribeProxy(
			@WebParam(name = "streamUrl") String streamURL) {
		logger.info("createSubscribeProxy for stream " + streamURL);
		HttpServletRequest request = (HttpServletRequest) context
				.getMessageContext().get(MessageContext.SERVLET_REQUEST);
		String str = request.getRequestURL().toString();
		str = str.substring(0, str.lastIndexOf('/'));

		str = str + "/ec/subscribe/" + streamURL;
		subscribeEndpoints.add(str);

		return str;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.inria.eventcloud.webservices.api.EventCloudManagementWsApi#
	 * destroyEventCloud(java.lang.String)
	 */
	@Override
	@WebMethod
	public boolean destroyEventCloud(
			@WebParam(name = "streamUrl") String streamURL) {
		logger.info("destroyEventCloud for stream " + streamURL);
		System.out.println(context);

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.inria.eventcloud.webservices.api.EventCloudManagementWsApi#
	 * destroyPublishProxy(java.lang.String)
	 */
	@Override
	@WebMethod
	public boolean destroyPublishProxy(
			@WebParam(name = "publishProxyEndpoint") String streamURL) {
		logger.info("destroyPublishProxy for stream " + streamURL);
		System.out.println(context);

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.inria.eventcloud.webservices.api.EventCloudManagementWsApi#
	 * destroyPutGetProxy(java.lang.String)
	 */
	@Override
	@WebMethod
	public boolean destroyPutGetProxy(
			@WebParam(name = "publishProxyEndpoint") String streamURL) {
		logger.info("destroyPutGetProxy for stream " + streamURL);
		System.out.println(context);

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.inria.eventcloud.webservices.api.EventCloudManagementWsApi#
	 * destroySubscribeProxy(java.lang.String)
	 */
	@Override
	@WebMethod
	public boolean destroySubscribeProxy(
			@WebParam(name = "subscribeProxyEndpoint") String streamURL) {
		logger.info("destroySubscribeProxy for stream " + streamURL);
		System.out.println(context);

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.inria.eventcloud.webservices.api.EventCloudManagementWsApi#
	 * getEventCloudIds()
	 */
	@Override
	@WebMethod
	public List<String> getEventCloudIds() {
		logger.info("getEventCloudIds");
		System.out.println(context);

		return new ArrayList<String>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.inria.eventcloud.webservices.api.EventCloudManagementWsApi#
	 * getPublishProxyEndpointUrls(java.lang.String)
	 */
	@Override
	@WebMethod
	public List<String> getPublishProxyEndpointUrls(
			@WebParam(name = "streamUrl") String streamURL) {
		logger.info("getPublishProxyEndpointUrls for stream " + streamURL);
		System.out.println(context);

		return new ArrayList<String>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.inria.eventcloud.webservices.api.EventCloudManagementWsApi#
	 * getPutgetProxyEndpointUrls(java.lang.String)
	 */
	@Override
	@WebMethod
	public List<String> getPutgetProxyEndpointUrls(
			@WebParam(name = "streamUrl") String streamUrl) {
		logger.info("getPublishProxyEndpointUrls for stream " + streamUrl);
		System.out.println(context);

		return new ArrayList<String>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.inria.eventcloud.webservices.api.EventCloudManagementWsApi#
	 * getRegistryEndpointUrl()
	 */
	@Override
	@WebMethod
	public String getRegistryEndpointUrl() {
		logger.info("getRegistryEndpointUrl");
		System.out.println(context);

		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.inria.eventcloud.webservices.api.EventCloudManagementWsApi#
	 * getSubscribeProxyEndpointUrls(java.lang.String)
	 */
	@Override
	@WebMethod
	public List<String> getSubscribeProxyEndpointUrls(
			@WebParam(name = "streamUrl") String streamUrl) {
		logger.info("getSubscribeProxyEndpointUrls for stream " + streamUrl);
		System.out.println(context);

		return new ArrayList<String>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.inria.eventcloud.webservices.api.EventCloudManagementWsApi#isCreated
	 * (java.lang.String)
	 */
	@Override
	@WebMethod
	public boolean isCreated(@WebParam(name = "streamUrl") String streamUrl) {
		logger.info("getSubscribeProxyEndpointUrls for stream " + streamUrl);
		System.out.println(context);

		return true;
	}

}
