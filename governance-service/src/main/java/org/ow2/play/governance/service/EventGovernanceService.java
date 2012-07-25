/**
 * 
 */
package org.ow2.play.governance.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jws.WebMethod;
import javax.xml.namespace.QName;
import javax.xml.ws.wsaddressing.W3CEndpointReference;

import org.ow2.play.governance.api.EventGovernance;
import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.governance.api.bean.Topic;
import org.ow2.play.governance.client.ServiceRegistry;
import org.ow2.play.metadata.api.MetaResource;
import org.ow2.play.metadata.api.service.MetadataService;
import org.ow2.play.service.registry.api.Registry;
import org.ow2.play.service.registry.api.RegistryException;
import org.petalslink.dsb.cxf.CXFHelper;


/**
 * @author chamerling
 * 
 */
public class EventGovernanceService implements EventGovernance {

	static Logger logger = Logger.getLogger(EventGovernanceService.class
			.getName());

	private Registry serviceRegistry;

	/**
	 * 
	 */
	public EventGovernanceService() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.governance.api.EventGovernance#loadResources(java.io.
	 * InputStream)
	 */
	@Override
	@WebMethod(exclude = true)
	public void loadResources(InputStream topicNameSpaceInputStream)
			throws GovernanceExeption {
		throw new GovernanceExeption("Not implemented");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.EventGovernance#createTopic(eu.playproject
	 * .governance.api.bean.Topic)
	 */
	@Override
	public void createTopic(Topic topic) throws GovernanceExeption {
		throw new GovernanceExeption("Not implemented");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.governance.api.EventGovernance#getTopics()
	 */
	@Override
	public List<Topic> getTopics() throws GovernanceExeption {
		logger.fine("Get topics from metadata service...");

		List<Topic> result = new ArrayList<Topic>();

		if (serviceRegistry == null) {
			throw new GovernanceExeption("Can not get the service regsitry");
		}

		String endpoint = null;
		try {
			endpoint = serviceRegistry
					.get(org.ow2.play.service.registry.api.Constants.METADATA);
		} catch (RegistryException e1) {
			e1.printStackTrace();
			throw new GovernanceExeption(e1);
		}

		if (endpoint == null) {
			throw new GovernanceExeption(
					"Can not get the metadata provider endpoint from the service registry");
		}

		logger.info("Getting topics from " + endpoint);

		MetadataService client = getMetadataClient(endpoint);
		// FIXME : Get all for now, we need to only get the resources which are
		// topics
		List<MetaResource> resources = null;
		try {
			resources = client.list();
		} catch (Exception e) {
			throw new GovernanceExeption(e);
		}

		if (resources != null) {
			for (MetaResource r : resources) {
				logger.info("Resource : " + r.getResource());
				// TODO : Get the prefix from the metaresources, for now we only
				// get the topic from the resource name where the name is
				// 'stream'

				if ("stream".equals(r.getResource().getName())) {
					Topic topic = new Topic();
					String ns = r
							.getResource()
							.getUrl()
							.substring(
									0,
									r.getResource().getUrl().lastIndexOf('/') + 1);
					String name = r
							.getResource()
							.getUrl()
							.substring(
									r.getResource().getUrl().lastIndexOf('/') + 1);
					topic.setName(name);
					topic.setNs(ns);
					
					// TODO : get the prefix from a metadata entry
					topic.setPrefix("s");
					result.add(topic);
				} else {
					logger.info("Not a topic");
				}
			}
		}

		return result;
	}

	/**
	 * @param endpoint
	 * @return
	 */
	private MetadataService getMetadataClient(String endpoint) {
		return CXFHelper.getClientFromFinalURL(endpoint, MetadataService.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.EventGovernance#findTopicsByElement(javax
	 * .xml.namespace.QName)
	 */
	@Override
	public List<QName> findTopicsByElement(QName element)
			throws GovernanceExeption {
		throw new GovernanceExeption("Not implemented");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.EventGovernance#findEventProducersByTopics
	 * (java.util.List)
	 */
	@Override
	public List<W3CEndpointReference> findEventProducersByTopics(
			List<QName> topics) throws GovernanceExeption {
		throw new GovernanceExeption("Not implemented");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.api.EventGovernance#findEventProducersByElements
	 * (java.util.List)
	 */
	@Override
	public List<W3CEndpointReference> findEventProducersByElements(
			List<QName> element) throws GovernanceExeption {
		throw new GovernanceExeption("Not implemented");
	}

	/**
	 * @param serviceRegistry
	 *            the serviceRegistry to set
	 */
	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

}
