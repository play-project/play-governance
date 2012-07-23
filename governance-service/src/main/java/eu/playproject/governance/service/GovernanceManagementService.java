/**
 * 
 */
package eu.playproject.governance.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import eu.playproject.governance.api.EventGovernance;
import eu.playproject.governance.api.GovernanceExeption;
import eu.playproject.governance.api.GovernanceManagement;
import eu.playproject.governance.api.TopicMetadataLoader;
import eu.playproject.governance.api.TopicMetadataService;
import eu.playproject.governance.api.bean.Metadata;
import eu.playproject.governance.api.bean.Topic;
import eu.playproject.governance.api.bean.TopicMetadata;

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
