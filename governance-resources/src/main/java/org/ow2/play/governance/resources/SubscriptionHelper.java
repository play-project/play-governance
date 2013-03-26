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
package org.ow2.play.governance.resources;

import org.ow2.play.governance.api.Constants;
import org.ow2.play.governance.api.bean.Subscription;
import org.ow2.play.metadata.api.Resource;

/**
 * @author chamerling
 * 
 */
public class SubscriptionHelper {

	private SubscriptionHelper() {
	}

	public static String get(Subscription s) {
		return getSubscriptionURL(s.getId());
	}

	public static String get(Resource resource) {
		return null;
	}

	public static String getSubscriptionURL(String id) {
		return String.format(Constants.SUBCRIPTION_PATTERN, id);
	}

	public static String getSubscriptionID(String resourceURI) {
		if (!isSubscription(resourceURI)) {
			return "";
		}
		return resourceURI.substring(
				Constants.SUBCRIPTION_PREFIX_URL.length(),
				resourceURI.lastIndexOf("#"
						+ Constants.SUBCRIPTION_RESOURCE_NAME));
	}

	public static boolean isSubscription(String resourceURI) {
		return resourceURI != null
				&& resourceURI.endsWith("#"
						+ Constants.SUBCRIPTION_RESOURCE_NAME);
	}

}
