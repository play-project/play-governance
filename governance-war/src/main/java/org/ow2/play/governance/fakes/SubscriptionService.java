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

import java.util.UUID;

import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.bean.Subscription;

/**
 * @author chamerling
 * 
 */
public class SubscriptionService implements
		org.ow2.play.governance.api.SubscriptionService {

	@Override
	public Subscription subscribe(Subscription subscription)
			throws GovernanceExeption {
		System.out.println(">>>>>>>>>>>>>>> FAKE SUBSCRIBE TO " + subscription);
		subscription.setId(UUID.randomUUID().toString());
		return subscription;
	}

	@Override
	public boolean unsubscribe(Subscription subscription,
			String subscriptionManagementEndpoint) throws GovernanceExeption {
		System.out
				.println(">>>>>>>>>>>>>>> FAKE UNSUBSCRIBE FROM "
						+ subscription + " :::: ep : "
						+ subscriptionManagementEndpoint);

		return true;
	}

}
