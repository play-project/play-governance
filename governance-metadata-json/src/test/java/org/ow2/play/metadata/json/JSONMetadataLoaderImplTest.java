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

import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.Metadata;

/**
 * @author chamerling
 * 
 */
public class JSONMetadataLoaderImplTest extends TestCase {

	public void testLoadList2ElementsFromLoadFromClasspath() throws Exception {
		JSONMetadataLoaderImpl loader = new JSONMetadataLoaderImpl();

		List<MetaResource> list = loader.load(JSONMetadataLoaderImplTest.class
				.getResource("/shortlistmetadata.rdf.json").toString());

		System.out.println(list);
		assertNotNull(list);
		assertEquals(2, list.size());
	}

	public void testLoadList1ElementFromLoadFromClasspath() throws Exception {
		JSONMetadataLoaderImpl loader = new JSONMetadataLoaderImpl();

		List<MetaResource> list = loader.load(JSONMetadataLoaderImplTest.class
				.getResource("/shortlistmetadata1.rdf.json").toString());

		System.out.println(list);
		assertNotNull(list);
		assertEquals(1, list.size());

		MetaResource mr = list.get(0);
		assertNotNull(mr);

		assertEquals(
				"http://streams.event-processing.org/ids/FacebookStatusFeed",
				mr.getResource().getUrl());
		assertEquals("stream", mr.getResource().getName());

		List<Metadata> metadata = mr.getMetadata();
		assertNotNull(metadata);
		assertEquals(2, metadata.size());

		// Dummy test, guava shoulf be better...
		for (Metadata metadata2 : metadata) {
			assertNotNull(metadata2.getName());
			assertNotNull(metadata2.getData());
			assertEquals(1, metadata2.getData().size());

			assertTrue(metadata2.getName().equals(
					"http://purl.org/dc/elements/1.1/title")
					|| metadata2.getName().equals(
							"http://purl.org/dc/elements/1.1/description"));
		}
	}
}
