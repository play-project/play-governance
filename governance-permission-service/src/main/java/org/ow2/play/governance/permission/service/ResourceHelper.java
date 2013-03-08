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
package org.ow2.play.governance.permission.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.ow2.play.governance.permission.api.Constants;
import org.ow2.play.governance.permission.api.Permission;
import org.ow2.play.metadata.api.Data;
import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.Metadata;
import org.ow2.play.metadata.api.Resource;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

/**
 * @author chamerling
 * 
 */
public class ResourceHelper {

	public static final MetaResource toMeta(Permission permission) {
		Resource resource = new Resource(Constants.PERMISSION_RESOURCE,
				Constants.RESOURCE_NS + permission.name);
		List<Metadata> metas = new ArrayList<Metadata>();
		if (permission.accessTo != null) {
			metas.add(toMetaAccessTo(permission.accessTo));
		}
		if (permission.agent != null) {
			metas.add(toMetaAgent(permission.agent));
		}
		if (permission.mode != null) {
			metas.add(toMetaMode(permission.mode));
		}
		metas.add(getType());

		return new MetaResource(resource, metas);
	}

	public static final Metadata toMetaAccessTo(List<String> acls) {
		Collection<Data> datas = Collections2.transform(acls,
				new Function<String, Data>() {
					public Data apply(String input) {
						return new Data("uri", input);
					}
				});
		return new Metadata(Constants.ACCESS_TO, Lists.newArrayList(datas));
	}

	public static final Metadata toMetaAgent(List<String> acls) {
		Collection<Data> datas = Collections2.transform(acls,
				new Function<String, Data>() {
					public Data apply(String input) {
						return new Data("uri", input);
					}
				});
		return new Metadata(Constants.AGENT, Lists.newArrayList(datas));
	}

	public static final Metadata toMetaMode(List<String> acls) {
		Collection<Data> datas = Collections2.transform(acls,
				new Function<String, Data>() {
					public Data apply(String input) {
						return new Data("uri", input);
					}
				});
		return new Metadata(Constants.MODE, Lists.newArrayList(datas));
	}

	public static final Metadata getType() {
		return new Metadata(Constants.TYPE_KEY, new Data("uri",
				Constants.TYPE_VALUE));
	}
}
