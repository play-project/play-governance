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

import junit.framework.TestCase;

import org.ow2.play.metadata.api.Data;
import org.ow2.play.metadata.api.Metadata;
import org.ow2.play.metadata.api.Resource;

/**
 * @author chamerling
 * 
 */
public class InMemoryMetadataTest extends TestCase {

	public void testResourceExsists() throws Exception {
		InMemoryMetadataServiceImpl service = new InMemoryMetadataServiceImpl();
		assertFalse(service.resourceExists(null));

		Resource r = new Resource("foo", "http://bar");
		assertFalse(service.resourceExists(r));
	}

	public void testCreateResource() throws Exception {
		InMemoryMetadataServiceImpl service = new InMemoryMetadataServiceImpl();
		Resource r = new Resource("foo", "http://bar");

		service.create(r);
		assertTrue(service.resourceExists(r));
	}

	public void testAddGetMeta() throws Exception {
		InMemoryMetadataServiceImpl service = new InMemoryMetadataServiceImpl();
		Resource r = new Resource("foo", "http://bar");
		Metadata m = new Metadata("name", new Data("type", "value"));

		service.addMetadata(r, m);
		assertTrue(service.getMetaData(r).size() == 1);

		Resource rr = new Resource("bar", "http://foo");

		assertTrue(service.getMetaData(rr).size() == 0);

		assertTrue(service.getMetaData(null).size() == 0);

	}

	public void testRemove() throws Exception {
		InMemoryMetadataServiceImpl service = new InMemoryMetadataServiceImpl();
		Resource r = new Resource("foo", "http://bar");
		Metadata m = new Metadata("name", new Data("type", "value"));
		Metadata mm = new Metadata("name1", new Data("type1", "value1"));

		service.addMetadata(r, m);
		service.addMetadata(r, mm);

		service.removeMetadata(r, m);

		assertTrue(service.getMetaData(r).size() == 1);
	}

	public void testAddNSame() throws Exception {
		InMemoryMetadataServiceImpl service = new InMemoryMetadataServiceImpl();
		Resource r = new Resource("foo", "http://bar");
		Metadata m = new Metadata("name", new Data("type", "value"));

		service.addMetadata(r, m);
		service.addMetadata(r, m);

		assertEquals(1, service.getMetaData(r).size());
	}

	public void testDelete() throws Exception {
		InMemoryMetadataServiceImpl service = new InMemoryMetadataServiceImpl();
		Resource r = new Resource("foo", "http://bar");
		Metadata m = new Metadata("name", new Data("type", "value"));
		Metadata mm = new Metadata("name1", new Data("type1", "value1"));

		service.addMetadata(r, m);
		service.addMetadata(r, mm);

		service.deleteMetaData(r);
		assertEquals(0, service.getMetaData(r).size());
	}

	public void testGetMetadataValue() throws Exception {
		InMemoryMetadataServiceImpl service = new InMemoryMetadataServiceImpl();
		Resource r = new Resource("foo", "http://bar");
		Metadata m = new Metadata("findme", new Data("type", "value"));
		service.addMetadata(r, m);

		assertNull(service.getMetadataValue(r, "foo"));
		
		Metadata result = service.getMetadataValue(r, "findme");
		
		assertNotNull(result);
		assertEquals("findme", result.getName());
		assertEquals(1, result.getData().size());
		assertEquals(new Data("type", "value"), result.getData().get(0));
		
	}
	
	public void testList() throws Exception {
		InMemoryMetadataServiceImpl service = new InMemoryMetadataServiceImpl();
		Resource r = new Resource("foo", "http://bar");
		Metadata m = new Metadata("findme", new Data("type", "value"));
		service.addMetadata(r, m);

		assertNotNull(service.list());
		assertTrue(service.list().size() == 1);
	}
}
