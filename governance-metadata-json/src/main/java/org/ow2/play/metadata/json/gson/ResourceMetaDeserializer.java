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
package org.ow2.play.metadata.json.gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.ow2.play.metadata.api.Data;
import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.Metadata;
import org.ow2.play.metadata.api.Resource;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * Deserialize an array of resourcemeta as list
 * 
 * @author chamerling
 * 
 */
public class ResourceMetaDeserializer implements
		JsonDeserializer<List<MetaResource>> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement,
	 * java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public List<MetaResource> deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {

		List<MetaResource> result = new ArrayList<MetaResource>();
		JsonObject object = json.getAsJsonObject();

		Set<Map.Entry<String, JsonElement>> entries = object.entrySet();

		// we just need to process the first element...
		Iterator<Entry<String, JsonElement>> iter = entries.iterator();
		while (iter.hasNext()) {
			Map.Entry<java.lang.String, com.google.gson.JsonElement> entry = iter
					.next();
			Resource resource = new Resource();
			List<Metadata> meta = new ArrayList<Metadata>();

			if (entry.getKey().contains("#")) {
				resource.setName(entry.getKey().substring(
						entry.getKey().lastIndexOf('#') + 1));
				resource.setUrl(entry.getKey().substring(0,
						entry.getKey().lastIndexOf('#')));
			} else {
				resource.setName(entry.getKey());
				resource.setUrl(entry.getKey());
			}

			JsonObject metas = entry.getValue().getAsJsonObject();
			Set<Map.Entry<String, JsonElement>> metass = metas.entrySet();
			for (Entry<String, JsonElement> entry2 : metass) {
				Metadata md = new Metadata();
				md.setName(entry2.getKey());

				JsonArray array = entry2.getValue().getAsJsonArray();
				for (JsonElement jsonElement : array) {
					Data data = new Data(jsonElement.getAsJsonObject()
							.getAsJsonPrimitive("type").getAsString(),
							jsonElement.getAsJsonObject()
									.getAsJsonPrimitive("value").getAsString());
					md.getData().add(data);
				}
				meta.add(md);
			}
			result.add(new MetaResource(resource, meta));
		}

		return result;
	}
}
