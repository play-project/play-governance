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
package org.ow2.play.governance.resources;

import org.ow2.play.governance.api.Constants;
import org.ow2.play.governance.api.bean.Pattern;

/**
 * @author chamerling
 * 
 */
public class PatternHelper {

	private PatternHelper() {
	}

	public static String get(Pattern p) {
		return getPatternURI(p.id);
	}

	public static String getPatternURI(String id) {
		return String.format(Constants.PATTERN_PATTERN, id);
	}

	public static String getPatternID(String resourceURI) {
		if (!isPattern(resourceURI)) {
			return "";
		}
		return resourceURI.substring(
				Constants.PATTERN_PREFIX_URL.length(),
				resourceURI.lastIndexOf("#"
						+ Constants.PATTERN_RESOURCE_NAME));
	}

	public static boolean isPattern(String resourceURI) {
		return resourceURI != null
				&& resourceURI.endsWith("#"
						+ Constants.PATTERN_RESOURCE_NAME);
	}

}
