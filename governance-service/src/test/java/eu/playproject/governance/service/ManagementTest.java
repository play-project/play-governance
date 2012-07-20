/**
 * 
 */
package eu.playproject.governance.service;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.wsaddressing.W3CEndpointReference;

import junit.framework.TestCase;
import eu.playproject.governance.api.EventGovernance;
import eu.playproject.governance.api.GovernanceExeption;
import eu.playproject.governance.api.bean.Metadata;
import eu.playproject.governance.api.bean.Topic;
import eu.playproject.governance.mocks.LocalJSONLoader;

/**
 * @author chamerling
 *
 */
public class ManagementTest extends TestCase {
	
	public void testStart() throws Exception {
		
		final List<Topic> topics = new ArrayList<Topic>();
		Topic topic = new Topic();
		topic.setName("TopicA");
		topic.setNs("http://foo/bar");
		topic.setPrefix("t");
		topics.add(topic);
		
		GovernanceManagementService management = new GovernanceManagementService();
		management.setEventGovernance(new EventGovernance() {
			
			@Override
			public void loadResources(InputStream topicNameSpaceInputStream)
					throws GovernanceExeption {
			}
			
			@Override
			public List<Topic> getTopics() {
				return topics;
			}
			
			@Override
			public List<QName> findTopicsByElement(QName element)
					throws GovernanceExeption {
				return null;
			}
			
			@Override
			public List<W3CEndpointReference> findEventProducersByTopics(
					List<QName> topics) throws GovernanceExeption {
				return null;
			}
			
			@Override
			public List<W3CEndpointReference> findEventProducersByElements(
					List<QName> element) throws GovernanceExeption {
				return null;
			}
			
			@Override
			public void createTopic(Topic topic) throws GovernanceExeption {
			}
		});
		
		URL url = ManagementTest.class.getResource("/metalist.json");
		if (url == null) {
			fail();
		}
		management.setLoader(new LocalJSONLoader(url));
		
		TopicMetadataService metaService = new TopicMetadataService();
		management.setTopicMetadataService(metaService);
		
		// start all and check that sizes are OK...
		management.start();
		
		List<Metadata> result = metaService.getMetaData(topic);
		assertNotNull(result);
		assertEquals(2, result.size());
	}
}
