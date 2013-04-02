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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.Resource;

/**
 * @author chamerling
 * 
 */
public class ResourceHelperTest {

	@Test
	public void testIsNullNull() {
		assertFalse(ResourceHelper.is(null, null));
	}

	@Test
	public void testIsNull() throws Exception {
		assertFalse(ResourceHelper.is(new MetaResource(new Resource("foo",
				"http://bar"), null), null));
	}

	@Test
	public void testIsKO() throws Exception {
		assertFalse(ResourceHelper.is(new MetaResource(new Resource("foo",
				"http://bar"), null), "notfoo"));
	}

	@Test
	public void testIsOK() throws Exception {
		assertTrue(ResourceHelper.is(new MetaResource(new Resource("stream",
				"http://bar"), null), "stream"));
	}
}
