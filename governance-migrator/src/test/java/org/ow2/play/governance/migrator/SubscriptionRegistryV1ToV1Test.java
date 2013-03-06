/**
 * 
 */
package org.ow2.play.governance.migrator;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.ow2.play.governance.api.SubscriptionRegistry;
import org.ow2.play.governance.api.bean.Subscription;
import org.ow2.play.governance.api.bean.Topic;
import org.ow2.play.governance.cxf.CXFHelper;
import org.ow2.play.governance.cxf.Service;
import org.ow2.play.governance.service.InMemorySubscriptionRegistryService;

/**
 * @author chamerling
 * 
 */
public class SubscriptionRegistryV1ToV1Test extends TestCase {

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testMigrate() throws Exception {
		String source = "http://localhost:8493/foo/bar/Source";
		String destination = "http://localhost:8493/foo/bar/Destination";

		InMemorySubscriptionRegistryService sourceRegistry = new InMemorySubscriptionRegistryService();
		InMemorySubscriptionRegistryService destRegistry = new InMemorySubscriptionRegistryService();

		Service sourceService = CXFHelper.getServiceFromFinalURL(source,
				SubscriptionRegistry.class, sourceRegistry);
		Service destService = CXFHelper.getServiceFromFinalURL(destination,
				SubscriptionRegistry.class, destRegistry);

		List<Subscription> subscriptions = new ArrayList<Subscription>();
		for (int i = 0; i < 10; i++) {
			Topic t = new Topic();
			t.setName("" + i);
			t.setNs("http://foo/" + i);
			t.setPrefix("pre");
			Subscription s = new Subscription("" + i, "subscriber-" + i,
					"provider-" + i, t, System.currentTimeMillis());
			subscriptions.add(s);
			sourceRegistry.addSubscription(s);
		}

		sourceService.start();
		destService.start();

		List<Subscription> result = SubscriptionRegistryV1ToV1.migrate(source,
				destination);
		assertNotNull(result);
		assertEquals(subscriptions.size(), result.size());

	}

}
