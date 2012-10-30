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

import java.util.List;

import org.ow2.play.governance.api.Constants;
import org.ow2.play.governance.api.bean.Topic;
import org.ow2.play.metadata.api.Data;
import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.Metadata;
import org.ow2.play.metadata.api.Resource;
import org.ow2.play.metadata.api.Type;

import com.google.common.collect.Lists;

/**
 * Transform Topic to/from MetadataResource
 * 
 * @author chamerling
 * 
 */
public class Adapter {

	private Adapter() {
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

		Resource r = Helper.getResource(topic);
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
			String ns = Helper.getTopicNS(metaResource);
			String name = Helper.getTopicName(metaResource);
			String prefix = Helper.getTopicPrefix(metaResource);
			topic.setName(name);
			topic.setNs(ns);
			topic.setPrefix(prefix);
		}
		return topic;
	}
}
