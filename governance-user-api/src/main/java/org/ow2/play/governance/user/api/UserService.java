/**
 * 
 */
package org.ow2.play.governance.user.api;

import org.ow2.play.governance.user.api.bean.User;

/**
 * @author chamerling
 * 
 */
public interface UserService {

	/**
	 * Authenticate a user by checking name and password. External auth is not handled here.
	 * 
	 * @param name
	 * @param password
	 *            not hashed password
	 * @return
	 * @throws UserException
	 */
	User authenticate(String name, String password) throws UserException;
	
	/**
	 * Get a user from its name. One can get the user, do modifications and call
	 * {@link #update(User)}.
	 * 
	 * @param login
	 * @return
	 * @throws UserException
	 */
	User getUser(String login) throws UserException;
	
	/**
	 * Get a user from its main ID
	 * 
	 * @param id
	 * @return
	 * @throws UserException
	 */
	User getUserFromID(String id) throws UserException;

	
	/**
	 * Find a user from its provider login
	 * 
	 * @param provider the provider name 
	 * @param login the login of the user in the privder context
	 * @return the matching user or null if not found
	 * @throws UserException if something is bad
	 */
	User getUserFromProvider(String provider, String login) throws UserException;
	
	/**
	 * Get a user from the api token
	 * 
	 * @param token
	 * @return the user or null if not found
	 * @throws UserException
	 */
	User getUserFromToken(String token) throws UserException;

	/**
	 * Update a user (but not its name...). In order to update, name and
	 * password must be valid (ie {@link #authenticate(String, String)} is
	 * called). FIXME : Password can not be changed on this method since we need
	 * original password to validate password change. This will be added
	 * later...
	 * 
	 * @param user
	 * @return
	 * @throws UserException
	 */
	User update(User user) throws UserException;

	/**
	 * Register a new user
	 * 
	 * @param user
	 * @throws UserException
	 *             if something bad occurs
	 */
	User register(User user) throws UserException;

	/**
	 * Delete a user. In order to delete, name and password must be valid ie
	 * call to {@link #authenticate(String, String)} is valid.
	 * 
	 * @param user
	 * @throws UserException if the user to delete has not been found
	 */
	void delete(User user) throws UserException;
	
}
