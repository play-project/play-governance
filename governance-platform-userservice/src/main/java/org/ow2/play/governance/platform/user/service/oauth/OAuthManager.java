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

import java.util.List;

import org.apache.cxf.rs.security.oauth2.common.AccessTokenRegistration;
import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.common.OAuthPermission;
import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken;
import org.apache.cxf.rs.security.oauth2.common.UserSubject;
import org.apache.cxf.rs.security.oauth2.provider.OAuthDataProvider;
import org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException;
import org.apache.cxf.rs.security.oauth2.tokens.bearer.BearerAccessToken;
import org.ow2.play.governance.user.api.UserException;
import org.ow2.play.governance.user.api.UserService;
import org.ow2.play.governance.user.api.bean.User;

/**
 * @author chamerling
 * 
 */
public class OAuthManager implements OAuthDataProvider {

	private Client client;
	
	private UserService userService;
	
	/**
	 * 
	 */
	public OAuthManager() {
		client = new Client("id", "secret", false);
	}

	@Override
	public Client getClient(String clientId) throws OAuthServiceException {
		System.out.println("get client");
		return client;
	}

	@Override
	public ServerAccessToken createAccessToken(
			AccessTokenRegistration accessToken) throws OAuthServiceException {
		System.out.println("Create access token");
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServerAccessToken getAccessToken(String accessToken)
			throws OAuthServiceException {
		System.out.println("get access token " + accessToken);
		
		// check if the token is linked to a user
		UserSubject user = getUser(accessToken);
		if (user == null) {
			throw new OAuthServiceException("User not found");
		}
		ServerAccessToken token = new BearerAccessToken(this.client, 100000L);
		token.setSubject(user);
		
		// set permission to the current user
		
		return token;
	}

	@Override
	public ServerAccessToken getPreauthorizedToken(Client client,
			UserSubject subject, String grantType) throws OAuthServiceException {
		// TODO Auto-generated method stub
		System.out.println("getPreauthorizedToken");
		return null;
	}

	@Override
	public ServerAccessToken refreshAccessToken(String clientId,
			String refreshToken) throws OAuthServiceException {
		// TODO Auto-generated method stub
		System.out.println("refreshAccessToken");
		return null;
	}

	@Override
	public void removeAccessToken(ServerAccessToken accessToken)
			throws OAuthServiceException {
		System.out.println("removeAccessToken");
		throw new OAuthServiceException("Not available");
	}

	@Override
	public List<OAuthPermission> convertScopeToPermissions(Client client,
			List<String> requestedScope) {
		// TODO Auto-generated method stub
		System.out.println("convertScopeToPermissions");
		return null;
	}
	

	/**
	 * @param accessToken
	 * @return
	 */
	private UserSubject getUser(String accessToken) {
		User user = null;
		try {
			user = userService.getUserFromToken(accessToken);
		} catch (UserException e) {
			e.printStackTrace();
			return null;
		}
		if (user == null) {
			return null;
		}
		return new UserSubject(user.login);
	}
	
	/**
	 * @param userService the userService to set
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
