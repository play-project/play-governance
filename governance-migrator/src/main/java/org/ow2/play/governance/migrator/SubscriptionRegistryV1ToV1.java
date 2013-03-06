/**
 * 
 */
package org.ow2.play.governance.migrator;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.SubscriptionRegistry;
import org.ow2.play.governance.api.bean.Subscription;
import org.ow2.play.governance.cxf.CXFHelper;

/**
 * Migrate data from V1 registry to V1 registry
 * 
 * @author chamerling
 * 
 */
public class SubscriptionRegistryV1ToV1 {

	private static final Logger logger = Logger
			.getLogger(SubscriptionRegistryV1ToV1.class.getName());

	/**
	 * Return the list of subscriptions which have been migrated from source to
	 * destination
	 * 
	 * @param source
	 * @param destination
	 * @return
	 */
	public static List<Subscription> migrate(String source, String destination)
			throws GovernanceExeption {

		if (source == null || destination == null) {
			throw new GovernanceExeption(
					"Source and destination must be filled");
		}

		List<Subscription> result = new ArrayList<Subscription>();

		SubscriptionRegistry sourceRegistry = CXFHelper.getClientFromFinalURL(
				source, SubscriptionRegistry.class);

		SubscriptionRegistry destinationRegistry = CXFHelper
				.getClientFromFinalURL(destination, SubscriptionRegistry.class);

		List<Subscription> sources = sourceRegistry.getSubscriptions();
		if (sources != null) {
			for (Subscription subscription : sources) {
				try {
					destinationRegistry.addSubscription(subscription);
					result.add(subscription);
					if (logger.isLoggable(Level.INFO)) {
						logger.info("Added subscription = " + subscription);
					}
				} catch (Exception e) {
					logger.warning("Can not add the subscription to destination : "
							+ subscription);
				}
			}
		}
		return result;
	}

}
