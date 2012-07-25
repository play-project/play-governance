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
package org.ow2.play.governance.api.bean;

import javax.xml.bind.annotation.XmlRootElement;

import org.ow2.play.governance.api.bean.Topic;

/**
 * @author chamerling
 * 
 */
@XmlRootElement
public class Subscription {

	String id;
	String subscriber;
	String provider;
	Topic topic;
	long date;

	/**
	 * 
	 * @param id
	 * @param subscriber
	 * @param provider
	 * @param topic
	 */
	public Subscription(String id, String subscriber, String provider,
			Topic topic, long date) {
		super();
		this.id = id;
		this.subscriber = subscriber;
		this.provider = provider;
		this.topic = topic;
		this.date = date;
	}

	/**
	 * 
	 */
	public Subscription() {
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the subscriber
	 */
	public String getSubscriber() {
		return subscriber;
	}

	/**
	 * @param subscriber
	 *            the subscriber to set
	 */
	public void setSubscriber(String subscriber) {
		this.subscriber = subscriber;
	}

	/**
	 * @return the provider
	 */
	public String getProvider() {
		return provider;
	}

	/**
	 * @param provider
	 *            the provider to set
	 */
	public void setProvider(String provider) {
		this.provider = provider;
	}

	/**
	 * @return the topic
	 */
	public Topic getTopic() {
		return topic;
	}

	/**
	 * @param topic
	 *            the topic to set
	 */
	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	/**
	 * 
	 * @param date
	 */
	public void setDate(long date) {
		this.date = date;
	}

	/**
	 * 
	 * @return
	 */
	public long getDate() {
		return date;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Subscription [id=" + id + ", subscriber=" + subscriber
				+ ", provider=" + provider + ", topic=" + topic + ", date="
				+ date + "]";
	}

}
