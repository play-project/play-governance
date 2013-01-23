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
package org.ow2.play.service.registry.client.soap;

import java.util.List;

import javax.jws.WebMethod;

import org.ow2.play.service.registry.api.Entry;
import org.ow2.play.service.registry.api.Registry;
import org.ow2.play.service.registry.api.RegistryException;
import org.petalslink.dsb.cxf.CXFHelper;

/**
 * A CXF-based registry client
 * 
 * @author chamerling
 * 
 */
public class RegistryClient implements Registry {

	private String url;

	private Registry client;

	/**
	 * 
	 */
	public RegistryClient(String serviceURL) {
		this.url = serviceURL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.service.registry.api.Registry#get(java.lang.String)
	 */
	@Override
	@WebMethod
	public String get(String key) throws RegistryException {
		return getClient().get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.service.registry.api.Registry#put(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	@WebMethod
	public void put(String key, String value) throws RegistryException {
		getClient().put(key, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.service.registry.api.Registry#keys()
	 */
	@Override
	@WebMethod
	public List<String> keys() throws RegistryException {
		return getClient().keys();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.service.registry.api.Registry#clear()
	 */
	@Override
	@WebMethod
	public void clear() throws RegistryException {
		getClient().clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.service.registry.api.Registry#load(java.lang.String)
	 */
	@Override
	@WebMethod
	public void load(String url) throws RegistryException {
		getClient().load(url);
	}

	protected synchronized Registry getClient() {
		if (this.client == null) {
			this.client = CXFHelper.getClientFromFinalURL(this.url,
					Registry.class);
		}
		return this.client;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.service.registry.api.Registry#init()
	 */
	@Override
	public void init() throws RegistryException {
		getClient().init();
	}
	
	@Override
	public List<Entry> entries() throws RegistryException {
		return getClient().entries();
	}
}
