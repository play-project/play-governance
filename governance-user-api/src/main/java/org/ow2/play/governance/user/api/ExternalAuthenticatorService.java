/**
 * 
 */
package org.ow2.play.governance.user.api;

/**
 * External Authenticator provides way to authenticate using external OAuth
 * providers such as twitter, facebook, ...
 * 
 * @author chamerling
 * 
 */
public interface ExternalAuthenticatorService {

	/**
	 * Authenticate using the external authentication system. Up to the
	 * implementation to retrieve user tokens using the
	 * {@link UserService#getUser(String)}. Once correctly authenticated using
	 * the provider, fetch user data and return it.
	 * 
	 * @param name
	 * @return
	 * @throws UserException
	 */
	User authenticate(String name) throws UserException;

}
