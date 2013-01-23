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
package org.ow2.play.service.registry.mongo.rest;

import java.util.logging.Logger;

import javax.ws.rs.core.Response;

import org.ow2.play.service.registry.api.Entries;
import org.ow2.play.service.registry.api.RegistryException;

/**
 * @author chamerling
 * 
 */
public class Registry implements
		org.ow2.play.service.registry.api.rest.Registry {
	
	private static Logger logger = Logger.getLogger(Registry.class.getName());

	private org.ow2.play.service.registry.api.Registry registry;

	@Override
	public Response load(String url) {
		logger.info("Got load for url '" + url + "'");

		if (registry == null) {
			return Response.serverError().build();
		}
		
		try {
			registry.load(url);
		} catch (RegistryException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
		return Response.ok("Data loaded").build();
	}
	
	@Override
	public Response clear() {
		logger.info("Got clear call");

		if (registry == null) {
			return Response.serverError().build();
		}
		
		try {
			registry.clear();
		} catch (RegistryException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
		return Response.ok("Data deleted").build();
	}
	
	@Override
	public Response entries() {
		logger.info("Got get call");

		if (registry == null) {
			return Response.serverError().build();
		}

		try {
			return Response.ok(new Entries(registry.entries())).build();
		} catch (RegistryException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}

	public void setRegistry(org.ow2.play.service.registry.api.Registry registry) {
		this.registry = registry;
	}

}
