/**
 * 
 */
package org.ow2.play.governance.service;

import java.util.logging.Logger;

import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.GovernanceManagement;


/**
 * This is a dirty implementation which gets data from a remote properties file
 * and populate the metadata. It also gets all the current topics from the
 * topics registry (remote) and put metadata on the local registry.
 * 
 * @author chamerling
 * 
 */
public class GovernanceManagementService implements GovernanceManagement {

	static Logger logger = Logger.getLogger(GovernanceManagementService.class
			.getName());

	boolean started = false;

	@Override
	public void start() throws GovernanceExeption {
		if (started) {
			throw new GovernanceExeption(
					"Already started, please stop before...");
		}

		// does nothing
		started = true;
	}

	@Override
	public void stop() throws GovernanceExeption {
		if (!started) {
			throw new GovernanceExeption("Already stopped");
		}

		// let's clean all...

		started = false;
	}
}
