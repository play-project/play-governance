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
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jws.WebMethod;

import org.ow2.play.governance.bootstrap.api.LogService;


/**
 * @author chamerling
 * 
 */
public class MemoryLogServiceImpl implements LogService {

	Logger logger = Logger.getLogger(MemoryLogServiceImpl.class.getName());

	private static LogService INSTANCE;

	public static LogService get() {
		if (INSTANCE == null) {
			INSTANCE = new LogService() {

				List<String> logs = new ArrayList<String>();

				@Override
				public List<String> logs() {
					return logs;
				}

				@Override
				public void log(String message) {
					logs.add(new Date(System.currentTimeMillis()) + " : "
							+ message);
				}

				@Override
				public void log(String format, Object... values) {
					this.log(String.format(format, values));
				}

				@Override
				public void clear() {
					logs = new ArrayList<String>();
				}
			};

		}
		return INSTANCE;
	}

	public MemoryLogServiceImpl() {
	}

	@Override
	public void log(String message) {
		logger.fine(message);
		get().log(message);
	}

	@Override
	public List<String> logs() {
		logger.fine("Get logs");
		return get().logs();
	}

	@WebMethod(exclude = true)
	@Override
	public void log(String format, Object... values) {
		get().log(format, values);
	}

	@Override
	public void clear() {
		logger.fine("Clear logs");
		get().clear();
	}

}
