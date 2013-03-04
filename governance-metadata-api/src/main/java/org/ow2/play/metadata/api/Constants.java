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
package org.ow2.play.metadata.api;

/**
 * @author chamerling
 *
 */
public interface Constants {
	
	public static final String NS = "http://play.ow2.org/metadata";
	
	/**
	 * Metadata creation time
	 */
	public static final String CREATED_AT = NS + "/createdat";
	
	/**
	 * 
	 */
	public static final String GROUPS = NS + "/groups";
	
	/**
	 * The metadata can be linked to a complex event
	 */
	public static final String COMPLEX_EVENT = "http://www.play-project.eu/xml/ns/complexEvents";

	/**
	 * Use {@value #COMPLEX_EVENT}
	 */
	@Deprecated
	public static final String DSB_NEEDS_TO_SUBSCRIBE = "http://www.play-project.eu/xml/ns/dsbneedstosubscribe";
}
