/**
 *
 * Copyright (c) 2013, Linagora
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
package org.ow2.play.governance.fakes;

import java.util.ArrayList;
import java.util.List;

import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.bean.Pattern;
import org.ow2.play.governance.api.bean.Topic;

/**
 * @author chamerling
 * 
 */
public class SimplePatternService implements
		org.ow2.play.governance.api.SimplePatternService {

	@Override
	public void analyze(String pattern) throws GovernanceExeption {
		System.out.println(">>>>>>>>>>>>>>> ANALYZE QUERY" + pattern);
	}

	@Override
	public List<Topic> getInputTopics(String pattern) throws GovernanceExeption {
		System.out.println(">>>>>>>>>>>>>>> GET INPUT TOPICS " + pattern);
		return new ArrayList<Topic>();
	}

	@Override
	public List<Topic> getOutputTopics(String pattern)
			throws GovernanceExeption {
		System.out.println(">>>>>>>>>>>>>>> GET OUTPUT TOPICS " + pattern);
		return new ArrayList<Topic>();
	}

	@Override
	public String deploy(String id, String pattern) throws GovernanceExeption {
		System.out.println(">>>>>>>>>>>>>>> DEPLOY PATTERN " + id + " : " + pattern);
		return "OK";
	}

	@Override
	public String undeploy(String id) throws GovernanceExeption {
		System.out.println(">>>>>>>>>>>>>>> UNDEPLOY PATTERN " + id);
		return "OK";
	}

	@Override
	public List<Pattern> getPatterns() throws GovernanceExeption {
		System.out.println(">>>>>>>>>>>>>>> GET PATTERNS");
		return new ArrayList<Pattern>();
	}

	@Override
	public Pattern getPattern(String id) throws GovernanceExeption {
		System.out.println(">>>>>>>>>>>>>>> GET PATTERN " + id);
		return new Pattern();
	}

}
