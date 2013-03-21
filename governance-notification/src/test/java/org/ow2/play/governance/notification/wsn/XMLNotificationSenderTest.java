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
package org.ow2.play.governance.notification.wsn;

import javax.xml.transform.TransformerException;

import org.junit.Test;
import org.ow2.play.governance.api.GovernanceExeption;
import org.w3c.dom.Document;

import com.ebmwebsourcing.easycommons.xml.XMLHelper;

/**
 * @author chamerling
 * 
 */
public class XMLNotificationSenderTest {

	@Test
	public void testCreateMessageOK() {
		XMLNotificationSender sender = new XMLNotificationSender();
		Document doc = null;
		try {
			doc = sender.translate("<foo>bar</foo>");
			org.junit.Assert.assertNotNull(doc);

		} catch (GovernanceExeption e) {
			org.junit.Assert.fail(e.getMessage());

		}
		try {
			XMLHelper.writeDocument(doc, System.out);
		} catch (TransformerException e) {
		}
	}

	@Test
	public void testCreateMessageNull() {
		XMLNotificationSender sender = new XMLNotificationSender();
		try {
			sender.translate(null);
			org.junit.Assert.fail();

		} catch (GovernanceExeption e) {
			e.printStackTrace();
		}
	}

}
