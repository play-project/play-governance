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
package org.ow2.play.service.registry.api;

/**
 * Defines a minimal list of constants required by the Play platform components.
 * 
 * @author chamerling
 * 
 */
public interface Constants {
	
	/**
	 * Used to notify ie send WSN notify message to this endpoint
	 */
	public static final String DSB_CONSUMER = "org.ow2.play.dsb.wsn.consumer";
	
	/**
	 * Used to subscribe ie send WSN subscribe message to this endpoint
	 */
	public static final String DSB_PRODUCER = "org.ow2.play.dsb.wsn.producer";

	/**
	 * Used to unsubscribe ie send unsubscribe message to the endpoint
	 */
	public static final String DSB_SUBSCRIPTION = "org.ow2.play.dsb.wsn.subscription";

	
	/**
	 * The DSB TopicAware business service
	 */
	public static final String DSB_BUSINESS_TOPIC_MANAGEMENT = "org.ow2.play.dsb.topic.business.management";
	
	/**
	 * Where to get DSB notification stats
	 */
	public static final String DSB_BUSINESS_TOPIC_STATS = "org.ow2.play.dsb.topic.business.stats";

	/**
	 * Base endpoint
	 */
	public static final String MONITORING_DSB_BASE = "org.ow2.play.monitoring.dsb.base";

	/**
	 * Used by the DSB to send JSON data about messages
	 */
	public static final String MONITORING_DSB_WSN = "org.ow2.play.monitoring.dsb.wsn";

	/**
	 * 
	 */
	public static final String MONITORING_DSB_SERVICES = "org.ow2.play.monitoring.dsb.services";

	/**
	 * Monitoring BASE URL
	 */
	public static final String MONITORING_BASE = "org.ow2.play.monitoring.base";

	public static final String METADATA = "org.ow2.play.metadata";

	public static final String DSB_TO_EC_EC = "org.ow2.play.dsb2ec.eventcloud";

	public static final String DSB_TO_EC_EC_SUBSCRIBER = "org.ow2.play.dsb2ec.subscriber";

	public static final String EC_TO_DSB_DSB = "org.ow2.play.ec2dsb.dsb";

	public static final String EC_TO_DSB_EC = "org.ow2.play.ec2dsb.eventcloud";

	public static final String TOPIC = "org.ow2.play.topic";
	
	public static final String GOVERNANCE = "org.ow2.play.governance";
	
	public static final String GOVERNANCE_SUBSCRIPTION_REGISTRY = "org.ow2.play.governance.subscription.registry";
	
	public static final String GOVERNANCE_SUBSCRIPTIONMANAGEMENT_SERVICE = "org.ow2.play.governance.subscription.management";

	public static final String GOVERNANCE_BOOTSUBSCRIPTION_REGISTRY = "org.ow2.play.governance.subscription.bootregistry";

	public static final String GOVERNANCE_SUBSCRIPTION_SERVICE = "org.ow2.play.governance.subscription.service";

	public static final String GOVERNANCE_TOPICREGISTRY_SERVICE = "org.ow2.play.governance.topic.registry";
	
	public static final String GOVERNANCE_PATTERN_SIMPLE_SERVICE = "org.ow2.play.governance.pattern.simple";

	public static final String GOVERNANCE_PATTERN_COMPLEX_SERVICE = "org.ow2.play.governance.pattern.complex";

	public static final String GOVERNANCE_PATTERN_SERVICE = "org.ow2.play.governance.pattern.service";

	public static final String GOVERNANCE_PATTERN_REGISTRY = "org.ow2.play.governance.pattern.registry";
	
	public static final String GOVERNANCE_EC_SERVICE = "org.ow2.play.governance.ec.service";

	public static final String QUERY_DISPATCH_SERVICE = "org.ow2.play.querydispatcher";
	
	public static final String GOVERNANCE_BOOTSTRAP_REST = "org.ow2.play.governance.bootstrap.rest";
	
}
