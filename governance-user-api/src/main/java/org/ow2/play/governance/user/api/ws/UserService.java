/**
 * 
 */
package org.ow2.play.governance.user.api.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.ow2.play.governance.user.api.UserException;
import org.ow2.play.governance.user.api.bean.User;

/**
 * Dummy Web service API...
 * 
 * @author chamerling
 *
 */
@WebService
public interface UserService {
	
	@WebMethod
	User getUser(String login) throws UserException;
	
	@WebMethod
	User getUserFromProvider(String provider, String login) throws UserException;

	@WebMethod
	User update(User user) throws UserException;

	@WebMethod
	User register(User user) throws UserException;

	@WebMethod
	void delete(User user) throws UserException;
}
