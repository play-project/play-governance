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
package org.ow2.play.governance.ec;

import java.util.List;
import java.util.logging.Logger;

import org.ow2.play.governance.api.EventCloudsManagementWs;
import org.ow2.play.governance.api.GovernanceExeption;
import org.ow2.play.service.registry.api.Constants;
import org.ow2.play.service.registry.api.Registry;
import org.ow2.play.service.registry.api.RegistryException;
import org.petalslink.dsb.cxf.CXFHelper;

import fr.inria.eventcloud.webservices.api.EventCloudsManagementWsApi;


/**
 * @author chamerling
 * 
 */
public class EventCloudsManagementWsImpl implements EventCloudsManagementWs {

    private Registry registry;

    private static Logger logger = Logger.getLogger(EventCloudsManagementWsImpl.class.getName());

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRegistryEndpointUrl() throws GovernanceExeption {
        logger.info("getRegistryEndpointUrl");
        return getCloudManagementServiceApi().getRegistryEndpointUrl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean createEventCloud(String streamUrl) throws GovernanceExeption {
        logger.info("Create Event Cloud " + streamUrl);
        return getCloudManagementServiceApi().createEventCloud(streamUrl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCreated(String streamUrl) throws GovernanceExeption {
        logger.info("Is Created Event Cloud " + streamUrl);
        return getCloudManagementServiceApi().isCreated(streamUrl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getEventCloudIds() throws GovernanceExeption {
        logger.info("getEventCloudIds");
        return getCloudManagementServiceApi().getEventCloudIds();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean destroyEventCloud(String streamUrl) throws GovernanceExeption {
        logger.info("Destroy Event Cloud " + streamUrl);
        return getCloudManagementServiceApi().destroyEventCloud(streamUrl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String deployPublishWsnService(String streamUrl) throws GovernanceExeption {
        logger.info("Deploy Publish WSN Service " + streamUrl);
        return getCloudManagementServiceApi().deployPublishWsnService(streamUrl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String deploySubscribeWsnService(String streamUrl) throws GovernanceExeption {
        logger.info("Deploy Subscribe WSN Service " + streamUrl);
        return getCloudManagementServiceApi().deploySubscribeWsnService(streamUrl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String deployPublishWsProxy(String streamUrl) throws GovernanceExeption {
        logger.info("Deploy Publish WS Proxy " + streamUrl);
        return getCloudManagementServiceApi().deployPublishWsProxy(streamUrl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String deploySubscribeWsProxy(String streamUrl) throws GovernanceExeption {
        logger.info("Deploy Subscribe WS Proxy " + streamUrl);
        return getCloudManagementServiceApi().deploySubscribeWsProxy(streamUrl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String deployPutGetWsProxy(String streamUrl) throws GovernanceExeption {
        logger.info("Deploy Put/Get Proxy " + streamUrl);
        return getCloudManagementServiceApi().deployPutGetWsProxy(streamUrl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getPublishWsnServiceEndpointUrls(String streamUrl) throws GovernanceExeption {
        logger.info("Get Publish WSN Service endpoints " + streamUrl);
        return getCloudManagementServiceApi().getPublishWsnServiceEndpointUrls(streamUrl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getSubscribeWsnServiceEndpointUrls(String streamUrl) throws GovernanceExeption {
        logger.info("Get Subscribe WSN Service endpoints " + streamUrl);
        return getCloudManagementServiceApi().getSubscribeWsnServiceEndpointUrls(streamUrl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getPublishWsProxyEndpointUrls(String streamUrl) throws GovernanceExeption {
        logger.info("Get Publish WS Proxy endpoints " + streamUrl);
        return getCloudManagementServiceApi().getPublishWsProxyEndpointUrls(streamUrl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getSubscribeWsProxyEndpointUrls(String streamUrl) throws GovernanceExeption {
        logger.info("Get Subscribe WS Proxy endpoints " + streamUrl);
        return getCloudManagementServiceApi().getSubscribeWsProxyEndpointUrls(streamUrl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getPutGetWsProxyEndpointUrls(String streamUrl) throws GovernanceExeption {
        logger.info("Get Put/Get Proxy endpoints " + streamUrl);
        return getCloudManagementServiceApi().getPutGetWsProxyEndpointUrls(streamUrl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean destroyPublishWsnService(String publishWsnEndpointUrl) throws GovernanceExeption {
        logger.info("Destroy Publish WSN Service " + publishWsnEndpointUrl);
        return getCloudManagementServiceApi().destroyPublishWsnService(publishWsnEndpointUrl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean destroySubscribeWsnService(String subscribeWsnEndpointUrl) throws GovernanceExeption {
        logger.info("Destroy Subscribe WSN Service " + subscribeWsnEndpointUrl);
        return getCloudManagementServiceApi().destroySubscribeWsnService(subscribeWsnEndpointUrl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean destroyPublishWsProxy(String publishWsProxyEndpointUrl) throws GovernanceExeption {
        logger.info("Destroy Publish WS Proxy " + publishWsProxyEndpointUrl);
        return getCloudManagementServiceApi().destroyPublishWsProxy(publishWsProxyEndpointUrl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean destroySubscribeWsProxy(String subscribeWsProxyEndpointUrl) throws GovernanceExeption {
        logger.info("Destroy Subscribe WS Proxy " + subscribeWsProxyEndpointUrl);
        return getCloudManagementServiceApi().destroySubscribeWsProxy(subscribeWsProxyEndpointUrl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean destroyPutGetWsProxy(String putgetWsProxyEndpointUrl) throws GovernanceExeption {
        logger.info("Destroy Put/Get WS Proxy " + putgetWsProxyEndpointUrl);
        return getCloudManagementServiceApi().destroyPutGetWsProxy(putgetWsProxyEndpointUrl);
    }

    public EventCloudsManagementWsApi getCloudManagementServiceApi() throws GovernanceExeption {
        try {
            return CXFHelper.getClientFromFinalURL(registry.get(Constants.DSB_TO_EC_EC),
                    EventCloudsManagementWsApi.class);
        } catch (RegistryException e) {
            throw new GovernanceExeption(e.getMessage());
        }
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

}
