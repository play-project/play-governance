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
package org.ow2.play.governance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.ow2.play.governance.api.Constants;
import org.ow2.play.governance.api.bean.Topic;
import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.Metadata;
import org.ow2.play.metadata.api.Resource;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 * @author chamerling
 * 
 */
public class Helper {

	private static Logger logger = Logger.getLogger(Helper.class.getName());

	private Helper() {
	}
	
	public static final String getTopicName(MetaResource mr) {
		if (mr == null) {
			return null;
		}
		String result = null;

		Collection<Metadata> filter = Collections2.filter(mr.getMetadata(),
				new Predicate<Metadata>() {
					public boolean apply(Metadata metadata) {
						return metadata != null
								&& Constants.QNAME_LOACALPART_URL
										.equals(metadata.getName());
					};
				});

		if (filter != null && filter.size() > 0) {
			logger.fine("Getting TopicName from metadata");
			List<Metadata> l = new ArrayList<Metadata>(filter);
			if (l.get(0) != null && l.get(0).getData() != null
					&& l.get(0).getData().size() > 0) {
				result = l.get(0).getData().get(0).getValue();
			}
		}

		if (result == null) {
			logger.fine("Getting TopicName from metadata URL value");
			// get from Meta resource resource name...
			result = mr.getResource().getUrl()
					.substring(mr.getResource().getUrl().lastIndexOf('/') + 1);
		}

		return result;
	}

	public static final String getTopicPrefix(MetaResource mr) {
		if (mr == null) {
			return null;
		}
		String result = null;

		Collection<Metadata> filter = Collections2.filter(mr.getMetadata(),
				new Predicate<Metadata>() {
					public boolean apply(Metadata metadata) {
						return metadata != null
								&& Constants.QNAME_PREFIX_URL.equals(metadata
										.getName());
					};
				});

		if (filter != null && filter.size() > 0) {
			logger.fine("Getting Topic Prefix from metadata");
			List<Metadata> l = new ArrayList<Metadata>(filter);
			if (l.get(0) != null && l.get(0).getData() != null
					&& l.get(0).getData().size() > 0) {
				result = l.get(0).getData().get(0).getValue();
			}
		}

		if (result == null) {
			logger.fine("Was not able to get prefix from metadata...");
			result = "";
		}
		return result;
	}

	public static final String getTopicNS(MetaResource mr) {
		if (mr == null) {
			return null;
		}
		String result = null;

		Collection<Metadata> filter = Collections2.filter(mr.getMetadata(),
				new Predicate<Metadata>() {
					public boolean apply(Metadata metadata) {
						return metadata != null
								&& Constants.QNAME_NS_URL.equals(metadata
										.getName());
					};
				});

		if (filter != null && filter.size() > 0) {
			logger.fine("Getting TopicNS from metadata");
			List<Metadata> l = new ArrayList<Metadata>(filter);
			if (l.get(0) != null && l.get(0).getData() != null
					&& l.get(0).getData().size() > 0) {
				result = l.get(0).getData().get(0).getValue();
			}
		}

		if (result == null) {
			logger.fine("Getting TopicNS from metadata URL value");
			// get from Meta resource resource name...
			result = mr
					.getResource()
					.getUrl()
					.substring(0,
							mr.getResource().getUrl().lastIndexOf('/') + 1);
		}

		return result;
	}
	
	/**
	 * Get the resource from the topic
	 * 
	 * @param topic
	 * @return
	 */
	public static Resource getResource(Topic topic) {
		String url = topic.getNs();
		if (!url.endsWith("/")) {
			url = url + "/";
		}
		url = url + topic.getName();
		return new Resource(Constants.STREAM_RESOURCE_NAME, url);		
	}
}
