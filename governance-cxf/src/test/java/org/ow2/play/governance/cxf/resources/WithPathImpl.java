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
package org.ow2.play.governance.cxf.resources;

import javax.ws.rs.core.Response;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Christophe Hamerling - chamerling@linagora.com
 */
public class WithPathImpl implements WithPath {

    private final AtomicInteger count;

    public WithPathImpl(AtomicInteger count) {
        this.count = count;
    }

    @Override
    public Response hello() {
        System.out.println("Called");
        this.count.incrementAndGet();
        return Response.ok().build();
    }
}