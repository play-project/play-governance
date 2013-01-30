/**
 * 
 */
package org.ow2.play.governance.user.api;

/**
 * An external authentication provider. This is used to login using external
 * oauth providers such as twitter, google, github, facebook....
 * 
 * @author chamerling
 * 
 */
public class Account {

	/**
	 * The account provider (twitter, facebook, ...). We support only one auth
	 * type per provider. This is up to the provider implementation to deal with
	 * the right protocol.
	 */
	public String provider;

	/**
	 * The account id
	 */
	public String login;

	/**
	 * The OAuth1 token (available when authMethod is OAUTH1 or
	 * OPENID_OAUTH_HYBRID)
	 */
	public String token;

	/**
	 * The OAuth1 secret (available when authMethod is OAUTH1 or
	 * OPENID_OAUTH_HYBRID)
	 */
	public String secret;

	/**
	 * The OAuth2 access token (available when authMethod is OAUTH2)
	 */
	public String accessToken;

	public String fullName;

	public String email;

	public String avatarURL;

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((accessToken == null) ? 0 : accessToken.hashCode());
		result = prime * result
				+ ((avatarURL == null) ? 0 : avatarURL.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((fullName == null) ? 0 : fullName.hashCode());
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result
				+ ((provider == null) ? 0 : provider.hashCode());
		result = prime * result + ((secret == null) ? 0 : secret.hashCode());
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (accessToken == null) {
			if (other.accessToken != null)
				return false;
		} else if (!accessToken.equals(other.accessToken))
			return false;
		if (avatarURL == null) {
			if (other.avatarURL != null)
				return false;
		} else if (!avatarURL.equals(other.avatarURL))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!fullName.equals(other.fullName))
			return false;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		if (provider == null) {
			if (other.provider != null)
				return false;
		} else if (!provider.equals(other.provider))
			return false;
		if (secret == null) {
			if (other.secret != null)
				return false;
		} else if (!secret.equals(other.secret))
			return false;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;
	}

}
