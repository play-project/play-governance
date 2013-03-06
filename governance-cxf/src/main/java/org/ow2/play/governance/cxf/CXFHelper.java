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

import org.apache.cxf.jaxb.JAXBDataBinding;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;

/**
 * @author chamerling
 * 
 */
public class CXFHelper {

	/**
	 * Get a client for the given class
	 * 
	 * @param <T>
	 * @param baseURL
	 * @param clazz
	 * @return
	 */
	public static <T> T getClient(String baseURL, Class<T> clazz) {
		String address = baseURL;
		if (!address.endsWith("/")) {
			address = address + "/";
		}
		address = address + JAXWSHelper.getWebServiceName(clazz);
		return getClientFromFinalURL(address, clazz);
	}

	/**
	 * Get a client instance for a given URL. The URL is a final one and is not
	 * built from the JAXWS annotations like in
	 * {@link #getClient(String, Class)}
	 * 
	 * @param <T>
	 * @param fullURL
	 * @param clazz
	 * @return
	 */
	public static <T> T getClientFromFinalURL(String fullURL, Class<T> clazz) {
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setAddress(fullURL);
		factory.setServiceClass(clazz);
		Object client = factory.create();
		return clazz.cast(client);
	}

	public static <T> Service getService(String baseURL, Class<T> clazz,
			Object bean) {
		Class<?> wsClass = JAXWSHelper.getWebServiceClass(clazz);
		String serviceName = JAXWSHelper.getWebServiceName(wsClass);
		if (serviceName == null) {
			serviceName = clazz.getSimpleName();
		}
		String address = baseURL;
		if (!address.endsWith("/")) {
			address = address + "/";
		}
		address = address + serviceName;
		return getServiceFromFinalURL(address, clazz, bean);
	}

	public static <T> Service getServiceFromFinalURL(final String finalURL,
			Class<T> clazz, Object bean) {
		final JaxWsServerFactoryBean sf = new JaxWsServerFactoryBean();
		sf.setDataBinding(new JAXBDataBinding());
		sf.setServiceBean(bean);
		Class<?> wsClass = JAXWSHelper.getWebServiceClass(clazz);

		sf.setAddress(finalURL);
		sf.setServiceClass(wsClass);

		return new Service() {
			org.apache.cxf.endpoint.Server server;

			public void stop() {
				server.stop();
			}

			public void start() {
				server = sf.create();
			}

			public String getURL() {
				return finalURL;
			}
		};
	}
}