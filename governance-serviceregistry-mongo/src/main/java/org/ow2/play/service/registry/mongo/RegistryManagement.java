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
package org.ow2.play.service.registry.mongo;

import org.ow2.play.service.registry.api.RegistryException;

/**
 * TODO
 * @author chamerling
 * 
 */
public class RegistryManagement implements
		org.ow2.play.service.registry.api.RegistryManagement {

	private RegistryImpl registry;

	/**
	 * 
	 */
	public RegistryManagement() {
	}

	public void start() {

	}

	public void stop() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.service.registry.api.RegistryManagement#setProperties()
	 */
	@Override
	public void setProperties() throws RegistryException {

	}

	/**
	 * @param registry
	 *            the registry to set
	 */
	public void setRegistry(RegistryImpl registry) {
		this.registry = registry;
	}

}
