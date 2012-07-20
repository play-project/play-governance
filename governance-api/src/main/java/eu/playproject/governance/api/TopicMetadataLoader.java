/**
 * 
 */
package eu.playproject.governance.api;

import java.net.URL;
import java.util.Collection;

import eu.playproject.governance.api.bean.TopicMetadata;

/**
 * @author chamerling
 *
 */
public interface TopicMetadataLoader {
	
	/**
	 * Load topic metadata from URL
	 * 
	 * @param url
	 * @return
	 */
	Collection<TopicMetadata> load(URL url) throws GovernanceExeption;

}
