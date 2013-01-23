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
package org.ow2.play.service.registry;

import java.net.URL;

import junit.framework.TestCase;

/**
 * @author chamerling
 * 
 */
public class InMemoryRegistryImplTest extends TestCase {

	public void testPutGet() throws Exception {
		InMemoryRegistryImpl impl = new InMemoryRegistryImpl();
		assertNull(impl.get("foo"));

		impl.put("foo", "http://bar");

		assertNotNull(impl.get("foo"));

		assertEquals("http://bar", impl.get("foo"));
	}

	public void testClear() throws Exception {
		InMemoryRegistryImpl impl = new InMemoryRegistryImpl();
		impl.put("foo", "http://bar");
		assertNotNull(impl.get("foo"));

		impl.clear();
		assertNull(impl.get("foo"));
	}

	public void testKeys() throws Exception {
		InMemoryRegistryImpl impl = new InMemoryRegistryImpl();

		assertEquals(0, impl.keys().size());

		impl.put("foo", "http://bar");
		assertNotNull(impl.keys());

		assertEquals(1, impl.keys().size());
		assertEquals("foo", impl.keys().get(0));
	}

	public void testPutNotURL() throws Exception {
		InMemoryRegistryImpl impl = new InMemoryRegistryImpl();
		assertNull(impl.get("foo"));

		impl.put("foo", "bar");

		assertNull(impl.get("foo"));
	}

	public void testLoadFromClasspath() throws Exception {
		InMemoryRegistryImpl impl = new InMemoryRegistryImpl();

		URL url = InMemoryRegistryImpl.class.getResource("/data.properties");

		assertNotNull(url);
		
		assertEquals(0, impl.keys().size());

		impl.load(url.toString());
		
		assertEquals(1, impl.keys().size());
		assertEquals("foo", impl.keys().get(0));
		assertEquals("http://bar", impl.get("foo"));

	}

}
