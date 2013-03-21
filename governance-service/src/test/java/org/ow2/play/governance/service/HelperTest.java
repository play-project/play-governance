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
import org.ow2.play.governance.Helper;
import org.ow2.play.governance.api.bean.Topic;
import org.ow2.play.metadata.api.MetaResource;

/**
 * @author chamerling
 * 
 */
public class HelperTest extends TestCase {

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

	public void testGetTopicName() throws Exception {
		Topic topic = new Topic();
		topic.setName("Test");
		topic.setNs("http://play.eu");
		topic.setPrefix("p");

		MetaResource mr = Adapter.transform(topic);

		String name = Helper.getTopicName(mr);
		assertEquals(topic.getName(), name);

		String prefix = Helper.getTopicPrefix(mr);
		assertEquals(topic.getPrefix(), prefix);

		String ns = Helper.getTopicNS(mr);
		assertEquals(topic.getNs(), ns);

	}

	public void testGetTopicPrefix() throws Exception {
		Topic topic = new Topic();
		topic.setName("Test");
		topic.setNs("http://play.eu");
		topic.setPrefix("p");

		MetaResource mr = Adapter.transform(topic);

		String prefix = Helper.getTopicPrefix(mr);
		assertEquals(topic.getPrefix(), prefix);
	}

	public void testGetTopicNS() throws Exception {
		Topic topic = new Topic();
		topic.setName("Test");
		topic.setNs("http://play.eu");
		topic.setPrefix("p");

		MetaResource mr = Adapter.transform(topic);

		String ns = Helper.getTopicNS(mr);
		assertEquals(topic.getNs(), ns);
	}
}
