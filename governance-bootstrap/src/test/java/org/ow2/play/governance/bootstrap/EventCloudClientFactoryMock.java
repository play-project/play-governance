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
package org.ow2.play.governance.bootstrap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ow2.play.governance.bootstrap.api.EventCloudClientFactory;

import fr.inria.eventcloud.webservices.api.EventCloudsManagementWsApi;


/**
 * @author chamerling
 * 
 */
public class EventCloudClientFactoryMock implements EventCloudClientFactory {

    Map<String, EventCloudsManagementWsApi> services = new HashMap<String, EventCloudsManagementWsApi>();

    String eventCloudEndpointPrefix;

    public EventCloudClientFactoryMock(String eventCloudEndpointPrefix) {
        this.eventCloudEndpointPrefix = eventCloudEndpointPrefix;
    }

    @Override
    public EventCloudsManagementWsApi getClient(String endpoint) {

        if (services.get(endpoint) == null) {
            createService(endpoint);
        }
        return services.get(endpoint);
    }

    private final void createService(String endpoint) {
        services.put(endpoint, new EventCloudsManagementWsApi() {

            @Override
            public String getRegistryEndpointUrl() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public boolean createEventCloud(String streamUrl) {
                System.out.println("Create event cloud operation");
                return true;
            }

            @Override
            public boolean isCreated(String streamUrl) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public List<String> getEventCloudIds() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public boolean destroyEventCloud(String streamUrl) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public String deployPublishWsnService(String streamUrl) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String deploySubscribeWsnService(String streamUrl) {
                System.out.println("Deploy subscribe WSN Proxy operation " + streamUrl);
                return eventCloudEndpointPrefix + "SubscribePoxy";
            }

            @Override
            public String deployPublishWsProxy(String streamUrl) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String deploySubscribeWsProxy(String streamUrl) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String deployPutGetWsProxy(String streamUrl) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public List<String> getPublishWsnServiceEndpointUrls(String streamUrl) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public List<String> getSubscribeWsnServiceEndpointUrls(String streamUrl) {
                List<String> result = new ArrayList<String>();
                result.add(eventCloudEndpointPrefix + "SubscribePoxy");
                System.out.println("Get subscribe WSN proxy " + result);
                return result;
            }

            @Override
            public List<String> getPublishWsProxyEndpointUrls(String streamUrl) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public List<String> getSubscribeWsProxyEndpointUrls(String streamUrl) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public List<String> getPutGetWsProxyEndpointUrls(String streamUrl) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public boolean destroyPublishWsnService(String publishWsnEndpointUrl) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean destroySubscribeWsnService(String subscribeWsnEndpointUrl) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean destroyPublishWsProxy(String publishWsProxyEndpointUrl) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean destroySubscribeWsProxy(String subscribeWsProxyEndpointUrl) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean destroyPutGetWsProxy(String putgetWsProxyEndpointUrl) {
                // TODO Auto-generated method stub
                return false;
            }

        });
    }

}
