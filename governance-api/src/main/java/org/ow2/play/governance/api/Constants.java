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
package org.ow2.play.governance.api;

import org.ow2.play.metadata.api.Data;
import org.ow2.play.metadata.api.Type;

/**
 * @author chamerling
 * 
 */
public interface Constants {

	public static String STREAM_RESOURCE_NAME = "stream";

	public static String SUBCRIPTION_RESOURCE_NAME = "subscription";
	
	public static String SUBCRIPTION_PREFIX_URL = "http://subscriptions.play.ow2.org/";
	
	public static String SUBCRIPTION_PATTERN = SUBCRIPTION_PREFIX_URL + "%s#" + SUBCRIPTION_RESOURCE_NAME;

	public static String PATTERN_RESOURCE_NAME = "pattern";
	
	public static String PATTERN_PREFIX_URL = "http://patterns.play.ow2.org/";
	
	public static String PATTERN_PATTERN = PATTERN_PREFIX_URL + "%s#" + PATTERN_RESOURCE_NAME;

	public static String QNAME_PREFIX_URL = "http://www.play-project.eu/xml/ns/topic/prefix";

	public static String QNAME_NS_URL = "http://www.play-project.eu/xml/ns/topic/ns";

	public static String QNAME_LOACALPART_URL = "http://www.play-project.eu/xml/ns/topic/localpart";

	public static String TOPIC = "http://www.play-project.eu/xml/ns/topic";

	public static String TOPIC_CREATED_AT = "http://www.play-project.eu/xml/ns/topic/creation/timestamp";

	public static String TOPIC_CREATED_BY = "http://www.play-project.eu/xml/ns/topic/creation/by";

	public static String TOPIC_MODE = "http://www.play-project.eu/xml/ns/topic/mode";

	public static String TOPIC_ACTIVE = "http://www.play-project.eu/xml/ns/topic/active";

	public static String TOPIC_MODE_PUBLISHER = "publisher";

	public static String TOPIC_MODE_SUBSCRIBER = "subscriber";

	public static Data BOOLEAN_TRUE = new Data(Type.LITERAL, "true");
	
	public static Data BOOLEAN_FALSE = new Data(Type.LITERAL, "false");
	
	public static final String DEFAULT_PREFIX = "s";

}
