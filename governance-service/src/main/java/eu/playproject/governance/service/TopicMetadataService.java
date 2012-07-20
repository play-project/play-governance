/**
 * 
 */
package eu.playproject.governance.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import eu.playproject.governance.api.GovernanceExeption;
import eu.playproject.governance.api.bean.Metadata;
import eu.playproject.governance.api.bean.Topic;

/**
 * @author chamerling
 * 
 */
public class TopicMetadataService implements
		eu.playproject.governance.api.TopicMetadataService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.playproject.governance.api.TopicMetadataService#addMetadata(eu.playproject
	 * .governance.api.bean.Topic, eu.playproject.governance.api.bean.Metadata)
	 */
	@Override
	public void addMetadata(Topic topic, Metadata metadata)
			throws GovernanceExeption {
		if (topic == null || metadata == null) {
			throw new GovernanceExeption("Topic/metadata can not be null");
		}
		GovernanceEngine.getInstance().addMetadata(topic, metadata);
	}

	public void removeMetadata(Topic topic, Metadata metadata)
			throws GovernanceExeption {
		if (topic == null || metadata == null) {
			throw new GovernanceExeption("Topic/metadata can not be null");
		}
		GovernanceEngine.getInstance().removeMetadata(topic, metadata);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.playproject.governance.api.TopicMetadataService#getMetaData(eu.playproject
	 * .governance.api.bean.Topic)
	 */
	@Override
	public List<Metadata> getMetaData(Topic topic) throws GovernanceExeption {
		if (topic == null) {
			throw new GovernanceExeption("Topic can not be null");
		}
		return GovernanceEngine.getInstance().getMetaData(topic);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.playproject.governance.api.TopicMetadataService#getMetadata(eu.playproject
	 * .governance.api.bean.Topic, java.lang.String)
	 */
	@Override
	public Metadata getMetadataValue(Topic topic, String key)
			throws GovernanceExeption {
		if (topic == null || key == null) {
			throw new GovernanceExeption("Topic/key can not be null");
		}
		return GovernanceEngine.getInstance().getMetaData(topic, key);
	}

	@Override
	public boolean deleteMetaData(Topic topic) throws GovernanceExeption {
		if (topic == null) {
			throw new GovernanceExeption("Topic can not be null");
		}
		GovernanceEngine.getInstance().deleteMetadata(topic);
		return true;
	}

	@Override
	public List<Topic> getTopicsWithMeta(List<Metadata> include) throws GovernanceExeption {
		List<Topic> result = new ArrayList<Topic>();
		if (include == null) {
			return result;
		}

		List<Topic> topics = GovernanceEngine.getInstance().getTopics();

		Set<Topic> included = new HashSet<Topic>();
		if (include != null) {
			for (Topic topic : topics) {
				// for each current topic, loop and check if this needs to be
				// included

				List<Metadata> topicMetadata = getMetaData(topic);
				if (topicMetadata != null) {
					for (Metadata meta : include) {
						if (topicMetadata.contains(meta)) {
							included.add(topic);
						}
					}
				}
			}
		}
		
		result.addAll(included);
		return result;
	}

}
