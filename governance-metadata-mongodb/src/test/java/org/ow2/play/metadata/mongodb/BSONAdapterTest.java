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

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.bson.BSONObject;
import org.ow2.play.metadata.api.Data;
import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.Metadata;
import org.ow2.play.metadata.api.Resource;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;

/**
 * FIXME : Bad testing...
 * 
 * @author chamerling
 * 
 */
public class BSONAdapterTest extends TestCase {

	public void testCreateData() throws Exception {
		BSONAdapterImpl adapter = new BSONAdapterImpl();
		BSONObject o = adapter.createBSON(new Data("int", "123"));

		System.out.println(o);
		assertNotNull(o);
	}

	public void testCreateResource() throws Exception {
		BSONAdapterImpl adapter = new BSONAdapterImpl();
		BSONObject o = adapter.createBSON(new Resource("petals",
				"http://petals.ow2.org"));

		System.out.println(o);
		assertNotNull(o);
	}

	public void testCreateMeta() throws Exception {
		BSONAdapterImpl adapter = new BSONAdapterImpl();
		List<Data> data = new ArrayList<Data>();
		data.add(new Data("int", "1"));
		data.add(new Data("string", "123456"));
		BSONObject o = adapter.createBSON(new Metadata("metalist", data));

		System.out.println(o);
		assertNotNull(o);
	}

	public void testCreateMetaResource() throws Exception {
		BSONAdapterImpl adapter = new BSONAdapterImpl();
		List<Data> data = new ArrayList<Data>();
		data.add(new Data("int", "1"));
		data.add(new Data("string", "123456"));
		Metadata meta = new Metadata("metalist", data);

		List<Metadata> list = new ArrayList<Metadata>();
		list.add(meta);

		Resource r = new Resource("stream", "http://play.ow2.org/resource/foo");
		MetaResource mr = new MetaResource(r, list);
		BSONObject o = adapter.createBSON(mr);

		System.out.println(o);
		assertNotNull(o);
	}

}
