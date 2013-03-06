/**
 * 
 */
package org.ow2.play.governance.user;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.ow2.play.commons.security.Crypto;
import org.ow2.play.governance.user.api.UserException;
import org.ow2.play.governance.user.api.bean.User;
import org.ow2.play.governance.user.utils.TokenHelper;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

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
			user.apiToken = TokenHelper.generate(UUID.randomUUID().toString().getBytes("UTF-8"));
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
		//this.authenticate(user.login, user.password);
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

		org.ow2.play.governance.user.bean.User user = mongoTemplate.findOne(query,
				org.ow2.play.governance.user.bean.User.class);

		if (user == null) {
			return null;
		}
		return toAPI(user);
	}

	@Override
	public User update(User user) throws UserException {
		if (user == null || user.login == null)
			throw new UserException("null user data");

		//this.authenticate(user.login, user.password);

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
		return result;
	}
}
