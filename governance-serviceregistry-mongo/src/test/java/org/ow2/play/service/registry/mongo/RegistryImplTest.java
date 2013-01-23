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
package org.ow2.play.service.registry.mongo;

import java.util.List;
import java.util.UUID;

import junit.framework.TestCase;

import org.ow2.play.service.registry.api.RegistryException;

/**
 * @author chamerling
 * 
 */
public class RegistryImplTest extends TestCase {

	public void testNotInitialized() {
		RegistryImpl reg = new RegistryImpl();
		try {
			reg.get("foo");
			fail();
		} catch (RegistryException e) {
		}
	}

	public void testInit() {
		RegistryImpl reg = new RegistryImpl();
		try {
			reg.init();
		} catch (RegistryException e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Test that there are no exception...
	 * 
	 */
	public void testPut() {
		RegistryImpl reg = new RegistryImpl();
		try {
			reg.init();
			String name = UUID.randomUUID().toString();
			reg.put(name, "http://localhost");
		} catch (RegistryException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			reg.close();
		}
	}

	public void testPutGet() {
		RegistryImpl reg = new RegistryImpl();
		try {
			reg.init();
			String name = UUID.randomUUID().toString();
			reg.put(name, "foo");

			String value = reg.get(name);
			assertEquals("foo", value);
		} catch (RegistryException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			reg.close();
		}
	}

	public void testPutNullName() throws Exception {
		RegistryImpl reg = new RegistryImpl();
		try {
			reg.init();
			reg.put(null, "foo");
			fail();
		} catch (RegistryException e) {
		} finally {
			reg.close();
		}
	}

	public void testPutNullURL() throws Exception {
		RegistryImpl reg = new RegistryImpl();
		try {
			reg.init();
			String name = UUID.randomUUID().toString();
			reg.put(name, null);
			fail();
		} catch (RegistryException e) {
		} finally {
			reg.close();
		}
	}

	public void testGetNull() throws Exception {
		RegistryImpl reg = new RegistryImpl();
		try {
			reg.init();
			String name = UUID.randomUUID().toString();
			String value = reg.get(name);
			assertNull(value);
		} catch (RegistryException e) {
			fail(e.getMessage());
		} finally {
			reg.close();
		}
	}

	public void testGetKeys() throws Exception {
		RegistryImpl reg = new RegistryImpl();
		try {
			reg.init();
			String name = UUID.randomUUID().toString();

			// put a pair, get all the keys and check that the added one is on
			// the list
			
			reg.put(name, "http://getkeystest");

			List<String> keys = reg.keys();
			assertNotNull(keys);
			assertTrue(keys.contains(name));
			
			System.out.println(keys);
		} catch (RegistryException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			reg.close();
		}
	}
}
