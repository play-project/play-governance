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
package org.ow2.play.metadata.service;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ow2.play.metadata.api.Data;
import org.ow2.play.metadata.api.Metadata;
import org.ow2.play.metadata.api.MetadataException;
import org.ow2.play.metadata.api.Resource;
import org.ow2.play.metadata.service.document.MetaResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author chamerling
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class MetadataServiceTest {

	@Autowired
	MetadataServiceImpl metadataService;

	@Autowired
	MongoTemplate mongoTemplate;

	/**
	 * Clear the collection for each test
	 */
	@After
	public void tearDown() {
		mongoTemplate.dropCollection("metadata");
	}

	@Test
	public void createNull() {
		try {
			metadataService.create(null);
			fail();
		} catch (MetadataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void createNotNull() {
		org.ow2.play.metadata.api.MetaResource mr = new org.ow2.play.metadata.api.MetaResource();
		mr.setResource(new Resource("foo", "http://bar"));
		try {
			metadataService.create(mr);
		} catch (MetadataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}

		long nb = mongoTemplate.count(new Query(), MetaResource.class);
		assertTrue(nb == 1);
	}

	/**
	 * 
	 */
	@Test
	public void addMetadata() {
		Resource r = new Resource(UUID.randomUUID().toString(), "http://bar");

		org.ow2.play.metadata.api.MetaResource mr = new org.ow2.play.metadata.api.MetaResource();
		mr.setResource(r);
		try {
			metadataService.create(mr);
		} catch (MetadataException e) {
			e.printStackTrace();
			fail();
		}

		Metadata md = new Metadata("mymeta", new Data("mytype", "myvalue"));
		try {
			metadataService.addMetadata(r, md);
		} catch (MetadataException e) {
			e.printStackTrace();
			fail();
		}

		List<MetaResource> list = mongoTemplate.findAll(MetaResource.class);
		int i = 0;
		for (MetaResource metaResource : list) {
			System.out.println(metaResource.id);
			System.out.println(metaResource.resource);
			System.out.println(metaResource.metadata);
			i++;
		}
		assertTrue(i > 0);
	}

	@Test
	public void getMetaFromResource() {
		Resource r = new Resource(UUID.randomUUID().toString(), "http://bar");

		org.ow2.play.metadata.api.MetaResource mr = new org.ow2.play.metadata.api.MetaResource();
		mr.setResource(r);
		try {
			metadataService.create(mr);
		} catch (MetadataException e) {
			e.printStackTrace();
			fail();
		}

		try {
			List<Metadata> list = metadataService.getMetaData(r);
			assertNotNull(list);
			System.out.println(list);
		} catch (MetadataException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void removeMeta() {
		Resource r = new Resource(UUID.randomUUID().toString(), "http://bar");
		Metadata pullme = new Metadata("pullme", new Data("mytype", "myvalue"));
		Metadata dontpullme = new Metadata("dontpullme", new Data("mytype",
				"myvalue"));

		org.ow2.play.metadata.api.MetaResource mr = new org.ow2.play.metadata.api.MetaResource();
		mr.setResource(r);
		mr.getMetadata().add(pullme);
		mr.getMetadata().add(dontpullme);

		try {
			metadataService.create(mr);
		} catch (MetadataException e) {
			e.printStackTrace();
			fail();
		}

		try {
			List<Metadata> list = metadataService.getMetaData(r);
			assertTrue(list.size() >= 2); // potentially having data inserted by
											// the framework...

			assertTrue(list.contains(pullme));
			assertTrue(list.contains(dontpullme));

			System.out.println(list);

			metadataService.removeMetadata(r, pullme);
			List<Metadata> list2 = metadataService.getMetaData(r);
			assertTrue(list2.size() < list.size());

			assertTrue(!list2.contains(pullme));
			assertTrue(list2.contains(dontpullme));

		} catch (MetadataException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void removeUnknownMeta() {
		Resource r = new Resource(UUID.randomUUID().toString(), "http://bar");
		Metadata pullme = new Metadata("pullme", new Data("mytype", "myvalue"));
		Metadata dontpullme = new Metadata("dontpullme", new Data("mytype",
				"myvalue"));

		org.ow2.play.metadata.api.MetaResource mr = new org.ow2.play.metadata.api.MetaResource();
		mr.setResource(r);
		mr.getMetadata().add(dontpullme);

		try {
			metadataService.create(mr);
		} catch (MetadataException e) {
			e.printStackTrace();
			fail();
		}

		try {
			List<Metadata> list = metadataService.getMetaData(r);
			assertTrue(list.size() >= 2); // potentially having data inserted by
											// the framework...

			assertTrue(!list.contains(pullme));
			assertTrue(list.contains(dontpullme));

			System.out.println(list);

			metadataService.removeMetadata(r, pullme);
			List<Metadata> list2 = metadataService.getMetaData(r);
			assertTrue(list2.size() == list.size());

			assertTrue(!list2.contains(pullme));
			assertTrue(list2.contains(dontpullme));

		} catch (MetadataException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void existsTrue() {
		Resource r = new Resource(UUID.randomUUID().toString(), "http://bar");

		org.ow2.play.metadata.api.MetaResource mr = new org.ow2.play.metadata.api.MetaResource();
		mr.setResource(r);

		try {
			metadataService.create(mr);
		} catch (MetadataException e) {
			e.printStackTrace();
			fail();
		}

		try {
			assertTrue(metadataService.exists(r));
		} catch (MetadataException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void existsFalse() {
		Resource r = new Resource(UUID.randomUUID().toString(), "http://bar");

		org.ow2.play.metadata.api.MetaResource mr = new org.ow2.play.metadata.api.MetaResource();
		mr.setResource(r);

		try {
			metadataService.create(mr);
		} catch (MetadataException e) {
			e.printStackTrace();
			fail();
		}

		try {
			assertFalse(metadataService.exists(new Resource(UUID.randomUUID()
					.toString(), "http://foo")));
		} catch (MetadataException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void delete() {
		Resource foo = new Resource(UUID.randomUUID().toString(), "http://foo");
		Resource bar = new Resource(UUID.randomUUID().toString(), "http://bar");

		try {
			metadataService.create(new org.ow2.play.metadata.api.MetaResource(
					foo, new ArrayList<Metadata>()));
			metadataService.create(new org.ow2.play.metadata.api.MetaResource(
					bar, new ArrayList<Metadata>()));
		} catch (MetadataException e) {
			e.printStackTrace();
			fail();
		}

		try {
			assertTrue(metadataService.list().size() == 2);

			metadataService.deleteResource(foo);

			List<org.ow2.play.metadata.api.MetaResource> list = metadataService
					.list();
			assertTrue(list.size() == 1);

			System.out.println(list);

			metadataService.deleteResource(new Resource(UUID.randomUUID()
					.toString(), "http://foobar"));

			assertTrue(metadataService.list().size() == 1);

		} catch (MetadataException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void deleteMeta() {
		Resource foo = new Resource(UUID.randomUUID().toString(), "http://foo");
		org.ow2.play.metadata.api.MetaResource mr = new org.ow2.play.metadata.api.MetaResource(
				foo, new ArrayList<Metadata>());
		mr.getMetadata().add(new Metadata("foo", new Data("mytype", "myvalue")));

		try {
			metadataService.create(mr);
		} catch (MetadataException e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			metadataService.deleteMetaData(foo);
			List<Metadata> list = metadataService.getMetaData(foo);
			assertTrue(list.size() == 0);
		} catch (MetadataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void listWhere() {
		Resource foo = new Resource("foo", "http://foo"+UUID.randomUUID().toString());
		Resource bar = new Resource("foo", "http://foo"+UUID.randomUUID().toString());

		try {
			metadataService.create(new org.ow2.play.metadata.api.MetaResource(
					foo, new ArrayList<Metadata>()));
			metadataService.create(new org.ow2.play.metadata.api.MetaResource(
					bar, new ArrayList<Metadata>()));
		} catch (MetadataException e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			List<org.ow2.play.metadata.api.MetaResource> list = metadataService.listWhere("foo", null);
			assertTrue(list.size() == 2);
			
			list = metadataService.listWhere("bar", null);
			assertTrue(list.size() == 0);
			
			list = metadataService.listWhere(null, "123");
			assertTrue(list.size() == 0);
			
			list = metadataService.listWhere(foo.getName(), foo.getUrl());
			assertTrue(list.size() == 1);
			
		} catch (MetadataException e) {
			e.printStackTrace();
			fail();
		}
		
	}
	
	@Test
	public void getMetadataValue() {
		Resource foo = new Resource(UUID.randomUUID().toString(), "http://foo");
		org.ow2.play.metadata.api.MetaResource mr = new org.ow2.play.metadata.api.MetaResource(
				foo, new ArrayList<Metadata>());
		Metadata m = new Metadata("foo", new Data("mytype", "myvalue"));
		mr.getMetadata().add(m);

		try {
			metadataService.create(mr);
		} catch (MetadataException e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			Metadata md = metadataService.getMetadataValue(foo, m.getName());
			assertNotNull(md);
			assertEquals(md.getName(), m.getName());
			assertEquals(md.getData().get(0), m.getData().get(0));
			
			md = metadataService.getMetadataValue(foo, "bar");
			assertNull(md);
			
			md = metadataService.getMetadataValue(new Resource("foo", "bar"), "baz");
			assertNull(md);
			
		} catch (MetadataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void getlistWhereDataIn() {
		Data data = new Data("mytype", "myvalue");
		String name = UUID.randomUUID().toString();
		
		Resource foo = new Resource(name, "http://foo");
		org.ow2.play.metadata.api.MetaResource mr = new org.ow2.play.metadata.api.MetaResource(
				foo, new ArrayList<Metadata>());
		Metadata m = new Metadata("foo", data);
		mr.getMetadata().add(m);

		try {
			metadataService.create(mr);
		} catch (MetadataException e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			List<org.ow2.play.metadata.api.MetaResource> list = metadataService.listWhereData(foo.getName(), "foo", data);
			assertNotNull(list);
			assertEquals(list.size(), 1);
			
		} catch (MetadataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void getlistWhereDataNotIn() {
		Data data = new Data("mytype", "myvalue");
		String name = UUID.randomUUID().toString();
		
		Resource foo = new Resource(name, "http://foo");
		org.ow2.play.metadata.api.MetaResource mr = new org.ow2.play.metadata.api.MetaResource(
				foo, new ArrayList<Metadata>());
		Metadata m = new Metadata("foo", data);
		mr.getMetadata().add(m);

		try {
			metadataService.create(mr);
		} catch (MetadataException e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			List<org.ow2.play.metadata.api.MetaResource> list = metadataService.listWhereData("A", "foo", data);
			assertNotNull(list);
			assertEquals(list.size(), 0);
			
		} catch (MetadataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void getlistWhereDataList() {
		Data data = new Data("mytype", "myvalue");
		String name = UUID.randomUUID().toString();
		
		Resource foo = new Resource(name, "http://foo");
		org.ow2.play.metadata.api.MetaResource mr = new org.ow2.play.metadata.api.MetaResource(
				foo, new ArrayList<Metadata>());
		
		mr.getMetadata().add(new Metadata("foo", data));
		mr.getMetadata().add(new Metadata("bar", data));
		
		int size = 10;
		
		try {
			for (int i = 0; i < size; i++) {
				metadataService.create(mr);
			}
		} catch (MetadataException e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			List<org.ow2.play.metadata.api.MetaResource> list = metadataService.listWhereData(name, "foo", data);
			assertNotNull(list);
			assertEquals(10, list.size());
			
		} catch (MetadataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void getlistWhereDataNotInList() {
		Data data = new Data("mytype", "myvalue");
		String name = UUID.randomUUID().toString();
		
		Resource foo = new Resource(name, "http://foo");
		org.ow2.play.metadata.api.MetaResource mr = new org.ow2.play.metadata.api.MetaResource(
				foo, new ArrayList<Metadata>());
		
		mr.getMetadata().add(new Metadata("foo", data));
		mr.getMetadata().add(new Metadata("bar", data));
		
		int size = 10;
		
		try {
			for (int i = 0; i < size; i++) {
				metadataService.create(mr);
			}
		} catch (MetadataException e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			List<org.ow2.play.metadata.api.MetaResource> list = metadataService.listWhereData(name, "foo", new Data("foo", "bar"));
			assertNotNull(list);
			assertEquals(0, list.size());
			
		} catch (MetadataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
	}
}
