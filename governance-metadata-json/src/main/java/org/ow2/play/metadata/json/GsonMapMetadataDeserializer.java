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
import java.util.Map;
import java.util.Map.Entry;

import org.ow2.play.metadata.api.Data;
import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.Metadata;
import org.ow2.play.metadata.api.MetadataException;
import org.ow2.play.metadata.api.Resource;
import org.ow2.play.metadata.api.service.MetadataDeserializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

/**
 * @author chamerling
 * 
 */
public class GsonMapMetadataDeserializer implements MetadataDeserializer {

	private Gson gson;

	/**
	 * 
	 */
	public GsonMapMetadataDeserializer() {
		GsonBuilder builder = new GsonBuilder();
		gson = builder.create();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.metadata.api.service.MetadataDeserializer#read(java.io.
	 * InputStream)
	 */
	@Override
	public List<MetaResource> read(InputStream is) throws MetadataException {
		Type type = new TypeToken<Map<String, Map<String, List<Data>>>>() {
		}.getType();

		List<MetaResource> result = new ArrayList<MetaResource>();
		try {
			JsonReader reader = new JsonReader(new InputStreamReader(is));
			Map<String, Map<String, List<Data>>> o = gson
					.fromJson(reader, type);

			for (Entry<String, Map<String, List<Data>>> set : o.entrySet()) {
				Resource resource = new Resource();

				String key = set.getKey();
				if (key.contains("#")) {
					resource.setName(key.substring(key.lastIndexOf('#') + 1));
					resource.setUrl(key.substring(0, key.lastIndexOf('#')));
				} else {
					resource.setName(key);
					resource.setUrl(key);
				}

				Map<String, List<Data>> value = set.getValue();
				List<Metadata> list = new ArrayList<Metadata>();
				for (Entry<String, List<Data>> v : value.entrySet()) {
					Metadata metadata = new Metadata();
					metadata.setName(v.getKey());
					metadata.setData(v.getValue());
					list.add(metadata);
				}
				result.add(new MetaResource(resource, list));
			}
		} catch (Exception e) {
			throw new MetadataException(e);
		}
		return result;
	}
}
