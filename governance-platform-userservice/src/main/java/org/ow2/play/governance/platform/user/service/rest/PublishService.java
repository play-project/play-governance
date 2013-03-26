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

import static org.ow2.play.governance.platform.user.api.rest.helpers.Response.error;
import static org.ow2.play.governance.platform.user.api.rest.helpers.Response.ok;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.NotificationService;
import org.ow2.play.governance.resources.TopicHelper;

/**
 * @author chamerling
 * 
 */
public class PublishService extends AbstractService implements
		org.ow2.play.governance.platform.user.api.rest.PublishService {

	private NotificationService notificationService;

	public Response notify(String resource, String message) {

		// for now publish is only supported in topics
		if (resource == null) {
			return error(Status.BAD_REQUEST, "resource is mandatory");
		}

		if (message == null) {
			return error(Status.BAD_REQUEST, "message is mandatory");
		}

		if (!TopicHelper.isTopic(resource)) {
			return error(Status.BAD_REQUEST,
					"Publish is only supported in topics/streams");
		}

		// user can publish into the resource only if he has enough
		// permissions...
		if (!permissionChecker.checkResource(getUser().login,
				resource)) {
			return error(Status.UNAUTHORIZED, "Not allowed to pusblish into "
					+ resource);
		}

		// let's push data to the real notification service (ie DSB at the end)
		try {
			notificationService.publish(resource,
					message, "json");
		} catch (GovernanceExeption e) {
			e.printStackTrace();
			return error(Status.INTERNAL_SERVER_ERROR,
					"Something bad occured in the platform...");
		}

		return ok();
	}

	/**
	 * @param notificationService
	 *            the notificationService to set
	 */
	public void setNotificationService(NotificationService notificationService) {
		this.notificationService = notificationService;
	}
}
