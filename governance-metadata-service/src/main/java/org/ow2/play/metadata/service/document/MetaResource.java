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
package org.ow2.play.metadata.service.document;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.ow2.play.metadata.api.Metadata;
import org.ow2.play.metadata.api.Resource;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author chamerling
 *
 */
@Document(collection = "metadata")
public class MetaResource {

	@Id
	public ObjectId id;
	
	public Resource resource;
	
	public List<Metadata> metadata = new ArrayList<Metadata>();
	
	public MetaResource() {
	}
}
