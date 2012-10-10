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
package org.ow2.play.governance.service;

import junit.framework.TestCase;

import org.ow2.play.governance.Adapter;
import org.ow2.play.governance.api.bean.Topic;
import org.ow2.play.metadata.api.MetaResource;

/**
 * @author chamerling
 * 
 */
public class AdapterTest extends TestCase {

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test that transforming topic and metaresource is symetric using the
	 * adapter...
	 * 
	 * @throws Exception
	 */
	public void testToFrom() throws Exception {

		Topic t = new Topic();
		t.setName("TestTopic");
		t.setNs("http://play.eu/test");
		t.setPrefix("p");

		MetaResource mr = Adapter.transform(t);
		assertNotNull(mr);
		
		assertEquals("stream", mr.getResource().getName());
		assertEquals("http://play.eu/test/TestTopic", mr.getResource().getUrl());
		
		System.out.println(mr);
		
		Topic topic = Adapter.transform(mr);
		assertEquals(t, topic);

	}
	
	public void testGetTopicFromMetaJSON() {
		
	}
}
