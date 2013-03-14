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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.permission.api.Permission;
import org.ow2.play.metadata.api.MetaResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author chamerling
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class PermissionServiceTest {

	@Autowired
	PermissionService permissionService;

	@Autowired
	MongoTemplate mongoTemplate;

	/**
	 * Clear the collection for each test
	 */
	@After
	public void tearDown() {
		System.out.println("Dropping collection");
		mongoTemplate.dropCollection("metadata");
	}

	@Before
	public void setup() {
		System.out.println("Dropping collection");
		mongoTemplate.dropCollection("metadata");
	}

	@Test
	public void testAddGet() {
		Permission permission = new Permission();
		permission.accessTo.add("accessto");
		permission.agent.add("agent");
		permission.mode.add("mode");
		permission.name = "MyPermissionTest";

		try {
			permissionService.addPermission(permission);

			assertTrue(permissionService.getPermissions().size() == 1);

		} catch (GovernanceExeption e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetModeExists() throws Exception {
		Permission p1 = new Permission();
		p1.accessTo.add("accessto");
		p1.agent.add("agent");
		p1.mode.add("MyMode");
		p1.name = "MyPermissionTest1";

		Permission p2 = new Permission();
		p2.accessTo.add("accessto");
		p2.agent.add("agent");
		p2.mode.add("othermode");
		p2.name = "MyPermissionTest2";

		try {
			permissionService.addPermission(p1);
			permissionService.addPermission(p2);

			List<MetaResource> permissions = permissionService
					.getWhereMode("MyMode");

			assertTrue(permissions.size() == 1);

			permissions = permissionService
					.getWhereMode("othermode");

			assertTrue(permissions.size() == 1);

		} catch (GovernanceExeption e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetModeNotExists() throws Exception {
		Permission p1 = new Permission();
		p1.accessTo.add("accessto");
		p1.agent.add("agent");
		p1.mode.add("MyMode");
		p1.name = "MyPermissionTest1";

		Permission p2 = new Permission();
		p2.accessTo.add("accessto");
		p2.agent.add("agent");
		p2.mode.add("othermode");
		p2.name = "MyPermissionTest2";

		try {
			permissionService.addPermission(p1);
			permissionService.addPermission(p2);

			List<MetaResource> permissions = permissionService
					.getWhereMode("MyUnknownMode");

			assertTrue(permissions.size() == 0);

		} catch (GovernanceExeption e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
