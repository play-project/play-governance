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
import java.util.logging.Logger;

import javax.jws.WebMethod;

import org.ow2.play.governance.api.ComplexPatternService;
import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.SimplePatternService;
import org.ow2.play.governance.api.bean.Pattern;
import org.ow2.play.governance.api.bean.Topic;
import org.ow2.play.metadata.api.Metadata;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

/**
 * User API to interact with the pattern stuff. Hides the
 * {@link ComplexPatternServiceImpl} complexity.
 * 
 * @author chamerling
 * 
 */
public class PatternServiceImpl implements SimplePatternService {

	private static Logger logger = Logger.getLogger(PatternServiceImpl.class
			.getName());

	private ComplexPatternService complexPatternService;

	private SimplePatternService simplePatternService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.SimplePatternService#getInputTopics(java.
	 * lang.String)
	 */
	@Override
	@WebMethod
	public List<Topic> getInputTopics(String pattern) throws GovernanceExeption {
		logger.info("Get input topics");
		return simplePatternService.getInputTopics(pattern);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.SimplePatternService#getOutputTopics(java
	 * .lang.String)
	 */
	@Override
	@WebMethod
	public List<Topic> getOutputTopics(String pattern)
			throws GovernanceExeption {
		return simplePatternService.getOutputTopics(pattern);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.SimplePatternService#deploy(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	@WebMethod
	public String deploy(String id, String pattern) throws GovernanceExeption {
		String result = "";
		List<Metadata> out = complexPatternService.deploy(id, pattern, null);

		// get the endpoint to subscribe to from the metadata
		List<Metadata> endpoints = Lists.newArrayList(Collections2.filter(out,
				new Predicate<Metadata>() {
					public boolean apply(Metadata md) {
						return md != null && md.getName() != null
								&& md.getName().equals("endpoint");
					};
				}));

		result = endpoints.get(0) != null && endpoints.get(0).getData() != null
				&& endpoints.get(0).getData().get(0) != null ? endpoints.get(0)
				.getData().get(0).getValue() : "";
		return result;
	}
	
	@Override
	@WebMethod
	public List<Pattern> getPatterns() throws GovernanceExeption{
		return simplePatternService.getPatterns();
	}

	@Override
	@WebMethod
	public Pattern getPattern(String id) throws GovernanceExeption {
		if (id == null) {
			throw new GovernanceExeption("Null pattern id parameter");
		}
		return simplePatternService.getPattern(id);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.SimplePatternService#undeploy(java.lang.String
	 * )
	 */
	@Override
	@WebMethod
	public String undeploy(String id) throws GovernanceExeption {
		this.complexPatternService.undeploy(id);
		return "";
	}

	public void setComplexPatternService(
			ComplexPatternService complexPatternService) {
		this.complexPatternService = complexPatternService;
	}

	public void setSimplePatternService(
			SimplePatternService simplePatternService) {
		this.simplePatternService = simplePatternService;
	}
}
