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
package org.ow2.play.metadata.service.rest;

import java.util.List;

import javax.ws.rs.core.Response;

import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.MetadataException;
import org.ow2.play.metadata.api.service.rest.MetadataService;
import org.ow2.play.metadata.service.provider.MetadataProvider;

/**
 * Get Metadata as JSON/RDF. Almost a clone of {@link MetadataServiceImpl} but
 * it uses {@link MetadataProvider} to serialize data and does not care about
 * Arrays like in {@link MetadataServiceImpl}.
 * 
 * @author chamerling
 * 
 */
public class MetadataRDFServiceImpl implements MetadataService {

	private org.ow2.play.metadata.api.service.MetadataService metadataService;

	@Override
	public Response all() {
		List<MetaResource> result = null;
		try {
			result = metadataService.list();
		} catch (MetadataException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
		return Response.ok(result).build();
	}

	@Override
	public Response get(String id) {
		MetaResource result = null;
		try {
			result = metadataService.get(id);
		} catch (MetadataException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
		return Response.ok(result).build();
	}

	@Override
	public Response getResources(String name, String url) {
		List<MetaResource> result = null;
		try {
			result = metadataService.listWhere(name, url);
		} catch (MetadataException e) {
			return Response.serverError().build();
		}
		return Response.ok(result).build();
	}

	public void setMetadataService(
			org.ow2.play.metadata.api.service.MetadataService metadataService) {
		this.metadataService = metadataService;
	}

}
