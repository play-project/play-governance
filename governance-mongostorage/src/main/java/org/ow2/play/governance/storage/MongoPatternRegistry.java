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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jws.WebMethod;

import org.ow2.petals.nosql.mongo.AbstractMongoService;
import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.PatternRegistry;
import org.ow2.play.governance.api.bean.Pattern;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * @author chamerling
 * 
 */
public class MongoPatternRegistry extends AbstractMongoService implements
		PatternRegistry {

	private static final Logger logger = Logger
			.getLogger(MongoPatternRegistry.class.getName());

	public MongoPatternRegistry() {
		super();
		setCollectionName("patterns");
		setDatabaseName("play");
	}

	/**
	 * Called by spring at startup
	 */
	@WebMethod(exclude = true)
	public void init() {
		initializeMongo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.PatternRegistry#put(org.ow2.play.governance
	 * .api.bean.Pattern)
	 */
	@Override
	public void put(Pattern pattern) throws GovernanceExeption {
		if (pattern == null) {
			throw new GovernanceExeption("Null pattern can not be added");
		}

		if (pattern.recordDate == null) {
			pattern.recordDate = System.currentTimeMillis() + "";
		}
		DBObject dbo = Helper.toDBO(pattern);
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Adding pattern object to collection : " + dbo);
		}
		getDbCollection().insert(dbo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.governance.api.PatternRegistry#delete(java.util.List)
	 */
	@Override
	public void delete(List<String> ids) throws GovernanceExeption {
		throw new GovernanceExeption("Not implemented");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.governance.api.PatternRegistry#clear()
	 */
	@Override
	public void clear() throws GovernanceExeption {
		logger.info("Removing all the patterns");
		clearCollection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.governance.api.PatternRegistry#get(java.lang.String)
	 */
	@Override
	public Pattern get(String id) throws GovernanceExeption {
		Pattern p = null;
		DBObject query = new BasicDBObject();
		query.put(Helper.ID_KEY, id);
		DBObject result = getCollection().findOne(query);
		
		if (result != null) {
			p = Helper.toPattern(result);
		}
		return p;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.governance.api.PatternRegistry#list(java.lang.String)
	 */
	@Override
	public List<Pattern> all() throws GovernanceExeption {
		List<Pattern> result = new ArrayList<Pattern>();

		DBCursor cursor = getDbCollection().find();
		Iterator<DBObject> iter = cursor.iterator();
		while (iter.hasNext()) {
			DBObject dbo = iter.next();
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("Extracting Pattern : " + dbo);
			}

			Pattern s = Helper.toPattern(dbo);
			if (s != null) {
				result.add(s);
			}
		}
		return result;
	}

}
