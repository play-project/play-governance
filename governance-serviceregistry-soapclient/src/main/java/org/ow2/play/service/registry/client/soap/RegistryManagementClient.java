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

import org.ow2.play.service.registry.api.RegistryException;
import org.ow2.play.service.registry.api.RegistryManagement;
import org.petalslink.dsb.cxf.CXFHelper;

/**
 * @author chamerling
 * 
 */
public class RegistryManagementClient implements RegistryManagement {

	private String url;

	private RegistryManagement client;

	/**
	 * 
	 */
	public RegistryManagementClient(String url) {
		this.url = url;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.service.registry.api.RegistryManagement#start()
	 */
	@Override
	public void start() throws RegistryException {
		getClient().start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.service.registry.api.RegistryManagement#stop()
	 */
	@Override
	public void stop() throws RegistryException {
		getClient().stop();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.service.registry.api.RegistryManagement#setProperties()
	 */
	@Override
	public void setProperties() throws RegistryException {
		getClient().setProperties();
	}

	protected synchronized RegistryManagement getClient() {
		if (this.client == null) {
			this.client = CXFHelper.getClientFromFinalURL(this.url,
					RegistryManagement.class);
		}
		return this.client;
	}

}
