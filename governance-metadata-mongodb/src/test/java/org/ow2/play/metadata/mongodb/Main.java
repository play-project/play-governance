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
package org.ow2.play.metadata.mongodb;

import org.ow2.play.metadata.api.service.MetadataService;
import org.petalslink.dsb.commons.service.api.Service;
import org.petalslink.dsb.cxf.CXFHelper;

/**
 * @author chamerling
 * 
 */
public class Main {

	public static void main(String[] args) {

		
		MongoMetadataServiceImpl bean = new MongoMetadataServiceImpl();
		bean.setBsonAdapter(new BSONAdapterImpl());
		bean.init();
		
		Service service = CXFHelper
				.getServiceFromFinalURL(
						"http://localhost:8080/play-metadata-mongodb-war/MetadataService",
						MetadataService.class, bean);
		
		service.start();
		
		try {
			Thread.sleep(1000000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		

	}

}
