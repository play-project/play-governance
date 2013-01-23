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
package org.ow2.play.metadata.json;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.MetadataException;
import org.ow2.play.metadata.api.service.MetaDataSerializer;
import org.ow2.play.metadata.json.gson.ResourceMetaSerializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author chamerling
 * 
 */
public class GsonMetadataSerializer implements MetaDataSerializer {

	private Gson gson;

	/**
	 * 
	 */
	public GsonMetadataSerializer() {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(MetaResource.class,
				new ResourceMetaSerializer());
		gson = builder.create();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.metadata.api.service.MetaDataSerializer#write(org.ow2.play
	 * .metadata.api.Resource, org.ow2.play.metadata.api.Metadata,
	 * java.io.OutputStream)
	 */
	@Override
	public void write(List<MetaResource> metaResource, OutputStream os)
			throws MetadataException {

		if (os == null) {
			throw new MetadataException("Null output stream is not allowed...");
		}

		try {
			IOUtils.write(gson.toJson(metaResource), os);
		} catch (IOException e) {
			throw new MetadataException(e);
		}
	}
	
}
