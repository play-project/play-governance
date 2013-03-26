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
import static org.ow2.play.governance.platform.user.api.rest.helpers.Response.notFound;
import static org.ow2.play.governance.platform.user.api.rest.helpers.Response.ok;

import java.net.URI;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.SubscriptionRegistry;
import org.ow2.play.governance.api.bean.Topic;
import org.ow2.play.governance.platform.user.api.rest.bean.Subscription;
import org.ow2.play.governance.resources.SubscriptionHelper;
import org.ow2.play.governance.resources.TopicHelper;
import org.ow2.play.governance.user.api.UserException;
import org.ow2.play.governance.user.api.bean.Resource;
import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.MetadataException;
import org.ow2.play.metadata.api.service.MetadataService;
import org.ow2.play.service.registry.api.Constants;
import org.ow2.play.service.registry.api.Registry;
import org.ow2.play.service.registry.api.RegistryException;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 * @author chamerling
 * 
 */
public class SubscriptionService extends AbstractService implements
		org.ow2.play.governance.platform.user.api.rest.SubscriptionService {

	@Context
	private MessageContext mc;

	private org.ow2.play.governance.api.SubscriptionService subscriptionService;

	private SubscriptionRegistry subscriptionRegistry;

	private MetadataService metadataService;

	private Registry registry;

	@Override
	public Response subscribe(Subscription subscription) {

		if (subscription == null) {
			return error(Status.BAD_REQUEST, "Need input subscription");
		}

		if (subscription.resource == null) {
			return error(Status.BAD_REQUEST,
					"Resource to subscribe to is mandatory");
		}

		if (subscription.subscriber == null) {
			return error(Status.BAD_REQUEST, "Subscriber is manadatory");
		}

		// only topics are supported for now
		if (!TopicHelper.isTopic(subscription.resource)) {
			return error(Status.BAD_REQUEST,
					"Subscribe is only supported in topics/streams");
		}

		// TODO : Check if the user can subscribe to the resource in the
		// permissions
		if (!permissionChecker.checkResource(getUser().login,
				subscription.resource)) {
			return error(Status.UNAUTHORIZED, "Not allowed to subcribe to "
					+ subscription.resource);
		}

		// let's subscribe, store the subscription and send back a response
		// 1. ask the governance to subscribe
		String provider = null;
		try {
			provider = getSubscriptionEndpoint();
		} catch (RegistryException e1) {
			// TODO : Send alert to alert system (if only there is one...)
			return error(Status.INTERNAL_SERVER_ERROR,
					"Got a problem when subscribing to platform #RESTSUBS01");
		}

		org.ow2.play.governance.api.bean.Subscription in = new org.ow2.play.governance.api.bean.Subscription();
		in.setProvider(provider);
		in.setSubscriber(subscription.subscriber);
		org.ow2.play.governance.api.bean.Subscription result = null;
		try {
			in.setTopic(getTopic(subscription.resource));
			result = this.subscriptionService.subscribe(in);
		} catch (GovernanceExeption e) {
			e.printStackTrace();
			// TODO : Send alert to alert system (if only there is one...)
			return error(Status.INTERNAL_SERVER_ERROR,
					"Got a problem when subscribing to platform #RESTSUBS02");
		}

		// 2. when ok, store the subscription
		try {
			subscriptionRegistry.addSubscription(result);
		} catch (GovernanceExeption e) {
			// TODO : Send alert to alert system (if only there is one...)
			e.printStackTrace();
			// do not care about storage... If it fails, it will be difficult to
			// find subscriptions per user... Even more difficult if we can't
			// update the user in the next operation...
		}

		// 3. when stored, update the user object with the new subscription
		// resource
		try {
			userService.addResource(getUser().login,
					SubscriptionHelper.getSubscriptionURL(result.getId()));
		} catch (UserException e) {
			System.out.println("Can not store subscription into the user...");
			e.printStackTrace();
			// do not care if we can store, but send an alert to the monitoring
			// system...
		}

		// update the input data to send it back...
		subscription.subscriptionID = result.getId();
		subscription.resourceUrl = getResourceURI(subscription);

		return created(subscription);
	}

	@Override
	public Response unsubscribe(String subscriptionID) {
		if (subscriptionID == null) {
			return error(Status.BAD_REQUEST, "subcription ID is mandatary");
		}

		// get the subscription URI
		String url = SubscriptionHelper.getSubscriptionURL(subscriptionID);

		Resource resource = getUserResource(url);
		if (resource == null) {
			return error(Status.BAD_REQUEST,
					"Subscription %s has not been found in user subscriptions #RESTUNSUBS01");
		}

		org.ow2.play.governance.api.bean.Subscription subscription = new org.ow2.play.governance.api.bean.Subscription();
		subscription.setId(subscriptionID);

		try {
			boolean ok = subscriptionService.unsubscribe(subscription,
					getUnsubscribeEndpoint());
			if (!ok) {
				return error(Status.INTERNAL_SERVER_ERROR,
						"Can not unsubscribe from %s", subscriptionID);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return error(Status.INTERNAL_SERVER_ERROR,
					"Error while unsubscribing #RESTUNSUBS02");
		}

		// delete subscription from the user object.
		try {
			userService.removeResource(getUser().login, url);
		} catch (UserException e) {
			// just warn...
			e.printStackTrace();
		}

		// question: Can we keep subscriptions in the registry for stats? if
		// yes, will be cool to change their state
		try {
			boolean removed = subscriptionRegistry.remove(subscription);
			System.out.println("REMOVED from storage : " + removed);
		} catch (GovernanceExeption e) {
			// just warn
			e.printStackTrace();
		}

		return deleted();
	}

	@Override
	public Response subscriptions() {

		List<Resource> resources = getUser().resources;
		Collection<Resource> filtered = Collections2.filter(resources,
				new Predicate<Resource>() {
					public boolean apply(Resource input) {
						return isSubscription(input);
					}
				});

		Collection<Subscription> subscriptions = Collections2.transform(
				filtered, new Function<Resource, Subscription>() {
					public Subscription apply(Resource subscriptionResource) {
						// ask the subscription registry...

						String url = subscriptionResource.uri;
						org.ow2.play.governance.api.bean.Subscription filter = new org.ow2.play.governance.api.bean.Subscription();
						filter.setId(SubscriptionHelper.getSubscriptionID(url));
						try {
							List<org.ow2.play.governance.api.bean.Subscription> list = subscriptionRegistry
									.getSubscriptions(filter);
							
							if (list != null && list.size() > 0) {
								org.ow2.play.governance.api.bean.Subscription s = list
										.get(0);
								Subscription out = new Subscription();
								out.subscriber = s.getSubscriber();
								out.subscriptionID = s.getId();
								out.resource = TopicHelper.get(s.getTopic());
								out.resourceUrl = getResourceURI(out);
								return out;
							}
						} catch (GovernanceExeption e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// FIXME : check what to return in case of error...
						return new Subscription();
					}
				});

		return ok(subscriptions.toArray(new Subscription[subscriptions.size()]));
	}

	@Override
	public Response subscription(String id) {
		if (id == null) {
			return error(Status.BAD_REQUEST, "subcription ID is mandatary");
		}

		// check if it is a user subscription first...
		String url = SubscriptionHelper.getSubscriptionURL(id);

		Resource resource = getUserResource(url);
		if (resource == null) {
			return notFound();
		}

		org.ow2.play.governance.api.bean.Subscription filter = new org.ow2.play.governance.api.bean.Subscription();
		filter.setId(id);
		List<org.ow2.play.governance.api.bean.Subscription> s = null;
		try {
			s = subscriptionRegistry.getSubscriptions(filter);
		} catch (GovernanceExeption e) {
			return error(Status.INTERNAL_SERVER_ERROR,
					"Something bad occured in the server");
		}

		if (s == null || s.size() == 0) {
			return notFound();
		}

		org.ow2.play.governance.api.bean.Subscription result = s.get(0);
		Subscription out = new Subscription();
		out.resource = TopicHelper.get(result.getTopic());
		out.subscriber = result.getSubscriber();
		out.subscriptionID = result.getId();
		out.resourceUrl = getResourceURI(out);
		return ok(out);
	}

	protected String getResourceURI(Subscription subscription) {
		URI uri = mc.getUriInfo().getBaseUri();
		return uri.toString() + "subcriptions/" + subscription.subscriptionID;
	}

	protected boolean isSubscription(Resource resource) {
		return SubscriptionHelper.isSubscription(resource.uri);
	}

	/**
	 * Get a topic from the resource. This will call the metadata service to
	 * retrieve all the required information.
	 * 
	 * @param resource
	 * @return
	 * @throws GovernanceExeption
	 */
	protected Topic getTopic(String resource) throws GovernanceExeption {
		org.ow2.play.metadata.api.Resource r = org.ow2.play.metadata.api.Helper
				.getResourceFromURI(resource);
		List<MetaResource> metas = null;
		try {
			metas = metadataService.listWhere(r.getName(), r.getUrl());
		} catch (MetadataException e) {
			e.printStackTrace();
			throw new GovernanceExeption("Error while getting metadata", e);
		}

		if (metas == null || metas.size() == 0) {
			throw new GovernanceExeption(
					"Can not find the resource in the platform");
		}

		return TopicHelper.transform(metas.get(0));

	}

	/**
	 * Where to send the subscribe
	 * 
	 * @return
	 * @throws RegistryException
	 */
	protected String getSubscriptionEndpoint() throws RegistryException {
		return registry.get(Constants.DSB_PRODUCER);
	}

	/**
	 * @return
	 * @throws RegistryException
	 */
	protected String getUnsubscribeEndpoint() throws RegistryException {
		return registry.get(Constants.DSB_SUBSCRIPTION);
	}

	/**
	 * @param subscriptionRegistry
	 *            the subscriptionRegistry to set
	 */
	public void setSubscriptionRegistry(
			SubscriptionRegistry subscriptionRegistry) {
		this.subscriptionRegistry = subscriptionRegistry;
	}

	/**
	 * @param subscriptionService
	 *            the subscriptionService to set
	 */
	public void setSubscriptionService(
			org.ow2.play.governance.api.SubscriptionService subscriptionService) {
		this.subscriptionService = subscriptionService;
	}

	/**
	 * @param metadataService
	 *            the metadataService to set
	 */
	public void setMetadataService(MetadataService metadataService) {
		this.metadataService = metadataService;
	}

	/**
	 * @param registry
	 *            the registry to set
	 */
	public void setRegistry(Registry registry) {
		this.registry = registry;
	}
}
