/**
 * 
 */
package org.ow2.play.governance.user.repositories;

import java.util.List;

import org.bson.types.ObjectId;
import org.ow2.play.governance.user.User;
import org.springframework.data.repository.CrudRepository;

/**
 * @author chamerling
 * 
 */
public interface UserRepository extends CrudRepository<User, ObjectId> {

	/**
	 * Get all the users from the given group
	 * 
	 * @param name
	 * @return
	 */
	List<User> findAllFromGroup(String name);
	
	/**
	 * Find a user
	 * 
	 * @param name
	 * @return
	 */
	List<User> findByName(String name);
}
