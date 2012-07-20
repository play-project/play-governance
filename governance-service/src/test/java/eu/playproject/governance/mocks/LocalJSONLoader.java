/**
 * 
 */
package eu.playproject.governance.mocks;

import java.net.URL;
import java.util.Collection;

import eu.playproject.governance.JSONTopicMetadataLoader;
import eu.playproject.governance.api.GovernanceExeption;
import eu.playproject.governance.api.bean.TopicMetadata;

/**
 * @author chamerling
 * 
 */
public class LocalJSONLoader extends JSONTopicMetadataLoader {

	URL url;

	public LocalJSONLoader(URL url) {
		this.url = url;
	}

	public Collection<TopicMetadata> load(URL url) throws GovernanceExeption {
		// override the given data
		return super.load(this.url);
	}

}
