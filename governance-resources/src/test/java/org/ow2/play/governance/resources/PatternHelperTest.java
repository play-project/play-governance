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

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.ow2.play.governance.api.Constants;

/**
 * @author chamerling
 * 
 */
public class PatternHelperTest {

	@Test
	public void testGenerateURI() {
		assertEquals(Constants.PATTERN_PREFIX_URL + "123#"
				+ Constants.PATTERN_RESOURCE_NAME,
				PatternHelper.getPatternURI("123"));
	}

	@Test
	public void testGetID() {
		assertEquals(
				"123",
				PatternHelper.getPatternID(Constants.PATTERN_PREFIX_URL
						+ "123#" + Constants.PATTERN_RESOURCE_NAME));
	}
}
