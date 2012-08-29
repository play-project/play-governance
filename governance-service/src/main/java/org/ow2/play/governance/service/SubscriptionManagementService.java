/**
 * 
 */
package org.ow2.play.governance.service;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;

import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.SubscriptionManagement;
import org.ow2.play.governance.api.SubscriptionService;
import org.ow2.play.governance.api.bean.Subscription;

/**
 * TODO
 * @author chamerling
 * 
 */
public class SubscriptionManagementService implements SubscriptionManagement {
	
	//private SubscriptionService SubscriptionService;

	@Override
	@WebMethod
	public List<Subscription> subscribe(List<Subscription> subscriptions)
			throws GovernanceExeption {
		if (subscriptions == null) {
			throw new GovernanceExeption("Can not create null subscription");
		}
		throw new GovernanceExeption("subscribe :: Not implemented");
	}

	@Override
	@WebMethod
	public boolean unsubscribe(List<Subscription> subscriptions)
			throws GovernanceExeption {
		throw new GovernanceExeption("unsubscribe :: Not implemented");

	}

	@Override
	@WebMethod
	public void requestNew(List<Subscription> subscriptions)
			throws GovernanceExeption {
		throw new GovernanceExeption("requestNew :: Not implemented");
	}

	@Override
	public List<Subscription> replay(List<Subscription> subscriptions)
			throws GovernanceExeption {
		throw new GovernanceExeption("replay :: Not implemented");
	}

}
