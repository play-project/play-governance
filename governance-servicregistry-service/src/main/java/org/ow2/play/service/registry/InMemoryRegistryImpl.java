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
package org.ow2.play.service.registry;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.jws.WebMethod;

import org.ow2.play.service.registry.api.Entry;
import org.ow2.play.service.registry.api.Registry;
import org.ow2.play.service.registry.api.RegistryException;

/**
 * @author chamerling
 * 
 */
public class InMemoryRegistryImpl implements Registry {

	private static Logger logger = Logger.getLogger(InMemoryRegistryImpl.class
			.getName());

	private Map<String, String> map;

	/**
	 * 
	 */
	public InMemoryRegistryImpl() {
		try {
			this.init();
		} catch (RegistryException e) {
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.service.registry.api.Registry#init()
	 */
	@Override
	public void init() throws RegistryException {
		map = new ConcurrentHashMap<String, String>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.service.registry.api.Registry#get(java.lang.String)
	 */
	@Override
	@WebMethod
	public String get(String key) {
		logger.info("Get value for key : " + key);
		return map.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.service.registry.api.Registry#put(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	@WebMethod
	public void put(String key, String value) {
		logger.info("Put value '" + value + "' for key '" + key + "'");
		if (key != null && value != null && isURL(value)) {
			this.map.put(key, value);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.service.registry.api.Registry#keys()
	 */
	@Override
	@WebMethod
	public List<String> keys() {
		return new ArrayList<String>(map.keySet());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.service.registry.api.Registry#clear()
	 */
	@Override
	@WebMethod
	public void clear() {
		this.map.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.service.registry.api.Registry#load(java.lang.String)
	 */
	@Override
	@WebMethod
	public void load(String url) {
		if (url != null) {
			try {
				URL u = new URL(url);
				Properties props = new Properties();
				props.load(u.openStream());

				for (Object key : props.keySet()) {
					String value = props.getProperty(key.toString());
					if (isURL(value)) {
						this.put(key.toString(), value);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	@Override
	@WebMethod
	public List<Entry> entries() throws RegistryException {
		List<Entry> result = new ArrayList<Entry>();
		for (String key  : keys()) {
			Entry e = new Entry();
			e.key = key;
			e.value = get(key);
			result.add(e);
		}
		
		return result;
	}

	protected boolean isURL(String url) {
		if (url == null) {
			return false;
		}

		try {
			new URL(url);
			return true;
		} catch (MalformedURLException e) {
			return false;
		}
	}

}
