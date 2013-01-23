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
package org.ow2.play.metadata.mongodb;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jws.WebMethod;

import org.bson.types.BasicBSONList;
import org.ow2.play.metadata.api.Constants;
import org.ow2.play.metadata.api.Data;
import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.Metadata;
import org.ow2.play.metadata.api.MetadataException;
import org.ow2.play.metadata.api.Resource;
import org.ow2.play.metadata.api.Type;
import org.ow2.play.metadata.api.service.Initializable;
import org.ow2.play.metadata.api.service.MetadataService;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.WriteResult;

/**
 * @author chamerling
 * 
 */
public class MongoMetadataServiceImpl implements MetadataService, Initializable {

	private final static String DEFAULT_MONGO_DB_HOSTNAME = "localhost";
	private final static String DEFAULT_MONGO_DB_PORT = "27017";
	private final static String DEFAULT_MONGO_DB_DATABASE_NAME = "play";
	private final static String DEFAULT_MONGO_DB_COLLECTION_NAME = "metadata";

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
			.getLogger(MongoMetadataServiceImpl.class.getName());

	private BSONAdapter bsonAdapter;

	/**
	 * 
	 */
	public MongoMetadataServiceImpl() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.metadata.api.service.Initializable#init(java.util.Properties
	 * )
	 */
	@Override
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

	@Override
	public void clear() throws MetadataException {
		logger.info("Got a clear call");
		checkInitialized();
		clearCollection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.metadata.api.service.MetadataService#addMetadata(org.ow2
	 * .play.metadata.api.Resource, org.ow2.play.metadata.api.Metadata)
	 */
	@Override
	@WebMethod
	public void addMetadata(Resource resource, Metadata metadata)
			throws MetadataException {

		if (logger.isLoggable(Level.INFO))
			logger.info(String.format("Adding metdata %s to resource %s",
					metadata, resource));

		checkInitialized();

		DBObject o = findFirst(resource);
		if (o != null) {

			if (logger.isLoggable(Level.FINE))
				logger.fine("Resource already exists, Add metadata to the current entry");
			// update the current record

			Object meta = o.get("metadata");
			if (meta != null && meta instanceof BasicBSONList) {
				DBObject metabson = bsonAdapter.createBSON(metadata);
				BasicBSONList list = (BasicBSONList) meta;
				list.add(metabson);
				this.collection.save(o);
			} else {
				if (logger.isLoggable(Level.WARNING))
					logger.warning("Can not find the list to add metadata to...");
			}
		} else {
			// create
			if (logger.isLoggable(Level.INFO))
				logger.info("Resource does not exists, Create a new meta resource entry in the DB");

			List<Metadata> list = new ArrayList<Metadata>();
			if (metadata != null) {
				list.add(metadata);
			}
			MetaResource metaResource = new MetaResource(resource, list);

			DBObject bson = bsonAdapter.createBSON(metaResource);
			try {
				this.collection.insert(bson);
			} catch (MongoException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.metadata.api.service.MetadataService#setMetadata(org.ow2
	 * .play.metadata.api.Resource, org.ow2.play.metadata.api.Metadata)
	 */
	@Override
	public void setMetadata(Resource resource, Metadata metadata)
			throws MetadataException {
		if (logger.isLoggable(Level.INFO))
			logger.info(String.format("Setting metdata %s to resource %s",
					metadata, resource));

		checkInitialized();

		DBObject o = findFirst(resource);
		if (o != null) {
			if (logger.isLoggable(Level.FINE))
				logger.fine("Resource already exists, Add metadata to the current entry");
			// update the current record

			Object meta = o.get("metadata");
			if (meta != null && meta instanceof BasicBSONList) {
				BasicBSONList list = (BasicBSONList) meta;
				boolean found = false;
				
				for (Object object : list) {
					if (object != null && object instanceof DBObject) {
						DBObject entry = (DBObject) object;
						if (entry.get("name") != null && entry.get("name").toString().equals(metadata.getName())) {
							found = true;
							// update the entry with the input value
							BasicBSONList bsl = new BasicBSONList();
							for (Data data : metadata.getData()) {
								bsl.add(bsonAdapter.createBSON(data));
							}
							entry.put("data", bsl);
						}
					}
				}
				
				if (!found) {
					// create the metadata and add it to the resource
					DBObject dbo = bsonAdapter.createBSON(metadata);
					list.add(dbo);
				}
				
				// will update the entry
				this.collection.save(o);
			} else {
				if (logger.isLoggable(Level.WARNING))
					logger.warning("Can not find the list to add metadata to...");
			}
		} else {
			// do bot create
			throw new MetadataException("Can not add a metadata to a resource that does not exists");
		}	
	}
	
	/* (non-Javadoc)
	 * @see org.ow2.play.metadata.api.service.MetadataService#create(org.ow2.play.metadata.api.MetaResource)
	 */
	@Override
	public boolean create(MetaResource metaResource) throws MetadataException {
		boolean result = true;
		
		if (logger.isLoggable(Level.INFO)) {
			logger.info("Create metaresource " + metaResource);
		}
		
		if (metaResource == null || metaResource.getResource() == null) {
			logger.warning("Can not create a null resource");
			throw new MetadataException("Can not create a null resource...");
		}
		
		// add creation date
		metaResource.getMetadata().add(
				new Metadata(Constants.CREATED_AT, new Data(Type.LITERAL, ""
						+ System.currentTimeMillis())));
		
		// will create the resource if it does not exists, else add metas
		checkInitialized();
		DBObject o = findFirst(metaResource.getResource());
		
		if (o == null) {
			// create from root
			DBObject bson = bsonAdapter.createBSON(metaResource);
			try {
				this.collection.insert(bson);
			} catch (MongoException e) {
				throw new MetadataException(e);
			}
		} else {
			// we do not accept operation if the resource already exists in the
			// repository
			// if needed, use the #addMetadata method.
			final String message = "The resource already exists, can not be created. Use #addMetadata if you want to populate resource";
			logger.warning(message);
			throw new MetadataException(message);
		}
		
		
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.metadata.api.service.MetadataService#removeMetadata(org.
	 * ow2.play.metadata.api.Resource, org.ow2.play.metadata.api.Metadata)
	 */
	@Override
	@WebMethod
	public void removeMetadata(Resource resource, Metadata metadata)
			throws MetadataException {
		throw new MetadataException("Not implemented");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.metadata.api.service.MetadataService#getMetaData(org.ow2
	 * .play.metadata.api.Resource)
	 */
	@Override
	@WebMethod
	public List<Metadata> getMetaData(Resource resource)
			throws MetadataException {
		checkInitialized();

		List<Metadata> result = new ArrayList<Metadata>();

		DBObject dbo = findFirst(resource);
		if (dbo != null) {
			Object o = dbo.get("metadata");
			if (o != null && o instanceof BasicDBList) {
				BasicDBList metalist = (BasicDBList) o;
				for (Object object : metalist) {
					DBObject entry = (DBObject) object;
					Metadata md = new Metadata();
					md.setName(entry.get("name").toString());

					Object data = entry.get("data");
					if (data != null && data instanceof BasicDBList) {
						BasicDBList list = (BasicDBList) data;
						for (Object object2 : list) {
							md.getData().add(
									new Data(((DBObject) object2).get("type")
											.toString(), ((DBObject) object2)
											.get("value").toString()));
						}
					}
					result.add(md);
				}
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.metadata.api.service.MetadataService#getMetadataValue(org
	 * .ow2.play.metadata.api.Resource, java.lang.String)
	 */
	@Override
	@WebMethod
	public Metadata getMetadataValue(Resource resource, String key)
			throws MetadataException {
		checkInitialized();

		Metadata result = null;

		// FIXME : How to get just a leaf?
		DBObject dbo = this.findFirst(resource);
		if (dbo != null) {
			MetaResource mr = bsonAdapter.readMetaResource(dbo);
			List<Metadata> list = mr.getMetadata();
			// TODO = Google guava have such list filter...
			Iterator<Metadata> iter = list.iterator();
			boolean found = false;
			while (iter.hasNext() && !found) {
				Metadata metadata = iter.next();
				if (metadata.getName() != null
						&& metadata.getName().equals(key)) {
					found = true;
					result = metadata;
				}
			}
		}

		return result;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.metadata.api.service.MetadataService#deleteMetaData(org.
	 * ow2.play.metadata.api.Resource)
	 */
	@Override
	@WebMethod
	public boolean deleteMetaData(Resource resource) throws MetadataException {
		checkInitialized();

		boolean deleted = false;
		DBObject o = findFirst(resource);
		if (o != null) {
			logger.fine("Deleting the associated resource " + resource);
			// TODO : How to delete entries in a resource...
			deleted = false;
		} else {
			logger.fine("Can not delete the resource, not found in the DB...");
		}
		return deleted;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.metadata.api.service.MetadataService#getResoucesWithMeta
	 * (java.util.List)
	 */
	@Override
	@WebMethod
	public List<MetaResource> getResoucesWithMeta(List<Metadata> include)
			throws MetadataException {
		throw new MetadataException("Not implemented");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.metadata.api.service.MetadataService#list()
	 */
	@Override
	@WebMethod
	public List<MetaResource> list() throws MetadataException {
		checkInitialized();

		List<MetaResource> result = new ArrayList<MetaResource>();

		DBCursor cursor = this.collection.find();
		Iterator<DBObject> iter = cursor.iterator();
		while (iter.hasNext()) {
			DBObject dbObject = iter.next();

			if (dbObject != null) {
				if (logger.isLoggable(Level.FINE)) {
					logger.fine(dbObject.toString());
				}
				MetaResource mr = bsonAdapter.readMetaResource(dbObject);
				if (mr != null) {
					result.add(mr);
				}
			} else {
				if (logger.isLoggable(Level.FINE)) {
					logger.fine("Null object, not added");
				}
			}
		}

		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.ow2.play.metadata.api.service.MetadataService#exists(org.ow2.play.metadata.api.Resource)
	 */
	@Override
	public boolean exists(Resource resource) throws MetadataException {
		if (resource == null || resource.getName() == null || resource.getUrl() == null) {
			throw new MetadataException("Can not search a null object...");
		}
		return (findFirst(resource) != null);
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

	protected DBObject findFirst(Resource resource) {
		BasicDBObject query = new BasicDBObject();
		BasicDBObject resourceObject = new BasicDBObject();
		resourceObject.put("name", resource.getName());
		resourceObject.put("url", resource.getUrl());
		query.put("resource", resourceObject);

		return this.collection.findOne(query);
	}

	protected List<DBObject> findAll(Resource resource) {
		List<DBObject> result = new ArrayList<DBObject>();

		BasicDBObject query = new BasicDBObject();
		BasicDBObject resourceObject = new BasicDBObject();
		resourceObject.put("name", resource.getName());
		resourceObject.put("url", resource.getUrl());
		query.put("resource", resourceObject);

		DBCursor cursor = this.collection.find(query);
		Iterator<DBObject> iter = cursor.iterator();
		while (iter.hasNext()) {
			result.add(iter.next());
		}
		return result;
	}
	
	protected void clearCollection() {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Remove all objects from the collection");
		}
		WriteResult wr = getDbCollection().remove(new BasicDBObject());

		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Write result : " + wr);
		}
	}

	protected void checkInitialized() throws MetadataException {
		if (!initialized) {
			throw new MetadataException(
					"MongoDB has not been initialized, call #init() first");
		}
	}

	/**
	 * @param bsonAdapter
	 *            the bsonAdapter to set
	 */
	public void setBsonAdapter(BSONAdapter bsonAdapter) {
		this.bsonAdapter = bsonAdapter;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
}
