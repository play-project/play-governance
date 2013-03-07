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
package org.ow2.play.governance.platform.user.service.oauth;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.rs.security.oauth2.common.OAuthContext;

/**
 * Temp, will need to be removed on newer CXF version support (2.7.1) cf
 * http://svn
 * .apache.org/repos/asf/cxf/trunk/rt/rs/security/oauth-parent/oauth2/src
 * /main/java/org/apache/cxf/rs/security/oauth2/utils/OAuthContextUtils.java
 * 
 * @author chamerling
 * 
 */
public class OAuthHelper {

	public static OAuthContext getContext(final MessageContext mc) {
		final OAuthContext oauth = mc.getContent(OAuthContext.class);
		if ((oauth == null) || (oauth.getSubject() == null)
				|| (oauth.getSubject().getLogin() == null)) {
			throw new WebApplicationException(Response.status(401).build());
		}
		return oauth;
	}

	public static String resolveUserName(final MessageContext mc) {
		final OAuthContext oauth = getContext(mc);
		return oauth.getSubject().getLogin();
	}

}
