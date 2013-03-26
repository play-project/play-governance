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
package org.ow2.play.governance.service;

import java.util.List;
import java.util.Map;

import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.WSNTopicNotificationSender;
import org.ow2.play.governance.resources.TopicHelper;
import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.MetadataException;
import org.ow2.play.metadata.api.Resource;
import org.ow2.play.metadata.api.service.MetadataService;
import org.ow2.play.service.registry.api.Registry;
import org.ow2.play.service.registry.api.RegistryException;

/**
 * @author chamerling
 * 
 */
public class NotificationService implements
		org.ow2.play.governance.api.NotificationService {

	private Registry registry;

	private MetadataService metadataService;

	private Map<String, WSNTopicNotificationSender> senders;

	@Override
	public void publish(String resource, String message, String type)
			throws GovernanceExeption {

		WSNTopicNotificationSender sender = getSender(type);
		if (sender == null) {
			throw new GovernanceExeption("Can not find WSN sender for type "
					+ type);
		}

		try {
			// get the DSB notify endpoint, translate resource to topic and send
			// it the notification
			// get the meta resource for the given resource
			Resource r = org.ow2.play.metadata.api.Helper
					.getResourceFromURI(resource);
			List<MetaResource> metas = null;
			try {
				metas = metadataService.listWhere(r.getName(), r.getUrl());
			} catch (MetadataException e) {
				e.printStackTrace();
				throw new GovernanceExeption("Error while getting metadata", e);
			}

			if (metas == null || metas.size() == 0) {
				throw new GovernanceExeption(
						"Can not find the resource in the platform");
			}

			sender.notify(
					registry.get(org.ow2.play.service.registry.api.Constants.DSB_CONSUMER),
					TopicHelper.transform(metas.get(0)), message);

		} catch (RegistryException e) {
			throw new GovernanceExeption(e);
		}
	}

	protected WSNTopicNotificationSender getSender(String type) {
		return senders.get(type);
	}

	/**
	 * @param registry
	 *            the registry to set
	 */
	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	/**
	 * @param metadataService
	 *            the metadataService to set
	 */
	public void setMetadataService(MetadataService metadataService) {
		this.metadataService = metadataService;
	}

	/**
	 * @param senders
	 *            the senders to set
	 */
	public void setSenders(Map<String, WSNTopicNotificationSender> senders) {
		this.senders = senders;
	}

}
