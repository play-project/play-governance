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
package org.ow2.play.governance.storage;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jws.WebMethod;
import javax.jws.WebParam;

import org.ow2.play.governance.api.SubscriptionRegistry;
import org.ow2.play.governance.api.bean.Subscription;
import org.ow2.play.governance.api.bean.Topic;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.ServerAddress;
import com.mongodb.WriteResult;

/**
 * @author chamerling
 * 
 */
public class MongoSubscriptionRegistry implements SubscriptionRegistry {

	private final static String DEFAULT_MONGO_DB_HOSTNAME = "localhost";
	private final static String DEFAULT_MONGO_DB_PORT = "27017";
	private final static String DEFAULT_MONGO_DB_DATABASE_NAME = "play";
	private final static String DEFAULT_MONGO_DB_COLLECTION_NAME = "subscription";

	private static final String ID_KEY = "id";
	private static final String SUBSCRIBER_KEY = "subscriber";
	private static final String PRODUCER_KEY = "producer";
	private static final String TOPIC_KEY = "topic";
	private static final String DATE_KEY = "date";
	private static final String STATUS_KEY = "status";

	private static final String TOPIC_NAME_KEY = "name";
	private static final String TOPIC_NS_KEY = "ns";
	private static final String TOPIC_PREFIX_KEY = "prefix";

	private String hostname = DEFAULT_MONGO_DB_HOSTNAME;
	private String port = DEFAULT_MONGO_DB_PORT;
	private String databaseName = DEFAULT_MONGO_DB_DATABASE_NAME;
	private String collectionName = DEFAULT_MONGO_DB_COLLECTION_NAME;
	private String userName;
	private String password;
	private Mongo mongo;
	private DBCollection collection;

	private Properties properties;

	private boolean initialized = false;

	private static Logger logger = Logger
			.getLogger(MongoSubscriptionRegistry.class.getName());

	/**
	 * 
	 */
	public MongoSubscriptionRegistry() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.metadata.api.service.Initializable#init(java.util.Properties
	 * )
	 */
	@WebMethod(exclude = true)
	public void init() {
		logger.info("Initializing metadata service");

		if (mongo != null) {
			close();
		}

		if (properties != null) {
			hostname = properties.getProperty("mongo.hostname",
					DEFAULT_MONGO_DB_HOSTNAME);
			port = properties.getProperty("mongo.port", DEFAULT_MONGO_DB_PORT);
			userName = properties.getProperty("mongo.username", userName);
			password = properties.getProperty("mongo.password", password);
			collectionName = properties.getProperty("mongo.collection",
					DEFAULT_MONGO_DB_COLLECTION_NAME);
		}

		if (logger.isLoggable(Level.INFO)) {
			logger.info(String.format(
					"Connection to %s %s with credentials %s %s", hostname,
					port, userName, "******"));
		}

		List<ServerAddress> addresses = getServerAddresses(hostname, port);
		mongo = getMongo(addresses);

		DB database = getDatabase(mongo, databaseName);

		if (userName != null && userName.trim().length() > 0) {
			if (!database.authenticate(userName, password.toCharArray())) {
				throw new RuntimeException(
						"Unable to authenticate with MongoDB server.");
			}

			// Allow password to be GCed
			password = null;
		}

		setCollection(database.getCollection(collectionName));
		initialized = true;
	}

	protected void clearCollection() {
		if (logger.isLoggable(Level.WARNING)) {
			logger.warning("Going to clear collection : " + collectionName);
		}

		if (initialized) {
			WriteResult wr = getDbCollection().remove(new BasicDBObject());
			if (logger.isLoggable(Level.INFO)) {
				logger.info("Clear collection result :" + wr);
			}
		}
	}

	/*
	 * This method could be overridden to provide the DB instance from an
	 * existing connection.
	 */
	protected DB getDatabase(Mongo mongo, String databaseName) {
		return mongo.getDB(databaseName);
	}

	protected DBCollection getDbCollection() {
		return this.collection;
	}

	/*
	 * This method could be overridden to provide the Mongo instance from an
	 * existing connection.
	 */
	protected Mongo getMongo(List<ServerAddress> addresses) {
		if (addresses.size() == 1) {
			return new Mongo(addresses.get(0));
		} else {
			// Replica set
			return new Mongo(addresses);
		}
	}

	protected void close() {
		if (mongo != null) {
			collection = null;
			mongo.close();
		}
	}

	/**
	 * Note: this method is primarily intended for use by the unit tests.
	 * 
	 * @param collection
	 *            The MongoDB collection to use when logging events.
	 */
	public void setCollection(final DBCollection collection) {
		assert collection != null : "collection must not be null";

		this.collection = collection;
	}

	/**
	 * Returns a List of ServerAddress objects for each host specified in the
	 * hostname property. Returns an empty list if configuration is detected to
	 * be invalid, e.g.:
	 * <ul>
	 * <li>Port property doesn't contain either one port or one port per host</li>
	 * <li>After parsing port property to integers, there isn't either one port
	 * or one port per host</li>
	 * </ul>
	 * 
	 * @param hostname
	 *            Blank space delimited hostnames
	 * @param port
	 *            Blank space delimited ports. Must specify one port for all
	 *            hosts or a port per host.
	 * @return List of ServerAddresses to connect to
	 */
	private List<ServerAddress> getServerAddresses(String hostname, String port) {
		List<ServerAddress> addresses = new ArrayList<ServerAddress>();

		String[] hosts = hostname.split(" ");
		String[] ports = port.split(" ");

		if (ports.length != 1 && ports.length != hosts.length) {
			// errorHandler
			// .error("MongoDB appender port property must contain one port or a port per host",
			// null, ErrorCode.ADDRESS_PARSE_FAILURE);
		} else {
			List<Integer> portNums = getPortNums(ports);
			// Validate number of ports again after parsing
			if (portNums.size() != 1 && portNums.size() != hosts.length) {
				// error("MongoDB appender port property must contain one port or a valid port per host",
				// null, ErrorCode.ADDRESS_PARSE_FAILURE);
			} else {
				boolean onePort = (portNums.size() == 1);

				int i = 0;
				for (String host : hosts) {
					int portNum = (onePort) ? portNums.get(0) : portNums.get(i);
					try {
						addresses.add(new ServerAddress(host.trim(), portNum));
					} catch (UnknownHostException e) {
						// errorHandler
						// .error("MongoDB appender hostname property contains unknown host",
						// e, ErrorCode.ADDRESS_PARSE_FAILURE);
					}
					i++;
				}
			}
		}
		return addresses;
	}

	private List<Integer> getPortNums(String[] ports) {
		List<Integer> portNums = new ArrayList<Integer>();

		for (String port : ports) {
			try {
				Integer portNum = Integer.valueOf(port.trim());
				if (portNum < 0) {
					// errorHandler
					// .error("MongoDB appender port property can't contain a negative integer",
					// null, ErrorCode.ADDRESS_PARSE_FAILURE);
				} else {
					portNums.add(portNum);
				}
			} catch (NumberFormatException e) {
				// errorHandler
				// .error("MongoDB appender can't parse a port property value into an integer",
				// e, ErrorCode.ADDRESS_PARSE_FAILURE);
			}

		}

		return portNums;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.SubscriptionRegistry#addSubscription(org.
	 * ow2.play.governance.api.bean.Subscription)
	 */
	@Override
	@WebMethod
	public void addSubscription(Subscription subscription) {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Adding subscription : " + subscription);
		}

		if (subscription == null) {
			logger.warning("Can not add null subscription");
			return;
		}

		DBObject dbo = new BasicDBObject();
		dbo.put(ID_KEY, subscription.getId());
		dbo.put(PRODUCER_KEY, subscription.getProvider());
		dbo.put(SUBSCRIBER_KEY, subscription.getSubscriber());
		dbo.put(DATE_KEY, Long.toString(subscription.getDate()));
		dbo.put(STATUS_KEY, subscription.getStatus());

		if (subscription.getTopic() != null) {
			DBObject topic = new BasicDBObject();
			topic.put(TOPIC_NAME_KEY, subscription.getTopic().getName());
			topic.put(TOPIC_NS_KEY, subscription.getTopic().getNs());
			topic.put(TOPIC_PREFIX_KEY, subscription.getTopic().getPrefix());
			dbo.put(TOPIC_KEY, topic);
		} else {
			logger.warning("No topic set in the subscription, added to collection without it...");
		}

		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Adding subscription object to collection : " + dbo);
		}
		getDbCollection().insert(dbo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.governance.api.SubscriptionRegistry#getSubscriptions()
	 */
	@Override
	@WebMethod
	public List<Subscription> getSubscriptions() {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Getting subscritions");
		}

		DBCursor cursor = getDbCollection().find();
		Iterator<DBObject> iter = cursor.iterator();
		List<Subscription> result = new ArrayList<Subscription>();
		while (iter.hasNext()) {
			DBObject dbo = iter.next();
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("Extracting subscription : " + dbo);
			}

			Subscription s = toSubscription(dbo);
			if (s != null) {
				result.add(toSubscription(dbo));
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.SubscriptionRegistry#getSubscriptions(org
	 * .ow2.play.governance.api.bean.Subscription)
	 */
	@Override
	@WebMethod
	public List<Subscription> getSubscriptions(
			@WebParam(name = "filter") Subscription filter) {
		List<Subscription> result = null;

		if (filter == null) {
			return getSubscriptions();
		}
		result = new ArrayList<Subscription>();

		DBObject f = new BasicDBObject();
		if (filter.getDate() >= 0L) {
			f.put(DATE_KEY, Long.toString(filter.getDate()));
		}

		if (filter.getId() != null) {
			f.put(ID_KEY, filter.getId());
		}

		if (filter.getProvider() != null) {
			f.put(PRODUCER_KEY, filter.getProvider());
		}

		if (filter.getSubscriber() != null) {
			f.put(SUBSCRIBER_KEY, filter.getSubscriber());
		}
		
		if (filter.getStatus() != null) {
			f.put(STATUS_KEY, filter.getStatus());
		}
		
		// use the dot notation to search inner args
		if (filter.getTopic() != null) {
			if (filter.getTopic().getName() != null) {
				f.put(TOPIC_KEY + "." + TOPIC_NAME_KEY, filter.getTopic().getName());
			}
			if (filter.getTopic().getNs() != null) {
				f.put(TOPIC_KEY + "." + TOPIC_NS_KEY, filter.getTopic().getNs());
			}
			if (filter.getTopic().getPrefix() != null) {
				f.put(TOPIC_KEY + "." + TOPIC_PREFIX_KEY, filter.getTopic().getNs());
			}
		}

		logger.fine("Filtering with : " + f);

		DBCursor cursor = getDbCollection().find(f);
		Iterator<DBObject> iter = cursor.iterator();
		while (iter.hasNext()) {
			DBObject dbo = iter.next();
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("Extracting subscription : " + dbo);
			}

			Subscription s = toSubscription(dbo);
			if (s != null) {
				result.add(toSubscription(dbo));
			}
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.governance.api.SubscriptionRegistry#removeAll()
	 */
	@Override
	@WebMethod
	public List<Subscription> removeAll() {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Remove all the subscriptions from the collection");
		}
		WriteResult wr = getDbCollection().remove(new BasicDBObject());

		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Write result : " + wr);
		}
		return new ArrayList<Subscription>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.SubscriptionRegistry#remove(org.ow2.play.
	 * governance.api.bean.Subscription)
	 */
	@Override
	@WebMethod
	public boolean remove(Subscription subscription) {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Removing subscription : " + subscription);
		}

		// remove from the ID
		if (subscription == null || subscription.getId() == null) {
			return false;
		}

		DBObject filter = new BasicDBObject();
		filter.put(ID_KEY, subscription.getId());

		WriteResult wr = getDbCollection().remove(filter);
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Remove result : " + wr);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.SubscriptionRegistry#removeAllFromProvider
	 * (java.lang.String)
	 */
	@Override
	@WebMethod
	public List<Subscription> removeAllFromProvider(String provider) {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Remove subscriptions from provider : " + provider);
		}

		List<Subscription> result = new ArrayList<Subscription>();
		// remove from the ID
		if (provider == null) {
			return result;
		}

		DBObject filter = new BasicDBObject();
		filter.put(PRODUCER_KEY, provider);

		WriteResult wr = getDbCollection().remove(filter);

		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Remove result : " + wr);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.SubscriptionRegistry#removeAllFromConsumer
	 * (java.lang.String)
	 */
	@Override
	@WebMethod
	public List<Subscription> removeAllFromConsumer(String consumer) {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Remove all from consumer : " + consumer);
		}

		List<Subscription> result = new ArrayList<Subscription>();
		// remove from the ID
		if (consumer == null) {
			return result;
		}

		DBObject filter = new BasicDBObject();
		filter.put(SUBSCRIBER_KEY, consumer);

		WriteResult wr = getDbCollection().remove(filter);
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Remove result : " + wr);
		}

		return result;
	}

	protected Subscription toSubscription(DBObject dbo) {
		if (dbo == null) {
			return null;
		}
		Subscription s = new Subscription();
		s.setId(dbo.get(ID_KEY) == null ? "" : dbo.get(ID_KEY).toString());
		s.setSubscriber(dbo.get(SUBSCRIBER_KEY) == null ? "" : dbo.get(
				SUBSCRIBER_KEY).toString());
		s.setProvider(dbo.get(PRODUCER_KEY) == null ? "" : dbo
				.get(PRODUCER_KEY).toString());
		s.setDate(dbo.get(DATE_KEY) == null ? 0L : Long.parseLong(dbo.get(
				DATE_KEY).toString()));
		s.setStatus(dbo.get(STATUS_KEY) == null ? "" : dbo
				.get(STATUS_KEY).toString());

		if (dbo.get(TOPIC_KEY) != null
				&& dbo.get(TOPIC_KEY) instanceof DBObject) {
			DBObject to = (DBObject) dbo.get(TOPIC_KEY);
			Topic t = new Topic();
			t.setName(to.get(TOPIC_NAME_KEY) == null ? "" : to.get(
					TOPIC_NAME_KEY).toString());
			t.setNs(to.get(TOPIC_NS_KEY) == null ? "" : to.get(TOPIC_NS_KEY)
					.toString());
			t.setPrefix(to.get(TOPIC_PREFIX_KEY) == null ? "" : to.get(
					TOPIC_PREFIX_KEY).toString());
			s.setTopic(t);
		} else {
			// WARN
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("Topic is null");
			}
		}

		return s;
	}

}
