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
package org.ow2.play.governance.bootstrap.client;

import java.io.InputStream;
import java.util.List;

import javax.jws.WebMethod;
import javax.xml.namespace.QName;
import javax.xml.ws.wsaddressing.W3CEndpointReference;

import org.ow2.play.governance.api.EventGovernance;
import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.bean.Topic;
import org.ow2.play.governance.bootstrap.api.GovernanceClient;
import org.ow2.play.service.registry.api.Constants;
import org.ow2.play.service.registry.api.RegistryException;
import org.petalslink.dsb.cxf.CXFHelper;


/**
 * Governance client used to retrieve topics and metadata
 * 
 * @author chamerling
 * 
 */
public class GovernanceClientImpl implements GovernanceClient {

	private EventGovernance eventClient = null;

	private ServiceRegistry serviceRegistry = null;

	private EventGovernance getEventClient() {

		if (eventClient == null) {
			try {
				eventClient = CXFHelper.getClientFromFinalURL(
						serviceRegistry.get(Constants.GOVERNANCE),
						EventGovernance.class);
			} catch (RegistryException e) {
				e.printStackTrace();
			}
		}
		return eventClient;
	}

	@Override
	public String createTopic(Topic topic) throws GovernanceExeption {
		throw new GovernanceExeption(
				"This method is not implemented in the client");
	}

	@Override
	public List<W3CEndpointReference> findEventProducersByElements(
			List<QName> arg0) throws GovernanceExeption {
		throw new GovernanceExeption(
				"This method is not implemented in the client");
	}

	@Override
	public List<W3CEndpointReference> findEventProducersByTopics(
			List<QName> arg0) throws GovernanceExeption {
		throw new GovernanceExeption(
				"This method is not implemented in the client");
	}

	@Override
	public List<QName> findTopicsByElement(QName arg0)
			throws GovernanceExeption {
		throw new GovernanceExeption(
				"This method is not implemented in the client");
	}

	@Override
	public List<Topic> getTopics() throws GovernanceExeption {
		return getEventClient().getTopics();
	}

	@Override
	public void loadResources(InputStream arg0) throws GovernanceExeption {
		throw new GovernanceExeption(
				"This method is not implemented in the client");
	}

	@Override
	public boolean deleteTopic(Topic topic) throws GovernanceExeption {
		throw new GovernanceExeption(
				"This method is not implemented in the client");
	}
	
	@Override
	public String createPublisherTopic(Topic topic) throws GovernanceExeption {
		throw new GovernanceExeption(
				"This method is not implemented in the client");
	}

	@Override
	public String createSubscriberTopic(Topic topic) throws GovernanceExeption {
		throw new GovernanceExeption(
				"This method is not implemented in the client");
	}

	/**
	 * @param serviceRegistry
	 *            the serviceRegistry to set
	 */
	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}


}
