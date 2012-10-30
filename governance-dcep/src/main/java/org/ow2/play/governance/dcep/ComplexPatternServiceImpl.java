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

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jws.WebMethod;

import org.ow2.play.governance.api.ComplexPatternService;
import org.ow2.play.governance.api.EventGovernance;
import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.PatternRegistry;
import org.ow2.play.governance.api.SimplePatternService;
import org.ow2.play.governance.api.bean.Pattern;
import org.ow2.play.governance.api.bean.Topic;
import org.ow2.play.metadata.api.Data;
import org.ow2.play.metadata.api.Metadata;

import com.google.common.collect.Lists;

/**
 * @author chamerling
 * 
 */
public class ComplexPatternServiceImpl implements ComplexPatternService {

	private static Logger logger = Logger
			.getLogger(ComplexPatternServiceImpl.class.getName());

	private SimplePatternService simplePatternService;

	private EventGovernance eventGovernance;
	
	private PatternRegistry patternRegistry;

	private AtomicLong id = new AtomicLong(0);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.ComplexPatternService#deploy(java.lang.String
	 * , java.lang.String, java.util.List)
	 */
	@Override
	@WebMethod
	public List<Metadata> deploy(String id, String pattern,
			List<Metadata> metadata) throws GovernanceExeption {

		long sequenceId = this.id.incrementAndGet();
		long start = System.currentTimeMillis();

		if (logger.isLoggable(Level.FINE)) {
			logger.fine("[SEQ:" + sequenceId + "] Deploying pattern : " + id);
		}

		List<Metadata> result = Lists.newArrayList();

		// 1. create the topics in the platform if needed
		// up to the platform to create them or not, we do not check it here.
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("[SEQ:" + sequenceId + "] creating topics...");
		}
		String endpoint = this.createTopics(pattern);
		result.add(new Metadata("endpoint", new Data("url", endpoint)));
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("[SEQ:" + sequenceId + "] topic creation endpoint : "
					+ endpoint);
		}

		// 2. deploy the pattern to the cep
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("[SEQ:" + sequenceId + "] deploying pattern...");
		}
		this.deployPattern(id, pattern);
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("[SEQ:" + sequenceId + "] pattern deployed!");
		}

		// 3. Register the pattern in the metadata registry for later use...
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("[SEQ:" + sequenceId + "] registering pattern...");
		}
		this.registerPattern(id, pattern, metadata);
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("[SEQ:" + sequenceId + "] pattern registred!");
		}

		logger.info("Pattern deployed in the platform in "
				+ (System.currentTimeMillis() - start) + " ms");

		return result;
	}

	/**
	 * TODO Register the pattern in the metadata storage.
	 * 
	 * @param id
	 * @param pattern
	 * @param metadata
	 */
	private void registerPattern(String id, String pattern,
			List<Metadata> metadata) {
		if (patternRegistry == null) {
			logger.fine("Pattern registry is null");
			return;
		}
		
		Pattern p = new Pattern();
		p.id = id;
		p.content = pattern;
		p.name = "_" + id;
		p.recordDate = "" + System.currentTimeMillis();
		
		try {
			this.patternRegistry.put(p);
		} catch (GovernanceExeption e) {
			logger.warning("Can not register pattern in registry : " + e.getMessage());
		}
	}

	/**
	 * @param pattern
	 * @throws GovernanceExeption
	 */
	private void deployPattern(String id, String pattern)
			throws GovernanceExeption {
		this.simplePatternService.deploy(id, pattern);
	}

	/**
	 * Create the topics in the platform
	 * 
	 * @param topics
	 */
	private String createTopics(String pattern) {
		String result = null;
		try {
			List<Topic> inTopics = this.simplePatternService
					.getInputTopics(pattern);
			List<Topic> outTopics = this.simplePatternService
					.getOutputTopics(pattern);

			// create the topics and EC if needed

			// create the topics to subscribe to
			for (Topic topic : outTopics) {
				// create the topics on the platform if needed
				result = eventGovernance.createSubscriberTopic(topic);
			}

		} catch (GovernanceExeption e) {
			e.printStackTrace();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.ComplexPatternService#undeploy(java.lang.
	 * String)
	 */
	@Override
	@WebMethod
	public List<Metadata> undeploy(String id) throws GovernanceExeption {
		// forward
		this.simplePatternService.undeploy(id);
		return Lists.newArrayList();
	}

	public void setSimplePatternService(
			SimplePatternService simplePatternService) {
		this.simplePatternService = simplePatternService;
	}

	public void setEventGovernance(EventGovernance eventGovernance) {
		this.eventGovernance = eventGovernance;
	}
	
	public void setPatternRegistry(PatternRegistry patternRegistry) {
		this.patternRegistry = patternRegistry;
	}
}
