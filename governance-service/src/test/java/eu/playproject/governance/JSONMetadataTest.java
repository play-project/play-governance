/**
 * 
 */
package eu.playproject.governance;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import eu.playproject.governance.api.bean.Metadata;
import eu.playproject.governance.api.bean.TopicMetadata;

/**
 * @author chamerling
 * 
 */
public class JSONMetadataTest extends TestCase {

	public void testSerializeMeta() throws Exception {
		Gson gson = new Gson();
		List<Metadata> list = new ArrayList<Metadata>();
		Metadata meta = new Metadata();
		meta.setName("metaname");
		meta.setValue("metavalue");
		list.add(meta);

		Metadata meta2 = new Metadata();
		meta2.setName("metaname2");
		meta2.setValue("metavalue2");
		list.add(meta2);

		System.out.println(gson.toJson(list));
	}

	public void testSerializeTopicMeta() throws Exception {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		List<TopicMetadata> metaList = new ArrayList<TopicMetadata>();

		TopicMetadata tm = new TopicMetadata();
		tm.setTopic("TopicA");

		List<Metadata> list = new ArrayList<Metadata>();
		Metadata meta = new Metadata();
		meta.setName("metaname");
		meta.setValue("metavalue");
		list.add(meta);

		Metadata meta2 = new Metadata();
		meta2.setName("metaname2");
		meta2.setValue("metavalue2");
		list.add(meta2);

		tm.setMetadata(list);
		metaList.add(tm);

		System.out.println("+++");
		System.out.println(gson.toJson(metaList));
	}

	public void testDeserialize() throws Exception {
		Gson gson = new Gson();
		Type type = new TypeToken<Collection<TopicMetadata>>() {
		}.getType();
		JsonReader reader = new JsonReader(new InputStreamReader(
				JSONMetadataTest.class.getResourceAsStream("/metalist.json")));
		Collection<TopicMetadata> list = gson.fromJson(reader, type);

		System.out.println(list.size());
		assertTrue(list.size() > 0);
		
		for (TopicMetadata topicMetadata : list) {
			System.out.println(topicMetadata.getTopic());
			System.out.println(topicMetadata.getMetadata().size());
		}
	}
}
