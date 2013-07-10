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

import junit.framework.Assert;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.ow2.play.governance.cxf.resources.WithPath;
import org.ow2.play.governance.cxf.resources.WithPathImpl;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Christophe Hamerling - chamerling@linagora.com
 */
@RunWith(value = JUnit4.class)
public class RESTHelperTest {

    @Test
    public void testExposeAndCall() {
        final AtomicInteger count = new AtomicInteger(0);
        final String address = "http://localhost:9323/foo/bar";
        Service s = RESTHelper.exposeResource(address, new WithPathImpl(count));
        s.start();
        WithPath client = JAXRSClientFactory.create(address, WithPath.class);
        client.hello();
        Assert.assertEquals(1, count.get());
        s.stop();
    }
}
