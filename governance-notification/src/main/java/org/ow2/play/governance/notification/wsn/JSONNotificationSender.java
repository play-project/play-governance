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

import org.ow2.play.governance.api.GovernanceExeption;
import org.w3c.dom.Document;

import com.ebmwebsourcing.easycommons.xml.DocumentBuilders;

import eu.play_project.play_commons.eventformat.EventFormatHelpers;

/**
 * Send JSON message to the WSN notify service
 * 
 * @author chamerling
 * 
 */
public class JSONNotificationSender extends AbstractSender {

	public JSONNotificationSender() {
		super();
	}

	@Override
	protected Document translate(String message) throws GovernanceExeption {
		if (message == null) {
			throw new GovernanceExeption("Null input");
		}

		Document doc = DocumentBuilders.newDocument();
		doc.appendChild(doc.adoptNode(EventFormatHelpers
				.wrapWithDomNativeMessageElement(message).cloneNode(true)));
		return doc;
	}

}
