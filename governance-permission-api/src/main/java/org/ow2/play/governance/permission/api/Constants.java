/**
 *
 * Copyright (c) 2013, Linagora
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
package org.ow2.play.governance.permission.api;

/**
 * @author chamerling
 *
 */
public interface Constants {

    public static final String ACL_PREFIX = "http://www.w3.org/ns/auth/acl";

    public static String PERMISSION_RESOURCE = "permission";
	
	public static String RESOURCE_NS = "http://permissions.event-processing.org/id/";
	
	public static String ACCESS_TO = "http://www.w3.org/ns/auth/acl#accessTo";
	
	public static String AGENT = "http://www.w3.org/ns/auth/acl#agent";

	public static String MODE = "http://www.w3.org/ns/auth/acl#mode";
	
	public static String TYPE_KEY = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";

	public static String TYPE_VALUE = "http://www.w3.org/ns/auth/acl#Authorization";
	
	public static String WRITE = "http://www.w3.org/ns/auth/acl#Write";
	
	public static String READ = "http://www.w3.org/ns/auth/acl#Read";

	public static String SUBSCRIBE = "http://docs.oasis-open.org/wsn/b-2/Subscribe";

	public static String NOTIFY = "http://docs.oasis-open.org/wsn/b-2/Notify";

}
