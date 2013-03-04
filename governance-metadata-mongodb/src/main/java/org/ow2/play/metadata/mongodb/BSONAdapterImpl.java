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
package org.ow2.play.metadata.mongodb;

import org.bson.types.BasicBSONList;
import org.ow2.play.metadata.api.Data;
import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.Metadata;
import org.ow2.play.metadata.api.Resource;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * @author chamerling
 * 
 */
public class BSONAdapterImpl implements BSONAdapter {

	public DBObject createBSON(MetaResource metaResource) {
		DBObject result = null;

		if (metaResource != null) {
			result = new BasicDBObject();
			result.put("resource", createBSON(metaResource.getResource()));

			BasicBSONList list = new BasicBSONList();
			for (Metadata md : metaResource.getMetadata()) {
				list.add(createBSON(md));
			}
			result.put("metadata", list);
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.metadata.mongodb.BSONAdapter#createBSON(org.ow2.play.metadata
	 * .api.Resource)
	 */
	@Override
	public DBObject createBSON(Resource resource) {
		DBObject result = null;
		if (resource != null) {
			result = new BasicDBObject();
			result.put("name", resource.getName());
			result.put("url", resource.getUrl());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.metadata.mongodb.BSONAdapter#createBSON(org.ow2.play.metadata
	 * .api.Metadata)
	 */
	@Override
	public DBObject createBSON(Metadata metadata) {
		DBObject result = null;
		if (metadata != null) {
			result = new BasicDBObject();
			result.put("name", metadata.getName());
			BasicBSONList list = new BasicBSONList();
			for (Data data : metadata.getData()) {
				list.add(createBSON(data));
			}
			result.put("data", list);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.metadata.mongodb.BSONAdapter#createBSON(org.ow2.play.metadata
	 * .api.Data)
	 */
	@Override
	public DBObject createBSON(Data data) {
		DBObject result = null;
		if (data != null) {
			result = new BasicDBObject();
			result.put("type", data.getType());
			result.put("value", data.getValue());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.metadata.mongodb.BSONAdapter#readObject(com.mongodb.DBObject
	 * )
	 */
	@Override
	public MetaResource readMetaResource(DBObject dbo) {
		if (dbo == null) {
			return null;
		}

		MetaResource result = new MetaResource();
		result.setId(dbo.get("_id").toString());
		
		result.setResource(readResource((DBObject) dbo.get("resource")));

		Object o = dbo.get("metadata");
		if (o != null && o instanceof BasicDBList) {
			BasicDBList metalist = (BasicDBList) o;
			for (Object object : metalist) {

				if (object != null && object instanceof DBObject) {
					DBObject entry = (DBObject) object;

					System.out.println(entry);

					Metadata md = new Metadata();
					md.setName(entry.get("name").toString());

					Object data = entry.get("data");
					if (data != null && data instanceof BasicDBList) {
						BasicDBList list = (BasicDBList) data;
						for (Object object2 : list) {
							md.getData().add(
									new Data(((DBObject) object2).get("type")
											.toString(), ((DBObject) object2)
											.get("value").toString()));
						}
					}
					result.getMetadata().add(md);
				} else {
					System.out.println("BAD object : " + object);
				}
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.metadata.mongodb.BSONAdapter#readResource(com.mongodb.DBObject
	 * )
	 */
	@Override
	public Resource readResource(DBObject object) {
		if (object == null) {
			return null;
		}

		return new Resource(object.get("name").toString(), object.get("url")
				.toString());
	}

}
