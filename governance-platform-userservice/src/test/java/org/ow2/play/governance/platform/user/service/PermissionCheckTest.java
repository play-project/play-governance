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
package org.ow2.play.governance.platform.user.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Set;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.permission.api.Permission;
import org.ow2.play.governance.permission.api.PermissionChecker;
import org.ow2.play.governance.permission.service.PermissionService;
import org.ow2.play.governance.resources.ModeHelper;
import org.ow2.play.governance.user.UserService;
import org.ow2.play.governance.user.api.UserException;
import org.ow2.play.governance.user.api.bean.Resource;
import org.ow2.play.governance.user.api.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;

/**
 * @author chamerling
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class PermissionCheckTest {

	@Autowired
	PermissionChecker check;

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	PermissionService permissionService;
	
	@Autowired
	UserService userService;
	
	/**
	 * Clear the collection for each test
	 */
	@After
	public void tearDown() {
		System.out.println("Dropping collection");
		mongoTemplate.dropCollection("metadata");
		mongoTemplate.dropCollection("user");
	}

	@Before
	public void setup() {
		System.out.println("Dropping collection");
		mongoTemplate.dropCollection("metadata");
		mongoTemplate.dropCollection("user");
	}

	@Test
	public void getGroupForResource() {

		String resource = "MyResource";
		String group = "http://group.foo.com/FooBar#group";

		// create permissions
		Permission p = new Permission();
		p.name = "foo";
		p.accessTo.add(resource);
		p.agent.add(group);
		try {
			permissionService.addPermission(p);
		} catch (GovernanceExeption e) {
			e.printStackTrace();
			fail();
		}

		Set<String> groups = check.getGroupsForResource(resource);
		System.out.println(groups);

		assertEquals(1, groups.size());
		assertEquals(group, Lists.newArrayList(groups).get(0));
	}

	@Test
	public void getSingleGroupForResourceFromNPermissions() {

		String resource = "MyResource";
		String group = "http://group.foo.com/FooBar#group";

		// create permissions
		Permission p = new Permission();
		p.accessTo.add(resource);
		p.agent.add(group);

		int size = 100;
		try {
			for (int i = 0; i < size; i++) {
				p.name = "p" + i;
				permissionService.addPermission(p);
			}
		} catch (GovernanceExeption e) {
			e.printStackTrace();
			fail();
		}

		Set<String> groups = check.getGroupsForResource(resource);
		System.out.println(groups);

		assertEquals(1, groups.size());
		assertEquals(group, Lists.newArrayList(groups).get(0));
	}
	
	@Test
	public void getMultipleGroupsForResourceFromNPermissions() {

		String resource = "MyResource";
		String group = "http://group.foo.com/FooBar%s#group";

		// create permissions

		int size = 100;
		try {
			for (int i = 0; i < size; i++) {
				Permission p = new Permission();
				p.accessTo.add(resource);
				p.agent.add(String.format(group, ""+i));
				p.name = "p" + i;
				permissionService.addPermission(p);
			}
		} catch (GovernanceExeption e) {
			e.printStackTrace();
			fail();
		}

		Set<String> groups = check.getGroupsForResource(resource);
		System.out.println(groups);

		assertEquals(size, groups.size());
	}
	
	@Test
	public void testUserGroupOK() {
		String group = "http://foo/bar/MyGroup";
		String group2 = "http://foo/bar/MyGroup2";
		String resource = "MyResource";
		
		User user = new User();
		user.login = "chamerling";
		user.password = "foo";
		user.groups.add(getGroup(group));
		try {
			userService.register(user);
		} catch (UserException e) {
			e.printStackTrace();
			fail();
		}
		
		Permission p = new Permission();
		p.name = "foo";
		p.accessTo.add(resource);
		p.agent.add(group + "#group");
		try {
			permissionService.addPermission(p);
		} catch (GovernanceExeption e) {
			e.printStackTrace();
			fail();
		}
		
		// just created it so it should be here...
		assertTrue(check.checkGroup("chamerling", group + "#group"));

		// not in
		assertFalse(check.checkGroup("chamerling", group2 + "#group"));

		// wrong user
		assertFalse(check.checkGroup("nop", group));
				
	}
	
	@Test
	public void checkResourceEmpty() {
		String user = "chamerling";
		String resource = "http://foo/bar/Resource#stream";
		
		assertFalse(check.checkResource(user, resource));
	}
	
	@Test
	public void checkResourceUserInGroup() {
		String login = "chamerling";
		String resource = "http://foo/bar/Resource#stream";
		String group = "http://foo/bar/Group";
		
		User user = new User();
		user.login = login;
		user.password = "foo";
		user.groups.add(getGroup(group));
		try {
			userService.register(user);
		} catch (UserException e) {
			e.printStackTrace();
			fail();
		}
		
		Permission p = new Permission();
		p.name = "foo";
		p.accessTo.add(resource);
		p.agent.add(group + "#group");
		try {
			permissionService.addPermission(p);
		} catch (GovernanceExeption e) {
			e.printStackTrace();
			fail();
		}
		
		assertTrue(check.checkResource(login, resource));
	}
	
	@Test
	public void checkResourceUserNotInGroup() {
		String login = "chamerling";
		String resource = "http://foo/bar/Resource#stream";
		String group = "http://foo/bar/Group#group";
		String group2 = "http://foo/bar/Group2";
		
		User user = new User();
		user.login = login;
		user.password = "foo";
		user.groups.add(getGroup(group2));
		try {
			userService.register(user);
		} catch (UserException e) {
			e.printStackTrace();
			fail();
		}
		
		Permission p = new Permission();
		p.name = "foo";
		p.accessTo.add(resource);
		p.agent.add(group);
		try {
			permissionService.addPermission(p);
		} catch (GovernanceExeption e) {
			e.printStackTrace();
			fail();
		}
		
		
		assertFalse(check.checkResource(login, resource));
	}

    @Test
	public void checkUnknownResource() {
		String login = "chamerling";
		String resource = "http://foo/bar/Resource#stream";
		String resource2 = "http://foo/bar/Resource2#stream";
		String group = "http://foo/bar/Group";
		
		User user = new User();
		user.login = login;
		user.password = "foo";
		user.groups.add(getGroup(group));
		try {
			userService.register(user);
		} catch (UserException e) {
			e.printStackTrace();
			fail();
		}
		
		Permission p = new Permission();
		p.name = "foo";
		p.accessTo.add(resource);
		p.agent.add(group + "#group");
		try {
			permissionService.addPermission(p);
		} catch (GovernanceExeption e) {
			e.printStackTrace();
			fail();
		}
		assertFalse(check.checkResource(login, resource2));
	}

    /**
     * Test the the user can access to a resource which is in right mode and where the user belongs to the group
     *
     */
    @Test
    public void getGroupsForResourceInMode() {
        String login = "chamerling";
        String resource = "http://foo/bar/Resource#stream";
        String groupIn = "http://foo/bar/GroupIn";
        String groupNotIn = "http://foo/bar/GroupNotIn";


        User user = new User();
        user.login = login;
        user.password = "foo";
        user.groups.add(getGroup(groupIn));
        try {
            userService.register(user);
        } catch (UserException e) {
            e.printStackTrace();
            fail();
        }

        Permission p = new Permission();
        p.name = "foo";
        p.accessTo.add(resource);
        p.agent.add(groupIn + "#group");
        p.mode.add(ModeHelper.getFullURI("read"));
        try {
            permissionService.addPermission(p);
        } catch (GovernanceExeption e) {
            e.printStackTrace();
            fail();
        }

        assertTrue(true);
        // TODO
        //assertTrue(check.checkMode(login, resource, "read"));

    }

    @Test
    public void getGroupsForResourceInModeCheckModeDiff() {
        String resource = "http://foo/bar/ResourceA#stream";
        String groupIn = "http://foo/bar/GroupIn";
        Permission p = new Permission();
        p.name = "foo";
        p.accessTo.add(resource);
        p.agent.add(groupIn + "#group");
        p.mode.add(ModeHelper.getFullURI("read"));
        try {
            permissionService.addPermission(p);
        } catch (GovernanceExeption e) {
            e.printStackTrace();
            fail();
        }

        // check that we can find what we inserted
        Set<String> groups = check.getGroupsForResourceInMode(resource, ModeHelper.getFullURI("read"));
        assertEquals(1, groups.size());
        System.out.println(groups);

        // This one is not available
        groups = check.getGroupsForResourceInMode(resource, ModeHelper.getFullURI("write"));
        assertEquals(0, groups.size());

        // check an unknown resource
        groups = check.getGroupsForResourceInMode("http://foo/bar/ResourceB#stream", ModeHelper.getFullURI("read"));
        assertEquals(0, groups.size());

        // create a new permission for the stream in another group with another mode...
        String groupIn2 = "http://foo/bar/GroupIn2";
        p = new Permission();
        p.name = "bar";
        p.accessTo.add(resource);
        p.agent.add(groupIn2 + "#group");
        p.mode.add(ModeHelper.getFullURI("write"));
        try {
            permissionService.addPermission(p);
        } catch (GovernanceExeption e) {
            e.printStackTrace();
            fail();
        }

        // check that we still have one group for read
        groups = check.getGroupsForResourceInMode(resource, ModeHelper.getFullURI("read"));
        assertEquals(1, groups.size());
        String group = Iterables.getFirst(groups, "");
        assertEquals(groupIn + "#group", group);
        System.out.println(groups);

        // and check that we can find the newly inserted value
        groups = check.getGroupsForResourceInMode(resource, ModeHelper.getFullURI("write"));
        assertEquals(1, groups.size());
        group = Iterables.getFirst(groups, "");
        assertEquals(groupIn2 + "#group", group);
        System.out.println(groups);
    }

    @Test
    public void getGroupsForResourceInModeCheckAfterMultipleInsert() {
        String resource = "http://foo/bar/ResourceA#stream";
        String groupIn = "http://foo/bar/GroupIn";
        Permission p = new Permission();
        p.name = "foo";
        p.accessTo.add(resource);
        p.agent.add(groupIn + "#group");
        p.mode.add(ModeHelper.getFullURI("read"));
        try {
            permissionService.addPermission(p);
        } catch (GovernanceExeption e) {
            e.printStackTrace();
            fail();
        }

        // create a new permission for the stream in another group with another mode...
        String groupIn2 = "http://foo/bar/GroupIn2";
        p = new Permission();
        p.name = "bar";
        p.accessTo.add(resource);
        p.agent.add(groupIn2 + "#group");
        p.mode.add(ModeHelper.getFullURI("write"));
        try {
            permissionService.addPermission(p);
        } catch (GovernanceExeption e) {
            e.printStackTrace();
            fail();
        }

        // check that we have one group for read
        Set<String> groups = check.getGroupsForResourceInMode(resource, ModeHelper.getFullURI("read"));
        assertEquals(1, groups.size());
        String group = Iterables.getFirst(groups, "");
        assertEquals(groupIn + "#group", group);
        System.out.println(groups);

        // and check that we can find the newly inserted value
        groups = check.getGroupsForResourceInMode(resource, ModeHelper.getFullURI("write"));
        assertEquals(1, groups.size());
        group = Iterables.getFirst(groups, "");
        assertEquals(groupIn2 + "#group", group);
        System.out.println(groups);
    }

    /**
     * Register a permission for a resource in a mode and query another resource in another mode.
     * Hopefully, this should return nothing...
     */
    @Test
    public void getGroupsForResourceInModeCheckUnregisteredResource() {

        // register a resource, then query an unregistered one

        String resource = "http://foo/bar/ResourceA#stream";
        String groupIn = "http://foo/bar/GroupIn";
        Permission p = new Permission();
        p.name = "foo";
        p.accessTo.add(resource);
        p.agent.add(groupIn + "#group");
        p.mode.add(ModeHelper.getFullURI("read"));
        try {
            permissionService.addPermission(p);
        } catch (GovernanceExeption e) {
            e.printStackTrace();
            fail();
        }

        // check an unknown resource
        Set<String> groups = check.getGroupsForResourceInMode("http://foo/bar/ResourceB#stream", ModeHelper.getFullURI("read"));
        System.out.println(groups);
        assertEquals(0, groups.size());
    }

    /**
     * Register a resource with a mode and query it with another mode.
     * This should return nothing.
     */
    @Test
    public void getGroupsForResourceInModeCheckResourceWithInvalidMode() {

        // register a resource, then query an unregistered one

        String resource = "http://foo/bar/ResourceA#stream";
        String groupIn = "http://foo/bar/GroupIn";
        Permission p = new Permission();
        p.name = "foo";
        p.accessTo.add(resource);
        p.agent.add(groupIn + "#group");
        p.mode.add(ModeHelper.getFullURI("read"));
        try {
            permissionService.addPermission(p);
        } catch (GovernanceExeption e) {
            e.printStackTrace();
            fail();
        }

        // check resource with write instead of read
        Set<String> groups = check.getGroupsForResourceInMode("http://foo/bar/ResourceA#stream", ModeHelper.getFullURI("write"));
        System.out.println(groups);
        assertEquals(0, groups.size());
    }

    /**
     * Check that the registered user can access to a resource which is registered in its group
     */
    @Test
    public void checkModeInOneGroup() {
        String group = "http://foo/bar/CheckModeGroup";
        String resource = "http://foo/bar/ResourceCheckMode#stream";

        User user = new User();
        user.login = "chamerling";
        user.password = "foo";
        user.groups.add(getGroup(group));
        try {
            userService.register(user);
        } catch (UserException e) {
            e.printStackTrace();
            fail();
        }

        Permission p = new Permission();
        p.name = "foo";
        p.accessTo.add(resource);
        p.agent.add(group + "#group");
        p.mode.add(ModeHelper.getFullURI("read"));

        try {
            permissionService.addPermission(p);
        } catch (GovernanceExeption e) {
            e.printStackTrace();
            fail();
        }

        assertTrue(check.checkMode(user.login, resource, ModeHelper.getFullURI("read")));
    }

    @Test
    public void checkModeInNGroups() {
        String group = "http://foo/bar/CheckModeGroup";
        String group2 = "http://foo/bar/CheckModeGroup2";
        String resource = "http://foo/bar/ResourceCheckMode#stream";

        User user = new User();
        user.login = "chamerling";
        user.password = "foo";
        user.groups.add(getGroup(group));
        user.groups.add(getGroup(group2));
        try {
            userService.register(user);
        } catch (UserException e) {
            e.printStackTrace();
            fail();
        }

        Permission p = new Permission();
        p.name = "foo";
        p.accessTo.add(resource);
        p.agent.add(group + "#group");
        p.mode.add(ModeHelper.getFullURI("read"));

        try {
            permissionService.addPermission(p);
        } catch (GovernanceExeption e) {
            e.printStackTrace();
            fail();
        }

        assertTrue(check.checkMode(user.login, resource, ModeHelper.getFullURI("read")));
    }

    @Test
    public void checkModeInNoGroups() {
        String group = "http://foo/bar/CheckModeGroup";
        String group2 = "http://foo/bar/CheckModeGroup2";
        String resource = "http://foo/bar/ResourceCheckMode#stream";

        User user = new User();
        user.login = "chamerling";
        user.password = "foo";
        user.groups.add(getGroup(group));
        try {
            userService.register(user);
        } catch (UserException e) {
            e.printStackTrace();
            fail();
        }

        Permission p = new Permission();
        p.name = "foo";
        p.accessTo.add(resource);
        p.agent.add(group2 + "#group");
        p.mode.add(ModeHelper.getFullURI("read"));

        try {
            permissionService.addPermission(p);
        } catch (GovernanceExeption e) {
            e.printStackTrace();
            fail();
        }

        assertFalse(check.checkMode(user.login, resource, ModeHelper.getFullURI("read")));
    }

    @Test
    public void checkModeInvalidModeValue() {
        String group = "http://foo/bar/CheckModeGroup";
        String resource = "http://foo/bar/ResourceCheckMode#stream";

        User user = new User();
        user.login = "chamerling";
        user.password = "foo";
        user.groups.add(getGroup(group));
        try {
            userService.register(user);
        } catch (UserException e) {
            e.printStackTrace();
            fail();
        }

        Permission p = new Permission();
        p.name = "foo";
        p.accessTo.add(resource);
        p.agent.add(group + "#group");
        p.mode.add(ModeHelper.getFullURI("read"));

        try {
            permissionService.addPermission(p);
        } catch (GovernanceExeption e) {
            e.printStackTrace();
            fail();
        }
        assertFalse(check.checkMode(user.login, resource, ModeHelper.getFullURI("write")));
    }

	private Resource getGroup(String url) {
		Resource r = new Resource();
		r.uri = url;
		r.name = "group";
		r.date = "" + System.currentTimeMillis();
		return r;
	}

}
