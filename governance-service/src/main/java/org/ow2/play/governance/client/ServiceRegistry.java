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
package org.ow2.play.governance.client;

import java.util.List;
import java.util.logging.Logger;

import javax.jws.WebMethod;

import org.ow2.play.service.registry.api.Entry;
import org.ow2.play.service.registry.api.Registry;
import org.ow2.play.service.registry.api.RegistryException;
import org.petalslink.dsb.cxf.CXFHelper;

/**
 * WS client for service registry
 * 
 * @author chamerling
 * 
 */
public class ServiceRegistry implements Registry {

	private Registry client;

	private String url;

	private static Logger logger = Logger.getLogger(ServiceRegistry.class
			.getName());

	protected synchronized Registry getClient() {
		if (client == null) {
			logger.fine("Builidng CXF client");
			client = CXFHelper.getClientFromFinalURL(url, Registry.class);
		}
		return client;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.service.registry.api.Registry#clear()
	 */
	@Override
	@WebMethod
	public void clear() throws RegistryException {
		logger.fine("Clear registry");
		getClient().clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.service.registry.api.Registry#get(java.lang.String)
	 */
	@Override
	@WebMethod
	public String get(String name) throws RegistryException {
		logger.fine("Get endpoint for name " + name);
		return getClient().get(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.service.registry.api.Registry#keys()
	 */
	@Override
	@WebMethod
	public List<String> keys() throws RegistryException {
		logger.info("Get keys");
		return getClient().keys();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.service.registry.api.Registry#load(java.lang.String)
	 */
	@Override
	@WebMethod
	public void load(String url) throws RegistryException {
		logger.info("Load from " + url);
		getClient().load(url);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.service.registry.api.Registry#put(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	@WebMethod
	public void put(String name, String value) throws RegistryException {
		logger.info("Put data in service registry : " + name + " - " + value);
		getClient().put(name, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.service.registry.api.Registry#init()
	 */
	@Override
	@WebMethod
	public void init() throws RegistryException {
		logger.fine("Initialize service registry");
		getClient().init();
	}
	
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
		this.client = null;
	}

	/* (non-Javadoc)
	 * @see org.ow2.play.service.registry.api.Registry#entries()
	 */
	@Override
	@WebMethod
	public List<Entry> entries() throws RegistryException {
		return getClient().entries();
	}

}
