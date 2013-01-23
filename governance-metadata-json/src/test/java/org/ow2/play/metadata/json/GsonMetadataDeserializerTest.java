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
package org.ow2.play.metadata.json;

import java.util.List;

import junit.framework.TestCase;

import org.ow2.play.metadata.api.Data;
import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.Metadata;

/**
 * @author chamerling
 * 
 */
public class GsonMetadataDeserializerTest extends TestCase {

	public void testDeserializeFromResource() throws Exception {
		GsonMetadataDeserializer deserializer = new GsonMetadataDeserializer();
		List<MetaResource> result = deserializer
				.read(GsonMetadataDeserializerTest.class
						.getResourceAsStream("/metadata-1resource.rdf.json"));

		assertEquals(1, result.size());

		MetaResource mr = result.get(0);
		System.out.println(mr);

		assertEquals("stream", mr.getResource().getName());
		assertEquals(
				"http://streams.event-processing.org/ids/FacebookStatusFeed",
				mr.getResource().getUrl());

		assertEquals(5, mr.getMetadata().size());

		List<Metadata> list = mr.getMetadata();

		Metadata meta = new Metadata("http://purl.org/dc/elements/1.1/title",
				new Data("literal", "Facebook Status Feed"));
		assertTrue(list.contains(meta));
		
		meta = new Metadata("http://purl.org/dc/elements/1.1/description",
				new Data("literal", "A stream of Facebook Wall updates."));
		assertTrue(list.contains(meta));

		meta = new Metadata("http://www.w3.org/2002/06/xhtml2/icon",
				new Data("uri", "http://s-static.ak.facebook.com/rsrc.php/yi/r/q9U99v3_saj.ico"));
		assertTrue(list.contains(meta));

		meta = new Metadata("http://www.play-project.eu/xml/ns/topic",
				new Data("uri", "http://streams.event-processing.org/ids/FacebookStatusFeed"));
		assertTrue(list.contains(meta));

		meta = new Metadata("http://www.w3.org/1999/02/22-rdf-syntax-ns#type",
				new Data("uri", "http://events.event-processing.org/types/Stream"));
		assertTrue(list.contains(meta));

	}

	public void testDeserializeFromResource2() throws Exception {
		GsonMetadataDeserializer deserializer = new GsonMetadataDeserializer();
		List<MetaResource> result = deserializer
				.read(GsonMetadataDeserializerTest.class
						.getResourceAsStream("/metadata-2resources.rdf.json"));

		assertEquals(2, result.size());
	}

}
