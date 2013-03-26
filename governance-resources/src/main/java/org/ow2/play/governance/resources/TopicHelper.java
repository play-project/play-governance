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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.ow2.play.governance.api.Constants;
import org.ow2.play.governance.api.bean.Topic;
import org.ow2.play.metadata.api.Data;
import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.Metadata;
import org.ow2.play.metadata.api.Resource;
import org.ow2.play.metadata.api.Type;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

/**
 * @author chamerling
 *
 */
public class TopicHelper {
	
	private static Logger logger = Logger.getLogger(TopicHelper.class.getName());
	
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
	
	public static boolean isTopic(String resourceURI) {
		return resourceURI != null && resourceURI.endsWith("#" + Constants.STREAM_RESOURCE_NAME);
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
	
	/**
	 * Create a metaresource from a topic.
	 * 
	 * @param topic
	 * @return
	 */
	public static final MetaResource transform(Topic topic) {
		if (topic == null) {
			return null;
		}

		Resource r = getResource(topic);
		List<Metadata> meta = Lists.newArrayList(
				new Metadata(Constants.QNAME_LOACALPART_URL, new Data(
						Type.LITERAL, topic.getName())),
				new Metadata(Constants.QNAME_NS_URL, new Data(Type.URI, topic
						.getNs())), new Metadata(Constants.QNAME_PREFIX_URL,
						new Data(Type.LITERAL, topic.getPrefix())),
				new Metadata(Constants.TOPIC, new Data(Type.URI, r.getUrl())));

		return new MetaResource(r, meta);
	}

	/**
	 * Transform a metadata resource into a Topic ie get all the topic
	 * information from the metaresource.
	 * 
	 * @param metaResource
	 * @return
	 */
	public static final Topic transform(MetaResource metaResource) {
		if (metaResource == null) {
			return null;
		}
		Topic topic = new Topic();

		if (Constants.STREAM_RESOURCE_NAME.equals(metaResource.getResource()
				.getName())) {
			String ns = getTopicNS(metaResource);
			String name = getTopicName(metaResource);
			String prefix = getTopicPrefix(metaResource);
			topic.setName(name);
			topic.setNs(ns);
			topic.setPrefix(prefix);
		}
		return topic;
	}
	
}
