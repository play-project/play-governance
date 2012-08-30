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
package org.ow2.play.governance.bootstrap.api;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.ow2.play.governance.api.bean.Subscription;

/**
 * Bootstrap API between notification consumers and providers.
 * 
 * @author chamerling
 * 
 */
@WebService
public interface BootstrapService {

	/**
	 * Subscribes to a topic on the provider endpoint on behalf of the
	 * subscriber. It means that when the subscription is done, the subscriber
	 * will be notified when new messages are published on the topic by the
	 * provider.
	 * 
	 * @return a list of subscriptions
	 */
	@WebMethod
	List<Subscription> bootstrap(String providerEndpoint,
			String subscriberEndpoint) throws BootstrapFault;

}
