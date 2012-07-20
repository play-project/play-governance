/**
 * 
 */
package eu.playproject.governance.api.bean;

import java.util.List;

/**
 * @author chamerling
 * 
 */
public class TopicMetadata {

	private String topic;

	private List<Metadata> metadata;

	public TopicMetadata() {
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topicName) {
		this.topic = topicName;
	}

	public List<Metadata> getMetadata() {
		return metadata;
	}

	public void setMetadata(List<Metadata> metadata) {
		this.metadata = metadata;
	}

}
