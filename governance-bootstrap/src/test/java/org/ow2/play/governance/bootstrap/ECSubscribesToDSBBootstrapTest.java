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
package org.ow2.play.governance.bootstrap;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.ow2.play.governance.bootstrap.ECSubscribesToDSBBootstrapServiceImpl;
import org.ow2.play.metadata.api.service.MetadataLoader;
import org.ow2.play.metadata.json.JSONMetadataLoaderImpl;
import org.ow2.play.metadata.service.BootstrapServiceImpl;
import org.ow2.play.metadata.service.InMemoryMetadataServiceImpl;

/**
 * @author chamerling
 * 
 */
public class ECSubscribesToDSBBootstrapTest extends TestCase {

	public void testNeedsToSubscribe() throws Exception {
		ECSubscribesToDSBBootstrapServiceImpl bootstrap = new ECSubscribesToDSBBootstrapServiceImpl();

		InMemoryMetadataServiceImpl metadataService = new InMemoryMetadataServiceImpl();
		BootstrapServiceImpl bootstrapServiceImpl = new BootstrapServiceImpl();
		MetadataLoader loader = new JSONMetadataLoaderImpl();

		bootstrapServiceImpl.setMetadataLoader(loader);
		bootstrapServiceImpl.setMetadataService(metadataService);
		List<String> urls = new ArrayList<String>();
		urls.add(DSBSubscribesToECBootstrapTest.class.getResource(
				"/metadata.rdf.json").toString());
		bootstrapServiceImpl.init(urls);
		bootstrap.setMetadataServiceClient(metadataService);

		assertTrue(metadataService.list().size() > 0);
		
		assertFalse(bootstrap
				.needsToSubscribe("http://streams.event-processing.org/ids/FacebookCepResults"));
		assertTrue(bootstrap
				.needsToSubscribe("http://streams.event-processing.org/ids/FacebookStatusFeed"));
	}

}
