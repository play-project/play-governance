/**
 * 
 */
package org.ow2.play.governance.user.api;

import java.util.List;

/**
 * Manage groups. Groups are part of the platform resources and are described
 * using metadata stuff.
 * 
 * @author chamerling
 * 
 */
public interface GroupService {

	List<String> getGroupForStream(String stream);
	
}
