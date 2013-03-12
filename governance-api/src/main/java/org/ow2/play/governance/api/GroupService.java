/**
 * 
 */
package org.ow2.play.governance.api;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.omg.CORBA.UserException;
import org.ow2.play.governance.api.bean.Group;

/**
 * Manage groups. Groups are part of the platform resources and are described
 * using metadata stuff.
 * 
 * @author chamerling
 * 
 */
@WebService
public interface GroupService {

	/**
	 * Create a group in the store
	 * 
	 * @param group
	 * @return the created group (with ID)
	 */
	@WebMethod
	Group create(Group group) throws GovernanceExeption;
	
	/**
	 * Get all the groups
	 * 
	 * @return
	 * @throws UserException
	 */
	@WebMethod
	List<Group> get() throws GovernanceExeption;
	
	/**
	 * Delete a group
	 * 
	 * @param group
	 * @throws UserException
	 */
	@WebMethod
	void delete(Group group) throws GovernanceExeption;

	/**
	 * Get a group from its unique ID
	 * 
	 * @param id
	 * @return
	 */
	@WebMethod
	Group getGroupFromID(String id) throws GovernanceExeption;

	/**
	 * Get group from its unique URI identifier
	 * 
	 * @param uri
	 * @return
	 * @throws GovernanceExeption
	 */
	@WebMethod
	Group getGroupFromURI(String uri) throws GovernanceExeption;

	/**
	 * Get group from its name (QName are better...)
	 * 
	 * @param name
	 * @return
	 */
	@WebMethod
	Group getGroupFromName(String name) throws GovernanceExeption;

}
