/**
 * 
 */
package org.ow2.play.governance.user.api.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * User bean
 * 
 * @author chamerling
 * 
 */
@XmlRootElement(name = "user")
public class User {
	
	public String id;

	/**
	 * The user id in the Play platform
	 */
	public String login;

	public String fullName;

	/**
	 * If any...
	 */
	public String password;

	public String email;

	public String avatarURL;
	
	/**
	 * The API token while waiting for a OAuth provider
	 */
	public String apiToken;
	
	/**
	 * Providers accounts for the user
	 */
	public List<Account> accounts = new ArrayList<Account>();

	/**
	 * List of groupIds the user belongs to
	 */
	public List<String> groups = new ArrayList<String>();

	public User() {
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((accounts == null) ? 0 : accounts.hashCode());
		result = prime * result
				+ ((avatarURL == null) ? 0 : avatarURL.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((fullName == null) ? 0 : fullName.hashCode());
		result = prime * result + ((groups == null) ? 0 : groups.hashCode());
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
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
		User other = (User) obj;
		if (accounts == null) {
			if (other.accounts != null)
				return false;
		} else if (!accounts.equals(other.accounts))
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
		if (groups == null) {
			if (other.groups != null)
				return false;
		} else if (!groups.equals(other.groups))
			return false;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		return true;
	}
}
