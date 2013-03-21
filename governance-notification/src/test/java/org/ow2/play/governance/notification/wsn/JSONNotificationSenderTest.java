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
public class JSONNotificationSenderTest {

	@Test
	public void testCreateMessage() {
		JSONNotificationSender sender = new JSONNotificationSender();
		Document doc = null;
		try {
			doc = sender.translate("{'foo' : 'bar'}");
		} catch (GovernanceExeption e) {
			org.junit.Assert.fail(e.getMessage());
		}
		try {
			XMLHelper.writeDocument(doc, System.out);
		} catch (TransformerException e) {
			org.junit.Assert.fail(e.getMessage());
		}
		
		// TODO : Doc must contain the JSON stuff
	}

	@Test
	public void testCreateMessageNull() {
		JSONNotificationSender sender = new JSONNotificationSender();
		try {
			sender.translate(null);
			org.junit.Assert.fail();

		} catch (GovernanceExeption e) {
			e.printStackTrace();
		}
	}

}
