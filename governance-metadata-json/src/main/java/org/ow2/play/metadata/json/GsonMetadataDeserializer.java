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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.MetadataException;
import org.ow2.play.metadata.api.service.MetadataDeserializer;
import org.ow2.play.metadata.json.gson.ResourceMetaDeserializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

/**
 * @author chamerling
 * 
 */
public class GsonMetadataDeserializer implements MetadataDeserializer {

	private Gson gson;

	public GsonMetadataDeserializer() {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(MetaResource.class,
				new ResourceMetaDeserializer());
		gson = builder.create();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.metadata.api.service.MetadataDeserializer#read(java.net.URL)
	 */
	@Override
	public List<MetaResource> read(InputStream is) throws MetadataException {

		Type type = new TypeToken<List<MetaResource>>() {
		}.getType();

		// FIXME : DO not know why we should hack like this.
		// We cannot do something like List<MetaResource> out = gson.fromJson(reader, type);
		// if we do that, we have a list of list of metaresource and then it fails...
		List<MetaResource> result = new ArrayList<MetaResource>();
		try {
			JsonReader reader = new JsonReader(new InputStreamReader(is));
			List<List<MetaResource>> o = gson.fromJson(reader, type);
			if (o.get(0) != null) {
				result.addAll(o.get(0));
			}
		} catch (Exception e) {
			throw new MetadataException(e);
		}
		return result;
	}
}
