/**
 * 
 */
package eu.playproject.governance.service;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import eu.playproject.governance.api.bean.Metadata;
import eu.playproject.governance.api.bean.Topic;

/**
 * @author chamerling
 * 
 */
public class TopicMetaDataServiceTest extends TestCase {

	public void testFilterInclude() throws Exception {
		GovernanceEngine gov = GovernanceEngine.getInstance();

		Topic t = new Topic();
		t.setName("t1");
		t.setNs("http://foo/bar");
		t.setPrefix("ns");
		gov.createTopic(t);

		Topic t2 = new Topic();
		t2.setName("t2");
		t2.setNs("http://foo/bar");
		t2.setPrefix("ns");
		gov.createTopic(t2);

		Metadata meta = new Metadata();
		meta.setName("foo");
		meta.setName("bar");

		Metadata meta2 = new Metadata();
		meta2.setName("hello");
		meta2.setName("world");

		gov.addMetadata(t, meta);
		gov.addMetadata(t2, meta2);

		TopicMetadataService service = new TopicMetadataService();

		List<Metadata> filter = new ArrayList<Metadata>();
		filter.add(meta);

		List<Topic> filtered = service.getTopicsWithMeta(filter);
		assertEquals(1, filtered.size());
		assertEquals("t1", filtered.get(0).getName());
	}

	public void testNoFilter() throws Exception {
		TopicMetadataService service = new TopicMetadataService();
		List<Metadata> filter = new ArrayList<Metadata>();
		List<Topic> filtered = service.getTopicsWithMeta(filter);
		assertEquals(0, filtered.size());
	}
	
	public void testNullFilter() throws Exception {
		TopicMetadataService service = new TopicMetadataService();
		List<Topic> filtered = service.getTopicsWithMeta(null);
		assertEquals(0, filtered.size());
	}


}
