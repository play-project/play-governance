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

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.ow2.play.metadata.api.Data;
import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.Metadata;
import org.ow2.play.metadata.api.Resource;

import com.google.common.collect.Lists;

/**
 * @author chamerling
 *
 */
public class ResourceHelperTest {

	@Test
	public void testName() {
		Resource resource = new Resource("group", "http://foo/bar/MyGroupName");
		String name = ResourceHelper.getGroupName(resource);
		assertEquals("MyGroupName", name);
		
		String ns = ResourceHelper.getGroupNS(resource);
		assertEquals("http://foo/bar/", ns);
	}
	
	@Test
	public void getDescription() {
		Resource resource = new Resource("group", "http://foo/bar/MyGroupName");
		List<Metadata> list = Lists.newArrayList();
		list.add(new Metadata(Constants.DESCRIPTION, new Data("literal", "The description")));
		MetaResource mr = new MetaResource(resource, list);
		
		assertEquals("The description", ResourceHelper.getDescription(mr));
	}
	
	@Test
	public void getTitle() {
		Resource resource = new Resource("group", "http://foo/bar/MyGroupName");
		List<Metadata> list = Lists.newArrayList();
		list.add(new Metadata(Constants.TITLE, new Data("literal", "The title")));
		MetaResource mr = new MetaResource(resource, list);
		
		assertEquals("The title", ResourceHelper.getTitle(mr));
	}
	
	@Test
	public void getResource() {
		String uri = Constants.PREFIX + "foo" + "#" + Constants.RESOURCE_NAME;
		System.out.println(uri);
		
		assertEquals(Constants.PREFIX + "foo", ResourceHelper.getResource(uri).getUrl());
		assertEquals(Constants.RESOURCE_NAME, ResourceHelper.getResource(uri).getName());
	}

}
