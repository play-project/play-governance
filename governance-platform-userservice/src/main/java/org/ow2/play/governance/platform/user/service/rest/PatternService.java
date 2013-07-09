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
package org.ow2.play.governance.platform.user.service.rest;

import static org.ow2.play.governance.platform.user.api.rest.helpers.Response.created;
import static org.ow2.play.governance.platform.user.api.rest.helpers.Response.deleted;
import static org.ow2.play.governance.platform.user.api.rest.helpers.Response.error;
import static org.ow2.play.governance.platform.user.api.rest.helpers.Response.ok;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.ow2.play.governance.api.Constants;
import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.PatternRegistry;
import org.ow2.play.governance.api.SimplePatternService;
import org.ow2.play.governance.api.bean.Topic;
import org.ow2.play.governance.platform.user.api.rest.bean.Pattern;
import org.ow2.play.governance.resources.ModeHelper;
import org.ow2.play.governance.resources.PatternHelper;
import org.ow2.play.governance.resources.TopicHelper;
import org.ow2.play.governance.user.api.UserException;
import org.ow2.play.governance.user.api.bean.Resource;
import org.ow2.play.governance.user.api.bean.User;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

/**
 * 
 * @author chamerling
 * 
 */
public class PatternService extends AbstractService implements
		org.ow2.play.governance.platform.user.api.rest.PatternService {

	private PatternRegistry patternRegistry;

	private SimplePatternService patternService;

	public Response patterns() {
		User user = getUser();
		List<Resource> resources = user.resources;
		Collection<Resource> filtered = Collections2.filter(resources,
				new Predicate<Resource>() {
					public boolean apply(Resource input) {
						return input.name
								.equals(Constants.PATTERN_RESOURCE_NAME);
					}
				});

		Collection<Pattern> patterns = Collections2.transform(filtered,
				new Function<Resource, Pattern>() {
					public Pattern apply(Resource patternResource) {
						try {
							org.ow2.play.governance.api.bean.Pattern pattern = patternRegistry.get(PatternHelper
									.getPatternIDFromBaseURI(patternResource.uri));
							Pattern out = new Pattern();
							out.data = pattern.content;
							out.id = pattern.id;
							out.resourceUrl = getResourceURI(out);
							out.date = patternResource.date;

							return out;
						} catch (GovernanceExeption e) {
							// ...
							return new Pattern();
						}
					}
				});

		return ok(patterns.toArray(new Pattern[patterns.size()]));
	}

	public Response pattern(String id) {
		if (id == null) {
			return error(Status.BAD_REQUEST, "Pattern ID is mandatary");
		}

		// check if it is a user subscription first...
		final String url = PatternHelper.getBaseURI(id);
		
		Resource resource = getUserResource(url,
				Constants.PATTERN_RESOURCE_NAME);
		if (resource == null) {
			return error(Status.BAD_REQUEST,
					"Pattern %s has not been found in user pattern #RESPATTERN01", id);
		}
		
		org.ow2.play.governance.api.bean.Pattern pattern = null;
		try {
			pattern = patternRegistry.get(id);
			if (pattern == null) {
				return error(Status.NOT_FOUND,
						"Pattern %s has not been found in pattern registry #RESPATTERN02", id);
			}
		} catch (GovernanceExeption e) {
			e.printStackTrace();
			// TODO : send to monitoring
			return error(Status.INTERNAL_SERVER_ERROR,
					"Error while getting pattern #RESPATTERN03");
		}

		Pattern out = new Pattern();
		out.data = pattern.content;
		out.id = pattern.id;
		out.resourceUrl = getResourceURI(out);
		out.date = resource.date;
		return ok(out);
	}

	public Response deploy(String pattern) {
		if (pattern == null) {
			return error(Status.BAD_REQUEST, "Pattern data is missing");
		}

		final User user = getUser();
		String id = UUID.randomUUID().toString();

        List<String> inputs = Lists.newArrayList();
        List<String> outputs = Lists.newArrayList();

        try {
            inputs.addAll(Collections2.transform(patternService.getInputTopics(pattern), new Function<Topic, String>() {
                @Override
                public String apply(Topic input) {
                    return TopicHelper.get(input);
                }
            }));

            outputs.addAll(Collections2.transform(patternService.getOutputTopics(pattern), new Function<Topic, String>() {
                @Override
                public String apply(Topic input) {
                    return TopicHelper.get(input);
                }
            }));

        } catch (GovernanceExeption e) {
            e.printStackTrace();
            return error(Status.INTERNAL_SERVER_ERROR, "Can not retrieve streams from pattern : %s", e.getMessage());
        }

        // check the inputs
        Collection<String> authorizedInputs = Collections2.filter(inputs, new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return permissionChecker.checkMode(user.login, input, ModeHelper.SUBSCRIBE);
            }
        });

        if (authorizedInputs.size() != inputs.size()) {
            // invalid permissions, some streams are not accessible to the current user
            return error(Status.PRECONDITION_FAILED, "Invalid permissions, some input streams are not accessible to the current user");
        }

        // check the outputs
        Collection<String> authorizedOutputs = Collections2.filter(outputs, new Predicate<String>() {
            @Override
            public boolean apply(String output) {
                return permissionChecker.checkMode(user.login, output, ModeHelper.NOTIFY);
            }
        });

        if (authorizedOutputs.size() != outputs.size()) {
            // invalid permissions, some streams are not accessible to the current user
            return error(Status.PRECONDITION_FAILED, "Invalid permissions, some output streams are not accessible to the current user");
        }

        // if all is OK (ie user have valid access to all), deploy!
		try {
			patternService.deploy(id, pattern);
		} catch (GovernanceExeption e) {
			return error(Status.INTERNAL_SERVER_ERROR,
					"Pattern can not be deployed : %s", e.getMessage());
		}

		// once deployed, register it
		try {
			org.ow2.play.governance.api.bean.Pattern p = new org.ow2.play.governance.api.bean.Pattern();
			p.content = pattern;
			p.id = id;
			patternRegistry.put(p);
		} catch (GovernanceExeption e) {
			e.printStackTrace();
		}

		// once registred, add it to the user...
		Pattern p = new Pattern();
		p.id = id;
		p.data = pattern;
		p.resourceUrl = getResourceURI(p);

		try {
			// store the pattern as resource ie with name pattern and base URI
			userService.addResource(user.id, PatternHelper.getBaseURI(p.id),
					Constants.PATTERN_RESOURCE_NAME);
		} catch (UserException e) {
			e.printStackTrace();
		}

		return created(p);
	}

	public Response undeploy(String id) {

		if (id == null) {
			return error(Status.BAD_REQUEST, "Pattern ID is mandatary");
		}

		final String url = PatternHelper.getBaseURI(id);
		User user = getUser();
		
		Resource resource = getUserResource(url,
				Constants.PATTERN_RESOURCE_NAME);
		if (resource == null) {
			return error(Status.BAD_REQUEST,
					"Pattern %s has not been found in user pattern #RESTUNDEPLOY01", id);
		}
		
		try {
			patternService.undeploy(id);
		} catch (Exception e) {
			e.printStackTrace();
			return error(Status.INTERNAL_SERVER_ERROR,
					"Error while undeploying pattern #RESTUNDEPLOY02");
		}

		// delete pattern from the user object.
		try {
			user = userService.removeResource(user.id, url,
					Constants.PATTERN_RESOURCE_NAME);
		} catch (UserException e) {
			// just warn...
			e.printStackTrace();
		}

		// question: Can we keep pattern in the registry for stats? if
		// yes, will be cool to change their state
		try {
			patternRegistry.delete(Lists.newArrayList(id));
		} catch (GovernanceExeption e) {
			// just warn...
			e.printStackTrace();
		}

		return deleted();
	}

	public Response analyze(String pattern) {
		if (pattern == null || pattern.trim().length() == 0) {
			return error(Status.BAD_REQUEST, "Pattern is mandatary");
		}

		try {
			patternService.analyze(pattern);
		} catch (GovernanceExeption e) {
			e.printStackTrace();
			return error(Status.BAD_REQUEST, e.getMessage());
		}
		return Response.ok().build();
	}

	protected String getResourceURI(Pattern pattern) {
		URI uri = mc.getUriInfo().getBaseUri();
		return uri.toString() + "patterns/" + pattern.id;
	}

	/**
	 * @param patternRegistry
	 *            the patternRegistry to set
	 */
	public void setPatternRegistry(PatternRegistry patternRegistry) {
		this.patternRegistry = patternRegistry;
	}

	/**
	 * @param patternService
	 *            the patternService to set
	 */
	public void setPatternService(SimplePatternService patternService) {
		this.patternService = patternService;
	}

}
