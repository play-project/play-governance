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

import static org.junit.Assert.*;

import org.junit.Test;
import org.ow2.play.governance.api.bean.Topic;
import org.ow2.play.metadata.api.MetaResource;

/**
 * @author chamerling
 *
 */
public class TopicHelperTest {

	/**
	 * Test that transforming topic and metaresource is symetric using the
	 * adapter...
	 * 
	 * @throws Exception
	 */
	@Test
	public void testToFrom() throws Exception {

		Topic t = new Topic();
		t.setName("TestTopic");
		t.setNs("http://play.eu/test");
		t.setPrefix("p");

		MetaResource mr = TopicHelper.transform(t);
		assertNotNull(mr);
		
		assertEquals("stream", mr.getResource().getName());
		assertEquals("http://play.eu/test/TestTopic", mr.getResource().getUrl());
		
		System.out.println(mr);
		
		Topic topic = TopicHelper.transform(mr);
		assertEquals(t, topic);

	}
	
	@Test
	public void testGetTopicName() throws Exception {
		Topic topic = new Topic();
		topic.setName("Test");
		topic.setNs("http://play.eu");
		topic.setPrefix("p");

		MetaResource mr = TopicHelper.transform(topic);

		String name = TopicHelper.getTopicName(mr);
		assertEquals(topic.getName(), name);

		String prefix = TopicHelper.getTopicPrefix(mr);
		assertEquals(topic.getPrefix(), prefix);

		String ns = TopicHelper.getTopicNS(mr);
		assertEquals(topic.getNs(), ns);

	}

	@Test
	public void testGetTopicPrefix() throws Exception {
		Topic topic = new Topic();
		topic.setName("Test");
		topic.setNs("http://play.eu");
		topic.setPrefix("p");

		MetaResource mr = TopicHelper.transform(topic);

		String prefix = TopicHelper.getTopicPrefix(mr);
		assertEquals(topic.getPrefix(), prefix);
	}

	@Test
	public void testGetTopicNS() throws Exception {
		Topic topic = new Topic();
		topic.setName("Test");
		topic.setNs("http://play.eu");
		topic.setPrefix("p");

		MetaResource mr = TopicHelper.transform(topic);

		String ns = TopicHelper.getTopicNS(mr);
		assertEquals(topic.getNs(), ns);
	}
}
