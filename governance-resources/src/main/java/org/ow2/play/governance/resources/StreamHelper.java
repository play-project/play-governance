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

import java.util.logging.Logger;

import org.ow2.play.governance.api.Constants;
import org.ow2.play.governance.api.bean.Topic;
import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.Resource;

/**
 * @author chamerling
 * 
 */
public class StreamHelper {

	private static Logger logger = Logger.getLogger(StreamHelper.class
			.getName());

	private StreamHelper() {
	}

	public static final String getStreamURL(MetaResource mr) {
		if (mr == null || !isStream(mr.getResource())) {
			return null;
		}
		return getStreamURL(mr.getResource());
	}

	public static String getStreamURL(Resource resource) {
		if (!isStream(resource)) {
			return null;
		}
		return resource.getUrl() + "#" + resource.getName();
	}

	public static final boolean isStream(String resourceURI) {
		return resourceURI != null
				&& resourceURI.endsWith("#" + Constants.STREAM_RESOURCE_NAME);
	}

	public static final boolean isStream(Resource resource) {
		return resource != null && resource.getName() != null
				&& resource.getName().equals(Constants.STREAM_RESOURCE_NAME);
	}

	public static final String getStreamID(Topic topic) {
		if (topic == null) {
			return null;
		}
		return TopicHelper.get(topic);
	}

}
