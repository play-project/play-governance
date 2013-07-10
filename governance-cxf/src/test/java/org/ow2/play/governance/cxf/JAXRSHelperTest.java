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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.ow2.play.governance.cxf.resources.WithPath;
import org.ow2.play.governance.cxf.resources.WithPathImpl;
import org.ow2.play.governance.cxf.resources.WithoutPath;

import static org.junit.Assert.*;

/**
 * @author Christophe Hamerling - chamerling@linagora.com
 */
@RunWith(JUnit4.class)
public class JAXRSHelperTest {

    @Test
    public void annotatedInterface() {
        assertEquals(WithPath.class, JAXRSHelper.getResourceClass(WithPath.class));
    }

    @Test
    public void annotatedClass() {
        assertEquals(WithPath.class, JAXRSHelper.getResourceClass(WithPathImpl.class));
    }

    @Test
    public void notAnnotatedClass() {
        assertNull(JAXRSHelper.getResourceClass(WithoutPath.class));
    }

    @Test
    public void interfaceHasPath() {
        assertTrue(JAXRSHelper.hasPathAnnotation(WithPath.class));
    }

    @Test
    public void classHasPath() {
        assertTrue(JAXRSHelper.hasPathAnnotation(WithPathImpl.class));
    }

    @Test
    public void classWithoutPath() {
        assertFalse(JAXRSHelper.hasPathAnnotation(WithoutPath.class));
    }

    @Test
    public void pathFromInterface() {
        assertEquals("/foo/bar", JAXRSHelper.getResourcePath(WithPath.class));
    }

    @Test
    public void noPathFromClass() {
        assertNull(JAXRSHelper.getResourcePath(WithoutPath.class));
    }

}
