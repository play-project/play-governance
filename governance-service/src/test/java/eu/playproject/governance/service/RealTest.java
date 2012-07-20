/**
 * 
 */
package eu.playproject.governance.service;

import java.util.ArrayList;
import java.util.List;

import eu.playproject.governance.JSONTopicMetadataLoader;
import eu.playproject.governance.api.bean.Metadata;
import eu.playproject.governance.api.bean.Topic;

/**
 * @author chamerling
 * 
 */
public class RealTest {// extends TestCase {

	public void main() throws Exception {
		EventGovernanceService eventGovernanceService = new EventGovernanceService();
		GovernanceManagementService governanceManagementService = new GovernanceManagementService();
		TopicMetadataService topicMetadataService = new TopicMetadataService();

		governanceManagementService.setEventGovernance(eventGovernanceService);
		governanceManagementService.setLoader(new JSONTopicMetadataLoader());
		governanceManagementService
				.setTopicMetadataService(topicMetadataService);

		List<Metadata> filter = new ArrayList<Metadata>();
		Metadata m = new Metadata();
		m.setName("dsbneedstosubscribe");
		m.setValue("true");
		filter.add(m);
		
		System.out.println("++++++++ " + topicMetadataService.getTopicsWithMeta(filter));

		governanceManagementService.start();
		
		for (Topic topic : eventGovernanceService.getTopics()) {
			List<Metadata> meta = topicMetadataService.getMetaData(topic);
			System.out.println("Topic = " + topic);
			for (Metadata metadata : meta) {
				System.out.println("  - META : " + metadata.getName() + "=" + metadata.getValue());
			}
		}
		
		Topic fb = new Topic();
		fb.setName("FacebookCepResults");
		fb.setNs("http://streams.event-processing.org/ids/");
		fb.setPrefix("s");
		
	
		System.out.println("+++++++++++++++++ " + topicMetadataService.getTopicsWithMeta(filter));
		
	}

}
