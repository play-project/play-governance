/**
 *
 * Copyright (c) 2012, PetalsLink
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA 
 *
 */
package org.ow2.play.governance.dcep;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jws.WebMethod;

import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.SimplePatternService;
import org.ow2.play.governance.api.bean.Pattern;
import org.ow2.play.governance.api.bean.Topic;
import org.ow2.play.governance.cxf.CXFHelper;
import org.ow2.play.metadata.api.Metadata;
import org.ow2.play.metadata.api.Resource;
import org.ow2.play.metadata.api.service.MetadataService;
import org.ow2.play.service.registry.api.Constants;
import org.ow2.play.service.registry.api.Registry;
import org.ow2.play.service.registry.api.RegistryException;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import eu.play_project.play_platformservices.api.QueryDetails;
import eu.play_project.play_platformservices.api.QueryDispatchApi;
import eu.play_project.play_platformservices.api.QueryDispatchException;
import eu.play_project.play_platformservices.jaxb.Query;

/**
 * Wrap the initial query dispatcher
 * 
 * @author chamerling
 * 
 */
public class SimplePatternServiceImpl implements SimplePatternService {

	private static Logger logger = Logger
			.getLogger(SimplePatternServiceImpl.class.getName());

	private Registry registry;
	
	private MetadataService metadataService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.SimplePatternService#getInputTopics(java.
	 * lang.String)
	 */
	@Override
	public List<Topic> getInputTopics(String pattern) throws GovernanceExeption {
		List<Topic> result = Lists.newArrayList();
		QueryDispatchApi dispatchClient = getQueryDispatchApiClient();
		if (dispatchClient != null) {
			QueryDetails details = null;
			try {
				details = dispatchClient.analyseQuery(generateID(),
						pattern);
			} catch (QueryDispatchException e) {
				throw new GovernanceExeption(e);
			}
			if (details != null) {
				Set<String> input = details.getInputStreams();
				result.addAll(Collections2.transform(input,
						new Function<String, Topic>() {
							public Topic apply(String in) {
								return streamToTopic(in);
							}
						}));
			} else {
				logger.warning("No details returned from the dispatcher");
			}
		} else {
			logger.warning("No dispatch client");
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.SimplePatternService#getOuputTopics(java.
	 * lang.String)
	 */
	@Override
	@WebMethod
	public List<Topic> getOutputTopics(String pattern)
			throws GovernanceExeption {
		List<Topic> result = Lists.newArrayList();
		QueryDispatchApi dispatchClient = getQueryDispatchApiClient();
		if (dispatchClient != null) {
			QueryDetails details = null;
			try {
				details = dispatchClient.analyseQuery(generateID(),
						pattern);
			} catch (QueryDispatchException e) {
				throw new GovernanceExeption(e);
			}
			if (details != null) {
				String output = details.getOutputStream();
				result.add(streamToTopic(output));
			} else {
				logger.warning("No details returned from the dispatcher");
			}
		} else {
			logger.warning("No dispatch client found in the registry");
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.SimplePatternService#deploy(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public String deploy(String id, String statement) throws GovernanceExeption {
		if (statement == null) {
			throw new GovernanceExeption("Can not deploy a null statement");
		}
		if (id == null) {
			id = generateID();
		}
		String result = null;
		try {
			try {
				result = getQueryDispatchApiClient().registerQuery(id, statement);
			} catch (QueryDispatchException e) {
				throw new GovernanceExeption("Can not register pattern", e);
			}
		} catch (Exception e) {
			throw new GovernanceExeption(e);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.SimplePatternService#undeploy(java.lang.String
	 * )
	 */
	@Override
	public String undeploy(String id) throws GovernanceExeption {
		logger.info("Undeploying pattern " + id);
		try {
			getQueryDispatchApiClient().unregisterQuery(id);
		} catch (Exception e) {
			throw new GovernanceExeption("Can not undeploy pattern");
		}
		return "";
	}
	
	@Override
	@WebMethod
	public List<Pattern> getPatterns() throws GovernanceExeption {
		List<Pattern> result = new ArrayList<Pattern>();
		
		List<Query> patterns = getQueryDispatchApiClient().getRegisteredQueries();
		if (patterns == null) {
			return result;	
		}
		
		for (Query q : patterns) {
			Pattern pattern = new Pattern();
			pattern.id = q.id;
			pattern.content = q.content;
			pattern.name = q.name;
			pattern.recordDate = q.recordDate;
			result.add(pattern);
		}
		
		return result;
	}

	@Override
	@WebMethod
	public Pattern getPattern(String id) throws GovernanceExeption {
		String pattern = null;
		Pattern result = null;
		try {
			pattern = getQueryDispatchApiClient().getRegisteredQuery(id);
		} catch (QueryDispatchException e) {
			throw new GovernanceExeption(e);
		}
		if (pattern != null) {
			result = new Pattern();
			result.content = pattern;
			result.id = id;
		}
		return result;
	}

	protected QueryDispatchApi getQueryDispatchApiClient()
			throws GovernanceExeption {
		try {
			String url = registry.get(Constants.QUERY_DISPATCH_SERVICE);
			if (url != null) {
				return CXFHelper.getClientFromFinalURL(url,
						QueryDispatchApi.class);
			}
		} catch (RegistryException e) {
			throw new GovernanceExeption(e);
		}
		return null;
	}

	protected Topic streamToTopic(String stream) {
		if (stream == null) {
			return null;
		}

		Topic topic = new Topic();
		// TODO get the topic from the registry to retrieve its NS, if new, use
		// the default NS
		// TODO : Constant from GOV
		// Constants.STREAM_RESOURCE_NAME
		Resource r = new Resource(org.ow2.play.governance.api.Constants.STREAM_RESOURCE_NAME, stream);

		String ns = r.getUrl().substring(0,
				r.getUrl().lastIndexOf('/') + 1);
		String name = r.getUrl().substring(
				r.getUrl().lastIndexOf('/') + 1);
		
		topic.setName(name);
		topic.setNs(ns);
		
		// get the NS from the metadataservice, only useful if the topic already exists
		try {
			Metadata md = metadataService.getMetadataValue(r, org.ow2.play.governance.api.Constants.QNAME_PREFIX_URL);
			if (md != null && md.getData() != null
					&& md.getData().size() == 1
					&& md.getData().get(0).getValue() != null) {
				topic.setPrefix(md.getData().get(0).getValue());
			} else {
				topic.setPrefix(org.ow2.play.governance.api.Constants.DEFAULT_PREFIX);
			}
		} catch (Exception e) {
			final String msg = "Can not get registry endpoint";
			if (logger.isLoggable(Level.FINE)) {
				logger.log(Level.FINE, msg, e);
			} else {
				logger.warning(msg);
			}
			topic.setPrefix(org.ow2.play.governance.api.Constants.DEFAULT_PREFIX);
		}


		return topic;
	}

	protected String generateID() {
		return "http://play.ow2.org/pattern/" + UUID.randomUUID().toString();
	}
	
	/**
	 * @param metadataService the metadataService to set
	 */
	public void setMetadataService(MetadataService metadataService) {
		this.metadataService = metadataService;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

}
