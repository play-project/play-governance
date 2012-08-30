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
package org.ow2.play.governance.bootstrap.rest;

import javax.ws.rs.core.Response;

import org.ow2.play.governance.bootstrap.MemoryLogServiceImpl;
import org.ow2.play.governance.bootstrap.api.rest.LogService;


/**
 * @author chamerling
 * 
 */
public class LogServiceImpl implements LogService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.play.governance.bootstrap.api.rest.LogService#logs()
	 */
	@Override
	public Response logs() {
		return Response.ok(new Logs(MemoryLogServiceImpl.get().logs())).build();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.play.governance.bootstrap.api.rest.LogService#clear()
	 */
	@Override
	public Response clear() {
		MemoryLogServiceImpl.get().clear();
		return Response.ok(new Boolean(true)).build();
	}

}
