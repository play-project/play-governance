/**
 * 
 */
package org.ow2.play.governance.user;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.List;
import java.util.UUID;

import org.ow2.play.commons.security.Crypto;
import org.ow2.play.governance.user.api.UserException;
import org.ow2.play.governance.user.api.bean.Account;
import org.ow2.play.governance.user.api.bean.Resource;
import org.ow2.play.governance.user.api.bean.User;
import org.ow2.play.governance.user.utils.TokenHelper;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

/**
 * User management. Use mongo as backend.
 * 
 * @author chamerling
 * 
 */
public class UserService implements
		org.ow2.play.governance.user.api.UserService {

	static String LOGIN = "login";

	static String APITOKEN = "apitoken";

	private MongoTemplate mongoTemplate;

	/**
	 * Check if a user is available with the given name and is the password
	 * matches...
	 * 
	 * @return the authenticated user
	 * @throws exception
	 *             if the user credentials are bad
	 */
	@Override
	public User authenticate(String name, String password) throws UserException {
		org.ow2.play.governance.user.bean.User user = mongoTemplate.findOne(
				query(where(LOGIN).is(name)),
				org.ow2.play.governance.user.bean.User.class);
		if (user == null) {
			throw new UserException("User not found");
		}
		if (!user.password.equals(Crypto.passwordHash(password))) {
			throw new UserException("Wrong password");
		}
		return toAPI(user);
	}

	@Override
	public User register(User user) throws UserException {
		if (user == null || user.login == null) {
			throw new UserException("Missing user/login information");
		}

		user.password = Crypto.passwordHash(user.password);

		try {
			// create an unique API token...
			user.apiToken = TokenHelper.generate(UUID.randomUUID().toString()
					.getBytes("UTF-8"));
		} catch (Exception e) {
		}

		mongoTemplate.save(fromAPI(user));
		return user;
	}

	@Override
	public void delete(User user) throws UserException {
		if (user == null || user.login == null) {
			throw new UserException("Null user data...");
		}
		// this.authenticate(user.login, user.password);
		User u = getUser(user.login);
		if (u == null) {
			throw new UserException("User nto found");
		}

		mongoTemplate.remove(u);
	}

	@Override
	public User getUser(String login) throws UserException {
		org.ow2.play.governance.user.bean.User user = mongoTemplate.findOne(
				query(where(LOGIN).is(login)),
				org.ow2.play.governance.user.bean.User.class);
		if (user == null) {
			throw new UserException("User not found");
		}
		return toAPI(user);
	}

	@Override
	public User getUserFromID(String id) throws UserException {
		org.ow2.play.governance.user.bean.User user = mongoTemplate.findOne(
				query(where("_id").is(id)),
				org.ow2.play.governance.user.bean.User.class);
		if (user == null) {
			throw new UserException("User not found");
		}
		return toAPI(user);
	}

	@Override
	public User getUserFromToken(String token) throws UserException {
		org.ow2.play.governance.user.bean.User user = mongoTemplate.findOne(
				query(where(APITOKEN).is(token)),
				org.ow2.play.governance.user.bean.User.class);
		if (user == null) {
			throw new UserException("User not found");
		}
		return toAPI(user);
	}

	@Override
	public User getUserFromProvider(String provider, String login)
			throws UserException {

		Query query = query(where("accounts").elemMatch(
				where("login").is(login).and("provider").is(provider)));

		org.ow2.play.governance.user.bean.User user = mongoTemplate.findOne(
				query, org.ow2.play.governance.user.bean.User.class);

		if (user == null) {
			return null;
		}
		return toAPI(user);
	}

	/**
	 * FIXME : This method is buggy, it will delete resources if the incoming
	 * user is not well filled (not up to date...)
	 * 
	 * @deprecated use atomic methods...
	 */
	@Deprecated
	@Override
	public User update(User user) throws UserException {
		if (user == null || user.login == null)
			throw new UserException("null user data");

		// this.authenticate(user.login, user.password);

		Update update = new Update();
		// get the original data to check the diff...
		User original = getUser(user.login);

		if (user.email != null && !original.email.equalsIgnoreCase(user.email)) {
			update.set("email", user.email);
		}

		if (user.accounts != null) {
			// let's change all...
			// FIXME : Will be better to change only the diff...
			update.set("accounts", user.accounts);
		}

		if (user.groups != null) {
			update.set("groups", user.groups);
		}

		if (user.resources != null) {
			update.set("resources", user.resources);
		}

		return toAPI(mongoTemplate.findAndModify(
				new Query(where(LOGIN).is(user.login)), update,
				new FindAndModifyOptions().returnNew(true),
				org.ow2.play.governance.user.bean.User.class));
	}

	@Override
	public User addResource(String id, String resourceURI, String resourceName)
			throws UserException {
		User user = getUserFromID(id);

		Resource r = new Resource();
		r.date = "" + System.currentTimeMillis();
		r.uri = resourceURI;
		r.name = resourceName;
		
		Update update = new Update();
		update.push("resources", r);

		return toAPI(mongoTemplate.findAndModify(
				new Query(where(LOGIN).is(user.login)), update,
				new FindAndModifyOptions().returnNew(true),
				org.ow2.play.governance.user.bean.User.class));
	}

	@Override
	public User removeResource(String id, final String resourceURI,
			final String resourceName)
			throws UserException {
		User user = getUserFromID(id);

		List<Resource> resources = Lists.newArrayList(Collections2.filter(
				user.resources,
				new Predicate<Resource>() {
					public boolean apply(Resource input) {
						return !(input.name.equals(resourceName) && input.uri
								.equals(resourceURI));
					};
				}));

		Update update = new Update();
		update.set("resources", resources);
		
		return toAPI(mongoTemplate.findAndModify(
				new Query(where(LOGIN).is(user.login)), update,
				new FindAndModifyOptions().returnNew(true),
				org.ow2.play.governance.user.bean.User.class));
	}

	@Override
	public User addAccount(String id, Account account) throws UserException {
		User user = getUserFromID(id);

		if (account == null || account.provider == null) {
			throw new UserException("Null account");
		}

		Update update = new Update();
		update.push("accounts", account);

		return toAPI(mongoTemplate.findAndModify(
				new Query(where(LOGIN).is(user.login)), update,
				new FindAndModifyOptions().returnNew(true),
				org.ow2.play.governance.user.bean.User.class));
	}

	@Override
	public User removeAccount(String id, String provider)
			throws UserException {
		throw new UserException("Not implemented");
	}

	@Override
	public User addGroup(String id, String uri)
			throws UserException {
		User user = getUserFromID(id);
		if (uri == null || uri.trim().length() == 0) {
			throw new UserException("Bad group parameters");
		}
		
		Update update = new Update();
		Resource resource = new Resource();
		// FIXME : Constant
		resource.name = "group";
		resource.uri = uri;
		resource.date = "" + System.currentTimeMillis();

		update.push("groups", resource);

		return toAPI(mongoTemplate.findAndModify(
				new Query(where(LOGIN).is(user.login)), update,
				new FindAndModifyOptions().returnNew(true),
				org.ow2.play.governance.user.bean.User.class));
	}

	@Override
	public User removeGroup(String id, final String groupURI)
			throws UserException {
		User user = getUserFromID(id);

		// TODO : Constants
		final String groupName = "group";

		List<Resource> groups = Lists.newArrayList(Collections2.filter(
				user.groups, new Predicate<Resource>() {
					public boolean apply(Resource input) {
						return !(input.name.equals(groupName) && input.uri
								.equals(groupURI));
					};
				}));

		Update update = new Update();
		update.set("groups", groups);

		return toAPI(mongoTemplate.findAndModify(
				new Query(where(LOGIN).is(user.login)), update,
				new FindAndModifyOptions().returnNew(true),
				org.ow2.play.governance.user.bean.User.class));
	}

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	/**
	 * @param user
	 * @return
	 */
	private User toAPI(org.ow2.play.governance.user.bean.User user) {
		User result = new User();
		result.id = user.id.toStringMongod();
		result.apiToken = user.apitoken;
		result.accounts = user.accounts;
		result.email = user.email;
		result.fullName = user.fullName;
		result.groups = user.groups;
		result.login = user.login;
		result.password = user.password;
		result.avatarURL = user.avatarURL;
		result.resources = user.resources;
		return result;
	}

	private org.ow2.play.governance.user.bean.User fromAPI(User user) {
		org.ow2.play.governance.user.bean.User result = new org.ow2.play.governance.user.bean.User();
		result.accounts = user.accounts;
		result.apitoken = user.apiToken;
		result.email = user.email;
		result.fullName = user.fullName;
		result.groups = user.groups;
		result.login = user.login;
		result.password = user.password;
		result.avatarURL = user.avatarURL;
		result.resources = user.resources;
		return result;
	}
}
