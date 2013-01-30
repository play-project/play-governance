/**
 * 
 */
package org.ow2.play.governance.user.ws;

import javax.jws.WebMethod;

import org.ow2.play.governance.user.api.User;
import org.ow2.play.governance.user.api.UserException;
import org.ow2.play.governance.user.api.ws.UserService;

/**
 * @author chamerling
 * 
 */
public class UserServiceImpl implements UserService {

	private org.ow2.play.governance.user.api.UserService userService;

	@Override
	@WebMethod
	public User getUser(String login) throws UserException {
		return userService.getUser(login);
	}

	@Override
	@WebMethod
	public User getUserFromProvider(String provider, String login)
			throws UserException {
		return userService.getUserFromProvider(provider, login);
	}

	@Override
	@WebMethod
	public User update(User user) throws UserException {
		return userService.update(user);
	}

	@Override
	@WebMethod
	public User register(User user) throws UserException {
		return userService.register(user);
	}

	@Override
	@WebMethod
	public void delete(User user) throws UserException {
		userService.delete(user);
	}
	
	public void setUserService(
			org.ow2.play.governance.user.api.UserService userService) {
		this.userService = userService;
	}

}
