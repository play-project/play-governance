/**
 * 
 */
package eu.playproject.governance.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import eu.playproject.governance.api.GovernanceExeption;
import eu.playproject.governance.api.bean.Metadata;
import eu.playproject.governance.api.bean.Topic;

/**
 * This is a fake governance engine while waiting for the real one
 * 
 * @author chamerling
 * 
 */
public class GovernanceEngine {

	static Logger logger = Logger.getLogger(GovernanceEngine.class.getName());

	// dummy implementation, to be added in a DB somewhere or in the cloud...
	Map<Topic, Set<Metadata>> metadata;

	private static GovernanceEngine instance;

	public synchronized static GovernanceEngine getInstance() {
		if (instance == null) {
			instance = new GovernanceEngine();
		}
		return instance;
	}

	/**
	 * 
	 */
	private GovernanceEngine() {
		this.metadata = new ConcurrentHashMap<Topic, Set<Metadata>>();
	}

	public synchronized void addMetadata(Topic topic, Metadata metadata) {
		if (!topicExists(topic)) {
			createTopic(topic);
		}
		this.metadata.get(topic).add(metadata);
	}

	public synchronized void removeMetadata(Topic topic, Metadata metadata) {
		if (!topicExists(topic)) {
			return;
		}
		this.metadata.get(topic).remove(metadata);
	}

	public List<Metadata> getMetaData(Topic topic) throws GovernanceExeption {
		List<Metadata> result = new ArrayList<Metadata>();
		Set<Metadata> set = this.metadata.get(topic);
		if (set != null) {
			result.addAll(set);
		}
		return result;
	}

	public Metadata getMetaData(Topic topic, String key)
			throws GovernanceExeption {
		List<Metadata> result = getMetaData(topic);
		Metadata m = null;
		if (result != null) {

		}
		Set<Metadata> set = this.metadata.get(topic);
		if (set != null) {
			Iterator<Metadata> iter = set.iterator();
			boolean found = false;
			while (!found && iter.hasNext()) {
				Metadata tmp = iter.next();
				if (key.equals(tmp.getName())) {
					found = true;
					m = tmp;
				}
			}
		}
		return m;
	}

	/**
	 * 
	 * @param topic
	 * @return
	 */
	public boolean topicExists(Topic topic) {
		return metadata.containsKey(topic);
	}

	/**
	 * 
	 * @param topic
	 */
	public synchronized void createTopic(Topic topic) {
		if (!topicExists(topic)) {
			metadata.put(topic, new HashSet<Metadata>());
		}
	}

	/**
	 * @return
	 */
	public List<Topic> getTopics() {
		List<Topic> result = new ArrayList<Topic>();
		result.addAll(metadata.keySet());
		return result;
	}

	/**
	 * 
	 * @param topic
	 */
	public void deleteMetadata(Topic topic) {
		if (topicExists(topic)) {
			this.metadata.get(topic).clear();
		}
	}
}
