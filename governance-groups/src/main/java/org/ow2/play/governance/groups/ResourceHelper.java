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
package org.ow2.play.governance.groups;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.NoSuchElementException;

import org.ow2.play.governance.api.bean.Group;
import org.ow2.play.governance.api.bean.Meta;
import org.ow2.play.metadata.api.Data;
import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.Metadata;
import org.ow2.play.metadata.api.Resource;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * @author chamerling
 * 
 */
public class ResourceHelper {

	private ResourceHelper() {
	}

	/**
	 * @param metaResource
	 * @return
	 */
	public static Group toGroup(MetaResource metaResource) {
		Group result = new Group();
		result.description = ResourceHelper.getDescription(metaResource);
		result.title = ResourceHelper.getTitle(metaResource);
		result.name = ResourceHelper.getGroupName(metaResource.getResource());
		result.resourceURI = metaResource.getResource().toString();

		for (Metadata metadata : metaResource.getMetadata()) {
			if (metadata.getData() != null && metadata.getData().size() > 0) {
				Meta meta = new Meta();
				meta.name = metadata.getName();
				meta.type = metadata.getData().get(0).getType();
				meta.value = metadata.getData().get(0).getValue();
				result.metadata.add(meta);
			}
		}
		return result;
	}

	public static MetaResource toMeta(Group group) {
		MetaResource result = new MetaResource();
		result.setResource(getGroupResource(group));
		if (group.description == null) {
			group.description = "";
		}
		Metadata description = new Metadata(Constants.DESCRIPTION, new Data(
				"literal", group.description));
		result.getMetadata().add(description);

		if (group.title != null) {
			Metadata title = new Metadata(Constants.TITLE, new Data("literal",
					group.title));
			result.getMetadata().add(title);
		}
		
		Metadata type = new Metadata(Constants.RDF_TYPE, new Data("uri",
				Constants.RDFS_USERGROUP));
		result.getMetadata().add(type);

		return result;
	}

	public static final Resource getGroupResource(Group group) {
		return new Resource(Constants.RESOURCE_NAME, Constants.PREFIX
				+ group.name);
	}

	public static final String getIcon(MetaResource metaResource) {
		return getValue(metaResource, Constants.ICON, "");
	}

	public static final String getTitle(MetaResource metaResource) {
		return getValue(metaResource, Constants.TITLE, "");
	}

	public static final String getDescription(MetaResource metaResource) {
		return getValue(metaResource, Constants.DESCRIPTION, "");
	}

	public static final String getValue(final MetaResource metaResource,
			final String key, final String def) {
		String result = null;
		try {
			Metadata md = Iterables.find(metaResource.getMetadata(),
					new Predicate<Metadata>() {
						public boolean apply(Metadata input) {
							return input.getName().equals(key);
						};
					});
			if (md.getData() != null && md.getData().size() > 0) {
				result = md.getData().get(0).getValue();
			} else {
				result = def;
			}
		} catch (NoSuchElementException e) {
			result = def;
		}
		return result;
	}

	public static final String getGroupName(Resource resource) {
		checkArgument(resource != null && goodFormat(resource.getUrl()));

		return resource.getUrl().substring(
				resource.getUrl().lastIndexOf('/') + 1);
	}

	public static final String getGroupNS(Resource resource) {
		checkArgument(resource != null && goodFormat(resource.getUrl()));

		return resource.getUrl().substring(0,
				resource.getUrl().lastIndexOf('/') + 1);
	}
	
	/**
	 * Get the resource from the URI
	 * 
	 * @param uri
	 * @return
	 */
	public static final Resource getResource(String uri) {
		checkArgument(uri != null);
		checkArgument(uri.endsWith("#" + Constants.RESOURCE_NAME));
		checkArgument(uri.startsWith(Constants.PREFIX));
		return new Resource(Constants.RESOURCE_NAME, uri.substring(0, uri.indexOf('#')));
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	public static final boolean goodFormat(String url) {
		return url != null && !url.endsWith("/") && url.contains("/");
	}

}
