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
package org.ow2.play.metadata.service;

import java.util.List;
import java.util.logging.Logger;

import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.Metadata;
import org.ow2.play.metadata.api.MetadataException;
import org.ow2.play.metadata.api.service.MetadataBootstrap;
import org.ow2.play.metadata.api.service.MetadataLoader;
import org.ow2.play.metadata.api.service.MetadataService;

/**
 * A bootstrap service used to load metadata from resources
 * 
 * @author chamerling
 * 
 */
public class BootstrapServiceImpl implements MetadataBootstrap {

	private MetadataService metadataService;

	private MetadataLoader metadataLoader;

	static Logger logger = Logger.getLogger(BootstrapServiceImpl.class
			.getName());

	/**
	 * 
	 */
	public BootstrapServiceImpl() {
	}

	/**
	 * Load data from a list of resources. Add them to the registry if needed.
	 */
	public void init(List<String> urls) {
		if (metadataLoader != null && metadataService != null && urls != null) {
			for (String url : urls) {
				logger.info("Loading data from " + url);
				try {
					List<MetaResource> list = metadataLoader.load(url);

					for (MetaResource metaResource : list) {
						for (Metadata metadata : metaResource.getMetadata()) {
							metadataService.addMetadata(
									metaResource.getResource(), metadata);
						}
					}
				} catch (MetadataException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @param metadataService
	 *            the metadataService to set
	 */
	public void setMetadataService(MetadataService metadataService) {
		this.metadataService = metadataService;
	}

	/**
	 * @param metadataLoader
	 *            the metadataLoader to set
	 */
	public void setMetadataLoader(MetadataLoader metadataLoader) {
		this.metadataLoader = metadataLoader;
	}

}
