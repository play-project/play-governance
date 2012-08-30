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
package org.ow2.play.governance.bootstrap.api.rest.beans;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.ow2.play.governance.api.bean.Subscription;

/**
 * Used to get subscriptions list for REST services without any additional
 * factory, writer...
 * 
 * @author chamerling
 * 
 */
@XmlRootElement(name = "subscriptions")
public class Subscriptions {
	@XmlElement(name = "subscription")
	private List<Subscription> s;

	public Subscriptions() {
	}

	public Subscriptions(List<Subscription> s) {
		this.s = s;
	}

	public List<Subscription> get() {
		return s;
	}

	public void set(List<Subscription> c) {
		this.s = c;
	}
}
