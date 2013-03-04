/**
 * 
 */
package org.ow2.play.governance.user.api;

import org.ow2.play.governance.user.api.bean.Group;

/**
 * Manage groups. Groups are part of the platform resources and are described
 * using metadata stuff.
 * 
 * @author chamerling
 * 
 */
public interface GroupService {

	/**
	 * Create a group in the store
	 * 
	 * @param group
	 * @return the created group (with ID)
	 */
	Group create(Group group) throws UserException;

	/**
	 * Get a group from its unique ID
	 * 
	 * @param id
	 * @return
	 */
	Group getGroupFromID(String id) throws UserException;

	/**
	 * Get group from its name (QName are better...)
	 * 
	 * @param name
	 * @return
	 */
	Group getGroupFromName(String name) throws UserException;

}
