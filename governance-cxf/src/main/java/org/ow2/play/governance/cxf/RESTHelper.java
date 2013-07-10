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
package org.ow2.play.governance.cxf;

import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;

/**
 * Utility class to expose REST resource quickly with CXF JAXRS API
 *
 * @author Christophe Hamerling - chamerling@linagora.com
 */
public class RESTHelper {

    /**
     * Expose a resource on the given URL
     *
     * @param address
     * @param bean
     * @return
     */
    public static Service exposeResource(final String address, Object bean) {
        final JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setServiceBean(bean);
        sf.setAddress(address);

        return new Service() {
            org.apache.cxf.endpoint.Server server;

            public void stop() {
                server.stop();
            }

            public void start() {
                server = sf.create();
            }

            public String getURL() {
                return address;
            }
        };
    }
}
