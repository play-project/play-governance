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
package org.ow2.play.metadata.api;

import junit.framework.TestCase;

/**
 * @author chamerling
 *
 */
public class HelperTest extends TestCase {
	
	public void testGetURI() throws Exception {
		Resource resource = new Resource("foo", "http://bar");
		assertEquals("http://bar#foo", Helper.getURI(resource));
	}
	
	public void testGetID() throws Exception {
		Resource resource = new Resource("foo", "http://bar/baz");
		assertEquals("baz", Helper.getID(resource));
	}
	
	public void testGetIDEnd() throws Exception {
		Resource resource = new Resource("foo", "http://bar/baz/");
		assertEquals("baz", Helper.getID(resource));
	}
	
	public void testGetResourceFromURI() {
		String name = "stream";
		String url = "http://streams.foo.com/Stream";
		String uri = url + "#" + name;
		
		Resource r = Helper.getResourceFromURI(uri);
		assertEquals(name, r.getName());
		assertEquals(url, r.getUrl());
	}

}
