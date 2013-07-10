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

import javax.ws.rs.Path;

/**
 * @author chamerling
 * 
 */
public class JAXRSHelper {

    /**
     * Get resource class if Path annotation is defined in the class or interfaces
     *
     * @param cls
     * @return
     */
	public static final Class<?> getResourceClass(Class<?> cls) {
		if (cls == null) {
			return null;
		}
		if (null != cls.getAnnotation(Path.class)) {
			return cls;
		}

        // returns the first one...
		for (Class<?> inf : cls.getInterfaces()) {
			if (null != inf.getAnnotation(Path.class)) {
				return inf;
			}
		}
		return getResourceClass(cls.getSuperclass());
	}

    /**
     * Get resource path from Path annotation value. Returns null if not found in class or interface.
     *
     * @param resourceClass
     * @return
     */
	public static final String getResourcePath(Class<?> resourceClass) {
		if (!hasPathAnnotation(resourceClass)) {
            return null;
        }

        String result = null;

		Path path = resourceClass.getAnnotation(Path.class);
        if (path == null) {
            return null;
        }

		if ((path.value() == null)
				|| (path.value().trim().length() == 0)) {
			result = resourceClass.getSimpleName();
		} else {
			result = path.value();
		}
		return result;
	}

    /**
     * Check if the given class or interface has a Path annotation
     *
     * @param cls
     * @return
     */
	public static boolean hasPathAnnotation(Class<?> cls) {
		if (cls == null) {
			return false;
		}
		if (null != cls.getAnnotation(Path.class)) {
			return true;
		}
		for (Class<?> inf : cls.getInterfaces()) {
			if (null != inf.getAnnotation(Path.class)) {
				return true;
			}
		}
		return hasPathAnnotation(cls.getSuperclass());
	}
}
