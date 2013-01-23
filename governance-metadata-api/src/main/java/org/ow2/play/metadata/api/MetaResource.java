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
package org.ow2.play.metadata.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Bind resource and metadata
 * 
 * @author chamerling
 * 
 */
public class MetaResource {

	private Resource resource;

	private List<Metadata> metadata;

	/**
	 * 
	 */
	public MetaResource() {
		metadata = new ArrayList<Metadata>();
	}

	public MetaResource(Resource resource, List<Metadata> metadata) {
		super();
		this.resource = resource;
		this.metadata = metadata;
	}

	/**
	 * @return the resource
	 */
	public Resource getResource() {
		return resource;
	}

	/**
	 * @param resource
	 *            the resource to set
	 */
	public void setResource(Resource resource) {
		this.resource = resource;
	}

	/**
	 * @return the metadata
	 */
	public List<Metadata> getMetadata() {
		return metadata;
	}

	/**
	 * @param metadata
	 *            the metadata to set
	 */
	public void setMetadata(List<Metadata> metadata) {
		this.metadata = metadata;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MetaResource [resource=" + resource + ", metadata=" + metadata
				+ "]";
	}
}
