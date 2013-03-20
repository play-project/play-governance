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
package org.ow2.play.governance.api.helpers;

import org.ow2.play.governance.api.Constants;
import org.ow2.play.governance.api.bean.Topic;

/**
 * @author chamerling
 *
 */
public class ResourceHelper {
	
	/**
	 * Change a topic to a platform resource identifier
	 * 
	 * @param topic
	 * @return
	 */
	public static String get(Topic topic) {
		StringBuffer sb = new StringBuffer(topic.getNs());
		if (!topic.getNs().endsWith("/")) {
			sb.append("/");
		}
		sb.append(topic.getName());
		sb.append("#");
		sb.append(Constants.STREAM_RESOURCE_NAME);
		return sb.toString();
	}

}
