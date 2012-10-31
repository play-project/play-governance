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

/**
 * An EC wrapper to get rid of dependencies. We should be able to get all this
 * information using any EC API version.
 * 
 * @author chamerling
 * 
 */
@WebService
public interface EventCloudManagementService {

	/**
	 * Creates and deploys a new eventcloud for the specified {@code streamUrl}.
	 * 
	 * @param streamUrl
	 *            an URL which identifies an eventcloud among an organization.
	 * 
	 * @return a boolean which indicates whether an eventcloud has been created
	 *         for the specified {@code streamUrl} or not. In the former case,
	 *         when the return value is {@code false}, this means that an
	 *         eventcloud is already running for the specified {@code streamUrl}
	 *         .
	 */
	@WebMethod
	boolean createEventCloud(@WebParam(name = "streamUrl") String streamUrl) throws GovernanceExeption;

	/**
	 * Detroys the eventcloud identified by {@code streamUrl}.
	 * 
	 * @param streamUrl
	 *            an URL which identifies an eventcloud among an organization.
	 * 
	 * @return a boolean which indicates whether the eventcloud has been
	 *         destroyed or not. A return value equals to {@code false} probably
	 *         means that there was no eventcloud found with the specified
	 *         {@code streamUrl}.
	 */
	@WebMethod
	boolean destroyEventCloud(@WebParam(name = "streamUrl") String streamUrl) throws GovernanceExeption;

	/**
	 * Indicates whether an eventcloud exists or not according to its streamUrl.
	 * 
	 * @param streamUrl
	 *            an URL which identifies an eventcloud among an organization.
	 * 
	 * @return {@code true} if the eventcloud identified by the specified
	 *         streamUrl is already created, {@code false} otherwise.
	 */
	@WebMethod
	boolean isCreated(@WebParam(name = "streamUrl") String streamUrl) throws GovernanceExeption;

	/**
	 * Returns the endpoint URL associated to the eventclouds registry which
	 * knows what are the eventclouds that are manageable.
	 * 
	 * @return the endpoint URL associated to the eventclouds registry which
	 *         knows what are the eventclouds that are manageable.
	 */
	@WebMethod
	String getRegistryEndpointUrl() throws GovernanceExeption;

	/**
	 * Returns a list that contains the URLs of the streams/eventclouds which
	 * are managed by the eventclouds registry used by this webservice.
	 * 
	 * @return a list that contains the URLs of the streams which are managed by
	 *         the eventclouds registry used by this webservice.
	 */
	@WebMethod
	List<String> getEventCloudIds() throws GovernanceExeption;

	/**
	 * Creates, deploys and exposes as a web service a publish proxy for the
	 * eventcloud identified by the specified {@code streamUrl}. When the call
	 * succeeds, the endpoint URL to the publish web service is returned.
	 * 
	 * @param streamUrl
	 *            an URL which identifies an eventcloud which is running.
	 * 
	 * @return the endpoint URL to the publish web service which is deployed or
	 *         {@code null} if there was no existing eventcloud for the
	 *         specified {@code streamUrl}.
	 */
	@WebMethod
	String createPublishProxy(@WebParam(name = "streamUrl") String streamUrl) throws GovernanceExeption;

	/**
	 * Creates, deploys and exposes as a web service a subscribe proxy for the
	 * eventcloud identified by the specified {@code streamUrl}. When the call
	 * succeeds, the endpoint URL to the subscribe web service is returned.
	 * 
	 * @param streamUrl
	 *            an URL which identifies an eventcloud which is running.
	 * 
	 * @return the endpoint URL to the publish web service which is deployed or
	 *         {@code null} if there was no existing eventcloud for the
	 *         specified {@code streamUrl}.
	 */
	@WebMethod
	String createSubscribeProxy(@WebParam(name = "streamUrl") String streamUrl) throws GovernanceExeption;

	/**
	 * Creates, deploys and exposes as a web service a putget proxy for the
	 * eventcloud identified by the specified {@code streamUrl}. When the call
	 * succeeds, the endpoint URL to the putget web service is returned.
	 * 
	 * @param streamUrl
	 *            an URL which identifies an eventcloud which is running.
	 * 
	 * @return the endpoint URL to the publish web service which is deployed or
	 *         {@code null} if there was no existing eventcloud for the
	 *         specified {@code streamUrl}.
	 */
	@WebMethod
	String createPutGetProxy(@WebParam(name = "streamUrl") String streamUrl) throws GovernanceExeption;

	/**
	 * Detroys the publish proxy identified by {@code publishProxyEndpoint}.
	 * 
	 * @param publishProxyEndpoint
	 *            the endpoint of the publish proxy to destroy.
	 * 
	 * @return {@code true} if the publish proxy has been destroyed,
	 *         {@code false} otherwise.
	 */
	@WebMethod
	boolean destroyPublishProxy(
			@WebParam(name = "publishProxyEndpoint") String publishProxyEndpoint) throws GovernanceExeption;

	/**
	 * Detroys the subscribe proxy identified by {@code subscribeProxyEndpoint}.
	 * 
	 * @param subscribeProxyEndpoint
	 *            the endpoint of the subscribe proxy to destroy.
	 * 
	 * @return {@code true} if the subscribe proxy has been destroyed,
	 *         {@code false} otherwise.
	 */
	@WebMethod
	boolean destroySubscribeProxy(
			@WebParam(name = "subscribeProxyEndpoint") String subscribeProxyEndpoint) throws GovernanceExeption;

	/**
	 * Detroys the putget proxy identified by {@code putgetProxyEndpoint}.
	 * 
	 * @param putgetProxyEndpoint
	 *            the endpoint of the putget proxy to destroy.
	 * 
	 * @return {@code true} if the putget proxy has been destroyed,
	 *         {@code false} otherwise.
	 */
	@WebMethod
	boolean destroyPutGetProxy(
			@WebParam(name = "publishProxyEndpoint") String putgetProxyEndpoint) throws GovernanceExeption;

	/**
	 * Returns the endpoint URLs for the publish proxies which have been created
	 * for the specified {@code streamUrl}.
	 * 
	 * @param streamUrl
	 *            an URL which identifies an eventcloud which is running.
	 * 
	 * @return the endpoint URLs for the publish proxies which have been created
	 *         for the specified {@code streamUrl}.
	 */
	@WebMethod
	List<String> getPublishProxyEndpointUrls(
			@WebParam(name = "streamUrl") String streamUrl) throws GovernanceExeption;

	/**
	 * Returns the endpoint URLs for the subscribe proxies which have been
	 * created for the specified {@code streamUrl}.
	 * 
	 * @param streamUrl
	 *            an URL which identifies an eventcloud which is running.
	 * 
	 * @return the endpoint URLs for the subscribe proxies which have been
	 *         created for the specified {@code streamUrl}.
	 */
	@WebMethod
	List<String> getSubscribeProxyEndpointUrls(
			@WebParam(name = "streamUrl") String streamUrl) throws GovernanceExeption;

	/**
	 * Returns the endpoint URLs for the putget proxies which have been created
	 * for the specified {@code streamUrl}.
	 * 
	 * @param streamUrl
	 *            an URL which identifies an eventcloud which is running.
	 * 
	 * @return the endpoint URLs for the putget proxies which have been created
	 *         for the specified {@code streamUrl}.
	 */
	@WebMethod
	List<String> getPutgetProxyEndpointUrls(
			@WebParam(name = "streamUrl") String streamUrl) throws GovernanceExeption;

}
