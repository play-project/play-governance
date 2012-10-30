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

import org.ow2.play.governance.api.bean.Pattern;
import org.ow2.play.governance.api.bean.Subscription;
import org.ow2.play.governance.api.bean.Topic;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * @author chamerling
 * 
 */
public class Helper {

	public static final String ID_KEY = "id";
	public static final String SUBSCRIBER_KEY = "subscriber";
	public static final String PRODUCER_KEY = "producer";
	public static final String TOPIC_KEY = "topic";
	public static final String DATE_KEY = "date";
	public static final String STATUS_KEY = "status";

	public static final String TOPIC_NAME_KEY = "name";
	public static final String TOPIC_NS_KEY = "ns";
	public static final String TOPIC_PREFIX_KEY = "prefix";

	public static final String CONTENT_KEY = "content";
	public static final String NAME_KEY = "name";

	private Helper() {
	}

	protected static final DBObject toDBO(Subscription subscription) {
		if (subscription == null) {
			return null;
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
		}

		return dbo;
	}

	protected static final Subscription toSubscription(DBObject dbo) {
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
		s.setStatus(dbo.get(STATUS_KEY) == null ? "" : dbo.get(STATUS_KEY)
				.toString());

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
		}
		return s;
	}

	protected static final DBObject toDBO(Pattern pattern) {
		if (pattern == null) {
			return null;
		}

		DBObject dbo = new BasicDBObject();
		dbo.put(ID_KEY, pattern.id);
		dbo.put(NAME_KEY, pattern.name);
		dbo.put(CONTENT_KEY, pattern.content);
		dbo.put(DATE_KEY,
				pattern.recordDate == null ? "" + System.currentTimeMillis()
						: pattern.recordDate);

		return dbo;
	}

	protected static final Pattern toPattern(DBObject dbo) {
		if (dbo == null) {
			return null;
		}

		Pattern p = new Pattern();
		p.id = dbo.get(ID_KEY) == null ? "" : dbo.get(ID_KEY).toString();
		p.content = dbo.get(CONTENT_KEY) == null ? "" : dbo.get(CONTENT_KEY)
				.toString();
		p.name = dbo.get(NAME_KEY) == null ? "" : dbo.get(NAME_KEY).toString();
		p.recordDate = dbo.get(DATE_KEY) == null ? "" : dbo.get(DATE_KEY)
				.toString();

		return p;
	}
}
