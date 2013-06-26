/**
 * 
 */
package org.ow2.play.governance.storage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.ow2.petals.nosql.mongo.AbstractMongoService;
import org.ow2.petals.nosql.mongo.MongoHelper;
import org.ow2.play.governance.api.BootSubscriptionService;
import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.bean.Subscription;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * 
 * @author chamerling
 * 
 */
@WebService
public class MongoBootSubscriptionService extends AbstractMongoService implements BootSubscriptionService {
	
	private static Logger logger = Logger
			.getLogger(MongoSubscriptionRegistry.class.getName());

	/**
	 * 
	 */
	public MongoBootSubscriptionService() {
		super();
		setCollectionName("bootsubscriptions");
		setDatabaseName("play");
	}
	
	@WebMethod(exclude=true)
	public void init() {
		initializeMongo();
	}
	
	@Override
	@WebMethod
	public void add(Subscription subscription) throws GovernanceExeption {
		if (subscription == null) {
			throw new GovernanceExeption("Null subscriptions can not be added");
		}
		
		DBObject dbo = Helper.toDBO(subscription);
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Adding subscription object to collection : " + dbo);
		}
		getDbCollection().insert(dbo);
	}

	@Override
	public void remove(Subscription subscription) throws GovernanceExeption {
		if (subscription == null) {
			throw new GovernanceExeption("Can not remove null subscriptions");
		}
		
		throw new GovernanceExeption("Remove is not implemented");
	}
	
	@Override
	@WebMethod
	public void removeAll() {
        MongoHelper.clearCollection(getDbCollection());
	}

	@Override
	@WebMethod
	public void load(String url) throws GovernanceExeption {

	}

	@Override
	@WebMethod
	public List<Subscription> all() throws GovernanceExeption {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Getting subscritions");
		}
		
		List<Subscription> result = new ArrayList<Subscription>();

		DBCursor cursor = getDbCollection().find();
		Iterator<DBObject> iter = cursor.iterator();
		while (iter.hasNext()) {
			DBObject dbo = iter.next();
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("Extracting subscription : " + dbo);
			}

			Subscription s = Helper.toSubscription(dbo);
			if (s != null) {
				result.add(s);
			}
		}
		
		return result;
	}

}
