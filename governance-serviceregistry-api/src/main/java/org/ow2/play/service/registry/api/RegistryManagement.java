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
package org.ow2.play.service.registry.api;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Registry management, should be secure!
 * 
 * @author chamerling
 * 
 */
@WebService
public interface RegistryManagement {

	@WebMethod
	void start() throws RegistryException;

	@WebMethod
	void stop() throws RegistryException;
	
	@WebMethod
	void setProperties() throws RegistryException;

}
