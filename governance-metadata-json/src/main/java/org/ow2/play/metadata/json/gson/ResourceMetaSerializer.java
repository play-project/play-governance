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

import org.ow2.play.metadata.api.Data;
import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.Metadata;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @author chamerling
 * 
 */
public class ResourceMetaSerializer implements JsonSerializer<MetaResource> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object,
	 * java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(MetaResource metaResource, Type type,
			JsonSerializationContext context) {

		JsonObject result = new JsonObject();
		JsonObject elements = new JsonObject();

		// let's iterate over the metadata to create custom JSONObject
		for (Metadata metadata : metaResource.getMetadata()) {
			JsonArray array = new JsonArray();
			for (Data data : metadata.getData()) {
				JsonObject typeValueElement = new JsonObject();
				typeValueElement.addProperty("type", data.getType());
				typeValueElement.addProperty("value", data.getValue());
				array.add(typeValueElement);
			}
			elements.add(metadata.getName(), array);
		}

		result.add(metaResource.getResource().toString(), elements);

		return result;
	}
}
