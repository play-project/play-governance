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

	// let's do it with json, to be moved elsewhere since we use injection...
	private String config = "https://raw.github.com/play-project/play-configfiles/master/platformservices/governancemetadata/metadata.json";

	boolean started = false;

	private TopicMetadataLoader loader;

	private TopicMetadataService topicMetadataService;

	private EventGovernance eventGovernance;

	@Override
	public void start() throws GovernanceExeption {
		if (started) {
			throw new GovernanceExeption(
					"Already started, please stop before...");
		}

		logger.fine("Starting...");

		URL url;
		try {
			url = new URL(config);
		} catch (MalformedURLException e) {
			logger.warning("Can not build the URL");
			throw new GovernanceExeption("Can not build the URL from " + config);
		}

		Collection<TopicMetadata> list = loader.load(url);

		logger.fine("Number of meta entries " + list.size());

		for (TopicMetadata topicMetadata : list) {
			// override in all the cases...
			logger.info("Processing Metadata for topic " + topicMetadata.getTopic());
			Topic topic = getTopicFromName(topicMetadata.getTopic());
			if (topic != null) {
				// let's add all the metadata
				for (Metadata meta : topicMetadata.getMetadata()) {
					logger.info("Metadata : " + meta.getName() + ":"
							+ meta.getValue());
					topicMetadataService.addMetadata(topic, meta);
				}
			} else {
				logger.info("Can not find the topic " + topicMetadata.getTopic());
			}
		}
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

	protected Topic getTopicFromName(String name) {
		logger.fine("Getting topic " + name);

		List<Topic> topics = eventGovernance.getTopics();

		boolean found = false;
		Topic topic = null;

		Iterator<Topic> it = topics.iterator();
		while (it.hasNext() && !found) {
			topic = it.next();
			found = (topic != null && name.equals(topic.getName()));
		}

		logger.fine("Topic found : " + topic);
		return topic;
	}

	// injected

	public void setLoader(TopicMetadataLoader loader) {
		this.loader = loader;
	}

	public void setTopicMetadataService(
			TopicMetadataService topicMetadataService) {
		this.topicMetadataService = topicMetadataService;
	}

	public void setEventGovernance(EventGovernance eventGovernance) {
		this.eventGovernance = eventGovernance;
	}
}
