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

import javax.jws.WebService;

/**
 * @author chamerling
 * 
 */
public class JAXWSHelper {

	public static final Class<?> getWebServiceClass(Class<?> cls) {
		if (cls == null) {
			return null;
		}
		if (null != cls.getAnnotation(WebService.class)) {
			return cls;
		}
		for (Class<?> inf : cls.getInterfaces()) {
			if (null != inf.getAnnotation(WebService.class)) {
				return inf;
			}
		}
		return getWebServiceClass(cls.getSuperclass());
	}

	public static final String getWebServiceName(Class<?> wsClass) {
		String serviceName = null;
		WebService anno = wsClass.getAnnotation(WebService.class);
		if ((anno.serviceName() == null)
				|| (anno.serviceName().trim().length() == 0)) {
			serviceName = wsClass.getSimpleName();
		} else {
			serviceName = anno.serviceName();
		}
		return serviceName;
	}

	public static boolean hasWebServiceAnnotation(Class<?> cls) {
		if (cls == null) {
			return false;
		}
		if (null != cls.getAnnotation(WebService.class)) {
			return true;
		}
		for (Class<?> inf : cls.getInterfaces()) {
			if (null != inf.getAnnotation(WebService.class)) {
				return true;
			}
		}
		return hasWebServiceAnnotation(cls.getSuperclass());
	}

}
