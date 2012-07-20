/**
 * 
 */
package eu.playproject.governance.api;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import eu.playproject.governance.api.bean.Metadata;
import eu.playproject.governance.api.bean.Topic;

/**
 * CRUD metadata for topics
 * 
 * @author chamerling
 * 
 */
@WebService
public interface TopicMetadataService {

	@WebMethod
	void addMetadata(Topic topic, Metadata metadata) throws GovernanceExeption;

	@WebMethod
	void removeMetadata(Topic topic, Metadata metadata)
			throws GovernanceExeption;

	@WebMethod
	List<Metadata> getMetaData(Topic topic) throws GovernanceExeption;

	@WebMethod
	Metadata getMetadataValue(Topic topic, String key) throws GovernanceExeption;

	@WebMethod
	boolean deleteMetaData(Topic topic) throws GovernanceExeption;

	/**
	 * Get a list of topic filtered with the metadata entries.
	 * 
	 * @param include
	 *            include this metadata
	 * @return
	 * @throws GovernanceExeption
	 */
	@WebMethod
	public List<Topic> getTopicsWithMeta(List<Metadata> include)
			throws GovernanceExeption;

}
