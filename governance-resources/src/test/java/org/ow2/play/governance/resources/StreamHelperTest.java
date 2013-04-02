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
package org.ow2.play.governance.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.Resource;

/**
 * @author chamerling
 * 
 */
public class StreamHelperTest {

	@Test
	public void testIsStreamNull() {
		assertFalse(StreamHelper.isStream((String) null));
	}

	@Test
	public void testIsStreamKO() {
		assertFalse(StreamHelper.isStream("http://foo/bar#baz"));
	}

	@Test
	public void testIsStreamOK() {
		assertTrue(StreamHelper.isStream("http://foo/bar#stream"));
	}

	@Test
	public void testGetStreamURLNull() throws Exception {
		assertNull(StreamHelper.getStreamURL((MetaResource) null));
	}

	@Test
	public void testGetStreamURLNotStreamResource() throws Exception {
		MetaResource mr = new MetaResource(new Resource("foo", "http://bar"),
				null);
		assertNull(StreamHelper.getStreamURL(mr));
	}
	
	@Test
	public void testGetStreamURLIsStreamResource() throws Exception {
		MetaResource mr = new MetaResource(new Resource("stream", "http://bar"),
				null);
		assertEquals("http://bar#stream", StreamHelper.getStreamURL(mr));
	}

}
