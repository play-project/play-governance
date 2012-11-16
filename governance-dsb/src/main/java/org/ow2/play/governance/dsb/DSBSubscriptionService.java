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
package org.ow2.play.governance.dsb;

import javax.jws.WebMethod;

import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.SubscriptionService;
import org.ow2.play.governance.api.bean.Subscription;

/**
 * @author chamerling
 *
 */
public class DSBSubscriptionService implements SubscriptionService {

	/* (non-Javadoc)
	 * @see org.ow2.play.governance.api.SubscriptionService#subscribe(org.ow2.play.governance.api.bean.Subscription)
	 */
	@Override
	@WebMethod
	public Subscription subscribe(Subscription subscription) throws GovernanceExeption {
		throw new GovernanceExeption("Not implemented");
	}

	/* (non-Javadoc)
	 * @see org.ow2.play.governance.api.SubscriptionService#unsubscribe(org.ow2.play.governance.api.bean.Subscription, java.lang.String)
	 */
	@Override
	@WebMethod
	public boolean unsubscribe(Subscription subscription,
			String subscriptionManagementEndpoint) throws GovernanceExeption {
		throw new GovernanceExeption("Not implemented");
	}

}
