/**
 * 
 */
package eu.playproject.governance;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import eu.playproject.governance.api.GovernanceExeption;
import eu.playproject.governance.api.TopicMetadataLoader;
import eu.playproject.governance.api.bean.TopicMetadata;

/**
 * Load from remote URL using Gson
 * 
 * @author chamerling
 * 
 */
public class JSONTopicMetadataLoader implements TopicMetadataLoader {

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.playproject.governance.api.TopicMetadataLoader#load()
	 */
	@Override
	public Collection<TopicMetadata> load(URL url) throws GovernanceExeption {
		Type type = new TypeToken<Collection<TopicMetadata>>() {
		}.getType();

		Gson gson = new Gson();
		Collection<TopicMetadata> list = new ArrayList<TopicMetadata>();
		try {
			JsonReader reader = new JsonReader(new InputStreamReader(
					url.openStream()));
			list = gson.fromJson(reader, type);
		} catch (Exception e) {
			throw new GovernanceExeption(e);
		}
		return list;
	}

}
