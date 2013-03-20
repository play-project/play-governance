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
package org.ow2.play.governance.platform.user.service.rest;

import static org.ow2.play.governance.platform.user.api.rest.helpers.Response.ok;

import javax.ws.rs.core.Response;

import org.ow2.play.governance.platform.user.api.rest.bean.Notification;

/**
 * @author chamerling
 *
 */
public class PublishService extends AbstractService implements org.ow2.play.governance.platform.user.api.rest.PublishService {

	public Response notify(Notification notification) {
		System.out.println(">>>>>>>> N RESOURCE " + notification.resource);
		System.out.println(">>>>>>>> N MESSAGE " + notification.message);
		
		return ok();
	}
}
