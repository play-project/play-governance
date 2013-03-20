/**
 *
 * Copyright (c) 2013, Linagora
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
package org.ow2.play.governance.platform.user.api.rest.bean;

import javax.xml.bind.annotation.XmlElement;

/**
 * A Play platform resource
 * 
 * @author chamerling
 * 
 */
public abstract class Resource {

	/**
	 * The resource URL in the platform. Based on auth mechanism, clients can
	 * access to resource directly by directly going to the provided URL.
	 */
	@XmlElement(name = "resource_url")
	public String resourceUrl;

}
