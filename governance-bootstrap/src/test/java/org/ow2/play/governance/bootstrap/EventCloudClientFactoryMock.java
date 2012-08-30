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

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;

import org.oasis_open.docs.wsn.b_2.GetCurrentMessage;
import org.oasis_open.docs.wsn.b_2.GetCurrentMessageResponse;
import org.oasis_open.docs.wsn.b_2.Renew;
import org.oasis_open.docs.wsn.b_2.RenewResponse;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;
import org.oasis_open.docs.wsn.bw_2.InvalidFilterFault;
import org.oasis_open.docs.wsn.bw_2.InvalidMessageContentExpressionFault;
import org.oasis_open.docs.wsn.bw_2.InvalidProducerPropertiesExpressionFault;
import org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault;
import org.oasis_open.docs.wsn.bw_2.MultipleTopicsSpecifiedFault;
import org.oasis_open.docs.wsn.bw_2.NoCurrentMessageOnTopicFault;
import org.oasis_open.docs.wsn.bw_2.NotifyMessageNotSupportedFault;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.oasis_open.docs.wsn.bw_2.TopicExpressionDialectUnknownFault;
import org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault;
import org.oasis_open.docs.wsn.bw_2.UnableToDestroySubscriptionFault;
import org.oasis_open.docs.wsn.bw_2.UnacceptableInitialTerminationTimeFault;
import org.oasis_open.docs.wsn.bw_2.UnacceptableTerminationTimeFault;
import org.oasis_open.docs.wsn.bw_2.UnrecognizedPolicyRequestFault;
import org.oasis_open.docs.wsn.bw_2.UnsupportedPolicyRequestFault;
import org.oasis_open.docs.wsrf.rw_2.ResourceUnknownFault;
import org.ow2.play.governance.bootstrap.api.EventCloudClientFactory;

import fr.inria.eventcloud.webservices.api.EventCloudManagementServiceApi;

/**
 * @author chamerling
 * 
 */
public class EventCloudClientFactoryMock implements EventCloudClientFactory {

    Map<String, EventCloudManagementServiceApi> services =
            new HashMap<String, EventCloudManagementServiceApi>();

    String eventCloudEndpointPrefix;

    public EventCloudClientFactoryMock(String eventCloudEndpointPrefix) {
        this.eventCloudEndpointPrefix = eventCloudEndpointPrefix;
    }

    @Override
    public EventCloudManagementServiceApi getClient(String endpoint) {

        if (services.get(endpoint) == null) {
            createService(endpoint);
        }
        return services.get(endpoint);
    }

    private final void createService(String endpoint) {
        services.put(endpoint, new EventCloudManagementServiceApi() {

            @Override
            public boolean createEventCloud(String arg0) {
                System.out.println("Create event cloud operation");
                return true;
            }

            @Override
            public String createPublishProxy(String arg0) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String createPutGetProxy(String arg0) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String createSubscribeProxy(String arg0) {
                System.out.println("Create subscribe Proxy operation " + arg0);
                return eventCloudEndpointPrefix + "SubscribePoxy";
            }

            @Override
            public boolean destroyEventCloud(String arg0) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean destroyPublishProxy(String arg0) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean destroyPutGetProxy(String arg0) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean destroySubscribeProxy(String arg0) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public List<String> getEventCloudIds() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public List<String> getPublishProxyEndpointUrls(String arg0) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public List<String> getPutgetProxyEndpointUrls(String arg0) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getRegistryEndpointUrl() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public List<String> getSubscribeProxyEndpointUrls(String arg0) {
                List<String> result = new ArrayList<String>();
                result.add(eventCloudEndpointPrefix + "SubscribePoxy");
                System.out.println("Get subscribe proxy " + result);
                return result;
            }

            @Override
            @WebMethod
            public boolean isCreated(@WebParam(name = "streamUrl") String arg0) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            @WebResult(name = "GetCurrentMessageResponse", targetNamespace = "http://docs.oasis-open.org/wsn/b-2", partName = "GetCurrentMessageResponse")
            @WebMethod(operationName = "GetCurrentMessage")
            public GetCurrentMessageResponse getCurrentMessage(@WebParam(partName = "GetCurrentMessageRequest", name = "GetCurrentMessage", targetNamespace = "http://docs.oasis-open.org/wsn/b-2") GetCurrentMessage arg0)
                    throws NoCurrentMessageOnTopicFault,
                    TopicNotSupportedFault, ResourceUnknownFault,
                    MultipleTopicsSpecifiedFault,
                    TopicExpressionDialectUnknownFault,
                    InvalidTopicExpressionFault {
                return null;
            }

            @Override
            @WebResult(name = "SubscribeResponse", targetNamespace = "http://docs.oasis-open.org/wsn/b-2", partName = "SubscribeResponse")
            @WebMethod(operationName = "Subscribe")
            public SubscribeResponse subscribe(@WebParam(partName = "SubscribeRequest", name = "Subscribe", targetNamespace = "http://docs.oasis-open.org/wsn/b-2") Subscribe arg0)
                    throws UnrecognizedPolicyRequestFault,
                    SubscribeCreationFailedFault,
                    InvalidProducerPropertiesExpressionFault,
                    UnsupportedPolicyRequestFault, TopicNotSupportedFault,
                    NotifyMessageNotSupportedFault, ResourceUnknownFault,
                    UnacceptableInitialTerminationTimeFault,
                    InvalidMessageContentExpressionFault, InvalidFilterFault,
                    TopicExpressionDialectUnknownFault,
                    InvalidTopicExpressionFault {
                return null;
            }

            @Override
            @WebResult(name = "RenewResponse", targetNamespace = "http://docs.oasis-open.org/wsn/b-2", partName = "RenewResponse")
            @WebMethod(operationName = "Renew")
            public RenewResponse renew(@WebParam(partName = "RenewRequest", name = "Renew", targetNamespace = "http://docs.oasis-open.org/wsn/b-2") Renew arg0)
                    throws UnacceptableTerminationTimeFault,
                    ResourceUnknownFault {
                return null;
            }

            @Override
            @WebResult(name = "UnsubscribeResponse", targetNamespace = "http://docs.oasis-open.org/wsn/b-2", partName = "UnsubscribeResponse")
            @WebMethod(operationName = "Unsubscribe")
            public UnsubscribeResponse unsubscribe(@WebParam(partName = "UnsubscribeRequest", name = "Unsubscribe", targetNamespace = "http://docs.oasis-open.org/wsn/b-2") Unsubscribe arg0)
                    throws UnableToDestroySubscriptionFault,
                    ResourceUnknownFault {
                return null;
            }

        });
    }

}
