/**
 * 
 */
package org.ow2.play.governance.user;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.UUID;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ow2.play.governance.user.api.UserException;
import org.ow2.play.governance.user.api.bean.Account;
import org.ow2.play.governance.user.api.bean.Resource;
import org.ow2.play.governance.user.api.bean.User;
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
public class UserServiceTest {

	@Autowired
	UserService userService;

	@Autowired
	MongoTemplate mongoTemplate;

	/**
	 * Clear the collection for each test
	 */
	@After
	public void tearDown() {
		mongoTemplate.dropCollection("user");
	}

	@Test
	public void testCreateNull() {
		User user = null;
		try {
			userService.register(user);
			fail();
		} catch (UserException e) {
		}
	}

	@Test
	public void testCreate() {
		User user = new User();
		user.login = "cha";
		user.password = "123";

		try {
			userService.register(user);
		} catch (UserException e) {
			fail();
		} finally {
			// clear TODO
		}
	}

	@Test
	public void testAuthOK() {
		User user = new User();
		user.login = UUID.randomUUID().toString();
		user.password = "123";

		try {
			userService.register(user);
		} catch (UserException e) {
			fail();
		}

		try {
			userService.authenticate(user.login, "123");
		} catch (UserException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testAuthBadPassword() {
		User user = new User();
		user.login = UUID.randomUUID().toString();
		user.password = "123";

		try {
			userService.register(user);
		} catch (UserException e) {
			fail();
		}

		try {
			userService.authenticate(user.login, "234");
			fail();
		} catch (UserException e) {
		}
	}

	@Test
	public void testAuthBadUser() {
		User user = new User();
		user.login = UUID.randomUUID().toString();
		user.password = "123";

		try {
			userService.register(user);
		} catch (UserException e) {
			fail();
		}

		try {
			userService.authenticate("christophe", "123");
			fail();
		} catch (UserException e) {
		}
	}

	@Test
	public void testAuthBadUserPassword() {
		User user = new User();
		user.login = UUID.randomUUID().toString();
		user.password = "123";

		try {
			userService.register(user);
		} catch (UserException e) {
			fail();
		}

		try {
			userService.authenticate("christophe", "12334");
			fail();
		} catch (UserException e) {
		}
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCreateWithAccount() throws Exception {
		User user = new User();
		user.login = "cha-" + System.currentTimeMillis();
		user.password = "123";
		Account a = new Account();
		a.provider = "twitter";
		a.secret = UUID.randomUUID().toString();
		a.token = UUID.randomUUID().toString();
		user.accounts.add(a);

		try {
			User u = userService.register(user);

			u = userService.getUser(user.login);
			assertNotNull(u);
			assertNotNull(u.accounts);
			assertTrue(u.accounts.size() == 1);
			assertTrue(u.accounts.get(0).equals(a));

		} catch (UserException e) {
			fail();
		} finally {
			// clear TODO
		}
	}

	@Test
	public void testAddAcount() throws Exception {
		User user = new User();
		String password = "123";
		user.login = "cha-" + System.currentTimeMillis();
		user.password = password;
		Account a = new Account();
		a.provider = "twitter";
		a.secret = UUID.randomUUID().toString();
		a.token = UUID.randomUUID().toString();
		user.accounts.add(a);

		try {
			user = userService.register(user);
		} catch (UserException e) {
			fail();
		} finally {
			// clear TODO
		}

		// get the user and add account
		User u = userService.getUser(user.login);
		assertNotNull(u);
		assertTrue(u.accounts != null);
		assertTrue(u.accounts.size() == 1);

		// add a new account
		Account b = new Account();
		b.provider = "facebook";
		b.secret = UUID.randomUUID().toString();
		b.token = UUID.randomUUID().toString();
		u.accounts.add(b);

		User uu = userService.addAccount(u.id, b);
		assertNotNull(uu);
		assertTrue(uu.accounts != null);
		assertTrue(uu.accounts.size() == 2);

		System.out.println(uu);
	}

	@Test
	public void testAddGroup() throws Exception {
		User user = new User();
		String password = "123";
		user.login = "cha-" + System.currentTimeMillis();
		user.password = password;
		user.groups.add(getGroup("http://foo/bar/a"));

		try {
			user = userService.register(user);
		} catch (UserException e) {
			fail();
		} finally {
			// clear TODO
		}
		
		// get the user and add account
		// not a test...
		User u = userService.getUser(user.login);
		assertNotNull(u);
		assertTrue(u.groups != null);
		assertTrue(u.groups.size() == 1);
		
		
		User uu = userService.addGroup(u.id, "http://foo/bar/b");
		
		assertNotNull(uu);
		assertTrue(uu.groups != null);
		assertTrue(uu.groups.size() == 2);
		
		System.out.println(uu);
	}
	
	@Test
	public void testQueryAccount() throws Exception {
		User user = new User();
		String password = "123";
		user.login = "cha-" + System.currentTimeMillis();
		user.password = password;
		Account a = new Account();
		a.provider = "twitter";
		a.secret = UUID.randomUUID().toString();
		a.token = UUID.randomUUID().toString();
		a.login = "chacha";
		user.accounts.add(a);

		User b = new User();
		b.login = "cha-" + System.currentTimeMillis();
		b.password = password;
		Account bb = new Account();
		bb.provider = "google";
		bb.secret = UUID.randomUUID().toString();
		bb.token = UUID.randomUUID().toString();
		bb.login = "chachacha";
		
		b.accounts.add(bb);
		
		try {
			userService.register(user);
			userService.register(b);

		} catch (UserException e) {
			fail();
		} finally {
			// clear TODO
		}
		
		User u = userService.getUserFromProvider("twitter", "chacha");
		assertNotNull(u);
		
		User uu = userService.getUserFromProvider("google", "chachacha");
		assertNotNull(uu);
		
		User uuu = userService.getUserFromProvider("twitter", "chachacha");
		assertNull(uuu);
		
	}
	
	@Test
	public void testAddResource() throws Exception {
		User user = new User();
		String password = "123";
		user.login = "cha-" + System.currentTimeMillis();
		user.password = password;
		user.groups.add(getGroup("http://group/a"));
		
		String resource = "http://myresource";
		String resourceName = "test";

		try {
			userService.register(user);
		} catch (UserException e) {
			fail();
		} finally {
			// clear TODO
		}
		
		user = userService.getUser(user.login);
		System.out.println(user.id);
		try {
			userService.addResource(user.id, resource, resourceName);
		} catch (UserException e) {
			e.printStackTrace();
			fail();
		}
		
		// get the user and add account
		User u = userService.getUser(user.login);
		assertNotNull(u);
		assertTrue(u.resources != null);
		assertTrue(u.resources.size() == 1);
		
		assertTrue(u.resources.get(0).uri.equals(resource));
		assertTrue(u.resources.get(0).name.equals(resourceName));

	}
	
	@Test
	public void testRemoveResource() throws Exception {
		User user = new User();
		String password = "123";
		user.login = "cha-" + System.currentTimeMillis();
		user.password = password;
		user.groups.add(getGroup("http://foo/bar/group/a"));

		String resource1 = "http://myresource1";
		String resource2 = "http://myresource2";

		String resourcesName = "test";

		Resource r = new Resource();
		r.date = System.currentTimeMillis() + "";
		r.uri = resource1;
		r.name = resourcesName;
		
		Resource rr = new Resource();
		rr.date = System.currentTimeMillis() + "";
		rr.uri = resource2;
		rr.name = resourcesName;
		
		user.resources.add(r);
		user.resources.add(rr);

		try {
			userService.register(user);
		} catch (UserException e) {
			fail();
		} finally {
			// clear TODO
		}
		
		user = userService.getUser(user.login);
		System.out.println(user);

		try {
			userService.removeResource(user.id, resource1, resourcesName);
		} catch (UserException e) {
			fail();
		}
		
		// get the user and add account
		User u = userService.getUser(user.login);

		System.out.println(u);

		assertNotNull(u);
		assertTrue(u.resources != null);
		System.out.println(u.resources);
		assertTrue(u.resources.size() == 1);
		
		assertTrue(u.resources.get(0).uri.equals(resource2));
	}

	@Test
	public void testRemoveGroup() throws Exception {
		User user = new User();
		String password = "123";
		user.login = "cha-" + System.currentTimeMillis();
		user.password = password;
		user.groups.add(getGroup("http://foo/bar/group/a"));
		user.groups.add(getGroup("http://foo/bar/group/b"));

		String resource1 = "http://myresource1";
		String resource2 = "http://myresource2";

		String resourcesName = "test";

		Resource r = new Resource();
		r.date = System.currentTimeMillis() + "";
		r.uri = resource1;
		r.name = resourcesName;

		Resource rr = new Resource();
		rr.date = System.currentTimeMillis() + "";
		rr.uri = resource2;
		rr.name = resourcesName;

		user.resources.add(r);
		user.resources.add(rr);

		try {
			userService.register(user);
		} catch (UserException e) {
			fail();
		} finally {
			// clear TODO
		}

		user = userService.getUser(user.login);
		System.out.println(user);

		try {
			userService.removeGroup(user.id, "http://foo/bar/group/a");
		} catch (UserException e) {
			fail();
		}

		// get the user and add account
		User u = userService.getUser(user.login);

		System.out.println(u);

		assertNotNull(u);
		assertTrue(u.groups != null);
		System.out.println(u.groups);
		assertTrue(u.groups.size() == 1);

		assertTrue(u.groups.get(0).uri.equals("http://foo/bar/group/b"));
	}

	private Resource getGroup(String uri) {
		Resource resource = new Resource();
		resource.name = "group";
		resource.uri = uri;
		return resource;
	}
}
