/**
 * 
 */
package org.ow2.play.governance.service;

import java.util.ArrayList;
import java.util.List;

import org.ow2.play.governance.api.bean.Topic;
import org.ow2.play.governance.service.GovernanceManagementService;


import junit.framework.TestCase;

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
		
		// start all and check that sizes are OK...
		management.start();
		
	}
}
